package com.mahao.customview.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.mahao.customview.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MyDialogFragment : DialogFragment {

    private val TAG = "MyDialogFragment"

    private var param1: String? = null
    private var param2: String? = null

    constructor(){

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setShowsDialog(false)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

   /* override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var dialog = AlertDialog.Builder(context)
                .setTitle("系统弹窗")
                .setMessage("弹窗优先级比下面的OnCreateView的优先级更高")
                .setIcon(R.drawable.ic_launcher_background)
                .setNegativeButton("取消",object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        TODO("Not yet implemented")
                    }
                }).setPositiveButton("确定",object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {

                    }
                }).create()
        return dialog
    }*/

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun onStart() {
        super.onStart()
        if(dialog != null && dialog?.window != null){
            dialog?.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog?.window?.setBackgroundDrawableResource(R.drawable.green)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView: " + container)
        return inflater.inflate(R.layout.fragment_my_dialog, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                MyDialogFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}