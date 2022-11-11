package com.dongyang.mysolelife.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.mobile.auth.userpools.SignUpActivity
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.results.SignUpResult
import com.dongyang.mysolelife.R


class OkActivity : AppCompatActivity() {
    var TAG: String = IntroActivity::class.java.getSimpleName()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ok)

        // 인증 확인버튼
        val Ok_button = findViewById<Button>(R.id.Ok_button)

        // SingUpActivity 에서 사용된 username 정보를 가져와 TextView에 넣는다.
        val TextView = findViewById<TextView>(R.id.signUpUsername2)
        val intent = intent
        val bundle = intent.extras
        val username = bundle!!.getString("email")
        TextView.text = username

        // 인증 버튼
        Ok_button.setOnClickListener {
            val code_name = findViewById<EditText>(R.id.code_name)
            val code = code_name.text.toString()
            AWSMobileClient.getInstance().confirmSignUp(
                username,
                code,
                object :
                    Callback<SignUpResult> {
                    override fun onResult(signUpResult: SignUpResult) {
                        runOnUiThread {
                            Log.d(
                                TAG,
                                "Sign-up callback state: " + signUpResult.confirmationState
                            )
                            if (!signUpResult.confirmationState) {
                                val details = signUpResult.userCodeDeliveryDetails
                                Toast.makeText(
                                    applicationContext,
                                    "Confirm sign-up with: " + details.destination,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {

                                // 회원가입이 완료되면 로그인 창으로 이동
                                Toast.makeText(
                                    applicationContext,
                                    "성공적으로 회원가입 되셨습니다..",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val i = Intent(this@OkActivity, IntroActivity::class.java)
                                startActivity(i)
                                finish()
                            }
                        }
                    }

                    override fun onError(e: Exception) {
                        Log.e(TAG, "Confirm sign-up error", e)
                    }
                })
        }
    }

    // 뒤로가기 2번 눌러야 종료
    private val FINISH_INTERVAL_TIME: Long = 1000
    private var backPressedTime: Long = 0
    override fun onBackPressed() {
        val tempTime = System.currentTimeMillis()
        val intervalTime = tempTime - backPressedTime

        // 뒤로 가기 할 경우 SignActivity 화면으로 이동
        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            val i = Intent(this@OkActivity, SignUpActivity::class.java)
            startActivity(i)
            finish()
        } else {
            backPressedTime = tempTime
            Toast.makeText(applicationContext, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
    }
}