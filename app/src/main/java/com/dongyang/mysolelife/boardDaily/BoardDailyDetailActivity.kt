package com.dongyang.mysolelife.boardDaily

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.dongyang.mysolelife.R
import com.dongyang.mysolelife.board.BoardModel

class BoardDailyDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_daily_detail)

        val boardData = intent.getSerializableExtra("daily info") as BoardDailyModel

        val title : TextView = findViewById(R.id.title)
        val content : TextView = findViewById(R.id.content)
        val boardDailyDetailImg : ImageView = findViewById(R.id.boardDailyDetailImg)
        val boardDailyDetailCategory : TextView = findViewById(R.id.boardDailyDetailCategory)
        val time : TextView = findViewById(R.id.time)

        title.text = boardData.title
        content.text = boardData.content
        boardDailyDetailCategory.text = "#" + boardData.category
        time.text = boardData.time

        Glide.with(this).load(boardData.img_url).into(boardDailyDetailImg)
    }
}