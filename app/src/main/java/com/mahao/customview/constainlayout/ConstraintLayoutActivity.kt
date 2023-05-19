package com.mahao.customview.constainlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mahao.customview.R

/**
 *  1 : chain :  只需要再一个约束的child设置就可以了。
 *     chain_spead ：  元素均匀分布，默认模式
 *     chain_spead_inside : 2个child依附在两端。
 *     packed :  三个child居中。
 *  2 : 居中：
 *    1个child居中，通过left_to_left ; right_to_right
 *    1个child居中 通过guideline
 *    2个child居中，通过chain_style : packed
 *  3 ： layout_constraintHorizontal_weight  需要child的mactch_parent 为 0
 *  4 : barrier就是把2个或者多个控件封装在一起和其他控件约束
 */
class ConstraintLayoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_layout)
    }
}