package com.example.facialasymmetryandroid

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import com.tmall.ultraviewpager.UltraViewPager
import com.tmall.ultraviewpager.transformer.UltraDepthScaleTransformer
import kotlinx.android.synthetic.main.activity_select_menu.*
import kotlinx.android.synthetic.main.type_item.*
import kotlinx.android.synthetic.main.type_item.view.*


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
            .setFocusColor(R.color.main)
            .setNormalColor(Color.WHITE)
            .setRadius(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)
                    .toInt()
            )

        ultra_viewpager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)
        ultra_viewpager.getIndicator().build()
        ultra_viewpager.setInfiniteLoop(true)

        ultra_viewpager.setMultiScreen(0.5f)
        ultra_viewpager.setItemRatio(1.0);
        ultra_viewpager.setAutoMeasureHeight(true);

        ultra_viewpager.setPageTransformer(false,UltraDepthScaleTransformer())

        //todo 타입 선택 시 해당 타입으로 테스트창으로 이동



    }
}