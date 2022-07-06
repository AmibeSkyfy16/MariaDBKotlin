package ch.skyfy.mariadbkotlin

import java.lang.Thread.sleep
import kotlin.test.Test

class MariaDBTests {

    @Test
    fun createWithDefault(){

        // Simulate minecraft server thread

        println("minecraft console, listing mods...")
        sleep(5000)
        println("12 mods listed.")

        println("Loading mods...")
        println("- BetterBackpack.jar")
        println("- BetterGui.jar")
        println("- MariaDBServerFabricMC.jar") // Loading my mod that will use this library to install and start a mariadb server

        val db = DB.createEmbeddedDatabase(DBConfig.Builder().build()) {
            println("Files for mariadb server have been installed")
            println("Starting the server ...")

            // after downloaded, copied, and extracted, we can decide to start the mariadb server
            it.start {
                println("mariadb server has started")
                sleep(1000)
                println("creating new data database")
                it.createDatabase("Hello")
            }
        } // download, copy and extract mariadb (did inside another thread)

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

}