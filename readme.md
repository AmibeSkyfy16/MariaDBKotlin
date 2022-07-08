# MariaDBKotlin

The goal of this project is to create a small library to download, install and start a mariadb database server.
It will be used for one of my fabricmc mod.

### How to use

#### This is the default configuration

````kotlin

    data class Builder(
        var port: Int = 3306, // The port use for the connection
        var mariaDBVersion: MariaDBVersion = MariaDBVersion.STABLE_10_8_3, // The version of mariadb to download
        var installationDir: String = SystemUtils.JAVA_IO_TMPDIR + "/EmbeddedMariaDB", // The folder where mariadb will be installed
        var mariaDBFolderAsZip: String = installationDir + "/${mariaDBVersion.filename}", // The path for the downloaded file (mariadb-10.8.3-winx64.zip)
        var mariaDBFolder: String = mariaDBFolderAsZip.replace("\\.\\w+$".toRegex(), ""), // The path for the future folder which will contain the mariadb files (after file extraction)
        var dataDir: String = "$mariaDBFolder/data", //  The area where the retain database would be stored
        var os: OS = if (SystemUtils.IS_OS_WINDOWS) OS.WINDOWS else OS.LINUX, // On which OS is it (now only windows is supported well) 
        var isRunInThread: Boolean = false // If the call for method setupFiles() and start() should be run in a separated thread or not
    )

````

#### A basic example

````kotlin

    val builder: DBConfig.Builder = DBConfig.Builder() // create a builder first
    val dbconfig: DBConfig = builder.build() // build the dbconfig
    val db: DB = DB(dbconfig) // create a DB Object based on the dbconfig
    db.setupFiles() // call setupFiles to download, extract and install the mariadb server
    db.start() // Then start the mariadb server

````

#### Example where operations for database are run in a separate thread


````kotlin
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
````