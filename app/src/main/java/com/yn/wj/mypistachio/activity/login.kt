package com.yn.wj.mypistachio.activity

import android.app.Application
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.os.Bundle
import android.view.View
import com.yn.wj.mypistachio.BR
import com.yn.wj.mypistachio.R
import com.yn.wj.mypistachio.databinding.ActivityLoginBinding
import com.yn.wj.pistachio.base.BaseActivity
import com.yn.wj.pistachio.base.BaseModel
import com.yn.wj.pistachio.base.BaseViewModel
import com.yn.wj.pistachio.binding.command.BindingCommand
import com.yn.wj.pistachio.binding.command.BindingConsumer
import com.yn.wj.pistachio.bus.SingLiveEvent
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod

/**
 *
 *
 * @date Create time：2020/2/20
 */
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_login
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): LoginViewModel? {
        return ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    override fun initData() {

    }

    override fun initViewObservable() {
        viewModel.pSwitchEvent.observe(this, Observer {
            val boolean = viewModel.pSwitchEvent.value
            if (boolean == null || !boolean) {
                binding.ivSwitchPassword.setImageResource(R.drawable.iconvisibilitydark)
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            } else {
                binding.ivSwitchPassword.setImageResource(R.drawable.iconvisibilitywhite)
                binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
        })
    }

}

class LoginViewModel(app: Application, model: MainRepository? = null) : BaseViewModel<MainRepository>(app, model) {

    constructor(app: Application) : this(app, MainRepository())

    var userName: ObservableField<String> = ObservableField("")

    val password: ObservableField<String> = ObservableField("")

    var cleanButtonVisibility: ObservableInt = ObservableInt(View.INVISIBLE)

    var onFocusChangeCommand: BindingCommand<Boolean> = BindingCommand(object : BindingConsumer<Boolean> {
        override fun call(t: Boolean?) {
            cleanButtonVisibility.set(if (t!!) View.VISIBLE else View.INVISIBLE)
        }
    })

    var clearUserNameOnClickCommand: BindingCommand<Unit> = BindingCommand(object : BindingConsumer<Unit>{
        override fun call(t: Unit?) {
            userName.set("")
        }
    })

    var loginOnClickCommand: BindingCommand<Unit> = BindingCommand(object : BindingConsumer<Unit>{
        override fun call(t: Unit?) {
            login()
        }
    })

    var passwordShowSwitch: BindingCommand<Unit> = BindingCommand(object : BindingConsumer<Unit>{
        override fun call(t: Unit?) {
            pSwitchEvent.value = (pSwitchEvent.value == null || !pSwitchEvent.value!!)
        }
    })

    var pSwitchEvent: SingLiveEvent<Boolean> = SingLiveEvent()

    private fun login() {
        val boolean = model?.checkPassword(userName.get(), password.get())
        if (boolean != null && boolean) {

        } else {

        }
    }
}

class MainRepository : BaseModel() {

    fun checkPassword(usrName: String?, password: String?): Boolean {
        return "wangyani" == usrName && password == "19960128"
    }
}