package com.example.lib;

import java.net.StandardSocketOptions;
import java.util.Arrays;

//找出最长无重复子串。
public class Solution5 {

    private static final String TAG = "AA";

    public static int lengthOfLongestSubstring(String s) {

        /*har[] chars = s.toCharArray();
        char[] reverseChars = new char[chars.length];
        int index = 0;
        for (int i = chars.length-1; i >= 0 ; i--) {
             reverseChars[index] = chars[i];
             index++;
        }*/
        int i = dealWith(s);
        System.out.println("end =====================   ");
        return i;
    }

    public static int dealWith(String s) {
        if (s.equals("")) {
            return 0;
        }

        if (s.length() == 1) {
            return 1;
        }

        char[] chars = s.toCharArray();
        int maxLength1 = 1;
        int[] arr = new int[chars.length];

        for (int i = 0; i < chars.length; i++) {

            int range = 1;
            maxLength1 = 1;
            for (int j = i + 1; j < chars.length; j++) {

                if (chars[i] == chars[j]) {
                    //    System.out.println("lengthOfLongestSubstring: " + (j - i) + "  " + maxLength1);
                    maxLength1 = Math.max(j - i, maxLength1);
                    range = 1;
                    break;
                } else {
                    range++;
                    maxLength1 = Math.max(range, maxLength1);
                    //       System.out.println("range ===== " + range + "   " + chars[j]);
                    //    System.out.println("lengthOfLongestSubstring:  -----    " + maxLength1);
                }
            }
            arr[i] = maxLength1;
            System.out.println(Arrays.toString(arr));
        }

        for (int i = 0; i < arr.length; i++) {

            System.out.println(arr[i]);
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    arr[i] = 1;
                }
            }
        }
        System.out.println("------------");
        System.out.println(Arrays.toString(arr));

        maxLength1 = 1;
        for (int i = 0; i < arr.length; i++) {
            maxLength1 = Math.max(maxLength1, arr[i]);
        }
        return maxLength1;
    }


    public static void main(String[] args) {
        int[] aa = new int[19];
        // "abcabcbb"
        int finalMax = lengthOfLongestSubstring("abba");

        System.out.println("finlmax = " + finalMax);
    }
}
