package com.capstone.capstonenft.dto

data class LoginRequest(
    val name: String,
    val pw: String,
    val fcm: String
)