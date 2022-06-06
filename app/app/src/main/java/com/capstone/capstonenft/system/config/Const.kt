package com.capstone.capstonenft.system.config

import android.os.Environment
import java.util.*

object Const
{
    interface NETWORK {
        enum class APP(private val domain: String, private val path: String) {
            SERVER("http://3.36.62.18:3000/", "");

            fun getDomain() = domain

            fun getPath() = path

            fun getUrl() = domain + path
        }
    }
}