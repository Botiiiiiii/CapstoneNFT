package com.capstone.capstonenft.dto

data class GalleryList(
    val cursor: String,
    val items: ArrayList<Gallery>

)

data class Gallery(
    val createdAt: Int,
    val owner: String,
    val previousOwner: String,
    val tokenId: String,
    val tokenUri: String,
    val transactionHash: String,
    val updatedAt: Int
)