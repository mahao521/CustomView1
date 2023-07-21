package com.mahao.customview.stackmanager

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.mahao.customview.R

class AActivity : BaseActivity() {

    private val TAG = "AActivity"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a)
        ActivityStackManager.getInstance().print(TAG+"create")
        findViewById<Button>(R.id.btn_A).setOnClickListener {
            startActivity(Intent(this,BActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        ActivityStackManager.getInstance().print(TAG+"onResume")
    }


    override fun onPause() {
        super.onPause()
    }
}

