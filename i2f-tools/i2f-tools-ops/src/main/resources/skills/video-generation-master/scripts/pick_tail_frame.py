#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""pick_tail_frame.py -- 从视频末尾采几帧，挑最清晰的一帧作为"续写首帧"。

用途（长视频末帧续写）：
    10 秒一段地生成，然后把上一段的"最后一帧"当作下一段的参考图/首帧，
    接着往下写。但直接取最后一帧经常是模糊的（运镜未停、运动模糊、编码拖影），
    拿它当首帧会把"糊"传染给下一段。所以要在最后几秒里多采几帧，挑最清晰的。

"清晰"用拉普拉斯方差（Laplacian variance）衡量：数值越高边缘越锐利。
同时排除过暗/过曝的帧（纯黑收尾、闪白转场）。

示例：
    # 在最后 3 秒里按 4fps 采样，取最清晰的 1 帧
    python pick_tail_frame.py -i part1.mp4 -o tail.png --seconds 3 --fps 4

    # 只看打分，不落盘（先确认哪帧最好）
    python pick_tail_frame.py -i part1.mp4 --list --seconds 3 --fps 6

    # 同时导出前 3 名，人工再挑
    python pick_tail_frame.py -i part1.mp4 -o tail.png --top 3
"""

import argparse
import os
import shutil
import sys
import tempfile

import numpy as np

try:
    import cv2
except ImportError:  # pragma: no cover
    sys.exit("ERROR: opencv-python not found. Install it with: pip install opencv-python")


def imwrite_unicode(path, img):
    ext = os.path.splitext(path)[1] or ".png"
    ok, buf = cv2.imencode(ext, img)
    if not ok:
        sys.exit("ERROR: cannot encode image: %s" % path)
    d = os.path.dirname(os.path.abspath(path))
    if d and not os.path.isdir(d):
        os.makedirs(d, exist_ok=True)
    buf.tofile(path)


def open_video(path):
    """cv2.VideoCapture 在 Windows 上读不了中文路径，非 ASCII 路径先复制到临时目录。"""
    if not os.path.isfile(path):
        sys.exit("ERROR: input not found: %s" % path)
    if path.isascii():
        cap = cv2.VideoCapture(path)
        if not cap.isOpened():
            sys.exit("ERROR: cannot open video: %s" % path)
        return cap, None

    tmpdir = tempfile.mkdtemp(prefix="picktail_")
    tmp = os.path.join(tmpdir, "in.mp4")
    shutil.copy2(path, tmp)
    cap = cv2.VideoCapture(tmp)
    if not cap.isOpened():
        sys.exit("ERROR: cannot open video: %s" % path)
    return cap, tmpdir


def score(frame):
    """越大越清晰；同时惩罚过暗/过曝。"""
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    sharp = cv2.Laplacian(gray, cv2.CV_64F).var()
    mean = float(gray.mean())
    if mean < 25 or mean > 235:          # 纯黑收尾 / 闪白转场
        return 0.0, sharp, mean
    return float(sharp), float(sharp), mean


def main(argv=None):
    ap = argparse.ArgumentParser(description="Pick the sharpest frame in the tail of a video.")
    ap.add_argument("-i", "--input", required=True, help="source video")
    ap.add_argument("-o", "--output", default="", help="output png (default tail.png)")
    ap.add_argument("--seconds", type=float, default=3.0, help="tail window in seconds (default 3)")
    ap.add_argument("--fps", type=float, default=4.0, help="sampling rate inside the window (default 4)")
    ap.add_argument("--top", type=int, default=1, help="export top N sharpest frames (default 1)")
    ap.add_argument("--list", action="store_true", help="only print scores, write nothing")
    args = ap.parse_args(argv)

    cap, tmpdir = open_video(args.input)
    try:
        total = int(cap.get(cv2.CAP_PROP_FRAME_COUNT) or 0)
        fps = float(cap.get(cv2.CAP_PROP_FPS) or 24.0)
        if total <= 0:
            sys.exit("ERROR: cannot read frame count (is this a video?)")

        start = max(0, int(total - args.seconds * fps))
        step = max(1, int(round(fps / args.fps)))
        print("input   : %s" % args.input)
        print("frames  : %d total, fps %.2f, ~%.2fs" % (total, fps, total / fps))
        print("window  : frame %d..%d (last %.1fs), sampling every %d frames"
              % (start, total - 1, args.seconds, step))

        cands = []
        cap.set(cv2.CAP_PROP_POS_FRAMES, start)
        idx = start
        while True:
            ok, frame = cap.read()
            if not ok:
                break
            if (idx - start) % step == 0:
                sc, sharp, mean = score(frame)
                cands.append((sc, idx, frame, sharp, mean))
            idx += 1
            if idx >= total:
                break

        if not cands:
            sys.exit("ERROR: no frame decoded in the tail window")

        cands.sort(key=lambda c: c[0], reverse=True)
        for sc, i, _f, sharp, mean in cands[:8]:
            print("  frame %5d | score %8.1f | laplacian %8.1f | mean %6.1f"
                  % (i, sc, sharp, mean))
        if args.list:
            print("done    : list only, nothing written")
            return 0

        base = args.output or "tail.png"
        root, ext = os.path.splitext(base)
        ext = ext or ".png"
        for rank, (sc, i, frame, _sharp, _mean) in enumerate(cands[: max(1, args.top)], 1):
            out = base if (args.top == 1 or rank == 1) else "%s-%d%s" % (root, rank, ext)
            imwrite_unicode(out, frame)
            print("picked  : rank %d -> %s (frame %d, score %.1f)" % (rank, out, i, sc))
        print("done")
        return 0
    finally:
        cap.release()
        if tmpdir:
            shutil.rmtree(tmpdir, ignore_errors=True)


if __name__ == "__main__":
    sys.exit(main())