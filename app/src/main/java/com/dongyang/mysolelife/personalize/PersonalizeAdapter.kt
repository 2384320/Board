package com.dongyang.mysolelife.personalize

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.dongyang.mysolelife.R

class PersonalizeAdapter(private val items: MutableList<PersonalizeModel>): BaseAdapter() {
    override fun getCount(): Int = items.size

    override fun getItem(position: Int): PersonalizeModel = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.activity_board_daily_listview, parent, false)
        }

        val item: PersonalizeModel = items[position]

        val img_url = view?.findViewById<ImageView>(R.id.img_urlArea)


        if (view != null) {
            if (img_url != null) {
                Glide.with(view).load(item.img_url).into(img_url)
            }
        }

        return view!!

    }
}