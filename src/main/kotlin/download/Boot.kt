package org.robologic.download

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.robologic.crosstest.Logger
import org.robologic.releasediv.ReleaseFiles
import org.robologic.tools.LanguageResource
import org.robologic.tools.MessageSender
import org.robologic.tools.RoboTools

class Boot : Plugin<Project>, MessageSender {

    private lateinit var projectTarget:Project

    private lateinit var createConfigures: CreateConfigures
    private lateinit var httpGetFiles: HttpGetFiles


    fun doBoot() {
        /**
         *
         * 1.First Step:Get status of configures file(aar.dependencies.kts).
         *      If file don't exist,create it and write configures.
         *
         * 2.Second Step:Read configures file,get dependencies status,download them one by one,return them finally.
         *
         * 3.Third Step:Get downloaded list,release them and put child files in current files.
         *
         * Tips:All the Task have to do First.And Build this Project.
         *
         * */

        Logger.setLoggerLevel(Logger.LOG_WARN)

        //Risk of memory leaks
        RoboTools.targetProject = projectTarget
        createConfigures = CreateConfigures(this,projectTarget)
        val configuresFile = createConfigures.createConfigureFile()

        projectTarget.logger.lifecycle(LanguageResource.getString("welcome"))

        httpGetFiles = HttpGetFiles(this)

        configuresFile?.let { it1 ->
            val files = httpGetFiles.startDownloadTaskQueue(it1)

            if (files.isEmpty()) {
                Logger.debug(LanguageResource.getString("First use"))
                projectTarget.logger.lifecycle(LanguageResource.getString("First use"))

            } else {

                for (file in files) {
                    println("Name:${file.first},SavePath:${file.second}")
                }

                val releaseFiles = ReleaseFiles(this,files)
                releaseFiles.releaseAndInstallJars()

                httpGetFiles.cancelTmpDir()
            }
        }


    }

    private fun installExternalLib() {

        val ajLib = RoboTools.createTmpDir("AARToJAR-Libraries")
//        @Suppress("DEPRECATION") val exLib = File(projectTarget.buildDir, "externalLibs")

        projectTarget.tasks.register("Install JAR") {

            ajLib.listFiles()?.forEach { file ->
//                Files.move(file.toPath(), Path(exLib.path + file.name))
                projectTarget.files(file.absolutePath)
                    .let { it1 -> projectTarget.dependencies.add("implementation", it1) }

            }

        }

    }

    override fun apply(target: Project) {
//        target.logger.lifecycle("roboLogic aar to jar task:")
        projectTarget = target
        doBoot()
        installExternalLib()
    }

    override fun sendMessageOrImplantString(sendType: Int, title: String, stringList: List<String>?) {


        val message = if (stringList != null) {
            LanguageResource.getImplantString(title, stringList)
        } else {
            LanguageResource.getString(title)
        }

        when (sendType) {

            MessageSender.TYPE_LIFECYCLE -> {
                projectTarget.logger.lifecycle(message)
            }

            MessageSender.TYPE_INFO -> {
                projectTarget.logger.info(message)
            }

            MessageSender.TYPE_WARN -> {
                projectTarget.logger.warn(message)
            }

            MessageSender.TYPE_ERROR -> {
                projectTarget.logger.error(message)
                throw GradleException(LanguageResource.getString("Build Stop"))
            }


        }


    }

    override fun sendMessageWithAdditionString(sendType: Int, title: String, addString: String) {

        val message = LanguageResource.getString(title)



        when (sendType) {

            MessageSender.TYPE_LIFECYCLE -> {
                projectTarget.logger.lifecycle("$message $addString")
            }

            MessageSender.TYPE_INFO -> {
                projectTarget.logger.info("$message $addString")
            }

            MessageSender.TYPE_WARN -> {
                projectTarget.logger.warn("$message $addString")
            }

            MessageSender.TYPE_ERROR -> {
                projectTarget.logger.error("$message $addString")
                throw GradleException(LanguageResource.getString("Build Stop"))
            }

        }
    }

}


