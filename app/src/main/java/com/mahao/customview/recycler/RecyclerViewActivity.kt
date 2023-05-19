package com.mahao.customview.recycler

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.mahao.customview.R
import com.mahao.customview.recycler.util.HookClass
import com.mahao.customview.recycler.util.MyRecyclerPool
import com.mahao.customview.recycler.util.MyRecyclerView
import java.util.*

class RecyclerViewActivity : AppCompatActivity() {

    private val TAG = "RecyclerViewActivity"
    lateinit var recyclerView: MyRecyclerView;
    lateinit var btn1: Button
    lateinit var btn2: Button
    lateinit var btn3: Button
    lateinit var btn4: Button
    lateinit var btn5: Button
    lateinit var manager: LinearLayoutManager
    lateinit var adapter: ItemAdapter
    var currentClick = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        recyclerView = findViewById(R.id.recyclerView)
        btn1 = findViewById(R.id.btn_1)
        btn2 = findViewById(R.id.btn_2)
        btn3 = findViewById(R.id.btn_3)
        btn4 = findViewById(R.id.btn_4)
        btn5 = findViewById(R.id.btn_5)
        manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        manager.stackFromEnd = false
        //   recyclerView.setHasFixedSize(true)
        var animator = DefaultItemAnimator()
        animator.addDuration = 1000
        animator.removeDuration = 7000
        animator.changeDuration = 2000
        animator.moveDuration = 1000
        recyclerView.itemAnimator = animator
        recyclerView.layoutManager = manager
        manager.requestSimpleAnimationsInNextLayout()
        // manager.scrollToPositionWithOffset(32, 100)
        var recyclerPool = MyRecyclerPool(recyclerView)
        recyclerView.setRecycledViewPool(recyclerPool)
        adapter = ItemAdapter()
        adapter.setStringList(getStringList1())
        adapter.setOnItemClickListener(object : ItemAdapter.ItemClickListener {
            override fun onClick(view: View?, holder: ViewHolder?) {
                Log.d(TAG, "onClick: " + holder?.adapterPosition)
                currentClick = holder?.adapterPosition!!
                testOpenClose()
                testOpenClose(holder?.adapterPosition!!)
                var intent = Intent(this@RecyclerViewActivity,GridViewActivity::class.java)
                startActivity(intent)
            }
        })
        recyclerView.adapter = adapter
        adapter.setVisiableFlag(true)
        recyclerView.post({
           // setVisiableAnimal(false)
        })
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //    printCacheSize()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //      var cacheViewList = getCacheList("mCachedViews")
                //   Log.d(TAG, "onScrollStateChanged: " + cacheViewList?.size)
            }
        })
        var aa = false
        btn1.setOnClickListener {
            aa = !aa
            /*     if (aa) {
                     adapter.setStringList(getStringList2())
                 } else {
                     adapter.setStringList(getStringList1())
                 }*/

            //  adapter.mStringList.set(97, "abcd" + Random.Default.nextInt(1000))
            //  adapter.notifyDataSetChanged()
            //    adapter.notifyItemChanged(97)
            var childAt = manager.getChildAt(5)
            childAt?.let {
                var rect = Rect()
                manager.getTransformedBoundingBox(it, true, rect)
                Log.d(TAG, "onCreate: " + rect.toString())
            }
            //  recyclerView.smoothScrollToPosition(-1000)
            //  recyclerView.requestLayout()
            // printCacheSize()
            manager.scrollToPositionWithOffset(34, 100)
        }
        btn2.setOnClickListener {
            adapter.mStringList.set(3, "abcd")
            adapter.notifyItemChanged(3)
            recyclerView.postDelayed({
                adapter.mStringList.removeAt(3)
                adapter.notifyItemRemoved(3)
            }, 1000)
            printCacheSize()
        }
        btn3.setOnClickListener {
            printCacheSize()
            var helperField = manager.javaClass.getDeclaredField("mOrientationHelper")
            if (!helperField.isAccessible) {
                helperField.isAccessible = true
            }
            var helper = helperField.get(manager) as OrientationHelper
            for (index in 0..20) {
                var childView = manager.getChildAt(index)
                childView?.let {
                    var viewHolder = recyclerView.findContainingViewHolder(childView) as ViewHolder
                    viewHolder.mTvPosition.setText("position = " + viewHolder.adapterPosition + " end = " + helper.getDecoratedEnd(childView) + " start = " + helper.getDecoratedStart(childView))
                    var bottom = manager.getDecoratedBottom(childView)
                    Log.d(TAG, "onCreate: " + helper.endAfterPadding + " " + helper.startAfterPadding)
                    Log.d(TAG, "onCreate: ---  " + (-helper.getDecoratedStart(childView)
                            + helper.getStartAfterPadding()))
                }
            }
            Log.d(TAG, "onCreate: " + recyclerView.computeVerticalScrollExtent() + "  " +
                    recyclerView.computeVerticalScrollOffset() + " " +
                    recyclerView.computeVerticalScrollRange())

        }
        btn4.setOnClickListener {
            // setVisiableAnimal()
            /*           var removePosition = manager.findFirstVisibleItemPosition() + 1
                       var findViewHolderForAdapterPosition = recyclerView.findViewHolderForAdapterPosition(removePosition)
                       findViewHolderForAdapterPosition?.itemView?.left = -500
                       adapter.mStringList.removeAt(removePosition)
                       adapter.notifyItemRemoved(removePosition)*/
        //    var findFirstVisibleItemPosition = manager.findFirstVisibleItemPosition()
         //   var findLastVisibleItemPosition = manager.findLastVisibleItemPosition()
            //   adapter.mStringList.addLast("111")
            //   adapter.mStringList.addLast( "222")
            //   adapter.mStringList.add(0, "11")
            //    recyclerView.smoothScrollToPosition(0)
            //  adapter.notifyItemRangeInserted(0, 1)
            //       adapter.notifyItemRangeChanged(findFirstVisibleItemPosition,findLastVisibleItemPosition+1)
            //  recyclerView.smoothScrollToPosition(0)
            //     adapter.notifyItemRangeChanged(3,findLastVisibleItemPosition - 3 + 1)
            //   adapter.notifyItemRangeChanged(findFirstVisibleItemPosition, (findLastVisibleItemPosition - findFirstVisibleItemPosition) + 1)
            // setVisiableAnimal()
            /*     var firstTop = 0
                 for (index in findFirstVisibleItemPosition until findLastVisibleItemPosition + 1) {
                     var viewHolder = recyclerView.findViewHolderForAdapterPosition(index)
                     viewHolder?.let {
                         var layoutParams = viewHolder.itemView.layoutParams
                         viewHolder.itemView.left = recyclerView.width +100*index
                         if (index == findFirstVisibleItemPosition) {
                             firstTop = viewHolder.itemView.top
                         }
                         viewHolder.itemView.top = firstTop
                     }
                 }*/
            //    recyclerView.scrollToPosition(findFirstVisibleItemPosition)
            /*       for(index in findFirstVisibleItemPosition until findFirstVisibleItemPosition + 3){
                       adapter.mStringList.removeAt(index)
                   }*/
            Log.d(TAG, "onCreate:   ------ " + adapter.mStringList + " ")
            //   var removeLast = adapter.mStringList.removeAt(77)
            //     adapter.mStringList.add(findFirstVisibleItemPosition+3,"abc")
            //   adapter.notifyItemInserted(findFirstVisibleItemPosition+3)
            //    Log.d(TAG, "onCreate: " + adapter.mStringList +" " + removeLast)
            // adapter.mStringList.removeAt(findFirstVisibleItemPosition)
            //  recyclerView.itemAnimator = null
            //   adapter.notifyItemRemoved(findFirstVisibleItemPosition)
            //   adapter.notifyItemRangeChanged(findFirstVisibleItemPosition,7)
            //   adapter.notifyItemChanged(findFirstVisibleItemPosition)
            //    manager.scrollToPositionWithOffset(32, 350)
            recyclerView.requestLayout()
            //recyclerView.smoothScrollToPosition(29)
            //   adapter.mStringList.set(findFirstVisibleItemPosition,"abd"+ Random.Default.nextInt(110))
            //          adapter.notifyItemChanged(findFirstVisibleItemPosition)
            //    adapter.notifyItemRangeInserted(findFirstVisibleItemPosition , 4)
        }
        btn5.setOnClickListener {
             setVisiableAnimal(false)
        }
    }

    fun setVisiableAnimal(align: Boolean = true) {
        adapter.setVisiableFlag(true)
        var firstTop = 0
        var findFirstVisibleItemPosition = manager.findFirstVisibleItemPosition()
        var findLastVisibleItemPosition = manager.findLastVisibleItemPosition()
        for (index in findFirstVisibleItemPosition until findLastVisibleItemPosition + 1) {
            var viewHolder = recyclerView.findViewHolderForAdapterPosition(index)
            viewHolder?.let {
                viewHolder.itemView.left = recyclerView.width + if (!align) index * 100 else 0
                if (index == findFirstVisibleItemPosition) {
                    firstTop = viewHolder.itemView.top
                }
                viewHolder.itemView.top = firstTop
            }
        }
        adapter.notifyItemRangeChanged(findFirstVisibleItemPosition, (findLastVisibleItemPosition - findFirstVisibleItemPosition) + 1)
    }


    private fun printCacheSize() {
        //mChangeScrap
        var changeScrapList = getCacheList("mChangedScrap")
        Log.d(TAG, "onScrolled: changeScrap " + changeScrapList?.size)
        //mAttachScrap
        Log.d(TAG, "onScrolled: attachScrap " + getCacheList("mAttachedScrap")?.size)
        //mhideView
        Log.d(TAG, "onScrolled:  hideView " + getHideList()?.size)
        //mCachedViews
        var cacheViewList = getCacheList("mCachedViews")
        Log.d(TAG, "onScrolled:  cache " + cacheViewList?.size)
        //viewPool
        //    var recycledView = recyclerView.recycledViewPool.getRecycledView(0)
        var declaredMethod = recyclerView.recycledViewPool.javaClass.superclass.getDeclaredMethod("size")
        if (!declaredMethod.isAccessible) {
            declaredMethod.isAccessible = true
        }
        var size = declaredMethod.invoke(recyclerView.getRecycledViewPool())
        Log.d(TAG, "onScrolled: pool " + size + " " + recyclerView.recycledViewPool.getRecycledViewCount(0))
    }


    //  private var myList = MyArrayList<RecyclerView.ViewHolder>();
    private fun getCacheList(name: String): ArrayList<RecyclerView.ViewHolder>? {
        var recyclerField = recyclerView.javaClass.superclass.getDeclaredField("mRecycler")
        if (!recyclerField.isAccessible) {
            recyclerField.isAccessible = true
        }
        var recycler = recyclerField.get(recyclerView)
        var declaredField = recycler.javaClass.getDeclaredField(name)
        if (!declaredField.isAccessible) {
            declaredField.isAccessible = true
        }
        var get = declaredField.get(recycler) as? ArrayList<RecyclerView.ViewHolder>
        get?.let {
            for (index in get?.indices) {
                //    myList.add(get[index])
            }
        }
        var hookArrayList = HookClass().hookArrayList()
        declaredField.set(recycler, hookArrayList)
        return get
    }

    private fun getHideList(): List<View>? {
        var declaredField = recyclerView.javaClass.superclass.getDeclaredField("mChildHelper")
        if (!declaredField.isAccessible) {
            declaredField.isAccessible = true
        }
        var childHelper = declaredField.get(recyclerView)
        var hideViews = childHelper.javaClass.getDeclaredField("mHiddenViews")
        if (!hideViews.isAccessible) {
            hideViews.isAccessible = true
        }
        var hideViewList = hideViews.get(childHelper) as? List<View>
        return hideViewList
    }

    private fun getStringList1(): List<String> {
        var list = LinkedList<String>()
        for (i in 0..100) {
            list.add(i.toString())
        }
        Log.d(TAG, "onCreate: " + list.toString())
        return list
    }

    private fun getStringList2(): List<String> {
        var list = LinkedList<String>()
        for (index in 100 downTo 0) {
            list.add(index.toString())
        }
        return list
    }

    //每次都会改变。
    fun testOpenClose() {
        this.window.decorView.postDelayed({
      //      Log.d(TAG, "testOpenClose 1----: " + currentClick)
        }, 2000)
        Thread(Runnable {
            Thread.sleep(1000)
            Log.d(TAG, "testOpenClose 1----: " + currentClick)
        }).start()
    }

    //模拟网络i请求，耗时操作后，这个clickposition还是之前的值
    fun testOpenClose(clickPostion: Int) {
        this.window.decorView.postDelayed({
         //   Log.d(TAG, "testOpenClose 2=====: " + clickPostion)
        }, 2000)
        Thread(Runnable {
            Thread.sleep(1000)
            Log.d(TAG, "testOpenClose 2=====: " + clickPostion)
        }).start()
    }

}

















