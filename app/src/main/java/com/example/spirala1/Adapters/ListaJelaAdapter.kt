package com.example.spirala1.Adapters

import android.content.Context
import android.widget.ArrayAdapter


class ListaJelaAdapter(context: Context, val items: MutableList<String>) :
    ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items) {

    fun addItem(item: String): Boolean {
        val jeloToLower = item.lowercase()
        if (!items.any { it.lowercase() == jeloToLower }) {
            items.add(item)
            notifyDataSetChanged()
            return true
        }
        return false
    }

    fun getListJelo() : List<String> {
        return items
    }
}
