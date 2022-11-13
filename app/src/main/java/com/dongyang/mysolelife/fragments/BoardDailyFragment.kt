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
import com.dongyang.mysolelife.board.BoardDetailActivity
import com.dongyang.mysolelife.boardDaily.BoardDailyAdapter
import com.dongyang.mysolelife.boardDaily.BoardDailyDetailActivity
import com.dongyang.mysolelife.boardDaily.BoardDailyModel
import com.dongyang.mysolelife.boardDaily.BoardDailyWriteActivity
import com.dongyang.mysolelife.databinding.FragmentBoarddailyBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class BoardDailyFragment : Fragment() {

    private lateinit var binding : FragmentBoarddailyBinding
    var datas = mutableListOf<BoardDailyModel>()

    private val TAG = BoardDailyFragment::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onResume() {
        super.onResume()
        binding.boardGridView.adapter = null

        val task: GetData = GetData()
        task.execute("https://pekvc7dpz3.execute-api.us-east-2.amazonaws.com/default/BoardDailyGetData")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_boarddaily, container, false)

        binding.writeBtn.setOnClickListener {
            val intent = Intent(context, BoardDailyWriteActivity::class.java)
            startActivity(intent)
        }
        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardDailyFragment_to_homeFragment)
        }

        binding.myPageTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardDailyFragment_to_myPageFragment)
        }

        binding.talkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_boardDailyFragment_to_talkFragment)
        }

        val task: GetData = GetData()
        task.execute("https://pekvc7dpz3.execute-api.us-east-2.amazonaws.com/default/BoardDailyGetData")

        binding.boardGridView.setOnItemClickListener { adapterView, view, i, l ->
            val clickedItem = datas[i]
            val myIntent = Intent(context, BoardDailyDetailActivity::class.java)
            myIntent.putExtra("daily info", clickedItem)
            startActivity(myIntent)
        }

        return binding.root
    }

    internal inner class GetData: AsyncTask<String?, Void?, String>(){

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            datas = mutableListOf<BoardDailyModel>()
            //리스트 새로고침
            val data = JSONObject(result).getString("body")
            val items = JSONObject(data).getJSONArray("Items")
            var i = 0
            while( i < items.length()){
                val jsonObject = items.getJSONObject(i)

                val titleType = jsonObject.getString("title")
                val title = JSONObject(titleType).getString("S")

                val contentType = jsonObject.getString("content")
                val content = JSONObject(contentType).getString("S")

                val categoryType = jsonObject.getString("category")
                val category = JSONObject(categoryType).getString("S")

                val timeType = jsonObject.getString("time")
                val time = JSONObject(timeType).getString("S")

                val uidType = jsonObject.getString("uid")
                val uid = JSONObject(uidType).getString("S")

                val img_urlType = jsonObject.getString("img_url")
                val img_url = JSONObject(img_urlType).getString("S")

                datas.add(BoardDailyModel(title, content, category, time, uid, img_url))
                // uid는 고정적으로 2로 들어가는데 BoardDailyWriteActivity에서 확인 가능함.

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