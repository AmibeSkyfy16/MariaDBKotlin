plugins {
    kotlin("jvm") version "1.7.0"
    kotlin("kapt") version "1.7.0"
    id("java-library")
}

allprojects {

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.kapt")


    repositories {
        mavenCentral()
        mavenLocal()

        dependencies {

            implementation("org.apache.logging.log4j:log4j-core:2.18.0")
            implementation("org.apache.logging.log4j:log4j-api:2.18.0")


            testImplementation("org.jetbrains.kotlin:kotlin-test:1.7.0")
            testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.3")
            testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
            testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
            testImplementation("org.junit.platform:junit-platform-runner:1.8.2")
            testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
        }

    }
}

subprojects {

    kapt {
        arguments {
            arg("project", "${project.group}/${project.name}")
        }
    }


    tasks {

        named<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>("compileKotlin") {
            kotlinOptions.jvmTarget = "17"
        }

        named<JavaCompile>("compileJava") {
            options.release.set(17)
        }

        named<Test>("test") {
            useJUnitPlatform()

            // source: https://stackoverflow.com/questions/40954017/gradle-how-to-get-output-from-test-stderr-stdout-into-console
            testLogging {
                outputs.upToDateWhen { false } // When we execute build task, stderr-stdout of test classes will be show
                showStandardStreams = true
                showStackTraces = true
            }
        }

    }

}

configure(subprojects.filter {
    listOf(
        "MariaDBKotlin-core"
    ).contains(it.name)
}) {

    repositories {
        dependencies {

            if (project.name == "MariaDBKotlin-core") {
                implementation("net.lingala.zip4j:zip4j:2.11.1")
                implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")

                implementation("org.mariadb.jdbc:mariadb-java-client:3.0.5")
                implementation("org.ktorm:ktorm-core:3.5.0")
                implementation("org.ktorm:ktorm-support-mysql:3.5.0")

                implementation("commons-io:commons-io:2.11.0")
                implementation("org.apache.commons:commons-lang3:3.12.0")
            }
        }
    }

    // ------------------------------------------------------------------------------------------------ \\
    // Here are the different ways I found to create an executable jar file with java -jar filename.jar \\
    // ------------------------------------------------------------------------------------------------ \\

    tasks.withType<Jar> {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
        from(java.sourceSets.main.get().output)
    }

}

