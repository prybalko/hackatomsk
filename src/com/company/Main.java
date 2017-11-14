package com.company;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Main {


    public static void main(String[] args) {
        Set<Thread> threads = new LinkedHashSet<Thread>();
        for(int i=0; i<10; i++) {
            Thread t = new Thread(new CalcTask(i));
            t.start();
            threads.add(t);
        }
        Iterator<Thread> itr = threads.iterator();
        while(itr.hasNext()){
            Thread t = itr.next();
            while (t.getState() == Thread.State.TIMED_WAITING) {}
            synchronized(t) {
                t.interrupt();
                try {
                    t.join();
                }
                catch(InterruptedException ex) {}
            }

        }
    }
}

class CalcTask implements Runnable {
    private Integer number;

    CalcTask(Integer x)
    {
        number = x;
    }

    public void run() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, 1000 );
        try {
            Thread.sleep(randomNum);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
        synchronized(this){
            try {
                wait();
            }
            catch(InterruptedException ex) {
                System.out.println(2*number);
            }
        }

    }
}
