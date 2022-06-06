package com.capstone.capstonenft.dto

import java.io.Serializable

data class LoginResponse(
    val message: String = "",
    val name: String = "",
    val privatekey: String = "",
    val address: String = "",
    var profile_img: String? = "",
    val token_list:ArrayList<Token> = arrayListOf(),
    val token_sale:ArrayList<Token> = arrayListOf()
)

data class Token(
    val tokenId: Int,
    val title: String,
    val description: String,
    val imageSrc: String,
    var owner: String,
    val creator: String,
    val price: Float?
): Serializable

data class TokenResponse(val token:Token)

