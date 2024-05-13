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
	wchar_t* filePath_ptr=jstring2wchar(env, filePath);
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
	circle(x,y,radius);
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
	rectangle(left,top,right,bottom);
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
	outtextxy(x,y,str_ptr);
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
	COLORREF ret=HSLtoRGB(h, s, l);
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
	RGBtoHSL(color,&h,&s,&l);
	jfloat arr[] = { h, s, l };
	jfloatArray ret=env->NewFloatArray(3);
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
	COLORREF ret=getbkcolor();
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
	int ret=getrop2();
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
	jfloatArray ret=env->NewFloatArray(2);
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
	setaspectratio(xasp,yasp);
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
	setorigin(x,y);
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
	int ret=drawtext(text_ptr,&r,uFormat);
	freeWchar(text_ptr);
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getTextColor)(
JNIEnv* env,
jobject obj
){
	COLORREF ret=gettextcolor();
	return (jint)ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(textHeight)(
JNIEnv* env,
jobject obj,
jstring text
){
	wchar_t* text_ptr = jstring2wchar(env, text);
	int ret=textheight(text_ptr);
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
	DWORD* ret=GetImageBuffer(ptr);
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
	HDC ret=GetImageHDC(ptrOf<IMAGE*>(pImage));
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
	return MouseHit()==true;
}

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(getMouseMsg)(
JNIEnv* env,
jobject obj
){
	MOUSEMSG msg=GetMouseMsg();
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
	wchar_t* ptr=GetEasyXVer();
	return wchar2jstring(env,ptr);
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
	wchar_t* prompt_ptr = jstring2wchar(env,prompt);
	wchar_t* title_ptr = jstring2wchar(env, title);
	wchar_t* defaultText_ptr = jstring2wchar(env, defaultText);
	bool ok=InputBox(buff, 4096, prompt_ptr, title_ptr, defaultText_ptr, width, height, (onlyOk==true?true:false));
	freeWchar(prompt_ptr);
	freeWchar(title_ptr);
	freeWchar(defaultText_ptr);
	if (ok==false){
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
	ft.lfItalic = lfItalic?TRUE:FALSE;
	ft.lfUnderline = lfUnderline?TRUE:FALSE;
	ft.lfStrikeOut = lfStrikeOut ? TRUE : FALSE;
	ft.lfCharSet = lfCharSet;
	ft.lfOutPrecision = lfOutPrecision;
	ft.lfClipPrecision = lfClipPrecision;
	ft.lfQuality = lfQuality;
	ft.lfPitchAndFamily = lfPitchAndFamily;
	wchar_t* faceName_ptr = jstring2wchar(env, lfFaceName);
	if (faceName_ptr != NULL){
		lstrcpyW(ft.lfFaceName,faceName_ptr);
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
		lfWeight, lfItalic?true:false, lfUnderline?true:false, lfStrikeOut?true:false, lfCharSet, lfOutPrecision,
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

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(getTxtStyle)(
JNIEnv* env,
jobject obj
){
	LOGFONTW ft = { 0 };
	gettextstyle(&ft);
	wchar_t buff[2048] = { 0 };
		swprintf(buff, L"lfHeight:%d;#;lfWidth:%d;#;lfEscapement:%d;#;lfOrientation:%d;#;lfWeight:%d;#;lfItalic:%d;#;lfUnderline:%d;#;lfStrikeOut:%d;#;lfCharSet:%d;#;lfOutPrecision:%d;#;lfClipPrecision:%d;#;lfQuality:%d;#;lfPitchAndFamily:%d;#;lfFaceName:%s",
		ft.lfHeight,
		ft.lfWidth,
		ft.lfEscapement,
		ft.lfOrientation,
		ft.lfWeight,
		ft.lfItalic==TRUE?1:0,
		ft.lfUnderline==TRUE?1:0,
		ft.lfStrikeOut==TRUE?1:0,
		ft.lfCharSet,
		ft.lfOutPrecision,
		ft.lfClipPrecision,
		ft.lfQuality,
		ft.lfPitchAndFamily,
		ft.lfFaceName
		);
	return wchar2jstring(env, buff);
}