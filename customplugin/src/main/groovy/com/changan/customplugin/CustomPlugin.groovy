package com.changan.customplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class CustomPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.task('myTask').doLast {
            println "Hi this is micky's plugin"
        }

        project.android.registerTransform(new MethodTimerTransform(project))
    }
}