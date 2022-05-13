package com.capstone.nft.system.net

import com.capstone.nft.system.utils.Trace


interface HttpResponsable<in RES>
{
    fun onResponse(res: RES)

    fun onFailure(nError: Int, strMsg: String) {
        Trace.debug(">> onFailure() $strMsg[$nError]")
    }
}