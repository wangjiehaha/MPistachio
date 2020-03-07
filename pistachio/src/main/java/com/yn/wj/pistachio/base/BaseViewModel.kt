package com.yn.wj.pistachio.base

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.os.Bundle
import com.yn.wj.pistachio.bus.SingLiveEvent
import com.trello.rxlifecycle2.LifecycleProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import java.lang.ref.WeakReference

/**
 *
 *
 * @date Create time：2020/2/1
 */
open class BaseViewModel<M : BaseModel>(app: Application, protected var model:M? = null) : AndroidViewModel(app),
        IBaseViewModel, Consumer<Disposable> {

    private lateinit var mWeakLifecycleProvider: WeakReference<LifecycleProvider<*>>
    private var mCompositeDisposable: CompositeDisposable = CompositeDisposable()
    var startActivityEvent: SingLiveEvent<HashMap<String, Any>>? = null
        get() {
            if (field == null){
                field = SingLiveEvent()
            }
            return field
        }
        private set
    var startContainerActivityEvent: SingLiveEvent<HashMap<String, Any>>? = null
        get() {
            if (field == null) {
                field = SingLiveEvent()
            }
            return field
        }
        private set
    var finishEvent: SingLiveEvent<Unit>? = null
        get() {
            if (field == null) {
                field = SingLiveEvent()
            }
            return field
        }
        private set

    private fun addSubscribe(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    fun injectLifecycleProvider(lifecycleProvider: LifecycleProvider<*>) {
        mWeakLifecycleProvider = WeakReference(lifecycleProvider)
    }

    fun getLifecycleProvider(): LifecycleProvider<*>? {
        return mWeakLifecycleProvider.get()
    }

    fun startActivity(clazz: Class<*>, bundle: Bundle? = null) {
        val param: HashMap<String, Any> = HashMap()
        param[ParameterField.CLASS] = clazz
        if (bundle != null) {
            param[ParameterField.BUNDLE] = bundle
        }
        startActivityEvent?.postValue(param)
    }

    fun startContainerActivity(canonicalName: String, bundle: Bundle?) {
        val param: HashMap<String, Any> = HashMap()
        param[ParameterField.CANONICAL_NAME] = canonicalName
        if (bundle != null) {
            param[ParameterField.BUNDLE] = bundle
        }
        startContainerActivityEvent?.postValue(param)
    }

    fun finish() {
        finishEvent?.call()
    }

    override fun onCleared() {
        super.onCleared()
        model?.onCleared()
        mCompositeDisposable.clear()
    }

    override fun accept(t: Disposable) {
        addSubscribe(t)
    }

    override fun onAny(owner: LifecycleOwner, event: Lifecycle.Event) {
    }

    override fun onCreate() {
    }

    override fun onStart() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    override fun onStop() {
    }

    override fun onDestroy() {
    }

    override fun registerRxBus() {
    }

    override fun removeRxBus() {
    }

    object ParameterField {
        const val CLASS = "CLASS"
        const val CANONICAL_NAME = "CANONICAL_NAME"
        const val BUNDLE = "BUNDLE"
    }
}