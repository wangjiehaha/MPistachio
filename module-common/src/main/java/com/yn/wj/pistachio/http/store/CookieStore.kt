package com.yn.wj.pistachio.http.store

import okhttp3.Cookie
import okhttp3.HttpUrl

interface CookieStore {

    fun saveCookie(url: HttpUrl, cookie: ArrayList<Cookie>)

    fun saveCookie(url: HttpUrl, cookie: Cookie)

    fun loadCookie(url: HttpUrl): ArrayList<Cookie>

    fun getAllCookie(): ArrayList<Cookie>

    fun getCookie(url: HttpUrl): ArrayList<Cookie>

    fun removeCookie(url: HttpUrl, cookie: Cookie): Boolean

    fun removeCookie(url: HttpUrl): Boolean

    fun removeAllCookie()
}