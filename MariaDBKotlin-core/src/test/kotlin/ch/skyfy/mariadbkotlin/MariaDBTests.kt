@file:Suppress("RedundantExplicitType")

package ch.skyfy.mariadbkotlin

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.Thread.sleep
import java.nio.file.Paths
import java.util.function.Consumer
import kotlin.test.Test

class MariaDBTests {

    @Test
    fun test1() {

        // Simulate minecraft server thread

        println("minecraft console, listing mods...")
        sleep(5000)
        println("12 mods listed.")

        println("Loading mods...")
        println("- BetterBackpack.jar")
        println("- BetterGui.jar")
        println("- MariaDBServerFabricMC.jar") // Loading my mod that will use this library to install and start a mariadb server

        val builder: DBConfig.Builder = DBConfig.Builder() // create a builder first
        val dbconfig: DBConfig = builder.build() // build the dbconfig
        val db: DB = DB(dbconfig) // create a DB Object based on the dbconfig
        db.setupFiles() // call setupFiles to download, extract and install the mariadb server
        db.start() // Then start the mariadb server


        // Here minecraft server thread continue his stuff
        println("- TheGraveYard.jar")
        println("- AnotherMod.jar")
        println("- AnotherMod2.jar")
        println("- AnotherMod3.jar")

        println("Loading resources...")
        sleep(10_000)
        println("All resources loaded !")

        sleep(10_000)

        println("Server has started ! Players can now connect")
    }

    @Test
    fun test2() {

        // Simulate minecraft server thread

        println("minecraft console, listing mods...")
        sleep(5000)
        println("12 mods listed.")

        println("Loading mods...")
        println("- BetterBackpack.jar")
        println("- BetterGui.jar")
        println("- MariaDBServerFabricMC.jar") // Loading my mod that will use this library to install and start a mariadb server

        val builder: DBConfig.Builder = DBConfig.Builder() // create a builder first
        builder.isRunInThread(true) // false by default. Here we define operations to be run in a thread
        val dbconfig: DBConfig = builder.build() // build the dbconfig
        val db: DB = DB(dbconfig) // create a DB Object based on the dbconfig
        db.setupFilesCallback = Consumer<DB> { // A callback called when the setupFiles method is finished (at this stage, mariadb server is ready to start)
            it.start() // start mariadb server juste after downloading, extracting and installing have been finished (ran in a separate thread in this example)
        }
        db.startedCallback = Consumer<DB> {
            // Here the mariadb server has started
            // You can now for example use JDBC or something similar to create your first database, table, etc.
        }
        db.setupFiles() // call setupFiles to download, extract and install the mariadb server (ran in a separate thread in this example)

        // Here minecraft server thread continue his stuff
        println("- TheGraveYard.jar")
        println("- AnotherMod.jar")
        println("- AnotherMod2.jar")
        println("- AnotherMod3.jar")

        println("Loading resources...")
        sleep(10_000)
        println("All resources loaded !")

        sleep(10_000)

        println("Server has started ! Players can now connect")
    }

    @Test
    fun simpleTest() {
        println("Running command: mysql.exe --user=root --password=\"\" --port=3307")

//            val args = listOf("cmd", "/c", "\"mysql.exe\" --user=root --password=\"\" --port=3307")
//            val args = listOf("cmd", "/c", "start", "cmd.exe","/k", "\"mysql.exe --user=root --password=\"\" --port=3307\"")
//            val args = listOf("cmd","/k", "\"mysql.exe --user=root --password=\"\" --port=3307\"")
        val args = listOf("cmd", "/c", "\"mysql.exe --user=root --password=\"\" --port=3307 \"")
//        val args = listOf("\"mysql.exe --user=root --password=\"\" --port=3307\"")

        val pb = ProcessBuilder(args)
        pb.directory(Paths.get("C:\\Users\\Skyfy16\\AppData\\Local\\Temp\\EmbeddedMariaDB\\mariadb-10.8.3-winx64\\bin").toFile())
        pb.redirectErrorStream(true)
        pb.environment()
        pb.inheritIO()
        val process = pb.start()

        process.descendants().use { stream ->

            stream.findFirst().ifPresent { processHandle ->
                val info = processHandle.info()
                processHandle.children().use { stream2 ->
                    stream2.findFirst().ifPresent { processHandle2 ->
                        val info2 = processHandle2.info()
                        println()
                    }
                }
                info.command().ifPresent {
//                    val pro = ProcessBuilder(it).start()

                    println()
                }
            }

        }

        process.children().use { str ->
            str.findFirst().ifPresent { processHandle ->


                processHandle.info().command().ifPresent { command ->
                    val pb2 = ProcessBuilder(command)
//                    val p2 = pb2.start()

//                    BufferedReader(InputStreamReader(p2.inputStream)).use {
//                        while (true) {
//                            val line = it.readLine() ?: break
//                            println(line)
//                            if (line == "MariaDB [(none)]>") {
//                                BufferedWriter(OutputStreamWriter(process.outputStream)).use { bw ->
//                                    bw.write("create database if not exists `hadda queloz`; exit;")
//                                    bw.flush()
//                                }
//                            }
//                        }
//                    }

                    println()
                }
            }
        }

        BufferedReader(InputStreamReader(process.inputStream)).use {
            while (true) {
                val line = it.readLine() ?: break
                println(line)
                if (line == "MariaDB [(none)]>") {
                    BufferedWriter(OutputStreamWriter(process.outputStream)).use { bw ->
                        bw.write("create database if not exists `hadda queloz`; exit;")
                        bw.flush()
                    }
                }
            }
        }

        sleep(2000)
    }

}