package org.robologic.tools

import org.gradle.api.Project
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.File
import java.nio.file.Paths
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory

class RoboTools {

    companion object {

        const val FIND_URL = 0
        const val FIND_LANGUAGE = 1
        const val FIND_MIRROR = 2

        var targetProject: Project? = null

        private fun generateRandomString(): String {
            val characters = ('a'..'z') + ('A'..'Z')
            val random = Random()
            return (1..6)
                .map { characters[random.nextInt(characters.size)] }
                .joinToString("")
        }

        fun extractFileNameFromUrl(url: String): String {
            val index = url.lastIndexOf('/')
            if (index != -1 && index < url.length - 1) {
                return url.substring(index + 1)
            }
            return "generateRandomString(6).unknown"
        }

        fun xmlAnalyzer(xmlFile: File, findType: Int): MutableList<Pair<String, String>> {

            val dbf = DocumentBuilderFactory.newInstance()

            val analyzeList = mutableListOf<Pair<String, String>>()

            try {

                val xmlDoc = dbf.newDocumentBuilder().parse(xmlFile)
                xmlDoc.normalize()

                when (findType) {

                    FIND_URL -> {

                        val urlList = xmlDoc.getElementsByTagName("dependency")

                        for (i in 0 until urlList.length) {

                            val urlNode = urlList.item(i)

                            if (urlNode.nodeType == Node.ELEMENT_NODE) {
                                val urlElement = urlNode as Element

                                val name = urlElement.getAttribute("name")
                                val url = urlElement.textContent

                                analyzeList.add(Pair(name, url))
                            }

                        }

                    }

                    FIND_LANGUAGE -> {
                        val languageInfo = xmlDoc.getElementsByTagName("localLanguage").item(0).textContent
                        LanguageResource.languageType = languageInfo
                        analyzeList.add(Pair("LanguageType", languageInfo))
                    }

                    FIND_MIRROR -> {
                        val mirrorResult: String = if (xmlDoc.getElementsByTagName("mirrorDownload").length != 0) {
                            xmlDoc.getElementsByTagName("mirrorDownload").item(0).textContent
                        } else {
                            ""
                        }
                        when (mirrorResult) {
                            "" -> {
                                analyzeList.add(Pair("MirrorChoose", "False"))
                            }

                            "True" -> {
                                analyzeList.add(Pair("MirrorChoose", "True"))
                            }

                            else -> {
                                analyzeList.add(Pair("MirrorChoose", "False"))
                            }
                        }
                    }

                }

            } catch (e: Throwable) {
                e.printStackTrace()
            }

            return analyzeList

        }

        fun binaryNumberTransfer(b: Double, speed: Boolean): String {

            var transferCounter = 0
            var resultNumber: Float = b.toFloat()
            var resultString: String

            while (resultNumber >= 1024) {
                resultNumber /= 1024
                transferCounter++
            }

            resultString = String.format("%.2f", resultNumber)

            if (speed) {
                when (transferCounter) {
                    0 -> {
                        resultString = "${resultString}KB"
                    }

                    1 -> {
                        resultString = "${resultString}MB"
                    }

                    2 -> {
                        resultString = "${resultString}GB"
                    }

                }
            } else {

                when (transferCounter) {
                    0 -> {
                        resultString = "${resultString}B"
                    }

                    1 -> {
                        resultString = "${resultString}KB"
                    }

                    2 -> {
                        resultString = "${resultString}MB"
                    }

                    3 -> {
                        resultString = "${resultString}GB"
                    }
                }
            }

            return resultString
        }

        fun createTmpDir(filePath: String?): File {

            if (filePath == null) {
                var tempDir = File(targetProject!!.projectDir,"tmp-" + generateRandomString())

                while (tempDir.exists()) {
                    tempDir =  File(targetProject!!.projectDir,"tmp-" + generateRandomString())
                }

                tempDir.mkdir()

                return tempDir
            } else {

                val normalDir = File(targetProject!!.projectDir, filePath)

                if (!normalDir.isDirectory)
                    normalDir.mkdir()

                return normalDir

            }
        }

    }

}