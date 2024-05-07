#pragma once
#define WIN32_LEAN_AND_MEAN             //  �� Windows ͷ�ļ����ų�����ʹ�õ���Ϣ
// Windows ͷ�ļ�: 
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
	//��ȡ��C++���õ��ַ���
	const char * cstr = env->GetStringUTFChars(str, 0);
	int size = getRequireSize4Utf82Unicode(cstr);
	wchar_t* ret = (wchar_t*)malloc(size*sizeof(wchar_t));
	Utf82Unicode(cstr, ret);
	env->ReleaseStringUTFChars(str, cstr);
	return ret;
}

/**
* ��������Java+����+����+������
* Java.i2f.natives.windows.NativeWindows.hello
*/
extern "C" JNIEXPORT jstring JNICALL
Java_i2f_natives_windows_NativeWindows_hello(
JNIEnv* env,
jobject /* this */) {
	/*
	* ����������
	* JNIEnv* env:
	*      ����Java����ķ��������
	* jobject obj:
	*      ˭�����˴�API���ʹ���˭
	*
	* ��ˣ������������Ǳ����
	* */

	//Ҫ���ص��ַ�������C++�е���ʽ
	std::string hello = "Hello from C++";

	//ֱ�ӷ�����һ���ַ�����java�ַ����й������һ�����ֽ����鹹���
	//��������ֽ����鹹����ʽ
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