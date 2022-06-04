package com.capstone.capstonenft.dto

data class MintRequest(
    val to: String,
    val image: String,
    val title: String,
    val desc: String,
    val uri: String
)
