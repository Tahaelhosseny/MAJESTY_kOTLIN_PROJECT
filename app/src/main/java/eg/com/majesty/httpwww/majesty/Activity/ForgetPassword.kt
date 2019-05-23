package eg.com.majesty.httpwww.majesty.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import kotlinx.android.synthetic.main.activity_forget_password.*
import kotlinx.android.synthetic.main.activity_forget_password.email
import org.json.JSONArray
import java.util.regex.Pattern

class ForgetPassword : AppCompatActivity() {


    var codesend = false


    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    )



    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
    }



    fun comentLay(view: View)
    {

        if(!codesend)
        {
            if(!EMAIL_ADDRESS_PATTERN.matcher(email.text.toString()).matches())
            {
                email.setError(this.resources.getString(R.string.mailnotvalid))
            }else
            {
                sendmail()
            }
        }else
        {
            if(pOne.text.toString().length<5)
            {
                pOne.setError(this.resources.getString(R.string.vercodeerror))
            }else if (Password.text.toString().length < 6)
                {
                    Password.setError(this.resources.getString(R.string.pass6))
                }else if (!cPassword.text.toString().equals(Password.text.toString()))
                {

                    Password.setError(this.resources.getString(R.string.passnotmatch))
                    cPassword.setError(this.resources.getString(R.string.passnotmatch))
                } else
                {
                  sendVerificationCode()
                }
        }

    }



    fun sendmail()
    {
        var makeRequest = MakeRequest("SendVerificationEmailToResetPassword?isArabic="+Utils.isArabic(this)+"&email="+email.text.toString() ,"0",this,"SendVerificationEmailToResetPassword",true)
        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {
                var jsonObject = JSONArray(result.get("res").toString()).getJSONObject(0)
                if(jsonObject.getBoolean("IsSucceed"))
                {
                    phoneee.visibility = View.VISIBLE
                    eemail.visibility = View.GONE
                    ecPassword.visibility = View.VISIBLE
                    ePassword.visibility = View.VISIBLE
                    codesend = true
                }else
                {

                    var jsonObjecttt = jsonObject.getJSONArray("Remarks").getJSONObject(0)
                    email.setError(jsonObjecttt.getString("ErrorDescription"))
                }

            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })
    }




    fun sendVerificationCode()
    {
        var makeRequest = MakeRequest("ResetPassword?isArabic="+Utils.isArabic(this)+"&verdicationCode="+pOne.text.toString()+"&NewPassword=" + Password.text.toString() ,"0",this,"ResetPassword",true)
        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {
                var jsonObject = JSONArray(result.get("res").toString()).getJSONObject(0)
                if(jsonObject.getBoolean("IsSucceed"))
                {
                    phoneee.visibility = View.VISIBLE
                    eemail.visibility = View.GONE
                    codesend = true
                    finish()
                }else
                {
                    var jsonObjecttt = jsonObject.getJSONArray("Remarks").getJSONObject(0)
                    pOne.setError(jsonObjecttt.getString("ErrorDescription"))
                }

            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })
    }
}
