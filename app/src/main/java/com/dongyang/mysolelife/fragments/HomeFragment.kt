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
import com.dongyang.mysolelife.boardDaily.BoardDailyAdapter
import com.dongyang.mysolelife.boardDaily.BoardDailyDetailActivity
import com.dongyang.mysolelife.boardDaily.BoardDailyModel
import com.dongyang.mysolelife.databinding.FragmentHomeBinding
import com.dongyang.mysolelife.personalize.PersonalizeAdapter
import com.dongyang.mysolelife.personalize.PersonalizeDetailActivity
import com.dongyang.mysolelife.personalize.PersonalizeModel
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    var boardData = mutableListOf<BoardModel>()
    var personalizeData = mutableListOf<PersonalizeModel>()
    var i : Int = 0
    var a : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.talkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_talkFragment)
        }

        binding.boardDailyTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_boardDailyFragment)
        }

        binding.myPageTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_myPageFragment)
        }

        var task1 = GetData1()
        task1.execute("https://ah25ys9ec9.execute-api.us-east-2.amazonaws.com/default/BoardCommunityGetData")

        binding.homeBoardListView.setOnItemClickListener { adapterView, view, i, l ->
            val clickedItem = boardData[i]
            val myIntent = Intent(context, BoardDetailActivity::class.java)
            myIntent.putExtra("info", clickedItem)
            startActivity(myIntent)
        }

        val task2 = GetData2()
        task2.execute("https://y44v76txl0.execute-api.us-east-2.amazonaws.com/default/BoardPersonalizeGetData")

        binding.homeBoardGridView.setOnItemClickListener { adapterView, view, i, l ->
            val clickedItem = personalizeData[i]
            val myIntent = Intent(context, PersonalizeDetailActivity::class.java)
            myIntent.putExtra("personalize info", clickedItem)
            startActivity(myIntent)
        }

        return binding.root
    }
    internal inner class GetData1: AsyncTask<String?, Void?, String>(){

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)


            var data = JSONObject(result).getString("body")
            var items = JSONObject(data).getJSONArray("Items")
            i = 0

                while(i < 4){
                    val jsonObject = items.getJSONObject(i)

                    val titleType = jsonObject.getString("title")
                    val title = JSONObject(titleType).getString("S")

                    val contentType = jsonObject.getString("content")
                    val content = JSONObject(contentType).getString("S")

                    val timeType = jsonObject.getString("time")
                    val time = JSONObject(timeType).getString("S")

                    val uidType = jsonObject.getString("uid")
                    val uid = JSONObject(uidType).getString("S")

                    boardData.add(BoardModel(title, content, time, uid))
                    i++
                }
                val boardAdapter = BoardAdapter(boardData)
                binding.homeBoardListView.adapter = boardAdapter

                var totalHeight = 0
                for (i in 0 until boardAdapter.getCount()) {
                    val listItem: View = boardAdapter.getView(i, null, binding.homeBoardListView)
                    listItem.measure(0, 0)
                    totalHeight += listItem.measuredHeight
                }
                val params: ViewGroup.LayoutParams = binding.homeBoardListView.getLayoutParams()
                params.height =
                    totalHeight + binding.homeBoardListView.getDividerHeight() * (boardAdapter.getCount() - 1)
                binding.homeBoardListView.setLayoutParams(params)
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

    internal inner class GetData2: AsyncTask<String?, Void?, String>(){

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            //리스트 새로고침
            val items = JSONObject(result).getJSONArray("Items")
            var i = 0
            while(i < 10){
                val jsonObject = items.getJSONObject(i)

                val titleType = jsonObject.getString("title")
                val title = JSONObject(titleType).getString("S")

                val contentType = jsonObject.getString("content")
                val content = JSONObject(contentType).getString("S")

                val categoryType = jsonObject.getString("category")
                val category = JSONObject(categoryType).getString("S")

                val upload_timeType = jsonObject.getString("upload_time")
                val upload_time = JSONObject(upload_timeType).getString("S")

                val uidType = jsonObject.getString("uid")
                val uid = JSONObject(uidType).getString("S")

                val img_urlType = jsonObject.getString("img_url")
                val img_url = JSONObject(img_urlType).getString("S")

                personalizeData.add(PersonalizeModel(title, content, category, upload_time, uid, img_url))
                // uid는 고정적으로 2로 들어가는데 BoardDailyWriteActivity에서 확인 가능함.

                i++
            }
            val personalizeAdapter = PersonalizeAdapter(personalizeData)
            binding.homeBoardGridView.adapter = personalizeAdapter

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