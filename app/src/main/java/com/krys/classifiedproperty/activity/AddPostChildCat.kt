package com.krys.classifiedproperty.activity

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
import com.krys.classifiedproperty.adapters.ChildSubCategoriesAdapter
import com.krys.classifiedproperty.adapters.SubCategoriesAdapter
import com.krys.classifiedproperty.model.ChildSubCatModel
import com.krys.kotlinamritlife.NetworkCall.AppUtils
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPostChildCat : AppCompatActivity() {

    val childsubcatarray:ArrayList<ChildSubCatModel> = ArrayList()
    var value:String = ""
    var childcat:String = ""
    lateinit var fabProgresschildsub: ProgressBar
    lateinit var fabProgress: ProgressBar
    var subcadapter = SubCategoriesAdapter()
    var childsubcadapter = ChildSubCategoriesAdapter()
    lateinit var childsubcategoryrview: RecyclerView
    lateinit var fabaddpostchild: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post_child_cat)
        supportActionBar?.hide()
        childsubcategoryrview = findViewById(R.id.childsubcategoryrview)
        val title: TextView = findViewById(R.id.title)
        fabaddpostchild = findViewById(R.id.fabaddpostchild)
        fabProgresschildsub = findViewById(R.id.fabProgresschildsub)
        childsubcadapter = ChildSubCategoriesAdapter(childsubcatarray)
        val back: ImageView = findViewById(R.id.back)
        back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        childsubcategoryrview.layoutManager = LinearLayoutManager(this@AddPostChildCat)
        childsubcategoryrview.adapter = childsubcadapter
        var titletext = intent.getStringExtra("titletext")
        val catid = intent.getStringExtra("catid")
        val ccatid = intent.getStringExtra("ccatid")
        title.text = titletext+"'s Category"

        Log.e("checkdata1",catid)
        Log.e("checkdata2",ccatid)
        fabaddpostchild.setOnClickListener(View.OnClickListener {
            if(value.equals("")){
                Toast.makeText(this@AddPostChildCat,"Please choose a category !", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this@AddPostChildCat, AddPostDetails::class.java)
                intent.putExtra("catid",catid)
                intent.putExtra("ccatid",ccatid)
                intent.putExtra("childcat",childcat)
                startActivity(intent)
                overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            }
        })
        childsubcadapter.setItemClickListener(object :ChildSubCategoriesAdapter.ItemClickListener{
            override fun onItemClick(view: View, position: Int, subSubcategoryId: String) {
                value = position.toString()
                childcat = subSubcategoryId
            }
        })
        callGetChildSubCategory(catid,ccatid)
    }

    fun callGetChildSubCategory(categoryid:String,subcategoryid:String) {


        Log.e("data",categoryid+"  "+ subcategoryid)
        childsubcatarray.clear()
      //  Toast.makeText(this,"the",Toast.LENGTH_SHORT).show()
        if (AppUtils.isNetworkAvalilable(this@AddPostChildCat)) {
            fabProgresschildsub.visibility = View.VISIBLE
            fabaddpostchild.isClickable = false
            fabaddpostchild.isEnabled = false
            val internalObject = JsonObject()
            internalObject.addProperty("app_key", AppUtils.getApiKey())
            internalObject.addProperty("category_id",categoryid)
            internalObject.addProperty("sub_category_id",subcategoryid)
            val call = AppUtils.service.getchildcategory(internalObject)

            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {


                        val jsob = JSONObject(response.body()!!.toString())

                        Log.e("dataw2",jsob.toString())

                        if(jsob.getString("status").equals("true")){
                            val jsonArray = jsob.getJSONArray("childCategory")

                            Log.e("dataw22",jsob.toString())

                            for (i in 0 until jsonArray.length()) {



                                val jobj = jsonArray.getJSONObject(i)

                                Log.e("checkdata",jsonArray.length().toString())
                                //Log.e("Blogurl",jobj.getString("blog_url"));
                                childsubcatarray.add(
                                    ChildSubCatModel(
                                        jobj.getString("sub_subcategory_id"),
                                        jobj.getString("sub_subcategory_name"),
                                        jobj.getString("sub_subcategory_name_hindi"),
                                        jobj.getString("sub_subcategory_icon"))
                                )
                            }
                            childsubcadapter.notifyDataSetChanged()
                            fabProgresschildsub.visibility = View.INVISIBLE
                            fabaddpostchild.isClickable = true
                            fabaddpostchild.isEnabled = true

                        } else {
                            Toast.makeText(this@AddPostChildCat,jsob.getString("msg"), Toast.LENGTH_SHORT).show()
                            fabProgresschildsub.visibility = View.INVISIBLE
                            fabaddpostchild.isClickable = true
                            fabaddpostchild.isEnabled = true

                        }

                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(this@AddPostChildCat, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fadein,R.anim.fadeout)
    }
}
