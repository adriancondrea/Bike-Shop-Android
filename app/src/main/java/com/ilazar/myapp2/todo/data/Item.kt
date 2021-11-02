package com.ilazar.myapp2.todo.data

data class Item(
    val _id: String,
    var text: String
) {
    override fun toString(): String = text
}
