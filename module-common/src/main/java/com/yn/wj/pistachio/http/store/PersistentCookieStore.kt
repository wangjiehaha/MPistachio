package com.yn.wj.pistachio.http.store

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.text.TextUtils
import okhttp3.Cookie
import okhttp3.HttpUrl
import java.io.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.experimental.and

class PersistentCookieStore(context: Context): CookieStore {

    companion object{
        const val COOKIE_PREFS = "persistent_cookie"
        const val COOKIE_NAME_PREFIX = "cookie_"

        fun isCookieExpired(cookie: Cookie): Boolean {
            return cookie.expiresAt() < System.currentTimeMillis()
        }
    }

    private val cookiePrefs: SharedPreferences
    private val cookies: HashMap<String, ConcurrentHashMap<String, Cookie>>

    init {

        cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, Context.MODE_PRIVATE)
        cookies = HashMap()

        val prefsMap: Map<String, *> = cookiePrefs.all
        for (entry: Map.Entry<String, *> in prefsMap) {
            if ((entry.value != null && !entry.key.startsWith(COOKIE_NAME_PREFIX))) {
                val cookieNames = TextUtils.split(entry.value as String, ",")
                for (name in cookieNames) {
                    val encodedCookie = cookiePrefs.getString(COOKIE_NAME_PREFIX + name, null)
                    if (encodedCookie != null) {
                        val decodeCookie = decodeCookie(encodedCookie)
                        if (decodeCookie != null) {
                            if (!cookies.containsKey(entry.key)) {
                                cookies[entry.key] = ConcurrentHashMap()
                            }
                            cookies[entry.key]?.put(name, decodeCookie)
                        }
                    }
                }
            }
        }
    }

    private fun getCookieToken(cookie: Cookie): String {
        return cookie.name() + "@" + cookie.domain()
    }

    private fun decodeCookie(cookieString: String): Cookie? {
        val bytes = hexStringToByteArray(cookieString)
        val byteArrayInputStream = ByteArrayInputStream(bytes)
        var cookie: Cookie? = null
        try {
            val objectInputStream = ObjectInputStream(byteArrayInputStream)
            cookie = (objectInputStream.readObject() as SerializableHttpCookie).getCookie()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return cookie
    }

    override fun saveCookie(url: HttpUrl, cookie: ArrayList<Cookie>) {
        if (!cookies.containsKey(url.host())) {
            cookies[url.host()] = ConcurrentHashMap()
        }
        for (c in cookie) {
            if (isCookieExpired(c)) {
                removeCookie(url, c)
            } else {
                saveCookie(url, c, getCookieToken(c))
            }
        }
    }

    private fun saveCookie(url: HttpUrl, cookie: Cookie, name: String) {
        cookies[url.host()]?.put(name, cookie)
        val prefsWrite: SharedPreferences.Editor = cookiePrefs.edit()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val tokens: Set<*> = (cookies[url.host()])!!.keys
            prefsWrite.putString(url.host(), TextUtils.join(",", tokens))
            prefsWrite.putString(COOKIE_NAME_PREFIX + name, encodeCookie(SerializableHttpCookie(cookie)))
        }
        prefsWrite.apply()
    }

    private fun encodeCookie(cookie: SerializableHttpCookie): String? {
        val os: ByteArrayOutputStream = ByteArrayOutputStream()
        try {
            val outputStream = ObjectOutputStream(os)
            outputStream.writeObject(cookie)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return byteArrayToHexString(os.toByteArray())
    }

    override fun saveCookie(url: HttpUrl, cookie: Cookie) {
        if (!cookies.containsKey(url.host())) {
            cookies[url.host()] = ConcurrentHashMap()
        }
        if (isCookieExpired(cookie)) {
            removeCookie(url, cookie)
        } else {
            saveCookie(url, cookie, getCookieToken(cookie))
        }
    }

    override fun loadCookie(url: HttpUrl): ArrayList<Cookie> {
        val ret = arrayListOf<Cookie>()
        if (cookies.containsKey(url.host())) {
            val urlCookies = cookies[url.host()]?.values as Collection<Cookie>
            for (cookie in urlCookies) {
                if (isCookieExpired(cookie)) {
                    removeCookie(url, cookie)
                } else {
                    ret.add(cookie)
                }
            }
        }
        return ret
    }

    override fun getAllCookie(): ArrayList<Cookie> {
        val ret = ArrayList<Cookie>()
        for (key in cookies.keys) {
            ret.addAll(cookies[key]!!.values)
        }
        return ret
    }

    override fun getCookie(url: HttpUrl): ArrayList<Cookie> {
        val ret = ArrayList<Cookie>()
        val mapCookie = cookies[url.host()]
        if (mapCookie != null) {
            ret.addAll(mapCookie.values)
        }
        return ret
    }

    override fun removeCookie(url: HttpUrl, cookie: Cookie): Boolean {
        val name = getCookieToken(cookie)
        val boolean = if (cookies[url.host()] != null) cookies[url.host()]?.contains(name)!! else false
        return if (cookies.containsKey(url.host()) && boolean) {
            cookies[url.host()]?.remove(name)
            val prefsWriter = cookiePrefs.edit()
            if (cookiePrefs.contains(COOKIE_NAME_PREFIX + name)) {
                prefsWriter.remove(COOKIE_NAME_PREFIX + name)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val tokens = (cookies[url.host()])!!.keys
                prefsWriter.putString(url.host(), TextUtils.join(",", tokens))
            }
            prefsWriter.apply()
            true
        } else {
            false
        }
    }

    override fun removeCookie(url: HttpUrl): Boolean {
        return if (cookies.containsKey(url.host())) { //文件移除
            val cookieNames: Set<String> = cookies[url.host()]!!.keys
            val prefsWriter = cookiePrefs.edit()
            for (cookieName in cookieNames) {
                if (cookiePrefs.contains(COOKIE_NAME_PREFIX + cookieName)) {
                    prefsWriter.remove(COOKIE_NAME_PREFIX + cookieName)
                }
            }
            prefsWriter.remove(url.host()).apply()
            cookies.remove(url.host())
            true
        } else {
            false
        }
    }

    override fun removeAllCookie() {
        val prefsWriter = cookiePrefs.edit()
        prefsWriter.clear().apply()
        cookies.clear()
    }

    private fun byteArrayToHexString(bytes: ByteArray): String {
        val sb = StringBuilder(bytes.size * 2)
        for (element in bytes) {
            val v = element and 0xff.toByte()
            if (v < 16) {
                sb.append('0')
            }
            sb.append(Integer.toHexString(v.toInt()))
        }
        return sb.toString().toUpperCase(Locale.US)
    }

    private fun hexStringToByteArray(hexString: String): ByteArray {
        val len = hexString.length
        val data = ByteArray(len /2)
        for (index in 0 until len step 2) {
            data[index / 2] = Character.digit(hexString[index], 16).toByte()
        }
        return data
    }
}