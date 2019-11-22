package com.krys.classifiedproperty.activity

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.krys.classifiedproperty.R
import com.krys.classifiedproperty.utils.GifLoaderClass

class AddPostSucces : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post_succes)
        supportActionBar?.hide()
        val gifsucess:ImageView = findViewById(R.id.gifsucess)
        val fabviewAdd:FloatingActionButton = findViewById(R.id.fabviewAdd)
        val back:ImageView = findViewById(R.id.back)
        back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        Glide.with(this)
            .load(R.raw.sucess)
            .into(GifLoaderClass(gifsucess,1))
        fabviewAdd.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

    }

    override fun onBackPressed() {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        finish()
    }
}