package com.dongyang.mysolelife.fragments

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.amazonaws.mobile.client.AWSMobileClient

import com.dongyang.mysolelife.auth.IntroActivity
import com.dongyang.mysolelife.databinding.FragmentMypageBinding



class MyPageFragment : Fragment(), View.OnClickListener{

    private lateinit var binding : FragmentMypageBinding
    private lateinit var auth:AWSMobileClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ID or 닉네임이 textView에 보이기 위한 함수
        auth = AWSMobileClient.getInstance()
        //유저 이름 전달 안됨 ㅜㅜ
        // username.text = auth.username.toString()

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, com.dongyang.mysolelife.R.layout.fragment_mypage, container, false)

        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(com.dongyang.mysolelife.R.id.action_myPageFragment_to_homeFragment)
        }

        binding.boardDailyTap.setOnClickListener {
            it.findNavController().navigate(com.dongyang.mysolelife.R.id.action_myPageFragment_to_boardDailyFragment)
        }

        binding.talkTap.setOnClickListener {
            it.findNavController().navigate(com.dongyang.mysolelife.R.id.action_myPageFragment_to_talkFragment)
        }
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.button5.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when (v.id) {
            com.dongyang.mysolelife.R.id.button5 -> {
                activity?.let{
                    auth.signOut()
                    val i = Intent(context, IntroActivity::class.java)
                    startActivity(i)
                }

            }
        }



    }
}
