package com.example.facialasymmetryandroid

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)


        val imageBytes = intent.getStringExtra("imageBytes")
        val message = intent.getStringExtra("message")
        result_percent_tv.text = message

        Log.d("Test", "onCreate: ${imageBytes}")

        //val encodeByte = android.util.Base64.decode(imageBytes, android.util.Base64.DEFAULT)
        //val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        //result_iv.setImageBitmap(bitmap)
    }
}