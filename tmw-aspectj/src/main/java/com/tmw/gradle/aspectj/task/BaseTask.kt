package com.tmw.gradle.aspectj.task

abstract class BaseTask {

    protected var classPath: String? = null
    protected var aspectPath: String? = null
    protected var bootClasspath: String? = null

    fun setClassPath(classPath: String?): BaseTask {
        this.classPath = classPath
        return this
    }

    fun setAspectPath(aspectPath: String?): BaseTask {
        this.aspectPath = aspectPath
        return this
    }

    fun setBootClasspath(bootClasspath: String?): BaseTask {
        this.bootClasspath = bootClasspath
        return this
    }

    abstract fun execute()
}