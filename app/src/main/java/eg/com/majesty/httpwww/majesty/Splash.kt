package eg.com.majesty.httpwww.majesty

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.jaeger.library.StatusBarUtil
import eg.com.majesty.httpwww.majesty.Activity.MainActivity_
import eg.com.majesty.httpwww.majesty.Activity.TutorialPage_
import eg.com.majesty.httpwww.majesty.Adapters.GetFoodMenus
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.Models.GetFoodMenusModel
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_favorite.*
import org.androidannotations.annotations.EActivity
import org.json.JSONObject

@EActivity(R.layout.activity_splash)
class Splash : Activity()
{


    override fun onCreate(savedInstanceState: Bundle?)
    {

        StatusBarUtil.setTransparent(this)

        super.onCreate(savedInstanceState)

        var foreraaParameter = ForeraaParameter(applicationContext)
        Handler().postDelayed(
                {
                    getTutorial()
                },1000)


    }










    fun getTutorial ()
    {
        var makeRequest = MakeRequest("GetAppIntroData?isArabic=false" , "0", this, "GetFoodMenuTypes", false)
        makeRequest.request(object : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                val gson = Gson()

                var str = result.get("res")


                var foreraaParameter = ForeraaParameter(this@Splash)
                foreraaParameter.setString("GetAppIntroData" , str.toString())
                startActivity(Intent(this@Splash , TutorialPage_::class.java))
                finish()
            }
        }, object : ONRetryHandler {
            override fun onRetryHandler(funName: String) {

            }
        })



    }
}
