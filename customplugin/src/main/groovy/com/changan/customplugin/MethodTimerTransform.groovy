package com.changan.customplugin

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.google.common.collect.Sets
import org.gradle.api.Project

class MethodTimerTransform extends Transform {
    Project project

    MethodTimerTransform(Project project) {
        this.project = project
    }

    @Override
    String getName() {
        return "MethodTimerTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return Collections.singleton(QualifiedContent.DefaultContentType.CLASSES)
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return Sets.immutableEnumSet(
                QualifiedContent.Scope.PROJECT,
                QualifiedContent.Scope.SUB_PROJECTS,
                QualifiedContent.Scope.EXTERNAL_LIBRARIES
        )
    }

    @Override
    boolean isIncremental() {
        return true
    }

    @Override
    void transform(TransformInvocation invocation) throws TransformException, InterruptedException, IOException {
        project.logger.error("Begin HAHHHHHHAAAAAA")
        long startTime = System.currentTimeMillis()
        DirectoryInput dir
        HashSet<JarInput> jar = new HashSet()
        invocation.inputs.each { input ->
            input.directoryInputs.each {//工程下的目录
                if (dir == null && it.file != null)
                    dir = it
            }
            input.jarInputs.each {//jar和aar中的jar
                jar.add(it)
            }
        }
        project.logger.quiet("------costtime-----" + (System.currentTimeMillis() - startTime) + "ms")
    }
}