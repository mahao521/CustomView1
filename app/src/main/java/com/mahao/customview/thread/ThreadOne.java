package com.mahao.customview.thread;

public class ThreadOne extends Thread {

    private int i = 1000;

    @Override
    public void run() {
        super.run();
        for (; i > 0 ; i--) {
            if(i <= 2){
                System.out.println(getName() + " ------ " + i);
            }else {
                System.out.println(getName() + " " + i);
            }

        }
    }


}
