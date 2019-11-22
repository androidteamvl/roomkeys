package com.krys.classifiedproperty.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import com.goodiebag.pinview.Pinview
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.JsonObject
import com.krys.classifiedproperty.R
import com.krys.kotlinamritlife.NetworkCall.AppUtils
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class OTPandCreateProfile : AppCompatActivity() {

    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    lateinit var edtdob: TextView
    val DATE_PICKER_ID = 1111
    var otp:String = ""
    var gender:Int = -1
    lateinit var fabProgressotp:ProgressBar
    lateinit var fabProgress:ProgressBar
    lateinit var fabProgresscreate:ProgressBar
    internal var cTimer: CountDownTimer? = null
    var otptomatch:String = ""
    var login_otp:String = ""



    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val layforpass:RelativeLayout = findViewById(R.id.layforpass)
        val layforotp:RelativeLayout = findViewById(R.id.layforotp)
        val layforcreate:RelativeLayout = findViewById(R.id.layforcreate)
        val btnforpass: FloatingActionButton = findViewById(R.id.nextbtnpass)
        val edtpass: EditText = findViewById(R.id.edtpass)
        val edtnumdisplay: EditText = findViewById(R.id.edtnumdisplay)
        val edtpassforcreate: EditText = findViewById(R.id.edtpassforcreate)
        val btnforotp:FloatingActionButton = findViewById(R.id.nextbtnotp)
        val btnforcreate:FloatingActionButton = findViewById(R.id.nextbtncreate)
        val forgotpass:TextView = findViewById(R.id.forgotpass)
        val text51:TextView = findViewById(R.id.text51)
        val resend:TextView = findViewById(R.id.resend)
        fabProgress = findViewById(R.id.fabProgress)
        fabProgresscreate = findViewById(R.id.fabProgresscreate)
        fabProgressotp = findViewById(R.id.fabProgressotp)
        fabProgressotp.visibility = View.INVISIBLE
        fabProgress.visibility = View.INVISIBLE
        fabProgresscreate.visibility = View.INVISIBLE
        val male:TextView = findViewById(R.id.male)
        val female:TextView = findViewById(R.id.female)
        val pinview:Pinview = findViewById(R.id.pinview)
        val getValue:String? = intent.getStringExtra("exist")
        if(intent.getStringExtra("login_otp") !=null){
            login_otp = intent.getStringExtra("login_otp")
        }
        val preference: SharedPreferences = getSharedPreferences("MyPrf", Context.MODE_PRIVATE)
        val mobileno = preference.getString("mobileno",null)
        val mask = mobileno!!.replace("(?<!^.?).(?!.?$)".toRegex(), "*")
        val spanstring = SpannableString("Please type the verification code sent to +91 $mask. Wrong Number?")

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@OTPandCreateProfile, Login::class.java)
                intent.putExtra("number",mobileno)
                startActivity(intent)
                overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                finish()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.setUnderlineText(false)
            }
        }
        spanstring.setSpan(ForegroundColorSpan(Color.RED), 58, 71, 0)
        spanstring.setSpan(BackgroundColorSpan(Color.WHITE), 58, 71, 0)
        spanstring.setSpan(clickableSpan, 58, 71, 0)
        text51.movementMethod = LinkMovementMethod.getInstance()
        text51.setText(spanstring)
        if(login_otp!=null){
            otp = login_otp
        }
        if(getValue.equals("yes")){
            layforpass.visibility = View.VISIBLE
            layforotp.visibility = View.GONE
        } else {
            layforpass.visibility = View.GONE
            layforotp.visibility = View.VISIBLE
        }

        resend.setOnClickListener(View.OnClickListener {
            if (AppUtils.isNetworkAvalilable(this)) {
                val internalObject = JsonObject()
                internalObject.addProperty("app_key", AppUtils.getApiKey())
                internalObject.addProperty("mobile",mobileno)
                val call = AppUtils.service.checklogin(internalObject)
                call.enqueue(object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful) {
                            val jsob = JSONObject(response.body()!!.toString())
                            if(jsob.getString("status").equals("true")){
                                login_otp = jsob.getString("login_otp")
                                Toast.makeText(this@OTPandCreateProfile, "OTP resend, please check your mobile now.", Toast.LENGTH_SHORT).show()
                                cTimer = object : CountDownTimer(30000, 1000) {

                                    override fun onTick(millisUntilFinished: Long) {
                                        resend.setEnabled(false)
                                        resend.setText("Resend OTP in " + millisUntilFinished / 1000 + " seconds")
                                        //here you can have your logic to set text to edittext
                                    }

                                    override fun onFinish() {
                                        resend.setEnabled(true)
                                        resend.setText("Resend")
                                    }

                                }.start()
                            } else {
                                Toast.makeText(this@OTPandCreateProfile, "Failed! Please again later.", Toast.LENGTH_SHORT).show()

                            }

                        }
                    }
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        Toast.makeText(this@OTPandCreateProfile, t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        })
        cTimer = object : CountDownTimer(30000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                resend.setEnabled(false)
                resend.setText("Resend OTP in " + millisUntilFinished / 1000 + " seconds")
                //here you can have your logic to set text to edittext
            }

            override fun onFinish() {
                resend.setEnabled(true)
                resend.setText("Resend")
            }

        }.start()

        forgotpass.setOnClickListener(View.OnClickListener {
            callApiForgotPass(mobileno)
        })
        //pinview.requestPinEntryFocus()
        supportActionBar?.hide()

        edtdob = findViewById(R.id.edtconpassforcreate)
        val c: Calendar = Calendar.getInstance();
        year  = 2005
        month = 0
        day   = 1
        val shake23 = AnimationUtils.loadAnimation(this@OTPandCreateProfile, R.anim.fadeout)
        val shake223 = AnimationUtils.loadAnimation(this@OTPandCreateProfile, R.anim.fadein)
        pinview.setTextColor(getResources().getColor(R.color.black));
        pinview.setPinViewEventListener(Pinview.PinViewEventListener { pinview, fromUser ->
            val comotp: String? = pinview.value
            if (otp.equals(comotp)) {
                layforotp.animation = shake23
                layforotp.visibility = View.GONE
                layforcreate.animation = shake223
                layforcreate.visibility = View.VISIBLE
                hideKeyboardforview(this@OTPandCreateProfile,pinview)
            } else {

                val va = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    va.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    va.vibrate(500)
                }
                val shake = AnimationUtils.loadAnimation(this@OTPandCreateProfile, R.anim.shake)
                pinview.startAnimation(shake)
                Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
            }
        })
        edtdob.setOnClickListener(View.OnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                edtdob.setText(StringBuilder().append(month + 1).append("-").append(day).append("-").append(year).append(" "))
            }, year, month, day)
            dpd.getDatePicker().setMaxDate(System.currentTimeMillis())
            dpd.show()


        })
        val type = preference.getString("type",null)
        if(type.equals("new")){
            layforpass.visibility = View.GONE
            layforotp.visibility = View.VISIBLE
        }
        btnforpass.setOnClickListener(View.OnClickListener {
            if(edtpass.text.toString().isEmpty()){
                Toast.makeText(this@OTPandCreateProfile, "Password can't be empty!", Toast.LENGTH_SHORT).show()
            } else {
                callPassApi(mobileno,edtpass.text.toString())
            }
        })
        male.setOnClickListener(View.OnClickListener {
            male.setTextColor(ContextCompat.getColor(this@OTPandCreateProfile,R.color.white))
            male.setBackgroundResource(R.drawable.radiocheck)
            female.setTextColor(ContextCompat.getColor(this@OTPandCreateProfile,R.color.black))
            female.setBackgroundResource(R.drawable.radiouncheck)
            gender = 0
        })
        female.setOnClickListener(View.OnClickListener {
            male.setTextColor(ContextCompat.getColor(this@OTPandCreateProfile,R.color.black))
            male.setBackgroundResource(R.drawable.radiouncheck)
            female.setTextColor(ContextCompat.getColor(this@OTPandCreateProfile,R.color.white))
            female.setBackgroundResource(R.drawable.radiocheck)
            gender = 1
        })
        btnforotp.setVisibility(View.GONE)
        btnforcreate.setOnClickListener(View.OnClickListener {
            if(edtpassforcreate.text.toString().isEmpty()){
                Toast.makeText(this@OTPandCreateProfile, "Password can't be empty!", Toast.LENGTH_SHORT).show()
            } else if(edtdob.text.toString().isEmpty()){
                Toast.makeText(this@OTPandCreateProfile, "Date of birth can't be empty!", Toast.LENGTH_SHORT).show()
            } else if(gender.equals(-1)){
                Toast.makeText(this@OTPandCreateProfile, "Please choose your gender", Toast.LENGTH_SHORT).show()
            } else {
                callCreateAcApi(edtpassforcreate.text.toString(),mobileno,edtdob.text.toString(),gender,edtnumdisplay.text.toString())
            }
        })
        hideKeyboard(this@OTPandCreateProfile)

    }

    override fun onDestroy() {
        super.onDestroy()
        hideKeyboard(this@OTPandCreateProfile)
    }

    override fun onStop() {
        super.onStop()
        hideKeyboard(this@OTPandCreateProfile)
    }


    fun hideKeyboardforview(activity: Activity,view: View) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun callCreateAcApi(password:String, mobileno: String,dob:String,gender:Int,name:String) {
        if (AppUtils.isNetworkAvalilable(this)) {
            fabProgresscreate.visibility = View.VISIBLE
            val internalObject = JsonObject()
            internalObject.addProperty("app_key", AppUtils.getApiKey())
            internalObject.addProperty("mobile", mobileno)
            internalObject.addProperty("password", password)
            internalObject.addProperty("dob", dob)
            internalObject.addProperty("name", name)
            internalObject.addProperty("gender", gender)
            val call = AppUtils.service.insertmobile(internalObject)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val jsob = JSONObject(response.body()!!.toString())
                        if(jsob.getString("status").equals("true")){
                            fabProgresscreate.visibility = View.INVISIBLE
                            Toast.makeText(this@OTPandCreateProfile, jsob.getString("msg"), Toast.LENGTH_SHORT).show()
                            val editor: SharedPreferences.Editor = this@OTPandCreateProfile.getSharedPreferences("MyPrf", Context.MODE_PRIVATE).edit()
                            editor.putString("userid", jsob.getString("user_id"))
                            editor.apply()
                            editor.commit()
                            val intent = Intent(this@OTPandCreateProfile, Home::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                            finish()
                        } else {
                            fabProgresscreate.visibility = View.INVISIBLE
                            Toast.makeText(this@OTPandCreateProfile, jsob.getString("msg"), Toast.LENGTH_SHORT).show()

                        }

                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(this@OTPandCreateProfile, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun callPassApi(mobile:String?, password: String?) {
        if (AppUtils.isNetworkAvalilable(this)) {
            fabProgress.visibility = View.VISIBLE
            val internalObject = JsonObject()
            internalObject.addProperty("app_key", AppUtils.getApiKey())
            internalObject.addProperty("mobile", mobile)
            internalObject.addProperty("password", password)
            val call = AppUtils.service.userlogin(internalObject)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val jsob = JSONObject(response.body()!!.toString())
                        if(jsob.getString("status").equals("true")){
                            //Toast.makeText(this@OTPandCreateProfile,"Hey",Toast.LENGTH_SHORT).show()
                            fabProgress.visibility = View.INVISIBLE

                            val preference=getSharedPreferences("MyPrf", Context.MODE_PRIVATE)
                            val editor=preference.edit()
                            editor.putString("userid", jsob.getString("user_id"))
                            editor.putString("username",jsob.getString("name"))
                            editor.putString("usermobile",jsob.getString("mobile"))
                            editor.commit()
                            val intent = Intent(this@OTPandCreateProfile, Home::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                            finish()
                        } else {
                            Toast.makeText(this@OTPandCreateProfile,jsob.getString("msg"),Toast.LENGTH_SHORT).show()
                            fabProgress.visibility = View.INVISIBLE

                        }

                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(this@OTPandCreateProfile, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun callApiForgotPass(mobile:String) {
        if (AppUtils.isNetworkAvalilable(this)) {
            fabProgress.visibility = View.VISIBLE
            val internalObject = JsonObject()
            internalObject.addProperty("app_key", AppUtils.getApiKey())
            internalObject.addProperty("mobile", mobile)
            val call = AppUtils.service.sendotpforgotpassword(internalObject)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val jsob = JSONObject(response.body()!!.toString())
                        if(jsob.getString("status").equals("true")){
                            fabProgress.visibility = View.INVISIBLE
                            otptomatch = jsob.getString("otp")
                            val intent = Intent(this@OTPandCreateProfile, ForgotPassword::class.java)
                            intent.putExtra("mobileno",mobile)
                            intent.putExtra("otp",otptomatch)
                            startActivity(intent)
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                            finish()
                           // Toast.makeText(this@OTPandCreateProfile, "OTP resend, please check your mobile now.", Toast.LENGTH_SHORT).show()
                        } else {
                            fabProgress.visibility = View.INVISIBLE
                            Toast.makeText(this@OTPandCreateProfile, "Failed! Please try again later.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(this@OTPandCreateProfile, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val editor: SharedPreferences.Editor = this@OTPandCreateProfile.getSharedPreferences("MyPrf", Context.MODE_PRIVATE).edit()
        editor.putString("back", "fromotp")
        editor.apply()
        editor.commit()
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
    }
}
