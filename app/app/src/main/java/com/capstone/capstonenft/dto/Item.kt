package com.capstone.capstonenft.dto

data class Item (
    val createdAt : String,
    val owner : String,
    val previousOwner : String,
    val tokenId : String,
    val tokenUri : String,
    val transactionHash : String,
    val updatedAt : String
    )