#pragma once
// Windows ͷ�ļ�: 
#include <windows.h>
#include <jni.h>
#include <string>
#include<string.h>
#define JNI_METHOD(name) Java_i2f_natives_windows_NativesWindows8_##name

// shell scale api
#include<ShellScalingApi.h>
#pragma comment (lib,"shcore.lib")


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
	//��ȡ��C++���õ��ַ���
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
* ��������Java+����+����+������
* Java.i2f.natives.windows.EasyxWindows.hello
*/
extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(hello)(
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
	std::string hello = "Hello, Windows 8+  API for JNI!";

	//ֱ�ӷ�����һ���ַ�����java�ַ����й������һ�����ֽ����鹹���
	//��������ֽ����鹹����ʽ
	return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jintArray JNICALL
JNI_METHOD(getDpiForMonitor)(
JNIEnv* env,
jobject obj,
jlong hMonitor,
jint dpiType
){
	UINT x = 0, y = 0;
	HRESULT ok = GetDpiForMonitor(ptrOf<HMONITOR>(hMonitor), (MONITOR_DPI_TYPE)dpiType, &x, &y);
	if (ok != S_OK){
		return env->NewIntArray(0);
	}
	jint arr[] = { x, y };
	jintArray ret = env->NewIntArray(2);
	env->SetIntArrayRegion(ret, 0, 2, arr);
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getScaleFactorForMonitor)(
JNIEnv* env,
jobject obj,
jlong hMonitor
){
	DEVICE_SCALE_FACTOR dsf = SCALE_100_PERCENT;
	HRESULT ret = GetScaleFactorForMonitor(ptrOf<HMONITOR>(hMonitor), &dsf);
	return (int)dsf;

}

