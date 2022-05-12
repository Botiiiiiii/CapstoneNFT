package com.capstone.nft.system.config

import android.os.Environment
import java.util.*

object Const
{
    interface NETWORK {
        enum class APP(private val domain: String, private val path: String) {
            SERVER("http://13.209.17.88:3000/", "");

            fun getDomain() = domain

            fun getPath() = path

            fun getUrl() = domain + path
        }
    }
}