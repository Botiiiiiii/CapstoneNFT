package com.capstone.capstonenft.dto

import java.io.Serializable

data class Owner(
    val userinfo: UserInfo,
    val token_list: ArrayList<Token> = arrayListOf(),
    val token_sale: ArrayList<Token> = arrayListOf()
)

data class UserInfo(
    val nickname: String = "",
    val address: String = "",
    var profile_img: String? = ""
)