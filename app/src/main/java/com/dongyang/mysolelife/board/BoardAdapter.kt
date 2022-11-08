package com.dongyang.mysolelife.board

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.dongyang.mysolelife.R

class BoardAdapter(private val items: MutableList<BoardModel>): BaseAdapter() {
    override fun getCount(): Int = items.size

    override fun getItem(position: Int): BoardModel = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {


        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.activity_board_listview, parent, false)
        }

        val item: BoardModel = items[position]

        val title = view?.findViewById<TextView>(R.id.listview_title)
        val content = view?.findViewById<TextView>(R.id.listview_content)


        title!!.text = item.title
        content!!.text = item.content


        return view!!

    }
}