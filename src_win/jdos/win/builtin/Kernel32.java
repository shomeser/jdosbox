package jdos.win.builtin;

import jdos.cpu.CPU;
import jdos.cpu.CPU_Regs;
import jdos.cpu.Callback;
import jdos.cpu.Paging;
import jdos.hardware.Memory;
import jdos.win.Console;
import jdos.win.Win;
import jdos.win.kernel.WinCallback;
import jdos.win.loader.BuiltinModule;
import jdos.win.loader.Loader;
import jdos.win.loader.Module;
import jdos.win.loader.winpe.LittleEndianFile;
import jdos.win.utils.Error;
import jdos.win.utils.*;

import java.util.Hashtable;
import java.util.Random;

public class Kernel32 extends BuiltinModule {
    private static final int STD_OUT = 1;
    private static final int STD_IN = 2;
    private static final int STD_ERROR = 3;

    private Hashtable files = new Hashtable();
    private long startTime = System.currentTimeMillis();

    public Kernel32(Loader loader, int handle) {
        super(loader, "kernel32.dll", handle);

        files.put(new Integer(STD_OUT), new File(File.FILE_TYPE_CHAR, STD_OUT));
        files.put(new Integer(STD_IN), new File(File.FILE_TYPE_CHAR, STD_IN));
        files.put(new Integer(STD_ERROR), new File(File.FILE_TYPE_CHAR, STD_ERROR));

        add(CloseHandle);
        add(CreateFileMappingA);
        add(CreateFileMappingW);
        add(CreateProcessA);
        add(CreateThread);
        add(DebugBreak);
        add(DecodePointer);
        add(DeleteCriticalSection);
        add(EncodePointer);
        add(EnterCriticalSection);
        add(EnumSystemLocalesA);
        add(EnumSystemLocalesW);
        add(ExitProcess);
        add(FatalAppExitA);
        add(FreeEnvironmentStringsA);
        add(FreeEnvironmentStringsW);
        add(FreeLibrary);
        add(GetACP);
        add(GetCommandLineA);
        add(GetCommandLineW);
        add(GetConsoleCP);
        add(GetConsoleMode);
        add(GetConsoleOutputCP);
        add(GetCPInfo);
        add(GetCurrentProcess);
        add(GetCurrentProcessId);
        add(GetCurrentThread);
        add(GetCurrentThreadId);
        add(GetDateFormatA);
        add(GetDateFormatW);
        add(GetEnvironmentStrings);
        add(GetEnvironmentStringsW);
        add(GetFileType);
        add(GetLastError);
        add(GetLocaleInfoA);
        add(GetLocaleInfoW);
        add(GetModuleFileNameA);
        add(GetModuleFileNameW);
        add(GetModuleHandleA);
        add(GetModuleHandleW);
        add(GetOEMCP);
        add(GetProcAddress);
        add(GetProcessHeap);
        add(GetStartupInfoA);
        add(GetStartupInfoW);
        add(GetStdHandle);
        add(GetStringTypeA);
        add(GetStringTypeW);
        add(GetSystemInfo);
        add(GetSystemTimeAsFileTime);
        add(GetTickCount);
        add(GetTimeFormatA);
        add(GetTimeFormatW);
        add(GetTimeZoneInformation);
        add(GetUserDefaultLCID);
        add(GetVersion);
        add(GetVersionExA);
        add(GetVersionExW);
        add(HeapAlloc);
        add(HeapCreate);
        add(HeapDestroy);
        add(HeapFree);
        add(HeapReAlloc);
        add(HeapValidate);
        add(InitializeCriticalSection);
        add(InitializeCriticalSectionAndSpinCount);
        add(InterlockedDecrement);
        add(InterlockedExchange);
        add(InterlockedIncrement);
        add(IsBadReadPtr);
        add(IsDebuggerPresent);
        add(IsValidCodePage);
        add(IsValidLocale);
        add(LCMapStringA);
        add(LCMapStringW);
        add(LeaveCriticalSection);
        add(LoadLibraryA);
        add(LoadLibraryW);
        add(lstrlenA);
        add(lstrlenW);
        add(MapViewOfFile);
        add(MultiByteToWideChar);
        add(OutputDebugStringW);
        add(OutputDebugStringA);
        add(QueryPerformanceCounter);
        add(RaiseException);
        add(RtlUnwind);
        add(SetConsoleCtrlHandler);
        add(SetFilePointer);
        add(SetHandleCount);
        add(SetLastError);
        add(SetStdHandle);
        add(SetUnhandledExceptionFilter);
        add(TerminateProcess);
        add(TlsAlloc);
        add(TlsFree);
        add(TlsGetValue);
        add(TlsSetValue);
        add(UnhandledExceptionFilter);
        add(VirtualAlloc);
        add(VirtualFree);
        add(VirtualQuery);
        add(WaitForSingleObject);
        add(WideCharToMultiByte);
        add(WriteConsoleA);
        add(WriteConsoleW);
        add(WriteFile);
    }

    // BOOL WINAPI CloseHandle(HANDLE hObject)
    private Callback.Handler CloseHandle = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.CloseHandle";
        }
        public void onCall() {
            int hObject = CPU.CPU_Pop32();
            WinObject object = WinSystem.getObject(hObject);
            if (object == null) {
                CPU_Regs.reg_eax.dword = WinAPI.FALSE;
                WinSystem.getCurrentThread().setLastError(Error.ERROR_INVALID_HANDLE);
                return;
            }
            if (object instanceof WinProcess) {

            } else if (object instanceof WinThread) {

            } else {
                Console.out("CloseHandle not implemented for type: "+object);
                notImplemented();
            }
            CPU_Regs.reg_eax.dword = WinAPI.TRUE;
        }
    };

    // HANDLE WINAPI CreateFileMapping(HANDLE hFile, LPSECURITY_ATTRIBUTES lpAttributes, DWORD flProtect, DWORD dwMaximumSizeHigh, DWORD dwMaximumSizeLow, LPCTSTR lpName)
    private Callback.Handler CreateFileMappingA = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.CreateFileMappingA";
        }
        public void onCall() {
            int hFile = CPU.CPU_Pop32();
            int addAtributes = CPU.CPU_Pop32();
            int flags = CPU.CPU_Pop32();
            int sizeHigh = CPU.CPU_Pop32();
            int sizeLow = CPU.CPU_Pop32();
            int addName = CPU.CPU_Pop32();
            String name = null;
            if (addName != 0)
                name = new LittleEndianFile(addName).readCString();
            if (hFile != -1) {
                Console.out("CreateFileMapping not not support mapping agaist real files yet.");
                notImplemented();
            }
            if (name != null) {
                WinObject object = (WinObject)WinSystem.getNamedObject(name);
                if (object != null) {
                    if (object instanceof FileMapping) {
                        FileMapping mapping = (FileMapping)object;
                        CPU_Regs.reg_eax.dword = mapping.handle;
                        WinSystem.getCurrentThread().setLastError(Error.ERROR_ALREADY_EXISTS);
                        return;
                    }
                    WinSystem.getCurrentThread().setLastError(Error.ERROR_INVALID_HANDLE);
                    CPU_Regs.reg_eax.dword = 0;
                    return;
                }
            }
            FileMapping mapping = WinSystem.createFileMapping(hFile, name);
            CPU_Regs.reg_eax.dword = mapping.handle;
        }
    };
    private Callback.Handler CreateFileMappingW = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.CreateFileMappingW";
        }
        public void onCall() {
            int hFile = CPU.CPU_Pop32();
            int addAtributes = CPU.CPU_Pop32();
            int flags = CPU.CPU_Pop32();
            int sizeHigh = CPU.CPU_Pop32();
            int sizeLow = CPU.CPU_Pop32();
            int name = CPU.CPU_Pop32();
            notImplemented();
        }
    };

    private Callback.Handler CreateThreadCleanup = new HandlerBase() {
        public String getName() {
            return "Kernel32.CreateThread - Cleanup";
        }
        public void onCall() {
            int handle = CPU.CPU_Pop32();
            WinThread thread = (WinThread)WinSystem.getObject(handle);
            thread.exit(CPU_Regs.reg_eax.dword);
        }
    };

    private int threadCleanup = -1;

    // HANDLE WINAPI CreateThread(LPSECURITY_ATTRIBUTES lpThreadAttributes, SIZE_T dwStackSize, LPTHREAD_START_ROUTINE lpStartAddress, LPVOID lpParameter, DWORD dwCreationFlags, LPDWORD lpThreadId)
    private Callback.Handler CreateThread = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.CreateThread";
        }
        public void onCall() {
            int attributes = CPU.CPU_Pop32();
            int stackSizeCommit = CPU.CPU_Pop32();
            int stackSizeReserved = stackSizeCommit;
            int start = CPU.CPU_Pop32();
            int params = CPU.CPU_Pop32();
            int flags = CPU.CPU_Pop32();
            int id = CPU.CPU_Pop32();

            if ((flags & 0x00010000)!=0) {
                stackSizeCommit = 0;
            }
            if ((flags & 0x00000004)!=0) {
                System.out.println("CreateThread with suspend flags not supported yet");
                Win.exit();
            }
            if (attributes != 0) {
                System.out.println("***WARNING*** attributes are not supported for CreateThread");
            }
            WinThread thread = WinSystem.getCurrentProcess().createThread(start, stackSizeCommit, stackSizeReserved);
            if (threadCleanup<0) {
                int cb = WinCallback.addCallback(CreateThreadCleanup);
                threadCleanup =  loader.registerFunction(cb);
            }
            thread.pushStack32(thread.handle);
            thread.pushStack32(0);  // what's this?
            thread.pushStack32(params);
            thread.pushStack32(threadCleanup);

            if (id != 0) {
                Memory.mem_writed(id, thread.getHandle());
            }
            CPU_Regs.reg_eax.dword = thread.getHandle();
        }
    };

    // BOOL WINAPI CreateProcess(LPCTSTR lpApplicationName, LPTSTR lpCommandLine, LPSECURITY_ATTRIBUTES lpProcessAttributes, LPSECURITY_ATTRIBUTES lpThreadAttributes, BOOL bInheritHandles, DWORD dwCreationFlags, LPVOID lpEnvironment, LPCTSTR lpCurrentDirectory, LPSTARTUPINFO lpStartupInfo, LPPROCESS_INFORMATION lpProcessInformation)
    private Callback.Handler CreateProcessA = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.CreateProcessA";
        }
        public void onCall() {
            int lpApplicationName = CPU.CPU_Pop32();
            int lpCommandLine = CPU.CPU_Pop32();
            int lpProcessAttributes = CPU.CPU_Pop32();
            int lpThreadAttributes = CPU.CPU_Pop32();
            int bInheritHandles = CPU.CPU_Pop32();
            int dwCreationFlags = CPU.CPU_Pop32();
            int lpEnvironment = CPU.CPU_Pop32();
            int lpCurrentDirectory = CPU.CPU_Pop32();
            int lpStartupInfo = CPU.CPU_Pop32();
            int lpProcessInformation = CPU.CPU_Pop32();
            String name = null;
            String cwd = null;

            String commandLine = null;
            WinProcess currentProcess = WinSystem.getCurrentProcess();
            if ((lpApplicationName == 0 && lpCommandLine == 0) || lpStartupInfo == 0 || lpProcessInformation == 0) {
                CPU_Regs.reg_eax.dword = WinAPI.FALSE;
                WinSystem.getCurrentThread().setLastError(Error.ERROR_INVALID_PARAMETER);
            }
            if (lpCommandLine != 0) {
                commandLine = new LittleEndianFile(lpCommandLine).readCString();
            }
            if (lpApplicationName != 0) {
                name = new LittleEndianFile(lpApplicationName).readCString();
            } else {
                name = StringUtil.parseQuotedString(commandLine)[0];
            }
            if (lpCurrentDirectory != 0) {
                cwd = new LittleEndianFile(lpCurrentDirectory).readCString();
            } else {
                cwd = currentProcess.currentWorkingDirectory;
            }
            WinProcess process = WinSystem.createProcess(name, commandLine, currentProcess.paths, currentProcess.currentWorkingDirectory);
            if (process == null) {
                CPU_Regs.reg_eax.dword = WinAPI.FALSE;
                WinSystem.getCurrentThread().setLastError(Error.ERROR_FILE_NOT_FOUND);
            } else {
                CPU_Regs.reg_eax.dword = WinAPI.TRUE;
//                typedef struct _PROCESS_INFORMATION {
//                  HANDLE hProcess;
//                  HANDLE hThread;
//                  DWORD  dwProcessId;
//                  DWORD  dwThreadId;
//                }
                Memory.mem_writed(lpProcessInformation, process.getHandle());
                Memory.mem_writed(lpProcessInformation+4, process.getMainThread().getHandle());
                Memory.mem_writed(lpProcessInformation+8, process.getHandle());
                Memory.mem_writed(lpProcessInformation+12, process.getMainThread().getHandle());
            }
        }
    };

    // void WINAPI DebugBreak(void)
    private Callback.Handler DebugBreak = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.DebugBreak";
        }
        public void onCall() {
            Console.out("DebugBreak was called\n");
            Win.exit();
        }
    };

    // PVOID DecodePointer(PVOID Ptr)
    static private Callback.Handler DecodePointer = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.DecodePointer";
        }
        public void onCall() {
            CPU_Regs.reg_eax.dword = CPU.CPU_Pop32() ^ pointerObfuscator;
        }
    };

    // void WINAPI DeleteCriticalSection(LPCRITICAL_SECTION lpCriticalSection)
    static private Callback.Handler DeleteCriticalSection = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.DeleteCriticalSection";
        }
        public void onCall() {
            notImplemented();
        }
    };

    static int pointerObfuscator = new Random().nextInt();

    // PVOID EncodePointer(PVOID Ptr)
    static private Callback.Handler EncodePointer = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.EncodePointer";
        }
        public void onCall() {
            CPU_Regs.reg_eax.dword = CPU.CPU_Pop32() ^ pointerObfuscator;
        }
    };

    // void WINAPI EnterCriticalSection(LPCRITICAL_SECTION lpCriticalSection)
    private Callback.Handler EnterCriticalSection = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.EnterCriticalSection";
        }
        public void onCall() {
            WinSystem.getCurrentThread().enterCriticalSection(CPU.CPU_Pop32());
        }
    };

    // BOOL EnumSystemLocales(LOCALE_ENUMPROC lpLocaleEnumProc, DWORD dwFlags)
    static private Callback.Handler EnumSystemLocalesA = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.EnumSystemLocalesA";
        }
        public void onCall() {
            notImplemented();
        }
    };
    static private Callback.Handler EnumSystemLocalesW = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.EnumSystemLocalesW";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // VOID WINAPI ExitProcess(UINT uExitCode)
    private Callback.Handler ExitProcess = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.ExitProcess";
        }
        public void onCall() {
            int exitCode = CPU.CPU_Pop32();
            System.out.println("Win32 Process has exited (PID "+WinSystem.getCurrentProcess().getHandle()+"): code = " + exitCode);
            WinSystem.getCurrentProcess().exit();
        }
    };

    // VOID WINAPI ExitThread(DWORD dwExitCode)
    private Callback.Handler ExitThread = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.ExitThread";
        }
        public void onCall() {
            int exitCode = CPU.CPU_Pop32();
            WinSystem.getCurrentThread().exit(exitCode);
        }
    };

    // void WINAPI FatalAppExit(UINT uAction, LPCTSTR lpMessageText)
    static private Callback.Handler FatalAppExitA = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.FatalAppExitA";
        }
        public void onCall() {
            notImplemented();
        }
    };
    static private Callback.Handler FatalAppExitW = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.FatalAppExitW";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // BOOL WINAPI FreeEnvironmentStrings(LPTCH lpszEnvironmentBlock)
    static private Callback.Handler FreeEnvironmentStringsA = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.FreeEnvironmentStringsA";
        }
        public void onCall() {
            int address = CPU.CPU_Pop32();
            CPU_Regs.reg_eax.dword = WinAPI.TRUE;
        }
    };
    static private Callback.Handler FreeEnvironmentStringsW = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.FreeEnvironmentStringsW";
        }
        public void onCall() {
            int address = CPU.CPU_Pop32();
            CPU_Regs.reg_eax.dword = WinAPI.TRUE;
        }
    };

    // BOOL WINAPI FreeLibrary(HMODULE hModule)
    static private Callback.Handler FreeLibrary = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.FreeLibrary";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // UINT GetACP(void)
    static private Callback.Handler GetACP = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetACP";
        }
        public void onCall() {
            CPU_Regs.reg_eax.dword = 1252; // ANSI Latin 1; Western European (Windows)
        }
    };

    // LPTSTR WINAPI GetCommandLine(void)
    private Callback.Handler GetCommandLineA = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetCommandLineA";
        }
        public void onCall() {
            CPU_Regs.reg_eax.dword = WinSystem.getCurrentProcess().getCommandLine();
        }
    };
    private Callback.Handler GetCommandLineW = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetCommandLineW";
        }
        public void onCall() {
            CPU_Regs.reg_eax.dword = WinSystem.getCurrentProcess().getCommandLineW();
        }
    };

    // UINT WINAPI GetConsoleCP(void)
    private Callback.Handler GetConsoleCP = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetConsoleCP";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // BOOL WINAPI GetConsoleMode(HANDLE hConsoleHandle, LPDWORD lpMode)
    private Callback.Handler GetConsoleMode = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetConsoleMode";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // UINT WINAPI GetConsoleOutputCP(void)
    private Callback.Handler GetConsoleOutputCP = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetConsoleOutputCP";
        }
        public void onCall() {
            notImplemented();
        }
    };

    /*
     typedef struct _cpinfo {
      UINT MaxCharSize;
      BYTE DefaultChar[MAX_DEFAULTCHAR];
      BYTE LeadByte[MAX_LEADBYTES];
    }
    */

    // BOOL GetCPInfo(UINT CodePage, LPCPINFO lpCPInfo)
    private Callback.Handler GetCPInfo = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetCPInfo";
        }
        public void onCall() {
            int CodePage = CPU.CPU_Pop32();
            int add = CPU.CPU_Pop32();
            if (CodePage == 1252) {
                Memory.mem_writed(add, 1); add+=4;// MaxCharSize
                Memory.mem_writeb(add, 63); add+=1; // DefaultChar ?
                Memory.mem_writeb(add, 0); add+=1; //
                Memory.mem_zero(add, 12); // LeadByte
                CPU_Regs.reg_eax.dword = WinAPI.TRUE;
            } else {
                CPU_Regs.reg_eax.dword = WinAPI.FALSE;
                WinSystem.getCurrentThread().setLastError(Error.ERROR_INVALID_PARAMETER);
            }
        }
    };

    // HANDLE WINAPI GetCurrentProcess(void)
    static private Callback.Handler GetCurrentProcess = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetCurrentProcess";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // DWORD WINAPI GetCurrentProcessId(void)
    private Callback.Handler GetCurrentProcessId = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetCurrentProcessId";
        }
        public void onCall() {
            CPU_Regs.reg_eax.dword = WinSystem.getCurrentProcess().getHandle();
        }
    };

    // HANDLE WINAPI GetCurrentThread(void)
    static private Callback.Handler GetCurrentThread = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetCurrentThread";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // DWORD WINAPI GetCurrentThreadId(void)
    static private Callback.Handler GetCurrentThreadId = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetCurrentThreadId";
        }
        public void onCall() {
            CPU_Regs.reg_eax.dword = 0x100;
        }
    };

    // int GetDateFormat(LCID Locale, DWORD dwFlags, const SYSTEMTIME *lpDate, LPCTSTR lpFormat, LPTSTR lpDateStr, int cchDate)
    static private Callback.Handler GetDateFormatA = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetDateFormatA";
        }
        public void onCall() {
            notImplemented();
        }
    };
    static private Callback.Handler GetDateFormatW = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetDateFormatW";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // LPTCH WINAPI GetEnvironmentStrings(void)
    private Callback.Handler GetEnvironmentStrings = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetEnvironmentStrings";
        }
        public void onCall() {
            CPU_Regs.reg_eax.dword = WinSystem.getCurrentProcess().getEnvironment();
        }
    };
    private Callback.Handler GetEnvironmentStringsW = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetEnvironmentStringsW";
        }
        public void onCall() {
            CPU_Regs.reg_eax.dword = WinSystem.getCurrentProcess().getEnvironmentW();
        }
    };

    // DWORD WINAPI GetFileType(HANDLE hFile)
    private Callback.Handler GetFileType = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetFileType";
        }
        public void onCall() {
            int hFile = CPU.CPU_Pop32();
            File file = (File)files.get(new Integer(hFile));
            if (file == null) {
                CPU_Regs.reg_eax.dword = 0;
                WinSystem.getCurrentThread().setLastError(Error.ERROR_INVALID_HANDLE);
            } else {
                CPU_Regs.reg_eax.dword = file.type;
            }
        }
    };

    // DWORD WINAPI GetLastError(void)
    private Callback.Handler GetLastError = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetLastError";
        }
        public void onCall() {
            CPU_Regs.reg_eax.dword = WinSystem.getCurrentThread().getLastError();
        }
    };

    // int GetLocaleInfo(LCID Locale, LCTYPE LCType, LPTSTR lpLCData, int cchData)
    static private Callback.Handler GetLocaleInfoA = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetLocaleInfoA";
        }
        public void onCall() {
            notImplemented();
        }
    };
    static private Callback.Handler GetLocaleInfoW = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetLocaleInfoW";
        }
        public void onCall() {
            notImplemented();
        }
    };

    //DWORD WINAPI GetModuleFileName(HMODULE hModule, LPTSTR lpFilename, DWORD nSize)
    private Callback.Handler GetModuleFileNameA = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetModuleFileNameA";
        }
        public void onCall() {
            int handle = CPU.CPU_Pop32();
            int buffer = CPU.CPU_Pop32();
            int cb = CPU.CPU_Pop32();
            Module module = WinSystem.getCurrentProcess().getModuleByHandle(handle);
            if (module == null) {
                WinSystem.getCurrentThread().setLastError(Error.ERROR_INVALID_HANDLE);
                CPU_Regs.reg_eax.dword = 0;
            } else {
                String path = module.getFileName(true);
                if (cb<path.length()+1) {
                    StringUtil.strncpy(buffer, path, cb);
                    CPU_Regs.reg_eax.dword = cb;
                } else {
                    StringUtil.strcpy(buffer, path);
                    CPU_Regs.reg_eax.dword = path.length();
                }
            }
        }
    };
    private Callback.Handler GetModuleFileNameW = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetModuleFileNameW";
        }
        public void onCall() {
            int handle = CPU.CPU_Pop32();
            int buffer = CPU.CPU_Pop32();
            int cb = CPU.CPU_Pop32();
            Module module = WinSystem.getCurrentProcess().getModuleByHandle(handle);
            if (module == null) {
                WinSystem.getCurrentThread().setLastError(Error.ERROR_INVALID_HANDLE);
                CPU_Regs.reg_eax.dword = 0;
            } else {
                String path = module.getFileName(true);
                if (cb<path.length()+1) {
                    StringUtil.strncpyW(buffer, path, cb);
                    CPU_Regs.reg_eax.dword = cb;
                } else {
                    StringUtil.strcpyW(buffer, path);
                    CPU_Regs.reg_eax.dword = path.length();
                }
            }
        }
    };

    // HMODULE WINAPI GetModuleHandle(LPCTSTR lpModuleName)
    private Callback.Handler GetModuleHandleA = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetModuleHandleA";
        }
        public void onCall() {
            int add = CPU.CPU_Pop32();
            String name = new LittleEndianFile(add).readCString();
            CPU_Regs.reg_eax.dword = WinSystem.getCurrentProcess().getModuleByName(name);
            if (CPU_Regs.reg_eax.dword == 0)
                WinSystem.getCurrentThread().setLastError(Error.ERROR_MOD_NOT_FOUND);
            System.out.println("    "+name);
        }
    };
    static private Callback.Handler GetModuleHandleW = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetModuleHandleW";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // UINT GetOEMCP(void)
    static private Callback.Handler GetOEMCP = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetOEMCP";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // FARPROC WINAPI GetProcAddress(HMODULE hModule, LPCSTR lpProcName)
     private Callback.Handler GetProcAddress = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetProcAddress";
        }
        public void onCall() {
            int handle = CPU.CPU_Pop32();
            int procName = CPU.CPU_Pop32();
            String name = new LittleEndianFile(procName).readCString();
            System.out.println("    "+name);
            CPU_Regs.reg_eax.dword = WinSystem.getCurrentProcess().getProcAddress(handle, name);
        }
    };
    
    // HANDLE WINAPI GetProcessHeap(void)
    private Callback.Handler GetProcessHeap = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetProcessHeap";
        }
        public void onCall() {
            CPU_Regs.reg_eax.dword = WinSystem.getCurrentProcess().getHeapHandle();
        }
    };

    /*
    typedef struct _STARTUPINFO {
      DWORD  cb;
      LPTSTR lpReserved;
      LPTSTR lpDesktop;
      LPTSTR lpTitle;
      DWORD  dwX;
      DWORD  dwY;
      DWORD  dwXSize;
      DWORD  dwYSize;
      DWORD  dwXCountChars;
      DWORD  dwYCountChars;
      DWORD  dwFillAttribute;
      DWORD  dwFlags;
      WORD   wShowWindow;
      WORD   cbReserved2;
      LPBYTE lpReserved2;
      HANDLE hStdInput;
      HANDLE hStdOutput;
      HANDLE hStdError;
    }
     */
    // VOID WINAPI GetStartupInfo(LPSTARTUPINFO lpStartupInfo)
    private Callback.Handler GetStartupInfoA = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetStartupInfoA";
        }
        public void onCall() {
            int add = CPU.CPU_Pop32();
            int cb = Memory.mem_readd(add);
            Memory.mem_writed(add, 68); add+=4; // cb
            Memory.mem_writed(add, 0); add+=4; // lpReserved
            Memory.mem_writed(add, 0); add+=4; // lpDesktop
            Memory.mem_writed(add, 0); add+=4; // lpTitle
            Memory.mem_writed(add, 0); add+=4; // dwX
            Memory.mem_writed(add, 0); add+=4; // dwY
            Memory.mem_writed(add, 0); add+=4; // dwXSize
            Memory.mem_writed(add, 0); add+=4; // dwYSize
            if (WinSystem.getCurrentProcess().console) {
                Memory.mem_writed(add, 80); add+=4; // dwXCountChars
                Memory.mem_writed(add, 25); add+=4; // dwYCountChars
            } else {
                Memory.mem_writed(add, 0); add+=4; // dwXCountChars
                Memory.mem_writed(add, 0); add+=4; // dwYCountChars
            }
            Memory.mem_writed(add, 0); add+=4; // dwFillAttribute
            Memory.mem_writew(add, 0); add+=2; // wShowWindow
            Memory.mem_writew(add, 0); add+=2; // cbReserved2
            Memory.mem_writed(add, 0); add+=4; // lpReserved2
            Memory.mem_writed(add, STD_IN); add+=4; // hStdInput
            Memory.mem_writed(add, STD_OUT); add+=4; // hStdOutput
            Memory.mem_writed(add, STD_ERROR); add+=4; // hStdError
        }
    };
    static private Callback.Handler GetStartupInfoW = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetStartupInfoW";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // HANDLE WINAPI GetStdHandle(DWORD nStdHandle)
    static private Callback.Handler GetStdHandle = new HandlerBase() {
        final int STD_INPUT_HANDLE = -10;
        final int STD_OUTPUT_HANDLE = -11;
        final int STD_ERROR_HANDLE = -12;

        public java.lang.String getName() {
            return "Kernel32.GetStdHandle";
        }
        public void onCall() {
            int param = CPU.CPU_Pop32();
            switch (param) {
                case STD_INPUT_HANDLE:
                    CPU_Regs.reg_eax.dword = STD_IN;
                    break;
                case STD_OUTPUT_HANDLE:
                    CPU_Regs.reg_eax.dword = STD_OUT;
                    break;
                case STD_ERROR_HANDLE:
                    CPU_Regs.reg_eax.dword = STD_ERROR;
                    break;
                default:
                    CPU_Regs.reg_eax.dword = WinAPI.INVALID_HANDLE_VALUE;
                    break;
            }
        }
    };

    // BOOL GetStringTypeA(LCID Locale, DWORD dwInfoType, LPCSTR lpSrcStr, int cchSrc, LPWORD lpCharType)
    static private Callback.Handler GetStringTypeA = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetStringTypeA";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // BOOL GetStringTypeW(DWORD dwInfoType, LPCWSTR lpSrcStr, int cchSrc, LPWORD lpCharType)
    private Callback.Handler GetStringTypeW = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetStringTypeW";
        }
        public void onCall() {
            int type = CPU.CPU_Pop32();
            int src = CPU.CPU_Pop32();
            int count = CPU.CPU_Pop32();
            int lpCharType = CPU.CPU_Pop32();

            CPU_Regs.reg_eax.dword = WinAPI.TRUE;
            if (count == -1) count = StringUtil.strlenW(src) + 1;
            switch(type)
            {
            case WinAPI.CT_CTYPE1:
                while (count-- > 0) {
                    char c = (char)Memory.mem_readw(src);
                    Memory.mem_writew(lpCharType, Unicode.get_char_typeW(c));
                    lpCharType+=2;
                    src+=2;
                }
                break;
            case WinAPI.CT_CTYPE2:
                while (count-- > 0) {
                    char c = (char)Memory.mem_readw(src);
                    Memory.mem_writew(lpCharType, Unicode.get_char_directionW(c));
                    lpCharType+=2;
                    src+=2;
                }
                break;
            case WinAPI.CT_CTYPE3:
                Console.out(getName()+" flag CT_CTYPE3 not implemented yet");
                notImplemented();
                break;
            default:
                CPU_Regs.reg_eax.dword = WinAPI.FALSE;
                WinSystem.getCurrentThread().setLastError(Error.ERROR_INVALID_PARAMETER);
            }
        }
    };

    /*
    typedef struct _SYSTEM_INFO {
      WORD wProcessorArchitecture;
      WORD wReserved;
      DWORD     dwPageSize;
      LPVOID    lpMinimumApplicationAddress;
      LPVOID    lpMaximumApplicationAddress;
      DWORD_PTR dwActiveProcessorMask;
      DWORD     dwNumberOfProcessors;
      DWORD     dwProcessorType;
      DWORD     dwAllocationGranularity;
      WORD      wProcessorLevel;
      WORD      wProcessorRevision;
    }
    */

    // void WINAPI GetSystemInfo(LPSYSTEM_INFO lpSystemInfo)
    static private Callback.Handler GetSystemInfo = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetSystemInfo";
        }
        public void onCall() {
            int add = CPU.CPU_Pop32();
            Memory.mem_writew(add, 0); // PROCESSOR_ARCHITECTURE_INTEL
            Memory.mem_writew(add + 2, 0); // Reserved
            Memory.mem_writed(add + 4, 4096); // Page Size
            Memory.mem_writed(add+8, 0x00010000); // :TODO: not sure if this matter, this is just what I say Windows 7 return
            Memory.mem_writed(add+12, 0x7ffeffff); // :TODO: not sure if this matter, this is just what I say Windows 7 return
            Memory.mem_writed(add+16, 255);
            Memory.mem_writed(add+20, 1); // Processor count
            Memory.mem_writed(add+24, 586); // Processor Type
            Memory.mem_writed(add+28, 4096); // Allocation Granulatiry Win 7 64-bit said 65536, but I think this might be better for here
            Memory.mem_writew(add + 32, 6); // :TODO: no idea
            Memory.mem_writew(add+34, 6660); // :TODO: no idea
        }
    };

    /*
        typedef struct _FILETIME {
            DWORD dwLowDateTime;
            DWORD dwHighDateTime;
        }
     */
    // void WINAPI GetSystemTimeAsFileTime(LPFILETIME lpSystemTimeAsFileTime)
    static private Callback.Handler GetSystemTimeAsFileTime = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetSystemTimeAsFileTime";
        }
        public void onCall() {
            int add = CPU.CPU_Pop32();
            long time = System.currentTimeMillis() * 10000l; // 100 nanosecond resolution
            int low = (int)time;
            int high = (int)(time >> 32);
            Memory.mem_writed(add, low);
            Memory.mem_writed(add+4, high);
        }
    };

    // DWORD WINAPI GetTickCount(void)
    private Callback.Handler GetTickCount = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetTickCount";
        }
        public void onCall() {
            CPU_Regs.reg_eax.dword = (int)(System.currentTimeMillis() - startTime);
        }
    };

    // int GetTimeFormat(LCID Locale, DWORD dwFlags, const SYSTEMTIME *lpTime, LPCTSTR lpFormat, LPTSTR lpTimeStr, int cchTime)
    static private Callback.Handler GetTimeFormatA = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetTimeFormatA";
        }
        public void onCall() {
            notImplemented();
        }
    };
    static private Callback.Handler GetTimeFormatW = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetTimeFormatW";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // DWORD WINAPI GetTimeZoneInformation(LPTIME_ZONE_INFORMATION lpTimeZoneInformation);
    static private Callback.Handler GetTimeZoneInformation = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetTimeZoneInformation";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // LCID GetUserDefaultLCID(void)
    static private Callback.Handler GetUserDefaultLCID = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetUserDefaultLCID";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // DWORD WINAPI GetVersion(void)
    static private Callback.Handler GetVersion = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetVersion";
        }
        public void onCall() {
            CPU_Regs.reg_eax.dword = 0x0A280105; // 5.2.2600 WinXP SP2
        }
    };

    /*
    typedef struct _OSVERSIONINFO {
      DWORD dwOSVersionInfoSize;
      DWORD dwMajorVersion;
      DWORD dwMinorVersion;
      DWORD dwBuildNumber;
      DWORD dwPlatformId;
      TCHAR szCSDVersion[128];
    } OSVERSIONINFO;

    typedef struct _OSVERSIONINFOEX {
      DWORD dwOSVersionInfoSize;
      DWORD dwMajorVersion;
      DWORD dwMinorVersion;
      DWORD dwBuildNumber;
      DWORD dwPlatformId;
      TCHAR szCSDVersion[128];
      WORD  wServicePackMajor;
      WORD  wServicePackMinor;
      WORD  wSuiteMask;
      BYTE  wProductType;
      BYTE  wReserved;
    }
    */

    // BOOL WINAPI GetVersionEx(LPOSVERSIONINFO lpVersionInfo)
    static private Callback.Handler GetVersionExA = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetVersionExA";
        }
        public void onCall() {
            int add = CPU.CPU_Pop32();
            int size = Memory.mem_readd(add);
            if (size == 148 || size == 156) {
                add+=4; // dwOSVersionInfoSize
                Memory.mem_writed(add, 5);add+=4; // dwMajorVersion
                Memory.mem_writed(add, 1);add+=4; // dwMinorVersion
                Memory.mem_writed(add, 2600);add+=4; // dwBuildNumber
                Memory.mem_writed(add, 2);add+=4; // dwPlatformId
                byte[] sp = "Service Pack 2".getBytes();
                Memory.mem_memcpy(add, sp, 0, sp.length);
                Memory.mem_writeb(add+sp.length, 0); add+=128;
                if (size == 156) {
                    Memory.mem_writew(add, 2);add+=2; // wServicePackMajor
                    Memory.mem_writew(add, 0);add+=2; // wServicePackMinor
                    Memory.mem_writew(add, 0);add+=2; // wSuiteMask
                    Memory.mem_writeb(add, 1);add+=1; // wProductType - VER_NT_WORKSTATION
                    Memory.mem_writeb(add, 0);add+=1; // wReserved
                }
            } else {
                Console.out(getName()+" was passed an unexpected size of "+size);
                Win.exit();
            }
        }
    };
    static private Callback.Handler GetVersionExW = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.GetVersionExW";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // LPVOID WINAPI HeapAlloc(HANDLE hHeap, DWORD dwFlags, SIZE_T dwBytes)
    private Callback.Handler HeapAlloc = new HandlerBase() {
        static final int HEAP_GENERATE_EXCEPTIONS = 0x00000004;
        static final int HEAP_NO_SERIALIZE = 0x00000001;
        static final int HEAP_ZERO_MEMORY = 0x00000008;

        public java.lang.String getName() {
            return "Kernel32.HeapAlloc";
        }
        public void onCall() {
            int hHeap = CPU.CPU_Pop32();
            int dwFlags = CPU.CPU_Pop32();
            int dwBytes = CPU.CPU_Pop32();
            if ((dwFlags & HEAP_GENERATE_EXCEPTIONS)!=0) {
                Console.out(getName()+" option HEAP_GENERATE_EXCEPTIONS not implemented yet");
                Win.exit();
            }
            CPU_Regs.reg_eax.dword = WinSystem.getCurrentProcess().getHeap().allocateHeap(hHeap, dwBytes);
        }
    };

    // HANDLE WINAPI HeapCreate(DWORD flOptions, SIZE_T dwInitialSize, SIZE_T dwMaximumSize)
    private Callback.Handler HeapCreate = new HandlerBase() {
        static private final int HEAP_CREATE_ENABLE_EXECUTE = 0x00040000;
        static private final int HEAP_GENERATE_EXCEPTIONS = 0x00000004;
        static private final int HEAP_NO_SERIALIZE = 0x00000001;

        public java.lang.String getName() {
            return "Kernel32.HeapCreate";
        }
        public void onCall() {
            int flOptions = CPU.CPU_Pop32();
            int dwInitialSize = (CPU.CPU_Pop32() + Paging.MEM_PAGE_SIZE) & (Paging.MEM_PAGE_SIZE-1);
            int dwMaximumSize = (CPU.CPU_Pop32() + Paging.MEM_PAGE_SIZE) & (Paging.MEM_PAGE_SIZE-1);
            if ((flOptions & HEAP_GENERATE_EXCEPTIONS)!=0) {
                Console.out(getName()+" option HEAP_GENERATE_EXCEPTIONS not implemented yet");
                Win.exit();
            } else {
                if (dwInitialSize==0)
                    dwInitialSize = Paging.MEM_PAGE_SIZE;
                CPU_Regs.reg_eax.dword = WinSystem.getCurrentProcess().getHeap().createHeap(dwInitialSize, dwMaximumSize);
            }
        }
    };

    // BOOL WINAPI HeapDestroy(HANDLE hHeap)
    static private Callback.Handler HeapDestroy = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.HeapDestroy";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // BOOL WINAPI HeapFree(HANDLE hHeap, DWORD dwFlags, LPVOID lpMem)
    private Callback.Handler HeapFree = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.HeapFree";
        }
        public void onCall() {
            int hHeap = CPU.CPU_Pop32();
            int dwFlags = CPU.CPU_Pop32();
            int lpMem = CPU.CPU_Pop32();
            CPU_Regs.reg_eax.dword = WinSystem.getCurrentProcess().getHeap().freeHeap(hHeap, lpMem);
        }
    };

    //LPVOID WINAPI HeapReAlloc(HANDLE hHeap, DWORD dwFlags, LPVOID lpMem, SIZE_T dwBytes)
    static private Callback.Handler HeapReAlloc = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.HeapReAlloc";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // BOOL WINAPI HeapValidate(HANDLE hHeap, DWORD dwFlags, LPCVOID lpMem)
    private Callback.Handler HeapValidate = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.HeapValidate";
        }
        public void onCall() {
            int heap = CPU.CPU_Pop32();
            int flags = CPU.CPU_Pop32();
            int address = CPU.CPU_Pop32();
            CPU_Regs.reg_eax.dword = WinSystem.getCurrentProcess().validateHeap(heap, flags, address);
        }
    };

    // void WINAPI InitializeCriticalSection(LPCRITICAL_SECTION lpCriticalSection)
    private Callback.Handler InitializeCriticalSection = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.InitializeCriticalSection";
        }
        public void onCall() {
            WinSystem.getCurrentThread().initializeCriticalSection(CPU.CPU_Pop32(), 0);
        }
    };

    // BOOL WINAPI InitializeCriticalSectionAndSpinCount(LPCRITICAL_SECTION lpCriticalSection, DWORD dwSpinCount)
    private Callback.Handler InitializeCriticalSectionAndSpinCount = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.InitializeCriticalSectionAndSpinCount";
        }
        public void onCall() {
            int address = CPU.CPU_Pop32();
            int spinCount = CPU.CPU_Pop32();
            WinSystem.getCurrentThread().initializeCriticalSection(address, spinCount);
            CPU_Regs.reg_eax.dword = WinAPI.TRUE;
        }
    };

    // LONG __cdecl InterlockedDecrement(LONG volatile *Addend)
    static private Callback.Handler InterlockedDecrement = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.InterlockedDecrement";
        }
        public void onCall() {
            int address = CPU.CPU_Peek32(0);
            int value = Memory.mem_readd(address);
            value--;
            Memory.mem_writed(address, value);
            CPU_Regs.reg_eax.dword = value;
        }
    };

    // LONG __cdecl InterlockedExchange(LONG volatile *Target, LONG Value)
    static private Callback.Handler InterlockedExchange = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.InterlockedExchange";
        }
        public void onCall() {
            notImplemented();
            // :TODO: notice _cdecl, don't pop stack
        }
    };

    // LONG __cdecl InterlockedIncrement(LONG volatile *Addend)
    static private Callback.Handler InterlockedIncrement = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.InterlockedIncrement";
        }
        public void onCall() {
            int address = CPU.CPU_Peek32(0);
            int value = Memory.mem_readd(address);
            value++;
            Memory.mem_writed(address, value);
            CPU_Regs.reg_eax.dword = value;
        }
    };

    // BOOL WINAPI IsBadReadPtr(const VOID *lp, UINT_PTR ucb)
    static private Callback.Handler IsBadReadPtr = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.IsBadReadPtr";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // BOOL WINAPI IsDebuggerPresent(void)
    static private Callback.Handler IsDebuggerPresent = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.IsDebuggerPresent";
        }
        public void onCall() {
            CPU_Regs.reg_eax.dword = WinAPI.FALSE;
        }
    };

    // BOOL IsValidCodePage(UINT CodePage)
    static private Callback.Handler IsValidCodePage = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.IsValidCodePage";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // BOOL IsValidLocale(LCID Locale, DWORD dwFlags)
    static private Callback.Handler IsValidLocale = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.IsValidLocale";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // Direct port of Wine's function
    //
    // int LCMapString(LCID Locale, DWORD dwMapFlags, LPCTSTR lpSrcStr, int cchSrc, LPTSTR lpDestStr, int cchDest)
    static private Callback.Handler LCMapStringA = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.LCMapStringA";
        }
        public void onCall() {
            notImplemented();
        }
    };
    private Callback.Handler LCMapStringW = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.LCMapStringW";
        }
        public void onCall() {
            int lcid = CPU.CPU_Pop32();
            int flags = CPU.CPU_Pop32();
            int src = CPU.CPU_Pop32();
            int srclen = CPU.CPU_Pop32();
            int dst = CPU.CPU_Pop32();
            int dstlen = CPU.CPU_Pop32();
            int dst_ptr;

            if (src==0 || srclen==0 || dstlen < 0)
            {
                WinSystem.getCurrentThread().setLastError(Error.ERROR_INVALID_PARAMETER);
                CPU_Regs.reg_eax.dword = 0;
                return;
            }

            /* mutually exclusive flags */
            if ((flags & (Locale.LCMAP_LOWERCASE | Locale.LCMAP_UPPERCASE)) == (Locale.LCMAP_LOWERCASE | Locale.LCMAP_UPPERCASE) ||
                (flags & (Locale.LCMAP_HIRAGANA | Locale.LCMAP_KATAKANA)) == (Locale.LCMAP_HIRAGANA | Locale.LCMAP_KATAKANA) ||
                (flags & (Locale.LCMAP_HALFWIDTH | Locale.LCMAP_FULLWIDTH)) == (Locale.LCMAP_HALFWIDTH | Locale.LCMAP_FULLWIDTH) ||
                (flags & (Locale.LCMAP_TRADITIONAL_CHINESE | Locale.LCMAP_SIMPLIFIED_CHINESE)) == (Locale.LCMAP_TRADITIONAL_CHINESE | Locale.LCMAP_SIMPLIFIED_CHINESE))
            {
                WinSystem.getCurrentThread().setLastError(Error.ERROR_INVALID_FLAGS);
                CPU_Regs.reg_eax.dword = 0;
                return;
            }

            if (dstlen==0) dst = 0;

            if ((flags & Locale.LCMAP_SORTKEY)!=0)
            {
                int ret = 0;
                if (src == dst)
                {
                    WinSystem.getCurrentThread().setLastError(Error.ERROR_INVALID_FLAGS);
                    CPU_Regs.reg_eax.dword = 0;
                    return;
                }

                if (srclen < 0) srclen = StringUtil.strlenW(src);

                Console.out(getName()+" LCMAP_SORTKEY not implemented yet");
                notImplemented();
                // ret = wine_get_sortkey(flags, src, srclen, (char *)dst, dstlen);
                if (ret == 0) {
                    WinSystem.getCurrentThread().setLastError(Error.ERROR_INSUFFICIENT_BUFFER);
                } else {
                    ret++;
                }
                CPU_Regs.reg_eax.dword = 0;
                return;
            }

            /* SORT_STRINGSORT must be used exclusively with LCMAP_SORTKEY */
            if ((flags & Locale.SORT_STRINGSORT)!=0)
            {
                WinSystem.getCurrentThread().setLastError(Error.ERROR_INVALID_FLAGS);
                CPU_Regs.reg_eax.dword = 0;
                return;
            }

            if (srclen < 0) srclen = StringUtil.strlenW(src) + 1;

            if (dst==0) /* return required string length */
            {
                int len;

                for (len = 0; srclen!=0; src+=2, srclen--)
                {
                    char wch = (char)Memory.mem_readw(src);
                    /* tests show that win2k just ignores NORM_IGNORENONSPACE,
                     * and skips white space and punctuation characters for
                     * NORM_IGNORESYMBOLS.
                     */
                    if ((flags & Locale.NORM_IGNORESYMBOLS)!=0 && (Unicode.get_char_typeW(wch) & (Unicode.C1_PUNCT | Unicode.C1_SPACE))!=0)
                        continue;
                    len++;
                }
                CPU_Regs.reg_eax.dword = len;
                return;
            }

            if ((flags & Locale.LCMAP_UPPERCASE)!=0)
            {
                for (dst_ptr = dst; srclen!=0 && dstlen!=0; src+=2, srclen--)
                {
                    char wch = (char)Memory.mem_readw(src);
                    if ((flags & Locale.NORM_IGNORESYMBOLS)!=0 && (Unicode.get_char_typeW(wch) & (Unicode.C1_PUNCT | Unicode.C1_SPACE))!=0)
                        continue;
                    Memory.mem_writew(dst_ptr, StringUtil.toupperW(wch));
                    dst_ptr+=2;
                    dstlen--;
                }
            }
            else if ((flags & Locale.LCMAP_LOWERCASE)!=0)
            {
                for (dst_ptr = dst; srclen!=0 && dstlen!=0; src+=2, srclen--)
                {
                    char wch = (char)Memory.mem_readw(src);
                    if ((flags & Locale.NORM_IGNORESYMBOLS)!=0 && (Unicode.get_char_typeW(wch) & (Unicode.C1_PUNCT | Unicode.C1_SPACE))!=0)
                        continue;

                    Memory.mem_writew(dst_ptr, StringUtil.tolowerW(wch));
                    dst_ptr+=2;
                    dstlen--;
                }
            }
            else
            {
                if (src == dst)
                {
                    WinSystem.getCurrentThread().setLastError(Error.ERROR_INVALID_FLAGS);
                    CPU_Regs.reg_eax.dword = 0;
                    return;
                }
                for (dst_ptr = dst; srclen!=0 && dstlen!=0; src+=2, srclen--)
                {
                    char wch = (char)Memory.mem_readw(src);
                    if ((flags & Locale.NORM_IGNORESYMBOLS)!=0 && (Unicode.get_char_typeW(wch) & (Unicode.C1_PUNCT | Unicode.C1_SPACE))!=0)
                        continue;
                    Memory.mem_writew(dst_ptr, wch);
                    dst_ptr+=2;
                    dstlen--;
                }
            }

            if (srclen>0)
            {
                WinSystem.getCurrentThread().setLastError(Error.ERROR_INSUFFICIENT_BUFFER);
                CPU_Regs.reg_eax.dword = 0;
                return;
            }

            CPU_Regs.reg_eax.dword = dst_ptr - dst;
        }
    };

    // void WINAPI LeaveCriticalSection(LPCRITICAL_SECTION lpCriticalSection)
    private Callback.Handler LeaveCriticalSection = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.LeaveCriticalSection";
        }
        public void onCall() {
            WinSystem.getCurrentThread().leaveCriticalSection(CPU.CPU_Pop32());
        }
    };

    // HMODULE WINAPI LoadLibrary(LPCTSTR lpFileName)
    private Callback.Handler LoadLibraryA = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.LoadLibraryA";
        }
        public void onCall() {
            String name = new LittleEndianFile(CPU.CPU_Pop32()).readCString();
            CPU_Regs.reg_eax.dword = WinSystem.getCurrentProcess().loadModule(name);
        }
    };

    static private Callback.Handler LoadLibraryW = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.LoadLibraryW";
        }
        public void onCall() {
            String name = new LittleEndianFile(CPU.CPU_Pop32()).readCStringW();
            CPU_Regs.reg_eax.dword = WinSystem.getCurrentProcess().loadModule(name);
        }
    };

    // int WINAPI lstrlen(LPCTSTR lpString)
    static private Callback.Handler lstrlenA = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.lstrlenA";
        }
        public void onCall() {
            notImplemented(); // Needs testing
            int lpString = CPU.CPU_Pop32();
            int len = 0;
            while (Memory.mem_readb(lpString++)!=0) {
                len++;
            }
            CPU_Regs.reg_eax.dword = len;
        }
    };
    static private Callback.Handler lstrlenW = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.lstrlenW";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // LPVOID WINAPI MapViewOfFile(HANDLE hFileMappingObject, DWORD dwDesiredAccess, DWORD dwFileOffsetHigh, DWORD dwFileOffsetLow, SIZE_T dwNumberOfBytesToMap)
    private Callback.Handler MapViewOfFile = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.MapViewOfFile";
        }
        public void onCall() {
            int hFileMappingObject = CPU.CPU_Pop32();
            int dwDesiredAccess = CPU.CPU_Pop32();
            int dwFileOffsetHigh = CPU.CPU_Pop32();
            int dwFileOffsetLow = CPU.CPU_Pop32();
            int dwNumberOfBytesToMap = CPU.CPU_Pop32();
            WinObject object = WinSystem.getObject(hFileMappingObject);
            if (object == null || !(object instanceof FileMapping)) {
                WinSystem.getCurrentThread().setLastError(Error.ERROR_INVALID_HANDLE);
                CPU_Regs.reg_eax.dword = 0;
            }
            FileMapping mapping = (FileMapping)object;
            if (mapping.fileHandle != -1) {
                Console.out("MapViewOfFile does not support working against real files yet");
                notImplemented();
            }
            mapping.heapHandle = WinSystem.getCurrentProcess().getHeap().allocateHeap(WinSystem.getCurrentProcess().getHeapHandle(), dwNumberOfBytesToMap);
            mapping.heapSize = dwNumberOfBytesToMap;
            CPU_Regs.reg_eax.dword = mapping.heapHandle;
        }
    };

    // int MultiByteToWideChar(UINT CodePage, DWORD dwFlags, LPCSTR lpMultiByteStr, int cbMultiByte, LPWSTR lpWideCharStr, int cchWideChar)
    private Callback.Handler MultiByteToWideChar = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.MultiByteToWideChar";
        }
        public void onCall() {
            int CodePage = CPU.CPU_Pop32();
            int dwFlags = CPU.CPU_Pop32();
            int lpMultiByteStr = CPU.CPU_Pop32();
            int cbMultiByte = CPU.CPU_Pop32();
            int lpWideCharStr = CPU.CPU_Pop32();
            int cchWideChar = CPU.CPU_Pop32();
            switch (CodePage) {
                case 1252:
                    LittleEndianFile file = new LittleEndianFile(lpMultiByteStr, cbMultiByte);
                    java.lang.String result = file.readCString();
                    char[] c = result.toCharArray();
                    if (cchWideChar == 0) {
                        CPU_Regs.reg_eax.dword = c.length+1;
                    } else if (cchWideChar < c.length+1) {
                        CPU_Regs.reg_eax.dword = 0;
                        WinSystem.getCurrentThread().setLastError(Error.ERROR_INSUFFICIENT_BUFFER);
                    } else if (lpWideCharStr == 0) {
                        CPU_Regs.reg_eax.dword = 0;
                        WinSystem.getCurrentThread().setLastError(Error.ERROR_INVALID_PARAMETER);
                    } else {
                        CPU_Regs.reg_eax.dword = c.length+1;
                        for (int i=0;i<c.length;i++) {
                            Memory.mem_writew(lpWideCharStr, c[i]);
                            lpWideCharStr+=2;
                        }
                        Memory.mem_writew(lpWideCharStr, 0);
                    }
                    break;
                default:
                    Console.out(getName()+" CodePage "+CodePage+" not implemented yet");
                    notImplemented();
                    return;
            }
        }
    };
    
    // void WINAPI OutputDebugString(LPCTSTR lpOutputString)
    static private Callback.Handler OutputDebugStringW = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.OutputDebugStringW";
        }
        public void onCall() {
            int lpOutputString = CPU.CPU_Pop32();
            System.out.println(new LittleEndianFile(lpOutputString).readCStringW());
        }
    };
    static private Callback.Handler OutputDebugStringA = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.OutputDebugStringA";
        }
        public void onCall() {
            int lpOutputString = CPU.CPU_Pop32();
            System.out.println(new LittleEndianFile(lpOutputString).readCString());
        }
    };

    // BOOL WINAPI QueryPerformanceCounter(LARGE_INTEGER *lpPerformanceCount)
    private Callback.Handler QueryPerformanceCounter = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.QueryPerformanceCounter";
        }
        public void onCall() {
            int add = CPU.CPU_Pop32();
            long time = System.nanoTime() * 21 / 17600; // 1GHz to 1.193182 MHz
            int low = (int)time;
            int high = (int)(time >> 32);
            Memory.mem_writed(add, low);
            Memory.mem_writed(add + 4, high);
            CPU_Regs.reg_eax.dword = WinAPI.TRUE;
        }
    };

    // void WINAPI RaiseException(DWORD dwExceptionCode, DWORD dwExceptionFlags, DWORD nNumberOfArguments, const ULONG_PTR *lpArguments)
    private Callback.Handler RaiseException = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.RaiseException";
        }
        public void onCall() {
            Console.out("RaiseException was called\n");
            Win.exit();
        }
    };

    // void WINAPI RtlUnwind(PVOID TargetFrame, PVOID TargetIp, PEXCEPTION_RECORD ExceptionRecord, PVOID ReturnValue)
    static private Callback.Handler RtlUnwind = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.RtlUnwind";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // BOOL WINAPI SetConsoleCtrlHandler(PHANDLER_ROUTINE HandlerRoutine, BOOL Add)
    private Callback.Handler SetConsoleCtrlHandler = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.SetConsoleCtrlHandler";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // DWORD WINAPI SetFilePointer(HANDLE hFile, LONG lDistanceToMove, PLONG lpDistanceToMoveHigh, DWORD dwMoveMethod)
    private Callback.Handler SetFilePointer = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.SetFilePointer";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // UINT SetHandleCount(UINT uNumber)
    private Callback.Handler SetHandleCount = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.SetHandleCount";
        }
        public void onCall() {
            // This only did something interesting on Win32s
            CPU_Regs.reg_eax.dword = CPU.CPU_Pop32();
        }
    };

    // void WINAPI SetLastError(DWORD dwErrCode)
    private Callback.Handler SetLastError = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.SetLastError";
        }
        public void onCall() {
            WinSystem.getCurrentThread().setLastError(CPU.CPU_Pop32());
        }
    };

    // BOOL WINAPI SetStdHandle(DWORD nStdHandle, HANDLE hHandle)
    private Callback.Handler SetStdHandle = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.SetStdHandle";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // LPTOP_LEVEL_EXCEPTION_FILTER WINAPI SetUnhandledExceptionFilter(LPTOP_LEVEL_EXCEPTION_FILTER lpTopLevelExceptionFilter)
    private Callback.Handler SetUnhandledExceptionFilter = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.SetUnhandledExceptionFilter";
        }
        public void onCall() {
            CPU.CPU_Pop32();
            CPU_Regs.reg_eax.dword = 0;
        }
    };

    // BOOL WINAPI TerminateProcess(HANDLE hProcess, UINT uExitCode)
    static private Callback.Handler TerminateProcess = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.TerminateProcess";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // DWORD WINAPI TlsAlloc(void)
    private Callback.Handler TlsAlloc = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.TlsAlloc";
        }
        public void onCall() {
            CPU_Regs.reg_eax.dword = WinSystem.getCurrentThread().tlsAlloc();
        }
    };

    // BOOL WINAPI TlsFree(DWORD dwTlsIndex)
    private Callback.Handler TlsFree = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.TlsFree";
        }
        public void onCall() {
            CPU_Regs.reg_eax.dword = WinSystem.getCurrentThread().tlsFree(CPU.CPU_Pop32());
        }
    };

    // LPVOID WINAPI TlsGetValue(DWORD dwTlsIndex)
    private Callback.Handler TlsGetValue = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.TlsGetValue";
        }
        public void onCall() {
            CPU_Regs.reg_eax.dword = WinSystem.getCurrentThread().tlsGetValue(CPU.CPU_Pop32());
        }
    };

    // BOOL WINAPI TlsSetValue(DWORD dwTlsIndex, LPVOID lpTlsValue)
    private Callback.Handler TlsSetValue = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.TlsSetValue";
        }
        public void onCall() {
            int index = CPU.CPU_Pop32();
            int value = CPU.CPU_Pop32();
            CPU_Regs.reg_eax.dword = WinSystem.getCurrentThread().tlsSetValue(index, value);
        }
    };

    // LONG WINAPI UnhandledExceptionFilter(struct _EXCEPTION_POINTERS *ExceptionInfo)
    private Callback.Handler UnhandledExceptionFilter = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.UnhandledExceptionFilter";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // LPVOID WINAPI VirtualAlloc(LPVOID lpAddress, SIZE_T dwSize, DWORD flAllocationType, DWORD flProtect)
    private Callback.Handler VirtualAlloc = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.VirtualAlloc";
        }
        public void onCall() {
            int address = CPU.CPU_Pop32();
            int size = CPU.CPU_Pop32();
            int flags = CPU.CPU_Pop32();
            int protect = CPU.CPU_Pop32();
            CPU_Regs.reg_eax.dword = WinSystem.getCurrentProcess().getMemory().virtualAlloc(address, size, flags, protect);
        }
    };

    // BOOL WINAPI VirtualFree(LPVOID lpAddress, SIZE_T dwSize, DWORD dwFreeType)
    private Callback.Handler VirtualFree = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.VirtualFree";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // SIZE_T WINAPI VirtualQuery(LPCVOID lpAddress, PMEMORY_BASIC_INFORMATION lpBuffer, SIZE_T dwLength)
    private Callback.Handler VirtualQuery = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.VirtualQuery";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // DWORD WINAPI WaitForSingleObject(HANDLE hHandle, DWORD dwMilliseconds)
    private Callback.Handler WaitForSingleObject = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.WaitForSingleObject";
        }
        public void onCall() {
            int handle = CPU.CPU_Pop32();
            int timeout = CPU.CPU_Pop32();
            WinObject object = WinSystem.getObject(handle);
            if (object == null || !(object instanceof WaitObject)) {
                CPU_Regs.reg_eax.dword = -1;
                WinSystem.getCurrentThread().setLastError(Error.ERROR_INVALID_HANDLE);
            } else {
                WaitObject waitObject = (WaitObject)object;
                waitObject.wait(WinSystem.getCurrentThread(), timeout);
            }
        }
    };

    // BOOL WINAPI WriteConsole(HANDLE hConsoleOutput, const VOID *lpBuffer, DWORD nNumberOfCharsToWrite, LPDWORD lpNumberOfCharsWritten, LPVOID lpReserved)
    private Callback.Handler WriteConsoleA = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.WriteConsoleA";
        }
        public void onCall() {
            notImplemented();
        }
    };
    private Callback.Handler WriteConsoleW = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.WriteConsoleW";
        }
        public void onCall() {
            notImplemented();
        }
    };

    // BOOL WINAPI WriteFile(HANDLE hFile, LPCVOID lpBuffer, DWORD nNumberOfBytesToWrite, LPDWORD lpNumberOfBytesWritten, LPOVERLAPPED lpOverlapped)
    static private Callback.Handler WriteFile = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.WriteFile";
        }
        public void onCall() {
            int hFile = CPU.CPU_Pop32();
            int lpBuffer = CPU.CPU_Pop32();
            int nNumberOfBytesToWrite = CPU.CPU_Pop32();
            int lpNumberOfBytesWritten = CPU.CPU_Pop32();
            int lpOverlapped = CPU.CPU_Pop32();
            if (hFile == 1) {
                byte[] buffer = new byte[nNumberOfBytesToWrite];
                Memory.mem_memcpy(buffer, 0, lpBuffer, nNumberOfBytesToWrite);
                Console.out(new java.lang.String(buffer));
                if (lpNumberOfBytesWritten != 0)
                    Memory.mem_writed(lpNumberOfBytesWritten, nNumberOfBytesToWrite);
                CPU_Regs.reg_eax.dword = WinAPI.TRUE;
            } else {
                notImplemented();
                CPU_Regs.reg_eax.dword = WinAPI.FALSE;
            }
        }
    };

    static private boolean[] c1252 = new boolean[] {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
                                                    true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
                                                    true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
                                                    true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,

                                                    true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
                                                    true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
                                                    true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
                                                    true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,

                                                    false, true, false, false, false, false, false, false, false, false, false, false, false, true, false, true,
                                                    true, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false,
                                                    true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
                                                    true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,

                                                    true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
                                                    true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
                                                    true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
                                                    true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true};

    // int WideCharToMultiByte(UINT CodePage, DWORD dwFlags, LPCWSTR lpWideCharStr, int cchWideChar, LPSTR lpMultiByteStr, int cbMultiByte, LPCSTR lpDefaultChar, LPBOOL lpUsedDefaultChar)
    private Callback.Handler WideCharToMultiByte = new HandlerBase() {
        public java.lang.String getName() {
            return "Kernel32.WideCharToMultiByte";
        }
        public void onCall() {
            int CodePage = CPU.CPU_Pop32();
            int dwFlags = CPU.CPU_Pop32();
            int lpWideCharStr = CPU.CPU_Pop32();
            int cchWideChar = CPU.CPU_Pop32();
            int lpMultiByteStr = CPU.CPU_Pop32();
            int cbMultiByte = CPU.CPU_Pop32();
            int lpDefaultChar = CPU.CPU_Pop32();
            int lpUsedDefaultChar = CPU.CPU_Pop32();
            switch (CodePage) {
                case 0: // CP_ACP
                case 1252:
                    if (cbMultiByte == 0) {
                        CPU_Regs.reg_eax.dword = cchWideChar;
                    } else if (cchWideChar == 0 || lpWideCharStr == 0) {
                        CPU_Regs.reg_eax.dword = 0;
                        WinSystem.getCurrentThread().setLastError(Error.ERROR_INVALID_PARAMETER);
                    } else if (cbMultiByte < cchWideChar) {
                        CPU_Regs.reg_eax.dword = 0;
                        WinSystem.getCurrentThread().setLastError(Error.ERROR_INSUFFICIENT_BUFFER);
                    } else {
                        byte defaultChar = 63;
                        int defaultCount = 0;
                        if (lpDefaultChar != 0)
                            defaultChar = (byte)Memory.mem_readb(lpDefaultChar);
                        for (int i=0;i<cchWideChar;i++) {
                            int c = Memory.mem_readw(lpWideCharStr);
                            lpWideCharStr+=2;
                            if (c>=c1252.length || !c1252[c])  {
                                c = defaultChar;
                                defaultCount++;
                            }
                            Memory.mem_writeb(lpMultiByteStr, c);
                            lpMultiByteStr+=1;
                        }
                        if (lpUsedDefaultChar != 0)
                            Memory.mem_writed(lpUsedDefaultChar, defaultCount==0?WinAPI.FALSE:WinAPI.TRUE);
                        CPU_Regs.reg_eax.dword = cchWideChar;
                    }
                    break;
                default:
                    Console.out(getName()+" CodePage "+CodePage+" not implemented yet");
                    notImplemented();
            }
        }
    };
}