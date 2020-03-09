package com.yn.wj.pistachio.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle2.components.support.RxFragment
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 *
 *
 * @date Create time：2020/2/19
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
abstract class BaseFragment<V: ViewDataBinding, VM: BaseViewModel<*>> : RxFragment(), IBaseView {

    protected var binding: V? = null
    protected var viewModel: VM? = null
    private var viewModelId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParam()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, initContentView(inflater, container, savedInstanceState), container,false)
        return binding?.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onDestroyView() {
        super.onDestroyView()
        viewModel?.removeRxBus()
        binding?.unbind()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewDataBinding()
        initData()
        initViewObservable()
        viewModel?.registerRxBus()
    }

    private fun initViewDataBinding() {
        viewModelId = initVariableId()
        viewModel = initViewModel()
        if (viewModel == null) {
            val type: Type = javaClass.genericSuperclass
            val modelClass:Class<*> = if (type is ParameterizedType) {
                type.actualTypeArguments[1] as Class<*>
            } else {
                BaseViewModel::class.java
            }
            viewModel = createViewModel(this, modelClass)
        }
        binding?.setVariable(viewModelId, viewModel)
        lifecycle.addObserver(viewModel!!)
        viewModel?.injectLifecycleProvider(this)
    }

    override fun initParam() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun isBackPress(): Boolean {
        return false
    }

    fun initViewModel(): VM? {
        return null
    }

    fun startActivity(clazz: Class<*>, bundle: Bundle? = null) {
        val intent = Intent(context, clazz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    fun startContainerActivity(canonicalName: String, bundle: Bundle? = null) {
        val intent = Intent(context, ContainerActivity::class.java)
        intent.putExtra(KEY_FRAGMENT, canonicalName)
        if (bundle != null) {
            intent.putExtra(KEY_BUNDLE, bundle)
        }
        startActivity(intent)
    }

    private fun <T : ViewModel> createViewModel(fragment: BaseFragment<V, VM>, clazz: Class<*>): T {
        return ViewModelProviders.of(fragment as Fragment).get(clazz as Class<T>)
    }

    abstract fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int

    abstract fun initVariableId(): Int
}