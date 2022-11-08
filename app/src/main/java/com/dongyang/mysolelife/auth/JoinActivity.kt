package com.dongyang.mysolelife.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.dongyang.mysolelife.MainActivity
import com.dongyang.mysolelife.R
import com.dongyang.mysolelife.databinding.ActivityJoinBinding

class JoinActivity : AppCompatActivity() {

    private lateinit var binding : ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)

        binding.joinBtn.setOnClickListener {

            var isGotoJoin = true

            val email = binding.emailArea.text.toString()
            val password1 = binding.passwordArea1.text.toString()
            val password2 = binding.passwordArea2.text.toString()

            // 저기 값이 비어있는지 확인
            if(email.isEmpty()) {
                Toast.makeText(this,"이메일을 입력해주세요.",Toast.LENGTH_LONG).show()
                isGotoJoin = false
            }

            if(password1.isEmpty()){
                Toast.makeText(this,"Password1을 입력해주세요.",Toast.LENGTH_LONG).show()
                isGotoJoin = false
            }

            if(password2.isEmpty()){
                Toast.makeText(this,"Password2을 입력해주세요.",Toast.LENGTH_LONG).show()
                isGotoJoin = false
            }
            // 비밀번호 재확인
            if(!password1.equals(password2)) {
                Toast.makeText(this,"비밀번호를 똑같이 입력해주세요.",Toast.LENGTH_LONG).show()
                isGotoJoin = false
            }
            // 비밀번호가 6자리 이상
            if(password1.length < 6) {
                Toast.makeText(this,"비밀번호를 6자리 이상으로 입력해주세요.",Toast.LENGTH_LONG).show()
                isGotoJoin = false
            }
            // 회원 가입 성공할 경우의 조건문
            if(isGotoJoin) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }
}