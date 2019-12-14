package com.shasratech.s_c_31;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author srihari
 */
public class MySemaphore {
    String ThreadName = "";
    Semaphore sem;
    String Lock_Name;

    public String getLock_Name() {
        return Lock_Name;
    }

    public void setLock_Name(String Lock_Name) {
        this.Lock_Name = Lock_Name;
    }

    public MySemaphore() {
        sem = new Semaphore(1);
        //SQL_stmt = new Statement();
    }

    public boolean IsSQL_Sem_Locked () {
        //System.out.println("sem.availablePermits() = " + sem.availablePermits());
        return (sem.availablePermits() < 1);
    }

    public boolean Sem_acquire() {
        try {
            sem.acquire();
            return true;
        } catch (InterruptedException ex) {
        }
        return false;
    }

    public void Release_Resources() {
        if (sem != null) {
            sem.release();
            sem = null;
        }
    }

    public void Sem_Release () {
        sem.release();
    }
}
