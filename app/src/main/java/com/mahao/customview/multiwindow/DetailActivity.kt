package com.mahao.customview.multiwindow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.mahao.customview.R
import kotlin.random.Random

class DetailActivity : AppCompatActivity() {

    var position = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        var textView = findViewById<TextView>(R.id.tv_detail)
        textView.setOnClickListener {
            textView.setText("detail + " + position++)
            startActivity(Intent(this, ItemActivity::class.java))
        }

        findViewById<TextView>(R.id.tv_finish).setOnClickListener {
            finish()
        }
    }
}