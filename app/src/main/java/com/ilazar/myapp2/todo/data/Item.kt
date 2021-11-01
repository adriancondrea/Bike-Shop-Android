package com.ilazar.myapp2.todo.data

data class Item(
    val id: String,
    var text: String
) {
    override fun toString(): String = text
}
