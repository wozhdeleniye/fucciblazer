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
            notifyChanges()
        }
    }

    fun addBlock(block: Block){
        blocks.add(block)
        blocks = ArrayList(blocks)
        notifyChanges()
    }

    fun editBlock(block:Block){
        blocks[block.id] = block
        blocks = ArrayList(blocks)
        notifyChanges()
    }

//    fun editBlock(block: Block){
//        blocks[block.id] = block
//        notifyChanges()
//    }

    fun swapBlock(oldInd: Int, newInd: Int) {
        blocks = ArrayList(blocks)
        Collections.swap(blocks, oldInd, newInd)
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