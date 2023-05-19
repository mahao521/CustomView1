package com.mahao.customview.viewbind

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mahao.customview.R

//class ViewBindImplActivity : ViewBindActivity<ActivityViewBindImplBinding>() {
    class ViewBindImplActivity : ViewBindActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
   //     binding.btnBinding.setText("我是基类控制的")
    }

 /*   override fun getViewBind(): ActivityViewBindImplBinding {
        TODO("Not yet implemented")
    }*/
}