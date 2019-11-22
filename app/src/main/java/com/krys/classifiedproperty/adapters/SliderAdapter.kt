package com.krys.classifiedproperty.adapters

import android.content.Context
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.krys.classifiedproperty.R


class SliderAdapter(val context: Context, private val images: ArrayList<Int>) : PagerAdapter() {



    override fun getCount(): Int {
        return images.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val root = LayoutInflater.from(view.context).inflate(R.layout.slide, view, false)
        val myImage = root.findViewById(R.id.image) as ImageView
        myImage.setImageResource(images[position])
        view.addView(root, 0)
        return root
    }

    override fun isViewFromObject(view: View, objt: Any): Boolean {
        return view.equals(objt)
    }

    override fun destroyItem(container: ViewGroup, position: Int, objt: Any) {
        container.removeView(objt as View)
    }
}