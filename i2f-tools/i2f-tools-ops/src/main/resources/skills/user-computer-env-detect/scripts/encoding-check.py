"""
Encoding probe (Python side). ASCII output + hex dumps only,
so the result itself can never be mangled by the console code page.

Usage: python env-tools\encoding-check.py
"""
import locale
import os
import sys
import tempfile

print("=" * 66)
print("PYTHON ENCODING PROBE")
print("=" * 66)
print("python version                 : %s" % sys.version.split()[0])
print("sys.getdefaultencoding()       : %s" % sys.getdefaultencoding())
print("locale.getpreferredencoding(False) : %s" % locale.getpreferredencoding(False))
print("locale.getpreferredencoding(True)  : %s" % locale.getpreferredencoding(True))
print("sys.stdout.encoding            : %s" % sys.stdout.encoding)
print("sys.stdin.encoding             : %s" % sys.stdin.encoding)
print("sys.stderr.encoding            : %s" % sys.stderr.encoding)
print("sys.getfilesystemencoding()    : %s" % sys.getfilesystemencoding())
print("sys.flags.utf8_mode            : %s" % sys.flags.utf8_mode)
print("locale.getlocale()             : %s" % (locale.getlocale(),))
print("OS ANSI code page (win32)      : %s"
      % (__import__("ctypes").windll.kernel32.GetACP()))
print("OS OEM  code page (win32)      : %s"
      % (__import__("ctypes").windll.kernel32.GetOEMCP()))

print("-" * 66)
print("ENV VARS")
for k in ("PYTHONUTF8", "PYTHONIOENCODING", "PYTHONLEGACYWINDOWSSTDIO",
          "PYTHONLEGACYWINDOWSFSENCODING", "LANG", "LC_ALL"):
    print("  %-32s = %r" % (k, os.environ.get(k)))

print("-" * 66)
print("open() WITHOUT encoding -> actual default")
probe = os.path.join(tempfile.gettempdir(), "_enc_probe.txt")
with open(probe, "w") as f:
    print("  open(..., 'w').encoding    = %s" % f.encoding)
with open(probe, "w", encoding="utf-8") as f:
    print("  explicit utf-8             = %s" % f.encoding)
os.remove(probe)

print("-" * 66)
print("BYTE-LEVEL PROOF (this is how you verify encodings reliably)")
s = "\u4e2d\u6587\u6d4b\u8bd5"   # CJK: zhong wen ce shi
print("  source string (escaped)    : %s" % s.encode("unicode_escape").decode("ascii"))
print("  len(characters)            : %d" % len(s))
print("  encode('utf-8')            : %s  (%d bytes)"
      % (s.encode("utf-8").hex(" "), len(s.encode("utf-8"))))
print("  encode('gbk')              : %s  (%d bytes)"
      % (s.encode("gbk").hex(" "), len(s.encode("gbk"))))
print("  encode('utf-8-sig') has BOM: %s ..." % s.encode("utf-8-sig")[:3].hex(" "))

print("-" * 66)
print("Windows-only pitfalls")
print("  stdout is a console?       : %s" % sys.stdout.isatty())
print("  -> if isatty() is False and you still get mojibake, the pipe is")
print("     the culprit, not Python. Set PYTHONIOENCODING=utf-8 or")
print("     sys.stdout.reconfigure(encoding='utf-8').")
print("=" * 66)