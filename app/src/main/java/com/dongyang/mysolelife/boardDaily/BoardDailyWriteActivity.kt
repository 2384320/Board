package com.dongyang.mysolelife.boardDaily

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.*
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.dongyang.mysolelife.R
import com.dongyang.mysolelife.databinding.ActivityBoardDailyWriteBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException

class BoardDailyWriteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBoardDailyWriteBinding
    private val REQUEST_PERMISSIONS = 1

    private var isImageUpload : Boolean = false
    var imgUrl =""
    private val TAG = BoardDailyWriteActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_daily_write)

        val view = binding.root
        setContentView(view)

        val spin = binding.spinner
        spin.adapter = ArrayAdapter.createFromResource(this, R.array.types, android.R.layout.simple_spinner_item)


        binding.imgUploadBtn.setOnClickListener {
            var permission = mutableMapOf<String, String>()
            permission["camera"] = Manifest.permission.CAMERA
            permission["storageRead"] = Manifest.permission.READ_EXTERNAL_STORAGE
            permission["storageWrite"] =  Manifest.permission.WRITE_EXTERNAL_STORAGE

            var denied = permission.count { ContextCompat.checkSelfPermission(this, it.value)  == PackageManager.PERMISSION_DENIED }

            if (denied > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permission.values.toTypedArray(), REQUEST_PERMISSIONS)
            }

            var gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery,100)

            isImageUpload = true
        }

        binding.writeBtn.setOnClickListener {

            val title = binding.titleArea.text.toString()
            val content = binding.contentArea.text.toString()
//            승연님 uid 받아 넣기, time은 람다에서 받아 넣고있긴하지만 여기서 넣을수 있으면 그래도 됨 // 지금은 임의값 입력
//            val uid = FBAuth.getUid()
//            var time = FBAuth.getTime()
            val time = "1"
            val uid = "2"

            if(isImageUpload){

            }

            val task:InsertData= InsertData()
            task.execute("https://ldxhg1ute8.execute-api.us-east-2.amazonaws.com/default/BoardDailyWrite", title, content, time, uid)

            Log.d(TAG,"input Title: $title, Content: $content")
            Toast.makeText(this,"게시글 작성 완료", Toast.LENGTH_LONG).show()

            finish()
        }
    }

    internal inner class InsertData: AsyncTask<String?, Void?, String>(){

        protected override fun doInBackground(vararg params: String?): String
        {
            val serverURL = params[0]
            val title = params[1]
            val content = params[2]
            val time = params[3]
            val uid = params[4]
            val jsonObject = JSONObject()

            Log.d(TAG, "POST response - back - $title $content $time $uid $imgUrl")
            try {
                jsonObject.put("title", title)
                jsonObject.put("content", content)
                jsonObject.put("time", time)
                jsonObject.put("uid", uid)
                jsonObject.put("img_url", imgUrl)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            val client = OkHttpClient()
            val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
            val body = RequestBody.create(JSON, jsonObject.toString())
            val request = Request.Builder().url(serverURL.toString()).post(body).build()
            var response: Response? = null

            imgUrl = ""
            return try {
                response = client.newCall(request).execute()
                response.body!!.string()

            } catch (e: IOException) {
                Log.e(TAG, "POST ERROR -$e")
                e.printStackTrace()
                e.toString()
            }
        }
    }
    // 이미지 실제 경로 반환
    fun getRealPathFromURI(uri: Uri): String {

        val buildName = Build.MANUFACTURER
        if (buildName.equals("Xiaomi")) {
            return uri.path!!
        }
        var columnIndex = 0
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        val result = cursor.getString(columnIndex)
        cursor.close()
        return result
    }
    fun imageUpload(fileName: String, file: File){

        val imageView = binding.imgArea
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()

        val awsCredentials: AWSCredentials =
            BasicAWSCredentials("AKIA3YG7XMLNIHT6SMMD", "tV2qiXkmU+zJlnIZ6iGfwY76Qny/ox371GtvXznK") // IAM 생성하며 받은 것 입력

        val s3Client: AmazonS3Client =
            AmazonS3Client(awsCredentials, Region.getRegion(Regions.US_EAST_2))

        val transferUtility = TransferUtility.builder().s3Client(s3Client)
            .context(applicationContext).build()

        TransferNetworkLossHandler.getInstance(applicationContext)

        val uploadObserver: TransferObserver = transferUtility.upload(
            "s3-board-daily-img/img",
            fileName,
            file
        ) // (bucket api, file이름, file객체)


        imgUrl = "https://s3-board-daily-img.s3.us-east-2.amazonaws.com/img/$fileName"
        Log.d("upload - url", imgUrl)
        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state === TransferState.COMPLETED) {
                    // Handle a completed upload
                }
            }

            override fun onProgressChanged(id: Int, current: Long, total: Long) {
                val done = (current.toDouble() / total * 100.0).toInt()
                Log.d("MYTAG", "UPLOAD - - ID: $id, percent done = $done")
            }

            override fun onError(id: Int, ex: Exception) {
                Log.d("MYTAG", "UPLOAD ERROR - - ID: $id - - EX:$ex")
            }
        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100){
            binding.imgArea.setImageURI(data?.data)
            Log.d(ContentValues.TAG,"img_url -1 $data")

            var temp: Uri? = data?.data
            Log.d(ContentValues.TAG,"img_url -2 $temp")

            var file = File(temp?.let { getRealPathFromURI(it) })
            Log.d("upload - file:", file.toString())

            //imageUpload 얘를 글쓰기 버튼 눌르면 함수 실행해야되는데 여기서 하는게 문제입니다 ㅇㅅㅇ// 현재 이미지업로드버튼으로 사진고르면 s3에 올라감
            imageUpload(file.getName(), file)

        }
    }
}

