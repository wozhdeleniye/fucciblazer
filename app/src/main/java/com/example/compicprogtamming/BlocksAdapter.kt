package com.example.compicprogtamming

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.compicprogtamming.BlocksAdapter.Companion.ID_REMOVE
import com.example.compicprogtamming.databinding.CardOperBinding
import com.example.compicprogtamming.databinding.CardOutBinding
import com.example.compicprogtamming.databinding.CardVarBinding
import com.example.compicprogtamming.model.Block
import com.example.compicprogtamming.model.OperBlock
import com.example.compicprogtamming.model.OutBlock
import com.example.compicprogtamming.model.VarBlock

interface BlockActionListener {
    fun onBlockDelete(block:Block)
    fun onBlockEdit(block: Block)
    fun onBlockSwap(oldInd: Int, newInd: Int)
    fun onBlockLeft(block: Block)
    fun onBlockRight(block:Block)
}

class BlocksDiffCallback(
    private val oldList: List<Block>,
    private val newList: List<Block>
) :DiffUtil.Callback(){
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldBlock = oldList[oldItemPosition]
        val newBlock = newList[newItemPosition]
        return oldBlock.id == newBlock.id
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldBlock = oldList[oldItemPosition]
        val newBlock = newList[newItemPosition]
        return oldBlock.id == newBlock.id && oldBlock.type == newBlock.type && oldBlock.tab == newBlock.tab
    }

}

class BlocksAdapter(
    val actionListener: BlockActionListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {

    class CardVarHolder(
        val binding: CardVarBinding
    ) : RecyclerView.ViewHolder(binding.root)

    class CardOperHolder(
        val binding: CardOperBinding
    ) : RecyclerView.ViewHolder(binding.root)

    class CardOutHolder(
        val binding: CardOutBinding
    ) : RecyclerView.ViewHolder(binding.root)

    var blocks: List<Block> = emptyList()
        set(newValue){
            val diffCallback = BlocksDiffCallback(field, newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onClick(v: View) {
        val block = v.tag as Block

        when(v.id){
            R.id.refactorMenuImageView ->{
                showPopupMenu(v)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        when(viewType){
            0 ->{
                val binding = CardVarBinding.inflate(inflater, parent, false)
                //добавление отклика на нажатие менюшки у карты и на нажатие на саму карточку
                binding.root.setOnClickListener(this)
                binding.refactorMenuImageView.setOnClickListener(this)

                return CardVarHolder(binding)
            }
            1 ->{
                val binding = CardOperBinding.inflate(inflater, parent, false)
                binding.root.setOnClickListener(this)
                binding.refactorMenuImageView.setOnClickListener(this)

                return CardOperHolder(binding)
            }
            else ->{
                val binding = CardOutBinding.inflate(inflater, parent, false)
                binding.root.setOnClickListener(this)
                binding.refactorMenuImageView.setOnClickListener(this)

                return CardOutHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val block = blocks[position]

        when(block.type){
            0->{
                val block = block as VarBlock
                with((holder as CardVarHolder).binding){
                    holder.itemView.tag = block
                    refactorMenuImageView.tag = block

                    varNameTextView.text = block.varName
                    varValueTextView.text = block.varValue
                }
            }
            1->{
                val block = block as OperBlock
                with((holder as CardOperHolder).binding){
                    holder.itemView.tag = block
                    refactorMenuImageView.tag = block

                    operNameTextView.text = block.varName
                    operValueTextView.text = block.varOper
                }
            }
            2->{
                val block = block as OutBlock
                with((holder as CardOutHolder).binding){
                    holder.itemView.tag = block
                    refactorMenuImageView.tag = block

                    outNameTextView.text = block.varName
                }
            }
        }
    }

    private fun showPopupMenu(view: View){
        val popupMenu = PopupMenu(view.context, view)
        val block = view.tag as Block

        popupMenu.menu.add(0, ID_EDIT,Menu.NONE,"Edit block")
        popupMenu.menu.add(0, ID_REMOVE,Menu.NONE,"Delete block")

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                ID_REMOVE ->{
                    actionListener.onBlockDelete(block)
                }
                else ->{
                    actionListener.onBlockEdit(block)
                }
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    override fun getItemViewType(position: Int): Int {
        return blocks[position].type
    }

    override fun getItemCount(): Int = blocks.size

    companion object {
        private const val ID_EDIT = 1
        private const val ID_REMOVE = 2
    }
}