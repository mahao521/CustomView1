package com.example.lib;


import java.math.BigInteger;
import java.util.Arrays;

public class Solution1 {

    public static  int reverse(int x) {
        String aa = String.valueOf(x);
        int[] arr = new int[aa.length()];
        boolean isLess = false;
        for (int i = 0; i < aa.length(); i++){
            char c = aa.toCharArray()[i];
            if(c == '-'){
                System.out.println("相等");
                isLess = true;
            }else {
                System.out.println("不相等");
                arr[aa.length() -i-1] = Integer.parseInt(aa.toCharArray()[i]+"");
            }
            System.out.println(c +"");
        }
        String x1 = Arrays.toString(arr);
        System.out.println(x1);
        System.out.println(x1.substring(1,x1.length()-1).replace(",","").replace(" ",""));
        long val = Long.parseLong(x1.substring(1, x1.length() - 1).replace(",", "").replace(" ", ""));
        return BigInteger.valueOf(val).intValue();
    }

    public static void main(String[] args) {
        int reverse = reverse(1534236469);
        System.out.println("---before --"+ reverse);
        if(reverse > 0){
            String s = String.valueOf(reverse);
            while (s.endsWith("0")){
                s = s.substring(0,s.length()-1);
            }
            reverse = Integer.parseInt(s);
        }
        System.out.println("--after---"+ reverse);



    }
}
