#pragma once
// Windows 头文件: 
#include<Windows.h>
#include <jni.h>
#include <string>
#include<string.h>
#include<stdio.h>
// easyx
#include<graphics.h> 
#pragma comment (lib,"EasyXw.lib")
#pragma comment (lib,"EasyXa.lib")
#include<conio.h>

#define JNI_METHOD(name) Java_i2f_natives_windows_easyx_NativesEasyX_##name

template<typename PTR>
PTR ptrOf(jlong hwnd){
	long long ptr = (long long)hwnd;
	return (PTR)ptr;
}

template<typename PTR>
jlong toPtr(PTR hwnd){
	return (long long)hwnd;
}



int getRequireSize4Unicode2Utf8(const wchar_t * wstr)
{
	return WideCharToMultiByte(CP_UTF8, 0, wstr, -1, NULL, 0, NULL, NULL);
}
int getRequireSize4Utf82Unicode(const char * astr)
{
	return MultiByteToWideChar(CP_UTF8, 0, astr, -1, NULL, 0);
}
char * Unicode2Utf8(const wchar_t * wstr, char * astr)
{
	int size = WideCharToMultiByte(CP_UTF8, 0, wstr, -1, NULL, 0, NULL, NULL);
	WideCharToMultiByte(CP_UTF8, 0, wstr, -1, astr, size, NULL, NULL);
	return astr;
}
wchar_t * Utf82Unicode(const char * astr, wchar_t * wstr)
{
	int size = MultiByteToWideChar(CP_UTF8, 0, astr, -1, NULL, 0);
	MultiByteToWideChar(CP_UTF8, 0, astr, -1, wstr, size);
	return wstr;
}

jstring wchar2jstring(JNIEnv* env, wchar_t * buff){
	int size = getRequireSize4Unicode2Utf8(buff);
	char * bts = (char*)malloc(size*sizeof(char));
	Unicode2Utf8(buff, bts);
	jstring ret = env->NewStringUTF(bts);
	free(bts);
	return ret;
}

wchar_t* jstring2wchar(JNIEnv* env, jstring str){
	if (str == nullptr){
		return NULL;
	}
	//获取到C++可用的字符串
	const char * cstr = env->GetStringUTFChars(str, 0);
	int size = getRequireSize4Utf82Unicode(cstr);
	wchar_t* ret = (wchar_t*)malloc(size*sizeof(wchar_t));
	Utf82Unicode(cstr, ret);
	env->ReleaseStringUTFChars(str, cstr);
	return ret;
}

void freeWchar(wchar_t * ptr){
	if (ptr == NULL){
		return;
	}
	free(ptr);
}

/**
* 命名规则：Java+包名+类名+方法名
* Java.i2f.natives.windows.EasyxWindows.hello
*/
extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(hello)(
JNIEnv* env,
jobject /* this */) {
	/*
	* 参数解析：
	* JNIEnv* env:
	*      访问Java对象的方法和类的
	* jobject obj:
	*      谁调用了此API，就传入谁
	*
	* 因此，这两个参数是必须的
	* */

	//要返回的字符串，在C++中的形式
	std::string hello = "Hello, Windows EasyX API for JNI!";

	//直接返回了一个字符串，java字符串中构造就有一个以字节数组构造的
	//这里就是字节数组构造形式
	return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(initGraph)(
JNIEnv* env,
jobject obj,
jint width,
jint height,
jint flag
){
	HWND hwnd = initgraph(width, height, flag);
	return toPtr(hwnd);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(closeGraph)(
JNIEnv* env,
jobject obj
){
	closegraph();
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(clearDevice)(
JNIEnv* env,
jobject obj
){
	cleardevice();
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(beginBatchDraw)(
JNIEnv* env,
jobject obj
){
	BeginBatchDraw();
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(endBatchDraw)(
JNIEnv* env,
jobject obj
){
	EndBatchDraw();
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(flushBatchDraw)(
JNIEnv* env,
jobject obj
){
	FlushBatchDraw();
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(setWorkingImage)(
JNIEnv* env,
jobject obj,
jlong pImage
){
	SetWorkingImage(ptrOf<IMAGE*>(pImage));
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(loadImage)(
JNIEnv* env,
jobject obj,
jstring filePath,
jint width,
jint height,
jboolean resize
){
	IMAGE * img = new IMAGE();
	wchar_t* filePath_ptr = jstring2wchar(env, filePath);
	loadimage(img, filePath_ptr, width, height, resize ? true : false);
	freeWchar(filePath_ptr);
	return toPtr(img);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(freeImage)(
JNIEnv* env,
jobject obj,
jlong pImage
){
	IMAGE* img = ptrOf<IMAGE*>(pImage);
	delete img;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(createImage)(
JNIEnv* env,
jobject obj,
jint width,
jint height
){
	IMAGE* img = new IMAGE(width, height);
	return toPtr(img);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(saveImage)(
JNIEnv* env,
jobject obj,
jlong pImage,
jstring filePath
){
	IMAGE * img = ptrOf<IMAGE*>(pImage);
	wchar_t* filePath_ptr = jstring2wchar(env, filePath);
	saveimage(filePath_ptr, img);
	freeWchar(filePath_ptr);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(putImage)(
JNIEnv* env,
jobject obj,
jint dstX,
jint dstY,
jlong pImage,
jlong dwRop
){
	IMAGE * img = ptrOf<IMAGE*>(pImage);
	putimage(dstX, dstY, img, dwRop);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(setBkMode)(
JNIEnv* env,
jobject obj,
jint mode
){
	setbkmode(mode);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(setBkColor)(
JNIEnv* env,
jobject obj,
jint color
){
	setbkcolor((COLORREF)color);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(setColor)(
JNIEnv* env,
jobject obj,
jint color
){
	setcolor((COLORREF)color);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(setFillColor)(
JNIEnv* env,
jobject obj,
jint color
){
	setfillcolor((COLORREF)color);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(setLineColor)(
JNIEnv* env,
jobject obj,
jint color
){
	setlinecolor((COLORREF)color);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(circle)(
JNIEnv* env,
jobject obj,
jint x,
jint y,
jint radius
){
	circle(x, y, radius);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(fillCircle)(
JNIEnv* env,
jobject obj,
jint x,
jint y,
jint radius
){
	fillcircle(x, y, radius);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(clearCircle)(
JNIEnv* env,
jobject obj,
jint x,
jint y,
jint radius
){
	clearcircle(x, y, radius);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(solidCircle)(
JNIEnv* env,
jobject obj,
jint x,
jint y,
jint radius
){
	solidcircle(x, y, radius);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(rectangle)(
JNIEnv* env,
jobject obj,
jint left,
jint top,
jint right,
jint bottom
){
	rectangle(left, top, right, bottom);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(fillRectangle)(
JNIEnv* env,
jobject obj,
jint left,
jint top,
jint right,
jint bottom
){
	fillrectangle(left, top, right, bottom);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(clearRectangle)(
JNIEnv* env,
jobject obj,
jint left,
jint top,
jint right,
jint bottom
){
	clearrectangle(left, top, right, bottom);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(solidRectangle)(
JNIEnv* env,
jobject obj,
jint left,
jint top,
jint right,
jint bottom
){
	solidrectangle(left, top, right, bottom);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(setTextColor)(
JNIEnv* env,
jobject obj,
jint color
){
	settextcolor((COLORREF)color);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(outText)(
JNIEnv* env,
jobject obj,
jstring str
){
	wchar_t* str_ptr = jstring2wchar(env, str);
	outtext(str_ptr);
	freeWchar(str_ptr);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(outTextXy)(
JNIEnv* env,
jobject obj,
jint x,
jint y,
jstring str
){
	wchar_t* str_ptr = jstring2wchar(env, str);
	outtextxy(x, y, str_ptr);
	freeWchar(str_ptr);
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(hslToRgb)(
JNIEnv* env,
jobject obj,
jfloat h,
jfloat s,
jfloat l
){
	COLORREF ret = HSLtoRGB(h, s, l);
	return (jint)ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(hsvToRgb)(
JNIEnv* env,
jobject obj,
jfloat h,
jfloat s,
jfloat v
){
	COLORREF ret = HSVtoRGB(h, s, v);
	return (jint)ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(bgr)(
JNIEnv* env,
jobject obj,
jint color
){
	COLORREF ret = BGR(color);
	return (jint)ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getRValue)(
JNIEnv* env,
jobject obj,
jint color
){
	COLORREF ret = GetRValue(color);
	return (jint)ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getGValue)(
JNIEnv* env,
jobject obj,
jint color
){
	COLORREF ret = GetGValue(color);
	return (jint)ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getBValue)(
JNIEnv* env,
jobject obj,
jint color
){
	COLORREF ret = GetBValue(color);
	return (jint)ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(rgbToGray)(
JNIEnv* env,
jobject obj,
jint color
){
	COLORREF ret = RGBtoGRAY(color);
	return (jint)ret;
}

extern "C" JNIEXPORT jfloatArray JNICALL
JNI_METHOD(rgbToHsl)(
JNIEnv* env,
jobject obj,
jint color
){
	float h = 0, s = 0, l = 0;
	RGBtoHSL(color, &h, &s, &l);
	jfloat arr[] = { h, s, l };
	jfloatArray ret = env->NewFloatArray(3);
	env->SetFloatArrayRegion(ret, 0, 3, arr);
	return ret;
}

extern "C" JNIEXPORT jfloatArray JNICALL
JNI_METHOD(rgbToHsv)(
JNIEnv* env,
jobject obj,
jint color
){
	float h = 0, s = 0, v = 0;
	RGBtoHSV(color, &h, &s, &v);
	jfloat arr[] = { h, s, v };
	jfloatArray ret = env->NewFloatArray(3);
	env->SetFloatArrayRegion(ret, 0, 3, arr);
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getBkColor)(
JNIEnv* env,
jobject obj
){
	COLORREF ret = getbkcolor();
	return (jint)ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getBkMode)(
JNIEnv* env,
jobject obj
){
	int ret = getbkmode();
	return (jint)ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getFillColor)(
JNIEnv* env,
jobject obj
){
	COLORREF ret = getfillcolor();
	return (jint)ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getLineColor)(
JNIEnv* env,
jobject obj
){
	COLORREF ret = getlinecolor();
	return (jint)ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getPolyFillMode)(
JNIEnv* env,
jobject obj
){
	int ret = getpolyfillmode();
	return (jint)ret;
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(setPolyFillMode)(
JNIEnv* env,
jobject obj,
jint mode
){
	setpolyfillmode(mode);
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getRop2)(
JNIEnv* env,
jobject obj
){
	int ret = getrop2();
	return ret;
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(setRop2)(
JNIEnv* env,
jobject obj,
jint mode
){
	setrop2(mode);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(clearClipRgn)(
JNIEnv* env,
jobject obj
){
	clearcliprgn();
}

extern "C" JNIEXPORT jfloatArray JNICALL
JNI_METHOD(getAspectRatio)(
JNIEnv* env,
jobject obj
){
	float x = 0, y = 0;
	getaspectratio(&x, &y);
	jfloat arr[] = { x, y };
	jfloatArray ret = env->NewFloatArray(2);
	env->SetFloatArrayRegion(ret, 0, 2, arr);
	return ret;
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(setAspectRatio)(
JNIEnv* env,
jobject obj,
jfloat xasp,
jfloat yasp
){
	setaspectratio(xasp, yasp);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(graphDefaults)(
JNIEnv* env,
jobject obj
){
	graphdefaults();
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(setOrigin)(
JNIEnv* env,
jobject obj,
jint x,
jint y
){
	setorigin(x, y);
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(drawText)(
JNIEnv* env,
jobject obj,
jstring text,
jint left,
jint top,
jint right,
jint bottom,
jint uFormat
){
	RECT r = { 0 };
	r.left = left;
	r.top = top;
	r.right = right;
	r.bottom = bottom;
	wchar_t* text_ptr = jstring2wchar(env, text);
	int ret = drawtext(text_ptr, &r, uFormat);
	freeWchar(text_ptr);
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getTextColor)(
JNIEnv* env,
jobject obj
){
	COLORREF ret = gettextcolor();
	return (jint)ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(textHeight)(
JNIEnv* env,
jobject obj,
jstring text
){
	wchar_t* text_ptr = jstring2wchar(env, text);
	int ret = textheight(text_ptr);
	freeWchar(text_ptr);
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(textWidth)(
JNIEnv* env,
jobject obj,
jstring text
){
	wchar_t* text_ptr = jstring2wchar(env, text);
	int ret = textwidth(text_ptr);
	freeWchar(text_ptr);
	return ret;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(getImageBuffer)(
JNIEnv* env,
jobject obj,
jlong pImage
){
	IMAGE* ptr = ptrOf<IMAGE*>(pImage);
	DWORD* ret = GetImageBuffer(ptr);
	return toPtr(ret);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(setImageBufferValue)(
JNIEnv* env,
jobject obj,
jlong pBuffer,
jint index,
jint value
){
	DWORD* ptr = ptrOf<DWORD*>(pBuffer);
	ptr[index] = value;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getImageBufferValue)(
JNIEnv* env,
jobject obj,
jlong pBuffer,
jint index
){
	DWORD* ptr = ptrOf<DWORD*>(pBuffer);
	return (jint)ptr[index];
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(getImageHDC)(
JNIEnv* env,
jobject obj,
jlong pImage
){
	HDC ret = GetImageHDC(ptrOf<IMAGE*>(pImage));
	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(getWorkingImage)(
JNIEnv* env,
jobject obj
){
	IMAGE* ret = GetWorkingImage();
	return toPtr(ret);
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getImageHeight)(
JNIEnv* env,
jobject obj,
jlong pImage
){
	IMAGE* ret = ptrOf<IMAGE*>(pImage);
	return ret->getheight();
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getImageWidth)(
JNIEnv* env,
jobject obj,
jlong pImage
){
	IMAGE* ret = ptrOf<IMAGE*>(pImage);
	return ret->getwidth();
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(resize)(
JNIEnv* env,
jobject obj,
jlong pImage,
jint width,
jint height
){
	IMAGE* ret = ptrOf<IMAGE*>(pImage);
	Resize(ret, width, height);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(rotateImage)(
JNIEnv* env,
jobject obj,
jlong pDstImage,
jlong pSrcImage,
jdouble radian,
jint bkColor,
jboolean autosize,
jboolean highQuality
){
	IMAGE* dst = ptrOf<IMAGE*>(pDstImage);
	IMAGE* src = ptrOf<IMAGE*>(pSrcImage);
	rotateimage(dst, src, radian, (COLORREF)bkColor, (autosize == true ? true : false), (highQuality == true ? true : false));
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(flushMouseMsgBuffer)(
JNIEnv* env,
jobject obj
){
	FlushMouseMsgBuffer();
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(mouseHit)(
JNIEnv* env,
jobject obj
){
	return MouseHit() == true;
}

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(getMouseMsg)(
JNIEnv* env,
jobject obj
){
	MOUSEMSG msg = GetMouseMsg();
	wchar_t buff[1024] = { 0 };
	swprintf(buff, L"uMsg:%d;#;mkCtrl:%d;#;mkShift:%d;#;mkLButton:%d;#;mkMButton:^%d;#;mkRButton:%d;#;x:%d;#;y:%d;#;wheel:%d",
		msg.uMsg,
		msg.mkCtrl ? 1 : 0,
		msg.mkShift ? 1 : 0,
		msg.mkLButton ? 1 : 0,
		msg.mkMButton ? 1 : 0,
		msg.mkRButton ? 1 : 0,
		msg.x,
		msg.y,
		msg.wheel);
	return wchar2jstring(env, buff);
}

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(getEasyXVer)(
JNIEnv* env,
jobject obj
){
	wchar_t* ptr = GetEasyXVer();
	return wchar2jstring(env, ptr);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(getHWnd)(
JNIEnv* env,
jobject obj
){
	HWND ret = GetHWnd();
	return toPtr(ret);
}

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(inputBox)(
JNIEnv* env,
jobject obj,
jstring prompt,
jstring title,
jstring defaultText,
jint width,
jint height,
jboolean onlyOk
){
	wchar_t buff[4096] = { 0 };
	wchar_t* prompt_ptr = jstring2wchar(env, prompt);
	wchar_t* title_ptr = jstring2wchar(env, title);
	wchar_t* defaultText_ptr = jstring2wchar(env, defaultText);
	bool ok = InputBox(buff, 4096, prompt_ptr, title_ptr, defaultText_ptr, width, height, (onlyOk == true ? true : false));
	freeWchar(prompt_ptr);
	freeWchar(title_ptr);
	freeWchar(defaultText_ptr);
	if (ok == false){
		return nullptr;
	}
	return wchar2jstring(env, buff);
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getColor)(
JNIEnv* env,
jobject obj
){
	COLORREF ret = getcolor();
	return (jint)ret;
}


extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getWidth)(
JNIEnv* env,
jobject obj
){
	int ret = getwidth();
	return (jint)ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getHeight)(
JNIEnv* env,
jobject obj
){
	int ret = getheight();
	return (jint)ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getMaxX)(
JNIEnv* env,
jobject obj
){
	int ret = getmaxx();
	return (jint)ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getMaxY)(
JNIEnv* env,
jobject obj
){
	int ret = getmaxy();
	return (jint)ret;
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(setTextStyleLogFont)(
JNIEnv* env,
jobject obj,
jint      lfHeight,
jint      lfWidth,
jint      lfEscapement,
jint      lfOrientation,
jint      lfWeight,
jboolean      lfItalic,
jboolean      lfUnderline,
jboolean      lfStrikeOut,
jint      lfCharSet,
jint      lfOutPrecision,
jint      lfClipPrecision,
jint      lfQuality,
jint      lfPitchAndFamily,
jstring     lfFaceName
){
	LOGFONTW ft = { 0 };
	ft.lfHeight = lfHeight;
	ft.lfWidth = lfWidth;
	ft.lfEscapement = lfEscapement;
	ft.lfOrientation = lfOrientation;
	ft.lfWeight = lfWeight;
	ft.lfItalic = lfItalic ? TRUE : FALSE;
	ft.lfUnderline = lfUnderline ? TRUE : FALSE;
	ft.lfStrikeOut = lfStrikeOut ? TRUE : FALSE;
	ft.lfCharSet = lfCharSet;
	ft.lfOutPrecision = lfOutPrecision;
	ft.lfClipPrecision = lfClipPrecision;
	ft.lfQuality = lfQuality;
	ft.lfPitchAndFamily = lfPitchAndFamily;
	wchar_t* faceName_ptr = jstring2wchar(env, lfFaceName);
	if (faceName_ptr != NULL){
		lstrcpyW(ft.lfFaceName, faceName_ptr);
	}
	freeWchar(faceName_ptr);
	settextstyle(&ft);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(setTextStyle3)(
JNIEnv* env,
jobject obj,
jint      lfHeight,
jint      lfWidth,
jstring     lfFaceName,
jint      lfEscapement,
jint      lfOrientation,
jint      lfWeight,
jboolean      lfItalic,
jboolean      lfUnderline,
jboolean      lfStrikeOut,
jint      lfCharSet,
jint      lfOutPrecision,
jint      lfClipPrecision,
jint      lfQuality,
jint      lfPitchAndFamily
){
	wchar_t* faceName_ptr = jstring2wchar(env, lfFaceName);
	settextstyle(lfHeight, lfWidth, faceName_ptr, lfEscapement, lfOrientation,
		lfWeight, lfItalic ? true : false, lfUnderline ? true : false, lfStrikeOut ? true : false, lfCharSet, lfOutPrecision,
		lfClipPrecision, lfQuality, lfPitchAndFamily);
	freeWchar(faceName_ptr);
}


extern "C" JNIEXPORT void JNICALL
JNI_METHOD(setTextStyle2)(
JNIEnv* env,
jobject obj,
jint      lfHeight,
jint      lfWidth,
jstring     lfFaceName,
jint      lfEscapement,
jint      lfOrientation,
jint      lfWeight,
jboolean      lfItalic,
jboolean      lfUnderline,
jboolean      lfStrikeOut
){
	wchar_t* faceName_ptr = jstring2wchar(env, lfFaceName);
	settextstyle(lfHeight, lfWidth, faceName_ptr, lfEscapement, lfOrientation,
		lfWeight, lfItalic ? true : false, lfUnderline ? true : false, lfStrikeOut ? true : false);
	freeWchar(faceName_ptr);
}


extern "C" JNIEXPORT void JNICALL
JNI_METHOD(setTextStyle)(
JNIEnv* env,
jobject obj,
jint      lfHeight,
jint      lfWidth,
jstring     lfFaceName
){
	wchar_t* faceName_ptr = jstring2wchar(env, lfFaceName);
	settextstyle(lfHeight, lfWidth, faceName_ptr);
	freeWchar(faceName_ptr);
}

static jstring stringify_LOGFONT(JNIEnv* env, LOGFONTW* ft){
	wchar_t buff[2048] = { 0 };
	swprintf(buff, L"lfHeight:%d;#;lfWidth:%d;#;lfEscapement:%d;#;lfOrientation:%d;#;lfWeight:%d;#;lfItalic:%d;#;lfUnderline:%d;#;lfStrikeOut:%d;#;lfCharSet:%d;#;lfOutPrecision:%d;#;lfClipPrecision:%d;#;lfQuality:%d;#;lfPitchAndFamily:%d;#;lfFaceName:%s",
		ft->lfHeight,
		ft->lfWidth,
		ft->lfEscapement,
		ft->lfOrientation,
		ft->lfWeight,
		ft->lfItalic == TRUE ? 1 : 0,
		ft->lfUnderline == TRUE ? 1 : 0,
		ft->lfStrikeOut == TRUE ? 1 : 0,
		ft->lfCharSet,
		ft->lfOutPrecision,
		ft->lfClipPrecision,
		ft->lfQuality,
		ft->lfPitchAndFamily,
		ft->lfFaceName
		);
	return wchar2jstring(env, buff);
}

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(getTxtStyle)(
JNIEnv* env,
jobject obj
){
	LOGFONTW ft = { 0 };
	gettextstyle(&ft);
	return stringify_LOGFONT(env, &ft);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(bar3d)(
JNIEnv* env,
jobject obj,
jint left,
jint top,
jint right,
jint bottom,
jint depth,
jboolean topFlag
){
	bar3d(left, top, right, bottom, depth, (topFlag == true ? true : false));
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(drawPoly)(
JNIEnv* env,
jobject obj,
jintArray points
){
	int len = env->GetArrayLength(points);
	jint* arr = env->GetIntArrayElements(points, NULL);
	int* args = (int*)malloc(sizeof(int)*len);
	for (int i = 0; i < len; i++){
		args[i] = arr[i];
	}
	drawpoly(len / 2, args);
	env->ReleaseIntArrayElements(points, arr, 0);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(fillPoly)(
JNIEnv* env,
jobject obj,
jintArray points
){
	int len = env->GetArrayLength(points);
	jint* arr = env->GetIntArrayElements(points, NULL);
	int* args = (int*)malloc(sizeof(int)*len);
	for (int i = 0; i < len; i++){
		args[i] = arr[i];
	}
	fillpoly(len / 2, args);
	env->ReleaseIntArrayElements(points, arr, 0);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(setWriteMode)(
JNIEnv* env,
jobject obj,
jint mode
){
	setwritemode(mode);
}

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(getFillStyle)(
JNIEnv* env,
jobject obj
){
	FILLSTYLE fs;
	getfillstyle(&fs);
	wchar_t buff[256] = { 0 };

	swprintf(buff, L"style:%d;#;hatch:%d;#;ppattern:%l64d", fs.style, fs.hatch, fs.ppattern);

	return wchar2jstring(env, buff);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(setFillStyle)(
JNIEnv* env,
jobject obj,
jint style,
jint hatch,
jlong pImage
){
	IMAGE* ppattern = ptrOf<IMAGE*>(pImage);
	setfillstyle(style, hatch, ppattern);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(setFillStylePattern8x8)(
JNIEnv* env,
jobject obj,
jbyteArray ppattern8x8
){
	byte arr[8] = { 0 };
	jbyte* bts = env->GetByteArrayElements(ppattern8x8, NULL);
	for (int i = 0; i < 8; i++){
		arr[i] = bts[i];
	}
	setfillstyle(arr);
	env->ReleaseByteArrayElements(ppattern8x8, bts, 0);
}

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(getLineStyle)(
JNIEnv* env,
jobject obj
){
	LINESTYLE ls;
	getlinestyle(&ls);
	wchar_t buff[1024] = { 0 };
	wchar_t tmp[50] = { 0 };

	swprintf(buff, L"style:%d;#;thickness:%d;#;userStyle:", ls.style, ls.thickness);
	DWORD* arr = ls.puserstyle;
	for (int i = 0; i < ls.userstylecount; i++){
		if (i>0){
			lstrcatW(buff, L",");
		}
		ZeroMemory(tmp, sizeof(tmp));
		swprintf(tmp, L"%d", arr[i]);
		lstrcatW(buff, tmp);
	}
	return wchar2jstring(env, buff);
}


extern "C" JNIEXPORT void JNICALL
JNI_METHOD(setLineStyle)(
JNIEnv* env,
jobject obj,
jint style,
jint thickness,
jintArray userType
){
	DWORD* puserStyle = NULL;
	int len = 0;
	if (userType != nullptr){
		len = env->GetArrayLength(userType);
		jint* elems = env->GetIntArrayElements(userType, NULL);
		puserStyle = (DWORD*)malloc(sizeof(DWORD)*len);
		for (int i = 0; i < len; i++){
			puserStyle[i] = elems[i];
		}
		env->ReleaseIntArrayElements(userType, elems, 0);
	}
	setlinestyle(style, thickness, puserStyle, len);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(arc)(
JNIEnv* env,
jobject obj,
jint left,
jint top,
jint right,
jint bottom,
jdouble startRadian,
jdouble endRadian
){
	arc(left, top, right, bottom, startRadian, endRadian);
}



extern "C" JNIEXPORT void JNICALL
JNI_METHOD(ellipse)(
JNIEnv* env,
jobject obj,
jint left,
jint top,
jint right,
jint bottom
){
	ellipse(left, top, right, bottom);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(fillEllipse)(
JNIEnv* env,
jobject obj,
jint left,
jint top,
jint right,
jint bottom
){
	fillellipse(left, top, right, bottom);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(clearEllipse)(
JNIEnv* env,
jobject obj,
jint left,
jint top,
jint right,
jint bottom
){
	clearellipse(left, top, right, bottom);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(solidEllipse)(
JNIEnv* env,
jobject obj,
jint left,
jint top,
jint right,
jint bottom
){
	solidellipse(left, top, right, bottom);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(pie)(
JNIEnv* env,
jobject obj,
jint left,
jint top,
jint right,
jint bottom,
jdouble startRadian,
jdouble endRadian
){
	pie(left, top, right, bottom, startRadian, endRadian);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(fillPie)(
JNIEnv* env,
jobject obj,
jint left,
jint top,
jint right,
jint bottom,
jdouble startRadian,
jdouble endRadian
){
	fillpie(left, top, right, bottom, startRadian, endRadian);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(clearPie)(
JNIEnv* env,
jobject obj,
jint left,
jint top,
jint right,
jint bottom,
jdouble startRadian,
jdouble endRadian
){
	clearpie(left, top, right, bottom, startRadian, endRadian);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(solidPie)(
JNIEnv* env,
jobject obj,
jint left,
jint top,
jint right,
jint bottom,
jdouble startRadian,
jdouble endRadian
){
	solidpie(left, top, right, bottom, startRadian, endRadian);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(polygon)(
JNIEnv* env,
jobject obj,
jintArray points
){
	int len = env->GetArrayLength(points);
	jint* arr = env->GetIntArrayElements(points, NULL);
	POINT* args = (POINT*)malloc(sizeof(POINT)*(len / 2));
	for (int i = 0; i < len / 2; i++){
		args[i].x = arr[i * 2];
		args[i].y = arr[i * 2 + 1];
	}
	polygon(args, len / 2);
	free(args);
	env->ReleaseIntArrayElements(points, arr, 0);
}


extern "C" JNIEXPORT void JNICALL
JNI_METHOD(fillPolygon)(
JNIEnv* env,
jobject obj,
jintArray points
){
	int len = env->GetArrayLength(points);
	jint* arr = env->GetIntArrayElements(points, NULL);
	POINT* args = (POINT*)malloc(sizeof(POINT)*(len / 2));
	for (int i = 0; i < len / 2; i++){
		args[i].x = arr[i * 2];
		args[i].y = arr[i * 2 + 1];
	}
	fillpolygon(args, len / 2);
	free(args);
	env->ReleaseIntArrayElements(points, arr, 0);
}


extern "C" JNIEXPORT void JNICALL
JNI_METHOD(clearPolygon)(
JNIEnv* env,
jobject obj,
jintArray points
){
	int len = env->GetArrayLength(points);
	jint* arr = env->GetIntArrayElements(points, NULL);
	POINT* args = (POINT*)malloc(sizeof(POINT)*(len / 2));
	for (int i = 0; i < len / 2; i++){
		args[i].x = arr[i * 2];
		args[i].y = arr[i * 2 + 1];
	}
	clearpolygon(args, len / 2);
	free(args);
	env->ReleaseIntArrayElements(points, arr, 0);
}


extern "C" JNIEXPORT void JNICALL
JNI_METHOD(solidPolygon)(
JNIEnv* env,
jobject obj,
jintArray points
){
	int len = env->GetArrayLength(points);
	jint* arr = env->GetIntArrayElements(points, NULL);
	POINT* args = (POINT*)malloc(sizeof(POINT)*(len / 2));
	for (int i = 0; i < len / 2; i++){
		args[i].x = arr[i * 2];
		args[i].y = arr[i * 2 + 1];
	}
	solidpolygon(args, len / 2);
	free(args);
	env->ReleaseIntArrayElements(points, arr, 0);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(roundRect)(
JNIEnv* env,
jobject obj,
jint left,
jint top,
jint right,
jint bottom,
jint ellipseWidth,
jint ellipseHeight
){
	roundrect(left, top, right, bottom, ellipseWidth, ellipseHeight);
}


extern "C" JNIEXPORT void JNICALL
JNI_METHOD(fillRoundRect)(
JNIEnv* env,
jobject obj,
jint left,
jint top,
jint right,
jint bottom,
jint ellipseWidth,
jint ellipseHeight
){
	fillroundrect(left, top, right, bottom, ellipseWidth, ellipseHeight);
}


extern "C" JNIEXPORT void JNICALL
JNI_METHOD(clearRoundRect)(
JNIEnv* env,
jobject obj,
jint left,
jint top,
jint right,
jint bottom,
jint ellipseWidth,
jint ellipseHeight
){
	clearroundrect(left, top, right, bottom, ellipseWidth, ellipseHeight);
}


extern "C" JNIEXPORT void JNICALL
JNI_METHOD(solidRoundRect)(
JNIEnv* env,
jobject obj,
jint left,
jint top,
jint right,
jint bottom,
jint ellipseWidth,
jint ellipseHeight
){
	solidroundrect(left, top, right, bottom, ellipseWidth, ellipseHeight);
}


extern "C" JNIEXPORT void JNICALL
JNI_METHOD(floodFill)(
JNIEnv* env,
jobject obj,
jint x,
jint y,
jint color,
jint fillType
){
	floodfill(x, y, (COLORREF)color, fillType);
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getPixel)(
JNIEnv* env,
jobject obj,
jint x,
jint y
){
	COLORREF ret=getpixel(x, y);
	return (jint)ret;
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(putPixel)(
JNIEnv* env,
jobject obj,
jint x,
jint y,
jint color
){
	putpixel(x, y, color);
}


extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getX)(
JNIEnv* env,
jobject obj
){
	return getx();
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getY)(
JNIEnv* env,
jobject obj
){
	return gety();
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(line)(
JNIEnv* env,
jobject obj,
jint x1,
jint y1,
jint x2,
jint y2
){
	line(x1, y1, x2, y2);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(lineRel)(
JNIEnv* env,
jobject obj,
jint dx,
jint dy
){
	linerel(dx,dy);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(lineTo)(
JNIEnv* env,
jobject obj,
jint x,
jint y
){
	lineto(x, y);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(moveRel)(
JNIEnv* env,
jobject obj,
jint dx,
jint dy
){
	moverel(dx, dy);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(moveTo)(
JNIEnv* env,
jobject obj,
jint x,
jint y
){
	moveto(x, y);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(polyBezier)(
JNIEnv* env,
jobject obj,
jintArray points
){
	int len = env->GetArrayLength(points);
	jint* arr = env->GetIntArrayElements(points, NULL);
	POINT* args = (POINT*)malloc(sizeof(POINT)*(len / 2));
	for (int i = 0; i < len / 2; i++){
		args[i].x = arr[i * 2];
		args[i].y = arr[i * 2 + 1];
	}
	polybezier(args, len / 2);
	free(args);
	env->ReleaseIntArrayElements(points, arr, 0);
}


extern "C" JNIEXPORT void JNICALL
JNI_METHOD(polyLine)(
JNIEnv* env,
jobject obj,
jintArray points
){
	int len = env->GetArrayLength(points);
	jint* arr = env->GetIntArrayElements(points, NULL);
	POINT* args = (POINT*)malloc(sizeof(POINT)*(len / 2));
	for (int i = 0; i < len / 2; i++){
		args[i].x = arr[i * 2];
		args[i].y = arr[i * 2 + 1];
	}
	polyline(args, len / 2);
	free(args);
	env->ReleaseIntArrayElements(points, arr, 0);
}