package com.capstone.capstonenft.dto

data class DialogItem(
    val okBtnName:String = "확인",
    var cancelBtnName:String = "",
    val title:String,
    val content:String
)