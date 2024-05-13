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
