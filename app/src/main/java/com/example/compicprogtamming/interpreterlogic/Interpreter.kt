package com.example.compicprogtamming.interpreterlogic

import android.media.VolumeShaper.Operation
import com.example.compicprogtamming.model.Block
import com.example.compicprogtamming.model.IfBlock
import com.example.compicprogtamming.model.VarBlock
import com.example.compicprogtamming.model.OperBlock
import com.example.compicprogtamming.model.OutBlock
import java.util.Stack

object Interpreter {
    var blockList = emptyList<Block>()

    var varList = hashMapOf<String, String>()



    var output = mutableListOf<String>()

    var tabNested  = 0
    fun outBlocks() : MutableList<String>{
        output = mutableListOf()
        varList = hashMapOf()// string(index), string(value)
        var line = 0
        blockList.forEach{
            if(it.tab < tabNested) tabNested = it.tab
            if(it.tab == tabNested){
                when (it.type){
                    0->{
                        varList.put((it as VarBlock).varName.toString(), calculate((it).varValue.toString()))

                    }
                    1->{
                        varList.put((it as OperBlock).varName.toString(), calculate((it).varOper.toString()))
                    }
                    2 ->{
                        output.add(calculate((it as OutBlock).varName.toString()))
                    }
                    else ->{
                        if(calculate((it as IfBlock).ifCondition.toString()) == "true"){
                            tabNested += 1
                        }
                    }
                }
            }
            line += 1
        }
        return output
    }



    fun getVar(variable: String): String?{
        return varList.get(variable)
    }

    //приоритет знаков для ОПС
    private val operatorPriority: HashMap<String, Int>
        get() = hashMapOf(
            "<" to 0,
            ">" to 0,
            ">=" to 0,
            "<=" to 0,
            "==" to 0,
            "!=" to 0,
            "+" to 1,
            "-" to 1,
            "*" to 2,
            "/" to 2
        )

    //выполнение простейших операций
    fun innerOperation(left: String, right: String, operation: String) : String{
        var a = left.toInt()
        var b = right.toInt()
        return when(operation){
            "+" ->{
                (a + b).toString()
            }
            "-" ->{
                (a - b).toString()
            }
            "*" ->{
                (a * b).toString()
            }
            "/" ->{
                (a / b).toString()
            }
            ">" ->{
                (a > b).toString()
            }
            "<" ->{
                (a < b).toString()
            }
            ">=" ->{
                (a >= b).toString()
            }
            "<=" ->{
                (a <= b).toString()
            }
            "==" ->{
                (a == b).toString()
            }
            "!=" ->{
                (a != b).toString()
            }
            else -> "0"
        }
    }

    //распарсинг строки на отдельные переменные/константы/символы
    fun parsingExpression(expression: String) : List<String>{
        val regular = """(?:[a-zA-Z]+\w*|\d+|\+|\-|\*|[()]|\/|([<>]=?|==|!=))""".toRegex()
        val result = regular.findAll(expression).map { it.value }.toMutableList()
        for(i in result.indices){
            if(getVar(result[i]) != null){
                result[i] = getVar(result[i]).toString()
            }
        }
        return result
    }

    fun OPS(parsed: List<String>) : List<String>{//перевод в обратную польскую строку
        val result = mutableListOf<String>()
        val stack = Stack<String>()
        parsed.forEach {item ->
            if(item == "(") stack.push(item)
            else if(item == ")") {
                while(stack.peek() != "(") {
                    result.add(stack.pop())
                }
                stack.pop()
            }
            else if(operatorPriority.keys.contains(item)) {
                var peek = if(stack.isNotEmpty()) stack.peek() else null
                while(operatorPriority.keys.contains(peek) && operatorPriority[peek]!! >= operatorPriority[item]!!) {
                    result.add(stack.pop())
                    peek = if(stack.isNotEmpty()) stack.peek() else null
                }
                stack.push(item)
            }
            else{
                result.add(item)
            }
        }
        while(stack.isNotEmpty()){
            result.add(stack.pop())
        }
        return result
    }


    fun calculate(expression: String): String {
        val stack = Stack<String>()
        var left: String
        var right: String
        OPS(parsingExpression(expression)).forEach{item ->
            if(operatorPriority.contains(item)){
                right = stack.pop()
                left = stack.pop()
                stack.push(innerOperation(left, right, item))
            }
            else{
                stack.push(item)
            }
        }
        return stack.pop()
    }
}