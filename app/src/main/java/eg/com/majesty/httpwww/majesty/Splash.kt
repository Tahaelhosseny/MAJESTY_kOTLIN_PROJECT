package eg.com.majesty.httpwww.majesty

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.jaeger.library.StatusBarUtil
import eg.com.majesty.httpwww.majesty.Activity.TutorialPage
//import eg.com.majesty.httpwww.majesty.Activity.MainActivity_
//import eg.com.majesty.httpwww.majesty.Activity.TutorialPage_
import eg.com.majesty.httpwww.majesty.Adapters.GetFoodMenus
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils.getKeyHash
import eg.com.majesty.httpwww.majesty.Models.GetFoodMenusModel
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_favorite.*
import org.json.JSONObject
import java.util.*


class Splash : Activity()
{

    var isArabic = false


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

        getKeyHash(this)

        var foreraaParameter = ForeraaParameter(applicationContext)
        Handler().postDelayed(
                {
                    getTutorial()
                },3000)


    }










    fun getTutorial ()
    {

        if(ForeraaParameter(this).getInt("language" ,0) == 0)
            isArabic = true
        else
            isArabic = false


        var makeRequest = MakeRequest("GetAppIntroData?isArabic=" + isArabic , "0", this, "GetFoodMenuTypes", false)
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
            override fun onRetryHandler(funName: String) {

            }
        })



    }
}
