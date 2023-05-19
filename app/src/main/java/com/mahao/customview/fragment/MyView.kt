package com.mahao.customview.fragment

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.ViewTreeSavedStateRegistryOwner
import com.mahao.customview.fragment.viewmodel.TestViewModel1
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass

class MyView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : RelativeLayout(context, attrs, defStyleAttr) {

    val viewModel1: TestViewModel1 by ((context as Fragment).viewModels())

    private val TAG = "MyView"

    init {
        var findViewTreeLifecycleOwner = findViewTreeLifecycleOwner()
        var lifecyclerOwner = ViewTreeLifecycleOwner.get(this)
        lifecyclerOwner?.lifecycle?.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            }
        })

        var viewModelStoreOwner = ViewTreeViewModelStoreOwner.get(this)
        var viewModel1 = ViewModelProvider(viewModelStoreOwner!!).get(TestViewModel1::class.java)
        viewModel1.testSave()

        var savedStateRegistryOwner = ViewTreeSavedStateRegistryOwner.get(this)
        savedStateRegistryOwner?.savedStateRegistry?.registerSavedStateProvider(TAG, object : SavedStateRegistry.SavedStateProvider {
            override fun saveState(): Bundle {
                var bundle = Bundle()
                bundle.putString("name", "haha")
                return bundle;
            }
        })
    }

    fun <T : AA> test2(viewModelClass: KClass<T>) {
    }

    inline fun <reified T : AA> test1(value: Int) {
        test2(T::class)
    }
}

interface AA {

}


/*@MainThread
public inline fun <reified VM : ViewModel> Fragment.viewModels(
        noinline ownerProducer: () -> ViewModelStoreOwner = { this },
        noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> {
    return createViewModelLazy(
            VM::class, { ownerProducer().viewModelStore },
            factoryProducer ?: {
                (ownerProducer() as? HasDefaultViewModelProviderFactory)?.defaultViewModelProviderFactory
                        ?: defaultViewModelProviderFactory
            }
    )
}*/

