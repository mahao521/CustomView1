package com.mahao.customview.thread.synchron;

public class Book {

    public String name;

    public void setName(String name) {
        this.name = name;
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized void subscribeByPerson(Person person) {
        System.out.println("执行了book------");
        for (int i = 0; i < 10; i++) {
            if (i == 0) {
                try {
                    System.out.println("book线程等待，让出资源");
                    Thread.sleep(100);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("book等待结束，需要person对象---阻塞");
            System.out.println(person.getPersonName() + "  " + i);
        }
    }

}
