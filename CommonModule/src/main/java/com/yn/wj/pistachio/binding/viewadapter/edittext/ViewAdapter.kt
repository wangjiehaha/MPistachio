package com.yn.wj.pistachio.binding.viewadapter.edittext

import android.content.Context
import android.databinding.BindingAdapter
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.yn.wj.pistachio.binding.command.BindingCommand

/**
 *
 *
 * @date Create time：2020/2/20
 */
object ViewAdapter {

    @JvmStatic
    @BindingAdapter(value = ["requestFocus"], requireAll = false)
    fun requestFocusCommand(editText: EditText, needFocus: Boolean) {
        if (needFocus) {
            editText.setSelection(editText.text.length)
            editText.requestFocus()
            val imm: InputMethodManager = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        }
        editText.isFocusableInTouchMode = needFocus
    }

    @JvmStatic
    @BindingAdapter(value = ["textChanged"], requireAll = false)
    fun addTextChangedListener(editText: EditText, bindingCommand: BindingCommand<String>?) {

        editText.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                bindingCommand?.execute(s?.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
}