package com.yn.wj.pistachio.binding.viewadapter.checkbox

import android.databinding.BindingAdapter
import android.widget.CheckBox
import com.yn.wj.pistachio.binding.command.BindingCommand

/**
 *
 *
 * @date Create time：2020/2/20
 */
object ViewAdapter {

    @JvmStatic
    @BindingAdapter(value = ["onCheckedChangedCommand"], requireAll = false)
    fun setCheckedChanged(checkBox: CheckBox, bindingCommand: BindingCommand<Boolean>) {
        checkBox.setOnCheckedChangeListener { buttonView, isChecked -> bindingCommand.execute(isChecked) }
    }
}