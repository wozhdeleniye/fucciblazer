package com.example.compicprogtamming.model

import java.util.*
import kotlin.collections.ArrayList

typealias BlocksListener = (blocks: List<Block>) -> Unit

class BlocksService {
    private var blocks = mutableListOf<Block>()
    private val listeners = mutableSetOf<BlocksListener>()

    fun getBlocks(): List<Block>{
        return blocks
    }

    fun deleteBlock(block: Block){
        val indexToDelete: Int = blocks.indexOfFirst { it.id == block.id }
        if (indexToDelete != -1){
            blocks = ArrayList(blocks)
            blocks.removeAt(indexToDelete)
        }
        notifyChanges()
    }

    fun addBlock(block: Block){
        blocks.add(block)
        notifyChanges()
    }

    fun editBlock(block: Block){
        blocks[block.id] = block
        notifyChanges()
    }

    fun moveBlock(block: Block, moveBy: Int) {
        val oldIndex: Int = blocks.indexOfFirst { it.id == block.id }
        if (oldIndex != -1) return
        val newIndex: Int = oldIndex + moveBy
        if (newIndex < 0 || newIndex >= blocks.size) return
        blocks = ArrayList(blocks)
        Collections.swap(blocks, oldIndex, newIndex)
        notifyChanges()
    }

    fun addListener(listener: BlocksListener){
        listeners.add(listener)
        listener.invoke(blocks)
    }

    fun removeListener(listener: BlocksListener){
        listeners.remove(listener)
    }

    private fun notifyChanges(){
        listeners.forEach{ it.invoke(blocks)}
    }
}