package com.yn.wj.pistachio.binding.command

/**
 *
 *
 * @date Create time：2020/2/2
 */
class BindingCommand<T>(private val bindingConsumer: BindingConsumer<T>) {

    fun execute(t: T? = null) {
        bindingConsumer.call(t)
    }
}