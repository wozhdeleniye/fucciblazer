package com.example.compicprogtamming.model

open class Block(
    val id: Int,
    val type: Int = -1
)

class VarBlock(
    id: Int,
    type: Int = 0,
    var varName: String? = null,
    var varValue: String? = null
) : Block(id, type = 0) {}

class OperBlock(
    id: Int,
    type: Int = 1,
    var varName: String? = null,
    var varOper: String? = null
) : Block(id, type = 1) {}

class OutBlock(
    id: Int,
    type: Int = 2,
    var varName: String? = null
) : Block(id, type = 2) {}