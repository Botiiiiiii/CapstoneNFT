package com.capstone.capstonenft.dto

import java.io.File

data class RequestUpload(
    val image: String,
    val title:String,
    val desc:String,
    val creator:String
)

data class ResponseUpload(
    val message:Boolean,
    val image:String,
    val uri:String
)