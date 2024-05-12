package org.robologic.crosstest

import java.time.Instant

object Logger {

    const val LOG_DEBUG = 0
    const val LOG_INFO = 1
    const val LOG_ERROR = 2
    const val LOG_WARN = 3

    private var LogType = LOG_DEBUG

    fun debug(info:String){
        loggerPrintBus(LOG_DEBUG,"${getCurrentTime()} [DEBUG]: $info")
    }

    fun info(info:String){
        loggerPrintBus(LOG_INFO,"${getCurrentTime()} [INFO]: $info")
    }

    fun error(info:String){
        loggerPrintBus(LOG_ERROR,"${getCurrentTime()} [ERROR]: $info")
    }

    fun warn(info:String){
        loggerPrintBus(LOG_WARN,"${getCurrentTime()} [WARN]: $info")
    }

    fun setLoggerLevel(level:Int){
        LogType = level
    }



    private fun getCurrentTime():String{
        return Instant.now().toString()
    }

    private fun loggerPrintBus(logLevel:Int,info:String){

        if(logLevel>= LogType){
            println(info)
        }

    }

}