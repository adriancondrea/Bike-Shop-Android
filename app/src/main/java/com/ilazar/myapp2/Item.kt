package com.ilazar.myapp2

data class Item(
    val id: String,
    var text: String
) {
    override fun toString(): String = text
}
