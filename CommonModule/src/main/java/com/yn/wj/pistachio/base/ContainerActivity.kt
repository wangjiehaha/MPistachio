package com.yn.wj.pistachio.base

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.WindowManager
import com.yn.wj.pistachio.R
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import java.lang.ref.WeakReference

/**
 *
 *
 * @date Create time：2020/2/2
 */
@Suppress("CAST_NEVER_SUCCEEDS")
class ContainerActivity: RxAppCompatActivity() {
    protected lateinit var fragmentRef: WeakReference<Fragment?>

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        val fm: FragmentManager = supportFragmentManager
        var fragment: Fragment? = null
        if (savedInstanceState != null) {
            fragment = fm.getFragment(savedInstanceState, FRAGMENT_TAG)
        }
        if (fragment != null) {
            fragment = initFromFragment(intent)
            val trans = fm.beginTransaction()
            trans.replace(R.id.content, fragment)
            trans.commitAllowingStateLoss()
        }
        fragmentRef = WeakReference(fragment)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        fragmentRef.get()?.let { outState?.let { it1 -> supportFragmentManager.putFragment(it1, FRAGMENT_TAG, it) } }
    }

    private fun initFromFragment(data: Intent?): Fragment {
        if (data != null) {
            try {
                val fragmentName = data.getStringExtra(KEY_FRAGMENT)
                if (fragmentName == null || "" == fragmentName) {
                    throw IllegalArgumentException("can not find page fragmentName")
                }
                val fragmentClass: Class<*> = Class.forName(fragmentName)
                val fragment: Fragment = fragmentClass.newInstance() as Fragment
                val args = data.getBundleExtra(KEY_BUNDLE)
                if (args != null) {
                    fragment.arguments = args
                }
                return fragment
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
            throw RuntimeException("fragment initialization failed!")
        } else {
            throw IllegalArgumentException("can not find page fragmentName")
        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.content)
        if (fragment is BaseFragment<*, *>) {
            if (!(fragment as BaseFragment<*, *>).isBackPress()) {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }
}
const val FRAGMENT_TAG = "content_fragment_tag"
const val KEY_FRAGMENT = "fragment"
const val KEY_BUNDLE = "bundle"