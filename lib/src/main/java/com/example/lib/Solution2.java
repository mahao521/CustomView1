package com.example.lib;

public class Solution2 {

    //58. 最后一个单词的长度
    public static int lengthofLastWord(String s) {

        char[] chars = s.toCharArray();
        int start = -1;
        int end = -1;

        for (int i = chars.length - 1; i >= 0; i--) {
            if (chars[i] != ' ' && start == -1) {
                start = i;
            }
            if (start != -1 && chars[i] == ' ') {
                end = i;
                break;
            }
        }
        System.out.println("start " + start +"   end  " + end);
        if (end == -1 && start != -1) {
            return start+1;
        }
        return (start - end);
    }


    public static void main(String[] args) {
        System.out.println(" " + lengthofLastWord("luffy is still joyboy"));
    }
}
