package com.mahao.customview.fragment

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Lifecycle
import com.mahao.customview.R

class MainFragmentActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "MainFragmentActivity"
    var clickCount = 0
    var fragmentOne: Fragment? = null
    var fragmentTwo: Fragment? = null
    var fragmentThree: Fragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        setContentView(R.layout.activity_main_fragment)
        var btnSwitch = findViewById<Button>(R.id.btn_switch)
        var btnContainer = findViewById<Button>(R.id.btn_container)
        var btnSave = findViewById<Button>(R.id.btn_save_data)
        var fragmentContainer = findViewById<FragmentContainerView>(R.id.fragment_container_stack)
        //   var bgContainerFragment = findViewById<TextView>(R.id.bg_fragment_container)
        btnSwitch.setOnClickListener(this)
        btnContainer.setOnClickListener(this)
        btnSave.setOnClickListener(this)
        testBackRecord()
        var textView = TextView(this)
        textView.setText("我是背景")
        textView.setTag(R.id.fragment_container_view_tag, fragmentTwo)
        fragmentContainer.addView(textView)
        fragmentContainer.post {
            //     Log.d(TAG, "onCreate: " + fragmentContainer.getFragment<Fragment>())
        }


        var intent = Intent()
        //startActivityForResult()
        testSave()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d(TAG, "onAttachedToWindow: ")
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.d(TAG, "onDetachedFromWindow: ")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        testRestore()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_switch -> {
                clickCount++
/*                fragmentOne = supportFragmentManager.findFragmentById(R.id.fragment_one)
                fragmentTwo = supportFragmentManager.findFragmentByTag("fragment_two")
                fragmentThree = supportFragmentManager.findFragmentByTag("fragment_three")
                supportFragmentManager.beginTransaction().hide(fragmentOne!!).hide(fragmentTwo!!).show(fragmentThree!!)
                        .commitAllowingStateLoss()*/
                Log.d(TAG, "onClick: onCreateAnimator  " + clickCount)
                //只需要hide上一次那个fragment
                when (clickCount % 3) {
                    0 -> {
                        supportFragmentManager.beginTransaction().hide(fragmentTwo!!)
                            .show(fragmentThree!!).commitAllowingStateLoss()
                    }
                    1 -> {
                        supportFragmentManager.beginTransaction().show(fragmentOne!!).hide(fragmentThree!!)
                            .commitAllowingStateLoss()
                    }
                    2 -> {
                        supportFragmentManager.beginTransaction().hide(fragmentOne!!)
                            .show(fragmentTwo!!).commitAllowingStateLoss()
                    }
                }
            }
            R.id.btn_container -> {
                var intent = Intent(this, FragmentContainerActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_save_data -> {
                if (clickCount % 2 == 0) {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                } else {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }
                var dialog = MyDialogFragment()
                dialog.show(supportFragmentManager, "myDialog")
                //  Settings.System.putInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION, requestedOrientation)
            }
        }
    }

    fun initFragment() {
        fragmentOne = OneFragment.newInstance("", "")
        fragmentTwo = TwoFragment.newInstance("", "")
        fragmentThree = ThreeFragment.newInstance("", "")
    }

    fun testBackRecord() {
        initFragment()
        supportFragmentManager.beginTransaction().add(R.id.fragment_container_stack, fragmentOne!!)
           // .addToBackStack("")
            .commit()
        supportFragmentManager.beginTransaction().add(R.id.fragment_container_stack, fragmentTwo!!)
           // .addToBackStack("")
            .commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container_stack, fragmentThree!!)
         //   .addToBackStack("")
            .commitAllowingStateLoss()
        supportFragmentManager.beginTransaction().show(fragmentThree!!).hide(fragmentTwo!!)
            .hide(fragmentOne!!)
            .commitAllowingStateLoss()
    }

    fun testSave() {
        savedStateRegistry.registerSavedStateProvider(TAG) {
            var bundle = Bundle()
            bundle.putString("data", "数据")
            Log.d(TAG, " Save: 保存  " + lifecycle.currentState.name)
            bundle
        }
        var aa = TextView(this)
    }

    fun testRestore() {
        var consumeRestoredStateForKey = savedStateRegistry.consumeRestoredStateForKey(TAG)
        Log.d(
            TAG,
            "restore: 恢复 " + consumeRestoredStateForKey?.get("data") + " " + lifecycle.currentState.name
        )
    }
}