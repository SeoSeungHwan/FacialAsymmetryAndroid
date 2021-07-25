package com.example.facialasymmetryandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_menu_select.*

class MenuSelect : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_select)

        linearLayout1.setOnClickListener(ButtonListener())
        linearLayout2.setOnClickListener(ButtonListener())
        linearLayout3.setOnClickListener(ButtonListener())
        linearLayout4.setOnClickListener(ButtonListener())
        linearLayout5.setOnClickListener(ButtonListener())
        linearLayout6.setOnClickListener(ButtonListener())

    }
    inner class ButtonListener: View.OnClickListener{
        override fun onClick(v: View?) {
            var typeStr = ""

            when(v?.id){
                R.id.linearLayout1 -> {
                    typeStr = "type1"
                }
                R.id.linearLayout2 -> {
                    typeStr = "type2"
                }
                R.id.linearLayout3 -> {
                    typeStr = "type3"
                }
                R.id.linearLayout4 -> {
                    typeStr = "type4"
                }
                R.id.linearLayout5 -> {
                    typeStr = "type5"
                }
                R.id.linearLayout6 -> {
                    typeStr = "type6"
                }
            }
            var intent = Intent(this@MenuSelect, MainActivity::class.java)
            intent.putExtra("type",typeStr)
            startActivity(intent)

        }
    }
}