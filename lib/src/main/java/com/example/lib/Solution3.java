package com.example.lib;

public class Solution3 {

    public static int[][] merge(int[][] intervals) {

        for (int i = 0; i < intervals.length - 1; i++) { // 1和2   1和3  1和4   2和3   2和4
            /*int current = intervals[i][intervals[i].length - 1];
            int next = intervals[i + 1][0];
*/
        for (int j = i + 1; j < intervals.length; j++) {

            int startone = intervals[i][0];
            int startTwo = intervals[j][0];
            int endOne = intervals[i][1];
            int endTwo = intervals[j][1];
            if (endOne >= startTwo && endTwo >= startone) {
                int[][] remove = remove(i,j, intervals, Math.min(startone, startTwo), Math.max(endOne, endTwo));
                printArray(remove);
                System.out.println("=================");
                intervals = merge(remove);
            }
        }
    }
        return intervals;
}


    public static int[][] remove(int index,int removeIndex, int[][] intervals, int start, int end) {
        int[][] backArray = new int[intervals.length - 1][2];
        int currentIndex = 0;
        for (int z = 0; z < intervals.length; z++) {
            if (index == z) {
                backArray[currentIndex][0] = start;
                backArray[currentIndex][1] = end;
                currentIndex++;
                continue;
            }
            if (removeIndex == z) {
                continue;
            }

            backArray[currentIndex][0] = intervals[z][0];
            backArray[currentIndex][1] = intervals[z][1];
            currentIndex++;
        }
        //     printArray(backArray);
        return backArray;
    }

    public static void printArray(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.println(array[i][j]);
            }
        }
    }

    public static void main(String[] args) {

        int[][] ints = new int[][]{
                {1, 3},
                {2, 6},
                {8, 10},
                {9, 12},
                {15, 18}
        };
        int[][] int1 = new int[][]{
                {1, 4},
                {0, 4},
        };
        int[][] int2 = new int[][]{
                {1, 4},
                {0, 1},
        };
        int[][] int3 = new int[][]{
                {2, 3},
                {5, 5},
                {2, 2},
                {3, 4},
                {3, 4}
        };
        printArray(merge(int3));

    }
}
