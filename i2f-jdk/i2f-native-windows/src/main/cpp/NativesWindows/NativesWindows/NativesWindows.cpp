#pragma once
// Windows 头文件: 
#include <windows.h>
#include <jni.h>
#include <string>
#include<string.h>
#include<list>
#include<stdio.h>
#include<conio.h>
#include<stdlib.h>
// GET_X_LPARAM 依赖
#include<windowsx.h>
#include<map>

#define JNI_METHOD(name) Java_i2f_natives_windows_NativesWindows_##name

//枚举进程依赖
#include<TlHelp32.h>
//回收站依赖
#include <Shlwapi.h>
#pragma comment (lib,"Shlwapi.lib")
// 服务管理依赖
#include<winnt.h>//NT 系统支持
#include<winsvc.h>//service 支持
// shell依赖
#include<ShellAPI.h>
#include <ShlObj.h>
#pragma comment (lib,"shell32.lib")
// msci
#include<mmsystem.h>
#pragma comment(lib,"winmm.lib")
// gdi 附加依赖
#pragma comment(lib,"msimg32.lib")


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
	std::string hello = "Hello, Windows API for JNI!";

	//直接返回了一个字符串，java字符串中构造就有一个以字节数组构造的
	//这里就是字节数组构造形式
	return env->NewStringUTF(hello.c_str());
}


extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(kbHit)(
JNIEnv* env,
jobject obj
){
	return 0 != kbhit();
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getCh)(
JNIEnv* env,
jobject obj
){
	return getch();
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(flushStdin)(
JNIEnv* env,
jobject obj
){
	fflush(stdin);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(flushStdout)(
JNIEnv* env,
jobject obj
){
	fflush(stdout);
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(rgb)(
JNIEnv* env,
jobject obj,
jint r,
jint g,
jint b
){
	return RGB(r, g, b);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(sleep)(
JNIEnv* env,
jobject obj,
jint millSeconds
){
	Sleep(millSeconds);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(system)(
JNIEnv* env,
jobject obj,
jstring cmd
){
	const char * cmd_ptr = env->GetStringUTFChars(cmd, NULL);
	system(cmd_ptr);
	env->ReleaseStringUTFChars(cmd, cmd_ptr);
}


extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(envStringToWcharPtr)(
JNIEnv* env,
jobject obj,
jstring str
){
	wchar_t* ptr = jstring2wchar(env, str);
	return toPtr(ptr);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(envFreeWcharPtr)(
JNIEnv* env,
jobject obj,
jlong ptr
){
	freeWchar((wchar_t*)ptr);
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getSystemMetrics)(
JNIEnv* env,
jobject obj,
jint metric
) {
	return GetSystemMetrics(metric);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(getDC)(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	return toPtr(GetDC(ptrOf<HWND>(hwnd)));
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(releaseDC)(
JNIEnv* env,
jobject obj,
jlong hwnd,
jlong hdc
) {
	long long ptr = (long long)hwnd;
	return ReleaseDC(ptrOf<HWND>(hwnd), ptrOf<HDC>(hwnd));
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getDeviceCaps)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint index
) {
	long long ptr = (long long)hdc;
	return GetDeviceCaps(ptrOf<HDC>(hdc), index);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(getForegroundWindow)(
JNIEnv* env,
jobject obj
) {
	return toPtr(GetForegroundWindow());
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(getDesktopWindow)(
JNIEnv* env,
jobject obj
) {
	return toPtr(GetDesktopWindow());
}

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(getWindowText)(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	wchar_t buff[4096] = { 0 };
	int len = GetWindowTextW(ptrOf<HWND>(hwnd), buff, 4096);
	return wchar2jstring(env, buff);
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(setWindowText)(
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
JNI_METHOD(windowFromPoint)(
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
JNI_METHOD(getCursorPos)(
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
JNI_METHOD(messageBox)(
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
JNI_METHOD(messageBeep)(
JNIEnv* env,
jobject obj,
jint type
) {
	BOOL ret = MessageBeep(type);
	return ret == TRUE;
}

extern "C" JNIEXPORT jintArray JNICALL
JNI_METHOD(getWindowRect)(
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
JNI_METHOD(setWindowPos)(
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
JNI_METHOD(showWindow)(
JNIEnv* env,
jobject obj,
jlong hwnd,
int cmdShow
) {
	BOOL ret = ShowWindow(ptrOf<HWND>(hwnd), cmdShow);
	return ret == TRUE;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(setParent)(
JNIEnv* env,
jobject obj,
jlong hwndChild,
jlong hwndNewParent
) {
	HWND ret = SetParent(ptrOf<HWND>(hwndChild), ptrOf<HWND>(hwndNewParent));
	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(findWindow)(
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
JNI_METHOD(getWindow)(
JNIEnv* env,
jobject obj,
jlong hwnd,
jint cmd
) {
	HWND ret = GetWindow(ptrOf<HWND>(hwnd), cmd);
	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(sendMessage)(
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
JNI_METHOD(postMessage)(
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
JNI_METHOD(getWindowThreadProcessId)(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	DWORD processId;
	GetWindowThreadProcessId(ptrOf<HWND>(hwnd), &processId);
	return processId;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(openProcess)(
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
JNI_METHOD(isInvalidHandle)(
JNIEnv* env,
jobject obj,
jlong handle
) {
	return ptrOf<HANDLE>(handle) == INVALID_HANDLE_VALUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(terminateProcess)(
JNIEnv* env,
jobject obj,
jlong hProcess,
jint uExitCode
) {
	BOOL ret = TerminateProcess(ptrOf<HANDLE>(hProcess), uExitCode);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(closeHandle)(
JNIEnv* env,
jobject obj,
jlong hObject
) {
	BOOL ret = CloseHandle(ptrOf<HANDLE>(hObject));
	return ret == TRUE;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(winExec)(
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
JNI_METHOD(getNextWindow)(
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
JNI_METHOD(getClassName)(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	wchar_t buff[4096] = { 0 };
	int len = GetClassNameW(ptrOf<HWND>(hwnd), buff, 4096);
	return wchar2jstring(env, buff);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(getCurrentProcess)(
JNIEnv* env,
jobject obj
) {
	HANDLE handle = GetCurrentProcess();
	return toPtr(handle);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(getCurrentProcessId)(
JNIEnv* env,
jobject obj
) {
	DWORD ret = GetCurrentProcessId();
	return ret;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(openProcessToken)(
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
JNI_METHOD(lookupPrivilegeValue)(
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
JNI_METHOD(adjustTokenPrivileges)(
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
JNI_METHOD(adjustProcessPrivileges)(
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
JNI_METHOD(getWindowLong)(
JNIEnv* env,
jobject obj,
jlong hwnd,
jint index
) {
	LONG ret = GetWindowLongW(ptrOf<HWND>(hwnd), index);
	return ret;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(getClassLong)(
JNIEnv* env,
jobject obj,
jlong hwnd,
jint index
) {
	LONG ret = GetClassLongW(ptrOf<HWND>(hwnd), index);
	return ret;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(setWindowLong)(
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
JNI_METHOD(setClassLong)(
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
JNI_METHOD(getLayeredWindowAttributes)(
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
JNI_METHOD(setLayeredWindowAttributes)(
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
JNI_METHOD(enumWindows)(
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
JNI_METHOD(enumChildWindows)(
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
JNI_METHOD(enumThreadWindows)(
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
JNI_METHOD(enumDesktopWindows)(
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
JNI_METHOD(setForegroundWindow)(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	BOOL ret = SetForegroundWindow(ptrOf<HWND>(hwnd));
	return ret == TRUE;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(getActiveWindow)(
JNIEnv* env,
jobject obj
) {
	HWND h = GetActiveWindow();
	return toPtr(h);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(setActiveWindow)(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	HWND h = SetActiveWindow(ptrOf<HWND>(hwnd));
	return toPtr(h);
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(enableWindow)(
JNIEnv* env,
jobject obj,
jlong hwnd,
jboolean enable
) {
	BOOL ret = EnableWindow(ptrOf<HWND>(hwnd), (enable ? TRUE : FALSE));
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(isWindowEnabled)(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	BOOL ret = IsWindowEnabled(ptrOf<HWND>(hwnd));
	return ret == TRUE;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(getWindowDC)(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	HDC hdc = GetWindowDC(ptrOf<HWND>(hwnd));
	return toPtr(hdc);
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(isWindowVisible)(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	BOOL ret = IsWindowVisible(ptrOf<HWND>(hwnd));
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(isWindowUnicode)(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	BOOL ret = IsWindowUnicode(ptrOf<HWND>(hwnd));
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(isChild)(
JNIEnv* env,
jobject obj,
jlong hwndParent,
jlong hwnd
) {
	BOOL ret = IsChild(ptrOf<HWND>(hwndParent), ptrOf<HWND>(hwnd));
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(isIconic)(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	BOOL ret = IsIconic(ptrOf<HWND>(hwnd));
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(openIcon)(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	BOOL ret = OpenIcon(ptrOf<HWND>(hwnd));
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(isZoomed)(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	BOOL ret = IsZoomed(ptrOf<HWND>(hwnd));
	return ret == TRUE;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(getParent)(
JNIEnv* env,
jobject obj,
jlong hwnd
) {
	HWND ret = GetParent(ptrOf<HWND>(hwnd));
	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(findWindowEx)(
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
JNI_METHOD(windowFromDC)(
JNIEnv* env,
jobject obj,
jlong hdc
) {
	HWND ret = WindowFromDC(ptrOf<HDC>(hdc));
	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(moveWindow)(
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
JNI_METHOD(createToolhelp32Snapshot)(
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
JNI_METHOD(process32First)(
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
JNI_METHOD(process32Next)(
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
JNI_METHOD(module32First)(
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
JNI_METHOD(module32Next)(
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
JNI_METHOD(thread32First)(
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
JNI_METHOD(thread32Next)(
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
JNI_METHOD(getExitCodeProcess)(
JNIEnv* env,
jobject obj,
jlong hProcess
) {
	DWORD code = 0;
	BOOL ret = GetExitCodeProcess(ptrOf<HANDLE>(hProcess), &code);
	return code;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(getExitCodeThread)(
JNIEnv* env,
jobject obj,
jlong hThread
) {
	DWORD code = 0;
	BOOL ret = GetExitCodeThread(ptrOf<HANDLE>(hThread), &code);
	return code;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(getLastError)(
JNIEnv* env,
jobject obj,
jlong hThread
) {
	DWORD ret = GetLastError();
	return ret;
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(setLastError)(
JNIEnv* env,
jobject obj,
jlong errCode
) {
	SetLastError(errCode);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(setLastErrorEx)(
JNIEnv* env,
jobject obj,
jlong errCode,
jlong type
) {
	SetLastErrorEx(errCode, type);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(openThread)(
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
JNI_METHOD(suspendThread)(
JNIEnv* env,
jobject obj,
jlong hThread
) {
	DWORD ret = SuspendThread(ptrOf<HANDLE>(hThread));
	return ret;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(resumeThread)(
JNIEnv* env,
jobject obj,
jlong hThread
) {
	DWORD ret = ResumeThread(ptrOf<HANDLE>(hThread));
	return ret;
}

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(getLogicalDriveStrings)(
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
JNI_METHOD(getDriveType)(
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
JNI_METHOD(getFileAttributes)(
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
JNI_METHOD(setFileAttributes)(
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
JNI_METHOD(deleteFileToRecycleBin)(
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
JNI_METHOD(getWindowInfo)(
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
JNI_METHOD(clientToScreen)(
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
JNI_METHOD(screenToClient)(
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
JNI_METHOD(keyboardEvent)(
JNIEnv* env,
jobject obj,
jint bVk,
jint bScan,
jlong dwFlags
) {
	keybd_event(bVk, bScan, dwFlags, 0);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(mouseEvent)(
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
JNI_METHOD(setCursorPos)(
JNIEnv* env,
jobject obj,
jint x,
jint y
) {
	BOOL ret = SetCursorPos(x, y);
	return ret == TRUE;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(regOpenKeyEx)(
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
JNI_METHOD(regEnumValue)(
JNIEnv* env,
jobject obj,
jlong hkey,
jint index
) {
	wchar_t szValueName[MAXBYTE] = { 0 };
	DWORD chValueName = MAXBYTE;
	DWORD type = 0;
	unsigned char data[MAXBYTE] = { 0 };
	DWORD cbData = MAXBYTE;


	LSTATUS ret = RegEnumValueW(ptrOf<HKEY>(hkey), index, szValueName, &chValueName, NULL, &type, data, &cbData);

	if (ret != ERROR_SUCCESS){
		return nullptr;
	}
	wchar_t buff[MAXBYTE] = { 0 };
	if (type == REG_SZ || type == REG_EXPAND_SZ || type == REG_MULTI_SZ){
		// string
		if (type == REG_MULTI_SZ){
			jstring jstr = env->NewStringUTF((const char *)data);
			wchar_t* bts = jstring2wchar(env, jstr);
			swprintf(buff, L"index:%d;#;valueName:%s;#;type:%d;#;valueData:%s", index, szValueName, type, data);
			freeWchar(bts);
		}
		else{
			swprintf(buff, L"index:%d;#;valueName:%s;#;type:%d;#;valueData:%s", index, szValueName, type, (const wchar_t *)data);
		}
	}
	else if (type == REG_DWORD || type == REG_DWORD_BIG_ENDIAN || type == REG_DWORD_LITTLE_ENDIAN){
		// int
		DWORD* ptr = (DWORD*)data;
		if (type == REG_DWORD){
			swprintf(buff, L"index:%d;#;valueName:%s;#;type:%d;#;valueData:%d", index, szValueName, type, ptr[0]);
		}
		else if (type == REG_DWORD_BIG_ENDIAN){
			DWORD wd = 0;
			for (int i = 0; i < cbData; i++){
				wd <<= 8;
				wd |= data[i];
			}
			swprintf(buff, L"index:%d;#;valueName:%s;#;type:%d;#;valueData:%d", index, szValueName, type, wd);
		}
		else if (type == REG_DWORD_LITTLE_ENDIAN){
			DWORD wd = 0;
			for (int i = cbData - 1; i >= 0; i--){
				wd <<= 8;
				wd |= data[i];
			}
			swprintf(buff, L"index:%d;#;valueName:%s;#;type:%d;#;valueData:%d", index, szValueName, type, wd);
		}

	}
	else if (type == REG_QWORD || type == REG_QWORD_LITTLE_ENDIAN){
		// long
		unsigned long long * ptr = (unsigned long long*)data;
		if (type == REG_QWORD){
			swprintf(buff, L"index:%d;#;valueName:%s;#;type:%d;#;valueData:%lld", index, szValueName, type, ptr[0]);
		}
		else if (type == REG_QWORD_LITTLE_ENDIAN){
			unsigned long long wd = 0;
			for (int i = cbData - 1; i >= 0; i--){
				wd <<= 8;
				wd |= data[i];
			}
			swprintf(buff, L"index:%d;#;valueName:%s;#;type:%d;#;valueData:%lld", index, szValueName, type, wd);
		}
	}
	else{
		swprintf(buff, L"index:%d;#;valueName:%s;#;type:%d;#;valueData:b", index, szValueName, type);

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
JNI_METHOD(regCloseKey)(
JNIEnv* env,
jobject obj,
jlong hkey
) {
	LSTATUS ret = RegCloseKey(ptrOf<HKEY>(hkey));
	return ret == ERROR_SUCCESS;
}

void filetime2timet(const FILETIME* ft, time_t * tt){
	ULARGE_INTEGER ui;
	ui.LowPart = ft->dwLowDateTime;
	ui.HighPart = ft->dwHighDateTime;
	*tt = ((long long)(ui.QuadPart - 116444736000000000) / 10000000);
}

void timet2filetime(const time_t * tt, FILETIME * ft){
	ULARGE_INTEGER ui;
	ui.QuadPart = (ULONGLONG)(((ULONGLONG)(*tt) * 10000000) + 116444736000000000);
	ft->dwLowDateTime = ui.LowPart;
	ft->dwHighDateTime = ui.HighPart;
}

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(regEnumKeyEx)(
JNIEnv* env,
jobject obj,
jlong hkey,
jint index
) {
	wchar_t szKeyName[MAXBYTE] = { 0 };
	DWORD chKeyName = MAXBYTE;
	wchar_t szClassName[MAXBYTE] = { 0 };
	DWORD chClassName = MAXBYTE;

	FILETIME lastWriteTime;

	LSTATUS ret = RegEnumKeyExW(ptrOf<HKEY>(hkey), index, szKeyName, &chKeyName, NULL, szClassName, &chClassName, &lastWriteTime);

	if (ret != ERROR_SUCCESS){
		return nullptr;
	}

	time_t tt;
	filetime2timet(&lastWriteTime, &tt);

	wchar_t buff[MAXBYTE] = { 0 };
	swprintf(buff, L"index:%d;#;keyName:%s;#;className:%s;#;lastWriteTime:%lld", index, szKeyName, szClassName, tt);

	return wchar2jstring(env, buff);
}


extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(regCreateKeyEx)(
JNIEnv* env,
jobject obj,
jlong hkey,
jstring subKey,
jstring className,
jlong dwOptions,
jlong samDesired
) {
	wchar_t* subKey_ptr = jstring2wchar(env, subKey);
	wchar_t* className_ptr = jstring2wchar(env, className);
	HKEY result = NULL;
	LSTATUS ret = RegCreateKeyExW(ptrOf<HKEY>(hkey), subKey_ptr, NULL, className_ptr, dwOptions, samDesired, NULL, &result, NULL);
	if (ret != ERROR_SUCCESS){
		return 0;
	}
	freeWchar(subKey_ptr);
	freeWchar(className_ptr);
	return toPtr(result);
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(regDeleteKey)(
JNIEnv* env,
jobject obj,
jlong hkey,
jstring subKey
) {
	wchar_t* subKey_ptr = jstring2wchar(env, subKey);
	LSTATUS ret = RegDeleteKeyW(ptrOf<HKEY>(hkey), subKey_ptr);
	freeWchar(subKey_ptr);
	return ret == ERROR_SUCCESS;
}

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(regQueryValueEx)(
JNIEnv* env,
jobject obj,
jlong hkey,
jstring valueName
) {
	wchar_t* valueName_ptr = jstring2wchar(env, valueName);

	DWORD type = 0;
	unsigned char data[MAXBYTE] = { 0 };
	DWORD cbData = MAXBYTE;
	LSTATUS ret = RegQueryValueExW(ptrOf<HKEY>(hkey), valueName_ptr, NULL, &type, data, &cbData);
	freeWchar(valueName_ptr);
	wchar_t buff[MAXBYTE] = { 0 };
	if (type == REG_SZ || type == REG_EXPAND_SZ || type == REG_MULTI_SZ){
		// string
		if (type == REG_MULTI_SZ){
			jstring jstr = env->NewStringUTF((const char *)data);
			wchar_t* bts = jstring2wchar(env, jstr);
			swprintf(buff, L"#;type:%d;#;valueData:%s", type, data);
			freeWchar(bts);
		}
		else{
			swprintf(buff, L"type:%d;#;valueData:%s", type, (const wchar_t *)data);
		}
	}
	else if (type == REG_DWORD || type == REG_DWORD_BIG_ENDIAN || type == REG_DWORD_LITTLE_ENDIAN){
		// int
		DWORD* ptr = (DWORD*)data;
		if (type == REG_DWORD){
			swprintf(buff, L"type:%d;#;valueData:%d", type, ptr[0]);
		}
		else if (type == REG_DWORD_BIG_ENDIAN){
			DWORD wd = 0;
			for (int i = 0; i < cbData; i++){
				wd <<= 8;
				wd |= data[i];
			}
			swprintf(buff, L"type:%d;#;valueData:%d", type, wd);
		}
		else if (type == REG_DWORD_LITTLE_ENDIAN){
			DWORD wd = 0;
			for (int i = cbData - 1; i >= 0; i--){
				wd <<= 8;
				wd |= data[i];
			}
			swprintf(buff, L"type:%d;#;valueData:%d", type, wd);
		}

	}
	else if (type == REG_QWORD || type == REG_QWORD_LITTLE_ENDIAN){
		// long
		unsigned long long * ptr = (unsigned long long*)data;
		if (type == REG_QWORD){
			swprintf(buff, L"type:%d;#;valueData:%lld", type, ptr[0]);
		}
		else if (type == REG_QWORD_LITTLE_ENDIAN){
			unsigned long long wd = 0;
			for (int i = cbData - 1; i >= 0; i--){
				wd <<= 8;
				wd |= data[i];
			}
			swprintf(buff, L"type:%d;#;valueData:%lld", type, wd);
		}
	}
	else{
		swprintf(buff, L"type:%d;#;valueData:b", type);

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
JNI_METHOD(regSetValueEx)(
JNIEnv* env,
jobject obj,
jlong hkey,
jstring valueName,
jlong type,
jstring data
) {
	wchar_t* valueName_ptr = jstring2wchar(env, valueName);


	unsigned char buff[MAXBYTE];
	DWORD size = 0;

	if (type == REG_SZ || type == REG_EXPAND_SZ || type == REG_MULTI_SZ){
		wchar_t* data_ptr = jstring2wchar(env, data);
		// string
		if (type == REG_MULTI_SZ){
			const char * bts = env->GetStringUTFChars(data, NULL);
			strcpy((char *)buff, bts);
			size = strlen((char *)buff);
			env->ReleaseStringUTFChars(data, bts);
		}
		else{
			size = lstrlenW(data_ptr)*sizeof(wchar_t);
			lstrcpyW((wchar_t*)buff, data_ptr);
		}
		freeWchar(data_ptr);
	}
	else if (type == REG_DWORD || type == REG_DWORD_BIG_ENDIAN || type == REG_DWORD_LITTLE_ENDIAN){
		const char * bts = env->GetStringUTFChars(data, NULL);
		long long num = _atoi64(bts);
		size = sizeof(DWORD);
		// int
		if (type == REG_DWORD){
			((DWORD *)buff)[0] = (DWORD)num;
		}
		else if (type == REG_DWORD_BIG_ENDIAN){
			DWORD p = (DWORD)num;
			for (int i = size - 1; i >= 0; i++){
				buff[i] = (p & 0x0ff);
				p >>= 8;
			}
		}
		else if (type == REG_DWORD_LITTLE_ENDIAN){
			DWORD p = (DWORD)num;
			for (int i = 0; i < size; i++){
				buff[i] = (p & 0x0ff);
				p >>= 8;
			}
		}
		env->ReleaseStringUTFChars(data, bts);
	}
	else if (type == REG_QWORD || type == REG_QWORD_LITTLE_ENDIAN){

		// long
		const char * bts = env->GetStringUTFChars(data, NULL);
		long long num = _atoi64(bts);
		size = sizeof(unsigned long long);
		if (type == REG_QWORD){
			((unsigned long long *)buff)[0] = (unsigned long long)num;
		}
		else if (type == REG_QWORD_LITTLE_ENDIAN){
			unsigned long long p = (unsigned long long)num;
			for (int i = size - 1; i >= 0; i++){
				buff[i] = (p & 0x0ff);
				p >>= 8;
			}
		}
		env->ReleaseStringUTFChars(data, bts);
	}
	else{
		const char * bts = env->GetStringUTFChars(data, NULL);
		int len = strlen(bts);
		const char * ptr = bts;
		if (len % 2 != 0){
			ptr = &(bts[1]);
			len--;
		}
		size = 0;
		char arr[3] = { 0 };
		for (int i = 0; i + 1 < len; i += 2){
			memset(arr, 0, 3);
			arr[0] = ptr[i];
			arr[1] = ptr[i + 1];
			int v = atoi(arr);
			buff[size] = (unsigned char)v;
			size++;
		}

		env->ReleaseStringUTFChars(data, bts);
	}

	LSTATUS ret = RegSetValueExW(ptrOf<HKEY>(hkey), valueName_ptr, NULL, type, buff, size);
	freeWchar(valueName_ptr);

	return ret == ERROR_SUCCESS;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(regDeleteValue)(
JNIEnv* env,
jobject obj,
jlong hkey,
jstring valueName
) {
	wchar_t* valueName_ptr = jstring2wchar(env, valueName);
	LSTATUS ret = RegDeleteValueW(ptrOf<HKEY>(hkey), valueName_ptr);
	freeWchar(valueName_ptr);

	return ret == ERROR_SUCCESS;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(openSCManager)(
JNIEnv* env,
jobject obj,
jstring machineName,
jstring databaseName,
jlong dwDesiredAccess
) {
	wchar_t* machineName_ptr = jstring2wchar(env, machineName);
	wchar_t* databaseName_ptr = jstring2wchar(env, databaseName);
	SC_HANDLE ret = OpenSCManagerW(machineName_ptr, databaseName_ptr, dwDesiredAccess);
	freeWchar(machineName_ptr);
	freeWchar(databaseName_ptr);

	return toPtr(ret);
}

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(enumServicesStatus)(
JNIEnv* env,
jobject obj,
jlong hScm,
jlong serviceType,
jlong serviceState
) {
	//查询服务数量和数据量大小，使用一些错误参数获取的技巧
	DWORD serviceCount = 0;
	DWORD dwSize = 0;
	BOOL ret = EnumServicesStatusW(ptrOf<SC_HANDLE>(hScm), serviceType, serviceState, NULL, 0, &dwSize, &serviceCount, NULL);
	//定义接受数据的指针
	LPENUM_SERVICE_STATUS lpInfo;
	//由于上面的查询，因为没有给定接受缓冲区，调用失败ERROR_MORE_DATA，需要更大的缓冲区
	if (!ret && GetLastError() == ERROR_MORE_DATA)
	{
		//申请匹配空间
		lpInfo = (LPENUM_SERVICE_STATUS)(new BYTE[dwSize]);
		//获取服务数据并保存
		ret = EnumServicesStatusW(ptrOf<SC_HANDLE>(hScm), serviceType, serviceState, (LPENUM_SERVICE_STATUS)lpInfo, dwSize, &dwSize, &serviceCount, NULL);
		if (!ret)
		{
			//服务数据获取失败则关闭服务句柄
			return nullptr;
		}
		wchar_t* buff = (wchar_t*)malloc(sizeof(wchar_t)*(64 * 1024 * 1024));
		ZeroMemory(buff, sizeof(wchar_t)*(64 * 1024 * 1024));
		wchar_t line[2048] = { 0 };
		//遍历获得的服务，进行填充到列表
		for (DWORD i = 0; i < serviceCount; i++)
		{
			ZeroMemory(line, sizeof(line));
			if (i>0){
				StrCatW(buff, L";$;");
			}
			swprintf(line, L"serviceName:%s;#;displayName:%s;#;currentState:%d;#;serviceType:%d;#;controlsAccepted:%d;#;win32ExitCode:%d;#;serviceSpecificExitCode:%d;#;checkPoint:%d;#;waitHint:%d", lpInfo[i].lpServiceName, lpInfo[i].lpDisplayName, lpInfo[i].ServiceStatus.dwCurrentState,
				lpInfo[i].ServiceStatus.dwServiceType,
				lpInfo[i].ServiceStatus.dwControlsAccepted,
				lpInfo[i].ServiceStatus.dwWin32ExitCode,
				lpInfo[i].ServiceStatus.dwServiceSpecificExitCode,
				lpInfo[i].ServiceStatus.dwCheckPoint,
				lpInfo[i].ServiceStatus.dwWaitHint
				);
			StrCatW(buff, line);
		}
		delete lpInfo;

		jstring str = wchar2jstring(env, buff);

		freeWchar(buff);
		return str;
	}
	return nullptr;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(closeServiceHandle)(
JNIEnv* env,
jobject obj,
jlong hScm
) {
	BOOL ret = CloseServiceHandle(ptrOf<SC_HANDLE>(hScm));
	return ret == TRUE;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(openService)(
JNIEnv* env,
jobject obj,
jlong hScm,
jstring serviceName,
jlong dwDesiredAccess
) {
	wchar_t* serviceName_ptr = jstring2wchar(env, serviceName);
	SC_HANDLE ret = OpenServiceW(ptrOf<SC_HANDLE>(hScm), serviceName_ptr, dwDesiredAccess);
	freeWchar(serviceName_ptr);
	return toPtr(ret);
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(startService)(
JNIEnv* env,
jobject obj,
jlong hService,
jint dwNumServiceArgs,
jstring serviceArgVectors
) {
	wchar_t* serviceArgVectors_ptr = jstring2wchar(env, serviceArgVectors);
	wchar_t** vectors = NULL;
	if (dwNumServiceArgs > 0 && serviceArgVectors != nullptr){
		vectors = (wchar_t**)malloc(sizeof(wchar_t*)*dwNumServiceArgs);
		for (int p = 0; p < dwNumServiceArgs; p++){
			vectors[p] = (wchar_t*)malloc(sizeof(wchar_t)* 2048);
			ZeroMemory(vectors[p], sizeof(wchar_t)* 2048);
		}
		wchar_t buff[2048] = { 0 };
		int i = 0;
		int j = 0;
		int k = 0;
		while (true){
			if (serviceArgVectors_ptr[i] == 0){
				break;
			}
			if (serviceArgVectors_ptr[i] == '\n'){
				lstrcpyW(vectors[j], buff);
				ZeroMemory(buff, sizeof(buff));
				k = 0;
				j++;
				if (j >= dwNumServiceArgs){
					break;
				}
			}
			else{
				buff[k] = serviceArgVectors_ptr[i];
				k++;
			}
			i++;

		}
	}
	BOOL ret = StartServiceW(ptrOf<SC_HANDLE>(hService), dwNumServiceArgs, (LPCWSTR*)vectors);
	freeWchar(serviceArgVectors_ptr);
	if (vectors != NULL){
		for (int p = 0; p < dwNumServiceArgs; p++){
			free(vectors[p]);
		}
		free(vectors);
	}
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(controlService)(
JNIEnv* env,
jobject obj,
jlong hService,
jlong dwControl
) {
	SERVICE_STATUS serviceState;
	BOOL ret = ControlService(ptrOf<SC_HANDLE>(hService), dwControl, &serviceState);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(deleteService)(
JNIEnv* env,
jobject obj,
jlong hService
) {
	BOOL ret = DeleteService(ptrOf<SC_HANDLE>(hService));
	return ret == TRUE;
}

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(queryServiceStatus)(
JNIEnv* env,
jobject obj,
jlong hService
) {
	SERVICE_STATUS serviceState;
	BOOL ret = QueryServiceStatus(ptrOf<SC_HANDLE>(hService), &serviceState);
	if (ret == FALSE){
		return nullptr;
	}
	wchar_t buff[1024] = { 0 };
	swprintf(buff, L"currentState:%d;#;serviceType:%d;#;controlsAccepted:%d;#;win32ExitCode:%d;#;serviceSpecificExitCode:%d;#;checkPoint:%d;#;waitHint:%d", serviceState.dwCurrentState,
		serviceState.dwServiceType,
		serviceState.dwControlsAccepted,
		serviceState.dwWin32ExitCode,
		serviceState.dwServiceSpecificExitCode,
		serviceState.dwCheckPoint,
		serviceState.dwWaitHint
		);
	return wchar2jstring(env, buff);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(createService)(
JNIEnv* env,
jobject obj,
jlong hScm,
jstring serviceName,
jstring displayName,
jlong dwDesiredAccess,
jlong dwServiceType,
jlong dwStartType,
jlong dwErrorControl,
jstring binaryPathName,
jstring loadOrderGroup,
jstring dependencies,
jstring serviceStartName,
jstring password
) {
	wchar_t* serviceName_ptr = jstring2wchar(env, serviceName);
	wchar_t* displayName_ptr = jstring2wchar(env, displayName);
	wchar_t* binaryPathName_ptr = jstring2wchar(env, binaryPathName);
	wchar_t* loadOrderGroup_ptr = jstring2wchar(env, loadOrderGroup);
	wchar_t* dependencies_ptr = jstring2wchar(env, dependencies);
	wchar_t* serviceStartName_ptr = jstring2wchar(env, serviceStartName);
	wchar_t* password_ptr = jstring2wchar(env, password);
	DWORD tagId = 0;
	SC_HANDLE ret = CreateServiceW(ptrOf<SC_HANDLE>(hScm), serviceName_ptr, displayName_ptr,
		dwDesiredAccess, dwServiceType, dwStartType, dwErrorControl,
		binaryPathName_ptr,
		loadOrderGroup_ptr, &tagId, dependencies_ptr,
		serviceStartName_ptr, password_ptr);
	freeWchar(serviceName_ptr);
	freeWchar(displayName_ptr);
	freeWchar(binaryPathName_ptr);
	freeWchar(loadOrderGroup_ptr);
	freeWchar(dependencies_ptr);
	freeWchar(serviceStartName_ptr);
	freeWchar(password_ptr);
	return toPtr(ret);
}


extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(createFile)(
JNIEnv* env,
jobject obj,
jstring filePath,
jlong dwDesiredAccess,
jlong dwShareMode,
jlong dwCreationDisposition,
jlong dwFlagAndAttributes
) {
	wchar_t* filePath_ptr = jstring2wchar(env, filePath);
	HANDLE ret = CreateFileW(filePath_ptr, dwDesiredAccess, dwShareMode, NULL, dwCreationDisposition, dwFlagAndAttributes, NULL);
	freeWchar(filePath_ptr);
	return toPtr(ret);
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(flushFileBuffers)(
JNIEnv* env,
jobject obj,
jlong hFile
) {
	BOOL ret = FlushFileBuffers(ptrOf<HANDLE>(hFile));
	return ret == TRUE;
}


extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(writeFile)(
JNIEnv* env,
jobject obj,
jlong hFile,
jbyteArray buff,
jint offset,
jint length
) {
	const jbyte* bts = env->GetByteArrayElements(buff, NULL);
	jsize len = env->GetArrayLength(buff);

	DWORD numbers = len;
	unsigned char * data = (unsigned char *)bts;
	if (offset > 0){
		data = (unsigned char *)(bts[offset]);
	}
	if (length > 0){
		numbers = length;
	}

	DWORD writeNumbers = 0;
	BOOL ret = WriteFile(ptrOf<HANDLE>(hFile), (void *)data, numbers, &writeNumbers, NULL);
	if (ret == FALSE){
		return -1;
	}
	return writeNumbers;
}


extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(readFile)(
JNIEnv* env,
jobject obj,
jlong hFile,
jbyteArray buff,
jint offset
) {
	jsize len = env->GetArrayLength(buff);
	DWORD cnLen = len;
	if (offset > 0){
		cnLen = len - offset;
	}
	unsigned char * data = (unsigned char *)malloc(sizeof(unsigned char)*cnLen);
	DWORD readSize = 0;
	BOOL ret = ReadFile(ptrOf<HANDLE>(hFile), (void *)data, cnLen, &readSize, NULL);
	if (ret == FALSE){
		return -1;
	}
	env->SetByteArrayRegion(buff, offset, readSize, (const jbyte *)data);
	free(data);
	return readSize;
}


extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(getFileAttributesEx)(
JNIEnv* env,
jobject obj,
jstring filePath
) {
	wchar_t* filePath_ptr = jstring2wchar(env, filePath);
	WIN32_FILE_ATTRIBUTE_DATA fileData;
	BOOL ret = GetFileAttributesExW(filePath_ptr, GetFileExInfoStandard, &fileData);
	freeWchar(filePath_ptr);
	if (ret == FALSE){
		return nullptr;
	}
	DWORD dwFileAttributes;
	time_t ftCreationTime;
	time_t ftLastAccessTime;
	time_t ftLastWriteTime;
	ULONGLONG nFileSize = (((ULONGLONG)fileData.nFileSizeHigh) << 32) | fileData.nFileSizeLow;
	filetime2timet(&fileData.ftCreationTime, &ftCreationTime);
	filetime2timet(&fileData.ftLastAccessTime, &ftLastAccessTime);
	filetime2timet(&fileData.ftLastWriteTime, &ftLastWriteTime);
	wchar_t buff[1024] = { 0 };
	swprintf(buff, L"dwFileAttributes:%d;#;ftCreationTime:%lld;#;ftLastAccessTime:%lld;#;ftLastWriteTime:%lld;#;nFileSize:%lld",
		fileData.dwFileAttributes, ftCreationTime, ftLastAccessTime, ftLastWriteTime, nFileSize);
	return wchar2jstring(env, buff);
}

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(getFileInformationByHandle)(
JNIEnv* env,
jobject obj,
jlong hFile
) {
	BY_HANDLE_FILE_INFORMATION info;
	BOOL ret = GetFileInformationByHandle(ptrOf<HANDLE>(hFile), &info);
	if (ret == FALSE){
		return nullptr;
	}
	time_t ftCreationTime;
	time_t ftLastAccessTime;
	time_t ftLastWriteTime;
	DWORD nNumberOfLinks;

	filetime2timet(&info.ftCreationTime, &ftCreationTime);
	filetime2timet(&info.ftLastAccessTime, &ftLastAccessTime);
	filetime2timet(&info.ftLastWriteTime, &ftLastWriteTime);
	ULONGLONG nFileSize = (((ULONGLONG)info.nFileSizeHigh) << 32) | info.nFileSizeLow;
	ULONGLONG nFileIndex = (((ULONGLONG)info.nFileIndexHigh) << 32) | info.nFileIndexLow;

	wchar_t buff[1024] = { 0 };
	swprintf(buff, L"dwFileAttributes:%d;#;ftCreationTime:%lld;#;ftLastAccessTime:%lld;#;ftLastWriteTime:%lld;#;nFileSize:%lld;#;dwVolumeSerialNumber:%d;#;nNumberOfLinks:%d;#;nFileIndex:%lld",
		info.dwFileAttributes, ftCreationTime, ftLastAccessTime, ftLastWriteTime, nFileSize, info.dwVolumeSerialNumber, info.nNumberOfLinks, nFileIndex);
	return wchar2jstring(env, buff);
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(setFileTime)(
JNIEnv* env,
jobject obj,
jlong hFile,
jlong creationTime,
jlong lastAccessTime,
jlong lastWriteTime
) {
	FILETIME ftCreationTime;
	FILETIME ftLastAccessTime;
	FILETIME ftLastWriteTime;
	timet2filetime(&creationTime, &ftCreationTime);
	timet2filetime(&lastAccessTime, &ftLastAccessTime);
	timet2filetime(&lastWriteTime, &ftLastWriteTime);
	BOOL ret = SetFileTime(ptrOf<HANDLE>(hFile), (creationTime > 0 ? &ftCreationTime : NULL), (lastAccessTime > 0 ? &ftLastAccessTime : NULL), (lastWriteTime > 0 ? &ftLastWriteTime : NULL));
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(moveFile)(
JNIEnv* env,
jobject obj,
jstring fromFilePath,
jstring toFilePath
) {
	wchar_t* fromFilePath_ptr = jstring2wchar(env, fromFilePath);
	wchar_t* toFilePath_ptr = jstring2wchar(env, toFilePath);
	BOOL ret = MoveFileW(fromFilePath_ptr, toFilePath_ptr);
	freeWchar(fromFilePath_ptr);
	freeWchar(toFilePath_ptr);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(copyFile)(
JNIEnv* env,
jobject obj,
jstring fromFilePath,
jstring toFilePath,
jboolean failIfExist
) {
	wchar_t* fromFilePath_ptr = jstring2wchar(env, fromFilePath);
	wchar_t* toFilePath_ptr = jstring2wchar(env, toFilePath);
	BOOL ret = CopyFileW(fromFilePath_ptr, toFilePath_ptr, (failIfExist == true ? TRUE : FALSE));
	freeWchar(fromFilePath_ptr);
	freeWchar(toFilePath_ptr);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(coInitialize)(
JNIEnv* env,
jobject obj
){
	HRESULT ret = CoInitialize(NULL);
	return ret == S_OK;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(coInitializeEx)(
JNIEnv* env,
jobject obj,
jlong dwColInit
){
	HRESULT ret = CoInitializeEx(NULL, dwColInit);
	return ret == S_OK;
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(coUninitialize)(
JNIEnv* env,
jobject obj
){
	CoUninitialize();
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(shGetSpecialFolderLocation)(
JNIEnv* env,
jobject obj,
jint csidl
){
	LPITEMIDLIST lplist;
	HRESULT rs = SHGetSpecialFolderLocation(NULL, csidl, &lplist);
	if (rs != S_OK){
		return 0;
	}
	return toPtr(lplist);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(coTaskMemFree)(
JNIEnv* env,
jobject obj,
jlong ptr
){
	CoTaskMemFree((LPVOID)ptr);
}

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(shGetPathFromIDList)(
JNIEnv* env,
jobject obj,
jlong lpItemIdList
){
	wchar_t buff[4096] = { 0 };
	BOOL ret = SHGetPathFromIDListW(ptrOf<LPITEMIDLIST>(lpItemIdList), buff);
	if (ret == FALSE){
		return nullptr;
	}
	return wchar2jstring(env, buff);
}

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(shGetSpecialFolderPath)(
JNIEnv* env,
jobject obj,
jint csidl // CSIDL_BITBUCKET
) {
	HRESULT rsi = CoInitialize(NULL);
	if (rsi != S_OK){
		return nullptr;
	}

	LPITEMIDLIST lplist;
	HRESULT rs = SHGetSpecialFolderLocation(NULL, csidl, &lplist);
	if (rs != S_OK){
		CoUninitialize();
		return nullptr;
	}


	wchar_t buff[4096] = { 0 };
	BOOL ret = SHGetPathFromIDListW(lplist, buff);
	if (ret == FALSE){
		CoTaskMemFree(lplist);
		CoUninitialize();
		return nullptr;
	}
	CoTaskMemFree(lplist);
	CoUninitialize();
	return wchar2jstring(env, buff);
}

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(getWindowsDirectory)(
JNIEnv* env,
jobject obj
){
	wchar_t buff[4096] = { 0 };
	UINT ret = GetWindowsDirectoryW(buff, 4096);
	return wchar2jstring(env, buff);
}

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(getSystemDirectory)(
JNIEnv* env,
jobject obj
){
	wchar_t buff[4096] = { 0 };
	UINT ret = GetSystemDirectoryW(buff, 4096);
	return wchar2jstring(env, buff);
}

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(getTempPath)(
JNIEnv* env,
jobject obj
){
	wchar_t buff[4096] = { 0 };
	UINT ret = GetTempPathW(4096, buff);
	return wchar2jstring(env, buff);
}

static long long clsid_map[][2] = {
	{ 1, (long long)&CLSID_ShellDesktop },
	{ 2, (long long)&CLSID_ShellFSFolder },
	{ 3, (long long)&CLSID_NetworkPlaces },
	{ 4, (long long)&CLSID_ShellLink },
	{ 5, (long long)&CLSID_QueryCancelAutoPlay },
	{ 6, (long long)&CLSID_DriveSizeCategorizer },
	{ 7, (long long)&CLSID_DriveTypeCategorizer },
	{ 8, (long long)&CLSID_FreeSpaceCategorizer },
	{ 9, (long long)&CLSID_TimeCategorizer },
	{ 10, (long long)&CLSID_SizeCategorizer },
	{ 11, (long long)&CLSID_AlphabeticalCategorizer },
	{ 12, (long long)&CLSID_MergedCategorizer },
	{ 13, (long long)&CLSID_ImageProperties },
	{ 14, (long long)&CLSID_PropertiesUI },
	{ 15, (long long)&CLSID_UserNotification },
	{ 16, (long long)&CLSID_CDBurn },
	{ 17, (long long)&CLSID_TaskbarList },
	{ 18, (long long)&CLSID_StartMenuPin },
	{ 19, (long long)&CLSID_WebWizardHost },
	{ 20, (long long)&CLSID_PublishDropTarget },
	{ 21, (long long)&CLSID_PublishingWizard },
	{ 22, (long long)&CLSID_InternetPrintOrdering },
	{ 23, (long long)&CLSID_FolderViewHost },
	{ 24, (long long)&CLSID_ExplorerBrowser },
	{ 25, (long long)&CLSID_ImageRecompress },
	{ 26, (long long)&CLSID_TrayBandSiteService },
	{ 27, (long long)&CLSID_TrayDeskBand },
	{ 28, (long long)&CLSID_AttachmentServices },
	{ 29, (long long)&CLSID_DocPropShellExtension },
	{ 30, (long long)&CLSID_ShellItem },
	{ 31, (long long)&CLSID_NamespaceWalker },
	{ 32, (long long)&CLSID_FileOperation },
	{ 33, (long long)&CLSID_FileOpenDialog },
	{ 34, (long long)&CLSID_FileSaveDialog },
	{ 35, (long long)&CLSID_KnownFolderManager },
	{ 36, (long long)&CLSID_FSCopyHandler },
	{ 37, (long long)&CLSID_SharingConfigurationManager },
	{ 38, (long long)&CLSID_PreviousVersions },
	{ 39, (long long)&CLSID_NetworkConnections },
	{ 40, (long long)&CLSID_NamespaceTreeControl },
	{ 41, (long long)&CLSID_IENamespaceTreeControl },
	{ 42, (long long)&CLSID_ScheduledTasks },
	{ 43, (long long)&CLSID_ApplicationAssociationRegistration },
	{ 44, (long long)&CLSID_ApplicationAssociationRegistrationUI },
	{ 45, (long long)&CLSID_SearchFolderItemFactory },
	{ 46, (long long)&CLSID_OpenControlPanel },
	{ 47, (long long)&CLSID_MailRecipient },
	{ 48, (long long)&CLSID_NetworkExplorerFolder },
	{ 49, (long long)&CLSID_DestinationList },
	{ 50, (long long)&CLSID_ApplicationDestinations },
	{ 51, (long long)&CLSID_ApplicationDocumentLists },
	{ 52, (long long)&CLSID_HomeGroup },
	{ 53, (long long)&CLSID_ShellLibrary },
	{ 54, (long long)&CLSID_AppStartupLink },
	{ 55, (long long)&CLSID_EnumerableObjectCollection },
	{ 56, (long long)&CLSID_DesktopGadget },
	{ -1, -1 },

};

static long long iid_map[][2] = {
	{ 1, (long long)&IID_INewShortcutHookA },
	{ 2, (long long)&IID_IShellBrowser },
	{ 3, (long long)&IID_IShellView },
	{ 4, (long long)&IID_IContextMenu },
	{ 5, (long long)&IID_IShellIcon },
	{ 6, (long long)&IID_IShellFolder },
	{ 7, (long long)&IID_IShellExtInit },
	{ 8, (long long)&IID_IShellPropSheetExt },
	{ 9, (long long)&IID_IPersistFolder },
	{ 10, (long long)&IID_IExtractIconA },
	{ 11, (long long)&IID_IShellDetails },
	{ 12, (long long)&IID_IShellLinkA },
	{ 13, (long long)&IID_ICopyHookA },
	{ 14, (long long)&IID_IFileViewerA },
	{ 15, (long long)&IID_ICommDlgBrowser },
	{ 16, (long long)&IID_IEnumIDList },
	{ 17, (long long)&IID_IFileViewerSite },
	{ 18, (long long)&IID_IContextMenu2 },
	{ 19, (long long)&IID_IShellExecuteHookA },
	{ 20, (long long)&IID_IPropSheetPage },
	{ 21, (long long)&IID_INewShortcutHookW },
	{ 22, (long long)&IID_IFileViewerW },
	{ 23, (long long)&IID_IShellLinkW },
	{ 24, (long long)&IID_IExtractIconW },
	{ 25, (long long)&IID_IShellExecuteHookW },
	{ 26, (long long)&IID_ICopyHookW },
	{ 27, (long long)&IID_IRemoteComputer },
	{ -1, -1 }
};

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(coCreateInstance)(
JNIEnv* env,
jobject obj,
jlong clsid,
jlong clsctx,
jlong iid
){
	CLSID* pclsid = NULL;
	IID* piid = NULL;
	int i = 0;
	while (clsid_map[i][0] > 0){
		if (clsid_map[i][0] == clsid){
			pclsid = (CLSID*)clsid_map[i][1];
			break;
		}
		i++;
	}
	i = 0;
	while (iid_map[i][0] > 0){
		if (iid_map[i][0] == clsid){
			piid = (IID*)iid_map[i][1];
			break;
		}
		i++;
	}

	if (pclsid == NULL || piid == NULL){
		return 0;
	}

	void * pLink = NULL;
	HRESULT cirs = CoCreateInstance(*pclsid, NULL, clsctx, *piid, (void **)&pLink);
	if (cirs != S_OK){
		return 0;
	}

	return toPtr(pLink);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(coReleaseInstance)(
JNIEnv* env,
jobject obj,
jlong ptrInstance)
{
	IUnknown* ptr = ptrOf<IUnknown*>(ptrInstance);
	ULONG ret = ptr->Release();
	return ret;
}

static long long qi_iid_map[][2] = {
	{ 0, (long long)&IID_IMarshal },
	{ 1, (long long)&IID_IMarshal2 },
	{ 2, (long long)&IID_IMalloc },
	{ 3, (long long)&IID_IMallocSpy },
	{ 4, (long long)&IID_IStdMarshalInfo },
	{ 5, (long long)&IID_IExternalConnection },
	{ 6, (long long)&IID_IMultiQI },
	{ 7, (long long)&IID_AsyncIMultiQI },
	{ 8, (long long)&IID_IInternalUnknown },
	{ 9, (long long)&IID_IEnumUnknown },
	{ 10, (long long)&IID_IBindCtx },
	{ 11, (long long)&IID_IEnumMoniker },
	{ 12, (long long)&IID_IRunnableObject },
	{ 13, (long long)&IID_IRunningObjectTable },
	{ 14, (long long)&IID_IPersist },
	{ 15, (long long)&IID_IPersistStream },
	{ 16, (long long)&IID_IMoniker },
	{ 17, (long long)&IID_IROTData },
	{ 18, (long long)&IID_IEnumString },
	{ 19, (long long)&IID_ISequentialStream },
	{ 20, (long long)&IID_IStream },
	{ 21, (long long)&IID_IEnumSTATSTG },
	{ 22, (long long)&IID_IStorage },
	{ 23, (long long)&IID_IPersistFile },
	{ 24, (long long)&IID_IPersistStorage },
	{ 25, (long long)&IID_ILockBytes },
	{ 26, (long long)&IID_IEnumFORMATETC },
	{ 27, (long long)&IID_IEnumSTATDATA },
	{ 28, (long long)&IID_IRootStorage },
	{ 29, (long long)&IID_IAdviseSink },
	{ 30, (long long)&IID_AsyncIAdviseSink },
	{ 31, (long long)&IID_IAdviseSink2 },
	{ 32, (long long)&IID_AsyncIAdviseSink2 },
	{ 33, (long long)&IID_IDataObject },
	{ 34, (long long)&IID_IDataAdviseHolder },
	{ 35, (long long)&IID_IMessageFilter },
	{ 36, (long long)&IID_IRpcChannelBuffer },
	{ 37, (long long)&IID_IRpcChannelBuffer2 },
	{ 38, (long long)&IID_IAsyncRpcChannelBuffer },
	{ 39, (long long)&IID_IRpcChannelBuffer3 },
	{ 40, (long long)&IID_IRpcSyntaxNegotiate },
	{ 41, (long long)&IID_IRpcProxyBuffer },
	{ 42, (long long)&IID_IRpcStubBuffer },
	{ 43, (long long)&IID_IPSFactoryBuffer },
	{ 44, (long long)&IID_IChannelHook },
	{ 45, (long long)&IID_IClientSecurity },
	{ 46, (long long)&IID_IServerSecurity },
	{ 47, (long long)&IID_IClassActivator },
	{ 48, (long long)&IID_IRpcOptions },
	{ 49, (long long)&IID_IGlobalOptions },
	{ 50, (long long)&IID_IFillLockBytes },
	{ 51, (long long)&IID_IProgressNotify },
	{ 52, (long long)&IID_ILayoutStorage },
	{ 53, (long long)&IID_IBlockingLock },
	{ 54, (long long)&IID_ITimeAndNoticeControl },
	{ 55, (long long)&IID_IOplockStorage },
	{ 56, (long long)&IID_ISurrogate },
	{ 57, (long long)&IID_IGlobalInterfaceTable },
	{ 58, (long long)&IID_IDirectWriterLock },
	{ 59, (long long)&IID_ISynchronize },
	{ 60, (long long)&IID_ISynchronizeHandle },
	{ 61, (long long)&IID_ISynchronizeEvent },
	{ 62, (long long)&IID_ISynchronizeContainer },
	{ 63, (long long)&IID_ISynchronizeMutex },
	{ 64, (long long)&IID_ICancelMethodCalls },
	{ 65, (long long)&IID_IAsyncManager },
	{ 66, (long long)&IID_ICallFactory },
	{ 67, (long long)&IID_IRpcHelper },
	{ 68, (long long)&IID_IReleaseMarshalBuffers },
	{ 69, (long long)&IID_IWaitMultiple },
	{ 70, (long long)&IID_IUrlMon },
	{ 71, (long long)&IID_IForegroundTransfer },
	{ 72, (long long)&IID_IAddrTrackingControl },
	{ 73, (long long)&IID_IAddrExclusionControl },
	{ 74, (long long)&IID_IPipeByte },
	{ 75, (long long)&IID_AsyncIPipeByte },
	{ 76, (long long)&IID_IPipeLong },
	{ 77, (long long)&IID_AsyncIPipeLong },
	{ 78, (long long)&IID_IPipeDouble },
	{ 79, (long long)&IID_AsyncIPipeDouble },
	{ 80, (long long)&IID_IThumbnailExtractor },
	{ 81, (long long)&IID_IDummyHICONIncluder },
	{ 85, (long long)&IID_IProcessLock },
	{ 86, (long long)&IID_ISurrogateService },
	{ 87, (long long)&IID_IComThreadingInfo },
	{ 88, (long long)&IID_IProcessInitControl },
	{ 89, (long long)&IID_IInitializeSpy },
	{ -1, -1 }
};

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(coInstanceQueryInterface)(
JNIEnv* env,
jobject obj,
jlong ptrInstance,
jlong iid)
{
	IUnknown* ptr = ptrOf<IUnknown*>(ptrInstance);

	IID* piid = NULL;
	int i = 0;
	while (qi_iid_map[i][0] > 0){
		if (qi_iid_map[i][0] == iid){
			piid = (IID*)qi_iid_map[i][1];
			break;
		}
		i++;
	}
	if (piid == NULL){
		return 0;
	}

	void * pInterface = NULL;
	HRESULT qirs = ptr->QueryInterface(*piid, (void **)&pInterface);
	if (qirs != S_OK){
		return 0;
	}
	return toPtr(pInterface);
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(createFileShortcut)(
JNIEnv* env,
jobject obj,
jstring srcFilePath,
jstring lnkFilePath,
jstring arguments,
jstring workDirPath,
jstring description,
jstring iconPath,
jint iconIndex,
jint hotKeyCmd,
jint hotKeyVk,
jint showCmd
){
	IShellLinkW * pLink = NULL;
	HRESULT cirs = CoCreateInstance(CLSID_ShellLink, NULL, CLSCTX_INPROC_SERVER, IID_IShellLink, (void **)&pLink);
	if (cirs != S_OK){
		return false;
	}

	IPersistFile * pPerFile = NULL;
	HRESULT qirs = pLink->QueryInterface(IID_IPersistFile, (void **)&pPerFile);
	if (qirs != S_OK){
		pLink->Release();
		return false;
	}

	wchar_t* srcFilePath_ptr = jstring2wchar(env, srcFilePath);
	pLink->SetPath(srcFilePath_ptr);

	wchar_t* arguments_ptr = jstring2wchar(env, arguments);
	if (arguments_ptr != NULL){
		pLink->SetArguments(arguments_ptr);
	}

	wchar_t* workDirPath_ptr = jstring2wchar(env, workDirPath);
	if (workDirPath_ptr != NULL){
		pLink->SetWorkingDirectory(workDirPath_ptr);
	}

	wchar_t* description_ptr = jstring2wchar(env, description);
	if (description_ptr != NULL){
		pLink->SetDescription(description_ptr);
	}

	wchar_t* iconPath_ptr = jstring2wchar(env, iconPath);
	if (iconPath_ptr != NULL){
		pLink->SetIconLocation(iconPath_ptr, (iconIndex > 0 ? iconIndex : 0));
	}


	if (hotKeyCmd > 0 && hotKeyVk > 0){
		pLink->SetHotkey(MAKEWORD(hotKeyVk, hotKeyCmd));
	}

	pLink->SetShowCmd(showCmd);

	wchar_t* lnkFilePath_ptr = jstring2wchar(env, lnkFilePath);
	pPerFile->Save(lnkFilePath_ptr, TRUE);


	pPerFile->Release();
	pLink->Release();

	freeWchar(srcFilePath_ptr);
	freeWchar(arguments_ptr);
	freeWchar(workDirPath_ptr);
	freeWchar(description_ptr);
	freeWchar(iconPath_ptr);
	freeWchar(lnkFilePath_ptr);

	return true;
}

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(getDiskFreeSpaceEx)(
JNIEnv* env,
jobject obj,
jstring filePath
){
	ULARGE_INTEGER lpFreeBytesAvailableToCaller;
	ULARGE_INTEGER lpTotalNumberOfBytes;
	ULARGE_INTEGER lpTotalNumberOfFreeBytes;

	wchar_t* filePath_ptr = jstring2wchar(env, filePath);
	BOOL ret = GetDiskFreeSpaceExW(filePath_ptr, &lpFreeBytesAvailableToCaller, &lpTotalNumberOfBytes, &lpTotalNumberOfFreeBytes);
	freeWchar(filePath_ptr);
	if (ret == FALSE){
		return nullptr;
	}
	wchar_t buff[1024] = { 0 };
	swprintf(buff, L"freeBytesAvailableToCaller:%lld;#;totalNumberOfBytes:%lld;#;totalNumberOfFreeBytes:%lld", lpFreeBytesAvailableToCaller, lpTotalNumberOfBytes, lpTotalNumberOfFreeBytes);
	return wchar2jstring(env, buff);
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(shEmptyRecycleBin)(
JNIEnv* env,
jobject obj,
jlong hwnd,
jstring rootPath,
jlong flags
){
	wchar_t* rootPath_ptr = jstring2wchar(env, rootPath);
	HRESULT ret = SHEmptyRecycleBinW(ptrOf<HWND>(hwnd), rootPath_ptr, flags);
	freeWchar(rootPath_ptr);
	return ret == S_OK;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(shFileOperation)(
JNIEnv* env,
jobject obj,
jlong hwnd,
jlong wFunc,
jstring pFrom,
jstring pTo,
jlong fFlags,
jboolean fAnyOperationsAborted,
jstring lpszProgressTitle
){
	SHFILEOPSTRUCTW op;
	op.hwnd = ptrOf<HWND>(hwnd);
	op.wFunc = wFunc;
	op.pFrom = jstring2wchar(env, pFrom);
	op.pTo = jstring2wchar(env, pTo);
	op.fFlags = fFlags;
	op.fAnyOperationsAborted = (fAnyOperationsAborted == true ? TRUE : FALSE);
	op.hNameMappings = NULL;
	op.lpszProgressTitle = jstring2wchar(env, lpszProgressTitle);
	int ret = SHFileOperationW(&op);
	freeWchar((wchar_t*)op.pFrom);
	freeWchar((wchar_t*)op.pTo);
	freeWchar((wchar_t*)op.lpszProgressTitle);
	return ret;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(monitorFromWindow)(
JNIEnv* env,
jobject obj,
jlong hwnd,
jlong dwFlags
){
	HMONITOR ret = MonitorFromWindow(ptrOf<HWND>(hwnd), dwFlags);
	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(monitorFromPoint)(
JNIEnv* env,
jobject obj,
jint x,
jint y,
jlong dwFlags
){
	POINT p;
	p.x = x;
	p.y = y;
	HMONITOR ret = MonitorFromPoint(p, dwFlags);
	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(monitorFromRect)(
JNIEnv* env,
jobject obj,
jint left,
jint top,
jint right,
jint bottom,
jlong dwFlags
){
	RECT r;
	r.left = left;
	r.top = top;
	r.right = right;
	r.bottom = bottom;
	HMONITOR ret = MonitorFromRect(&r, dwFlags);
	return toPtr(ret);
}


extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(colorAdjustLuma)(
JNIEnv* env,
jobject obj,
jint color,
jint n,
jboolean fScale
){
	COLORREF ret = ColorAdjustLuma((COLORREF)color, n, (fScale == true ? TRUE : FALSE));
	return (jint)ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(colorHLSToRGB)(
JNIEnv* env,
jobject obj,
jint wHue,
jint wLuminance,
jint wSaturation
){
	COLORREF ret = ColorHLSToRGB(wHue, wLuminance, wSaturation);
	return (jint)ret;
}

extern "C" JNIEXPORT jintArray JNICALL
JNI_METHOD(colorRGBToHLS)(
JNIEnv* env,
jobject obj,
jint color
){
	WORD wHue = 0, wLuminance = 0, wSaturation = 0;
	ColorRGBToHLS(color, &wHue, &wLuminance, &wSaturation);
	jint arr[] = { wHue, wLuminance, wSaturation };
	jintArray ret = env->NewIntArray(3);
	env->SetIntArrayRegion(ret, 0, 3, arr);
	return ret;
}

extern "C" JNIEXPORT jlongArray JNICALL
JNI_METHOD(getProcessAffinityMask)(
JNIEnv* env,
jobject obj,
jlong hProcess
){
	ULONGLONG processAffinityMask = 0, systemAffinityMask = 0;
	BOOL ok = GetProcessAffinityMask(ptrOf<HANDLE>(hProcess), &processAffinityMask, &systemAffinityMask);
	if (ok != TRUE){
		return env->NewLongArray(0);
	}
	jlong arr[] = { processAffinityMask, systemAffinityMask };
	jlongArray ret = env->NewLongArray(2);
	env->SetLongArrayRegion(ret, 0, 2, arr);
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(setProcessAffinityMask)(
JNIEnv* env,
jobject obj,
jlong hProcess,
jlong processAffinityMask
){
	ULONGLONG mask = processAffinityMask;
	BOOL ret = SetProcessAffinityMask(ptrOf<HANDLE>(hProcess), mask);
	return ret == TRUE;
}


extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getPriorityClass)(
JNIEnv* env,
jobject obj,
jlong hProcess
){
	DWORD ret = GetPriorityClass(ptrOf<HANDLE>(hProcess));
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(setPriorityClass)(
JNIEnv* env,
jobject obj,
jlong hProcess,
jint dwPriorityClass
){
	BOOL ret = SetPriorityClass(ptrOf<HANDLE>(hProcess), dwPriorityClass);
	return ret == TRUE;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getThreadPriority)(
JNIEnv* env,
jobject obj,
jlong hThread
){
	int ret = GetThreadPriority(ptrOf<HANDLE>(hThread));
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(setThreadPriority)(
JNIEnv* env,
jobject obj,
jlong hThread,
jint priority
){
	BOOL ret = SetThreadPriority(ptrOf<HANDLE>(hThread), priority);
	return ret == TRUE;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(setThreadAffinityMask)(
JNIEnv* env,
jobject obj,
jlong hThread,
jlong threadAffinityMask
){
	DWORD_PTR ret = SetThreadAffinityMask(ptrOf<HANDLE>(hThread), threadAffinityMask);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(isWow64Process)(
JNIEnv* env,
jobject obj,
jlong hProcess
){
	BOOL wow64 = FALSE;
	BOOL ret = IsWow64Process(ptrOf<HANDLE>(hProcess), &wow64);
	return wow64 == TRUE;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(getCurrentThread)(
JNIEnv* env,
jobject obj
){
	HANDLE ret = GetCurrentThread();
	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(getCurrentThreadId)(
JNIEnv* env,
jobject obj
){
	DWORD ret = GetCurrentThreadId();
	return toPtr(ret);
}

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(getCurrentDirectory)(
JNIEnv* env,
jobject obj
){
	wchar_t* buff = (wchar_t*)malloc(sizeof(wchar_t)* 8192);
	DWORD len = GetCurrentDirectoryW(8192, buff);
	jstring ret = wchar2jstring(env, buff);
	free(buff);
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(setCurrentDirectory)(
JNIEnv* env,
jobject obj,
jstring path
){
	wchar_t * path_ptr = jstring2wchar(env, path);
	BOOL ret = SetCurrentDirectoryW(path_ptr);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(createHardLink)(
JNIEnv* env,
jobject obj,
jstring newFileName,
jstring existFileName
){
	wchar_t* newFileName_ptr = jstring2wchar(env, newFileName);
	wchar_t* existsFileName_ptr = jstring2wchar(env, existFileName);
	BOOL ret = CreateHardLinkW(newFileName_ptr, existsFileName_ptr, NULL);
	freeWchar(newFileName_ptr);
	freeWchar(existsFileName_ptr);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(createSymbolicLink)(
JNIEnv* env,
jobject obj,
jstring symlinkFileName,
jstring targetFileName,
jint dwFlags
){
	wchar_t* symlinkFileName_ptr = jstring2wchar(env, symlinkFileName);
	wchar_t* targetFileName_ptr = jstring2wchar(env, targetFileName);
	BOOL ret = CreateSymbolicLinkW(symlinkFileName_ptr, targetFileName_ptr, dwFlags);
	freeWchar(symlinkFileName_ptr);
	freeWchar(targetFileName_ptr);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(encryptFile)(
JNIEnv* env,
jobject obj,
jstring fileName
){
	wchar_t* fileName_ptr = jstring2wchar(env, fileName);
	BOOL ret = EncryptFileW(fileName_ptr);
	freeWchar(fileName_ptr);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(decryptFile)(
JNIEnv* env,
jobject obj,
jstring fileName
){
	wchar_t* fileName_ptr = jstring2wchar(env, fileName);
	BOOL ret = DecryptFileW(fileName_ptr, 0);
	freeWchar(fileName_ptr);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(createDirectory)(
JNIEnv* env,
jobject obj,
jstring fileName
){
	wchar_t* fileName_ptr = jstring2wchar(env, fileName);
	BOOL ret = CreateDirectoryW(fileName_ptr, NULL);
	freeWchar(fileName_ptr);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(deleteFile)(
JNIEnv* env,
jobject obj,
jstring fileName
){
	wchar_t* fileName_ptr = jstring2wchar(env, fileName);
	BOOL ret = DeleteFileW(fileName_ptr);
	freeWchar(fileName_ptr);
	return ret == TRUE;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(fileEncryptionStatus)(
JNIEnv* env,
jobject obj,
jstring fileName
){
	wchar_t* fileName_ptr = jstring2wchar(env, fileName);
	DWORD ret = -1;
	BOOL ok = FileEncryptionStatusW(fileName_ptr, &ret);
	freeWchar(fileName_ptr);
	if (ok == FALSE){
		return -1;
	}
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getBinaryType)(
JNIEnv* env,
jobject obj,
jstring fileName
){
	wchar_t* fileName_ptr = jstring2wchar(env, fileName);
	DWORD ret = -1;
	BOOL ok = GetBinaryTypeW(fileName_ptr, &ret);
	freeWchar(fileName_ptr);
	if (ok == FALSE){
		return -1;
	}
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(removeDirectory)(
JNIEnv* env,
jobject obj,
jstring fileName
){
	wchar_t* fileName_ptr = jstring2wchar(env, fileName);
	BOOL ok = RemoveDirectoryW(fileName_ptr);
	return ok == TRUE;
}

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(getComputerNameEx)(
JNIEnv* env,
jobject obj,
jint format
){
	wchar_t buff[1024]={0};
	DWORD len = 1024;
	BOOL ret=GetComputerNameExW((COMPUTER_NAME_FORMAT)format,buff,&len); 
	if (ret == FALSE){
		return nullptr;
	}
	return wchar2jstring(env, buff);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(getPhysicallyInstalledSystemMemory)(
JNIEnv* env,
jobject obj
){
	ULONGLONG ret = 0;
	BOOL ok=GetPhysicallyInstalledSystemMemory(&ret);
	if (ok == FALSE){
		return -1;
	}
	return ret;
}

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(globalMemoryStatusEx)(
JNIEnv* env,
jobject obj
){
	MEMORYSTATUSEX ms = { 0 };
	BOOL ret=GlobalMemoryStatusEx(&ms);
	if (ret == FALSE){
		return nullptr;
	}
	wchar_t buff[2048] = { 0 };
	swprintf(buff, L"dwLength:%d;#;dwMemoryLoad:%d;#;ullTotalPhys:%lld;#;ullAvailPhys:%lld;#;ullTotalPageFile:%lld;#;ullAvailPageFile:%lld;#;ullTotalVirtual:%lld;#;ullAvailVirtual:%lld;#;ullAvailExtendedVirtual:%lld",
		ms.dwLength,
		ms.dwMemoryLoad,
		ms.ullTotalPhys,
		ms.ullAvailPhys,
		ms.ullTotalPageFile,
		ms.ullAvailPageFile,
		ms.ullTotalVirtual,
		ms.ullAvailVirtual,
		ms.ullAvailExtendedVirtual
		);
	return wchar2jstring(env, buff);
}

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(getVersionEx)(
JNIEnv* env,
jobject obj
){
	OSVERSIONINFOW osinfo = { 0 };
	osinfo.dwOSVersionInfoSize = sizeof(OSVERSIONINFOW);
	BOOL ret=GetVersionExW(&osinfo);
	if (ret == FALSE){
		return nullptr;
	}
	wchar_t buff[1024] = { 0 };
	swprintf(buff, L"dwOSVersionInfoSize:%d;#;dwMajorVersion:%d;#;dwMinorVersion:%d;#;dwBuildNumber:%d;#;dwPlatformId:%d;#;szCSDVersion:%s",
		osinfo.dwOSVersionInfoSize,
		osinfo.dwMajorVersion,
		osinfo.dwMinorVersion,
		osinfo.dwBuildNumber, 
		osinfo.dwPlatformId,
		osinfo.szCSDVersion);
	return wchar2jstring(env, buff);
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(exitWindowsEx)(
JNIEnv* env,
jobject obj,
jint flags,
jint reason
){
	BOOL ret=ExitWindowsEx(flags, reason);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(swapMouseButton)(
JNIEnv* env,
jobject obj,
jboolean swap
){
	BOOL ret = SwapMouseButton(swap?TRUE:FALSE);
	return ret == TRUE;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getAsyncKeyState)(
JNIEnv* env,
jobject obj,
jint vk
){
	SHORT ret = GetAsyncKeyState(vk);
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(mciSendString)(
JNIEnv* env,
jobject obj,
jstring command
){
	wchar_t* command_ptr = jstring2wchar(env, command);
	MCIERROR ret = mciSendStringW(command_ptr, NULL, 0, NULL);
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(deleteObject)(
JNIEnv* env,
jobject obj,
jlong hGdiObj
){
	BOOL ret=DeleteObject(ptrOf<HGDIOBJ>(hGdiObj));
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(deleteDC)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	BOOL ret = DeleteDC(ptrOf<HDC>(hdc));
	return ret == TRUE;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getStretchBltMode)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	int ret=GetStretchBltMode(ptrOf<HDC>(hdc));
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(setStretchBltMode)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint mode
){
	int ret = SetStretchBltMode(ptrOf<HDC>(hdc), mode); 
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getMapMode)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	int ret=GetMapMode(ptrOf<HDC>(hdc));
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(setMapMode)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint mode
){
	int ret = SetMapMode(ptrOf<HDC>(hdc),mode);
	return ret; 
}

extern "C" JNIEXPORT jintArray JNICALL
JNI_METHOD(getViewportOrgEx)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	POINT p = { 0 };
	BOOL ok=GetViewportOrgEx(ptrOf<HDC>(hdc), &p);
	if (!ok){
		return env->NewIntArray(0);
	}
	jint arr[] = { p.x, p.y };
	jintArray ret=env->NewIntArray(2);
	env->SetIntArrayRegion(ret, 0, 2, arr);
	return ret;
}

extern "C" JNIEXPORT jintArray JNICALL
JNI_METHOD(setViewportOrgEx)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint x,
jint y
){
	POINT p = { 0 };
	BOOL ok=SetViewportOrgEx(ptrOf<HDC>(hdc), x, y, &p);
	if (!ok){
		return env->NewIntArray(0);
	}
	jint arr[] = { p.x, p.y };
	jintArray ret = env->NewIntArray(2);
	env->SetIntArrayRegion(ret, 0, 2, arr);
	return ret;
}


extern "C" JNIEXPORT jintArray JNICALL
JNI_METHOD(getViewportExtEx)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	SIZE p = { 0 };
	BOOL ok = GetViewportExtEx(ptrOf<HDC>(hdc), &p);
	if (!ok){
		return env->NewIntArray(0);
	}
	jint arr[] = { p.cx, p.cy };
	jintArray ret = env->NewIntArray(2);
	env->SetIntArrayRegion(ret, 0, 2, arr);
	return ret;
}

extern "C" JNIEXPORT jintArray JNICALL
JNI_METHOD(setViewportExtEx)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint x,
jint y
){
	SIZE p = { 0 };
	BOOL ok = SetViewportExtEx(ptrOf<HDC>(hdc), x, y, &p);
	if (!ok){
		return env->NewIntArray(0);
	}
	jint arr[] = { p.cx, p.cy };
	jintArray ret = env->NewIntArray(2);
	env->SetIntArrayRegion(ret, 0, 2, arr);
	return ret;
}


extern "C" JNIEXPORT jintArray JNICALL
JNI_METHOD(getWindowOrgEx)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	POINT p = { 0 };
	BOOL ok = GetWindowOrgEx(ptrOf<HDC>(hdc), &p);
	if (!ok){
		return env->NewIntArray(0);
	}
	jint arr[] = { p.x, p.y };
	jintArray ret = env->NewIntArray(2);
	env->SetIntArrayRegion(ret, 0, 2, arr);
	return ret;
}

extern "C" JNIEXPORT jintArray JNICALL
JNI_METHOD(setWindowOrgEx)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint x,
jint y
){
	POINT p = { 0 };
	BOOL ok = SetWindowOrgEx(ptrOf<HDC>(hdc), x, y, &p);
	if (!ok){
		return env->NewIntArray(0);
	}
	jint arr[] = { p.x, p.y };
	jintArray ret = env->NewIntArray(2);
	env->SetIntArrayRegion(ret, 0, 2, arr);
	return ret;
}


extern "C" JNIEXPORT jintArray JNICALL
JNI_METHOD(getWindowExtEx)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	SIZE p = { 0 };
	BOOL ok = GetWindowExtEx(ptrOf<HDC>(hdc), &p);
	if (!ok){
		return env->NewIntArray(0);
	}
	jint arr[] = { p.cx, p.cy };
	jintArray ret = env->NewIntArray(2);
	env->SetIntArrayRegion(ret, 0, 2, arr);
	return ret;
}

extern "C" JNIEXPORT jintArray JNICALL
JNI_METHOD(setWindowExtEx)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint x,
jint y
){
	SIZE p = { 0 };
	BOOL ok = SetWindowExtEx(ptrOf<HDC>(hdc), x, y, &p);
	if (!ok){
		return env->NewIntArray(0);
	}
	jint arr[] = { p.cx, p.cy };
	jintArray ret = env->NewIntArray(2);
	env->SetIntArrayRegion(ret, 0, 2, arr);
	return ret;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(selectObject)(
JNIEnv* env,
jobject obj,
jlong hdc,
jlong hGdiObj
){
	HGDIOBJ ret=SelectObject(ptrOf<HDC>(hdc), ptrOf<HGDIOBJ>(hGdiObj));
	return toPtr(ret);
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(bitBlt)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint x,
jint y,
jint cx,
jint cy,
jlong hdcSrc,
jint x1,
jint y1,
jlong rop
){
	BOOL ret=BitBlt(ptrOf<HDC>(hdc), x, y, cx, cy, ptrOf<HDC>(hdcSrc), x1, y1, rop);
	return ret == TRUE;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(createSolidBrush)(
JNIEnv* env,
jobject obj,
jint color
){
	HBRUSH ret=CreateSolidBrush((COLORREF)color);
	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(createPen)(
JNIEnv* env,
jobject obj,
jint style,
jint width,
jint color
){
	HPEN ret = CreatePen(style,width,(COLORREF)color);
	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(createCompatibleDC)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	HDC ret = CreateCompatibleDC(ptrOf<HDC>(hdc));
	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(createCompatibleBitmap)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint cx,
jint cy
){
	HBITMAP ret = CreateCompatibleBitmap(ptrOf<HDC>(hdc),cx,cy);
	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(createDIBSection)(
JNIEnv* env,
jobject obj,
jlong hdc,
jlong pBitmapInfo,
jint usage,
jlong hSection,
jint offset
){
	BITMAPINFO info = { 0 };
	PVOID pvBits = NULL; 
	HBITMAP ret = CreateDIBSection(ptrOf<HDC>(hdc), &info, usage, &pvBits, ptrOf<HANDLE>(hSection), offset);
	return toPtr(ret);
}


extern "C" JNIEXPORT void JNICALL
JNI_METHOD(freeMallocPtr)(
JNIEnv* env,
jobject obj,
jlong ptr
){
	free(ptrOf<void*>(ptr));
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(deleteNewPtr)(
JNIEnv* env,
jobject obj,
jlong ptr
){
	void* p = (void*)p;
	delete p;
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(deleteNewArrayPtr)(
JNIEnv* env,
jobject obj,
jlong ptr
){
	void* p = (void*)p;
	delete[] p;
}


extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(mallocBitmapInfoHeader)(
JNIEnv* env,
jobject obj,
jint width,
jint height,
jint planes,
jint bitCount,
jint compression,
jint clrUsed,
jint sizeImage
){
	BITMAPINFOHEADER* ret = (BITMAPINFOHEADER*)malloc(sizeof(BITMAPINFOHEADER));
	memset(ret, 0, sizeof(BITMAPINFOHEADER));

	ret->biSize = sizeof(BITMAPINFOHEADER);
	ret->biWidth = width;
	ret->biHeight = height;
	ret->biPlanes = planes;
	ret->biBitCount = bitCount;
	ret->biCompression = compression;
	ret->biClrUsed = clrUsed;
	ret->biSizeImage = sizeImage;

	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(createPatternBrush)(
JNIEnv* env,
jobject obj,
jlong hBitmap
){
	HBRUSH ret=CreatePatternBrush(ptrOf<HBITMAP>(hBitmap));
	return toPtr(ret);
}


extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(mallocLogBrush)(
JNIEnv* env,
jobject obj,
jint color,
jint hatch,
jint style
){
	LOGBRUSH* ret = (LOGBRUSH*)malloc(sizeof(LOGBRUSH));
	ret->lbColor = color;
	ret->lbHatch = hatch;
	ret->lbStyle = style;

	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(createBrushIndirect)(
JNIEnv* env,
jobject obj,
jlong pLogBrush
){
	
	HBRUSH ret = CreateBrushIndirect((LOGBRUSH*)pLogBrush);
	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(mallocLogPen)(
JNIEnv* env,
jobject obj,
jint style,
jlong widthX,
jlong widthY,
jint color
){
	LOGPEN* ret = (LOGPEN*)malloc(sizeof(LOGPEN));
	ret->lopnStyle = style;
	ret->lopnWidth.x= widthX;
	ret->lopnWidth.y = widthY;
	ret->lopnColor = (COLORREF)color;

	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(createPenIndirect)(
JNIEnv* env,
jobject obj,
jlong pLogPen
){
	HPEN ret = CreatePenIndirect((LOGPEN*)pLogPen);
	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(mallocLogFont)(
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
	LOGFONTW* ft = (LOGFONTW*)malloc(sizeof(LOGFONTW));
	memset(ft, 0, sizeof(LOGFONTW));
	ft->lfHeight = lfHeight;
	ft->lfWidth = lfWidth;
	ft->lfEscapement = lfEscapement;
	ft->lfOrientation = lfOrientation;
	ft->lfWeight = lfWeight;
	ft->lfItalic = lfItalic ? TRUE : FALSE;
	ft->lfUnderline = lfUnderline ? TRUE : FALSE;
	ft->lfStrikeOut = lfStrikeOut ? TRUE : FALSE;
	ft->lfCharSet = lfCharSet;
	ft->lfOutPrecision = lfOutPrecision;
	ft->lfClipPrecision = lfClipPrecision;
	ft->lfQuality = lfQuality;
	ft->lfPitchAndFamily = lfPitchAndFamily;
	wchar_t* faceName_ptr = jstring2wchar(env, lfFaceName);
	if (faceName_ptr != NULL){
		lstrcpyW(ft->lfFaceName, faceName_ptr);
	}
	freeWchar(faceName_ptr);
	return toPtr(ft);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(createFontIndirect)(
JNIEnv* env,
jobject obj,
jlong pLogFont
){
	HFONT ret = CreateFontIndirectW((LOGFONTW*)pLogFont);
	return toPtr(ret);
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(ellipse)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint left,
jint top,
jint right,
jint bottom
){
	BOOL ret=Ellipse(ptrOf<HDC>(hdc), left, top, right, bottom);
	return ret == TRUE;
}


extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(rectangle)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint left,
jint top,
jint right,
jint bottom
){
	BOOL ret = Rectangle(ptrOf<HDC>(hdc), left, top, right, bottom);
	return ret == TRUE;
}


extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(setBkMode)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint mode
){
	int ret = SetBkMode(ptrOf<HDC>(hdc), mode);
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getBkMode)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	int ret = GetBkMode(ptrOf<HDC>(hdc));
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(setBkColor)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint color
){
	COLORREF ret = SetBkColor(ptrOf<HDC>(hdc), (COLORREF)color);
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getBkColor)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	COLORREF ret = GetBkColor(ptrOf<HDC>(hdc));
	return ret;
}


extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(setTextColor)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint color
){
	COLORREF ret = SetTextColor(ptrOf<HDC>(hdc), (COLORREF)color);
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getTextColor)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	COLORREF ret = GetTextColor(ptrOf<HDC>(hdc));
	return ret;
}

extern "C" JNIEXPORT jintArray JNICALL
JNI_METHOD(getClientRect)(
JNIEnv* env,
jobject obj,
jlong hwnd
){
	RECT rect = { 0 };
	BOOL ok = GetClientRect(ptrOf<HWND>(hwnd), &rect);
	if (ok == FALSE){
		return env->NewIntArray(0);
	}
	jint buff[] = { rect.left, rect.top, rect.right, rect.bottom };
	jintArray ret = env->NewIntArray(4);
	env->SetIntArrayRegion(ret, 0, 4, buff);
	return ret;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(defWindowProc)(
JNIEnv* env,
jobject obj,
jlong hwnd,
jlong message,
jlong wParam,
jlong lParam
){
	LRESULT ret=DefWindowProcW(ptrOf<HWND>(hwnd), message, (WPARAM)wParam, (LPARAM)lParam);
	return ret;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(getModuleHandle)(
JNIEnv* env,
jobject obj,
jstring moduleName
){
	wchar_t* moduleName_ptr = jstring2wchar(env, moduleName);
	HMODULE ret = GetModuleHandleW(moduleName_ptr);
	freeWchar(moduleName_ptr);
	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(extractIcon)(
JNIEnv* env,
jobject obj,
jlong hInstance,
jstring exeFileName,
jint iconIndex
){
	wchar_t* exeFileName_ptr = jstring2wchar(env, exeFileName);
	HICON ret=ExtractIconW(ptrOf<HINSTANCE>(hInstance), exeFileName_ptr, iconIndex);
	return toPtr(ret);
}


extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(destroyIcon)(
JNIEnv* env,
jobject obj,
jlong hIcon
){
	BOOL ret = DestroyIcon(ptrOf<HICON>(hIcon));
	return ret==TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(freeConsole)(
JNIEnv* env,
jobject obj
){
	BOOL ret = FreeConsole();
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(allocConsole)(
JNIEnv* env,
jobject obj
){
	BOOL ret = AllocConsole();
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(attachConsole)(
JNIEnv* env,
jobject obj,
jlong dwProcessId
){
	BOOL ret = AttachConsole(dwProcessId);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(updateWindow)(
JNIEnv* env,
jobject obj,
jlong hwnd
){
	BOOL ret = UpdateWindow(ptrOf<HWND>(hwnd));
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(invalidateRect)(
JNIEnv* env,
jobject obj,
jlong hwnd,
jboolean nullRect,
jint left,
jint top,
jint right,
jint bottom,
jboolean erase
){
	RECT rect = { 0 };
	rect.left = left;
	rect.top = top;
	rect.right = right;
	rect.bottom = bottom;
	BOOL ret = InvalidateRect(ptrOf<HWND>(hwnd),(nullRect?NULL:&rect),(erase?TRUE:FALSE));
	return ret == TRUE;
}


extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(invalidateRgn)(
JNIEnv* env,
jobject obj,
jlong hwnd,
jlong hRgn,
jboolean erase
){
	BOOL ret = InvalidateRgn(ptrOf<HWND>(hwnd), ptrOf<HRGN>(hRgn),(erase?TRUE:FALSE));
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(getMessage)(
JNIEnv* env,
jobject obj,
jlong pMsg,
jlong hwnd,
jlong uMsgFilterMin,
jlong uMsgFilterMax
){
	BOOL ret = GetMessageW(ptrOf<MSG*>(pMsg), ptrOf<HWND>(hwnd), uMsgFilterMin, uMsgFilterMax);
	return ret == TRUE;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(dispatchMessage)(
JNIEnv* env,
jobject obj,
jlong pMsg
){
	LRESULT ret = DispatchMessageW(ptrOf<MSG*>(pMsg));
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(translateMessage)(
JNIEnv* env,
jobject obj,
jlong pMsg
){
	BOOL ret = TranslateMessage(ptrOf<MSG*>(pMsg));
	return ret == TRUE;
}


extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(mallocMsg)(
JNIEnv* env,
jobject obj
){
	MSG* ret = (MSG*)malloc(sizeof(MSG));
	memset(ret, 0, sizeof(MSG));
	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(getConsoleWindow)(
JNIEnv* env,
jobject obj
){
	HWND ret=GetConsoleWindow();
	return toPtr(ret);
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getXLParam)(
JNIEnv* env,
jobject obj,
jlong lParam
){
	int ret = GET_X_LPARAM((LPARAM)lParam);
	return toPtr(ret);
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getYLParam)(
JNIEnv* env,
jobject obj,
jlong lParam
){
	int ret = GET_Y_LPARAM((LPARAM)lParam);
	return toPtr(ret);
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(makeWord)(
JNIEnv* env,
jobject obj,
jint a,
jint b
){
	WORD ret = MAKEWORD(a,b);
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(hiWord)(
JNIEnv* env,
jobject obj,
jlong a
){
	WORD ret = HIWORD(a);
	return ret;
}
extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(loWord)(
JNIEnv* env,
jobject obj,
jlong a
){
	WORD ret = LOWORD(a);
	return ret;
}


extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getRValue)(
JNIEnv* env,
jobject obj,
jint color
){
	int ret = GetRValue(color);
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getGValue)(
JNIEnv* env,
jobject obj,
jint color
){
	int ret = GetGValue(color);
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getBValue)(
JNIEnv* env,
jobject obj,
jint color
){
	int ret = GetBValue(color);
	return ret;
}


extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(getTickCount64)(
JNIEnv* env,
jobject obj
){
	ULONGLONG ret = GetTickCount64();
	return ret;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(queryUnbiasedInterruptTime)(
JNIEnv* env,
jobject obj
){
	ULONGLONG ret = 0;
	BOOL ok = QueryUnbiasedInterruptTime(&ret);
	return ret;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(queryProcessCycleTime)(
JNIEnv* env,
jobject obj,
jlong hProcess
){
	ULONG64 ret = 0;
	BOOL ok = QueryProcessCycleTime(ptrOf<HANDLE>(hProcess), &ret);
	return ret;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(queryThreadCycleTime)(
JNIEnv* env,
jobject obj,
jlong hThread
){
	ULONG64 ret = 0;
	BOOL ok = QueryThreadCycleTime(ptrOf<HANDLE>(hThread), &ret);
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(beginPath)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	BOOL ret = BeginPath(ptrOf<HDC>(hdc));
	return ret==TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(endPath)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	BOOL ret = EndPath(ptrOf<HDC>(hdc));
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(abortPath)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	BOOL ret = AbortPath(ptrOf<HDC>(hdc));
	return ret == TRUE;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(addFontResource)(
JNIEnv* env,
jobject obj,
jstring name
){
	wchar_t* name_ptr = jstring2wchar(env, name);
	int ret = AddFontResourceW(name_ptr);
	freeWchar(name_ptr);
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(removeFontResource)(
JNIEnv* env,
jobject obj,
jstring name
){
	wchar_t* name_ptr = jstring2wchar(env, name);
	BOOL ret = RemoveFontResourceW(name_ptr);
	freeWchar(name_ptr);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(angleArc)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint x,
jint y,
jint r,
jdouble startAngle,
jdouble sweepAngle
){
	BOOL ret=AngleArc(ptrOf<HDC>(hdc), x, y, r, startAngle, sweepAngle);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(arc)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint left,
jint top,
jint right,
jint bottom,
jint arcBeginX,
jint arcBeginY,
jint arcEndX,
jint arcEndY
){
	BOOL ret = Arc(ptrOf<HDC>(hdc),left,top,right,bottom,arcBeginX,arcBeginY,arcEndX,arcEndY);
	return ret == TRUE;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getArcDirection)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	int ret = GetArcDirection(ptrOf<HDC>(hdc));
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(setArcDirection)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint direction
){
	int ret = SetArcDirection(ptrOf<HDC>(hdc), direction);
	return ret; 
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(arcTo)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint left,
jint top,
jint right,
jint bottom,
jint arcBeginX,
jint arcBeginY,
jint arcEndX,
jint arcEndY
){
	BOOL ret = ArcTo(ptrOf<HDC>(hdc), left, top, right, bottom, arcBeginX, arcBeginY, arcEndX, arcEndY);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(cancelDC)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	BOOL ret = CancelDC(ptrOf<HDC>(hdc));
	return ret==TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(chord)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint left,
jint top,
jint right,
jint bottom,
jint arcBeginX,
jint arcBeginY,
jint arcEndX,
jint arcEndY
){
	BOOL ret = Chord(ptrOf<HDC>(hdc), left, top, right, bottom, arcBeginX, arcBeginY, arcEndX, arcEndY);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(closeFigure)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	BOOL ret = CloseFigure(ptrOf<HDC>(hdc));
	return ret == TRUE;
}



extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(createHalftonePalette)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	HPALETTE ret = CreateHalftonePalette(ptrOf<HDC>(hdc));
	return toPtr(ret);
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(realizePalette)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	UINT ret = RealizePalette(ptrOf<HDC>(hdc));
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(resizePalette)(
JNIEnv* env,
jobject obj,
jlong hPalette,
jint n
){
	BOOL ret = ResizePalette(ptrOf<HPALETTE>(hPalette),n);
	return ret==TRUE;
}


extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(selectPalette)(
JNIEnv* env,
jobject obj,
jlong hdc,
jlong hPalette,
jboolean forceBkgd
){
	HPALETTE ret = SelectPalette(ptrOf<HDC>(hdc), ptrOf<HPALETTE>(hPalette), forceBkgd?TRUE:FALSE);
	return toPtr(ret);
}


extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(createHatchBrush)(
JNIEnv* env,
jobject obj,
jint hatch,
jint color
){
	HBRUSH ret = CreateHatchBrush(hatch,(COLORREF)color);
	return toPtr(ret);
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(fillPath)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	BOOL ret = FillPath(ptrOf<HDC>(hdc));
	return ret==TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(fillRgn)(
JNIEnv* env,
jobject obj,
jlong hdc,
jlong hRgn,
jlong hBrush
){
	BOOL ret = FillRgn(ptrOf<HDC>(hdc),ptrOf<HRGN>(hRgn),ptrOf<HBRUSH>(hBrush));
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(flattenPath)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	BOOL ret = FlattenPath(ptrOf<HDC>(hdc));
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(floodFill)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint x,
jint y,
jint color
){
	BOOL ret = FloodFill(ptrOf<HDC>(hdc),x,y,color);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(extFloodFill)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint x,
jint y,
jint color,
jint type
){
	BOOL ret = ExtFloodFill(ptrOf<HDC>(hdc), x, y, color,type);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(frameRgn)(
JNIEnv* env,
jobject obj,
jlong hdc,
jlong hRgn,
jlong hBrush,
jint w,
jint h
){
	BOOL ret = FrameRgn(ptrOf<HDC>(hdc), ptrOf<HRGN>(hRgn), ptrOf<HBRUSH>(hBrush),w,h);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(transparentBlt)(
JNIEnv* env,
jobject obj,
jlong hdcDst,
jint x,
jint y,
jint width,
jint height,
jlong hdcSrc,
jint srcX,
jint srcY,
jint srcWidth,
jint srcHeight,
jint transparentColor
){
	BOOL ret = TransparentBlt(ptrOf<HDC>(hdcDst),x,y,width,height,ptrOf<HDC>(hdcSrc),srcX,srcY,srcWidth,srcHeight,transparentColor);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(stretchBlt)(
JNIEnv* env,
jobject obj,
jlong hdcDst,
jint x,
jint y,
jint width,
jint height,
jlong hdcSrc,
jint srcX,
jint srcY,
jint srcWidth,
jint srcHeight,
jlong rop
){
	BOOL ret = StretchBlt(ptrOf<HDC>(hdcDst), x, y, width, height, ptrOf<HDC>(hdcSrc), srcX, srcY, srcWidth, srcHeight, rop);
	return ret == TRUE;
}


extern "C" JNIEXPORT jintArray JNICALL
JNI_METHOD(getCurrentPositionEx)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	POINT p = { 0 };
	BOOL ok = GetCurrentPositionEx(ptrOf<HDC>(hdc), &p);
	if (ok == FALSE){
		return env->NewIntArray(0);
	}
	jint buff[] = { p.x, p.y };
	jintArray ret = env->NewIntArray(2);
	env->SetIntArrayRegion(ret, 0, 2, buff);
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getDCBrushColor)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	return GetDCBrushColor(ptrOf<HDC>(hdc));
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(setDCBrushColor)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint color
){
	return SetDCBrushColor(ptrOf<HDC>(hdc),color);
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getDCPenColor)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	return GetDCPenColor(ptrOf<HDC>(hdc));
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(setDCPenColor)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint color
){
	return SetDCPenColor(ptrOf<HDC>(hdc), color);
}


extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getPixel)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint x,
jint y
){
	return GetPixel(ptrOf<HDC>(hdc), x,y);
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(setPixel)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint x,
jint y,
jint color
){
	return SetPixel(ptrOf<HDC>(hdc), x, y,(COLORREF)color);
}


extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(setPixelV)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint x,
jint y,
jint color
){
	BOOL ret= SetPixelV(ptrOf<HDC>(hdc), x, y, (COLORREF)color);
	return ret == TRUE;
}


extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getPolyFillMode)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	int ret = GetPolyFillMode(ptrOf<HDC>(hdc));
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(setPolyFillMode)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint mode
){
	int ret = SetPolyFillMode(ptrOf<HDC>(hdc),mode);
	return ret;
}


extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getROP2)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	int ret = GetROP2(ptrOf<HDC>(hdc));
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(setROP2)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint rop2
){
	int ret = SetROP2(ptrOf<HDC>(hdc), rop2);
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(getTextAlign)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	int ret = GetTextAlign(ptrOf<HDC>(hdc));
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(setTextAlign)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint align
){
	int ret = SetTextAlign(ptrOf<HDC>(hdc), align);
	return ret;
}

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(getTextFace)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	wchar_t buff[1024] = { 0 };
	int ret = GetTextFaceW(ptrOf<HDC>(hdc),1024,buff);
	return wchar2jstring(env, buff);
}

extern "C" JNIEXPORT jintArray JNICALL
JNI_METHOD(moveToEx)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint x,
jint y
){
	POINT p = { 0 };
	BOOL ok=MoveToEx(ptrOf<HDC>(hdc), x, y, &p);
	if (ok == FALSE){
		return env->NewIntArray(0);
	}
	jint buff[] = { p.x, p.y };
	jintArray ret=env->NewIntArray(2);
	env->SetIntArrayRegion(ret, 0, 2, buff);
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(lineTo)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint x,
jint y
){
	BOOL ok = LineTo(ptrOf<HDC>(hdc), x, y);
	return ok == TRUE;
}

extern "C" JNIEXPORT jintArray JNICALL
JNI_METHOD(lpToDp)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint x,
jint y
){
	POINT p = { 0 };
	p.x = x;
	p.y = y;
	BOOL ok = LPtoDP(ptrOf<HDC>(hdc), &p,1);
	if (ok == FALSE){
		return env->NewIntArray(0);
	}
	jint buff[] = { p.x, p.y };
	jintArray ret = env->NewIntArray(2);
	env->SetIntArrayRegion(ret, 0, 2, buff);
	return ret;
}

extern "C" JNIEXPORT jintArray JNICALL
JNI_METHOD(dpToLp)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint x,
jint y
){
	POINT p = { 0 };
	p.x = x;
	p.y = y;
	BOOL ok = DPtoLP(ptrOf<HDC>(hdc), &p, 1);
	if (ok == FALSE){
		return env->NewIntArray(0);
	}
	jint buff[] = { p.x, p.y };
	jintArray ret = env->NewIntArray(2);
	env->SetIntArrayRegion(ret, 0, 2, buff);
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(maskBlt)(
JNIEnv* env,
jobject obj,
jlong hdcDst,
jint x,
jint y,
jint width,
jint height,
jlong hdcSrc,
jint srcX,
jint srcY,
jlong hBmMask,
jint xMask,
jint yMask,
jlong rop
){
	BOOL ret = MaskBlt(ptrOf<HDC>(hdcDst), x, y, width, height, ptrOf<HDC>(hdcSrc), srcX, srcY, ptrOf<HBITMAP>(hBmMask), xMask,yMask, rop);
	return ret == TRUE;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(offsetClipRgn)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint x,
jint y
){
	int ret=OffsetClipRgn(ptrOf<HDC>(hdc), x, y);
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(offsetRgn)(
JNIEnv* env,
jobject obj,
jlong hRgn,
jint x,
jint y
){
	int ret = OffsetRgn(ptrOf<HRGN>(hRgn), x, y);
	return ret;
}

extern "C" JNIEXPORT jintArray JNICALL
JNI_METHOD(offsetViewportOrgEx)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint x,
jint y
){
	POINT p = { 0 };
	BOOL ok = OffsetViewportOrgEx(ptrOf<HDC>(hdc), x, y,&p);
	if (ok == FALSE){
		return env->NewIntArray(0);
	}
	jint buff[] = { p.x, p.y };
	jintArray ret = env->NewIntArray(2);
	env->SetIntArrayRegion(ret, 0, 2, buff);
	return ret;
}

extern "C" JNIEXPORT jintArray JNICALL
JNI_METHOD(offsetWindowOrgEx)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint x,
jint y
){
	POINT p = { 0 };
	BOOL ok = OffsetWindowOrgEx(ptrOf<HDC>(hdc), x, y, &p);
	if (ok == FALSE){
		return env->NewIntArray(0);
	}
	jint buff[] = { p.x, p.y };
	jintArray ret = env->NewIntArray(2);
	env->SetIntArrayRegion(ret, 0, 2, buff);
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(paintRgn)(
JNIEnv* env,
jobject obj,
jlong hdc,
jlong hRgn
){
	BOOL ok = PaintRgn(ptrOf<HDC>(hdc), ptrOf<HRGN>(hRgn));
	return ok;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(patBlt)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint x,
jint y,
jint width,
jint height,
jlong rop
){
	BOOL ok = PatBlt(ptrOf<HDC>(hdc), x,y,width,height,rop);
	return ok;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(pathToRegion)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	HRGN ret = PathToRegion(ptrOf<HDC>(hdc));
	return toPtr(ret);
}


extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(pie)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint left,
jint top,
jint right,
jint bottom,
jint arcBeginX,
jint arcBeginY,
jint arcEndX,
jint arcEndY
){
	BOOL ret = Pie(ptrOf<HDC>(hdc), left, top, right, bottom, arcBeginX, arcBeginY, arcEndX, arcEndY);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(polyBezier)(
JNIEnv* env,
jobject obj,
jlong hdc,
jintArray points
){
	int len = env->GetArrayLength(points);
	jint* arr = env->GetIntArrayElements(points, NULL);
	POINT* args = (POINT*)malloc(sizeof(POINT)*(len/2));
	for (int i = 0; i < len / 2; i+=2){
		args[i].x = arr[i * 2];
		args[i].y = arr[i * 2 + 1];
	}
	
	BOOL ret = PolyBezier(ptrOf<HDC>(hdc),args,(len/2));
	env->ReleaseIntArrayElements(points, arr, 0);
	return ret == TRUE;
}


extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(polyBezierTo)(
JNIEnv* env,
jobject obj,
jlong hdc,
jintArray points
){
	int len = env->GetArrayLength(points);
	jint* arr = env->GetIntArrayElements(points, NULL);
	POINT* args = (POINT*)malloc(sizeof(POINT)*(len / 2));
	for (int i = 0; i < len / 2; i += 2){
		args[i].x = arr[i * 2];
		args[i].y = arr[i * 2 + 1];
	}

	BOOL ret = PolyBezierTo(ptrOf<HDC>(hdc), args, (len / 2));
	env->ReleaseIntArrayElements(points, arr, 0);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(polygon)(
JNIEnv* env,
jobject obj,
jlong hdc,
jintArray points
){
	int len = env->GetArrayLength(points);
	jint* arr = env->GetIntArrayElements(points, NULL);
	POINT* args = (POINT*)malloc(sizeof(POINT)*(len / 2));
	for (int i = 0; i < len / 2; i += 2){
		args[i].x = arr[i * 2];
		args[i].y = arr[i * 2 + 1];
	}

	BOOL ret = Polygon(ptrOf<HDC>(hdc), args, (len / 2));
	env->ReleaseIntArrayElements(points, arr, 0);
	return ret == TRUE;
}


extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(polyline)(
JNIEnv* env,
jobject obj,
jlong hdc,
jintArray points
){
	int len = env->GetArrayLength(points);
	jint* arr = env->GetIntArrayElements(points, NULL);
	POINT* args = (POINT*)malloc(sizeof(POINT)*(len / 2));
	for (int i = 0; i < len / 2; i += 2){
		args[i].x = arr[i * 2];
		args[i].y = arr[i * 2 + 1];
	}

	BOOL ret = Polyline(ptrOf<HDC>(hdc), args, (len / 2));
	env->ReleaseIntArrayElements(points, arr, 0);
	return ret == TRUE;
}


extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(polylineTo)(
JNIEnv* env,
jobject obj,
jlong hdc,
jintArray points
){
	int len = env->GetArrayLength(points);
	jint* arr = env->GetIntArrayElements(points, NULL);
	POINT* args = (POINT*)malloc(sizeof(POINT)*(len / 2));
	for (int i = 0; i < len / 2; i += 2){
		args[i].x = arr[i * 2];
		args[i].y = arr[i * 2 + 1];
	}

	BOOL ret = PolylineTo(ptrOf<HDC>(hdc), args, (len / 2));
	env->ReleaseIntArrayElements(points, arr, 0);
	return ret == TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(ptInRegion)(
JNIEnv* env,
jobject obj,
jlong hRgn,
jint x,
jint y
){
	BOOL ret=PtInRegion(ptrOf<HRGN>(hRgn), x, y);
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(ptVisible)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint x,
jint y
){
	BOOL ret = PtVisible(ptrOf<HDC>(hdc), x, y);
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(rectInRegion)(
JNIEnv* env,
jobject obj,
jlong hRgn,
jint left,
jint top,
jint right,
jint bottom
){
	RECT rect = { 0 };
	rect.left = left;
	rect.top = top;
	rect.right = right;
	rect.bottom = bottom;
	BOOL ret = RectInRegion(ptrOf<HRGN>(hRgn), &rect);
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(rectVisible)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint left,
jint top,
jint right,
jint bottom
){
	RECT rect = { 0 };
	rect.left = left;
	rect.top = top;
	rect.right = right;
	rect.bottom = bottom;
	BOOL ret = RectVisible(ptrOf<HDC>(hdc), &rect);
	return ret==TRUE;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(saveDC)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	int ret = SaveDC(ptrOf<HDC>(hdc));
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(restoreDC)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint nSavedDc
){
	BOOL ret = RestoreDC(ptrOf<HDC>(hdc), nSavedDc);
	return ret==TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(roundRect)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint left,
jint top,
jint right,
jint bottom,
jint width,
jint height
){
	BOOL ret = RoundRect(ptrOf<HDC>(hdc), left,top,right,bottom,width,height);
	return ret == TRUE;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(selectClipRgn)(
JNIEnv* env,
jobject obj,
jlong hdc,
jlong hRgn
){
	int ret = SelectClipRgn(ptrOf<HDC>(hdc), ptrOf<HRGN>(hRgn));
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(selectClipPath)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint mode
){
	BOOL ret = SelectClipPath(ptrOf<HDC>(hdc), mode);
	return ret == TRUE; 
}


extern "C" JNIEXPORT jintArray JNICALL
JNI_METHOD(getBrushOrgEx)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	POINT p = { 0 };
	BOOL ok = GetBrushOrgEx(ptrOf<HDC>(hdc), &p);
	if (!ok){
		return env->NewIntArray(0);
	}
	jint arr[] = { p.x, p.y };
	jintArray ret = env->NewIntArray(2);
	env->SetIntArrayRegion(ret, 0, 2, arr);
	return ret;
}

extern "C" JNIEXPORT jintArray JNICALL
JNI_METHOD(setBrushOrgEx)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint x,
jint y
){
	POINT p = { 0 };
	BOOL ok = SetBrushOrgEx(ptrOf<HDC>(hdc), x, y, &p);
	if (!ok){
		return env->NewIntArray(0);
	}
	jint arr[] = { p.x, p.y };
	jintArray ret = env->NewIntArray(2);
	env->SetIntArrayRegion(ret, 0, 2, arr);
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(setRectRgn)(
JNIEnv* env,
jobject obj,
jlong hRgn,
jint left,
jint top,
jint right,
jint bottom
){
	BOOL ret=SetRectRgn(ptrOf<HRGN>(hRgn), left, top, right, bottom);
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(setTextJustification)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint extra,
jint count
){
	BOOL ret = SetTextJustification(ptrOf<HDC>(hdc),extra,count);
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(strokeAndFillPath)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	BOOL ret = StrokeAndFillPath(ptrOf<HDC>(hdc));
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(strokePath)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	BOOL ret = StrokePath(ptrOf<HDC>(hdc));
	return ret;
}


extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(textOut)(
JNIEnv* env,
jobject obj,
jlong hdc,
jint x,
jint y,
jstring text
){
	wchar_t* text_ptr = jstring2wchar(env, text);
	BOOL ret = TextOutW(ptrOf<HDC>(hdc),x,y,text_ptr,lstrlenW(text_ptr));
	freeWchar(text_ptr);
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(unrealizeObject)(
JNIEnv* env,
jobject obj,
jlong hGdiObj
){
	BOOL ret=UnrealizeObject(ptrOf<HGDIOBJ>(hGdiObj));
	return ret;
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(updateColors)(
JNIEnv* env,
jobject obj,
jlong hdc
){
	BOOL ret = UpdateColors(ptrOf<HDC>(hdc));
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(loadCursor)(
JNIEnv* env,
jobject obj,
jlong        hInstance,
jstring      cursorName
){
	wchar_t* cursorName_ptr = jstring2wchar(env, cursorName);
	HICON ret = LoadCursorW(ptrOf<HINSTANCE>(hInstance), cursorName_ptr);
	freeWchar(cursorName_ptr);
	return toPtr(ret);
}


static long long cursor_icon_map[][2] = {
	{ 1, (long long)(void *)IDC_ARROW },
	{ 2, (long long)(void *)IDC_IBEAM },
	{ 3, (long long)(void *)IDC_WAIT },
	{ 4, (long long)(void *)IDC_CROSS },
	{ 5, (long long)(void *)IDC_UPARROW },
	{ 6, (long long)(void *)IDC_SIZE },
	{ 7, (long long)(void *)IDC_ICON },
	{ 8, (long long)(void *)IDC_SIZENWSE },
	{ 9, (long long)(void *)IDC_SIZENESW },
	{ 10, (long long)(void *)IDC_SIZEWE },
	{ 11, (long long)(void *)IDC_SIZENS },
	{ 12, (long long)(void *)IDC_SIZEALL },
	{ 13, (long long)(void *)IDC_HAND },
	{ 14, (long long)(void *)IDC_APPSTARTING },
	{ 15, (long long)(void *)IDC_HELP },
	{ -1, -1 }
};

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(loadCursorByStandardId)(
JNIEnv* env,
jobject obj,
jlong        hInstance,
jint      standardCursorId
){
	wchar_t* cursorName = NULL;
	IID* piid = NULL;
	int i = 0;
	while (cursor_icon_map[i][0] > 0){
		if (cursor_icon_map[i][0] == standardCursorId){
			cursorName = (wchar_t*)cursor_icon_map[i][1];
			break;
		}
		i++;
	}
	HICON ret = LoadCursorW(ptrOf<HINSTANCE>(hInstance), cursorName);
	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(convertBrushBySystemColorIndex)(
JNIEnv* env,
jobject obj,
jint index
){
	HBRUSH ret = (HBRUSH)(index + 1);
	return toPtr(ret);
}


typedef struct _JNI_WINAPP_ENV{
	JNIEnv* env;
	jobject callbacker;
	jclass clazz;
	jmethodID method;
} JNI_WINAPP_ENV, *P_JNI_WINAPP_ENV;

struct _WIN32_RAW_WINDOW;
typedef _WIN32_RAW_WINDOW WIN32_RAW_WINDOW;
typedef _WIN32_RAW_WINDOW* P_WIN32_RAW_WINDOW;

struct _WIN32_RAW_WINDOW{
	HWND hWnd;
	void * callbackEnv;
	LRESULT(*callbackFunc)(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam);
};

static std::map<HWND, P_WIN32_RAW_WINDOW> g_raw_map;

static JavaVM* g_jvm = NULL;

// 事件分发器
LRESULT CALLBACK dispatcherRawWndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam){

	P_WIN32_RAW_WINDOW p_instance = g_raw_map[hWnd];
	std::map<HWND, P_WIN32_RAW_WINDOW>* p_g_wnds = &g_raw_map;

	
	if (p_instance == NULL){
		return  DefWindowProcW(hWnd, message, wParam, lParam);
	}

	LRESULT ret = p_instance->callbackFunc(hWnd, message, wParam, lParam);

	if (message == WM_DESTROY){
		g_raw_map.erase(hWnd);
		P_JNI_WINAPP_ENV appEnv = (P_JNI_WINAPP_ENV)p_instance->callbackEnv;
		int status = g_jvm->GetEnv((void**)&appEnv->env, JNI_VERSION_1_8);
		if (status < 0){
			JavaVMAttachArgs args = { JNI_VERSION_1_8, __FUNCTION__, NULL };
			status = g_jvm->AttachCurrentThread((void**)&appEnv->env, &args);
			if (status < 0){
				MessageBoxA(NULL, "jvm get env error", "error", MB_OK);
			}
		}
		appEnv->env->DeleteGlobalRef(appEnv->callbacker);
		free(p_instance->callbackEnv);
		free(p_instance);
	}

	return ret;
}


LRESULT jniRawCallbackFunc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam){
	P_WIN32_RAW_WINDOW p_instance = g_raw_map[hWnd];
	P_JNI_WINAPP_ENV appEnv = (P_JNI_WINAPP_ENV)p_instance->callbackEnv;
	
	bool isAttached = false;
	int status=g_jvm->GetEnv((void**)&appEnv->env, JNI_VERSION_1_8);
	if (status < 0){
		JavaVMAttachArgs args = { JNI_VERSION_1_8, __FUNCTION__, NULL };
		status=g_jvm->AttachCurrentThread((void**)&appEnv->env, &args);
		isAttached = true;
		if (status < 0){
			MessageBoxA(NULL, "jvm get env error", "error", MB_OK);
		}
	}
	

	jlong ret = appEnv->env->CallLongMethod(appEnv->callbacker, appEnv->method, toPtr(hWnd), (jlong)message, (jlong)wParam, (jlong)lParam);

	if (isAttached){
		g_jvm->DetachCurrentThread();
	}
	return ret;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(registerClassEx)(
JNIEnv* env,
jobject obj,
jint        style,
jint         cbClsExtra,
jint         cbWndExtra,
jlong   hInstance,
jlong       hIcon,
jlong     hCursor,
jlong      hbrBackground,
jstring     lpszMenuName,
jstring     lpszClassName,
jlong       hIconSm
){
	
	wchar_t* lpszMenuName_ptr = jstring2wchar(env, lpszMenuName);
	wchar_t* lpszClassName_ptr = jstring2wchar(env, lpszClassName);
	// 注册类样式
	WNDCLASSEXW wc = { 0 };
	wc.cbSize = sizeof(WNDCLASSEXW);

	wc.style = style; 
	wc.lpfnWndProc = (WNDPROC)dispatcherRawWndProc;
	wc.cbClsExtra = cbClsExtra;
	wc.hInstance = ptrOf<HINSTANCE>(hInstance); 
	wc.hIcon = ptrOf<HICON>(hIcon); 
	wc.hCursor = ptrOf<HCURSOR>(hCursor);            
	wc.hbrBackground = ptrOf<HBRUSH>(hbrBackground);  
	wc.lpszMenuName = lpszMenuName_ptr;
	wc.lpszClassName = lpszClassName_ptr;
	wc.hIconSm = ptrOf<HICON>(hIconSm);

	ATOM ret=RegisterClassExW(&wc);

	freeWchar(lpszMenuName_ptr);
	freeWchar(lpszClassName_ptr);

	return ret;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(createWindowEx)(
JNIEnv* env,
jobject obj,
jint dwExStyle,
jstring lpClassName,
jstring lpWindowName,
jint dwStyle,
jint x,
jint y,
jint nWidth,
jint nHeight,
jlong hwndParent,
jlong hMenu,
jlong hInstance
){
	wchar_t* lpClassName_ptr = jstring2wchar(env, lpClassName);
	wchar_t* lpWindowName_ptr = jstring2wchar(env, lpWindowName);
	HWND ret=CreateWindowExW(dwExStyle,
		lpClassName_ptr,
		lpWindowName_ptr,
		dwStyle,
		x, y,
		nWidth, nHeight,
		ptrOf<HWND>(hwndParent),
		ptrOf<HMENU>(hMenu),
		ptrOf<HINSTANCE>(hInstance),
		NULL);
	freeWchar(lpClassName_ptr);
	freeWchar(lpWindowName_ptr);
	return toPtr(ret);
}


extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(mallocPaintStruct)(
JNIEnv* env,
jobject obj
){
	PAINTSTRUCT* ret = (PAINTSTRUCT*)malloc(sizeof(PAINTSTRUCT));
	memset(ret, 0, sizeof(PAINTSTRUCT));
	return toPtr(ret);
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(beginPaint)(
JNIEnv* env,
jobject obj,
jlong hwnd,
jlong pPaintStruct
){
	HDC hdc=BeginPaint(ptrOf<HWND>(hwnd), ptrOf<PAINTSTRUCT*>(pPaintStruct));
	return toPtr(hdc);
}

extern "C" JNIEXPORT jboolean JNICALL
JNI_METHOD(endPaint)(
JNIEnv* env,
jobject obj,
jlong hwnd,
jlong pPaintStruct
){
	BOOL ret=EndPaint(ptrOf<HWND>(hwnd), ptrOf<PAINTSTRUCT*>(pPaintStruct));
	return ret == TRUE;
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(postQuitMessage)(
JNIEnv* env,
jobject obj,
jint exitCode
){
	PostQuitMessage(exitCode);
}


extern "C" JNIEXPORT void JNICALL
JNI_METHOD(bindMessageCallbacker)(
JNIEnv* env,
jobject obj,
jlong hwnd,
jobject callbacker
){
	env->GetJavaVM(&g_jvm);

	callbacker = env->NewGlobalRef(callbacker);

	P_WIN32_RAW_WINDOW config = (P_WIN32_RAW_WINDOW)malloc(sizeof(WIN32_RAW_WINDOW));
	memset(config, 0, sizeof(WIN32_RAW_WINDOW));
	config->hWnd = ptrOf<HWND>(hwnd);


	P_JNI_WINAPP_ENV appEnv = (P_JNI_WINAPP_ENV)malloc(sizeof(JNI_WINAPP_ENV));
	memset(appEnv, 0, sizeof(JNI_WINAPP_ENV));
	appEnv->env = env;
	appEnv->callbacker = callbacker;
	config->callbackEnv = appEnv;
	config->callbackFunc = jniRawCallbackFunc;
	appEnv->clazz = env->GetObjectClass(callbacker);
	appEnv->method = env->GetMethodID(appEnv->clazz, "callback", "(JJJJ)J");


	g_raw_map[config->hWnd] = config;
}

enum RESIZE_MODE{
	RESIZE_MODE_ADAPT = 0,
	RESIZE_MODE_CUT = 1,
	RESIZE_MODE_FIXED = 2
};

typedef struct _BITMAP_DC{
	HDC hdc;
	HBITMAP hBitMap;
	int width;
	int height;
} BITMAP_DC, *P_BITMAP_DC;


extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(mallocBitMapDc)(
JNIEnv* env,
jobject obj
){
	BITMAP_DC* ret = (BITMAP_DC*)malloc(sizeof(BITMAP_DC));
	memset(ret, 0, sizeof(BITMAP_DC));
	return toPtr(ret);
}

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(getBitmapDcInfo)(
JNIEnv* env,
jobject obj,
jlong pBitmapDc
){
	BITMAP_DC* ret = ptrOf<BITMAP_DC*>(pBitmapDc);
	wchar_t buff[512] = { 0 };
	swprintf(buff, L"pBitmapDC:%lld;#;hdc:%lld;#;hBitmap:%lld;#;width:%d;#;height:%d",toPtr(ret), toPtr(ret->hdc), toPtr(ret->hBitMap), ret->width, ret->height);
	return wchar2jstring(env, buff);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(freopenStdio)(
JNIEnv* env,
jobject obj
){
	freopen("CONIN$", "r", stdin); // 重定向标准输入
	freopen("CONOUT$", "w", stdout); // 重定向标准输出
	freopen("CONOUT$", "w", stderr); // 重定向标准错误输出
}

DWORD WINAPI jniRawThreadProc(LPVOID lpParameter){
	P_JNI_WINAPP_ENV appEnv = (P_JNI_WINAPP_ENV)lpParameter;
	bool isAttached = false;
	try{
		int status = g_jvm->GetEnv((void**)&appEnv->env, JNI_VERSION_1_8);
		if (status < 0){
			JavaVMAttachArgs args = { JNI_VERSION_1_8, __FUNCTION__, NULL };
			status = g_jvm->AttachCurrentThread((void**)&appEnv->env, &args);
			isAttached = true;
			if (status < 0){

			}
		}

		appEnv->env->CallVoidMethod(appEnv->callbacker, appEnv->method);
	}
	catch (...){

	}
	appEnv->env->DeleteGlobalRef(appEnv->callbacker);
	free(appEnv);
	if (isAttached){
		g_jvm->DetachCurrentThread();
	}
	return 0;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(createThread)(
JNIEnv* env,
jobject obj,
jint dwStackSize,
jint dwCreatetionFlags,
jobject runnable
){
	env->GetJavaVM(&g_jvm);
	runnable = env->NewGlobalRef(runnable);

	P_JNI_WINAPP_ENV appEnv = (P_JNI_WINAPP_ENV)malloc(sizeof(JNI_WINAPP_ENV));
	memset(appEnv, 0, sizeof(JNI_WINAPP_ENV));
	appEnv->env = env;
	appEnv->callbacker = runnable;
	appEnv->clazz = env->GetObjectClass(runnable);
	appEnv->method = env->GetMethodID(appEnv->clazz, "run", "()V");

	DWORD threadId=0;
	HANDLE ret = CreateThread(NULL, dwStackSize, (PTHREAD_START_ROUTINE)jniRawThreadProc, (LPVOID)appEnv, dwCreatetionFlags, &threadId);

	return toPtr(ret);
}


extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(waitForSingleObject)(
JNIEnv* env,
jobject obj,
jlong hHandle,
jlong dwMillSeconds
){
	DWORD ret = WaitForSingleObject(ptrOf<HANDLE>(hHandle), dwMillSeconds<0 ? INFINITE : dwMillSeconds);
	return ret;
}

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(waitForMultipleObjects)(
JNIEnv* env,
jobject obj,
jlongArray hHandles,
jboolean waitAll,
jlong dwMillSeconds
){
	int len=env->GetArrayLength(hHandles);
	jlong * arr=env->GetLongArrayElements(hHandles, NULL);
	HANDLE* args = (HANDLE*)malloc(sizeof(HANDLE)*len);
	for (int i = 0; i < len; i++){
		args[i] = ptrOf<HANDLE>(arr[i]);
	}
	DWORD ret = WaitForMultipleObjects(len, args, (waitAll ? TRUE : FALSE), dwMillSeconds<0 ? INFINITE:dwMillSeconds);
	return ret;
}



void setWindowTransparentColor(HWND hWnd, COLORREF colorKey)
{
	//窗口透明
	LONG wlong = ::GetWindowLong(hWnd, GWL_EXSTYLE) | WS_EX_LAYERED;
	::SetWindowLong(hWnd, GWL_EXSTYLE, wlong);
	//穿透点击
	::SetLayeredWindowAttributes(hWnd, colorKey, 0, LWA_COLORKEY);
}
void setWindowTransparentAlpha(HWND hWnd, double transRate)
{
	//窗口透明
	LONG wlong = ::GetWindowLong(hWnd, GWL_EXSTYLE) | WS_EX_LAYERED;
	::SetWindowLong(hWnd, GWL_EXSTYLE, wlong);
	//不穿透点击
	::SetLayeredWindowAttributes(hWnd, 0x000000, (1.0 - transRate) * 255, LWA_ALPHA);
}





struct _WIN32_APP_CONFIG;
typedef _WIN32_APP_CONFIG WIN32_APP_CONFIG;
typedef _WIN32_APP_CONFIG* P_WIN32_APP_CONFIG;

struct _WIN32_APP_INSTANCE;
typedef _WIN32_APP_INSTANCE WIN32_APP_INSTANCE;
typedef _WIN32_APP_INSTANCE* P_WIN32_APP_INSTANCE;


struct _WIN32_APP_CONFIG{
	wchar_t className[1024];
	wchar_t windowTitle[1024];
	wchar_t iconFileName[2048];
	DWORD nCmdShow;
	BOOL showConsole;
	RESIZE_MODE mdcResizeMode;
	void * callbackEnv;
	LRESULT(*callbackFunc)(P_WIN32_APP_INSTANCE pInstance, HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam);
};

struct _WIN32_APP_INSTANCE{
	HINSTANCE hInstance;
	HWND hWnd;
	BITMAP_DC mdc;
	RESIZE_MODE mdcResizeMode;
	HICON icon;
	BOOL showConsole;
	void * callbackEnv;
	LRESULT(*callbackFunc)(P_WIN32_APP_INSTANCE pInstance, HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam);
};



// 管理多个窗体的容器
static std::map<HWND, P_WIN32_APP_INSTANCE> g_apps_map;


HBITMAP createBitmap(int wWid, int wHei){
	BITMAPINFOHEADER stBmpInfoHeader = { 0 };
	int nBytesPerLine = ((wWid * 32 + 31) & (~31)) >> 3;
	stBmpInfoHeader.biSize = sizeof(BITMAPINFOHEADER);
	stBmpInfoHeader.biWidth = wWid;
	stBmpInfoHeader.biHeight = wHei;
	stBmpInfoHeader.biPlanes = 1;
	stBmpInfoHeader.biBitCount = 32;
	stBmpInfoHeader.biCompression = BI_RGB;
	stBmpInfoHeader.biClrUsed = 0;
	stBmpInfoHeader.biSizeImage = nBytesPerLine * wHei;
	PVOID pvBits = NULL;
	HBITMAP bitMap = CreateDIBSection(NULL, (PBITMAPINFO)&stBmpInfoHeader, DIB_RGB_COLORS, &pvBits, NULL, 0);
	return bitMap;
}

extern "C" JNIEXPORT long JNICALL
JNI_METHOD(winAppCreateBitmap)(
JNIEnv* env,
jobject obj,
jint width,
jint height
){
	HBITMAP ret=createBitmap(width, height);
	return toPtr(ret);
}

void resizeCompatibleDC(HDC hdc, P_BITMAP_DC pBDC, int newWidth, int newHeight, RESIZE_MODE resizeMode){

	bool resize = true;
	bool init = (pBDC->hdc == NULL);
	if (!init){
		if (resizeMode == RESIZE_MODE_ADAPT){
			newWidth = max(newWidth, pBDC->width);
			newHeight = max(newHeight, pBDC->height);
			resize = true;
		}
		else if (resizeMode == RESIZE_MODE_CUT){
			resize = true;
		}
		else if (resizeMode == RESIZE_MODE_FIXED){
			newWidth = pBDC->width;
			newHeight = pBDC->height;
			resize = false;
		}
		if (newWidth == pBDC->width && newHeight == pBDC->height){
			resize = false;
		}
	}

	if (resize){

		HDC mdc = CreateCompatibleDC(hdc);

		HBITMAP bitMap = createBitmap(newWidth, newHeight);
		HGDIOBJ oldBitmap = SelectObject(mdc, bitMap);
		Rectangle(mdc, -1, -1, newWidth + 1, newHeight + 1);

		if (!init){
			BitBlt(mdc, 0, 0, pBDC->width, pBDC->height, pBDC->hdc, 0, 0, SRCCOPY);
			DeleteObject(pBDC->hBitMap);
			DeleteDC(pBDC->hdc);
		}

		pBDC->hBitMap = bitMap;
		pBDC->hdc = mdc;
		pBDC->width = newWidth;
		pBDC->height = newHeight;
	}

}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(winAppResizeCompatibleDC)(
JNIEnv* env,
jobject obj,
jlong hdc,
jlong pBDC,
jint newWidth,
jint newHeight,
jint resizeMode
){

	resizeCompatibleDC(ptrOf<HDC>(hdc), ptrOf<P_BITMAP_DC>(pBDC), newWidth, newHeight, (RESIZE_MODE)resizeMode);
}


// 事件分发器
LRESULT CALLBACK dispatcherWndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam){

	P_WIN32_APP_INSTANCE p_instance = g_apps_map[hWnd];
	std::map<HWND, P_WIN32_APP_INSTANCE>* p_g_apps = &g_apps_map;

	if (p_instance == NULL){
		return  DefWindowProcW(hWnd, message, wParam, lParam);
	}

	p_instance->callbackFunc(p_instance, hWnd, message, wParam, lParam);


}

int createWin32App(WIN32_APP_CONFIG* config){

	P_WIN32_APP_INSTANCE p_instance = (P_WIN32_APP_INSTANCE)malloc(sizeof(WIN32_APP_INSTANCE));
	memset(p_instance, 0, sizeof(WIN32_APP_INSTANCE));
	// 参数拷贝
	p_instance->showConsole = config->showConsole;
	// 设置MDC大小处理
	p_instance->mdcResizeMode = config->mdcResizeMode;

	// 获取实例句柄
	p_instance->hInstance = GetModuleHandleW(NULL);
	// 初始化回调
	p_instance->callbackEnv = config->callbackEnv;
	p_instance->callbackFunc = config->callbackFunc;

	// 加载图标
	if (lstrlenW(config->iconFileName) > 0){
		p_instance->icon = ExtractIconW(p_instance->hInstance, config->iconFileName, 0);
	}
	if ((int)p_instance->icon == 1){
		p_instance = NULL;
	}


	// 控制台
	if (p_instance->showConsole == FALSE) {
		FreeConsole();
	}



	// 注册类样式
	WNDCLASSEXW wc = { 0 };
	wc.cbSize = sizeof(WNDCLASSEXW);

	wc.style = CS_HREDRAW | CS_VREDRAW;
	wc.lpfnWndProc = (WNDPROC)dispatcherWndProc;
	wc.cbClsExtra = 0;
	wc.hInstance = p_instance->hInstance;
	wc.hIcon = p_instance->icon;
	wc.hCursor = LoadCursorW(p_instance->hInstance, IDC_ARROW);
	wc.hbrBackground = (HBRUSH)(COLOR_WINDOW + 1);
	wc.lpszMenuName = NULL;
	wc.lpszClassName = config->className;
	wc.hIconSm = p_instance->icon;

	ATOM retRc = RegisterClassExW(&wc);
	
	// 创建窗口
	p_instance->hWnd = CreateWindowW(config->className,
		config->windowTitle,
		WS_OVERLAPPEDWINDOW,
		CW_USEDEFAULT, 0,
		CW_USEDEFAULT, 0,
		NULL,
		NULL,
		p_instance->hInstance,
		NULL);

	// 绑定全局
	g_apps_map[p_instance->hWnd] = p_instance;


	// 初始化MDC
	RECT rect = { 0 };
	GetClientRect(p_instance->hWnd, &rect);

	int newWidth = rect.right - rect.left;
	int newHeight = rect.bottom - rect.top;

	HDC hdc = GetDC(p_instance->hWnd);
	resizeCompatibleDC(hdc, &p_instance->mdc, newWidth, newHeight, p_instance->mdcResizeMode);
	ReleaseDC(p_instance->hWnd, hdc);

	// 显示窗口
	BOOL retSw = ShowWindow(p_instance->hWnd, config->nCmdShow);
	BOOL retUw = UpdateWindow(p_instance->hWnd);

	// 开始消息循环
	MSG msg;
	while (GetMessageW(&msg, NULL, 0, 0)){
		TranslateMessage(&msg);
		DispatchMessageW(&msg);
	}

	return (int)msg.wParam;
}


LRESULT jniCallbackFunc(P_WIN32_APP_INSTANCE p_instance, HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam){

	P_JNI_WINAPP_ENV appEnv = (P_JNI_WINAPP_ENV)p_instance->callbackEnv;
	jlong ret = appEnv->env->CallLongMethod(appEnv->callbacker, appEnv->method, toPtr(p_instance), toPtr(hWnd), (jlong)message, (jlong)wParam, (jlong)lParam);

	
	PAINTSTRUCT ps;
	HDC hdc;

	static bool isLeftDown = false;
	static POINT lastPoint = { 0, 0 };
	static bool hasConsole = false;

	RECT rect = { 0 };
	GetClientRect(hWnd, &rect);


	switch (message){
	case WM_CREATE:

		break;
	case WM_COMMAND:

		break;
	case WM_PAINT:
		hdc = BeginPaint(hWnd, &ps);
		BitBlt(hdc, 0, 0, rect.right - rect.left, rect.bottom - rect.top, p_instance->mdc.hdc, 0, 0, SRCCOPY);
		EndPaint(hWnd, &ps);
		break;
	case WM_DESTROY:
		PostQuitMessage(0);
		g_apps_map.erase(hWnd);
		DeleteDC(p_instance->mdc.hdc);
		if (p_instance->icon != NULL){
			DestroyIcon(p_instance->icon);
		}
		DeleteObject(p_instance->mdc.hBitMap);
		free(p_instance->callbackEnv);
		free(p_instance);
		free(appEnv);
		break;
	case WM_KEYUP:
	{
					 if (wParam == VK_F12){
						 p_instance->showConsole = !p_instance->showConsole;
						 if (p_instance->showConsole){
							 AllocConsole();
							 freopen("CONIN$", "r", stdin); // 重定向标准输入
							 freopen("CONOUT$", "w", stdout); // 重定向标准输出
							 freopen("CONOUT$", "w", stderr); // 重定向标准错误输出
							 HWND h = GetConsoleWindow();
							 wchar_t buff[2048] = { 0 };
							 GetWindowTextW(hWnd, buff, 2048);
							 lstrcatW(buff, L" - Console");
							 SetWindowTextW(h, buff);
							 setWindowTransparentAlpha(h, 0.25);
							 // PostMessageW(h, WM_SETICON, ICON_BIG, (LPARAM)p_instance->icon);
						 }
						 else{
							 HWND h = GetConsoleWindow();
							 FreeConsole();
							 PostMessageW(h, WM_DESTROY, 0, 0);
							 PostMessageW(h, WM_QUIT, 0, 0);
						 }
					 }
	}
		break;
	case WM_LBUTTONDOWN:
	{
						   isLeftDown = true;
						   lastPoint.x = GET_X_LPARAM(lParam);
						   lastPoint.y = GET_Y_LPARAM(lParam);

	}
		InvalidateRect(hWnd, NULL, FALSE);
		break;
	case WM_LBUTTONUP:
	{
						 isLeftDown = false;
						 lastPoint.x = GET_X_LPARAM(lParam);
						 lastPoint.y = GET_Y_LPARAM(lParam);
	}
		InvalidateRect(hWnd, NULL, FALSE);
		break;
	case WM_RBUTTONUP:
	{
						 HBRUSH brush = CreateSolidBrush(0xffffff);
						 HGDIOBJ oldBrush = SelectObject(p_instance->mdc.hdc, brush);
						 Rectangle(p_instance->mdc.hdc, -1, -1, p_instance->mdc.width + 1, p_instance->mdc.height + 1);
						 SelectObject(p_instance->mdc.hdc, oldBrush);
						 DeleteObject(brush);
						 InvalidateRect(hWnd, NULL, TRUE);
	}
		InvalidateRect(hWnd, NULL, FALSE);
		break;
	case WM_MOUSEMOVE:
	{
						 int x = GET_X_LPARAM(lParam);
						 int y = GET_Y_LPARAM(lParam);

						 if (isLeftDown){
							 MoveToEx(p_instance->mdc.hdc, lastPoint.x, lastPoint.y, NULL);
							 LineTo(p_instance->mdc.hdc, x, y);
						 }

						 lastPoint.x = x;
						 lastPoint.y = y;
						 printf("%d,%d\n", x, y);
	}
		InvalidateRect(hWnd, NULL, FALSE);
		break;
	case WM_SIZE:
	{

					int newWidth = rect.right - rect.left;
					int newHeight = rect.bottom - rect.top;
					HDC dc = GetDC(hWnd);
					resizeCompatibleDC(dc, &p_instance->mdc, newWidth, newHeight, p_instance->mdcResizeMode);
					ReleaseDC(hWnd, dc);

	}
		break;
	default:
		return DefWindowProcW(hWnd, message, wParam, lParam);
	}

	return 0;
}

extern "C" JNIEXPORT jint JNICALL
JNI_METHOD(winAppCreateWin32App)(
JNIEnv* env,
jobject obj,
jstring className,
jstring windowTitle,
jstring iconFileName,
jint nCmdShow,
jboolean showConsole,
jint mdcResizeMode,
jobject callbacker
){
	wchar_t* className_ptr = jstring2wchar(env, className);
	wchar_t* windowTitle_ptr = jstring2wchar(env, windowTitle);
	wchar_t* iconFileName_ptr = jstring2wchar(env, iconFileName);
	WIN32_APP_CONFIG config = { 0 };
	lstrcpyW(config.className, className_ptr);
	lstrcpyW(config.windowTitle, windowTitle_ptr);
	if (iconFileName_ptr != NULL){
		lstrcpyW(config.iconFileName, iconFileName_ptr);
	}
	freeWchar(className_ptr);
	freeWchar(windowTitle_ptr);
	freeWchar(iconFileName_ptr);
	config.nCmdShow = nCmdShow;
	config.showConsole = showConsole ? TRUE : FALSE;
	config.mdcResizeMode = (RESIZE_MODE)mdcResizeMode;


	P_JNI_WINAPP_ENV appEnv = (P_JNI_WINAPP_ENV)malloc(sizeof(JNI_WINAPP_ENV));
	memset(appEnv, 0, sizeof(JNI_WINAPP_ENV));
	appEnv->env = env;
	appEnv->callbacker = callbacker;
	config.callbackEnv = appEnv;
	config.callbackFunc = jniCallbackFunc;
	appEnv->clazz = env->GetObjectClass(callbacker);
	appEnv->method = env->GetMethodID(appEnv->clazz, "callback", "(JJJJJ)J");

	return createWin32App(&config);
}
