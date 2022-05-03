package com.tmw.gradle.aspectj.base

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.google.common.collect.ImmutableSet
import java.io.IOException
import java.util.regex.Pattern

/**
 * @author tanmingwu
 * @since 2020/9/30
 */
abstract class BaseTransform : Transform() {

    private val pattern = Pattern.compile("lib/[^/]+/[^/]+\\.so")

    override fun getName(): String {
        return javaClass.simpleName
    }

    override fun getInputTypes(): Set<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope>? {
        return ImmutableSet.of(
            QualifiedContent.Scope.PROJECT,
            QualifiedContent.Scope.SUB_PROJECTS,
            QualifiedContent.Scope.EXTERNAL_LIBRARIES
        )
    }

    override fun isIncremental(): Boolean {
        return false
    }

    @Throws(IOException::class)
    override fun transform(transformInvocation: TransformInvocation) {
        doTransform(
            transformInvocation.inputs,
            transformInvocation.outputProvider,
            transformInvocation.isIncremental
        )
    }

    @Throws(IOException::class)
    private fun doTransform(
        inputs: Collection<TransformInput>,
        outputProvider: TransformOutputProvider,
        isIncremental: Boolean
    ) {
        if (!isIncremental) {
            outputProvider.deleteAll()
        }
        onTransform(inputs, outputProvider)
    }

    protected abstract fun onTransform(
        inputs: Collection<TransformInput>, outputProvider: TransformOutputProvider
    )
}