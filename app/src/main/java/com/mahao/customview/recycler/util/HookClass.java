package com.mahao.customview.recycler.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class HookClass {

    public List hookArrayList(){
       // MyArrayList myArrayList = new MyArrayList();
        ArrayList arrayList2 = new ArrayList();
        List<RecyclerView.ViewHolder> arrayList = (List<RecyclerView.ViewHolder>) Proxy.newProxyInstance(ArrayList.class.getClassLoader(), ArrayList.class.getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //    method.invoke(myArrayList, args);
                return method.invoke(arrayList2,args);
            }
        });
        return arrayList2;
    }
}
