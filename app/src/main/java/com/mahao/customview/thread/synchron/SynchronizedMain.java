package com.mahao.customview.thread.synchron;

public class SynchronizedMain {

    //1 、t1执行，book被锁住,线程等待，t2执行
    //2 、t2执行  Person被锁住，线程t1执行
    //3、t1等待结束，需要person，book 阻塞。
    //4、t2等待结束执行，t2 需要book  Person阻塞。


    public static void main(String[] args) {

        Book book = new Book();
        book.setName("一千零一夜");
        Person person = new Person();
        person.setPersonName("张三丰");

        Thread t1 = new Thread() {
            @Override
            public void run() {
                super.run();
                book.subscribeByPerson(person);
            }
        };

        Thread t2 = new Thread() {
            @Override
            public void run() {
                super.run();
                person.readBook(book);
            }
        };

        t1.start();
        t2.start();

    }

}
