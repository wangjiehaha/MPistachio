package com.yn.wj.pistachio.http.store

import okhttp3.Cookie
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

class SerializableHttpCookie(@Transient private val cookie: Cookie): Serializable {
    companion object{
        const val serialVersionUID = 6374381323722046732L;
    }

    @Transient
    private var clientCookie: Cookie? = null

    fun getCookie(): Cookie {
        var bestCookie = cookie
        if (clientCookie != null) {
            bestCookie = clientCookie as Cookie
        }
        return bestCookie
    }

    fun writeObject(out: ObjectOutputStream) {
        out.writeObject(cookie.name())
        out.writeObject(cookie.value())
        out.writeLong(cookie.expiresAt())
        out.writeObject(cookie.domain())
        out.writeObject(cookie.path())
        out.writeBoolean(cookie.secure())
        out.writeBoolean(cookie.httpOnly())
        out.writeBoolean(cookie.hostOnly())
        out.writeBoolean(cookie.persistent())
    }

    fun readObject(inner: ObjectInputStream) {
        val name:String = inner.readObject() as String
        val value:String = inner.readObject() as String
        val expiresAt:Long = inner.readLong()
        val domain:String = inner.readObject() as String
        val path:String = inner.readObject() as String
        val secure:Boolean = inner.readBoolean()
        val httpOnly:Boolean = inner.readBoolean()
        var builder: Cookie.Builder = Cookie.Builder()
        builder = builder.name(name).value(value).expiresAt(expiresAt)
        builder = if (httpOnly) builder.hostOnlyDomain(domain) else builder.domain(domain)
        builder = builder.path(path)
        builder = if (secure) builder.secure() else builder
        builder = if (httpOnly) builder.httpOnly() else builder
        clientCookie = builder.build()
    }
}