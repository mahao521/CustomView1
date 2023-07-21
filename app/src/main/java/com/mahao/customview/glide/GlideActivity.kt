package com.mahao.customview.glide

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.engine.cache.DiskCache
import com.bumptech.glide.load.engine.cache.MemoryCache
import com.bumptech.glide.load.engine.executor.GlideExecutor
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.Downsampler
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.example.glidemodule.ui.theme.GlideApp
import com.example.glidemodule.ui.theme.GlideAppModule

import com.mahao.customview.R
import com.mahao.customview.glide.second.GlideRecyclerViewActivity
import com.mahao.customview.recycler.util.DisplayUtil
import com.mahao.customview.view.TransLateActivity
import java.io.File
import java.lang.ref.WeakReference
import java.lang.reflect.Field
import java.security.MessageDigest
import kotlin.math.max

class GlideActivity : AppCompatActivity(), View.OnClickListener {

    //  var iv1: ImageView? = null
    var llContainer: LinearLayout? = null
    var sv: ScrollView? = null

    private val TAG = "GlideActivity"
    private var memoryCache: MemoryCache? = null
    private var bitMapPool: GlideBitmapPool? = null
    private lateinit var btnCleanMemoryCahe: Button
    private lateinit var btnCleanBitmapPool: Button
    private lateinit var btnCleanDiskCache: Button

    companion object {
        fun getList(): List<String> {
            var list =
                mutableListOf<String>(
                    "https://file.moyublog.com/d/file/2020-12-22/6cd26be6253355531099894d7c76a375.jpg",
                    "http://pic1.win4000.com/wallpaper/2019-02-26/5c7529e88e17b.jpg",
                    "https://img2.autotimes.com.cn/news/2021/05/0530_170425821284.jpg",
                    "https://up.enterdesk.com/edpic_source/96/04/86/960486de8c4708f962e9c34c537d9bef.jpg",
                    "https://img1.baidu.com/it/u=2001068454,67533716&fm=253&fmt=auto&app=138&f=JPEG?w=750&h=500",
                    "https://file.moyublog.com/d/file/2021-01-30/f2c80dba6072a8c10bc5398d9a19e62c.jpg",
                    "https://pic4.zhimg.com/v2-278d16af291198b3ee3141f525c60bc7_r.jpg",
                    "http://pic1.win4000.com/wallpaper/2018-05-03/5aea7228e0c60.jpg",
                    "http://pic1.win4000.com/wallpaper/2018-05-24/5b06486513835.jpg",
                    "http://pic1.win4000.com/wallpaper/2/58413b9fa478a.jpg",
                    "http://pic1.win4000.com/wallpaper/2018-05-04/5aebef85c6315.jpg",
                    "https://www2.autoimg.cn/chejiahaodfs/g22/M02/B9/8A/autohomecar__ChwFRV_gXX6AY8ULACubNbKW00I909.jpg",
                    "https://data.znds.com/attachment/forum/201701/19/210050ssajym3ukizcqi43.jpg",
                    "http://pic1.win4000.com/wallpaper/2018-12-13/5c124fa04d80e.jpg",
                    "https://pic.rmb.bdstatic.com/fdaaec94ecde4cfb7f829c50eadc71ed.jpeg",
                    "https://img2.baidu.com/it/u=3217352827,2783840331&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800",
                    "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.alicdn.com%2Fi4%2F3241317125%2FO1CN01qfrpWT22VITw9PG4a_%21%213241317125.jpg&refer=http%3A%2F%2Fimg.alicdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1687877634&t=64f67af117737916c0c6ecb6926d4743",
                    "https://file.moyublog.com/d/file/2020-12-30/c093179782f722afcdbac12e2630a7c0.jpg",
                    "https://www.sinaimg.cn/dy/slidenews/21_img/2016_03/41065_4797222_625438.jpg",
                    "https://img2.baidu.com/it/u=1067295259,1221284291&fm=253&fmt=auto&app=138&f=JPEG?w=333&h=499",
                    "http://img.xspic.com/img8/194/85/2446786_7.jpg",
                    "https://file.moyublog.com/d/file/2022-04-12/2s3kqws33ez.jpg",
                    "https://file.moyublog.com/d/file/2021-01-29/df8456f1514c71aa3b988ce780eb0f2f.jpg",
                    "https://up.enterdesk.com/edpic_source/17/6c/57/176c57c6595f3ed077cc1af57948dd1c.jpg",
                    "https://img0.baidu.com/it/u=1045431367,115254100&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=749",
                )
            return list
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glide)
        //    iv1 = findViewById(R.id.iv_glide_1)
        var screenWidth = DisplayUtil.getScreenWidth(this)
        var screenHeight = DisplayUtil.getScreenHeight(this)
        Log.d(TAG, "onCreate: 屏幕宽高 " + screenWidth + "  " + screenHeight)
        llContainer = findViewById<LinearLayout>(R.id.ll_iv_container)
        btnCleanMemoryCahe = findViewById(R.id.btn_clear_memory)
        btnCleanBitmapPool = findViewById(R.id.btn_clear_pool)
        btnCleanDiskCache = findViewById(R.id.btn_clear_disk)
        sv = findViewById(R.id.scroll_view)

        btnCleanMemoryCahe.setOnClickListener(this)
        btnCleanDiskCache.setOnClickListener(this)
        btnCleanBitmapPool.setOnClickListener(this)

        memoryCache = GlideLruResourceCache(10*1024  * 1024)
        bitMapPool = GlideBitmapPool(10* 1024 * 1024)

        var declaredField1 = Glide::class.java.getDeclaredField("glide")
        if (!declaredField1.isAccessible) {
            declaredField1.isAccessible = true
        }
        var get1 = declaredField1.get(null)
        Log.d(TAG, "onCreate: glide - " + get1)
        if (get1 == null) {
            Log.d(TAG, "onCreate:  glide初始化")
            var builder = GlideBuilder()
            builder.setMemoryCache(memoryCache)
                //.setBitmapPool(bitMapPool)
                .setDefaultRequestOptions(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
                .setIsActiveResourceRetentionAllowed(true)
            /* .setSourceExecutor(GlideExecutor.newSourceExecutor())
          .setDiskCacheExecutor(GlideExecutor.newDiskCacheExecutor())*/
            Glide.init(this, builder)
        }
        getMemoryCacheSize()
        getBitmapPoolSize()
          startActivity(Intent(this, TransLateActivity::class.java))
           finish()
           return;

        var bitmapPool = Glide.get(this).bitmapPool
        var arrayPool = Glide.get(this).arrayPool
        Log.d(TAG, "onCreate:  bitmapPool " + bitmapPool.maxSize + " ... " + arrayPool)
        var ivList = getList().subList(0,3)
        // var ivList =
        //     arrayOf("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.alicdn.com%2Fi4%2F3241317125%2FO1CN01qfrpWT22VITw9PG4a_%21%213241317125.jpg&refer=http%3A%2F%2Fimg.alicdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1687877634&t=64f67af117737916c0c6ecb6926d4743")
        ivList.forEachIndexed { index, url ->
            var iv = ImageView(this);
            iv.adjustViewBounds = true
            iv.scaleType = ImageView.ScaleType.FIT_CENTER
            var layoutParams: LinearLayout.LayoutParams
            if (index == 6 || index == 7 || index == 5 || index == 4) {
                layoutParams = LinearLayout.LayoutParams(
                    700,
                    700
                );
            } else {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                );

            }
            iv.setOnClickListener {
                //   llContainer?.removeViewAt(0)
                var iv1 = ImageView(this);
                iv1.adjustViewBounds = true
                iv1.scaleType = ImageView.ScaleType.FIT_CENTER
                var layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                );
                llContainer?.addView(iv1, 0, layoutParams)
                Glide.with(this)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .listener(getListener(index))
                    .onlyRetrieveFromCache(true)
                    .into(iv1)
                System.gc()
                System.runFinalization()
                printActivityLog()
                //startActivity(Intent(this@GlideActivity, GlideRecyclerViewActivity::class.java))
            }
            //   iv.maxHeight = 2550
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL
            layoutParams.topMargin = DisplayUtil.dp2Px(this@GlideActivity, 6f)
            llContainer?.addView(iv, layoutParams)
            if (index == 0) {
                var requestOptions =
                    RequestOptions.centerCropTransform()//.set(Downsampler.FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS,true)
                Glide.with(iv)
                    .load(url)
                    //.skipMemoryCache(true)
                    .override(150,100)
                    // .optionalCenterCrop()
                    //   .apply(requestOptions)
                    //    .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL,com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
                    // .transform(CenterCrop())//, RoundedCorners(DisplayUtil.dp2Px(this, 10f)))Å
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(getListener(index))
                    .into(iv)
            } else {
                Glide.with(this)
                    .load(url)
                    .skipMemoryCache(true)

                 //   .transform(CircleCrop())
                    //    .error(R.drawable.ic_launcher_background)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    /*                   .thumbnail(
                                           Glide.with(this).asBitmap().load(getList().get(0)).override(200, 200)
                                       )*/
                    .listener(getListener(index))
                    .into(object : ImageViewTarget<Drawable>(iv) {

                        override fun onStart() {
                            super.onStart()
                        }

                        override fun onStop() {
                            super.onStop()
                        }

                        override fun setResource(resource: Drawable?) {
                            Log.d(
                                TAG,
                                "setResource:  finished   " + index + " " + (resource.toString())
                            )
                            setDrawable(resource)
                            // iv.setImageBitmap(resource)
                            /* if (resource != null) {
                                 var intrinsicHeight = resource.intrinsicHeight
                                 var intrinsicWidth = resource.intrinsicWidth
                                 if (intrinsicWidth * intrinsicHeight * 4 > 20 * 1024 * 1024) {
                                     iv.setImageDrawable(
                                         this@GlideActivity.resources.getDrawable(
                                             R.drawable.img_splashscreens,
                                             null
                                         )
                                     )
                                 } else {
                                     Log.d(TAG, "setResource: 图片内存大小 20 M")
                                 }
                             } else {

                             }*/
                            // iv.setImageDrawable(resource)
                        }
                    })
                //.into(iv)
            }
            if (index == 0) {
                printActivityLog()
            }
        }

        sv?.postDelayed({
            var height = sv?.height
            var width = sv?.width
            var llHieght = llContainer?.height
            Log.d(
                TAG,
                "onCreate:加载完成宽 " + width + " scrollerView高 " + height + " Linearlayout高  " + llHieght
            )
        }, 500)
    }


    fun <T> getListener(index: Int): RequestListener<T> {
        return object : RequestListener<T> {

            override fun onResourceReady(
                resource: T,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<T>??,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                Log.d(TAG, "onResourceReady: " + Thread.currentThread())
                Log.d(
                    TAG,
                    "index = " + index + ": model = " + model + "  taget " + target.toString() + "  dataType = " + dataSource.toString() + " isFirst = " + isFirstResource + "  resource = " + resource.toString()
                )
                if (resource is BitmapDrawable) {
                    Log.d(
                        TAG,
                        "onResourceReady:--- " + index + "  " + resource.bitmap.width + " " + resource.bitmap.height
                    )
                }
                return false
            }

            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<T>?,
                isFirstResource: Boolean
            ): Boolean {
                Log.d(TAG, "onLoadFailed: " + e.toString())
                return false;
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun printActivityLog() {
        var glide = Glide.get(this)
        var declaredField = Glide::class.java.getDeclaredField("engine")
        if (!declaredField.isAccessible) {
            declaredField.isAccessible = true
        }
        var engine = declaredField.get(glide)
        var activityResource = engine.javaClass.getDeclaredField("activeResources")
        if (!activityResource.isAccessible) {
            activityResource.isAccessible = true
        }
        var activityResources = activityResource.get(engine)
        var mapResourceField = activityResources.javaClass.getDeclaredField("activeEngineResources")
        if (!mapResourceField.isAccessible) {
            mapResourceField.isAccessible = true
        }
        var map = mapResourceField.get(activityResources)
        if (map is Map<*, *>) {
            Log.d(TAG, "printActivityLog: " + map.size)
            //    Log.d(TAG, "printActivityLog: 内存缓存 " + memoryCache?.currentSize)
            getMemoryCacheSize()
            map?.forEach { any, u ->
                if (u is WeakReference<*>) {
                    //  Log.d(TAG, "printActivityLog: " + "  " + map.size + " " + u.get())
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getMemoryCacheSize() {
        var glide = Glide.get(this)
        var memoryCacheField = glide.javaClass.getDeclaredField("memoryCache")
        if (!memoryCacheField.isAccessible) {
            memoryCacheField.isAccessible = true
        }
        var memoryCache = memoryCacheField.get(glide)
        var cache = memoryCache as MemoryCache
        var currentSize = cache.currentSize
        Log.d(TAG, "getMemoryCacheSize: current " + currentSize + " maxSize = " + cache.maxSize)
        if (currentSize > 0) {
            var declaredField: Field;
            if (cache is GlideLruResourceCache) {
                declaredField = cache.javaClass.superclass.superclass.getDeclaredField("cache")
            } else {
                declaredField = cache.javaClass.superclass.getDeclaredField("cache")
            }

            if (!declaredField.isAccessible) {
                declaredField.isAccessible = true
            }
            var mapCache = declaredField.get(memoryCache)
            if (mapCache is Map<*, *>) {
                mapCache?.forEach { any, u ->
                    Log.d(TAG, "getMemoryCacheSize: key = " + any + "\n   value = " + u)
                }
            }
        }
    }


    fun getDiskCacheSize() {
        var glide = Glide.get(this)
        var declaredField = Glide::class.java.getDeclaredField("engine")
        if (!declaredField.isAccessible) {
            declaredField.isAccessible = true
        }
        var engine = declaredField.get(declaredField)
        var providerField = engine.javaClass.getDeclaredField("diskCacheProvider")
        if (!providerField.isAccessible) {
            providerField.isAccessible = true
        }
        var provider = providerField.get(engine)
        var diskCacheField = provider.javaClass.getDeclaredField("diskCache")
        if (!diskCacheField.isAccessible) {
            diskCacheField.isAccessible = true
        }
        var diskCacheAny = diskCacheField.get(provider)
        var diskCache = diskCacheAny as DiskCache
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_clear_memory -> { //清理内存
                Log.d(TAG, "onClick: 清除内存")
                Glide.get(this).clearMemory()
            }

            R.id.btn_clear_pool -> { //清理bitmap池
                Glide.get(this).bitmapPool.clearMemory()
            }

            R.id.btn_clear_disk -> { //清理磁盘
                clearDisk()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun clearMemory() {
        var glide = Glide.get(this)
        Log.d(TAG, "clearMemory: 内存前 " + getMemoryCacheSize())
        glide.clearMemory()
        Log.d(TAG, "clearMemory: 内存后 " + getMemoryCacheSize())
    }

    fun clearDisk() {
        var thread = HandlerThread("disk")
        thread.start()
        Handler(thread.looper).post {
            Log.d(TAG, "clearDisk: ")
            Glide.get(this).clearDiskCache()
        }
    }

    fun getBitmapPoolSize() {
        var glide = Glide.get(this)
        var maxSize = glide.bitmapPool.maxSize
        Log.d(TAG, "getBitmapPoolSize: " + maxSize)

    }
}