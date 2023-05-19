package com.mahao.customview.kotlin

class TestKotlinDsl {

    var finalValue: Int = 0

    fun myDsl(aa: AA.(Int, Int) -> String): Int {
        var innerClass = AA()
        var sum = innerClass.sum(10, 20)
        //aa.invoke(innerClass,10,20)
        //innerClass.aa(10,20)
        var aa1 = aa(innerClass, sum, 30).toInt()
        return aa1
    }

    fun myFun(aa: (AA, Int, Int) -> String): Int {
        var innerClass = AA()
        var sum = innerClass.sum(10, 20)
        var aa1 = aa(innerClass, sum, 30).toInt()
        return aa1
    }

    fun myDsl1(aa: TestKotlinDsl.() -> Int): Int {
        var aa1 = aa(this)
        return aa1 + 100
    }

    fun myDsl2(bb: AA.() -> Int): TestKotlinDsl {
        var aa2 = AA()
        var bb1 = aa2.bb()
        var sum = aa2.sum(bb1, 10)
        var TestKotlinDsl = TestKotlinDsl()
        TestKotlinDsl.finalValue = sum
        return TestKotlinDsl
    }

    inner class AA {
       inline fun sum(a: Int, b: Int): Int {
            return a + b
        }
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {

            var TestKotlinDsl = TestKotlinDsl()
            TestKotlinDsl()

            val result = TestKotlinDsl.myDsl { a, b ->
                var sum = sum(a, b)
                sum = sum + 10
                sum.toString()
            }
            println(result)

            val result1: Int = TestKotlinDsl {
                myDsl { i, i2 ->
                    (i + sum(i, i2)).toString()
                }
            }
            println(result1)


            val result2 = TestKotlinDsl {
                myDsl1 {
                    AA().sum(10, 20)
                }
            }
            println(result2)

            val result3 : String = TestKotlinDsl(33) { mm ->
                var aa = finalValue + 10
                myDsl2 {
                    sum(10, mm + aa)
                }
            }
            println(result3)

            val result4 = TestKotlinDsl {
                myFun { innerClass, a, b ->
                    innerClass.sum(a, b).toString()
                }
            }
            println(result4)
            kotlin.run {  }
            with(1){
                TestKotlinDsl.AA()
            }.sum(1,2)
        }
    }

    operator fun invoke() {
        print("my name is zhangsan\r\n")
    }

    operator fun invoke(block: TestKotlinDsl.() -> Int): Int {
        print("my name is lisi \r\n")
        return block()
    }

    operator fun invoke(a: Int, aa: TestKotlinDsl.(Int) -> TestKotlinDsl): String {
        println("my name is wangwu")
        var cc = aa.invoke(this, a)
        //this.aa(a)
        //aa(this,a)
        return cc.finalValue.toString()
    }
}