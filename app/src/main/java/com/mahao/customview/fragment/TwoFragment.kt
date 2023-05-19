package com.mahao.customview.fragment

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mahao.customview.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TwoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TwoFragment : Fragment() {
    private  val TAG = "TwoFragment"
    private val TAG_ANIM = "Two_Fragment_Anim"
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        Log.d(TAG, "onCreate: ")
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
        return inflater.inflate(R.layout.fragment_two, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TwoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                TwoFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator? {
        Log.d(TAG_ANIM, "onCreateAnimator: " + enter)
        Log.d(TAG, "onCreateAnimator: " + lifecycle.currentState)
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
}