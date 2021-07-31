package com.example.facialasymmetryandroid


import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.tmall.ultraviewpager.UltraViewPager
import com.tmall.ultraviewpager.transformer.UltraDepthScaleTransformer
import kotlinx.android.synthetic.main.activity_select_menu.*


class SelectMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_menu)

        val adapter = SelectMenuViewPagerAdapter(this)

        ultra_viewpager.adapter = adapter
        ultra_viewpager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL)
        ultra_viewpager.initIndicator()
        ultra_viewpager.getIndicator()
            .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
            .setFocusColor(Color.YELLOW)
            .setNormalColor(Color.WHITE)
            .setRadius(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)
                    .toInt()
            )
        ultra_viewpager.indicator.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)
        ultra_viewpager.indicator.build()
        ultra_viewpager.indicator.setMargin(0,0,0,50)
        ultra_viewpager.setInfiniteLoop(true)
        ultra_viewpager.setMultiScreen(0.6f)
        ultra_viewpager.setItemRatio(1.0);
        ultra_viewpager.setAutoMeasureHeight(true);
        ultra_viewpager.setPageTransformer(false,UltraDepthScaleTransformer())

        //todo type변경 시 해당 type에 대한 설명이 textview에 나타나게 해야함




    }
}