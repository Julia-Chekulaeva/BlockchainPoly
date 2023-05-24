import blockchain.Node1
import blockchain.Node2
import blockchain.Node3
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required
import java.io.File

const val endZeros = "0000"
const val dataLen = 256

fun createNode(nodeType: Int, isGenesis: Boolean, file: File) {
    val node = when (nodeType) {
        1 -> Node1(dataLen, file)
        2 -> Node2(dataLen, file)
        3 -> Node3(dataLen, file)
        else -> throw Exception("Illegal node type")
    }
    if (isGenesis) {
        node.createFirstBlock()
    }
    node.createBlocks()
}

fun main(args: Array<String>) {
    val argParser = ArgParser("blockchain arguments parser")
    val nodeType by argParser
        .option(ArgType.Int, fullName = "node-type", shortName = "n", description = "node type")
        .required()
    val isGenesis = System.getenv("GENESIS_NODE").toInt() == nodeType
    val file = File(System.getenv("FILE"))
    argParser.parse(args)
    createNode(nodeType, isGenesis, file)
}