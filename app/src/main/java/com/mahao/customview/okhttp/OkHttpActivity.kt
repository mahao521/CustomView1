package com.mahao.customview.okhttp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.mahao.customview.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import retrofit2.http.Url
import java.io.File
import java.io.IOException
import java.lang.reflect.Method
import java.net.URL
import java.util.concurrent.TimeUnit

class OkHttpActivity : AppCompatActivity() {

    private val TAG = "OkHttpActivity"

    companion object {
        const val APP_KEY = "tbyhzRE5IwMXoN1GuT6mTAjafvpshiuf"
        const val APP_CODE = "68cae9fe5b0449efb276f3c7e05ac872"
        const val URL = "https://ali-weather.showapi.com/area-to-weather-date"
    }

    private var tvContent: TextView? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ok_http)
        tvContent = findViewById(R.id.tv_show_content)
        tvContent?.setOnClickListener {
            var intent = Intent(this, RetrofitActivity::class.java)
            startActivity(intent)
        }

        var file = File(cacheDir, "mahao.txt")
        Log.d(TAG, "onCreate: " + file.exists())
        Log.d(TAG, "onCreate: " + file.absolutePath)

        Observable.create<String> {
            //   var okhttp = OkHttpClient()
            var okhttp = OkHttpClient.Builder().cache(Cache(file, 10 * 1024 * 1024))
                .addInterceptor(object : Interceptor {
                    override fun intercept(chain: Interceptor.Chain): Response {
                        var request = chain.request()
                        Log.d(TAG, "intercept: " + Thread.currentThread().name)
                        Log.d(TAG, "intercept: " + request.toString())
                        request.newBuilder().addHeader("mahao", "24")
                        Log.d(
                            TAG,
                            "intercept-1: 还没有配置requestBuilder, 现在还可以添加header，等配置信息哈"
                        )
                        Log.d(
                            TAG,
                            "intercept-2: proceed返回的数据也可能是本地/文件数据哈。这里也可以配置取缓存哈。"
                        )
                        return chain.proceed(request)
                    }
                }).addNetworkInterceptor(object : Interceptor {
                    override fun intercept(chain: Interceptor.Chain): Response {
                        var request = chain.request()
                        Log.d(TAG, "intercept-3:  网络已经连接啦，马上开始去读取服务器数据啦。")
                        var proceed = chain.proceed(request)
                        Log.d(TAG, "intercept: -5 " + proceed.headers.toString())
                        Log.d(TAG, "intercept: -6 " + proceed.isSuccessful)
                        Log.d(TAG, "intercept: -7 " + proceed.toString())
                        return proceed;
                    }
                }).build()
            var mutableMap = mutableMapOf<String, String>()
            mutableMap.put("area", "丽江");
            mutableMap.put("areaCode", "530700");
            mutableMap.put("date", "20230509");
            mutableMap.put("need3HourForcast", "1");
            var url: HttpUrl? = URL.toHttpUrlOrNull()
            url?.newBuilder()?.addQueryParameter("area", "丽江")
            var request = Request.Builder()
                .get()
                .header("Authorization", "APPCODE " + APP_CODE)
                .url(mergeUrl(URL, mutableMap))
                .cacheControl(
                    CacheControl
                        .Builder()
                        .maxStale(5, TimeUnit.DAYS)
                        .maxAge(5, TimeUnit.DAYS)
                        .build()
                )
                .build()
            var response = okhttp.newCall(request).execute();
            var string = response.body?.string()
            Log.d(TAG, "onCreate: " + string)
            it.onNext(string);
            it.onComplete()
            /*   var formBody = FormBody.Builder().add("mahao", "23").add("lisi", "24").build()
               request.newBuilder().post(formBody)*/
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                tvContent?.setText(it)
            }
        /* Observable.create<String> {
             var okhttp = OkHttpClient()
             var mutableMap = mutableMapOf<String, String>()
             mutableMap.put("area", "丽江");
             mutableMap.put("areaCode", "530700");
             mutableMap.put("date", "20200319");
             mutableMap.put("need3HourForcast", "1");
             var request = Request.Builder()
                 .get()
                 .header("Authorization", "APPCODE " + APP_CODE)
                 .url(mergeUrl(URL, mutableMap))
                 .build()
             okhttp.newCall(request).enqueue(object : Callback {
                 override fun onFailure(call: Call, e: IOException) {
                 }

                 override fun onResponse(call: Call, response: Response) {
                     it.onNext(response.body?.string())
                     it.onComplete()
                 }
             })
         }.subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe {
                 Log.d(TAG, "onCreate: " + it)
                 tvContent?.setText(it)
             }*/


    }

    fun mergeUrl(url: String, map: MutableMap<String, String>): String {
        var stringBuffer = StringBuffer(url)
        stringBuffer.append("?")
        map.forEach {
            var key = it.key
            var value = it.value
            stringBuffer.append(key)
            stringBuffer.append("=")
            stringBuffer.append(value)
            stringBuffer.append("&")
        }
        Log.d(TAG, "mergeUrl: " + stringBuffer.toString());
        return stringBuffer.delete(stringBuffer.length - 1, stringBuffer.length).toString()
    }

}



