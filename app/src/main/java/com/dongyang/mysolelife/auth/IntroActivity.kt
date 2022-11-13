package com.dongyang.mysolelife.auth

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Paint.Join
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserState
import com.amazonaws.mobile.client.UserStateDetails
import com.dongyang.mysolelife.MainActivity
import com.dongyang.mysolelife.R
import com.dongyang.mysolelife.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    private lateinit var binding : ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_intro)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro)
//        AWSMobileClient.getInstance()
//            .initialize(applicationContext, object : Callback<UserStateDetails> {
//                override fun onResult(userStateDetails: UserStateDetails) {
//                    Log.i(TAG, userStateDetails.userState.toString())
//
//                    // 로그인이 되어있으면 MainActivity 로 이동
//                    if (userStateDetails.userState == UserState.SIGNED_IN) {
//                        val i = Intent(this@IntroActivity, MainActivity::class.java)
//                        startActivity(i)
//                        finish()
//                    }
//                }
//
//                override fun onError(e: Exception) {
//                    Log.e(TAG, e.toString())
//                }
//            })
        binding.loginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.joinBtn.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

        binding.noAccountBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}