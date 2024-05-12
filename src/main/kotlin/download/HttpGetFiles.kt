package org.robologic.download

import org.robologic.crosstest.Logger
import org.robologic.tools.LanguageResource
import org.robologic.tools.MessageSender
import org.robologic.tools.RoboTools
import org.robologic.tools.RoboTools.Companion.binaryNumberTransfer
import org.robologic.tools.RoboTools.Companion.xmlAnalyzer
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels
import java.nio.file.Files
import java.security.MessageDigest
import javax.net.ssl.HttpsURLConnection
import kotlin.experimental.and
import kotlin.io.path.Path


class HttpGetFiles(private val messageSender: MessageSender) {

    private val mirrorHttps = "https://maven.aliyun.com/repository/"

    private val ajLib = RoboTools.createTmpDir("AARToJAR-Libraries")

    private val mirrorNode = listOf(
        "central", "releases", "google", "gradle-plugin", "public"
    )

    private lateinit var saveTempDir: File


    fun startDownloadTaskQueue(configureFile: File): List<Pair<String, File>> {

        val downloadList = xmlAnalyzer(configureFile, RoboTools.FIND_URL)
        val downloadFilesList = mutableListOf<Pair<String, File>>()

        val mirrorChoose = xmlAnalyzer(configureFile, RoboTools.FIND_MIRROR)[0].second

        //To be Optimized
        if (mirrorChoose == "True") {
            Logger.debug("选择镜像URL下载")

            for (i in 0 until downloadList.size) {
                downloadList[i] = Pair(downloadList[i].first, httpsUrlAssembler(downloadList[i].second))
            }
            //DEBUG
            for (mirror in downloadList) {
                Logger.debug("Name:${mirror.first} URL:${mirror.second}")
            }
        }

        if (downloadList.isNotEmpty()) {

            saveTempDir = RoboTools.createTmpDir(null)

            for (downloadItem in downloadList) {

                val hashCode = hashJARVerification(downloadItem.second)

                if (hashCode != null) {
                    val downloadName = downloadItem.first
                    val downloadUrl = URL(downloadItem.second)

                    val httpsConn = downloadUrl.openConnection() as HttpsURLConnection
                    val fileSize = httpsConn.contentLength

                    val fileSavePath =
                        "${saveTempDir.absolutePath}/${RoboTools.extractFileNameFromUrl(downloadUrl.toString())}"


                    Channels.newChannel((httpsConn.inputStream)).use { rbc ->
                        FileOutputStream(fileSavePath).use { fos ->

                            val fileChannel = fos.channel
                            var totalByTesRead: Long = 0
                            val startTime = System.currentTimeMillis()

                            //DEBUG
                            Logger.debug(
                                LanguageResource.getImplantString(
                                    "Download start",
                                    listOf(downloadUrl.toString())
                                )
                            )

                            messageSender.sendMessageOrImplantString(
                                MessageSender.TYPE_LIFECYCLE,
                                "Download start",
                                listOf(downloadUrl.toString())
                            )

                            fileChannel.transferFrom(rbc, 0, Long.MAX_VALUE).also { bytesRead ->

                                totalByTesRead += bytesRead

                                val progress = (totalByTesRead.toDouble() / fileSize * 100).toInt()
                                val currentTime = System.currentTimeMillis() - startTime
                                val speed = if (currentTime > 0) totalByTesRead / currentTime else 0

                                //DEBUG
                                Logger.debug(
                                    LanguageResource.getImplantString(
                                        "Download info",
                                        listOf(
                                            downloadName,
                                            "${binaryNumberTransfer(totalByTesRead.toDouble(), false)}/${
                                                binaryNumberTransfer(
                                                    fileSize.toDouble(), false
                                                )
                                            }",
                                            progress.toString(),
                                            binaryNumberTransfer(speed.toDouble(), true)
                                        )
                                    )
                                )

                                messageSender.sendMessageOrImplantString(
                                    MessageSender.TYPE_LIFECYCLE, "Download info",
                                    listOf(
                                        downloadName,
                                        "${binaryNumberTransfer(totalByTesRead.toDouble(), false)}/${
                                            binaryNumberTransfer(
                                                fileSize.toDouble(), false
                                            )
                                        }",
                                        progress.toString(),
                                        binaryNumberTransfer(speed.toDouble(), true)
                                    )
                                )


                            }

                            //DEBUG
                            Logger.debug(
                                LanguageResource.getImplantString(
                                    "Download complete",
                                    listOf(downloadName)
                                )
                            )

                            messageSender.sendMessageOrImplantString(
                                MessageSender.TYPE_LIFECYCLE, "Download complete",
                                listOf(downloadName)
                            )

                        }

                    }

                    downloadFilesList.add(Pair("$hashCode-$downloadName", File(fileSavePath)))

                } else {
                    messageSender.sendMessageOrImplantString(MessageSender.TYPE_LIFECYCLE, "Has been downloaded", null)
                    continue
                }
            }
        }
        return downloadFilesList
    }

    private fun httpsUrlAssembler(originUrl: String): String {
        val afterIndex = originUrl.indexOf("maven2")

        val newUrl = mirrorHttps + mirrorNode[2]
        if (afterIndex != -1) {
            return newUrl + originUrl.substring(afterIndex + "maven2".length)
        }
        return originUrl
    }

    private fun hashJARVerification(fileURL: String): String? {

        val digest = MessageDigest.getInstance("MD5")

        digest.update(fileURL.toByteArray())

        val digestByte = digest.digest()

        val digestString = StringBuilder().apply {
            for(byte in digestByte){
                append(String.format("%02x",byte and 0xff.toByte()))
            }
        }.toString()

        ajLib.listFiles()?.forEach { file->

            val fileHaseCode = file.name.substring(0,file.name.indexOf('-'))
            if (fileHaseCode == digestString){
                return null
            }

        }

        return digestString

    }

    fun cancelTmpDir(): Boolean {

        Files.walk(Path(saveTempDir.absolutePath))
            .sorted(Comparator.reverseOrder())
            .forEach(Files::deleteIfExists)

        return true
    }

}