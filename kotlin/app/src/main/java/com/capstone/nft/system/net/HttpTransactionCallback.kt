package com.capstone.nft.system.net

import com.capstone.nft.system.net.HttpRequestable

interface HttpTransactionCallback
{
    fun transactionBegin(protocol: HttpRequestable)

    fun transactionEnd(protocol: HttpRequestable)
}