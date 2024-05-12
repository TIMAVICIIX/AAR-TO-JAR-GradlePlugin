package org.robologic.crosstest

import org.robologic.download.Boot
import org.slf4j.LoggerFactory

fun main() {

      Boot().doBoot()

}

class Test{
    fun printLog(){
        Logger.setLoggerLevel(Logger.LOG_INFO)
        Logger.error("error")
        Logger.warn("warn")
        Logger.info("info")
        Logger.debug("debug")

    }

}

