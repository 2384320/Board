package com.dongyang.mysolelife.fragments

import android.content.Intent
import android.util.Log
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.dongyang.mysolelife.R
import com.dongyang.mysolelife.boardDaily.BoardDailyAdapter
import com.dongyang.mysolelife.boardDaily.BoardDailyModel
import com.dongyang.mysolelife.boardDaily.BoardDailyWriteActivity
import com.dongyang.mysolelife.databinding.FragmentBookmarkBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class BookmarkFragment : Fragment() {

    private lateinit var binding : FragmentBookmarkBinding

    private val TAG = BookmarkFragment::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmark, container, false)

        binding.writeBtn.setOnClickListener {
            val intent = Intent(context, BoardDailyWriteActivity::class.java)
            startActivity(intent)
        }
        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_homeFragment)
        }

        binding.tipTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_tipFragment)
        }

        binding.storeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_storeFragment)
        }

        binding.talkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_talkFragment)
        }

        val task: GetData = GetData()
        task.execute("https://pekvc7dpz3.execute-api.us-east-2.amazonaws.com/default/BoardDailyGetData")


        return binding.root
    }

    internal inner class GetData: AsyncTask<String?, Void?, String>(){

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            //리스트 새로고침
            val datas = mutableListOf<BoardDailyModel>()
            val data = JSONObject(result).getString("body")
            val items = JSONObject(data).getJSONArray("Items")
            var i = 0
            while( i < items.length()){
                val jsonObject = items.getJSONObject(i)
                val img_urlType = jsonObject.getString("img_url")
                val img_url = JSONObject(img_urlType).getString("S")

                // 임의 값  받아온거 파씽해주면 가능
                val title="1"
                val content="1"
                val time="1"
                val uid="a"

                datas.add(BoardDailyModel(title,content,uid,time,img_url))

                i++
            }
            val adapter = BoardDailyAdapter(datas)
            binding.boardGridView.adapter = adapter
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