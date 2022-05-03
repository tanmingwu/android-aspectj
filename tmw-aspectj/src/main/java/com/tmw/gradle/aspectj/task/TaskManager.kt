package com.tmw.gradle.aspectj.task

object TaskManager {

    private val taskList = mutableListOf<BaseTask>()

    fun registerTask(vararg tasks: BaseTask) {
        taskList.addAll(tasks)
    }

    fun execute(bootClassPath: String, classPath: String, aspectPath: String) {
        taskList.forEach {
            it.setBootClasspath(bootClassPath)
                .setClassPath(classPath)
                .setAspectPath(aspectPath)
                .execute()
        }
    }

    fun release() {
        taskList.clear()
    }
}