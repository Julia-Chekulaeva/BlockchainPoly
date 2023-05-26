package blockchain

import java.io.File
import java.util.*

class Node1(
    dataLen: Int, files: List<File>
) : Node(dataLen, BlockTypes.BLOCK1, files) {
    override fun changeNonce() {
        nonce = Random().nextInt()
    }
}

class Node2(
    dataLen: Int, files: List<File>
) : Node(dataLen, BlockTypes.BLOCK2, files) {

    private var diff: Int = 0

    override fun changeNonce() {
        diff++
        nonce += diff
    }
}

class Node3(
    dataLen: Int, files: List<File>
) : Node(dataLen, BlockTypes.BLOCK3, files) {

    private var diff: Int = 0

    private fun diff1InsteadOf0() = if (diff <= 0) 1 else diff

    override fun changeNonce() {
        val newDiff = nonce
        nonce += diff1InsteadOf0()
        diff = newDiff
    }
}