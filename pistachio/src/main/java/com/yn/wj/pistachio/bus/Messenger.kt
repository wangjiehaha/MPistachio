package com.yn.wj.pistachio.bus

import com.yn.wj.pistachio.binding.command.BindingConsumer
import java.lang.ref.WeakReference
import java.lang.reflect.Type
import java.util.*

/**
 *
 *
 * @date Create time：2020/2/2
 */
class Messenger {

    private lateinit var recipientsOfSubclassesAction: HashMap<Type, List<WeakActionAndToken>>
    private lateinit var recipientsStrictAction: HashMap<Type, List<WeakActionAndToken>>

    companion object {
        val instance = MessengerHolder.messenger
    }

    private object MessengerHolder {
        val messenger = Messenger()
    }
}

class WeakActionAndToken(var action: WeakAction<*>, any: Any)

class WeakAction<T>(objects: Any, val bindingConsumer: BindingConsumer<T>){

    private val weakReference: WeakReference<Any> = WeakReference(objects)

    fun execute(t: T? = null) {
        if (weakReference.get() != null) {
            bindingConsumer.call(t)
        }
    }

    fun getTarget() : Any?{
        return weakReference.get()
    }
}