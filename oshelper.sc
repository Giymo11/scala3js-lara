

object OsHelper {
    def isWindows(): Boolean = System.getProperty("os.name").toLowerCase().contains("win")
    def convertBackslash(in: os.Path): String = in.toString().replace("\\", "/")
    def convertBackslash(in: String): String = in.replace("\\", "/")
}

