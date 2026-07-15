import argparse
import sys
import os
import json
import easyocr

# https://www.jaided.ai/easyocr/documentation/
def parse_and_output(file_path, output_path=None):
    try:
        reader = easyocr.Reader(lang_list = ['ch_sim','en'])
        str_list = reader.readtext(file_path, detail = 0, paragraph =True)
		
		# to JSON string
        content = json.dumps(str_list, ensure_ascii=False) 
        
        # save output
        if output_path:
            with open(output_path, 'w', encoding='utf-8') as f:
                f.write(content)
            print(f"result save to file: {output_path}")
        else:
            print("<ocr_output>")
            print(content)
            print("</ocr_output>")
    except Exception as e:
        print("error, please install easyocr 'pip install easyocr'", file=sys.stderr)
        sys.exit(1)

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="OCR base on EasyOCR")
    parser.add_argument("input", help="input image path")
    parser.add_argument("-o", "--output", help="output json file path (optional).", default=None)
    args = parser.parse_args()
    parse_and_output(args.input, args.output)