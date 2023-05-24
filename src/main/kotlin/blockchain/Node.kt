package blockchain

import endZeros
import java.io.File
import java.security.MessageDigest

const val nodesCount = 3

abstract class Node(
    private val dataLen: Int, private val type: BlockTypes, private val files: List<File>
) {
    private var lastBlock: Block? = null

    protected var nonce = 0

    abstract fun changeNonce()

    private fun getNextIndex() = (lastBlock?.getIndex() ?: 0) + 1

    private fun getPrevHash() = lastBlock?.getHash() ?: ""

    private fun generateData(): String {
        var data = ""
        for (i in 0 until dataLen) {
            when (val c = (Math.random() * 62).toInt()) {
                in 0 until 10 -> data += '0' + c
                in 10 until 36 -> data += 'a' + c
                in 36 until 62 -> data += 'A' + c
            }
        }
        return data
    }

    companion object {
        fun getHash(byteArray: ByteArray) = MessageDigest.getInstance("SHA-256")
            .digest(byteArray).joinToString("") { "%02x".format(it) }
    }

    private fun calcHashItr(index: Int, prevHash: String?, data: String, nonce: Int): String {
        changeNonce()
        val hashInf = "$index$prevHash$data$nonce"
        return getHash(hashInf.toByteArray())
    }

    private fun calcHash(index: Int, prevHash: String?, data: String): String {
        var hash = calcHashItr(index, prevHash, data, nonce)
        while (!hash.endsWith(endZeros)) {
            if (index <= (lastBlock?.getIndex() ?: 0))
                resetNonceIndexPrevHashAndData()
            hash = calcHashItr(index, prevHash, data, nonce)
        }
        return hash
    }

    private fun resetNonceIndexPrevHashAndData() {
        val lastBlocks = List(nodesCount) { getBlockFromFile() }
        lastBlock = lastBlocks.maxBy { it.getIndex() }
        nonce = 0
    }

    private fun createNextBlock(isNotFirstBlock: Boolean) {
        if (isNotFirstBlock)
            resetNonceIndexPrevHashAndData()
        val index = getNextIndex()
        val prevHash = getPrevHash()
        val data = generateData()
        val nextBlock = Block(index, prevHash, calcHash(index, prevHash, data), data, nonce, type)
        if (isNotFirstBlock)
            resetNonceIndexPrevHashAndData()
        if (index == nextBlock.getIndex()) {
            println("Creating new node")
            loop@while (true) {
                try {
                    files[type.i].writeText(nextBlock.toString())
                    println(nextBlock.toString())
                    break@loop
                } catch (e: Exception) {
                    System.err.println("Error while writing file")
                }
            }
        }
    }

    private fun getTextFromFile(): String {
        var text: String? = null
        while (text == null) {
            try {
                text = files[type.i].readText()
            } catch (e: Exception) {
                continue
            }
        }
        return text
    }

    private fun getBlockFromFile(): Block {
        val text = getTextFromFile()
        val split = text.split(Regex("\\s*,\\s*")).map { it.split(Regex("\\s*=\\s*")) }
        val index = (split.find { it[0] == "Index" } ?: arrayListOf())[1].toInt()
        val prevHash = (split.find { it[0] == "previous_hash" } ?: arrayListOf())[1]
        val hash = (split.find { it[0] == "hash" } ?: arrayListOf())[1]
        val data = (split.find { it[0] == "data" } ?: arrayListOf())[1]
        val nonce = (split.find { it[0] == "nonce" } ?: arrayListOf())[1].toInt()
        val type = BlockTypes.valueOf((split.find { it[0] == "type" } ?: arrayListOf())[1])
        return Block(index, prevHash, hash, data, nonce, type)
    }

    fun createFirstBlock() {
        assert(lastBlock == null)
        createNextBlock(false)
    }

    fun createBlocks() {
        while (true) {
            createNextBlock(true)
        }
    }
}