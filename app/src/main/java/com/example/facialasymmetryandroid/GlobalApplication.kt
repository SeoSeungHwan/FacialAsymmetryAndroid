package com.example.facialasymmetryandroid

import android.app.Application
import com.example.facialasymmetryandroid.model.ReturnString

class GlobalApplication : Application() {

    companion object{
        lateinit var returnString : ReturnString
    }

}