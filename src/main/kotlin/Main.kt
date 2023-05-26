import blockchain.Node1
import blockchain.Node2
import blockchain.Node3
import java.io.File

const val endZeros = "0000"
const val dataLen = 256

fun createNode(nodeType: Int, isGenesis: Boolean, files: List<File>) {
    val node = when (nodeType) {
        1 -> Node1(dataLen, files)
        2 -> Node2(dataLen, files)
        3 -> Node3(dataLen, files)
        else -> throw Exception("Illegal node type")
    }
    if (isGenesis) {
        node.createFirstBlock()
    }
    node.createBlocks()
}

fun main() {
    val nodeType = System.getenv("NODE_TYPE")
    val isGenesis = System.getenv("GENESIS_NODE") == nodeType
    val files = System.getenv("FILES").split(",").map { File(it) }
    createNode(nodeType.toInt(), isGenesis, files)
}