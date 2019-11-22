package com.krys.classifiedproperty.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.*
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.goodiebag.pinview.Pinview
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.JsonObject
import com.krys.classifiedproperty.R
import com.krys.kotlinamritlife.NetworkCall.AppUtils
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPassword : AppCompatActivity() {

    lateinit var fabProgressotp: ProgressBar
    internal var cTimer: CountDownTimer? = null
    var otptomatch:String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        supportActionBar?.hide()
        val text51:TextView = findViewById(R.id.text51)
        val btnforotp: FloatingActionButton = findViewById(R.id.nextbtnotp)
        val resend: TextView = findViewById(R.id.resend)
        fabProgressotp = findViewById(R.id.fabProgressotp)
        fabProgressotp.visibility = View.INVISIBLE
        val pinview: Pinview = findViewById(R.id.pinview)
        val mobileno: String = intent.getStringExtra("mobileno")
        otptomatch = intent.getStringExtra("otp")
        val mask = mobileno.replace("(?<!^.?).(?!.?$)".toRegex(), "*")
        val spanstring = SpannableString("Please type the verification code sent to +91 $mask. Wrong Number?")

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@ForgotPassword, Login::class.java)
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
        pinview.setTextColor(getResources().getColor(R.color.black));
        pinview.setPinViewEventListener(Pinview.PinViewEventListener { pinview, fromUser ->
            val comotp: String? = pinview.value
            if (otptomatch.equals(comotp)) {
                val intent = Intent(this@ForgotPassword, ChangePassword::class.java)
                intent.putExtra("mobileno",mobileno)
                startActivity(intent)
                overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            } else {
                val va = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    va.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    va.vibrate(500)
                }
                val shake = AnimationUtils.loadAnimation(this@ForgotPassword, R.anim.shake)
                pinview.startAnimation(shake)
                Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
            }
        })
        resend.setOnClickListener(View.OnClickListener {
            callApiForgotPass(mobileno)
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
    }

    fun callApiForgotPass(mobile:String) {
        if (AppUtils.isNetworkAvalilable(this)) {
            val internalObject = JsonObject()
            internalObject.addProperty("app_key", AppUtils.getApiKey())
            internalObject.addProperty("mobile", mobile)
            val call = AppUtils.service.sendotpforgotpassword(internalObject)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val jsob = JSONObject(response.body()!!.toString())
                        if(jsob.getString("status").equals("true")){
                            otptomatch = jsob.getString("otp")
                             Toast.makeText(this@ForgotPassword, "OTP resend, please check your mobile now.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@ForgotPassword, "Failed! Please try again later.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(this@ForgotPassword, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
    }
}
