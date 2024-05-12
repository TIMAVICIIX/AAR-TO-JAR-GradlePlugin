package org.robologic.releasediv

import org.robologic.crosstest.Logger
import org.robologic.tools.MessageSender
import org.robologic.tools.RoboTools
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import kotlin.io.path.Path

class ReleaseFiles(private val messageSender: MessageSender,private val fileList: List<Pair<String, File>>) {

    private val releaseDir: File = RoboTools.createTmpDir(null)
    private val releaseJarDir: File = RoboTools.createTmpDir("AARToJAR-Libraries")

    /**
     * 1.Rename downloaded file
     * 2.Release renamed file
     * 3.choose jar file and move to 'lib' dir
     * 4.refresh temp dir who save unzip files
     * */
    fun releaseAndInstallJars() {


        for (file in fileList) {
            val zipFile = renameToZip(file.second)



            unZipFile(zipFile)
            jarFilter(file.first)
            refreshTempDir()
        }

        releaseDir.delete()

    }

    private fun jarFilter(firstName: String) {

        var haveCount = 0

        releaseDir.listFiles()?.forEach { file ->

            if (file.extension == "jar") {

                Files.move(file.toPath(), Path((releaseJarDir.path + "\\${firstName}-${file.name}")))
                haveCount++

            }
        }

        if (haveCount == 0) {
            messageSender.sendMessageOrImplantString(MessageSender.TYPE_LIFECYCLE, "No Jars", null)
            Logger.debug("Didn't have Jar to move Lib!")
        } else {
            messageSender.sendMessageOrImplantString(
                MessageSender.TYPE_INFO,
                "Have Jars",
                listOf(haveCount.toString())
            )
            Logger.debug("Have $haveCount Jars Move to Lib")
        }


    }

    private fun unZipFile(zipFile: File) {
        val unZipStream = ZipInputStream(Files.newInputStream(zipFile.toPath()))

        var entry: ZipEntry? = unZipStream.nextEntry

        if (entry != null) {
            while (entry != null) {
                val newFilePath = Paths.get(releaseDir.path, entry.name)
                if (!entry.isDirectory) {
                    Files.copy(unZipStream, newFilePath)
                } else {
                    Files.createDirectory(newFilePath)
                }
                unZipStream.closeEntry()
                entry = unZipStream.nextEntry
            }
        } else {
            messageSender.sendMessageOrImplantString(MessageSender.TYPE_LIFECYCLE, "Type warn", listOf(zipFile.name))
        }
    }

    private fun renameToZip(targetFile: File): File {

        val newZipName = targetFile.name.toString().replaceAfter(".", "zip")
        val newPathName = Path(targetFile.parent, newZipName)

        Files.move(targetFile.toPath(), newPathName)

        return File(newPathName.toUri())

    }


    private fun refreshTempDir(): Boolean {

        if (releaseDir.exists() && releaseDir.isDirectory) {
            releaseDir.listFiles()?.forEach { file ->
                if (file.isDirectory) {
                    file.deleteRecursively()
                } else {
                    file.delete()
                }
            }
        }
        return true

    }

}