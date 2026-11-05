// OpenGL capability probe: creates a hidden window with a real GL context and
// reads back what the driver actually reports.
//
// Build:  csc /nologo /out:%TEMP%\opengl-probe.exe env-tools\opengl-probe.cs
// Run:    %TEMP%\opengl-probe.exe
//
// ASCII-only on purpose: console code page here is 936 (GBK).
using System;
using System.Runtime.InteropServices;

class GlProbe
{
    [DllImport("user32.dll", SetLastError = true, CharSet = CharSet.Auto)]
    static extern IntPtr CreateWindowEx(uint exStyle, string cls, string name, uint style,
        int x, int y, int w, int h, IntPtr parent, IntPtr menu, IntPtr inst, IntPtr param);

    [DllImport("user32.dll")] static extern IntPtr GetDC(IntPtr hWnd);
    [DllImport("user32.dll")] static extern int ReleaseDC(IntPtr hWnd, IntPtr hDC);
    [DllImport("user32.dll")] static extern bool DestroyWindow(IntPtr hWnd);

    [DllImport("gdi32.dll", SetLastError = true)]
    static extern int ChoosePixelFormat(IntPtr hdc, ref PIXELFORMATDESCRIPTOR pfd);
    [DllImport("gdi32.dll", SetLastError = true)]
    static extern bool SetPixelFormat(IntPtr hdc, int fmt, ref PIXELFORMATDESCRIPTOR pfd);

    [DllImport("opengl32.dll")] static extern IntPtr wglCreateContext(IntPtr hdc);
    [DllImport("opengl32.dll")] static extern bool wglMakeCurrent(IntPtr hdc, IntPtr ctx);
    [DllImport("opengl32.dll")] static extern bool wglDeleteContext(IntPtr ctx);
    [DllImport("opengl32.dll")] static extern IntPtr glGetString(uint name);
    [DllImport("opengl32.dll")] static extern void glGetIntegerv(uint pname, int[] data);

    [StructLayout(LayoutKind.Sequential)]
    struct PIXELFORMATDESCRIPTOR
    {
        public ushort nSize, nVersion; public uint dwFlags;
        public byte iPixelType, cColorBits, cRedBits, cRedShift, cGreenBits, cGreenShift,
                    cBlueBits, cBlueShift, cAlphaBits, cAlphaShift, cAccumBits, cAccumRedBits,
                    cAccumGreenBits, cAccumBlueBits, cAccumAlphaBits, cDepthBits, cStencilBits,
                    cAuxBuffers, iLayerType, bReserved;
        public uint dwLayerMask, dwVisibleMask, dwDamageMask;
    }

    static string S(uint id)
    {
        IntPtr p = glGetString(id);
        if (p == IntPtr.Zero) return "<null>";
        return Marshal.PtrToStringAnsi(p) ?? "<null>";
    }

    static int Main()
    {
        const uint WS_OVERLAPPEDWINDOW = 0x00CF0000;
        IntPtr hWnd = CreateWindowEx(0, "STATIC", "glprobe", WS_OVERLAPPEDWINDOW,
            0, 0, 8, 8, IntPtr.Zero, IntPtr.Zero, IntPtr.Zero, IntPtr.Zero);
        if (hWnd == IntPtr.Zero)
        {
            Console.WriteLine("ERROR: CreateWindowEx failed " + Marshal.GetLastWin32Error());
            return 1;
        }

        IntPtr hdc = GetDC(hWnd);
        if (hdc == IntPtr.Zero) { Console.WriteLine("ERROR: GetDC failed"); return 1; }

        PIXELFORMATDESCRIPTOR pfd = new PIXELFORMATDESCRIPTOR();
        pfd.nSize = (ushort)Marshal.SizeOf(typeof(PIXELFORMATDESCRIPTOR));
        pfd.nVersion = 1;
        pfd.dwFlags = 0x00000004  // PFD_DRAW_TO_WINDOW
                    | 0x00000020  // PFD_SUPPORT_OPENGL
                    | 0x00000001; // PFD_DOUBLEBUFFER
        pfd.iPixelType = 0; pfd.cColorBits = 32; pfd.cDepthBits = 24; pfd.cStencilBits = 8;
        pfd.iLayerType = 0;

        int fmt = ChoosePixelFormat(hdc, ref pfd);
        if (fmt == 0)
        {
            Console.WriteLine("ERROR: ChoosePixelFormat failed " + Marshal.GetLastWin32Error());
            return 1;
        }
        if (!SetPixelFormat(hdc, fmt, ref pfd))
        {
            Console.WriteLine("ERROR: SetPixelFormat failed " + Marshal.GetLastWin32Error());
            return 1;
        }

        IntPtr ctx = wglCreateContext(hdc);
        if (ctx == IntPtr.Zero)
        {
            Console.WriteLine("ERROR: wglCreateContext failed " + Marshal.GetLastWin32Error());
            return 1;
        }
        if (!wglMakeCurrent(hdc, ctx)) { Console.WriteLine("ERROR: wglMakeCurrent failed"); return 1; }

        Console.WriteLine("GL_VENDOR                  : " + S(0x1F00));
        Console.WriteLine("GL_RENDERER                : " + S(0x1F01));
        Console.WriteLine("GL_VERSION                 : " + S(0x1F02));
        Console.WriteLine("GL_SHADING_LANGUAGE_VERSION: " + S(0x8B8C));

        int[] v = new int[1];
        // GL constants follow the Khronos headers exactly - never guess them
        glGetIntegerv(0x0D33, v); Console.WriteLine("GL_MAX_TEXTURE_SIZE             : " + v[0]); // 32768 typical
        glGetIntegerv(0x8073, v); Console.WriteLine("GL_MAX_3D_TEXTURE_SIZE          : " + v[0]);
        glGetIntegerv(0x851C, v); Console.WriteLine("GL_MAX_CUBE_MAP_TEXTURE_SIZE    : " + v[0]);
        glGetIntegerv(0x84E8, v); Console.WriteLine("GL_MAX_RENDERBUFFER_SIZE        : " + v[0]);
        glGetIntegerv(0x8869, v); Console.WriteLine("GL_MAX_VERTEX_ATTRIBS           : " + v[0]);
        glGetIntegerv(0x8872, v); Console.WriteLine("GL_MAX_TEXTURE_IMAGE_UNITS      : " + v[0]);
        glGetIntegerv(0x8B4D, v); Console.WriteLine("GL_MAX_COMBINED_TEXTURE_UNITS   : " + v[0]);
        glGetIntegerv(0x8B4A, v); Console.WriteLine("GL_MAX_VERTEX_UNIFORM_COMPONENTS : " + v[0]);
        glGetIntegerv(0x8A30, v); Console.WriteLine("GL_MAX_UNIFORM_BLOCK_SIZE       : " + v[0]);
        glGetIntegerv(0x8A2F, v); Console.WriteLine("GL_MAX_UNIFORM_BUFFER_BINDINGS  : " + v[0]);
        glGetIntegerv(0x90DD, v); Console.WriteLine("GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS : " + v[0]);
        glGetIntegerv(0x8B4C, v); Console.WriteLine("GL_MAX_VERTEX_UNIFORM_BLOCKS    : " + v[0]);

        wglMakeCurrent(IntPtr.Zero, IntPtr.Zero);
        wglDeleteContext(ctx);
        ReleaseDC(hWnd, hdc);
        DestroyWindow(hWnd);
        return 0;
    }
}