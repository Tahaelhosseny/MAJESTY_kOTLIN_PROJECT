package eg.com.majesty.httpwww.majesty

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.jaeger.library.StatusBarUtil
import eg.com.majesty.httpwww.majesty.Activity.TutorialPage
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import java.util.*
import android.R.string
import android.os.AsyncTask
import com.android.volley.*
import com.facebook.share.model.ShareMessengerMediaTemplateContent
import io.fabric.sdk.android.services.settings.IconRequest.build
import org.json.JSONObject
import com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
import com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES
import com.android.volley.toolbox.Volley
import com.android.volley.Request.Method.POST
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONException






class Splash : Activity()
{

    var isArabic = false
    var token = ""


    override fun onCreate(savedInstanceState: Bundle?)
    {

        StatusBarUtil.setTransparent(this)
        super.onCreate(savedInstanceState)
        if (!ForeraaParameter(this).isSetLanguage())
        {
            if (Locale.getDefault().getDisplayLanguage().equals("English"))
            {
                Utils.changeLocale(this, resources.getStringArray(R.array.languages_tag)[1])
                ForeraaParameter(this).setInt( "language" ,1)
            } else {
                Utils.changeLocale(this, resources.getStringArray(R.array.languages_tag)[0])
                ForeraaParameter(this).setInt( "language" , 0)
            }
        }else
        {
            Utils.changeLocale(this, resources.getStringArray(R.array.languages_tag)[ForeraaParameter(this).getInt( "language" ,0)])
        }

        setContentView((R.layout.activity_splash))


        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w( "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result?.token

                    Log.d("tokennnnn", token)
                })

       // getKeyHash(this)

        var foreraaParameter = ForeraaParameter(applicationContext)
        Handler().postDelayed(
                {
                    getTutorial()
                },3000)


    }










    fun getTutorial ()
    {
        FirebaseMessaging.getInstance().subscribeToTopic("all")

        if(ForeraaParameter(this).getInt("language" ,0) == 0)
            isArabic = true
        else
            isArabic = false
        var foreraaParameter = ForeraaParameter(this)

        if(!foreraaParameter.getString("UserID").equals(""))
        {
            token = FirebaseInstanceId.getInstance().getToken().toString()
            Log.e("jjjj",foreraaParameter.getString("UserID"))

        }

        var makeRequest = MakeRequest("GetAppIntroData?isArabic=" + isArabic +"&token="+token , "0", this, "GetFoodMenuTypes", false)
        makeRequest.request(object : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                var str = result.get("res")
                var foreraaParameter = ForeraaParameter(this@Splash)
                foreraaParameter.setString("GetAppIntroData" , str.toString())
                startActivity(Intent(this@Splash , TutorialPage::class.java))
                finish()
            }
        }, object : ONRetryHandler {
            override fun onRetryHandler(funName: String)
            {
                getTutorial()
            }
        })



    }





}
