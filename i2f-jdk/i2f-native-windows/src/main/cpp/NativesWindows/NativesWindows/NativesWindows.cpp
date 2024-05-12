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
	return 0!=kbhit();
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
	return RGB(r,g,b);
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
			swprintf(buff, L"index:%d;#;valueName:%s;#;type:%d;#;valueData:%l64d", index, szValueName, type, ptr[0]);
		}
		else if (type == REG_QWORD_LITTLE_ENDIAN){
			unsigned long long wd = 0;
			for (int i = cbData - 1; i >= 0; i--){
				wd <<= 8;
				wd |= data[i];
			}
			swprintf(buff, L"index:%d;#;valueName:%s;#;type:%d;#;valueData:%l64d", index, szValueName, type, wd);
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
	swprintf(buff, L"index:%d;#;keyName:%s;#;className:%s;#;lastWriteTime:%l64d", index, szKeyName, szClassName, tt);

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
			swprintf(buff, L"type:%d;#;valueData:%l64d", type, ptr[0]);
		}
		else if (type == REG_QWORD_LITTLE_ENDIAN){
			unsigned long long wd = 0;
			for (int i = cbData - 1; i >= 0; i--){
				wd <<= 8;
				wd |= data[i];
			}
			swprintf(buff, L"type:%d;#;valueData:%l64d", type, wd);
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
	swprintf(buff, L"dwFileAttributes:%d;#;ftCreationTime:%l64d;#;ftLastAccessTime:%l64d;#;ftLastWriteTime:%l64d;#;nFileSize:%l64d",
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
	swprintf(buff, L"dwFileAttributes:%d;#;ftCreationTime:%l64d;#;ftLastAccessTime:%l64d;#;ftLastWriteTime:%l64d;#;nFileSize:%l64d;#;dwVolumeSerialNumber:%d;#;nNumberOfLinks:%d;#;nFileIndex:%l64d",
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

	void * pLink=NULL;
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
	HRESULT qirs=ptr->QueryInterface(*piid, (void **)&pInterface);
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
		pLink->SetHotkey(MAKEWORD(hotKeyVk,hotKeyCmd));
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
	BOOL ret=GetDiskFreeSpaceExW(filePath_ptr,&lpFreeBytesAvailableToCaller,&lpTotalNumberOfBytes,&lpTotalNumberOfFreeBytes);
	freeWchar(filePath_ptr);
	if (ret == FALSE){
		return nullptr;
	}
	wchar_t buff[1024] = { 0 }; 
	swprintf(buff, L"freeBytesAvailableToCaller:%l64d;#;totalNumberOfBytes:%l64d;#;totalNumberOfFreeBytes:%l64d", lpFreeBytesAvailableToCaller, lpTotalNumberOfBytes, lpTotalNumberOfFreeBytes);
	return wchar2jstring(env,buff);
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
	HRESULT ret=SHEmptyRecycleBinW(ptrOf<HWND>(hwnd), rootPath_ptr, flags); 
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
	op.pFrom=jstring2wchar(env,pFrom);
	op.pTo=jstring2wchar(env,pTo);
	op.fFlags = fFlags; 
	op.fAnyOperationsAborted = (fAnyOperationsAborted==true?TRUE:FALSE);
	op.hNameMappings=NULL;
	op.lpszProgressTitle = jstring2wchar(env, lpszProgressTitle);
	int ret=SHFileOperationW(&op);
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
	COLORREF ret=ColorAdjustLuma((COLORREF)color, n, (fScale == true ? TRUE : FALSE));
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
	COLORREF ret=ColorHLSToRGB(wHue, wLuminance,wSaturation);
	return (jint)ret;
}

extern "C" JNIEXPORT jintArray JNICALL
JNI_METHOD(colorRGBToHLS)(
JNIEnv* env,
jobject obj,
jint color
){
	WORD wHue=0,  wLuminance=0,  wSaturation=0;
	ColorRGBToHLS(color, &wHue, &wLuminance, &wSaturation);
	jint arr[] = { wHue, wLuminance, wSaturation };
	jintArray ret = env->NewIntArray(3);
	env->SetIntArrayRegion(ret, 0, 3, arr);
	return ret;
}