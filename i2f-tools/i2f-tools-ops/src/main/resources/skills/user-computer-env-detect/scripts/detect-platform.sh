#!/bin/sh
# S-1 platform + architecture probe (POSIX sh).
# Targets: Linux / macOS / Android(Termux) / WSL / MSYS-Cygwin(git-bash).
# For native Windows PowerShell use detect-platform.ps1 instead.
#
#   sh detect-platform.sh
#
# ASCII-only output on purpose (locale/encoding varies wildly across these hosts).

echo "=============================================================="
echo "  S-1 PLATFORM & ARCHITECTURE"
echo "=============================================================="

uname_s=$(uname -s 2>/dev/null)
uname_m=$(uname -m 2>/dev/null)
uname_r=$(uname -r 2>/dev/null)
printf '%-24s: %s\n' "uname -s" "$uname_s"
printf '%-24s: %s\n' "uname -m" "$uname_m"
printf '%-24s: %s\n' "uname -r" "$uname_r"

# ---------- OS family ----------
os="unknown"
case "$uname_s" in
    Linux)
        os="linux"
        if grep -qi microsoft /proc/version 2>/dev/null; then os="wsl"; fi
        if [ -n "$PREFIX" ]; then
            case "$PREFIX" in
                *com.termux*) os="android-termux" ;;
            esac
        fi
        if [ "$(uname -o 2>/dev/null)" = "Android" ]; then os="android-termux"; fi
        ;;
    Darwin)                os="macos" ;;
    MINGW*|MSYS*|CYGWIN*)  os="windows-msys" ;;
    FreeBSD|OpenBSD|NetBSD) os="bsd" ;;
esac
if [ "$os" = "linux" ] && [ -d /data/data/com.termux/files/usr ]; then os="android-termux"; fi
printf '%-24s: %s\n' "OS family" "$os"

# ---------- WSL detail ----------
if [ "$os" = "wsl" ]; then
    printf '%-24s: %s\n' "WSL distro" "${WSL_DISTRO_NAME:-<unset>}"
    printf '%-24s: %s\n' "WSL interop (/mnt/c)" "$([ -d /mnt/c ] && echo yes || echo no)"
    printf '%-24s: %s\n' "WSL version hint" "$(grep -oiE 'microsoft-(standard-)?wsl2?' /proc/version 2>/dev/null | head -1)"
fi

# ---------- distro ----------
if [ -r /etc/os-release ]; then
    . /etc/os-release 2>/dev/null
    printf '%-24s: %s\n' "distro ID" "${ID:-?}"
    printf '%-24s: %s\n' "distro name" "${PRETTY_NAME:-${NAME:-?}}"
    printf '%-24s: %s\n' "distro version" "${VERSION_ID:-?}"
fi

# ---------- macOS detail ----------
if [ "$os" = "macos" ]; then
    printf '%-24s: %s\n' "macOS product" "$(sw_vers -productVersion 2>/dev/null)"
    printf '%-24s: %s\n' "macOS build" "$(sw_vers -buildVersion 2>/dev/null)"
    trans=$(sysctl -n sysctl.proc_translated 2>/dev/null)
    if [ "$trans" = "1" ]; then
        printf '%-24s: %s\n' "Rosetta translated" "YES (real arch is arm64)"
    elif [ "$trans" = "0" ]; then
        printf '%-24s: %s\n' "Rosetta translated" "no"
    fi
    printf '%-24s: %s\n' "Homebrew" "$(command -v brew 2>/dev/null || echo '<not found>')"
fi

# ---------- Android / Termux detail ----------
if [ "$os" = "android-termux" ]; then
    printf '%-24s: %s\n' "PREFIX" "${PREFIX:-<unset>}"
    printf '%-24s: %s\n' "Android release" "$(getprop ro.build.version.release 2>/dev/null)"
    printf '%-24s: %s\n' "Android SDK" "$(getprop ro.build.version.sdk 2>/dev/null)"
    printf '%-24s: %s\n' "Device model" "$(getprop ro.product.model 2>/dev/null)"
    printf '%-24s: %s\n' "root available" "$(command -v su >/dev/null 2>&1 && echo yes || echo no)"
    echo "  NOTE: Termux is unprivileged - no sudo, no system CUDA/OpenGL context,"
    echo "        package manager is 'pkg'/'apt' with termux repos only."
fi

# ---------- normalise architecture ----------
arch="unknown"
case "$uname_m" in
    x86_64|amd64|AMD64)            arch="x64"    ;;
    i386|i486|i586|i686|x86)       arch="x86"    ;;
    aarch64|arm64|ARM64)           arch="arm64"  ;;
    armv7l|armv7|armv6l|armv8l|armhf) arch="arm32" ;;
    riscv64)                        arch="riscv64" ;;
    ppc64le|ppc64)                  arch="ppc64" ;;
    s390x)                          arch="s390x" ;;
esac
printf '%-24s: %s  (normalised)\n' "architecture" "$arch"
case "$arch" in
    x64)     echo "  -> 64-bit; use amd64/x86_64 binaries" ;;
    x86)     echo "  -> 32-bit only; many modern toolchains have NO 32-bit build" ;;
    arm64)   echo "  -> 64-bit ARM; use aarch64/arm64 binaries" ;;
    arm32)   echo "  -> 32-bit ARM; limited toolchain support" ;;
    unknown) echo "  -> unrecognised; record the raw uname -m value" ;;
esac

# ---------- bit width (best effort) ----------
printf '%-24s: %s\n' "getconf LONG_BIT" "$(getconf LONG_BIT 2>/dev/null || echo '?')"

# ---------- shell / terminal ----------
printf '%-24s: %s\n' "SHELL" "${SHELL:-<unset>}"
printf '%-24s: %s\n' "current shell" "${0:-?}"
printf '%-24s: %s\n' "TERM" "${TERM:-<unset>}"
printf '%-24s: %s\n' "session (ssh/tty)" "${SSH_CONNECTION:+ssh}${SSH_CONNECTION:-local}"

# ---------- locale / encoding ----------
printf '%-24s: %s\n' "LANG" "${LANG:-<unset>}"
printf '%-24s: %s\n' "LC_ALL" "${LC_ALL:-<unset>}"
printf '%-24s: %s\n' "LC_CTYPE" "${LC_CTYPE:-<unset>}"
echo "  (on Linux/macOS the default encoding is usually UTF-8; verify per tool)"

# ---------- privilege ----------
if [ "$(id -u 2>/dev/null)" = "0" ]; then
    printf '%-24s: %s\n' "euid" "0 (root)"
else
    printf '%-24s: %s\n' "euid" "$(id -u 2>/dev/null) (not root)"
fi
printf '%-24s: %s\n' "sudo" "$(command -v sudo 2>/dev/null || echo '<not found>')"

# ---------- package manager ----------
pm=""
for c in apt-get dnf yum pacman zypper apk brew pkg port; do
    if command -v "$c" >/dev/null 2>&1; then pm="$pm $c"; fi
done
printf '%-24s: %s\n' "package managers" "${pm:-<none>}"

# ---------- CPU cores / memory ----------
printf '%-24s: %s\n' "cpu cores" "$(getconf _NPROCESSORS_ONLN 2>/dev/null || nproc 2>/dev/null || echo '?')"
if [ -r /proc/meminfo ]; then
    mem_kb=$(awk '/^MemTotal:/{print $2}' /proc/meminfo 2>/dev/null)
    [ -n "$mem_kb" ] && printf '%-24s: %s GB\n' "memory total" "$(awk -v k="$mem_kb" 'BEGIN{printf "%.1f", k/1048576}')"
elif [ "$os" = "macos" ]; then
    mem_b=$(sysctl -n hw.memsize 2>/dev/null)
    [ -n "$mem_b" ] && printf '%-24s: %s GB\n' "memory total" "$(awk -v b="$mem_b" 'BEGIN{printf "%.1f", b/1073741824}')"
fi

# ---------- disks ----------
echo ""
echo "DISKS (free space decides where AI artifacts may go)"
df -h 2>/dev/null | awk 'NR==1 || ($1 ~ /^\/dev/ || $1 ~ /^\/$/)' | head -20

# ---------- tool presence ----------
echo ""
echo "TOOL PRESENCE (on PATH)"
for t in git java javac mvn gradle go node npm pnpm python3 python pip3 cc gcc g++ clang make cmake ninja ffmpeg pandoc libreoffice soffice convert magick tesseract docker adb sqlite3 nvcc nvidia-smi vulkaninfo; do
    p=$(command -v "$t" 2>/dev/null)
    if [ -n "$p" ]; then printf '  %-12s -> %s\n' "$t" "$p"; fi
done
echo "  (tools absent from PATH may still exist - check /opt, /usr/local, ~/.local/bin, ~/sdkman, ~/.nvm, ~/.pyenv)"

# ---------- version managers (very common on Unix) ----------
echo ""
echo "VERSION MANAGERS"
for d in "$HOME/sdkman" "$HOME/.nvm" "$HOME/.pyenv" "$HOME/.asdf" "$HOME/.rbenv" "$HOME/.cargo" "$HOME/go" "$HOME/.local/bin" "/opt/homebrew" "/usr/local/opt"; do
    [ -d "$d" ] && printf '  found: %s\n' "$d"
done

# ---------- GPU ----------
echo ""
echo "GPU"
if command -v nvidia-smi >/dev/null 2>&1; then
    nvidia-smi -L 2>/dev/null | head -8
else
    echo "  nvidia-smi: not found"
fi
if [ -d /proc/driver/nvidia ]; then echo "  /proc/driver/nvidia present"; fi
if [ -d /dev/dri ]; then echo "  /dev/dri: $(ls /dev/dri 2>/dev/null | tr '\n' ' ')"; fi
case "$os" in
    macos) echo "  macOS: CUDA is NOT supported; use Metal / MPS. Apple Silicon = unified memory." ;;
    android-termux) echo "  Termux: no CUDA/OpenGL compute access." ;;
esac
if command -v vulkaninfo >/dev/null 2>&1; then
    vulkaninfo --summary 2>/dev/null | grep -iE 'deviceName|apiVersion|driverVersion' | head -6
else
    echo "  vulkaninfo: not found (runtime may still work)"
fi

echo ""
echo "--------------------------------------------------------------"
echo "  NEXT: pick the matching branch in references/00-platform-and-arch.md"
echo "        Windows  -> use detect-*.ps1 (PowerShell)"
echo "        Unix-like-> use sections in 00-platform-and-arch.md + detect-platform.sh"
echo "--------------------------------------------------------------"