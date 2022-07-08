@file:Suppress("SqlNoDataSourceInspection")

package ch.skyfy.mariadbkotlin

import org.ktorm.database.asIterable
import org.ktorm.logging.ConsoleLogger
import kotlin.test.Test

class KtormTest {

    @Test
    fun ktormDBConnection() {
        val database = org.ktorm.database.Database.connect(
            "jdbc:mariadb://localhost:3307",
            driver = "org.mariadb.jdbc.Driver",
            user = "root",
            password = ""
        )

        database.useConnection { conn ->
            val sql = "create database if not exists `test db`;"

            val v = conn.prepareStatement(sql).use { statement ->
                val r = statement.executeQuery()
                r
                println()
            }

        }
        println("done")
    }

}