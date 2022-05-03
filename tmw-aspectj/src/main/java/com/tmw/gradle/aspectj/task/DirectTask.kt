package com.tmw.gradle.aspectj.task

import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main

class DirectTask : BaseTask() {

    private var d: String? = null
    private var inPath: String? = null

    fun setInPath(inPath: String): DirectTask {
        this.inPath = inPath
        return this
    }

    fun setD(dir: String): DirectTask {
        this.d = dir
        return this
    }

    override fun execute() {
        val args = arrayOf(
            "-showWeaveInfo",
            "-inpath", inPath,
            "-aspectpath", aspectPath,
            "-d", d,
            "-classpath", classPath,
            "-bootclasspath", bootClasspath
        )
        val handler = MessageHandler()
        Main().run(args, handler)
        handler.getMessages(null, true).forEach {
            if (it.message.startsWith("Join point")) {
                println(it.message)
            }
            when (it.kind) {
                IMessage.ABORT, IMessage.ERROR, IMessage.FAIL -> {
                    System.err.println(it.message)
                }
            }
        }
    }
}