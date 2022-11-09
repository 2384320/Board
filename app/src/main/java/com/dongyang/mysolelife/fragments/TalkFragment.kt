package com.dongyang.mysolelife.fragments

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.dongyang.mysolelife.R
import com.dongyang.mysolelife.board.BoardAdapter
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        binding.tipTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_tipFragment)
        }

        binding.bookmarkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_boardDailyFragment)
        }

        binding.storeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_storeFragment)
        }
        //////////////////////////////////////////////////////////////////////////////////////////

        val task:GetData = GetData()
        task.execute("https://ah25ys9ec9.execute-api.us-east-2.amazonaws.com/default/BoardCommunityGetData")


        return binding.root
    }

    internal inner class GetData: AsyncTask<String?, Void?, String>(){

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            //리스트 새로고침
            val datas = mutableListOf<BoardModel>()
            val data = JSONObject(result).getString("body")
            val items = JSONObject(data).getJSONArray("Items")

            var i = 0
            while( i < items.length()){
                val jsonObject = items.getJSONObject(i)
                val titleType = jsonObject.getString("title")
                val title = JSONObject(titleType).getString("S")
                val contentType = jsonObject.getString("content")
                val content = JSONObject(contentType).getString("S")

                // time, uid 필요없으나 모델에따라 아무거나 넣는중 + 필요시 db 파씽해서 넣을 수 있음 /
                val time="1"
                val uid="a"

                datas.add(BoardModel(title,content,uid,time))

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