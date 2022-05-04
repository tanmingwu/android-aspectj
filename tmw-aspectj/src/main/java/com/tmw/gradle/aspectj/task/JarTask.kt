package com.tmw.gradle.aspectj.task

import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main

class JarTask : BaseTask() {

    private var injars: String? = null
    private var outjar: String? = null

    fun setInJars(injars: String): JarTask {
        this.injars = injars
        return this
    }

    fun setOutJar(outjar: String): JarTask {
        this.outjar = outjar
        return this
    }

    override fun execute() {
        val args = arrayOf(
            "-showWeaveInfo",
            "-inpath", injars,
            "-aspectpath", aspectPath,
            "-outjar", outjar,
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