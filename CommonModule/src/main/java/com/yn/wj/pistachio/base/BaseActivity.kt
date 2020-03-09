package com.yn.wj.pistachio.base

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 *
 *
 * @date Create time：2020/2/1
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@SuppressLint("NewApi")
abstract class BaseActivity<V: ViewDataBinding, VM: BaseViewModel<*>>: RxAppCompatActivity(), IBaseView {

    protected lateinit var binding: V
    protected lateinit var viewModel: VM
    private var viewModelId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParam()
        initViewDataBinding(savedInstanceState)
        registerUIChangeLiveDataCallBack()
        initData()
        initViewObservable()
        viewModel.registerRxBus()
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    private fun initViewDataBinding(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, initContentView(savedInstanceState))
        viewModelId = initVariableId()
        var model: VM? = initViewModel()
        if (model == null) {
            val type: Type = javaClass.genericSuperclass
            val modelClass:Class<*> = if (type is ParameterizedType) {
                type.actualTypeArguments[1] as Class<*>
            } else {
                BaseViewModel::class.java
            }
            model = createViewModel(this, modelClass)
        }
        viewModel = model
        binding.setVariable(viewModelId, viewModel)
        lifecycle.addObserver(viewModel)
        viewModel.injectLifecycleProvider(this)
    }

    private fun registerUIChangeLiveDataCallBack() {
        viewModel.startActivityEvent?.observe(this, Observer {
            val clazz: Class<*> = it?.get(BaseViewModel.ParameterField.CLASS) as Class<*>
            val bundle: Bundle? = it[BaseViewModel.ParameterField.BUNDLE] as Bundle
            startActivity(clazz, bundle)
        })
        viewModel?.startContainerActivityEvent?.observe(this, Observer {
            val name: String = it?.get(BaseViewModel.ParameterField.CANONICAL_NAME) as String
            val bundle: Bundle? = it[BaseViewModel.ParameterField.BUNDLE] as Bundle
            startContainerActivity(name, bundle)
        })
    }

    fun startActivity(clazz: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, clazz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    fun startContainerActivity(name: String, bundle: Bundle?) {
        val intent = Intent(this, ContainerActivity::class.java)
        intent.putExtra(KEY_FRAGMENT, name)
        if (bundle != null) {
            intent.putExtra(KEY_BUNDLE, bundle)
        }
        startActivity(intent)
    }

    private fun <T : ViewModel> createViewModel(fragmentActivity: FragmentActivity, clazz: Class<*>): T {
        return ViewModelProviders.of(fragmentActivity).get(clazz as Class<T>)
    }

    override fun initParam() {

    }

    abstract fun initContentView(savedInstanceState: Bundle?): Int

    abstract fun initVariableId(): Int

    abstract fun initViewModel(): VM?
}