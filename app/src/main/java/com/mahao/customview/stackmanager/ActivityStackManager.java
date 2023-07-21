package com.mahao.customview.stackmanager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Stack;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;

/**
 * Created by bigcatduan on 18/08/10.
 */
public class ActivityStackManager {
    private static final String TAG = "ActivityStackManager";
    /**
     * Activity栈
     */
    private Stack<WeakReference<FragmentActivity>> mActivityStack;

    private WeakReference<FragmentActivity> mCurrentActivity;


    private static ActivityStackManager activityStackManager = new ActivityStackManager();

    private ActivityStackManager() {
    }

    /***
     * 获得AppManager的实例
     *
     * @return AppManager实例
     */
    public static ActivityStackManager getInstance() {
        if (activityStackManager == null) {
            activityStackManager = new ActivityStackManager();
        }
        return activityStackManager;
    }


    /***
     * 栈中Activity的数
     *
     * @return Activity的数
     */
    public int stackSize() {
        return mActivityStack.size();
    }

    /***
     * 获得Activity栈
     *
     * @return Activity栈
     */
    public Stack<WeakReference<FragmentActivity>> getStack() {
        return mActivityStack;
    }


    public void setCurrentActivity(WeakReference<FragmentActivity> currentActivity) {
        this.mCurrentActivity = currentActivity;
    }


    /**
     * 添加Activity到堆栈
     */
    public void addActivity(WeakReference<FragmentActivity> activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        Log.d("bigcatduan", "add activity: " + activity.toString());
        mActivityStack.add(activity);
    }

    /**
     * //为什么 Activity.finish() 之后 10s 才 onDestroy ？通过onDestroy移除栈时间晚，所以getTopActivity不准确
     *
     * @param activity
     */
    public void fixTopActivity(WeakReference<FragmentActivity> activity) {
        if (mActivityStack != null) {
            FragmentActivity topActivity = getTopActivity();
            //onReseume时判断，topActivity和当前activity不同，说明有错误，需要修复
            if (activity == null || activity.get() == null || topActivity == null) {
                return;
            }
            Log.d(TAG, "fixTopActivity: " + Thread.currentThread().getName());
            if (activity.get() != topActivity) {
                Log.d("chao", "need fix activity: ");
                mActivityStack.pop();//top不对，先把top移除
                //循环遍历找到当前activity为止，把当前activity栈上面的activity移除。
                while (!mActivityStack.isEmpty()) {
                    WeakReference<FragmentActivity> stackActivity = mActivityStack.peek();
                    if (stackActivity.get() == null) {
                        Log.d("chao", "need fix activity: null");
                        mActivityStack.pop();
                        continue;
                    }
                    //找到当前activity后，break
                    if (stackActivity.get() == activity.get()) {
                        Log.d("chao", "need fix activity: fixed");
                        break;
                    } else {//没找到，移除
                        Log.d("chao", "need fix activity: fix remove");
                        mActivityStack.pop();
                    }
                }
            }
        }
    }

    /**
     * 删除ac
     *
     * @param activity 弱引用的ac
     */
    public void removeActivity(WeakReference<FragmentActivity> activity) {
        if (mActivityStack != null) {
            mActivityStack.remove(activity);
        }
        Log.d("bigcatduan", "removeActivity: " + activity.toString());
    }

    /***
     * 获取栈顶Activity（堆栈中最后一个压入的）
     *
     * @return Activity
     */
    public synchronized FragmentActivity getTopActivity() {
        try {
            if (mActivityStack != null && mActivityStack.lastElement() != null) {
                FragmentActivity activity = mActivityStack.lastElement().get();
                if (null == activity) {
                    return null;
                } else {
                    //B返回A  生命周期 . B onPause --> A restart --> A start --> A resume ---> B onStop  ---> B onDestroy
                    //所以在A resume阶段去获取 top。 获取的仍旧是B。
                    FragmentActivity lastActivity = mActivityStack.lastElement().get();
                    FragmentActivity currentActivity = mCurrentActivity.get();
                    if (currentActivity != null && lastActivity != null && !lastActivity.equals(currentActivity)) {
                        boolean currentIsResume =
                                currentActivity.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED);
                        Lifecycle.State topActivityState = lastActivity.getLifecycle().getCurrentState();
                        Log.d(TAG, "getTopActivity: " + currentIsResume +"   00000 " + topActivityState );
                        if ((topActivityState == Lifecycle.State.DESTROYED
                                || topActivityState == Lifecycle.State.CREATED) && currentIsResume) {
                            return currentActivity;
                        } else {
                            return lastActivity;
                        }
                    }
                    return lastActivity;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public FragmentActivity getTopActivity1() {
        try {
            if (mActivityStack != null && mActivityStack.lastElement() != null) {
                FragmentActivity activity = mActivityStack.lastElement().get();
                if (null == activity) {
                    return null;
                } else {
                    return mActivityStack.lastElement().get();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public FragmentActivity getActivityPreToTop() {
        if (mActivityStack != null && mActivityStack.size() >= 2) {
            FragmentActivity activity = mActivityStack.elementAt(mActivityStack.size() - 2).get();
            if (null == activity) {
                return null;
            } else {
                return mActivityStack.elementAt(mActivityStack.size() - 2).get();
            }
        } else {
            return null;
        }

    }

    /***
     * 通过class 获取栈顶Activity
     *
     * @param cls
     * @return Activity
     */
    public FragmentActivity getActivityByClass(Class<?> cls) {
        FragmentActivity return_activity = null;
        if (mActivityStack != null && mActivityStack.size() > 0) {
            for (WeakReference<FragmentActivity> activity : mActivityStack) {
                if (activity.get().getClass().equals(cls)) {
                    return_activity = activity.get();
                    break;
                }
            }
        }
        return return_activity;
    }

    /**
     * 结束栈顶Activity（堆栈中最后一个压入的）
     */
    public synchronized void killTopActivity() {
        try {
            WeakReference<FragmentActivity> activity = mActivityStack.lastElement();
            killActivity(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 结束指定的Activity
     *
     * @param activity
     */
    public synchronized void killActivity(WeakReference<FragmentActivity> activity) {
        try {
            Iterator<WeakReference<FragmentActivity>> iterator = mActivityStack.iterator();
            while (iterator.hasNext()) {
                WeakReference<FragmentActivity> stackActivity = iterator.next();
                if (stackActivity.get() == null) {
                    iterator.remove();
                    continue;
                }
                if (stackActivity.get().getClass().getName().equals(activity.get().getClass().getName())) {
                    iterator.remove();
                    stackActivity.get().finish();
                    stackActivity.get().overridePendingTransition(0, 0);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /***
     * 结束指定类名的Activity
     *
     * @param cls
     */
    public synchronized void killActivityExceptTop(Class<?>... cls) {
        try {
            for (int i = 0; i < cls.length; i++) {
                FragmentActivity topActivity = getTopActivity();
                ListIterator<WeakReference<FragmentActivity>> listIterator = mActivityStack.listIterator();
                while (listIterator.hasNext()) {
                    FragmentActivity activity = listIterator.next().get();
                    if (activity == null) {
                        listIterator.remove();
                        continue;
                    }
                    if (!activity.equals(topActivity) && isCertainClass(activity.getClass(), cls[i])) {
                        listIterator.remove();
                        if (activity != null) {
                            activity.finish();
                            activity.overridePendingTransition(0, 0);
                        }
                    }
//                    if ((activity.getClass() == cls[i] || activity.getClass().getSuperclass() == cls[i]) && !activity.equals(topActivity)) {
//                        listIterator.remove();
//                        if (activity != null) {
//                            activity.finish();
//                            activity.overridePendingTransition(0, 0);
//                        }
////                    break;
//                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 结束指定类名的Activity
     *
     * @param cls
     */
    public synchronized void killActivityIncludeSuper(Class<?>... cls) {
        try {
            for (int i = 0; i < cls.length; i++) {
                ListIterator<WeakReference<FragmentActivity>> listIterator = mActivityStack.listIterator();
                while (listIterator.hasNext()) {
                    FragmentActivity activity = listIterator.next().get();
                    if (activity == null) {
                        listIterator.remove();
                        continue;
                    }
                    if (isCertainClass(activity.getClass(), cls[i])) {
                        listIterator.remove();
                        if (activity != null) {
                            try {
                                Class c = Class.forName("hy.sohu.com.app.common.base.view.BaseActivity");
                                Field callField = c.getDeclaredField("fromTopToBottom");
                                callField.setAccessible(true);
                                callField.setBoolean(activity, false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            activity.finish();
                            activity.overridePendingTransition(0, 0);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isCertainClass(Class<?> activityCls, Class<?> cls) {
        if (activityCls == cls) {
            return true;
        }
        if (activityCls.getSuperclass() != null && activityCls.getSuperclass() != FragmentActivity.class) {
            return isCertainClass(activityCls.getSuperclass(), cls);
        } else {
            return false;
        }
    }

    public synchronized void killProfileAndFeeddetail(Class<?>... cls) {
        try {
            ListIterator<WeakReference<FragmentActivity>> listIterator = mActivityStack.listIterator();
            while (listIterator.hasNext()) {
                FragmentActivity activity = listIterator.next().get();
                if (activity == null) {
                    listIterator.remove();
                    continue;
                }
                boolean shouldRemove = true;
                for (int i = 0; i < cls.length; i++) {
                    if (activity.equals(getTopActivity()) || activity.equals(getActivityPreToTop()) || activity.getClass() == cls[i]) {
                        shouldRemove = false;
                        break;
                    }
                }
                if (shouldRemove) {
                    listIterator.remove();
                    if (activity != null) {
                        activity.finish();
                        activity.overridePendingTransition(0, 0);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isInStack(Class cls) {
        try {
            ListIterator<WeakReference<FragmentActivity>> listIterator = mActivityStack.listIterator();
            while (listIterator.hasNext()) {
                FragmentActivity activity = listIterator.next().get();
                if (activity.getClass() == cls) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized void killActivityExcept(Class<?>... cls) {
        try {
            FragmentActivity topActivity = getTopActivity();

            ListIterator<WeakReference<FragmentActivity>> listIterator = mActivityStack.listIterator();
            while (listIterator.hasNext()) {
                FragmentActivity activity = listIterator.next().get();
                if (activity == null) {
                    listIterator.remove();
                    continue;
                }
                boolean shouldRemove = true;
                for (int i = 0; i < cls.length; i++) {
                    if (activity.getClass() == cls[i] || activity.equals(topActivity)) {
                        shouldRemove = false;
                        break;
                    }
                }
                if (shouldRemove) {
                    listIterator.remove();
                    if (activity != null) {
                        activity.finish();
                        activity.overridePendingTransition(0, 0);
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 结束指定类名的Activity
     *
     * @param cls
     */
    public synchronized void killActivity(Class<?> cls) {
        try {
            ListIterator<WeakReference<FragmentActivity>> listIterator = mActivityStack.listIterator();
            while (listIterator.hasNext()) {
                FragmentActivity activity = listIterator.next().get();
                if (activity == null) {
                    listIterator.remove();
                    continue;
                }
                if (activity.getClass() == cls) {
                    listIterator.remove();
                    if (activity != null) {
                        activity.finish();
                        activity.overridePendingTransition(0, 0);
                    }
//                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束所有Activity
     */
    public synchronized void killAllActivity() {
        try {
            ListIterator<WeakReference<FragmentActivity>> listIterator = mActivityStack.listIterator();
            while (listIterator.hasNext()) {
                FragmentActivity activity = listIterator.next().get();
                if (activity != null) {
                    activity.finish();
                    activity.overridePendingTransition(0, 0);
                }
                listIterator.remove();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 移除除了某个activity的其他所有activity
     *
     * @param cls 界面
     */
    public synchronized void killAllActivityExceptOne(Class cls) {
        try {
            for (int i = 0; i < mActivityStack.size(); i++) {
                WeakReference<FragmentActivity> activity = mActivityStack.get(i);
                if (activity.getClass().equals(cls)) {
                    break;
                }
                if (mActivityStack.get(i) != null) {
                    killActivity(activity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 移除某个指定actvity之上的所有actvity，如果栈中无该actvity则不做任何操作
     *
     * @param cls 界面
     */
    public synchronized void killAllActivityAfter(Class cls) {
        try {
            if (isInStack(cls)) {
                Handler handler = new Handler(Looper.getMainLooper());
                while (!mActivityStack.peek().get().getClass().equals(cls)) {
                    WeakReference<FragmentActivity> actvity = mActivityStack.pop();
                    if (actvity.get() != null) {
                        actvity.get().finish();
                        actvity.get().overridePendingTransition(0, 0);
                    }
                    Log.d("zf__", "actvity = " + actvity.get().getClass().getSimpleName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Stack<WeakReference<FragmentActivity>> mActivityTemp = new Stack<>();

    public synchronized void print(String TAG) {
        if (mActivityStack != null) {
            mActivityTemp.addAll(mActivityStack);
            while (mActivityTemp.size() > 0) {
                WeakReference<FragmentActivity> actvity = mActivityTemp.pop();
                Log.d(TAG, "actvity = " + actvity.get().getClass().getSimpleName());
            }
        }
    }

}
