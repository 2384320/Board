package com.dongyang.mysolelife.fragments

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.dongyang.mysolelife.R
import com.dongyang.mysolelife.board.BoardAdapter
import com.dongyang.mysolelife.board.BoardDetailActivity
import com.dongyang.mysolelife.board.BoardModel
import com.dongyang.mysolelife.board.BoardWriteActivity
import com.dongyang.mysolelife.databinding.FragmentTalkBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class TalkFragment : Fragment() {

    private lateinit var binding : FragmentTalkBinding
    var datas = mutableListOf<BoardModel>()
    var datas2 = mutableListOf<BoardModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onResume() {
        super.onResume()
        binding.boardListView.adapter = null

        val task:GetData = GetData()
        task.execute("https://ah25ys9ec9.execute-api.us-east-2.amazonaws.com/default/BoardCommunityGetData")

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_talk, container, false)

        binding.writeBtn.setOnClickListener {
            val intent = Intent(context, BoardWriteActivity::class.java)
            startActivity(intent)
        }
        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_homeFragment)
        }

        binding.boardDailyTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_boardDailyFragment)
        }

        binding.myPageTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_myPageFragment)
        }
        //////////////////////////////////////////////////////////////////////////////////////////

        binding.boardListView.setOnItemClickListener { adapterView, view, i, l ->
            val clickedItem = datas2[i]
            val myIntent = Intent(context, BoardDetailActivity::class.java)
            myIntent.putExtra("info", clickedItem)
            startActivity(myIntent)
        }

        return binding.root
    }

    internal inner class GetData: AsyncTask<String?, Void?, String>(){

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            datas = mutableListOf<BoardModel>()
            //리스트 새로고침

            val data = JSONObject(result).getString("body")
            val items = JSONObject(data).getJSONArray("Items")
            var i = 0
            while( i < items.length()){
                val jsonObject = items.getJSONObject(i)

                val titleType = jsonObject.getString("title")
                val title = JSONObject(titleType).getString("S")
                val titleSlice = title.substring(0 until 10) + "..."

                val contentType = jsonObject.getString("content")
                val content = JSONObject(contentType).getString("S")
                val contentSlice = content.substring(0 until 10) + "..."

                val timeType = jsonObject.getString("time")
                val time = JSONObject(timeType).getString("S")

                val uidType = jsonObject.getString("uid")
                val uid = JSONObject(uidType).getString("S")

                datas.add(BoardModel(titleSlice, contentSlice, time, uid))
                datas2.add(BoardModel(title, content, time, uid))
                // uid는 고정적으로 2로 들어가는데 BoardWriteActivity에서 확인 가능함.

                i++
            }
            val adapter = BoardAdapter(datas)
            binding.boardListView.adapter = adapter

        }

        protected override fun doInBackground(vararg params: String?): String
        {

            val serverURL = params[0]
            val client = OkHttpClient()
            val request = Request.Builder().url(serverURL.toString()).get().build()
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