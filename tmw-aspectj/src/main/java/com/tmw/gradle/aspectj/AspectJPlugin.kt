package com.tmw.gradle.aspectj

import com.android.build.gradle.*
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.api.LibraryVariant
import com.google.common.base.Joiner
import com.tmw.gradle.aspectj.base.findExtension
import com.tmw.gradle.aspectj.base.getExtension
import com.tmw.gradle.aspectj.base.hasPlugin
import com.tmw.gradle.aspectj.base.implementation
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.DomainObjectSet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.dependencies
import java.io.File

/**
 * 利用Gradle Transform 来为Aspectj执行编织任务
 *
 * @author tanmingwu
 * @since 2020/9/30
 */
class AspectJPlugin : Plugin<Project> {

    companion object {
        private const val CONFIG_NAME = "aspectj"
        private const val ASPECTJ = "org.aspectj:aspectjrt:1.9.6"
        private const val KOTLIN_PATH = "build/tmp/kotlin-classes/debug"
    }

    override fun apply(project: Project) {
        if (!checkAndroidPlugin(project)) {
            return
        }
        println("AspectJPlugin start")
        project.extensions.create(CONFIG_NAME, AspectJExtension::class.java)
        val appExtension = project.findExtension<AppExtension>()
        if (appExtension != null) {
            appExtension.registerTransform(AspectJTransform(project))
        } else {
            val libraryExtension = project.getExtension<LibraryExtension>()
            val libraryVariants: DomainObjectSet<LibraryVariant> = libraryExtension.libraryVariants
            libraryVariants.all { variant: LibraryVariant ->
                println("libraryVariants start")
                val extension = project.extensions.getByType(AspectJExtension::class.java)
                addAspectConfig(variant, libraryExtension, extension.isEnable)
            }
        }

        project.dependencies {
            implementation(ASPECTJ)
            /*implementation(
                project.fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar")))
            )*/
        }
    }

    private fun addAspectConfig(variant: BaseVariant, extension: BaseExtension, enable: Boolean) {
        if (!enable) {
            System.err.println("Aspectj is disabled.")
            return
        }
        println("AspectPlugin ----- variant")
        val provider = variant.javaCompileProvider
        val javaCompile = provider.get()
        javaCompile.doLast {
            runAspectj(javaCompile, extension, false)
            runAspectj(javaCompile, extension, true)
        }
    }

    private fun runAspectj(javaCompile: JavaCompile, extension: BaseExtension, kt: Boolean) {
        val inPath: String = if (kt) {
            getKotlinClassPath(javaCompile.destinationDir.path)
        } else {
            javaCompile.destinationDir.path
        }
        val args = arrayOf(
            "-showWeaveInfo",
            "-1.8",
            "-inpath", inPath,
            "-aspectpath", javaCompile.classpath.asPath,
            "-d", inPath,
            "-classpath", javaCompile.classpath.asPath,
            "-bootclasspath", Joiner.on(File.pathSeparator).join(extension.bootClasspath)
        )
        val messageHandler = MessageHandler(true)
        val main = Main()
        main.run(args, messageHandler)
    }

    private fun getKotlinClassPath(javaPath: String): String {
        val pre = javaPath.split("build".toRegex()).toTypedArray()[0]
        return pre + KOTLIN_PATH
    }

    private fun checkAndroidPlugin(project: Project): Boolean {
        if (project.hasPlugin<AppPlugin>() || project.hasPlugin<LibraryPlugin>()) {
            return true
        }
        println("not a android application project or library project")
        return false
    }
}