package com.mahao.customview.test;

/**
 * 如果一个类B继承一个类A,类A实现了接口。 类B也实现了这个接口会怎么样。
 */
public class TestExtend {

    interface ColorTop {
        int getColor();
    }

    abstract class AA implements ColorTop {

    }

    interface ColorMiddle extends ColorTop {
        int getFood();
    }

    abstract class BB extends AA implements ColorMiddle {
        @Override
        public int getFood() {
            return 1;
        }
    }

}
