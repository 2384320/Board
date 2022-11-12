package com.dongyang.mysolelife.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.dongyang.mysolelife.R
import kotlinx.android.synthetic.main.activity_board_detail.*
import kotlinx.android.synthetic.main.content_rv_item.*

class BoardDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)

        val boardData = intent.getSerializableExtra("info") as BoardModel

        Log.d("태그", boardData.toString())
        val title : TextView = findViewById(R.id.title)
        val content : TextView = findViewById(R.id.content)
        val time : TextView = findViewById(R.id.title)

        title.text = boardData.title
        content.text = boardData.content
        time.text = boardData.time
    }
}