package com.capstone.capstonenft.dto

import java.io.File
import java.io.Serializable

data class Upload (val message:String,
                   val score: Float,
                   val sim_token_id: String,
                   val imageUrl: String):Serializable