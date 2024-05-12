package org.robologic.download

import org.gradle.api.Project
import org.robologic.crosstest.Logger
import org.robologic.tools.LanguageResource
import org.robologic.tools.MessageSender
import org.robologic.tools.RoboTools
import java.io.File
import java.io.IOException

class CreateConfigures(private val messageSender: MessageSender, private var fileCreateTarget: Project?) {

    private val createContent = """
        <dependencies>
        
        <!--Language Choose: Chinese,English
        Default Language is English-->
        <localLanguage>Chinese</localLanguage>
        
        <!--注意，过高版本的构件可能不会出现在镜像库中；届时请配置为源构件库。-->
        <!--Note that artifacts that are too high version may not appear in the registry; At that time, please configure it as the source component library.-->
        <!--Choose the mirror URL:True or False,default is False-->
        <mirrorDownload>False</mirrorDownload>
        
        <!--
        
            <dependency name = "//xxxyy">
               //url:https://xxx.yyy.com/xxx.zip/aar
            </dependency>
        
        -->
        
        </dependencies>
        
    """.trimIndent()

    fun createConfigureFile(): File? {

        val targetFile = fileCreateTarget!!.file("aar.dependencies.xml")

        //DEBUG
        //messageSender.sendMessageOrImplantString(MessageSender.TYPE_LIFECYCLE, targetFile!!.path, null)

        try {
            if (targetFile.exists()) {
                confirmLanguageResource(targetFile)
                messageSender.sendMessageOrImplantString(MessageSender.TYPE_LIFECYCLE, "existConfigure", null)
                Logger.debug("aar配置文件加载成功!")
                return targetFile
            } else {
                targetFile.apply {
                    createNewFile()
                    writeText(createContent)
                    confirmLanguageResource(targetFile)
                    messageSender.sendMessageOrImplantString(MessageSender.TYPE_LIFECYCLE, "createConfigure", null)
                    Logger.debug("aar配置文件创建成功!")
                    return targetFile
                }
            }


        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null

    }

    private fun confirmLanguageResource(configureFile: File) {

        val resultList = RoboTools.xmlAnalyzer(configureFile, RoboTools.FIND_LANGUAGE)
        LanguageResource.checkLanguage()

        if (resultList[0].second == "English" || resultList[0].second == "Chinese") {
            Logger.debug("语言模块配置成功!")
        } else {
            messageSender.sendMessageOrImplantString(MessageSender.TYPE_ERROR, "LanguageError", null)
        }

    }

}