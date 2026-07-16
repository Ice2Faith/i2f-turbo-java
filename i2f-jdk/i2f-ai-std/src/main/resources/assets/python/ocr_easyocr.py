# -*- coding: utf-8 -*-

import argparse
import sys
import os
import json
import easyocr

def get_reader():
    print('new reader work')
    return easyocr.Reader(lang_list = ['ch_sim','en'])

# https://www.jaided.ai/easyocr/documentation/
def parse_and_output(file_path, output_path=None,std_output=True,reader =None):
    try:
        if reader == None:
            reader = get_reader()
        str_list = reader.readtext(file_path, detail = 0, paragraph =True)
        
        # to JSON string
        content = json.dumps(str_list, ensure_ascii=False) 
        
        # save output
        if output_path:
            with open(output_path, 'w', encoding='utf-8') as f:
                f.write(content)
            print(f"result save to file: {output_path}")
        elif std_output:
            print("<ocr_output>")
            print(content)
            print("</ocr_output>")
        return str_list
    except Exception as e:
        print(f"error, please install easyocr 'pip install easyocr', {e}", file=sys.stderr)
        sys.exit(1)

def process_file_list(list_path,output_path=None):
    """读取文件列表并循环处理"""
    try:
        reader=get_reader()
        result_list=[]
        with open(list_path, 'r', encoding='utf-8') as f:
            # 过滤掉空行，并去除首尾空格/换行符
            image_paths = [line.strip() for line in f if line.strip()]
            
        if not image_paths:
            print("warning: file list is empty.", file=sys.stderr)
            return

        # 循环处理每一张图片
        for img_path in image_paths:
            print(f"processing: {img_path}")
            # 批量模式下，输出直接打印到控制台
            next_list=parse_and_output(img_path, output_path=None,std_output=False,reader=reader)
            if True:
                parsed_file=img_path+".parsed.txt"
                with open(parsed_file, 'w', encoding='utf-8') as f:
                    content = json.dumps(next_list, ensure_ascii=False) 
                    f.write(content)
                print(f"parsed save to file: {parsed_file}")
            next_map={"input": img_path,"output": next_list}
            result_list.append(next_map)
        # to JSON string
        content = json.dumps(result_list, ensure_ascii=False) 
        
        # save output
        if output_path:
            with open(output_path, 'w', encoding='utf-8') as f:
                f.write(content)
            print(f"result save to file: {output_path}")
        else:
            print("<ocr_output>")
            print(content)
            print("</ocr_output>")
        return result_list
    except Exception as e:
        print(f"error reading file list {list_path}: {e}", file=sys.stderr)
        sys.exit(1)

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="OCR base on EasyOCR")
    parser.add_argument("input", nargs='?', help="input image path", default=None)
    parser.add_argument("-o", "--output", help="output json file path (optional).", default=None)
    parser.add_argument("-f", "--file-list", help="input a txt file list, each line is an image path.", default=None)
    
    args = parser.parse_args()

    # 如果传入了 -f 参数，则进入文件列表处理模式
    if args.file_list:
        process_file_list(args.file_list, args.output)
    # 否则，走原有的单张图片处理逻辑
    elif args.input:
        parse_and_output(args.input, output_path=args.output,std_output=True,reader=None)
    else:
        # 如果什么都没传，打印帮助信息
        parser.print_help()
        sys.exit(1)