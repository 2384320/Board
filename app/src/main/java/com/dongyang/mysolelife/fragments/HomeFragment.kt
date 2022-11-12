package com.dongyang.mysolelife.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.dongyang.mysolelife.R
import com.dongyang.mysolelife.board.BoardAdapter
import com.dongyang.mysolelife.board.BoardModel
import com.dongyang.mysolelife.databinding.FragmentHomeBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("HomeFragment","onCreateView")
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


        val task: GetData = GetData()
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
            while( i < 3){
                val jsonObject = items.getJSONObject(i)
                val titleType = jsonObject.getString("title")
                val title = JSONObject(titleType).getString("S")
                val contentType = jsonObject.getString("content")
                val content = JSONObject(contentType).getString("S")

               when(i){
                   0 -> binding.board1.setText(title)
                   1 -> binding.board2.setText(title)
                   2 -> binding.board3.setText(title)
               }


                i++
            }


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