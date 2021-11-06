package com.example.facialasymmetryandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        Glide.with(this@ResultActivity)
            .load(GlobalApplication.returnString.imageBytes)
            .into(result_iv)

        final_result_tv.text = GlobalApplication.returnString.message.toString()+"%"

        val linearLayoutMangerWrapper = LinearLayoutManager(this@ResultActivity,
            RecyclerView.VERTICAL,
            false
        )
        val adapter = ResultDistanceRecyclerViewAdapter(GlobalApplication.returnString.distance!!)
        distance_rc.layoutManager = linearLayoutMangerWrapper
        distance_rc.adapter = adapter

        restart_btn.setOnClickListener {
            val intent = Intent(this@ResultActivity,SelectMenu::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}