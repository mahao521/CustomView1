package com.mahao.customview

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.shapes.RoundRectShape
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import com.mahao.customview.widget.CustomDrawable
import com.mahao.customview.widget.CustomLayout
import com.mahao.customview.widget.RoundRectDrawableWithShadow

/**
 *  1 验证scollerView 内部child大小，大于屏幕，而scrollview大小match_parent
 *
 *  2 验证 子view的大小 最终由父的模式和子的layoutParams 共同决定的，
 *    测量过程 从 父 到 子 再 从 子 到 父，确定view大小。
 *
 * 3、默认view/viewgroup 的 match_parent 和 wrap_content大小都是parentSize ,因此自定义控件，
 *    通常会处理这个。达到不一样的显示效果。
 *
 * 4、子layourparment = match_parent  父 wrap_content 最终子的测量模式为wrap_content 大小为父大小。
 *   参照viewgroup : getChildMeasureSpec
 *
 * 5、viewgroup 默认是不测量的。需要手动测量view 和 layoutView
 */
class CustomViewActivity : AppCompatActivity() {

    val TAG = "CustomViewActivity"
    val radius = 5f;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view)
        var horizontalScrollView = findViewById<HorizontalScrollView>(R.id.hsv)
        var linearLayout = findViewById<LinearLayout>(R.id.ll_children)
        var customLayout = findViewById<CustomLayout>(R.id.custom_layout)
        customLayout.setRadius(radius)
        var customLayout2 = findViewById<CustomLayout>(R.id.custon_layout_2)
        var cardView = findViewById<CardView>(R.id.cardView)
        cardView.setCardBackgroundColor(Color.RED)
        var backDrawable = RoundRectDrawableWithShadow(resources, null, radius, 10f, 20f)
        backDrawable.setAddPaddingForCorners(false)
        customLayout2.setBackground(backDrawable)
        var customDrawable = CustomDrawable()
        customDrawable.init()
        customLayout.setBackground(customDrawable)
        linearLayout.setOnClickListener {
            var width = horizontalScrollView.width   // 1080
            var width1 = linearLayout.width  //2200
            Log.d(TAG, "onCreate: " + width + " " + width1)
        }
    }
}