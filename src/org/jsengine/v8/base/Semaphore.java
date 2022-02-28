package org.jsengine.v8.base;

public class Semaphore {
    private NativeHandle native_handle_;
    public Semaphore(int count) {
        this.native_handle_ = new NativeHandle(count);
    }

    public void signal() {
        this.native_handle_.lock();
    }

    public void Wait() {
        this.native_handle_.unlock();
    }

    public boolean waitFor() {
        return false;
    }

    private static class NativeHandle extends java.util.concurrent.Semaphore {
        public NativeHandle(int count) {
            super(count);
        }

        public void lock() {
            try {
                this.acquire();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void unlock() {
            this.release();
        }
    }
}
