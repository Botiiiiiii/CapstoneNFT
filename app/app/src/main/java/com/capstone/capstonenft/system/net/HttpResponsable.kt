package com.capstone.capstonenft.system.net

import com.capstone.capstonenft.system.utils.Trace


interface HttpResponsable<in RES>
{
    fun onResponse(res: RES)

    fun onFailure(nError: Int, strMsg: String) {
        Trace.debug(">> onFailure() $strMsg[$nError]")
    }
}