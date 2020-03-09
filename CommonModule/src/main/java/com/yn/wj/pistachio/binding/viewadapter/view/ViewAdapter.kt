package com.yn.wj.pistachio.binding.viewadapter.view

import android.annotation.SuppressLint
import android.databinding.BindingAdapter
import android.view.View
import com.yn.wj.pistachio.binding.command.BindingCommand
import com.jakewharton.rxbinding2.view.RxView
import java.util.concurrent.TimeUnit

/**
 *
 *
 * @date Create time：2020/2/20
 */
@SuppressLint("CheckResult")
object ViewAdapter {

    @JvmStatic
    @BindingAdapter(value = ["onClickCommand", "isThrottleFirst"], requireAll = false)
    fun onClickCommand(view: View, bindingCommand: BindingCommand<Unit>?, isThrottleFirst: Boolean?) {
        if (isThrottleFirst == null || !isThrottleFirst) {
            RxView.clicks(view).subscribe { bindingCommand?.execute() }
        } else {
            RxView.clicks(view).throttleFirst(CLICK_INTERVAL.toLong(), TimeUnit.SECONDS)
                    .subscribe{ bindingCommand?.execute() }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["onLongClickCommand"], requireAll = false)
    fun onLongClickCommand(view: View, bindingCommand: BindingCommand<Unit>?) {
        RxView.longClicks(view).subscribe { bindingCommand?.execute()}
    }

    @JvmStatic
    @BindingAdapter(value = ["onFocusChangeCommand"])
    fun onFocusChangeCommand(view: View, bindingCommand: BindingCommand<Boolean>?) {
        view.onFocusChangeListener = View.OnFocusChangeListener {
            v, hasFocus -> bindingCommand?.execute(hasFocus)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["isVisible"], requireAll = false)
    fun isVisible(view: View, visible: Boolean?) {
        view.visibility = if (visible == null || visible) View.VISIBLE else View.GONE
    }
}
const val CLICK_INTERVAL: Int = 1