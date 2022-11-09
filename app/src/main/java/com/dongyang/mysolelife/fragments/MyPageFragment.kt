package com.dongyang.mysolelife.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.dongyang.mysolelife.R
import com.dongyang.mysolelife.databinding.FragmentMypageBinding


class MyPageFragment : Fragment() {

    private lateinit var binding : FragmentMypageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false)

        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_myPageFragment_to_homeFragment)
        }

        binding.boardDailyTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_myPageFragment_to_boardDailyFragment)
        }

        binding.talkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_myPageFragment_to_talkFragment)
        }
        return binding.root
    }



}