package com.example.facialasymmetryandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    val viewModel = MainViewModel()
    val TAG = "tag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        Glide.with(this@ResultActivity)
            .load(GlobalApplication.returnString.imageBytes)
            .into(result_iv)

        result_percent_tv.text = GlobalApplication.returnString.message.toString()

    }
}