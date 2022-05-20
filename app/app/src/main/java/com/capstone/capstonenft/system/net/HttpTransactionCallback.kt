package com.capstone.capstonenft.system.net

import com.capstone.capstonenft.system.net.HttpRequestable

interface HttpTransactionCallback
{
    fun transactionBegin(protocol: HttpRequestable)

    fun transactionEnd(protocol: HttpRequestable)
}