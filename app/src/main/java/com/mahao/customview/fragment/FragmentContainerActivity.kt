package com.mahao.customview.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.*
import com.mahao.customview.MainActivity
import com.mahao.customview.R
import com.mahao.customview.fragment.viewmodel.TestViewModel
import com.mahao.customview.fragment.viewmodel.TestViewModel1
import com.mahao.customview.kotlin.TestByLazy


class FragmentContainerActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "FragmentContainer"
    var fragmentContainer: FragmentContainerView? = null
    var fragmentOne: OneFragment? = null
    var fragmentTwo: TwoFragment? = null
    var fragmentThree: ThreeFragment? = null
    var liveData = MutableLiveData<Boolean>()
    var registerForActivityResult: ActivityResultLauncher<String>? = null
    var registerForActivityResult1: ActivityResultLauncher<Void>? = null
    var viewModel1: TestViewModel1? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)
        fragmentContainer = findViewById<FragmentContainerView>(R.id.fragment_container)
        var beginTransaction = supportFragmentManager.beginTransaction()
        fragmentOne = OneFragment.newInstance("", "")
        fragmentTwo = TwoFragment.newInstance("", "")
        fragmentThree = ThreeFragment.newInstance("", "")
        beginTransaction.add(R.id.fragment_container, fragmentOne!!, "fragment_one")
        beginTransaction.add(R.id.fragment_container, fragmentTwo!!, "fragment_two")
        beginTransaction.add(R.id.fragment_container, fragmentThree!!, "fragment_three")
        beginTransaction
                .setMaxLifecycle(fragmentOne!!, Lifecycle.State.STARTED)
                .setMaxLifecycle(fragmentTwo!!, Lifecycle.State.INITIALIZED)
                .setMaxLifecycle(fragmentThree!!, Lifecycle.State.INITIALIZED)
        beginTransaction.show(fragmentOne!!).hide(fragmentTwo!!).hide(fragmentThree!!)
                .commitAllowingStateLoss()
        fragmentContainer?.postDelayed({
            var fragmentOneByTag = supportFragmentManager.findFragmentByTag("fragment_one")
            Log.d(TAG, "onCreate: " + fragmentOneByTag?.isAdded + " " + fragmentOneByTag?.isHidden + " " + fragmentOneByTag.toString())
            Log.d(TAG, "onCreate: " + fragmentOneByTag?.id + "   " + (fragmentContainer?.parent as? View)?.id)
            Log.d(TAG, "onCreate: " + findViewById<RelativeLayout>(R.id.rl_root).id + "  " + R.id.fragment_container)
        }, 0)
        var btnSwitch = findViewById<Button>(R.id.btn_container_view)
        var btnLazyLoad1 = findViewById<Button>(R.id.btn_lazy_load_1)
        var btnLazyLoad2 = findViewById<Button>(R.id.btn_lazy_load_2)
        var btnLazyLoad3 = findViewById<Button>(R.id.btn_lazy_load_3)
        btnSwitch.setOnClickListener(this)
        btnLazyLoad1.setOnClickListener(this)
        btnLazyLoad2.setOnClickListener(this)
        btnLazyLoad3.setOnClickListener(this)
        LifecycleRegistry(this).addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                Log.d(TAG, "onStateChanged: " + source + " " + event.name)
            }
        })
        LifecycleRegistry(this).addObserver(object : DefaultLifecycleObserver {

            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
            }

            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                Log.d(TAG, "onStateChanged   onCreate: " + owner)
            }
        })
       // lifecycle.addObserver()
        liveData.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {
                Log.d(TAG, "onChanged: 接收到数据啦  -》 " + t)
            }
        })
        liveData.observeForever {
            Log.d(TAG, "onCreate: forever 接收到数据 " + it)
        }
        //  var viewModel = ViewModelProvider(this).get(TestViewModel::class.java)
        //  fragmentOne.parentFragmentManager.getFragmentStore()
        var viewModel = ViewModelProvider(this).get(TestViewModel::class.java)
        Log.d(TAG, "onCreate: " + viewModel.toString())

        registerForActivityResult = fragmentOne!!.registerForActivityResult(object : ActivityResultContract<String, String>() {
            override fun createIntent(context: Context, input: String?): Intent {
                var intent = Intent(context, MainActivity::class.java)
                intent.putExtra("name", input)
                return intent
            }

            override fun getSynchronousResult(context: Context, input: String?): SynchronousResult<String>? {
                if (input.equals("age")) {
                    return SynchronousResult("27")
                }
                return super.getSynchronousResult(context, input)
            }

            override fun parseResult(resultCode: Int, intent: Intent?): String {
                return "zhangsan"
            }
        }
                /*, object : ActivityResultRegistry() {

                    override fun <I : Any?, O : Any?> onLaunch(requestCode: Int, contract: ActivityResultContract<I, O>, input: I, options: ActivityOptionsCompat?) {
                        Log.d(TAG, "onLaunch: " + requestCode + "   "  + "  " + input)
                       // ActivityCompat.startActivityForResult(this@FragmentContainerActivity, intent, requestCode, null)
                         dispatchResult(requestCode, expectedResult)
                    }

                }*/, activityResultRegistry, object : ActivityResultCallback<String> {

            override fun onActivityResult(result: String?) {
                Log.d(TAG, "onActivityResult:  1   " + result)
            }
        })


        /*  registerForActivityResult1 = fragmentTwo!!.registerForActivityResult(ActivityResultContracts.PickContact(), object : ActivityResultCallback<Uri> {
              override fun onActivityResult(result: Uri?) {
                  Log.d(TAG, "onActivityResult: 2  " + result)
              }
          })

          fragmentThree!!.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
              Log.d(TAG, "onCreate: ")
          }*/
        viewModel1 = ViewModelProvider(this).get(TestViewModel1::class.java)
        viewModel1?.testResore()
        viewModel1?.testSave()

        val mStartForResult = registerForActivityResult(StartActivityForResult(),
                ActivityResultCallback<ActivityResult> { result ->
                    if (result.getResultCode() === RESULT_OK) {
                        val intent: Intent = result.data!!
                    }
                })
        //    mStartForResult.launch(Intent(this, MainActivity::class.java))
        var result = activityResultRegistry.register("key", this, object : ActivityResultContract<String, String>() {
            override fun createIntent(context: Context, input: String?): Intent {
                TODO("Not yet implemented")
            }

            override fun parseResult(resultCode: Int, intent: Intent?): String {
                TODO("Not yet implemented")
            }

        }, object : ActivityResultCallback<String> {
            override fun onActivityResult(result: String?) {

            }
        })
        Log.d(TAG, "onCreate: " + TestByLazy().data)
        Log.d(TAG, "onCreate: manager " + supportFragmentManager)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState: ")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(TAG, "onRestoreInstanceState: ")
        viewModel1?.testResore()
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.btn_lazy_load_1 -> {
                liveData.postValue(false)
                liveData.postValue(true)
                registerForActivityResult?.launch("aaa1")

                this@FragmentContainerActivity.supportFragmentManager.beginTransaction()
                        .setMaxLifecycle(fragmentOne!!, Lifecycle.State.RESUMED)
                        .setMaxLifecycle(fragmentTwo!!, Lifecycle.State.STARTED)
                        .show(fragmentOne!!).hide(fragmentTwo!!).hide(fragmentThree!!)
                        .commitAllowingStateLoss()
            }
            R.id.btn_lazy_load_2 -> {
                liveData.postValue(true)
                liveData.value = false
                registerForActivityResult1?.launch(null)
                var beginTransaction = this@FragmentContainerActivity.supportFragmentManager.beginTransaction()
                Log.d(TAG, "onClick: " + hasResume(fragmentTwo!!))
                if (!hasResume(fragmentTwo!!)) {
                    beginTransaction.setMaxLifecycle(fragmentTwo!!, Lifecycle.State.RESUMED)
                }
                beginTransaction
                        .hide(fragmentThree!!).hide(fragmentOne!!).show(fragmentTwo!!)
                        .commitAllowingStateLoss()
            }
            R.id.btn_lazy_load_3 -> {
                this@FragmentContainerActivity.supportFragmentManager.beginTransaction()
                        .setMaxLifecycle(fragmentTwo!!, Lifecycle.State.CREATED)
                        .setMaxLifecycle(fragmentThree!!, Lifecycle.State.RESUMED)
                        .show(fragmentThree!!).hide(fragmentOne!!).hide(fragmentTwo!!)
                        .commitAllowingStateLoss()
            }
            R.id.btn_container_view -> {  // 通过view获取view上面的fragment
                //var viewExtendFragment = fragmentContainer?.findFragment<Fragment>()
                var containerFragment = fragmentContainer?.getFragment<Fragment>()
                Log.d(TAG, "onClick:  View扩展Fragment = " + "  container Id 的fragment = " + containerFragment)
                //  viewModel1?.testResore()
            //    activityResultRegistry.dispatchResult()
            }
        }
    }

    fun hasResume(fragment: Fragment): Boolean {
        /*    var atLeast = fragmentOne!!.viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
            if (!atLeast) {
                (fragmentOne!!.lifecycle as LifecycleRegistry).currentState = Lifecycle.State.RESUMED
            }*/
        var atLeast = fragment.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
        return atLeast
    }

    override fun onDestroy() {
        super.onDestroy()
        //如果没有传入lifecycleOwner，需要手动取消注册。
        registerForActivityResult?.unregister()
        registerForActivityResult1?.unregister()
    }

}