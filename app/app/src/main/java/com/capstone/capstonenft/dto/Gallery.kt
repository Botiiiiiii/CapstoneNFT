package com.capstone.capstonenft.dto

import java.io.Serializable

data class GalleryList(
    val token: ArrayList<Token>
): Serializable

//data class Gallery(
//    val createdAt: Int,
//    val owner: String,
//    val previousOwner: String,
//    val tokenId: String,
//    val tokenUri: String,
//    val transactionHash: String,
//    val updatedAt: Int
//)