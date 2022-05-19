package com.capstone.capstonenft.system.utils

import android.util.Log

class Trace {
    companion object{
        fun debug(msg: String) {
            var strFileName = Thread.currentThread().stackTrace[4].fileName
            val nLineNumber = Thread.currentThread().stackTrace[4].lineNumber
            val strLog = String.format("%-20s %5d  %s\n", strFileName, nLineNumber, msg);
            Log.d(Thread.currentThread().name, strLog)
        }
    }
}