package com.krys.classifiedproperty.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.JsonObject
import com.krys.classifiedproperty.R
import com.krys.kotlinamritlife.NetworkCall.AppUtils
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPostDetails : AppCompatActivity() {

    var role: String = ""
    lateinit var fabProgresssub: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post_details)
        supportActionBar?.hide()
        val nsv: NestedScrollView = findViewById(R.id.nsv)
        val owner: TextView = findViewById(R.id.owner)
        val broker: TextView = findViewById(R.id.broker)
        val fabaddpost: FloatingActionButton = findViewById(R.id.fabaddpost)
        val edtTitle: EditText = findViewById(R.id.edtnumdisplay)
        val edtPrice: EditText = findViewById(R.id.edtpassforcreate)
        val edtDescription: EditText = findViewById(R.id.edtconpassforcreate)
        val textName: EditText = findViewById(R.id.name)
        val textNumber: EditText = findViewById(R.id.number)
        val catid = intent.getStringExtra("catid")
        val ccatid = intent.getStringExtra("ccatid")
        val childcat = intent.getStringExtra("childcat")
        val back: ImageView = findViewById(R.id.back)
        back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        val prefs:SharedPreferences = getSharedPreferences("MyPrf", Context.MODE_PRIVATE)
        val userid = prefs.getString("userid", null)
        val name = prefs.getString("username",null)
        val number = prefs.getString("usermobile",null)

        Log.e("name",name+"  "+number)

        textName.setText(name)
        textNumber.setText(number)
        fabProgresssub = findViewById(R.id.fabProgresssub)
//        nsv.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//            if (scrollY > oldScrollY) {
//                fabaddpost.show()
//            } else {
//                fabaddpost.hide()
//            }
//        })
        fabaddpost.setOnClickListener(View.OnClickListener {
            if (edtTitle.text.toString().isEmpty()) {
                Toast.makeText(this@AddPostDetails, "Please enter a title!", Toast.LENGTH_SHORT).show()
            } else if(edtPrice.text.toString().isEmpty()){
                Toast.makeText(this@AddPostDetails, "Please enter price!", Toast.LENGTH_SHORT).show()
            } else if(edtDescription.text.toString().isEmpty()){
                Toast.makeText(this@AddPostDetails, "Please enter description!", Toast.LENGTH_SHORT).show()
            } else if(role.equals("")){
                Toast.makeText(this@AddPostDetails, "Please choose your role!", Toast.LENGTH_SHORT).show()
            } else {
                val editor: SharedPreferences.Editor = this@AddPostDetails.getSharedPreferences("MyPrf", Context.MODE_PRIVATE).edit()
                editor.putString("category_id", catid)
                editor.putString("sub_category_id", ccatid)
                editor.putString("child_category_id", childcat)
                editor.putString("title", edtTitle.text.toString())
                editor.putString("amount", edtPrice.text.toString())
                editor.putString("description", edtDescription.text.toString())
                editor.putString("name", textName.text.toString())
                editor.putString("role", role)
                editor.putString("user_id", userid)
                editor.putString("mobile", textNumber.text.toString())
                editor.apply()
                editor.commit()
                val intent = Intent(this@AddPostDetails, AddPhotos::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            }
 
        })

        owner.setOnClickListener(View.OnClickListener {
            owner.setTextColor(ContextCompat.getColor(this@AddPostDetails, R.color.white))
            owner.setBackgroundResource(R.drawable.radiocheck)
            broker.setTextColor(ContextCompat.getColor(this@AddPostDetails, R.color.black))
            broker.setBackgroundResource(R.drawable.radiouncheck)
            role = "0"
        })
        broker.setOnClickListener(View.OnClickListener {
            owner.setTextColor(ContextCompat.getColor(this@AddPostDetails, R.color.black))
            owner.setBackgroundResource(R.drawable.radiouncheck)
            broker.setTextColor(ContextCompat.getColor(this@AddPostDetails, R.color.white))
            broker.setBackgroundResource(R.drawable.radiocheck)
            role = "1"
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
    }
}
