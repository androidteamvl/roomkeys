package com.krys.classifiedproperty.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.krys.classifiedproperty.R
import com.krys.classifiedproperty.adapters.FilterDataAdapter

class FilterResult : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_result)
        supportActionBar?.hide()
        val filterdata:RecyclerView = findViewById(R.id.filterdata)
        val filterDataAdapter = FilterDataAdapter()
        filterdata.layoutManager = LinearLayoutManager(this@FilterResult)
        filterdata.adapter = filterDataAdapter
        val back: ImageView = findViewById(R.id.back)
        back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
    }
}
