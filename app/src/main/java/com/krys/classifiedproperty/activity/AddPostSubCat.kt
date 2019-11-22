package com.krys.classifiedproperty.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
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

class AddPostSubCat : AppCompatActivity() {

    val subcatarray:ArrayList<SubCatModel> = ArrayList()
    lateinit var fabProgresssub: ProgressBar
    lateinit var fabProgress: ProgressBar
    var subcadapter = SubCategoriesAdapter()
    lateinit var subcategoryrview: RecyclerView
    lateinit var fabaddpostsub: FloatingActionButton
    var ccatid:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post_sub_cat)
        supportActionBar?.hide()
        subcategoryrview= findViewById(R.id.subcategoryrview)
        fabaddpostsub = findViewById(R.id.fabaddpostsub)
        fabProgresssub = findViewById(R.id.fabProgresssub)
        subcadapter = SubCategoriesAdapter(subcatarray)
        val back: ImageView = findViewById(R.id.back)
        back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        fabProgresssub.visibility = View.INVISIBLE
        subcategoryrview.layoutManager = LinearLayoutManager(this@AddPostSubCat)
        subcategoryrview.adapter = subcadapter
        var titletext = intent.getStringExtra("titletext")
        val catid = intent.getStringExtra("catid")
        val title:TextView = findViewById(R.id.title)
        title.text = titletext+"'s Category"
        subcadapter.setItemClickListener(object :SubCategoriesAdapter.ItemClickListener{
            override fun onItemClick(view: View, position: Int, title: String, subcategoryId: String) {
                titletext = title
                ccatid = subcategoryId
                Log.e("checkdata211",ccatid+" "+subcategoryId)
            }
        })
        fabaddpostsub.setOnClickListener(View.OnClickListener {

            if(ccatid.equals("")){
                Toast.makeText(this@AddPostSubCat,"Please choose a category !", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this@AddPostSubCat, AddPostChildCat::class.java)
                intent.putExtra("titletext",titletext)
                intent.putExtra("catid",catid)
                intent.putExtra("ccatid",ccatid)
                Log.e("checkdata234",ccatid+" "+catid)
                startActivity(intent)
                overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            }

        })
        callGetSubCategory(catid)
    }

    fun callGetSubCategory(categoryid:String) {
        subcatarray.clear()
        if (AppUtils.isNetworkAvalilable(this@AddPostSubCat)) {
            fabaddpostsub.isClickable = false
            fabaddpostsub.isEnabled = false
            fabProgresssub.visibility = View.VISIBLE
            val internalObject = JsonObject()
            internalObject.addProperty("app_key", AppUtils.getApiKey())
            internalObject.addProperty("category_id",categoryid)
            val call = AppUtils.service.getsubcategory(internalObject)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val jsob = JSONObject(response.body()!!.toString())
                        if(jsob.getString("status").equals("true")){
                            val jsonArray = jsob.getJSONArray("subCategory")
                            for (i in 0 until jsonArray.length()) {

                                val jobj = jsonArray.getJSONObject(i)

                                Log.e("checkdata",jobj.toString())
                                //Log.e("Blogurl",jobj.getString("blog_url"));
                                subcatarray.add(
                                    SubCatModel(
                                        jobj.getString("subcategory_id"),
                                        jobj.getString("subcategory_name"),
                                        jobj.getString("subcategory_name_hindi"),
                                        jobj.getString("subcategory_icon"))
                                )
                            }
                            subcadapter.notifyDataSetChanged()
                            fabProgresssub.visibility = View.INVISIBLE
                            fabaddpostsub.isClickable = true
                            fabaddpostsub.isEnabled = true

                        } else {
                            Toast.makeText(this@AddPostSubCat,jsob.getString("msg"), Toast.LENGTH_SHORT).show()
                            fabProgresssub.visibility = View.INVISIBLE
                            fabaddpostsub.isClickable = true
                            fabaddpostsub.isEnabled = true

                        }

                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(this@AddPostSubCat, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fadein,R.anim.fadeout)
    }
}
