package com.krys.classifiedproperty.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.JsonObject
import com.krys.classifiedproperty.R
import com.krys.classifiedproperty.adapters.CategoriesAdapter
import com.krys.classifiedproperty.adapters.ChildSubCategoriesAdapter
import com.krys.classifiedproperty.adapters.SubCategoriesAdapter
import com.krys.classifiedproperty.model.CatModel
import com.krys.classifiedproperty.model.ChildSubCatModel
import com.krys.classifiedproperty.model.SubCatModel
import com.krys.kotlinamritlife.NetworkCall.AppUtils
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPostCategory : AppCompatActivity() {

    val catarray:ArrayList<CatModel> = ArrayList()
    var titletext:String = ""
    lateinit var fabProgress: ProgressBar
    var cadapter = CategoriesAdapter()
    var catid:String = ""
    lateinit var autoFitRview: RecyclerView
    lateinit var fabaddpost: FloatingActionButton


    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post_category)
        supportActionBar?.hide()
        autoFitRview = findViewById(R.id.categoryrview)
        fabaddpost = findViewById(R.id.fabaddpost)
        fabProgress = findViewById(R.id.fabProgress)
        fabProgress.visibility = View.INVISIBLE
        val back:ImageView = findViewById(R.id.back)
        back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        fabaddpost.setOnClickListener(View.OnClickListener {
            if(catid.equals("")){
                Toast.makeText(this@AddPostCategory,"Please choose a category !", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this@AddPostCategory, AddPostSubCat::class.java)
                intent.putExtra("titletext",titletext)
                intent.putExtra("catid",catid)
                startActivity(intent)
                overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            }
        })
        cadapter = CategoriesAdapter(catarray)
        autoFitRview.layoutManager = GridLayoutManager(this@AddPostCategory, 2)
        autoFitRview.adapter = cadapter
        callGetCategory()
        cadapter.setItemClickListener(object :CategoriesAdapter.ItemClickListener{
            override fun onItemClick(view: View, position: Int, title: String, categoryId: String) {
                titletext = title
                catid = categoryId
            }
        })
    }

    fun callGetCategory() {
        catarray.clear()
        if (AppUtils.isNetworkAvalilable(this@AddPostCategory)) {
            fabaddpost.isClickable = false
            fabaddpost.isEnabled = false
            fabProgress.visibility = View.VISIBLE
            val internalObject = JsonObject()
            internalObject.addProperty("app_key", AppUtils.getApiKey())
            val call = AppUtils.service.getcategory(internalObject)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val jsob = JSONObject(response.body()!!.toString())
                        if(jsob.getString("status").equals("true")){
                            val jsonArray = jsob.getJSONArray("Category")
                            for (i in 0 until jsonArray.length()) {

                                val jobj = jsonArray.getJSONObject(i)

                                Log.e("checkdata",jobj.toString())
                                //Log.e("Blogurl",jobj.getString("blog_url"));
                                catarray.add(
                                    CatModel(
                                        jobj.getString("category_id"),
                                        jobj.getString("category_name"),
                                        jobj.getString("category_name_hindi"),
                                        jobj.getString("category_icon")))
                            }
                            cadapter.notifyDataSetChanged()
                            fabProgress.visibility = View.INVISIBLE
                            fabaddpost.isClickable = true
                            fabaddpost.isEnabled = true

                        } else {
                            Toast.makeText(this@AddPostCategory,jsob.getString("msg"), Toast.LENGTH_SHORT).show()
                            fabProgress.visibility = View.INVISIBLE
                            fabaddpost.isClickable = true
                            fabaddpost.isEnabled = true

                        }

                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(this@AddPostCategory, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fadein,R.anim.fadeout)
    }
}
