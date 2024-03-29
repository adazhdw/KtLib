package com.adazhdw.ktlib.core.viewbinding

import android.app.Activity
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


class ActivityViewBindingDelegate<T : ViewBinding>(private val clazz: Class<T>) : ReadOnlyProperty<Activity, T> {
    private var binding: T? = null

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Activity, property: KProperty<*>): T {
        binding?.let { return it }

        //inflate View class
        val inflateMethod = clazz.getMethod("inflate", LayoutInflater::class.java)

        //Bind layout
        val bindLayout = inflateMethod.invoke(null, thisRef.layoutInflater) as T

        //Set the content view
        thisRef.setContentView(bindLayout.root)

        return bindLayout.also { this.binding = it }
    }
}

inline fun <reified T : ViewBinding> Activity.inflate2() = ActivityViewBindingDelegate<T>(T::class.java)


inline fun <reified T : ViewBinding> Activity.inflate3() = lazy(LazyThreadSafetyMode.NONE) {
    this.inflate<T>(layoutInflater)
}

inline fun <reified T : ViewBinding> Activity.inflate(layoutInflater: LayoutInflater): T {
    return T::class.java.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as T
}

inline fun <reified T : ViewBinding> Activity.inflate() = lazy(LazyThreadSafetyMode.NONE) {
    this.inflate<T>(layoutInflater).apply { setContentView(this.root) }
}


