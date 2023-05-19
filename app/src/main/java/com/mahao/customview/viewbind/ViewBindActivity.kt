package com.mahao.customview.viewbind

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.mahao.customview.R
import java.lang.reflect.ParameterizedType

//abstract class ViewBindActivity<T : ViewBinding> : AppCompatActivity() {
abstract class ViewBindActivity : AppCompatActivity() {
    /*lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var classT = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>
        var declaredMethod = classT.getDeclaredMethod("inflate", LayoutInflater::class.java)
        if (!declaredMethod.isAccessible) {
            declaredMethod.isAccessible = true
        }
        binding = declaredMethod.invoke(null, layoutInflater) as T
     //   var binding1 = getViewBind()
        var root = binding?.root
        setContentView(root)*/
}

//  abstract fun getViewBind(): T

//}