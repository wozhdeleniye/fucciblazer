package com.example.compicprogtamming

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.compicprogtamming.databinding.ActivityMainBinding
import com.example.compicprogtamming.interpreterlogic.Interpreter
import com.example.compicprogtamming.model.Block
import com.example.compicprogtamming.model.BlocksListener
import com.example.compicprogtamming.model.BlocksService
import com.example.compicprogtamming.model.*
import org.apache.commons.lang3.ObjectUtils.Null
import java.util.Collections

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: BlocksAdapter

    private val blocksService: BlocksService
        get() = (applicationContext as App).blocksService



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(binding.root)


        adapter = BlocksAdapter(object : BlockActionListener{
            override fun onBlockDelete(block: Block) {
                blocksService.deleteBlock(block)
            }
            override fun onBlockSwap(oldInd: Int, newInd: Int){
                blocksService.swapBlock(oldInd, newInd)
            }

            override fun onBlockEdit(block: Block) {
                val position = block.id
                val type = block.type
                when(block.type){
                    0 ->{
                        callVarBlockEdit(position, type)
                    }
                    1 -> {
                        callOperBlockEdit(position, type)
                    }
                    2 -> {
                        callOutBlockEdit(position, type)
                    }
                }
            }

        })



        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        val itemTouchSwipes = ItemTouchHelper(SwapSwipe(adapter))
        itemTouchSwipes.attachToRecyclerView(binding.recyclerView)

        binding.apply {
            naviView.setNavigationItemSelectedListener {
                onSelect(it)
            }
            open.setOnClickListener{
                drawer.openDrawer(GravityCompat.END)
            }
            start.setOnClickListener{

            }
        }
        blocksService.addListener(blocksListener)

        binding.start.setOnClickListener(){
            startInterpreter()
        }
    }

    fun startInterpreter(){
        adapter.notifyDataSetChanged()
        Interpreter.blockList = blocksService.getBlocks()
        var output = ""
        for(str in Interpreter.outBlocks()) output += str + "\n"
        binding.outBlock.text = output
    }



    override fun onDestroy() {
        super.onDestroy()
        blocksService.removeListener(blocksListener)
    }

    private val blocksListener: BlocksListener = {
        adapter.blocks = it
    }

    fun onSelect(item: MenuItem):Boolean{
        when(item.itemId){
            R.id.crVar ->{
                callVarBlockMenu()
            }
            R.id.crOper ->{
                callOperBlockMenu()
            }
            R.id.crOut ->{
                callOutBlockMenu()
            }
            //создание других блоков.... потом
        }
        binding.drawer.closeDrawer(GravityCompat.END)
        return true
    }


    private var newBlockIndex = 0

    fun addBlock(varName: String, varValue: String, type: Int){

        when(type){
            0 -> {
                val newBlock = VarBlock(newBlockIndex, type)
                newBlock.varName = varName
                newBlock.varValue = varValue
                adapter.notifyDataSetChanged()
                blocksService.addBlock(newBlock)
            }
            1 -> {
                val newBlock = OperBlock(newBlockIndex, type)
                newBlock.varName = varName
                newBlock.varOper = varValue
                adapter.notifyDataSetChanged()
                blocksService.addBlock(newBlock)
            }
            2 -> {
                val newBlock = OutBlock(newBlockIndex, type)
                newBlock.varName = varName
                adapter.notifyDataSetChanged()
                blocksService.addBlock(newBlock)
            }
        }

        newBlockIndex += 1
    }

    fun callVarBlockMenu(){
        binding.drawer.closeDrawer(GravityCompat.END)

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.menu_cr_var, null);
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("Variable")
        val mAlertDialog = mBuilder.show()
        val mAlertDialogButton = mDialogView.findViewById<Button>(R.id.dialogCreateButton)
        val mAlertDialogEditTextName = mDialogView.findViewById<EditText>(R.id.crVarName)
        val mAlertDialogEditTextValue = mDialogView.findViewById<EditText>(R.id.crVarValue)
        mAlertDialogButton.setOnClickListener(){
            mAlertDialog.dismiss()
            val varName = mAlertDialogEditTextName.text.toString()
            val varValue = mAlertDialogEditTextValue.text.toString()
            addBlock(varName, varValue, 0)
            Toast.makeText(this,"Variable: " + varName + " = " + varValue, Toast.LENGTH_LONG).show()
        }
    }
    fun callOperBlockMenu(){
        binding.drawer.closeDrawer(GravityCompat.END)

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.menu_cr_oper, null);
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("Operation")
        val mAlertDialog = mBuilder.show()
        val mAlertDialogButton = mDialogView.findViewById<Button>(R.id.dialogCreateButton)
        val mAlertDialogEditTextName = mDialogView.findViewById<EditText>(R.id.crOperName)
        val mAlertDialogEditTextValue = mDialogView.findViewById<EditText>(R.id.crOperOper)
        mAlertDialogButton.setOnClickListener(){
            mAlertDialog.dismiss()
            val varName = mAlertDialogEditTextName.text.toString()
            val varOperation = mAlertDialogEditTextValue.text.toString()
            addBlock(varName, varOperation, 1)
            Toast.makeText(this,"Operation: " + varName + " = " + varOperation, Toast.LENGTH_LONG).show()
        }
    }
    fun callOutBlockMenu(){
        binding.drawer.closeDrawer(GravityCompat.END)

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.menu_cr_out, null);
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("Output")
        val mAlertDialog = mBuilder.show()
        val mAlertDialogButton = mDialogView.findViewById<Button>(R.id.dialogCreateButton)
        val mAlertDialogEditTextName = mDialogView.findViewById<EditText>(R.id.crOutName)
        mAlertDialogButton.setOnClickListener(){
            mAlertDialog.dismiss()
            val varName = mAlertDialogEditTextName.text.toString()
            addBlock(varName, "1", 2)
            Toast.makeText(this,"Output: " + varName, Toast.LENGTH_LONG).show()
        }
    }
    fun callVarBlockEdit(position: Int, type: Int){
        val mDialogView = LayoutInflater.from(this@MainActivity).inflate(R.layout.menu_cr_var, null);
        val mBuilder = AlertDialog.Builder(this@MainActivity)
            .setView(mDialogView)
            .setTitle("Variable")
        val mAlertDialog = mBuilder.show()
        val mAlertDialogButton = mDialogView.findViewById<Button>(R.id.dialogCreateButton)
        val mAlertDialogEditTextName = mDialogView.findViewById<EditText>(R.id.crVarName)
        val mAlertDialogEditTextValue = mDialogView.findViewById<EditText>(R.id.crVarValue)
        mAlertDialogButton.setOnClickListener(){
            mAlertDialog.dismiss()
            val varName = mAlertDialogEditTextName.text.toString()
            val varValue = mAlertDialogEditTextValue.text.toString()
            val newBlock = VarBlock(position, type)
            if((varName != "") and (mAlertDialogEditTextValue.text.toString() != "")){
                newBlock.varName = varName
                newBlock.varValue = varValue
                adapter.notifyDataSetChanged()
                blocksService.editBlock(newBlock)
                Toast.makeText(this@MainActivity,
                    "Variable: " + varName + " = " + varValue,
                    Toast.LENGTH_LONG).show()
            }
            else Toast.makeText(this@MainActivity,
                "Data wasn't changed",
                Toast.LENGTH_LONG).show()

        }
    }

    fun callOperBlockEdit(position: Int, type: Int){
        val mDialogView = LayoutInflater.from(this@MainActivity).inflate(R.layout.menu_cr_oper, null);
        val mBuilder = AlertDialog.Builder(this@MainActivity)
            .setView(mDialogView)
            .setTitle("Operation")
        val mAlertDialog = mBuilder.show()
        val mAlertDialogButton = mDialogView.findViewById<Button>(R.id.dialogCreateButton)
        val mAlertDialogEditTextName = mDialogView.findViewById<EditText>(R.id.crOperName)
        val mAlertDialogEditTextValue = mDialogView.findViewById<EditText>(R.id.crOperOper)
        mAlertDialogButton.setOnClickListener(){
            mAlertDialog.dismiss()
            val varName = mAlertDialogEditTextName.text.toString()
            val varOperation = mAlertDialogEditTextValue.text.toString()
            val newBlock = OperBlock(position, type)
            if((varName != "") and (varOperation != "")) {
                newBlock.varName = varName
                newBlock.varOper = varOperation
                adapter.notifyDataSetChanged()
                blocksService.editBlock(newBlock)
                Toast.makeText(
                    this@MainActivity,
                    "Operation: " + varName + " = " + varOperation,
                    Toast.LENGTH_LONG
                ).show()
            }
            else Toast.makeText(this@MainActivity,
                "Data wasn't changed",
                Toast.LENGTH_LONG).show()
        }
    }
    fun callOutBlockEdit(position: Int, type: Int){
        binding.drawer.closeDrawer(GravityCompat.END)

        val mDialogView = LayoutInflater.from(this@MainActivity).inflate(R.layout.menu_cr_out, null);
        val mBuilder = AlertDialog.Builder(this@MainActivity)
            .setView(mDialogView)
            .setTitle("Output")
        val mAlertDialog = mBuilder.show()
        val mAlertDialogButton = mDialogView.findViewById<Button>(R.id.dialogCreateButton)
        val mAlertDialogEditTextName = mDialogView.findViewById<EditText>(R.id.crOutName)
        mAlertDialogButton.setOnClickListener(){
            mAlertDialog.dismiss()
            val varName = mAlertDialogEditTextName.text.toString()
            val newBlock = OutBlock(position, type)
            if(varName != ""){
                newBlock.varName = varName
                adapter.notifyDataSetChanged()
                blocksService.editBlock(newBlock)
                Toast.makeText(
                    this@MainActivity,
                    "Output: " + varName,
                    Toast.LENGTH_LONG
                ).show()
            }
            else Toast.makeText(this@MainActivity,
                "Data wasn't changed",
                Toast.LENGTH_LONG).show()
        }
    }
}