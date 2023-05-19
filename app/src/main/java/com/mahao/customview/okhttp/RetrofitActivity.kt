package com.mahao.customview.okhttp

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mahao.customview.R
import com.mahao.customview.okhttp.RetrofitActivity.Companion.APP_CODE
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import java.io.File
import java.util.concurrent.Executors

class RetrofitActivity : AppCompatActivity() {

    private val TAG = "RetrofitActivity"

    companion object {
        const val APP_KEY = "tbyhzRE5IwMXoN1GuT6mTAjafvpshiuf"
        const val APP_CODE = "68cae9fe5b0449efb276f3c7e05ac872"
        const val URL = "https://ali-weather.showapi.com/area-to-weather-date"
        const val BASE_URL = "https://ali-weather.showapi.com"

    }

   lateinit var file: File


    private var tvContent: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrofit)
        tvContent = findViewById<TextView>(R.id.tv_retrofit)
        file = File(cacheDir, "mahao.txt")

        /* Observable.create<DateBean> {
             var okHttpClient = OkHttpClient.Builder().addNetworkInterceptor(object : Interceptor {
                 override fun intercept(chain: Interceptor.Chain): Response {
                     Log.d(TAG, "intercept: ------进行了网络请求")
                     var request = chain.request()
                     Log.d(TAG, "intercept: " + request.toString())
                     return chain.proceed(request)
                 }
             }).cache(Cache(file, 10 * 1024 * 1024))
                 .build()
             var retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                 .addConverterFactory(GsonConverterFactory.create())
                 .addCallAdapterFactory()
                 .client(okHttpClient)
                 .build()
             var service = retrofit.create(Service::class.java)
             var body = service.getDate2(
                 path = "area-to-weather-date",
                 map = mutableMapOf<String, String>(
                     "area" to "丽江",
                     "areaCode" to "530700",
                     "date" to "20200319",
                     "need3HourForcast" to "1"
                 )
             ).execute().body()
             Log.d(TAG, "onCreate: " + body)
             it.onNext(body);
             it.onComplete()
         }.subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe {
                 tvContent.setText(it.getShowapi_res_body().getCityInfo().toString())
             }*/

        /*Observable.create<DateBean> {
            var okHttpClient = OkHttpClient.Builder().addNetworkInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    Log.d(TAG, "intercept: ------进行了网络请求")
                    var request = chain.request()
                    Log.d(TAG, "intercept: " + request.toString())
                    return chain.proceed(request)
                }
            }).cache(Cache(file, 10 * 1024 * 1024))
                .build()
            var retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
            var service = retrofit.create(Service::class.java)
            var body = service.getDate2(
                path = "area-to-weather-date",
                map = mutableMapOf<String, String>(
                    "area" to "丽江",
                    "areaCode" to "530700",
                    "date" to "20200319",
                    "need3HourForcast" to "1"
                )
            ).enqueue(object : Callback<DateBean> {
                override fun onResponse(
                    call: Call<DateBean>,
                    response: retrofit2.Response<DateBean>
                ) {
                    it.onNext(response.body())
                    it.onComplete()
                    Log.d(TAG, "onCreate:  success ")
                }

                override fun onFailure(call: Call<DateBean>, t: Throwable) {
                    it.onComplete()
                    Log.d(TAG, "onCreate: fail")
                }
            })
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                tvContent.setText(it.getShowapi_res_body().getCityInfo().toString())
            }*/


        /*.subscribe {
                tvContent.setText(it.getShowapi_res_body().getCityInfo().toString())
            }*/
        getNetWork1()
    }

    fun getNetWork1() {
        Log.d(TAG, "getNetWork1: " + Thread.currentThread().name)
        val executorService = Executors.newCachedThreadPool();
        var okHttpClient = OkHttpClient.Builder().dispatcher(Dispatcher(executorService))
            .addInterceptor label@{
                Log.d(TAG, "getNetWork1:  数据请求发起线程  " + Thread.currentThread().name)
                return@label it.proceed(it.request())
            }
            .addNetworkInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    Log.d(TAG, "intercept: ------进行了网络请求" + Thread.currentThread().name)
                    var request = chain.request()
                    Log.d(TAG, "intercept: " + request.toString())
                    return chain.proceed(request)
                }
            }).cache(Cache(file, 10 * 1024 * 1024))
            .build()
        var retrofit = Retrofit.Builder().baseUrl(RetrofitActivity.BASE_URL)
            //  .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(okHttpClient)
            //异步结果返回的线程。就是enqueue的caback所在线程，默认主线程。
            .callbackExecutor(Executors.newCachedThreadPool())
            .build()
        // retrofit.callFactory().newCall()
        var service = retrofit.create(Service::class.java)
        service.getDate3(
            path = "area-to-weather-date",
            map = mutableMapOf<String, String>(
                "area" to "丽江",
                "areaCode" to "530700",
                "date" to "20200319",
                "need3HourForcast" to "1"
            )
        ).enqueue(object : Callback<DateBean> {
            override fun onResponse(
                call: Call<DateBean>,
                response: retrofit2.Response<DateBean>
            ) {
                Log.d(TAG, "onResponse: enqueue数据返回线程 " + Thread.currentThread().name)
                var body = response.body()
                tvContent?.setText(response.body()?.toString())
            }

            override fun onFailure(call: Call<DateBean>, t: Throwable) {
            }
        })
    }
}


class DateRequestBean {
    var area = ""
    var areaCode = ""
    var date = ""
    var need3HourForcast = ""

    constructor(area: String, areaCode: String, date: String, need3HourForcast: String) {
        this.area = area
        this.areaCode = areaCode
        this.date = date
        this.need3HourForcast = need3HourForcast
    }
}

interface Service {

    @Headers(
        "Cache-Control: max-age=640000, max-stale=864000",
        "Authorization: APPCODE " + APP_CODE,
    )
    @GET("/area-to-weather-date?area=丽江&areaCode=530700&date=20200319&need3HourForcast=1")
    fun getDate(): Call<DateBean>


    @Headers(
        "Cache-Control: max-age=640000, max-stale=864000",
        "Authorization: APPCODE " + APP_CODE,
    )
    @GET("/area-to-weather-date")
    fun getDate1(
        @Query("area") area: String,
        @Query("areaCode") areaCode: String,
        @Query("date") date: String,
        @Query("need3HourForcast") need3HourForcast: String
    ): Call<DateBean>


    @Headers(
        "Cache-Control: max-age=640000, max-stale=864000",
        "Authorization: APPCODE " + APP_CODE,
    )
    @GET("/{path}")
    fun getDate2(
        @Path("path") path: String,
        @QueryMap() map: MutableMap<String, String>
    ): Call<DateBean>


    @Headers(
        "Cache-Control: max-age=640000, max-stale=864000",
        "Authorization: APPCODE " + APP_CODE,
    )
    @GET("/{path}")
    fun getDate3(
        @Path("path") path: String,
        @QueryMap() map: MutableMap<String, String>
    ): Call<DateBean>


    /*  */
    /**
     *   只有post才有body。
     *//*
    @Headers(
        "Cache-Control: max-age=640000, max-stale=864000",
        "Authorization: APPCODE " + APP_CODE,
    )
    @GET("/area-to-weather-date")
    fun getDate3(@Field("reuqestBean") reuqestBean: DateRequestBean): Call<DateBean>*/

}