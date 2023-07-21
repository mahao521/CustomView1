package com.mahao.customview.binder

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import com.mahao.customview.IMyAidlInterface
import com.mahao.customview.R
import com.mahao.customview.binder.interfaces.ISetNameInterface
import com.mahao.customview.binder.interfaces.PersonService1

class BinderActivity : AppCompatActivity() {

    private val TAG = "BinderActivity"
    var myAidlInterface: IMyAidlInterface? = null
    var mIntent: Intent? = null
    var myConn: MyConn? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_binder)
        findViewById<Button>(R.id.btn_bind).setOnClickListener {
            var intent = Intent(this, PersonService::class.java)
            myConn = MyConn()
            bindService(intent, myConn!!, BIND_AUTO_CREATE)
        }
        findViewById<Button>(R.id.btn_bind_1).setOnClickListener {
            mIntent = Intent(this, PersonService1::class.java)
            bindService(intent, conn1, BIND_AUTO_CREATE)
        }
    }

    inner class MyConn : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "onServiceConnected: ")
            myAidlInterface = IMyAidlInterface.Stub.asInterface(service)
            myAidlInterface?.name = "zhangsan"
            Log.d(TAG, "onServiceConnected: " + myAidlInterface?.name)
            service?.linkToDeath(object : IBinder.DeathRecipient {
                override fun binderDied() {
                    myAidlInterface?.let {
                        it.asBinder().unlinkToDeath(this, 0)
                    }
                    myAidlInterface = null
                    //死亡之后重新绑定
                    bindService(mIntent, myConn!!, BIND_AUTO_CREATE)
                }

            }, 0)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected: ")
        }

    }

    var conn1 = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "onServiceConnected--1: ")
            //   Log.d(TAG, "onServiceConnected: " +  (service is PersonService1.MySetNameBinder))
            if (service is ISetNameInterface) {
                var iSetNameInterface = service as ISetNameInterface
                iSetNameInterface?.setName("lisi")
                Log.d(TAG, "onServiceConnected--1: " + iSetNameInterface?.getName())
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected--1: ")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if(myConn != null){
            unbindService(myConn!!)
        }
       if(conn1 != null){
           unbindService(conn1)
       }

    }
}