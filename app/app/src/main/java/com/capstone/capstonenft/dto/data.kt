package com.capstone.capstonenft.dto

data class regist_request(
    val name: String,
    val pw : String,
    val fcm : String
)
data class regist_response(
    val message: String,
    val name : String,
    val privatekey : String
)
data class login_request(
    val name: String,
    val pw : String,
    val fcm : String
)
data class login_response  (
    val message: String,
    val name : String,
    val privatekey : String
)

data class mint_request(
    val to: String,
    val title : String,
    val desc : String,
    val image : String
)
data class mint_response(
    val message : String,
    val tokenid : String,
    val tokenuri : String
)
data class token_regist_request(
    val from: String,
    val price : Int
)
data class token_regist_response(
    val message : String
)
data class token_buy_request(
    val from: String
)
data class token_buy_response(
    val message : String
)
data class token_burn_request(
    val from: String
)
data class token_burn_response(
    val message : String
)
data class token_list(
    val cursor : String,
    val items : Item
)
