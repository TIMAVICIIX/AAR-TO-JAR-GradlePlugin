package org.robologic.tools

interface MessageSender {

    companion object {

        const val TYPE_LIFECYCLE = 0
        const val TYPE_INFO = 1
        const val TYPE_ERROR = 2
        const val TYPE_WARN = 3
        const val TYPE_DEBUG = 4

    }

    fun sendMessageOrImplantString(sendType: Int, title: String, stringList: List<String>?)

    fun sendMessageWithAdditionString(sendType: Int, title: String, addString: String)

}