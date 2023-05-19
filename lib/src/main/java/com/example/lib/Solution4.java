package com.example.lib;

public class Solution4 {

    static int count = 0;
    public static int clamStairs(int n) {

        if (n == 0) {
            return 1;
        }
        int temp = clamStairs(n - 1);
        count = count + temp;
        return count;

        // 0 --> 1
        // 1 ---> 1 + 1 = 2
        //2 --> 2 + 2 = 4
        //3  -->  4+ 4 = 8
    }


    public static int count1 = 0;
    public static int clamStairs2(int n) {

        System.out.println(count);
        if(n >= 1){
            clamStairs(n-1);
            count1++;
        }
        if(n >= 2){
            clamStairs(n-2);
            count1++;
        }
        if (n == 0){
            return 1;
        }

        return count;
        //1 ===  1
        //2 === 1+


    }

    public static void main(String[] args) {
        int i = clamStairs2(4);
        System.out.println(i);
    }
}
