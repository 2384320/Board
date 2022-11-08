package com.dongyang.mysolelife.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.dongyang.mysolelife.MainActivity
import com.dongyang.mysolelife.R
import com.dongyang.mysolelife.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)

        binding.loginBtn.setOnClickListener {
            // val email = binding.emailArea.text.toString()
            // val password = binding.passwordArea.text.toString()

            // 권한 부여 후 로그인 성공시와 실패시로 나눠서 넣기, 우선은 로그인하기 버튼만 눌러도 이동되게끔 함함
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            Toast.makeText(this,"로그인 성공", Toast.LENGTH_LONG).show()
        }
   }
}