#pragma once
#define WIN32_LEAN_AND_MEAN             //  从 Windows 头文件中排除极少使用的信息
// Windows 头文件: 
#include <windows.h>
#include <jni.h>
#include <string>
#include<string.h>


template<typename PTR>
PTR ptrOf(jlong hwnd){
	long long ptr = (long long)hwnd;
	return (PTR)ptr;
}

template<typename PTR>
jlong toPtr(PTR hwnd){
	return (long long)hwnd;
}

static int getRequireSize4Unicode2Utf8(const wchar_t * wstr)
{
	return WideCharToMultiByte(CP_UTF8, 0, wstr, -1, NULL, 0, NULL, NULL);
}
static int getRequireSize4Utf82Unicode(const char * astr)
{
	return MultiByteToWideChar(CP_UTF8, 0, astr, -1, NULL, 0);
}
static char * Unicode2Utf8(const wchar_t * wstr, char * astr)
{
	int size = WideCharToMultiByte(CP_UTF8, 0, wstr, -1, NULL, 0, NULL, NULL);
	WideCharToMultiByte(CP_UTF8, 0, wstr, -1, astr, size, NULL, NULL);
	return astr;
}
static wchar_t * Utf82Unicode(const char * astr, wchar_t * wstr)
{
	int size = MultiByteToWideChar(CP_UTF8, 0, astr, -1, NULL, 0);
	MultiByteToWideChar(CP_UTF8, 0, astr, -1, wstr, size);
	return wstr;
}

static jstring wchar2jstring(JNIEnv* env,wchar_t * buff){
	int size = getRequireSize4Unicode2Utf8(buff);
	char * bts = (char*)malloc(size*sizeof(char));
	Unicode2Utf8(buff, bts);
	jstring ret = env->NewStringUTF(bts);
	free(bts);
	return ret;
}

static wchar_t* jstring2wchar(JNIEnv* env, jstring str){
	//获取到C++可用的字符串
	const char * cstr = env->GetStringUTFChars(str, 0);
	int size = getRequireSize4Utf82Unicode(cstr);
	wchar_t* ret = (wchar_t*)malloc(size*sizeof(wchar_t));
	Utf82Unicode(cstr, ret);
	env->ReleaseStringUTFChars(str, cstr);
	return ret;
}

/**
* 命名规则：Java+包名+类名+方法名
* Java.i2f.natives.windows.NativeWindows.hello
*/
extern "C" JNIEXPORT jstring JNICALL
Java_i2f_natives_windows_NativeWindows_hello(
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
	std::string hello = "Hello from C++";

	//直接返回了一个字符串，java字符串中构造就有一个以字节数组构造的
	//这里就是字节数组构造形式
	return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jint JNICALL
Java_i2f_natives_windows_NativeWindows_getSystemMetrics(
JNIEnv* env,
jobject obj,
jint metric
) {
	return GetSystemMetrics(metric);
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativeWindows_getDC(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	return toPtr(GetDC(ptrOf<HWND>(hwnd)));
}

extern "C" JNIEXPORT jint JNICALL
Java_i2f_natives_windows_NativeWindows_releaseDC(
JNIEnv* env,
jobject obj,
jlong hwnd,
jlong hdc
) {
	long long ptr = (long long)hwnd;
	return ReleaseDC(ptrOf<HWND>(hwnd), ptrOf<HDC>(hwnd));
}

extern "C" JNIEXPORT jint JNICALL
Java_i2f_natives_windows_NativeWindows_getDeviceCaps(
JNIEnv* env,
jobject obj,
jlong hdc,
jint index
) {
	long long ptr = (long long)hdc;
	return GetDeviceCaps(ptrOf<HDC>(hdc), index);
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativeWindows_getForegroundWindow(
JNIEnv* env,
jobject obj
) {
	return toPtr(GetForegroundWindow());
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativeWindows_getDesktopWindow(
JNIEnv* env,
jobject obj
) {
	return toPtr(GetDesktopWindow());
}

extern "C" JNIEXPORT jstring JNICALL
Java_i2f_natives_windows_NativeWindows_getWindowText(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	wchar_t buff[4096] = { 0 };
	int len = GetWindowTextW(ptrOf<HWND>(hwnd), buff, 4096);
	return wchar2jstring(env,buff);
}