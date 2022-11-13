package com.dongyang.mysolelife.personalize

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dongyang.mysolelife.R

class PersonalizeDetailActivity : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalize_detail)

        val boardData = intent.getSerializableExtra("personalize info") as PersonalizeModel

        val title : TextView = findViewById(R.id.title)
        val content : TextView = findViewById(R.id.content)
        val personalizeDetailImg : ImageView = findViewById(R.id.personalizeImg)
        val personalizeDetailCategory : TextView = findViewById(R.id.personalizeCategory)
        val upload_time : TextView = findViewById(R.id.time)

        title.text = boardData.title
        content.text = boardData.content
        personalizeDetailCategory.text = "#" + boardData.category
        upload_time.text = boardData.upload_time

        Glide.with(this).load(boardData.img_url).into(personalizeDetailImg)
    }
}