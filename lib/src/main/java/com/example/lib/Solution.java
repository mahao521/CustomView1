package com.example.lib;

public class Solution {

    /**
     * Definition for singly-linked l`ist.
     * public class ListNode {
     * int val;
     * ListNode next;
     * ListNode() {}
     * ListNode(int val) { this.val = val; }
     * ListNode(int val, ListNode next) { this.val = val; this.next = next; }
     * }
     */
    public static ListNode reverseBetween(ListNode head, int left, int right) {

        ListNode node = head;
        ListNode after = head;

        ListNode reverse = null;
        int leftCount = 1;

        if (node != null) {

            if (left == right) {
                return head;
            }

            while (leftCount < left) {
                System.out.println("node == " + node.val);
                node = node.next;
                leftCount++;
            }
            after = node.next;

            // node = 2 , before =1  ,after = 3
            System.out.println(node.val + " " + after.val);
            ListNode afterNext = after.next;
            reverse = node;
            reverse.next = null;
            ListNode temp = after;
            while (leftCount < right && temp != null) {
                afterNext = temp.next;
                temp.next = reverse;
                reverse = temp;
                temp = afterNext;
                leftCount++;
            }
            // [-3,-2,3,-5,3,4,-4]
            System.out.println("node = " + node.val);
            System.out.println("rever = " + reverse.val);
            System.out.println("heade = " + head.val);

            ListNode newNode = head;
            while (newNode != null && newNode.next != null) {
                System.out.println("newNode >>>  " + newNode.val);
                if (newNode.next.equals(node)) {
                    newNode.next = null;
                    break;
                } else {
                    newNode = newNode.next;
                }
            }
            System.out.println("newNode >>> > " + newNode.val);
            if (newNode.equals(node)) {
                newNode = reverse;
                head = reverse;
                System.out.println("相等");
            } else {
                newNode.next = reverse;
                System.out.println("不相等");
            }
            // [-3,-2,3,-5,3,4,-4]
            printLn(head);
            while (newNode != null) {
                System.out.println("reverse ---- " + newNode.val);
                if (newNode.next == null) {
                    newNode.next = afterNext;
                    break;
                }
                newNode = newNode.next;
            }
        }
        System.out.println("final ");
        return head;
    }

    public static void main(String[] args) {

        //    [-3,-2,3,-5,3,4,-4]
        //     5
        //      7
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);
        ListNode node6 = new ListNode(6);
        ListNode node7 = new ListNode(7);
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        node5.next = node6;
        node6.next = node7;

        ListNode node11 = new ListNode(5);
        ListNode node22 = new ListNode(6);
        ListNode node33 = new ListNode(4);
        node11.next = node22;
        node22.next = node33;

        //ListNode node = reverseBetween(node1, 5, 7);
        ListNode node = reverse2(node1);
        while (node != null) {
            System.out.println("结果展示" + node.val);
            node = node.next;
        }
    }

    public static void printLn(ListNode node) {
        int count = 0;
        while (node != null) {
            System.out.println("打印 node = " + node.val);
            node = node.next;
            count++;
            if (count == 7) {
                break;
            }
        }
    }

    public static ListNode reverse(ListNode node) {

        ListNode temp = node;
        ListNode reverse = null;
        if (temp == null) return node;

        while (temp != null) {
            ListNode aa = temp;
            ListNode aaNext = aa.next;
            if (reverse == null) {
                reverse = aa;
                reverse.next = null;
            } else {
                aa.next = reverse;
                reverse = aa;
            }
            temp = aaNext;
        }
        return reverse;
    }


    public static ListNode reverse2 (ListNode listNode){

        ListNode reverse = listNode;
        ListNode temp  = listNode.next;
        reverse.next = null;
        while (temp != null){
            ListNode node = temp.next;
            temp.next = reverse;
            reverse = temp;
            temp = node;
        }
        return reverse;
    }
}
