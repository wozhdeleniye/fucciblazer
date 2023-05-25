package com.example.compicprogtamming

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwapSwipe(
    val adapter: BlocksAdapter
) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swapFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlag = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(swapFlag, swipeFlag)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        adapter.actionListener.onBlockSwap(
            viewHolder.absoluteAdapterPosition,
            target.absoluteAdapterPosition
        )
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val ind = viewHolder.absoluteAdapterPosition
        val block = adapter.blocks[ind]

        when (direction) {
            ItemTouchHelper.LEFT -> {
                adapter.actionListener.onBlockLeft(block)
                adapter.notifyItemChanged(ind)
            }

            ItemTouchHelper.RIGHT -> {
                adapter.actionListener.onBlockRight(block)
                adapter.notifyItemChanged(ind)
            }
        }
    }
}