package com.tmw.gradle.aspectj

import com.android.build.api.transform.*
import com.android.build.gradle.AppExtension
import com.google.common.base.Joiner
import com.tmw.gradle.aspectj.base.BaseTransform
import com.tmw.gradle.aspectj.base.getExtension
import com.tmw.gradle.aspectj.task.DirectTask
import com.tmw.gradle.aspectj.task.JarTask
import com.tmw.gradle.aspectj.task.TaskManager
import com.tmw.gradle.aspectj.utils.FilterUtil
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import java.io.File

/**
 * 利用Transform 来处理AspectJ的任务
 * 核心就是利用Transform API来获取所有的class文件和jar文件的路径
 * 将需要处理的class或jar配置到AspectJ的任务并执行
 *
 * @author tanmingwu
 * @since 2020/9/30
 */
class AspectJTransform(project: Project) : BaseTransform() {

    private var mOutput: TransformOutputProvider? = null
    private val classpathFiles: MutableList<File> = ArrayList()
    private val aspectjFiles: MutableList<File> = ArrayList()
    private val extension: AspectJExtension = project.getExtension()
    private val appExtension: AppExtension = project.getExtension()

    private var aspectjPath: String? = null


    override fun onTransform(
        inputs: Collection<TransformInput>, outputProvider: TransformOutputProvider
    ) {
        val start = System.currentTimeMillis()
        mOutput = outputProvider
        inputs.map { it.directoryInputs }.filter { !it.isEmpty() }.forEach {
            it.forEach { directoryInput ->
                if (!directoryInput.file.listFiles().isNullOrEmpty()) {
                    classpathFiles.add(directoryInput.file)
                    aspectjFiles.add(directoryInput.file)
                    val output = outputProvider.getContentLocation(
                        directoryInput.file.absolutePath,
                        directoryInput.contentTypes,
                        directoryInput.scopes,
                        Format.DIRECTORY
                    )

                    val task = DirectTask().setD(output.absolutePath)
                        .setInPath(directoryInput.file.absolutePath)
                    TaskManager.registerTask(task)
                }
            }
        }

        val jarInputs = mutableListOf<JarInput>()
        inputs.map { it.jarInputs }.filter { !it.isEmpty() }.forEach {
            it.forEach { jarInput ->
                jarInputs.add(jarInput)
                if (jarInput.scopes.contains(QualifiedContent.Scope.PROJECT)
                    || jarInput.scopes.contains(QualifiedContent.Scope.SUB_PROJECTS)
                ) {
                    //这里把目录都包含，AOP文件可以写在任意地方
                    aspectjFiles.add(jarInput.file)
                } else {
                    classpathFiles.add(jarInput.file)
                }
            }
        }

        jarInputs.forEach { jarInput ->
            if (extension.isExcludeAllJar) {
                copyJar(jarInput, outputProvider)
                return@forEach //效果同continue
            }

            if (extension.includes.isEmpty() && extension.excludes.isEmpty()) {
                registerJarTask(jarInput, outputProvider)
                return@forEach
            }

            if (extension.includes.isEmpty()) {
                if (FilterUtil.isExclude(jarInput, extension.excludes)) {
                    copyJar(jarInput, outputProvider)
                } else {
                    registerJarTask(jarInput, outputProvider)
                }
            } else {
                if (FilterUtil.isInclude(jarInput, extension.includes)) {
                    registerJarTask(jarInput, outputProvider)
                } else {
                    copyJar(jarInput, outputProvider)
                }
            }
        }
        executeAspectj()
        val end = System.currentTimeMillis()
        println("time = " + (end - start) / 1000.0)
    }

    private fun executeAspectj() {
        val classpath = classpathFiles.joinToString(File.pathSeparator) { it.absolutePath }
        val aspectPath = aspectjFiles.joinToString(File.pathSeparator) { it.absolutePath }
        val bootClasspath = Joiner.on(File.pathSeparator).join(appExtension.bootClasspath)
        TaskManager.execute(bootClasspath, classpath, aspectPath)
        TaskManager.release()
    }

    private fun registerJarTask(jarInput: JarInput, outputProvider: TransformOutputProvider) {
        val task = JarTask().setInJars(jarInput.file.absolutePath)
            .setOutJar(getOutput(jarInput, outputProvider).absolutePath)
            .setAspectPath(aspectjPath)
        TaskManager.registerTask(task)
    }

    private fun copyJar(jarInput: JarInput, outputProvider: TransformOutputProvider) {
        val output = getOutput(jarInput, outputProvider)
        FileUtils.copyFile(jarInput.file, output)
    }

    private fun getOutput(jarInput: JarInput, outputProvider: TransformOutputProvider): File {
        return outputProvider.getContentLocation(
            jarInput.file.absolutePath,
            jarInput.contentTypes,
            jarInput.scopes,
            Format.JAR
        )
    }
}