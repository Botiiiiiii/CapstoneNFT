package com.capstone.capstonenft.system.net


interface HttpDownloadable<in RES>: HttpResponsable<RES>
{
    fun onStart(lFileLength: Long)

    fun onProgress(lDownloaded: Long, lFileLength: Long)

    fun onComplete(lDownloaded: Long)
}