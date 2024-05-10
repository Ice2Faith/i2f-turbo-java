#pragma once
// Windows 头文件: 
#include <windows.h>
#include <jni.h>
#include <string>
#include<xstring>
#include<string.h>
#include<list>
//枚举进程依赖
#include<TlHelp32.h>
//回收站依赖
#include <Shlwapi.h>
#pragma comment (lib,"Shlwapi.lib")

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
* Java.i2f.natives.windows.NativeWindows.hello
*/
extern "C" JNIEXPORT jstring JNICALL
Java_i2f_natives_windows_NativesWindows_hello(
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
	std::string hello = "Hello, Windows API for JNI!";

	//直接返回了一个字符串，java字符串中构造就有一个以字节数组构造的
	//这里就是字节数组构造形式
	return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jint JNICALL
Java_i2f_natives_windows_NativesWindows_getSystemMetrics(
JNIEnv* env,
jobject obj,
jint metric
) {
	return GetSystemMetrics(metric);
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_getDC(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	return toPtr(GetDC(ptrOf<HWND>(hwnd)));
}

extern "C" JNIEXPORT jint JNICALL
Java_i2f_natives_windows_NativesWindows_releaseDC(
JNIEnv* env,
jobject obj,
jlong hwnd,
jlong hdc
) {
	long long ptr = (long long)hwnd;
	return ReleaseDC(ptrOf<HWND>(hwnd), ptrOf<HDC>(hwnd));
}

extern "C" JNIEXPORT jint JNICALL
Java_i2f_natives_windows_NativesWindows_getDeviceCaps(
JNIEnv* env,
jobject obj,
jlong hdc,
jint index
) {
	long long ptr = (long long)hdc;
	return GetDeviceCaps(ptrOf<HDC>(hdc), index);
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_getForegroundWindow(
JNIEnv* env,
jobject obj
) {
	return toPtr(GetForegroundWindow());
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_getDesktopWindow(
JNIEnv* env,
jobject obj
) {
	return toPtr(GetDesktopWindow());
}

extern "C" JNIEXPORT jstring JNICALL
Java_i2f_natives_windows_NativesWindows_getWindowText(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	wchar_t buff[4096] = { 0 };
	int len = GetWindowTextW(ptrOf<HWND>(hwnd), buff, 4096);
	return wchar2jstring(env, buff);
}

extern "C" JNIEXPORT jboolean JNICALL
Java_i2f_natives_windows_NativesWindows_setWindowText(
JNIEnv* env,
jobject obj,
jlong hwnd,
jstring title
) {
	wchar_t* buff = jstring2wchar(env, title);
	BOOL ret = SetWindowTextW(ptrOf<HWND>(hwnd), buff);
	freeWchar(buff);
	return ret == TRUE;
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_windowFromPoint(
JNIEnv* env,
jobject obj,
jint x,
jint y
) {
	POINT p;
	p.x = x;
	p.y = y;
	return toPtr(WindowFromPoint(p));
}

extern "C" JNIEXPORT jintArray JNICALL
Java_i2f_natives_windows_NativesWindows_getCursorPos(
JNIEnv* env,
jobject obj
) {
	POINT p;
	GetCursorPos(&p);
	jint arr[] = { p.x, p.y };
	jintArray ret = env->NewIntArray(2);
	env->SetIntArrayRegion(ret, 0, 2, arr);
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
Java_i2f_natives_windows_NativesWindows_messageBox(
JNIEnv* env,
jobject obj,
jlong hwnd,
jstring content,
jstring title,
jint type
) {
	wchar_t* content_ptr = jstring2wchar(env, content);
	wchar_t* title_ptr = jstring2wchar(env, title);
	int ret = MessageBoxW(ptrOf<HWND>(hwnd), content_ptr, title_ptr, type);
	freeWchar(content_ptr);
	freeWchar(title_ptr);
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_i2f_natives_windows_NativesWindows_messageBeep(
JNIEnv* env,
jobject obj,
jint type
) {
	BOOL ret = MessageBeep(type);
	return ret == TRUE;
}

extern "C" JNIEXPORT jintArray JNICALL
Java_i2f_natives_windows_NativesWindows_getWindowRect(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	RECT rect;
	GetWindowRect(ptrOf<HWND>(hwnd), &rect);
	jint arr[] = { rect.left, rect.top, rect.right, rect.bottom };
	jintArray ret = env->NewIntArray(4);
	env->SetIntArrayRegion(ret, 0, 4, arr);
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_i2f_natives_windows_NativesWindows_setWindowPos(
JNIEnv* env,
jobject obj,
jlong hwnd,
jlong hwndInsertAfter,
jint x,
jint y,
jint cx,
jint cy,
jint flag
) {
	BOOL ret = SetWindowPos(ptrOf<HWND>(hwnd), ptrOf<HWND>(hwndInsertAfter), x, y, cx, cy, flag);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_i2f_natives_windows_NativesWindows_showWindow(
JNIEnv* env,
jobject obj,
jlong hwnd,
int cmdShow
) {
	BOOL ret = ShowWindow(ptrOf<HWND>(hwnd), cmdShow);
	return ret == TRUE;
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_setParent(
JNIEnv* env,
jobject obj,
jlong hwndChild,
jlong hwndNewParent
) {
	HWND ret = SetParent(ptrOf<HWND>(hwndChild), ptrOf<HWND>(hwndNewParent));
	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_findWindow(
JNIEnv* env,
jobject obj,
jstring className,
jstring windowName
) {
	wchar_t* className_ptr = jstring2wchar(env, className);
	wchar_t* windowName_ptr = jstring2wchar(env, windowName);

	HWND ret = FindWindowW(className_ptr, windowName_ptr);
	freeWchar(className_ptr);
	freeWchar(windowName_ptr);
	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_getWindow(
JNIEnv* env,
jobject obj,
jlong hwnd,
jint cmd
) {
	HWND ret = GetWindow(ptrOf<HWND>(hwnd), cmd);
	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_sendMessage(
JNIEnv* env,
jobject obj,
jlong hwnd,
int msg,
jlong wParamPtr,
jlong lParamPtr
) {
	LRESULT ret = SendMessageW(ptrOf<HWND>(hwnd), msg, wParamPtr, lParamPtr);
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_i2f_natives_windows_NativesWindows_postMessage(
JNIEnv* env,
jobject obj,
jlong hwnd,
int msg,
jlong wParamPtr,
jlong lParamPtr
) {
	BOOL ret = PostMessageW(ptrOf<HWND>(hwnd), msg, wParamPtr, lParamPtr);
	return ret == TRUE;
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_getWindowThreadProcessId(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	DWORD processId;
	GetWindowThreadProcessId(ptrOf<HWND>(hwnd), &processId);
	return processId;
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_openProcess(
JNIEnv* env,
jobject obj,
jlong dwDesiredAccess,
jboolean bInheritHandle,
jlong dwProcessId
) {
	HANDLE handle = OpenProcess(dwDesiredAccess, (bInheritHandle == true ? TRUE : FALSE), dwProcessId);
	return toPtr(handle);
}

extern "C" JNIEXPORT jboolean JNICALL
Java_i2f_natives_windows_NativesWindows_isInvalidHandle(
JNIEnv* env,
jobject obj,
jlong handle
) {
	return ptrOf<HANDLE>(handle) == INVALID_HANDLE_VALUE;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_i2f_natives_windows_NativesWindows_terminateProcess(
JNIEnv* env,
jobject obj,
jlong hProcess,
jint uExitCode
) {
	BOOL ret = TerminateProcess(ptrOf<HANDLE>(hProcess), uExitCode);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_i2f_natives_windows_NativesWindows_closeHandle(
JNIEnv* env,
jobject obj,
jlong hObject
) {
	BOOL ret = CloseHandle(ptrOf<HANDLE>(hObject));
	return ret == TRUE;
}

extern "C" JNIEXPORT jint JNICALL
Java_i2f_natives_windows_NativesWindows_winExec(
JNIEnv* env,
jobject obj,
jstring cmdLine,
jint uCmdShow
) {
	const char * cstr = env->GetStringUTFChars(cmdLine, 0);
	UINT ret = WinExec(cstr, uCmdShow);
	env->ReleaseStringUTFChars(cmdLine, cstr);
	return ret;
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_getNextWindow(
JNIEnv* env,
jobject obj,
jlong hwnd,
jint cmd
) {
	HWND handle = ptrOf<HWND>(hwnd);
	HWND ret = GetNextWindow(handle, cmd);
	return toPtr(ret);
}

extern "C" JNIEXPORT jstring JNICALL
Java_i2f_natives_windows_NativesWindows_getClassName(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	wchar_t buff[4096] = { 0 };
	int len = GetClassNameW(ptrOf<HWND>(hwnd), buff, 4096);
	return wchar2jstring(env, buff);
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_getCurrentProcess(
JNIEnv* env,
jobject obj
) {
	HANDLE handle = GetCurrentProcess();
	return toPtr(handle);
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_getCurrentProcessId(
JNIEnv* env,
jobject obj
) {
	DWORD ret = GetCurrentProcessId();
	return ret;
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_openProcessToken(
JNIEnv* env,
jobject obj,
jlong hProcess,
jlong dwDesiredAccess
) {
	HANDLE handle = NULL;
	BOOL ret = OpenProcessToken(ptrOf<HANDLE>(hProcess), dwDesiredAccess, &handle);
	if (ret == FALSE){
		return toPtr(INVALID_HANDLE_VALUE);
	}
	return toPtr(handle);
}

extern "C" JNIEXPORT jlongArray JNICALL
Java_i2f_natives_windows_NativesWindows_lookupPrivilegeValue(
JNIEnv* env,
jobject obj,
jstring pSystemName,
jstring pName
) {
	wchar_t* systemName_ptr = jstring2wchar(env, pSystemName);
	wchar_t* name_ptr = jstring2wchar(env, pName);
	LUID p;
	BOOL ok = LookupPrivilegeValueW(systemName_ptr, name_ptr, &p);
	freeWchar(systemName_ptr);
	freeWchar(name_ptr);
	if (ok == FALSE){
		return env->NewLongArray(0);
	}
	jlong arr[] = { p.LowPart, p.HighPart };
	jlongArray ret = env->NewLongArray(2);
	env->SetLongArrayRegion(ret, 0, 4, arr);
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_i2f_natives_windows_NativesWindows_adjustTokenPrivileges(
JNIEnv* env,
jobject obj,
jlong tokenHandle,
jboolean disableAllPrivileges,
jint attributes,
jlong luidLowPart,
jlong luidHighPart
) {
	TOKEN_PRIVILEGES token;
	token.PrivilegeCount = 1;
	token.Privileges[0].Luid.LowPart = luidLowPart;
	token.Privileges[0].Luid.HighPart = luidHighPart;
	token.Privileges[0].Attributes = attributes;
	BOOL ret = AdjustTokenPrivileges(ptrOf<HANDLE>(tokenHandle), (disableAllPrivileges == true ? TRUE : FALSE), &token, sizeof(token), NULL, NULL);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_i2f_natives_windows_NativesWindows_adjustProcessPrivileges(
JNIEnv* env,
jobject obj,
jlong processHandle,
jstring seName,
jboolean enable
) {
	HANDLE hToken = NULL;
	BOOL ret = OpenProcessToken(ptrOf<HANDLE>(processHandle), TOKEN_ALL_ACCESS, &hToken);
	if (ret == TRUE)
	{
		wchar_t* name_ptr = jstring2wchar(env, seName);

		TOKEN_PRIVILEGES tp;
		tp.PrivilegeCount = 1;
		LookupPrivilegeValueW(NULL, name_ptr, &tp.Privileges[0].Luid);
		tp.Privileges[0].Attributes = (enable ? SE_PRIVILEGE_ENABLED : SE_PRIVILEGE_REMOVED);
		AdjustTokenPrivileges(hToken, FALSE, &tp, sizeof(tp), NULL, NULL);
		CloseHandle(hToken);
		return true;
	}
	return false;
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_getWindowLong(
JNIEnv* env,
jobject obj,
jlong hwnd,
jint index
) {
	LONG ret = GetWindowLongW(ptrOf<HWND>(hwnd), index);
	return ret;
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_getClassLong(
JNIEnv* env,
jobject obj,
jlong hwnd,
jint index
) {
	LONG ret = GetClassLongW(ptrOf<HWND>(hwnd), index);
	return ret;
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_setWindowLong(
JNIEnv* env,
jobject obj,
jlong hwnd,
jint index,
jlong dwNewLong
) {
	LONG ret = SetWindowLongW(ptrOf<HWND>(hwnd), index, dwNewLong);
	return ret;
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_setClassLong(
JNIEnv* env,
jobject obj,
jlong hwnd,
jint index,
jlong dwNewLong
) {
	LONG ret = SetClassLongW(ptrOf<HWND>(hwnd), index, dwNewLong);
	return ret;
}

extern "C" JNIEXPORT jlongArray JNICALL
Java_i2f_natives_windows_NativesWindows_getLayeredWindowAttributes(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	COLORREF ref;
	BYTE alpha;
	DWORD flag;
	BOOL ok = GetLayeredWindowAttributes(ptrOf<HWND>(hwnd), &ref, &alpha, &flag);
	if (ok == FALSE){
		return env->NewLongArray(0);
	}
	jlong arr[] = { ref, alpha, flag };
	jlongArray ret = env->NewLongArray(3);
	env->SetLongArrayRegion(ret, 0, 3, arr);
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_i2f_natives_windows_NativesWindows_setLayeredWindowAttributes(
JNIEnv* env,
jobject obj,
jlong hwnd,
jint color,
jbyte alpha,
jlong flags
) {
	BOOL ret = SetLayeredWindowAttributes(ptrOf<HWND>(hwnd), color, alpha, flags);
	return ret == TRUE;
}

BOOL CALLBACK __EnumWindow_Proc(HWND hWnd, LPARAM lParam)
{
	std::list<HWND>* result = (std::list<HWND>*)lParam;

	result->push_back(hWnd);

	return TRUE;
}

extern "C" JNIEXPORT jlongArray JNICALL
Java_i2f_natives_windows_NativesWindows_enumWindows(
JNIEnv* env,
jobject obj
) {
	std::list<HWND> result;
	BOOL ok = EnumWindows(__EnumWindow_Proc, (LPARAM)&result);
	if (ok == FALSE){
		return env->NewLongArray(0);
	}
	int size = result.size();
	jlong* arr = (jlong*)malloc(size*sizeof(jlong));
	for (int i = 0; i < size; i++){
		HWND h = result.front();
		arr[i] = toPtr(h);
		result.pop_front();
	}
	jlongArray ret = env->NewLongArray(size);
	env->SetLongArrayRegion(ret, 0, size, arr);
	free(arr);
	return ret;
}

extern "C" JNIEXPORT jlongArray JNICALL
Java_i2f_natives_windows_NativesWindows_enumChildWindows(
JNIEnv* env,
jobject obj,
jlong hwndParent
) {
	std::list<HWND> result;
	BOOL ok = EnumChildWindows(ptrOf<HWND>(hwndParent), __EnumWindow_Proc, (LPARAM)&result);
	if (ok == FALSE){
		return env->NewLongArray(0);
	}
	int size = result.size();
	jlong* arr = (jlong*)malloc(size*sizeof(jlong));
	for (int i = 0; i < size; i++){
		HWND h = result.front();
		arr[i] = toPtr(h);
		result.pop_front();
	}
	jlongArray ret = env->NewLongArray(size);
	env->SetLongArrayRegion(ret, 0, size, arr);
	free(arr);
	return ret;
}


extern "C" JNIEXPORT jlongArray JNICALL
Java_i2f_natives_windows_NativesWindows_enumThreadWindows(
JNIEnv* env,
jobject obj,
jlong threadId
) {
	std::list<HWND> result;
	BOOL ok = EnumThreadWindows(threadId, __EnumWindow_Proc, (LPARAM)&result);
	if (ok == FALSE){
		return env->NewLongArray(0);
	}
	int size = result.size();
	jlong* arr = (jlong*)malloc(size*sizeof(jlong));
	for (int i = 0; i < size; i++){
		HWND h = result.front();
		arr[i] = toPtr(h);
		result.pop_front();
	}
	jlongArray ret = env->NewLongArray(size);
	env->SetLongArrayRegion(ret, 0, size, arr);
	free(arr);
	return ret;
}


extern "C" JNIEXPORT jlongArray JNICALL
Java_i2f_natives_windows_NativesWindows_enumDesktopWindows(
JNIEnv* env,
jobject obj,
jlong hDesktop
) {
	std::list<HWND> result;
	BOOL ok = EnumDesktopWindows(ptrOf<HDESK>(hDesktop), __EnumWindow_Proc, (LPARAM)&result);
	if (ok == FALSE){
		return env->NewLongArray(0);
	}
	int size = result.size();
	jlong* arr = (jlong*)malloc(size*sizeof(jlong));
	for (int i = 0; i < size; i++){
		HWND h = result.front();
		arr[i] = toPtr(h);
		result.pop_front();
	}
	jlongArray ret = env->NewLongArray(size);
	env->SetLongArrayRegion(ret, 0, size, arr);
	free(arr);
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_i2f_natives_windows_NativesWindows_setForegroundWindow(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	BOOL ret = SetForegroundWindow(ptrOf<HWND>(hwnd));
	return ret == TRUE;
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_getActiveWindow(
JNIEnv* env,
jobject obj
) {
	HWND h = GetActiveWindow();
	return toPtr(h);
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_setActiveWindow(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	HWND h = SetActiveWindow(ptrOf<HWND>(hwnd));
	return toPtr(h);
}

extern "C" JNIEXPORT jboolean JNICALL
Java_i2f_natives_windows_NativesWindows_enableWindow(
JNIEnv* env,
jobject obj,
jlong hwnd,
jboolean enable
) {
	BOOL ret = EnableWindow(ptrOf<HWND>(hwnd), (enable ? TRUE : FALSE));
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_i2f_natives_windows_NativesWindows_isWindowEnabled(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	BOOL ret = IsWindowEnabled(ptrOf<HWND>(hwnd));
	return ret == TRUE;
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_getWindowDC(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	HDC hdc = GetWindowDC(ptrOf<HWND>(hwnd));
	return toPtr(hdc);
}

extern "C" JNIEXPORT jboolean JNICALL
Java_i2f_natives_windows_NativesWindows_isWindowVisible(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	BOOL ret = IsWindowVisible(ptrOf<HWND>(hwnd));
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_i2f_natives_windows_NativesWindows_isWindowUnicode(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	BOOL ret = IsWindowUnicode(ptrOf<HWND>(hwnd));
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_i2f_natives_windows_NativesWindows_isChild(
JNIEnv* env,
jobject obj,
jlong hwndParent,
jlong hwnd
) {
	BOOL ret = IsChild(ptrOf<HWND>(hwndParent), ptrOf<HWND>(hwnd));
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_i2f_natives_windows_NativesWindows_isIconic(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	BOOL ret = IsIconic(ptrOf<HWND>(hwnd));
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_i2f_natives_windows_NativesWindows_openIcon(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	BOOL ret = OpenIcon(ptrOf<HWND>(hwnd));
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_i2f_natives_windows_NativesWindows_isZoomed(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	BOOL ret = IsZoomed(ptrOf<HWND>(hwnd));
	return ret == TRUE;
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_getParent(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	HWND ret = GetParent(ptrOf<HWND>(hwnd));
	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_findWindowEx(
JNIEnv* env,
jobject obj,
jlong hwndParent,
jlong hwndChildAfter,
jstring className,
jstring windowText
) {
	wchar_t* className_ptr = jstring2wchar(env, className);
	wchar_t* windowText_ptr = jstring2wchar(env, windowText);
	HWND h = FindWindowExW(ptrOf<HWND>(hwndParent), ptrOf<HWND>(hwndChildAfter), className_ptr, windowText_ptr);
	freeWchar(className_ptr);
	freeWchar(windowText_ptr);
	return toPtr(h);
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_windowFromDC(
JNIEnv* env,
jobject obj,
jlong hdc
) {
	HWND ret = WindowFromDC(ptrOf<HDC>(hdc));
	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_moveWindow(
JNIEnv* env,
jobject obj,
jlong hwnd,
jint x,
jint y,
jint width,
jint height,
jboolean repaint
) {
	BOOL ret = MoveWindow(ptrOf<HWND>(hwnd), x, y, width, height, (repaint == true ? TRUE : FALSE));
	return ret == TRUE;
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_createToolhelp32Snapshot(
JNIEnv* env,
jobject obj,
jlong dwFlags,
jlong processId
) {
	HANDLE h = CreateToolhelp32Snapshot(dwFlags, processId);
	return toPtr(h);
}

jstring stringify_PROCESSENTRY32W(JNIEnv* env, const PROCESSENTRY32W * pe){
	wchar_t buff[8192] = { 0 };
	_swprintf(buff, L"dwSize:%d;#;cntUsage:%d;#;th32ProcessID:%d;#;th32DefaultHeapID:%d;#;th32ModuleID:%d;#;cntThreads:%d;#;th32ParentProcessID:%d;#;pcPriClassBase:%d;#;dwFlags:%d;#;szExeFile:%s",
		pe->dwSize, pe->cntUsage, pe->th32ProcessID, pe->th32DefaultHeapID, pe->th32ModuleID, pe->cntThreads, pe->th32ParentProcessID, pe->pcPriClassBase, pe->dwFlags, pe->szExeFile
		);
	return wchar2jstring(env, buff);
}

extern "C" JNIEXPORT jstring JNICALL
Java_i2f_natives_windows_NativesWindows_process32First(
JNIEnv* env,
jobject obj,
jlong hSnapshot
) {
	PROCESSENTRY32W pe;
	pe.dwSize = sizeof(PROCESSENTRY32W);
	BOOL ret = Process32FirstW(ptrOf<HANDLE>(hSnapshot), &pe);
	if (ret == FALSE){
		return nullptr;
	}
	return stringify_PROCESSENTRY32W(env, &pe);
}

extern "C" JNIEXPORT jstring JNICALL
Java_i2f_natives_windows_NativesWindows_process32Next(
JNIEnv* env,
jobject obj,
jlong hSnapshot
) {
	PROCESSENTRY32W pe;
	pe.dwSize = sizeof(PROCESSENTRY32W);
	BOOL ret = Process32NextW(ptrOf<HANDLE>(hSnapshot), &pe);
	if (ret == FALSE){
		return nullptr;
	}
	return stringify_PROCESSENTRY32W(env, &pe);
}

jstring stringify_MODULEENTRY32W(JNIEnv* env, const MODULEENTRY32W * pe){
	wchar_t buff[8192] = { 0 };
	_swprintf(buff, L"dwSize:%d;#;th32ModuleID:%d;#;th32ProcessID:%d;#;GlblcntUsage:%d;#;ProccntUsage:%d;#;modBaseSize:%d;#;hModule:%d;#;szModule:%s;#;szExePath:%s",
		pe->dwSize, pe->th32ModuleID, pe->th32ProcessID, pe->GlblcntUsage, pe->ProccntUsage, pe->modBaseSize, pe->hModule, (long)pe->szModule, pe->szExePath
		);
	return wchar2jstring(env, buff);
}

extern "C" JNIEXPORT jstring JNICALL
Java_i2f_natives_windows_NativesWindows_module32First(
JNIEnv* env,
jobject obj,
jlong hSnapshot
) {
	MODULEENTRY32W pe;
	pe.dwSize = sizeof(MODULEENTRY32W);
	BOOL ret = Module32FirstW(ptrOf<HANDLE>(hSnapshot), &pe);
	if (ret == FALSE){
		return nullptr;
	}
	return stringify_MODULEENTRY32W(env, &pe);
}

extern "C" JNIEXPORT jstring JNICALL
Java_i2f_natives_windows_NativesWindows_module32Next(
JNIEnv* env,
jobject obj,
jlong hSnapshot
) {
	MODULEENTRY32W pe;
	pe.dwSize = sizeof(MODULEENTRY32W);
	BOOL ret = Module32NextW(ptrOf<HANDLE>(hSnapshot), &pe);
	if (ret == FALSE){
		return nullptr;
	}
	return stringify_MODULEENTRY32W(env, &pe);
}



jstring stringify_THREADENTRY32(JNIEnv* env, const THREADENTRY32 * pe){
	wchar_t buff[8192] = { 0 };
	_swprintf(buff, L"dwSize:%d;#;cntUsage:%d;#;th32ThreadID:%d;#;th32OwnerProcessID:%d;#;tpBasePri:%d;#;tpDeltaPri:%d;#;dwFlags:%d",
		pe->dwSize, pe->cntUsage, pe->th32ThreadID, pe->th32OwnerProcessID, pe->tpBasePri, pe->tpDeltaPri, pe->dwFlags
		);
	return wchar2jstring(env, buff);
}

extern "C" JNIEXPORT jstring JNICALL
Java_i2f_natives_windows_NativesWindows_thread32First(
JNIEnv* env,
jobject obj,
jlong hSnapshot
) {
	THREADENTRY32 pe;
	pe.dwSize = sizeof(THREADENTRY32);
	BOOL ret = Thread32First(ptrOf<HANDLE>(hSnapshot), &pe);
	if (ret == FALSE){
		return nullptr;
	}
	return stringify_THREADENTRY32(env, &pe);
}

extern "C" JNIEXPORT jstring JNICALL
Java_i2f_natives_windows_NativesWindows_thread32Next(
JNIEnv* env,
jobject obj,
jlong hSnapshot
) {
	THREADENTRY32 pe;
	pe.dwSize = sizeof(THREADENTRY32);
	BOOL ret = Thread32Next(ptrOf<HANDLE>(hSnapshot), &pe);
	if (ret == FALSE){
		return nullptr;
	}
	return stringify_THREADENTRY32(env, &pe);
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_getExitCodeProcess(
JNIEnv* env,
jobject obj,
jlong hProcess
) {
	DWORD code = 0;
	BOOL ret = GetExitCodeProcess(ptrOf<HANDLE>(hProcess), &code);
	return code;
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_getExitCodeThread(
JNIEnv* env,
jobject obj,
jlong hThread
) {
	DWORD code = 0;
	BOOL ret = GetExitCodeThread(ptrOf<HANDLE>(hThread), &code);
	return code;
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_getLastError(
JNIEnv* env,
jobject obj,
jlong hThread
) {
	DWORD ret = GetLastError();
	return ret;
}

extern "C" JNIEXPORT void JNICALL
Java_i2f_natives_windows_NativesWindows_setLastError(
JNIEnv* env,
jobject obj,
jlong errCode
) {
	SetLastError(errCode);
}

extern "C" JNIEXPORT void JNICALL
Java_i2f_natives_windows_NativesWindows_setLastErrorEx(
JNIEnv* env,
jobject obj,
jlong errCode,
jlong type
) {
	SetLastErrorEx(errCode, type);
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_openThread(
JNIEnv* env,
jobject obj,
jlong dwDesiredAccess,
jboolean inheritHandle,
jlong dwThreadId
) {
	HANDLE ret = OpenThread(dwDesiredAccess, (inheritHandle == true ? TRUE : FALSE), dwThreadId);
	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_suspendThread(
JNIEnv* env,
jobject obj,
jlong hThread
) {
	DWORD ret = SuspendThread(ptrOf<HANDLE>(hThread));
	return ret;
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_resumeThread(
JNIEnv* env,
jobject obj,
jlong hThread
) {
	DWORD ret = ResumeThread(ptrOf<HANDLE>(hThread));
	return ret;
}

extern "C" JNIEXPORT jstring JNICALL
Java_i2f_natives_windows_NativesWindows_getLogicalDriveStrings(
JNIEnv* env,
jobject obj
) {
	int len = GetLogicalDriveStringsA(0, NULL);
	char* buff = (char*)malloc(len*sizeof(char));
	GetLogicalDriveStringsA(len, buff);
	std::string str = "";
	std::string tmp = "";
	bool isFirst = true;
	for (int i = 0; i < len; i++){
		if (buff[i] == 0){
			if (tmp.length()>0){
				if (!isFirst){
					str += ";#;";
				}
				str += tmp;
				isFirst = false;
			}
			tmp = "";
		}
		else{
			tmp += buff[i];
		}
	}
	if (tmp.length() > 0){
		if (!isFirst){
			str += ";#;";
		}
		str += tmp;
	}
	return env->NewStringUTF(str.data());
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_getDriveType(
JNIEnv* env,
jobject obj,
jstring rootPathName
) {
	wchar_t* rootPathName_ptr = jstring2wchar(env, rootPathName);
	UINT ret = GetDriveTypeW(rootPathName_ptr);
	freeWchar(rootPathName_ptr);
	return ret;
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_getFileAttributes(
JNIEnv* env,
jobject obj,
jstring fileName
) {
	wchar_t* fileName_ptr = jstring2wchar(env, fileName);
	DWORD ret = GetFileAttributesW(fileName_ptr);
	freeWchar(fileName_ptr);
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_i2f_natives_windows_NativesWindows_setFileAttributes(
JNIEnv* env,
jobject obj,
jstring fileName,
jlong attribute
) {
	wchar_t* fileName_ptr = jstring2wchar(env, fileName);
	BOOL ret = SetFileAttributesW(fileName_ptr, attribute);
	freeWchar(fileName_ptr);
	return ret == TRUE;
}

extern "C" JNIEXPORT jint JNICALL
Java_i2f_natives_windows_NativesWindows_deleteFileToRecycleBin(
JNIEnv* env,
jobject obj,
jstring fileName
) {
	wchar_t* fileName_ptr = jstring2wchar(env, fileName);
	SHFILEOPSTRUCTW op = { 0 };
	op.hwnd = NULL;
	op.wFunc = FO_DELETE;
	op.pFrom = fileName_ptr;
	op.fFlags = FOF_ALLOWUNDO | FOF_NO_UI;
	op.fAnyOperationsAborted = true;
	op.hNameMappings = NULL;
	op.lpszProgressTitle = NULL;
	int ret = SHFileOperationW(&op);
	freeWchar(fileName_ptr);
	return ret;
}


jstring stringify_WINDOWINFO(JNIEnv* env, const WINDOWINFO * pe){
	wchar_t buff[8192] = { 0 };
	_swprintf(buff, L"cbSize:%d;#;rcWindow:%d,%d,%d,%d;#;rcClient:%d,%d,%d,%d;#;dwStyle:%d;#;dwExStyle:%d;#;dwWindowStatus:%d;#;cxWindowBorders:%d;#;cyWindowBorders:%d;#;atomWindowType:%d;#;wCreatorVersion:%d",
		pe->cbSize,
		pe->rcWindow.left, pe->rcWindow.top, pe->rcWindow.right, pe->rcWindow.bottom,
		pe->rcClient.left, pe->rcClient.top, pe->rcClient.right, pe->rcClient.bottom,
		pe->dwStyle, pe->dwExStyle, pe->dwWindowStatus,
		pe->cxWindowBorders, pe->cyWindowBorders,
		pe->atomWindowType, pe->wCreatorVersion
		);
	return wchar2jstring(env, buff);
}

extern "C" JNIEXPORT jstring JNICALL
Java_i2f_natives_windows_NativesWindows_getWindowInfo(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	WINDOWINFO info = { 0 };
	BOOL ret = GetWindowInfo(ptrOf<HWND>(hwnd), &info);
	if (ret == FALSE){
		return nullptr;
	}
	return stringify_WINDOWINFO(env, &info);
}

extern "C" JNIEXPORT jintArray JNICALL
Java_i2f_natives_windows_NativesWindows_clientToScreen(
JNIEnv* env,
jobject obj,
jlong hwnd,
jint x,
jint y
) {
	POINT p = { 0 };
	p.x = x;
	p.y = y;
	BOOL ok = ClientToScreen(ptrOf<HWND>(hwnd), &p);
	if (ok == FALSE){
		return env->NewIntArray(0);
	}
	jint arr[] = { p.x, p.y };
	jintArray ret = env->NewIntArray(2);
	env->SetIntArrayRegion(ret, 0, 2, arr);
	return ret;
}


extern "C" JNIEXPORT jintArray JNICALL
Java_i2f_natives_windows_NativesWindows_screenToClient(
JNIEnv* env,
jobject obj,
jlong hwnd,
jint x,
jint y
) {
	POINT p = { 0 };
	p.x = x;
	p.y = y;
	BOOL ok = ScreenToClient(ptrOf<HWND>(hwnd), &p);
	if (ok == FALSE){
		return env->NewIntArray(0);
	}
	jint arr[] = { p.x, p.y };
	jintArray ret = env->NewIntArray(2);
	env->SetIntArrayRegion(ret, 0, 2, arr);
	return ret;
}

extern "C" JNIEXPORT void JNICALL
Java_i2f_natives_windows_NativesWindows_keyboardEvent(
JNIEnv* env,
jobject obj,
jint bVk,
jint bScan,
jlong dwFlags
) {
	keybd_event(bVk, bScan, dwFlags, 0);
}

extern "C" JNIEXPORT void JNICALL
Java_i2f_natives_windows_NativesWindows_mouseEvent(
JNIEnv* env,
jobject obj,
jlong dwFlags,
jint dx,
jint dy,
jint dwData
) {
	mouse_event(dwFlags, dx, dy, dwData, 0);
}

extern "C" JNIEXPORT jboolean JNICALL
Java_i2f_natives_windows_NativesWindows_setCursorPos(
JNIEnv* env,
jobject obj,
jint x,
jint y
) {
	BOOL ret = SetCursorPos(x, y);
	return ret == TRUE;
}

extern "C" JNIEXPORT jlong JNICALL
Java_i2f_natives_windows_NativesWindows_regOpenKeyEx(
JNIEnv* env,
jobject obj,
jlong hkey,
jstring subKey,
jlong ulOptions,
jlong samDesired
) {
	wchar_t* subKey_ptr = jstring2wchar(env, subKey);
	HKEY key;
	LSTATUS ret = RegOpenKeyExW(ptrOf<HKEY>(hkey), subKey_ptr, ulOptions, samDesired, &key);
	freeWchar(subKey_ptr);
	if (ret != ERROR_SUCCESS){
		return 0;
	}
	return toPtr(key);
}


extern "C" JNIEXPORT jstring JNICALL
Java_i2f_natives_windows_NativesWindows_regEnumValue(
JNIEnv* env,
jobject obj,
jlong hkey,
jint index
) {
	wchar_t szValueName[MAXBYTE] = { 0 };
	DWORD chValueName = MAXBYTE;
	DWORD reserved = 0;
	DWORD type = 0;
	unsigned char data[MAXBYTE] = { 0 };
	DWORD cbData = MAXBYTE;


	LSTATUS ret = RegEnumValueW(ptrOf<HKEY>(hkey), index, szValueName, &chValueName,&reserved, &type, data, &cbData);
	
	if (ret != ERROR_SUCCESS){
		return nullptr;
	}
	wchar_t buff[MAXBYTE] = { 0 };
	if (type == REG_SZ || type == REG_EXPAND_SZ || type == REG_MULTI_SZ){
		// string
		if (type == REG_MULTI_SZ){
			jstring jstr = env->NewStringUTF((const char *)data);
			wchar_t* bts = jstring2wchar(env, jstr);
			swprintf(buff, L"index:%d;#;szValueName:%s;#;reserved:%d;#;type:%d;#;data:%s", index, szValueName, reserved, type, data);
			freeWchar(bts);
		}
		else{
			swprintf(buff, L"index:%d;#;szValueName:%s;#;reserved:%d;#;type:%d;#;data:%s", index, szValueName, reserved, type, (const wchar_t *)data);
		}
	}
	else if (type == REG_DWORD || type == REG_DWORD_BIG_ENDIAN || type == REG_DWORD_LITTLE_ENDIAN){
		// int
		DWORD* ptr = (DWORD*)data;
		if (type == REG_DWORD){
			swprintf(buff, L"index:%d;#;szValueName:%s;#;reserved:%d;#;type:%d;#;data:%d", index, szValueName, reserved, type, ptr[0]);
		}
		else if (type == REG_DWORD_BIG_ENDIAN){
			DWORD wd = 0;
			for (int i = 0; i < cbData; i++){
				wd <<= 8;
				wd |= data[i];
			}
			swprintf(buff, L"index:%d;#;szValueName:%s;#;reserved:%d;#;type:%d;#;data:%d", index, szValueName, reserved, type, wd);
		}
		else if (type == REG_DWORD_LITTLE_ENDIAN){
			DWORD wd = 0;
			for (int i = cbData-1; i >=0; i--){
				wd <<= 8;
				wd |= data[i];
			}
			swprintf(buff, L"index:%d;#;szValueName:%s;#;reserved:%d;#;type:%d;#;data:%d", index, szValueName, reserved, type, wd);
		}
		
	}
	else if (type == REG_QWORD || type == REG_QWORD_LITTLE_ENDIAN){
		// long
		unsigned long long * ptr = (unsigned long long*)data;
		if (type == REG_QWORD){
			swprintf(buff, L"index:%d;#;szValueName:%s;#;reserved:%d;#;type:%d;#;data:%l64d", index, szValueName, reserved, type, ptr[0]);
		}
		else if (type == REG_QWORD_LITTLE_ENDIAN){
			unsigned long long wd = 0;
			for (int i = cbData - 1; i >= 0; i--){
				wd <<= 8;
				wd |= data[i];
			}
			swprintf(buff, L"index:%d;#;szValueName:%s;#;reserved:%d;#;type:%d;#;data:%l64d", index, szValueName, reserved, type, wd);
		}
	}
	else{
		swprintf(buff, L"index:%d;#;szValueName:%s;#;reserved:%d;#;type:%d;#;data:b",index, szValueName, reserved, type);

		wchar_t tmp[100] = { 0 };
		for (int i = 0; i < cbData; i++){
			ZeroMemory(tmp, sizeof(tmp));
			swprintf(tmp, L"%02x", (int)data[i]);
		}
		StrCatW(buff, tmp);

	}
	return wchar2jstring(env, buff);
}

extern "C" JNIEXPORT jboolean JNICALL
Java_i2f_natives_windows_NativesWindows_regCloseKey(
JNIEnv* env,
jobject obj,
jlong hkey
) {
	LSTATUS ret=RegCloseKey(ptrOf<HKEY>(hkey));
	return ret == ERROR_SUCCESS;
}