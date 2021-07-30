package com.example.facialasymmetryandroid

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.tmall.ultraviewpager.transformer.UltraDepthScaleTransformer

class SelectMenuViewPagerAdapter(private val context : Context) : PagerAdapter(){

    private var layoutInflater : LayoutInflater? = null

    private var images = arrayOf(
        R.drawable.ic_crazy,
        R.drawable.ic_crazy,
        R.drawable.ic_crazy,
        R.drawable.ic_crazy,
        R.drawable.ic_crazy,
        R.drawable.ic_crazy
    )
    private var texts = arrayOf(
        "Type1",
        "Type2",
        "Type3",
        "Type4",
        "Type5",
        "Type6"
    )

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int = images.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater!!.inflate(R.layout.type_item,null)
        val image = view.findViewById<View>(R.id.type_iv) as ImageView
        val textview = view.findViewById<View>(R.id.type_tv) as TextView

        image.setImageResource(images[position])
        image.setOnClickListener {
            var intent = Intent(context, MainActivity::class.java)
            intent.putExtra("type",textview.text.toString())
            context.startActivity(intent)
        }
        textview.text = texts[position]
        val viewpager = container as ViewPager
        viewpager.addView(view,0)
        viewpager.setPageTransformer(true,UltraDepthScaleTransformer())

        return view
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        val view  = `object` as View
        viewPager.removeView(view)
    }


}