package com.mahao.customview.datastruct;

public class DataStruct {


    //第一个和第二个比  地二个和第三个比
    public void maopao(int[] arr) {

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {

                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j + 1];
                    arr[j + 1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }

    //第一个和第二，第三个
    public void choice(int[] brr) {

        for (int i = 0; i < brr.length; i++) {

            for (int j = i; j < brr.length; j++) {

                if (brr[i] > brr[j]) {

                }
            }
        }
    }


    public void reverse(Node node) {

        Node head = node;
        Node curr;
        Node prev = null;

        while (head != null) {
            //取出来第一个节点
            curr = head.next;
            //头插
            head.next = prev;
            prev = head;
            head = curr;

        }
    }


    class Node {

        Node next;
        int data;
    }
}
