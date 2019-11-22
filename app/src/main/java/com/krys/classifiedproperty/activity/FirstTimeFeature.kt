package com.krys.classifiedproperty.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.krys.classifiedproperty.R
import com.krys.classifiedproperty.adapters.FeatureAdapter
import java.util.ArrayList

class FirstTimeFeature : AppCompatActivity() {

    var imagearray: ArrayList<Int> = ArrayList()
    var namearray: ArrayList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_time_feature)
        supportActionBar?.hide()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        val homelist: RecyclerView = findViewById(R.id.homelist)
        val fabfirst: FloatingActionButton = findViewById(R.id.fabfirst)
        imagearray.add(R.drawable.zerofee)
        imagearray.add(R.drawable.leaf)
        imagearray.add(R.drawable.energy)
        imagearray.add(R.drawable.graph)
        imagearray.add(R.drawable.tree)
        imagearray.add(R.drawable.bus)
        namearray.add("No extra charges")
        namearray.add("Eco Friendly Enviroment")
        namearray.add("23 Hours Electricity supply")
        namearray.add("Full Jio Network")
        namearray.add("Garden and Parks in near by areas.")
        namearray.add("Free city bus service")
        homelist.layoutManager = LinearLayoutManager(this)
        val featureAdapter = FeatureAdapter(imagearray,namearray)
        homelist.adapter = featureAdapter
        fabfirst.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, Login::class.java)
            intent.putExtra("number","")
            startActivity(intent)
            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            finish()
        })
    }
}
