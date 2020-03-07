package com.yn.wj.pistachio.binding.command

/**
 *
 *
 * @date Create time：2020/2/2
 */
interface BindingConsumer<T> {
    fun call(t: T? = null)
}