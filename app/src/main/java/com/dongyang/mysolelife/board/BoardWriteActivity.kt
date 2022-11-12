package com.dongyang.mysolelife.board

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import com.dongyang.mysolelife.R
import com.dongyang.mysolelife.databinding.ActivityBoardWriteBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class BoardWriteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBoardWriteBinding

    private val TAG = BoardWriteActivity::class.java.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)

        binding.writeBtn.setOnClickListener {

            val title = binding.titleArea.text.toString()
            val content = binding.contentArea.text.toString()
//            승연님 uid 받아 넣기, time은 람다에서 받아 넣고있긴하지만 여기서 넣을수 있으면 그래도 됨 // 지금은 임의값 입력
//            val uid = FBAuth.getUid()
//            var time = FBAuth.getTime()
            val time = "1"
            val uid = "2"

            val task:InsertData= InsertData()
            task.execute("https://c9khmf8erj.execute-api.us-east-2.amazonaws.com/default/BoardCommunityWrite", title, content, time, uid)

            Log.d(TAG,"input Title: $title, Content: $content")
            Toast.makeText(this,"게시글 작성 완료", Toast.LENGTH_LONG).show()

            finish()
        }
    }

    internal inner class InsertData: AsyncTask<String?, Void?, String>(){

        protected override fun doInBackground(vararg params: String?): String
        {
            val serverURL = params[0]
            val title = params[1]
            val content = params[2]
            val time = params[3]
            val uid = params[4]
            val jsonObject = JSONObject()

            Log.d(TAG, "POST response - back - $title $content $time $uid")
            try {
                jsonObject.put("title", title)
                jsonObject.put("content", content)
                jsonObject.put("time", time)
                jsonObject.put("uid", uid)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            val client = OkHttpClient()
            val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
            val body = RequestBody.create(JSON, jsonObject.toString())
            val request = Request.Builder().url(serverURL.toString()).post(body).build()
            var response: Response? = null

            return try {
                response = client.newCall(request).execute()
                response.body!!.string()

            } catch (e: IOException) {
                e.printStackTrace()
                e.toString()
            }
        }
    }
}