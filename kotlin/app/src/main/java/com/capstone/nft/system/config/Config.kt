package com.capstone.nft.system.config

class Config {
    companion object{
        const val SUPPORT_DEBUG = false
        fun getAppServer(): Const.NETWORK.APP {
            return Const.NETWORK.APP.SERVER
        }
    }
}