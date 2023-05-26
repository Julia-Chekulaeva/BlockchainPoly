import blockchain.Node1
import blockchain.Node2
import blockchain.Node3
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required
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

fun main(args: Array<String>) {
    val argParser = ArgParser("blockchain arguments parser")
    val nodeType by argParser
        .option(ArgType.Int, fullName = "type", shortName = "t", description = "node type")
        .required()
    val isGenesis = System.getenv("GENESIS_NODE").toInt() == nodeType
    val files = System.getenv("FILES").split(";").map { File(it) }
    argParser.parse(args)
    createNode(nodeType, isGenesis, files)
}