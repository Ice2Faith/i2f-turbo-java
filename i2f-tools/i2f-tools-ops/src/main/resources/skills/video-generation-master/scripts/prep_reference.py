#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""prep_reference.py -- 参考图预处理：让视频模型在某个部位"没得参考"。

用途（视频 image-to-video 场景）：
    当用户明确要求"不要沿用参考图的面部 / 鞋子 / 腿部服饰 / 头饰"，
    仅靠提示词里写【禁止】往往压不住（i2v 模型的参考图权重高于文字）。
    本脚本把对应区域裁掉、或用重模糊/马赛克/纯色盖掉，
    模型看不到细节，就不会把细节迁移过去。

两种模式：
    crop  裁切：只保留需要的矩形区域。适合"只想要衣服+姿态，不要头也不要脸"。
    mask  遮挡：保留整图构图与比例，只把指定区域糊掉。适合"构图要保留、细节不能抄"。

区域描述：
    --preset face,shoes        用内置预设（归一化坐标，适配竖屏全身人像）
    --rect 0.24,0.02,0.76,0.20 自定义，归一化坐标 x1,y1,x2,y2（可多次给）
    --expand 0.02              每个矩形各向外扩 2% 画幅（宁可多糊一点，别留边）

示例：
    # 1) 不要参考面部：整图保留，脸糊掉
    python prep_reference.py -i ref.png -o ref_noface.png --preset face --fill blur

    # 2) 不要参考鞋子：脚部糊掉
    python prep_reference.py -i ref.png -o ref_noshoe.png --preset shoes

    # 3) 只要衣服和姿态，头和脚都裁掉（crop 保留最大内接框）
    python prep_reference.py -i ref.png -o ref_torso.png --mode crop --rect 0.10,0.18,0.90,0.85

    # 4) 只想确认框位置对不对，不产出成品
    python prep_reference.py -i ref.png -o /tmp/x.png --preset face,shoes --preview-only

注意：
    * 坐标为归一化值（0~1），x 向右、y 向下，左上角为原点。
    * 内置预设是按"竖屏全身人像、人物大致居中"标定的，人物偏离中心时要改用 --rect。
    * 输出为 PNG（无损），并会打印每个框的像素坐标，便于肉眼复核。
"""

import argparse
import os
import sys

import numpy as np

try:
    import cv2
except ImportError:  # pragma: no cover
    sys.exit("ERROR: opencv-python not found. Install it with: pip install opencv-python")


# 归一化预设：一律写成"横向整条带"（x1=0, x2=1）。
# 原因（实测过）：人物在画面里的水平位置不固定，用紧框经常一边漏掉半张脸、
# 或者把下巴留在框外，漏了就等于白做；整条带宁可多糊一点背景，也不会漏。
# 需要精确控制时用 --rect 自己给框。
PRESETS = {
    "head": (0.00, 0.06, 1.00, 0.40),
    "face": (0.00, 0.10, 1.00, 0.38),
    "hair": (0.00, 0.04, 1.00, 0.40),
    "torso": (0.00, 0.34, 1.00, 0.64),
    "skirt": (0.00, 0.48, 1.00, 0.78),
    "legs": (0.00, 0.55, 1.00, 0.97),
    "legwear": (0.00, 0.62, 1.00, 0.97),
    "shoes": (0.00, 0.84, 1.00, 1.00),
}


def imread_unicode(path):
    """OpenCV 的 imread 在 Windows 上读不了中文路径，用 imdecode 兜底。"""
    if not os.path.isfile(path):
        sys.exit("ERROR: input not found: %s" % path)
    buf = np.fromfile(path, dtype=np.uint8)
    img = cv2.imdecode(buf, cv2.IMREAD_COLOR)
    if img is None:
        sys.exit("ERROR: cannot decode image: %s" % path)
    return img


def imwrite_unicode(path, img):
    ext = os.path.splitext(path)[1] or ".png"
    ok, buf = cv2.imencode(ext, img)
    if not ok:
        sys.exit("ERROR: cannot encode image: %s" % path)
    d = os.path.dirname(os.path.abspath(path))
    if d and not os.path.isdir(d):
        os.makedirs(d, exist_ok=True)
    buf.tofile(path)


def parse_rect(text):
    parts = [p.strip() for p in text.split(",")]
    if len(parts) != 4:
        sys.exit("ERROR: --rect needs 4 comma separated numbers, got: %s" % text)
    try:
        vals = [float(p) for p in parts]
    except ValueError:
        sys.exit("ERROR: --rect accepts numbers only, got: %s" % text)
    x1, y1, x2, y2 = vals
    if not (0.0 <= x1 < x2 <= 1.0 and 0.0 <= y1 < y2 <= 1.0):
        sys.exit("ERROR: --rect must satisfy 0<=x1<x2<=1 and 0<=y1<y2<=1, got: %s" % text)
    return (x1, y1, x2, y2)


def to_pixels(rect, w, h, expand):
    x1, y1, x2, y2 = rect
    x1 = max(0.0, x1 - expand)
    y1 = max(0.0, y1 - expand)
    x2 = min(1.0, x2 + expand)
    y2 = min(1.0, y2 + expand)
    return (int(round(x1 * w)), int(round(y1 * h)), int(round(x2 * w)), int(round(y2 * h)))


def fill_region(img, boxes, style, feather_ratio=0.006):
    """按框生成软边蒙版，把框内替换为"无细节"的图。

    软边（feather）很重要：硬边的矩形会让视频模型在边缘处画出诡异接缝，
    羽化几个像素后模型会把它当成自然的虚焦过渡。
    """
    h, w = img.shape[:2]
    mask = np.zeros((h, w), np.uint8)
    for (x1, y1, x2, y2) in boxes:
        cv2.rectangle(mask, (x1, y1), (x2, y2), 255, -1)
    if feather_ratio > 0:
        sigma = max(1.0, feather_ratio * min(w, h))
        mask = cv2.GaussianBlur(mask, (0, 0), sigma)

    if style == "blur":
        # 整图先压到 1/20 再放大：细节全丢，色块与明暗关系保留
        small = cv2.resize(img, (max(1, w // 20), max(1, h // 20)), interpolation=cv2.INTER_AREA)
        proc = cv2.resize(small, (w, h), interpolation=cv2.INTER_LINEAR)
    elif style == "mosaic":
        small = cv2.resize(img, (max(1, w // 64), max(1, h // 64)), interpolation=cv2.INTER_AREA)
        proc = cv2.resize(small, (w, h), interpolation=cv2.INTER_NEAREST)
    elif style == "solid":
        proc = img.copy()
        for (x1, y1, x2, y2) in boxes:
            roi = img[y1:y2, x1:x2]
            if roi.size:
                proc[y1:y2, x1:x2] = roi.reshape(-1, 3).mean(axis=0).astype(np.uint8)
    elif style == "gray":
        proc = np.full_like(img, 128)
    else:
        sys.exit("ERROR: unknown --fill: %s" % style)

    a = (mask.astype(np.float32) / 255.0)[..., None]
    return (img.astype(np.float32) * (1.0 - a) + proc.astype(np.float32) * a).astype(np.uint8)


def draw_preview(img, boxes):
    prev = img.copy()
    for (x1, y1, x2, y2) in boxes:
        cv2.rectangle(prev, (x1, y1), (x2, y2), (0, 0, 255), max(2, img.shape[1] // 400))
    return prev


def crop_to(img, boxes):
    """crop 模式：取所有框的并集，再在其中取最大内接区域（这里直接用并集框）。"""
    h, w = img.shape[:2]
    x1 = min(b[0] for b in boxes)
    y1 = min(b[1] for b in boxes)
    x2 = max(b[2] for b in boxes)
    y2 = max(b[3] for b in boxes)
    # 至少留 64px，避免裁出一个点
    if x2 - x1 < 64 or y2 - y1 < 64:
        sys.exit("ERROR: crop region too small: %s" % ((x1, y1, x2, y2),))
    return img[y1:y2, x1:x2]


def main(argv=None):
    ap = argparse.ArgumentParser(description="Prepare a reference image for image-to-video generation.")
    ap.add_argument("-i", "--input", required=True, help="source image")
    ap.add_argument("-o", "--output", required=True, help="output image (png recommended)")
    ap.add_argument("--mode", choices=["mask", "crop"], default="mask",
                    help="mask=keep composition, hide region; crop=keep only the region")
    ap.add_argument("--preset", default="", help="comma separated presets, e.g. face,shoes")
    ap.add_argument("--rect", action="append", default=[],
                    help="normalized rect x1,y1,x2,y2 (repeatable)")
    ap.add_argument("--expand", type=float, default=0.0, help="expand each rect by this ratio")
    ap.add_argument("--fill", choices=["blur", "mosaic", "solid", "gray"], default="blur",
                    help="how to hide the region in mask mode")
    ap.add_argument("--feather", type=float, default=0.006,
                    help="soft edge ratio of the mask (0 disables). default 0.006")
    ap.add_argument("--preview", default="", help="also write a preview with red boxes")
    ap.add_argument("--preview-only", action="store_true", help="only write the preview, skip output")
    args = ap.parse_args(argv)

    rects = []
    if args.preset:
        for name in [p.strip() for p in args.preset.split(",") if p.strip()]:
            if name not in PRESETS:
                sys.exit("ERROR: unknown preset '%s'. Known: %s" % (name, ", ".join(sorted(PRESETS))))
            rects.append(PRESETS[name])
    for r in args.rect:
        rects.append(parse_rect(r))
    if not rects:
        sys.exit("ERROR: give at least one --preset or --rect")

    img = imread_unicode(args.input)
    h, w = img.shape[:2]
    boxes = [to_pixels(r, w, h, args.expand) for r in rects]

    print("input      : %s (%dx%d)" % (args.input, w, h))
    print("mode       : %s" % args.mode)
    for i, (r, b) in enumerate(zip(rects, boxes), 1):
        print("  box %d    : norm(%.3f,%.3f,%.3f,%.3f) -> px(%d,%d,%d,%d)"
              % (i, r[0], r[1], r[2], r[3], b[0], b[1], b[2], b[3]))

    if args.preview:
        imwrite_unicode(args.preview, draw_preview(img, boxes))
        print("preview    : %s" % args.preview)

    if args.preview_only:
        print("done       : preview only, no output written")
        return 0

    if args.mode == "crop":
        out = crop_to(img, boxes)
        print("crop rect  : (%d,%d)-(%d,%d) -> %dx%d"
              % (boxes[0][0], boxes[0][1], boxes[0][2], boxes[0][3], out.shape[1], out.shape[0]))
    else:
        out = fill_region(img, boxes, args.fill, args.feather)
        print("fill       : %s (feather %.4f)" % (args.fill, args.feather))

    imwrite_unicode(args.output, out)
    print("output     : %s (%dx%d)" % (args.output, out.shape[1], out.shape[0]))
    print("done")
    return 0


if __name__ == "__main__":
    sys.exit(main())