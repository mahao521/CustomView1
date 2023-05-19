package com.mahao.customview.fragment

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.TextView
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.savedstate.SavedStateRegistry
import com.mahao.customview.R
import com.mahao.customview.fragment.viewmodel.TestViewModel1
import kotlin.math.log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OneFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OneFragment : Fragment() {

    private val TAG = "OneFragment"
    private val TAG_ANIM = "OneFragment_Animator"
    private var tvContent:TextView? = null

    val viewmodel: TestViewModel1 by activityViewModels()

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        Log.d(TAG, "onCreate:   --->   " + param1)
        savedStateRegistry.registerSavedStateProvider(TAG, object : SavedStateRegistry.SavedStateProvider {
            override fun saveState(): Bundle {
                var bundle = Bundle()
                bundle.putString("friend", "zhangsan");
                return bundle
            }
        })
        Log.d(TAG, "onCreate: " + isAdded)
        if (this.isAdded) {
            parentFragmentManager
        }
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {

            }
        })
        Log.d(TAG, "onCreate: manager parent = " + parentFragmentManager + "  child = "
                + childFragmentManager)
   //    setFragmentResultListener()
            //postponeEnterTransition()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: ")
        return inflater.inflate(R.layout.fragment_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvContent = view.findViewById(R.id.tv_fragment_one)
        var arguments1 = arguments?.getString(ARG_PARAM1)
        if(!TextUtils.isEmpty(arguments1)){
            tvContent?.setText(arguments1)
        }else{
            tvContent?.setText("One")
        }

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d(TAG, "onViewStateRestored: ")
        var consumeRestoredStateForKey = savedStateRegistry.consumeRestoredStateForKey(TAG)
        var get = consumeRestoredStateForKey?.get("friend")
        Log.d(TAG, "onCreate: ---------------  " + get)
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState: ")
    }

    //postponeEnterTransition
    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator? {
        Log.d(TAG_ANIM, "onCreateAnimator: " + enter+" " + (context as Activity).window.decorView.measuredWidth.toFloat())
        var ofFloat : ObjectAnimator? = null
        if(!enter){
            ofFloat = ObjectAnimator.ofFloat(view, "translationX", 0f, -view?.measuredWidth?.toFloat()!!)
        }else{
            ofFloat = ObjectAnimator.ofFloat(view, "translationX", (context as Activity).window.decorView.measuredWidth.toFloat(),0f)
        }
        ofFloat.repeatCount = 0
        ofFloat.duration = 2000
        //return super.onCreateAnimator(transit, enter, nextAnim)
        return ofFloat
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        Log.d(TAG_ANIM, "onCreateAnimation: " + enter)
        var animation = ScaleAnimation(0.5f,1.0f,0.5f,1.0f,0.5f,0.5f)
        animation.repeatCount = 0
        animation.duration = 2000
        return super.onCreateAnimation(transit, enter, nextAnim)
    }

    override fun setReturnTransition(transition: Any?) {
        Log.d(TAG_ANIM, "setReturnTransition: ")
        super.setReturnTransition(transition)
    }

    override fun setExitTransition(transition: Any?) {
        Log.d(TAG_ANIM, "setExitTransition: ")
        super.setExitTransition(transition)
    }

    override fun setEnterTransition(transition: Any?) {
        Log.d(TAG_ANIM, "setEnterTransition: ")
        super.setEnterTransition(transition)
    }

    override fun setEnterSharedElementCallback(callback: SharedElementCallback?) {
        Log.d(TAG_ANIM, "setEnterSharedElementCallback: ")
        super.setEnterSharedElementCallback(callback)
    }

    override fun setExitSharedElementCallback(callback: SharedElementCallback?) {
        Log.d(TAG_ANIM, "setExitSharedElementCallback: ")
        super.setExitSharedElementCallback(callback)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OneFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                OneFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

}