package org.robologic.tools

class LanguageResource {

    companion object {

        var languageType = "English"

        private var strings: List<LanguageData> = mutableListOf<LanguageData>().apply {

            add(LanguageData("welcome", "RoboLogic-A-J-tool is started", "RoboLogic AR 转储工具已启动"))

            add(
                LanguageData(
                    "Build Stop", """
                |
                |Due to this Error,Gradle auto Building is stop; 
                |Please check 'aar.dependencies.xml' file by following steps:
                |1. Check if the file exists;
                |2. Check that the syntax in the file is correct;
                |3. check for other build errors;
                |4. read some of the previous warnings;
                |5. Rebuild the project or close the roboLogic plugin;
                |6. If you can't solve the problem, please send feedback to email:timaviciix@outlook.com,
                |Or visit the project address:https://github.com/TIMAVICIIX/RoboLogic_A-J-tool.
                |Release Date: 2024.5.12 Version:0.8.0 Beta
            """.trimMargin(), """
                |
                |由于以上错误,Gradle自动构建已停止;
                |请按以下步骤检查‘arr.dependencies.xml’文件:
                |1.检查该文件是否存在;
                |2.检查该文件中语法是否正确;
                |3.检查是否出现其他构建错误;
                |4.阅读之前的一些警告;
                |5.重新构建该项目或者关闭该roboLogic插件;
                |6.尝试关闭镜像下载配置;
                |7.无法解决问题请反馈至邮箱:timaviciix@outlook.com,
                |或者访问项目地址:https://github.com/TIMAVICIIX/RoboLogic_A-J-tool.
                |发行日期: 2024.5.12 版本号:0.8.0 Beta版
            """.trimMargin()
                )
            )
            add(
                LanguageData(
                    "LanguageError",
                    "Language Configuration Error,Please Check Configures File",
                    "语言配置错误,请检查配置文件语法问题以及语言种类"
                )
            )
            add(
                LanguageData(
                    "First use",
                    "Initializing the AAR to JAR plugin successfully! Please follow the prompts to fill in the download configuration in the \"aar.dependencies.xml\" file in the root directory of the project, and rebuild the project!\nIf the configuration file does not appear in the project root, right-click on the project root and left-click on \"Reload File from Disk\"!",
                    "初始化AAR to JAR插件成功! 请按照提示在项目根目录下的\"aar.dependencies.xml\"文件中填写下载配置，并重新进行项目构建!\n如果配置文件没有出现在项目根目录内，请右击项目根目录并左击\"重新从磁盘加载文件\"!"
                )
            )
            add(
                LanguageData("createConfigure", "Create AAR Configure File Successful!", "成功创建aar配置文件!")
            )
            add(LanguageData("existConfigure", "AAR Configure File load Successful!", "aar配置文件读取成功!"))
            add(LanguageData("Download start", "Download start:?", "下载开始:?"))
            add(
                LanguageData(
                    "Download info",
                    "\rName:? Size:? Schedule:?% Speed:?/s.",
                    "\r名称:? 大小:? 进度:?% 速度:?/s."
                )
            )
            add(LanguageData("Download complete", "? Download Completed!", "? 任务下载完成!"))
            add(
                LanguageData(
                    "No Jars",
                    "No JAR package is exported from the AAR package!",
                    "没有JAR包从该AAR包内导出!"
                )
            )
            add(LanguageData("Have Jars", "Export ? jars packages from the archive.", "从该压缩包内导出?个JAR包."))
            add(
                LanguageData(
                    "Type warn",
                    "The file cannot be extracted as a zip package: ? ,continue plugin build.",
                    "该文件不能作为压缩包解压: ? ,继续运行插件."
                )
            )
            add(LanguageData("Has been downloaded","The file has been downloaded!","该文件已被下载!"))
        }

        private var filtratedLanguageList: MutableList<Pair<String, String?>> = mutableListOf()

        fun getString(title: String): String {

            for (string in filtratedLanguageList) {
                if (string.first == title) {
                    return string.second ?: string.first
                }

            }

            return title
        }

        fun getImplantString(title: String, implantStrings: List<String>): String {

            var implantResultString = getString(title)

            implantStrings.forEach { replace ->
                implantResultString = implantResultString.replaceFirst("?", replace)
            }

            return implantResultString
        }

        fun checkLanguage(): Boolean {

            val result = languageType == "English" || languageType == "Chinese"

            if (result) {

                filtratedLanguageList = when (languageType) {

                    "English" -> strings.map { Pair(it.stringTitle, it.stringEn) }.toMutableList()
                    "Chinese" -> strings.map { Pair(it.stringTitle, it.stringCh) }.toMutableList()
                    else -> mutableListOf()

                }
            }

            return result

        }

    }

}