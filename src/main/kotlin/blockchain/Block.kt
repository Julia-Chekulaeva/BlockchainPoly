package blockchain

class Block(
    private val index: Int, private val prevHash: String?,
    private val hash: String, private val data: String,
    private var nonce: Int, private val type: BlockTypes
    ) {

    fun getIndex() = index

    fun getHash() = hash

    override fun toString(): String {
        return """Index = $index,
            |    previous_hash = $prevHash,
            |    hash = $hash,
            |    data = $data,
            |    nonce = $nonce,
            |    type = $type""".trimMargin()
    }
}