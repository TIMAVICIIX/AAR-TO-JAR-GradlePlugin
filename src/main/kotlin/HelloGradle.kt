package org.robologic

import org.gradle.api.Plugin
import org.gradle.api.Project

class HelloGradle : Plugin<Project> {

    override fun apply(target: Project) {

        val tasks = target.tasks

        tasks.create("MyTask").apply {
            doLast {
                println("This is My First Gradle Plugin!")
            }
        }

        target.logger.lifecycle("MyPlugin applied!")

    }

}