package com.tmw.gradle.aspectj

import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main

/**
 * @author tanmingwu
 * @since 2021/12/27
 */
class AspectTask {
    private var inPath: String? = null
    private var classPath: String? = null
    private var aspectPath: String? = null
    private var d: String? = null
    private var outjar: String? = null
    private var bootClasspath: String? = null

    fun setInPath(inPath: String?): AspectTask {
        this.inPath = inPath
        return this
    }

    fun setClassPath(classPath: String?): AspectTask {
        this.classPath = classPath
        return this
    }

    fun setAspectPath(aspectPath: String?): AspectTask {
        this.aspectPath = aspectPath
        return this
    }

    fun setD(d: String?): AspectTask {
        this.d = d
        return this
    }

    fun setBootClasspath(bootClasspath: String?): AspectTask {
        this.bootClasspath = bootClasspath
        return this
    }

    fun setOutjar(outjar: String?): AspectTask {
        this.outjar = outjar
        return this
    }

    fun execute() {
        val args = arrayOf(
            "-showWeaveInfo",
//            "-noWarn",
            "-inpath", inPath,
            "-aspectpath", aspectPath,
//            "-d", d,
            "-outjar", outjar,
            "-classpath", classPath,
            "-bootclasspath", bootClasspath
        )
        val handler = MessageHandler(true)
        //        new Main().run(args, handler);
        Main().runMain(args, false)
    }
}