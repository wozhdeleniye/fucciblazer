package com.example.compicprogtamming.model

open class Block(
    val id: Int,
    val type: Int = -1,
    var tab: Int
)

class VarBlock(
    id: Int,
    type: Int = 0,
    tab: Int,
    var varName: String? = null,
    var varValue: String? = null
) : Block(id, type = 0, tab) {}

class OperBlock(
    id: Int,
    type: Int = 1,
    tab: Int,
    var varName: String? = null,
    var varOper: String? = null
) : Block(id, type = 1, tab) {}

class OutBlock(
    id: Int,
    type: Int = 2,
    tab: Int,
    var varName: String? = null
) : Block(id, type = 2, tab) {}

class IfBlock(
    id: Int,
    type: Int = 3,
    tab: Int,
    var ifCondition: String? = null
) : Block(id, type = 3, tab) {}