package jdos.win.utils;

import jdos.hardware.Memory;

public class WinCriticalException {
//    public class CriticalSection {
//        int DebugInfo; // pointer
//
//        //
//        //  The following three fields control entering and exiting the critical
//        //  section for the resource
//        //
//
//        public int LockCount;
//        public int RecursionCount;
//        public int OwningThread; // handle       // from the thread's ClientId->UniqueThread
//        public int LockSemaphore; // handle
//        public int SpinCount;
//    }

    static public void initialize(int address, int spinCount) {
        WaitObject object = WinSystem.createWaitObject();
        Memory.mem_writed(address, object.getHandle()); address+=4;
        Memory.mem_writed(address, 0xFFFFFFFF); address+=4;
        Memory.mem_writed(address, 0); address+=4;
        Memory.mem_writed(address, 0); address+=4;
        Memory.mem_writed(address, 0); address+=4;
        Memory.mem_writed(address, spinCount); address+=4;
    }

    static public void enter(int address) {
        WinThread thread = WinSystem.getCurrentThread();
        int handle = thread.handle;

        int lockCount = Memory.mem_readd(address + 4);
        lockCount++;
        Memory.mem_writed(address+4, lockCount);
        int threadId = Memory.mem_readd(address+12);
        if (threadId == 0) {
            Memory.mem_writed(address+12, handle);
            threadId = handle;
        }
        if (threadId == handle) {
            int recursionCount = Memory.mem_readb(address+8);
            recursionCount++;
            Memory.mem_writed(address+8, recursionCount);
        } else {
            WaitObject object = (WaitObject)WinSystem.getObject(Memory.mem_readd(address));
            object.waiting.add(thread);
            WinSystem.scheduler.removeThread(thread, true);
        }
    }

    static public void leave(int address) {
        int lockCount = Memory.mem_readd(address+4);
        lockCount--;
        Memory.mem_writed(address+4, lockCount);

        int recursionCount = Memory.mem_readb(address+8);
        recursionCount--;
        if (recursionCount>0) {
            Memory.mem_writed(address+8, recursionCount);
        } else {
            WaitObject object = (WaitObject)WinSystem.getObject(Memory.mem_readd(address));
            if (object.waiting.size()>0) {
                WinThread thread = (WinThread)object.waiting.remove(0);
                Memory.mem_writed(address+12, thread.getHandle()); // set new owner
                WinSystem.scheduler.addThread(thread, false); // wake up the waiting thread
                // leave recursion count at 1

            } else {
                Memory.mem_writed(address+12, 0);
                Memory.mem_writed(address+8, 0);
            }
        }
    }

    static public void delete(int address) {
        WaitObject object = (WaitObject)WinSystem.getObject(Memory.mem_readd(address));
        object.close();
    }
}