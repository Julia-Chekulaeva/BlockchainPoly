import org.junit.jupiter.api.Test
import java.io.File

class Tests {

    @Test
    fun testNodes() {
        val fileName = "src/test/resources/lastBlock.txt"
        val thread1 = Thread {
            createNode(1, true, File(fileName))
        }
        thread1.start()
        val thread2 = Thread {
            createNode(2, false, File(fileName))
        }
        thread2.start()
        val thread3 = Thread {
            createNode(2, false, File(fileName))
        }
        thread3.start()
        Thread.sleep(120_000)
        thread1.interrupt()
        thread2.interrupt()
        thread3.interrupt()
    }
}