package com.example.spirala1.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import com.example.spirala1.Models.Enums.KlimatskiTip

class KlimatskiTipChoiceAdapter(context: Context, private val values: Array<KlimatskiTip>) :
    ArrayAdapter<KlimatskiTip>(context, -1, values) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(android.R.layout.simple_list_item_multiple_choice, parent, false)
        val checkedTextView = view.findViewById<CheckedTextView>(android.R.id.text1)
        val item = getItem(position)
        checkedTextView.text = item?.opis

        return view
    }
}