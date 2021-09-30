package com.example.facialasymmetryandroid

import android.os.Bundle
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
        ultra_viewpager.setInfiniteLoop(true)
        ultra_viewpager.setMultiScreen(0.7f)
        ultra_viewpager.setItemRatio(1.0);
        ultra_viewpager.setAutoMeasureHeight(true);
        ultra_viewpager.setPageTransformer(false,UltraDepthScaleTransformer())
        ultra_viewpager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}

            //Viewpager Page변경 시 설명란 Text 변경
            override fun onPageSelected(position: Int) {
                when(ultra_viewpager.currentItem){
                    0 -> description_tv.text = "resting status"
                    1 -> description_tv.text = "saying E"
                    2 -> description_tv.text = "saying O"
                    3 -> description_tv.text = "slight eye closure"
                    4 -> description_tv.text = "tight eye closure"
                    5 -> description_tv.text = "raised eyebrows"
                }
            }
        })
    }

    //뒤로가기 두번클릭시 나가게 하기
    var mBackWait: Long = 0
    override fun onBackPressed() {
        if (System.currentTimeMillis() - mBackWait >= 2000) {
            mBackWait = System.currentTimeMillis()
            Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else {
            finish()
        }
    }
}