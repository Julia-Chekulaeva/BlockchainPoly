import org.junit.jupiter.api.Test
import java.io.File

class Tests {

    @Test
    fun testNodes() {
        val files = listOf(
            "src/test/resources/lastBlock1.txt",
            "src/test/resources/lastBlock2.txt",
            "src/test/resources/lastBlock3.txt"
        ).map { File(it) }
        val thread1 = Thread {
            createNode(1, false, files)
        }
        val thread2 = Thread {
            createNode(2, false, files)
        }
        val thread3 = Thread {
            createNode(3, true, files)
        }
        thread1.start()
        thread2.start()
        thread3.start()
        Thread.sleep(120_000)
        thread1.interrupt()
        thread2.interrupt()
        thread3.interrupt()
    }
}