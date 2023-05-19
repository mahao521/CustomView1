package com.mahao.customview.thread.synchron;

public class Person {

    String personName;

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public synchronized String getPersonName() {
        return personName;
    }

    public synchronized void readBook(Book book) {
        System.out.println("执行了person");
        for (int i = 0; i < 10; i++) {
            if (i < 5) {
                try {
                    System.out.println("person线程等待，让出资源");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("i == " + i);
            } else {
                System.out.println("需要book对象，线程阻塞");
                System.out.println(book.getName() + "i = " + i);
            }

        }
    }

/*
    public synchronized void setPersonName(){
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
*/


}
