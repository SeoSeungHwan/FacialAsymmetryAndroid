package com.example.facialasymmetryandroid


import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
        /*ultra_viewpager.initIndicator()
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
        ultra_viewpager.indicator.setMargin(0,0,0,50)*/
        ultra_viewpager.setInfiniteLoop(true)
        ultra_viewpager.setMultiScreen(0.7f)
        ultra_viewpager.setItemRatio(1.0);
        ultra_viewpager.setAutoMeasureHeight(true);
        ultra_viewpager.setPageTransformer(false,UltraDepthScaleTransformer())


        ultra_viewpager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
            //viewpager변경 시 설명 란 변경
            override fun onPageSelected(position: Int) {
                val descriptions = arrayOf(
                    "resting status",
                    "saying E",
                    "saying O",
                    "slight eye closure",
                    "tight eye closure",
                    "raised eyebrows"
                )
                when(ultra_viewpager.currentItem){
                    0 -> description_tv.text = descriptions.get(0)
                    1 -> description_tv.text = descriptions.get(1)
                    2 -> description_tv.text = descriptions.get(2)
                    3 -> description_tv.text = descriptions.get(3)
                    4 -> description_tv.text = descriptions.get(4)
                    5 -> description_tv.text = descriptions.get(5)
                }

            }

        })
    }
}