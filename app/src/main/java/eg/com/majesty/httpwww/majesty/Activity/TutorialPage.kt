package eg.com.majesty.httpwww.majesty.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.jaeger.library.StatusBarUtil
import com.rd.pageindicatorview.view.PageIndicatorView
import com.rd.pageindicatorview.view.animation.AnimationType
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.Adapters.TutorilaAdapter
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.Models.AppLoginIntroSliderModel
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_tutorial_page.*
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap


class TutorialPage : Activity()
{


    override fun onCreate(savedInstanceState: Bundle?)
    {

        StatusBarUtil.setTransparent(this)
        setContentView(R.layout.activity_tutorial_page)
        super.onCreate(savedInstanceState)
        afterViewa()
    }


    override fun attachBaseContext(newBase: Context)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }



    fun afterViewa()
    {

      //  sendFCMPush()


        login.setTypeface(Utils.Exo2Bold(this))
        signUp.setTypeface(Utils.Exo2Bold(this))
        getStarted.setTypeface(Utils.Exo2Bold(this))



        var str = ForeraaParameter(this).getString("GetAppIntroData")
        val gson = Gson()
        var jsonObject = Gson().fromJson(str, JsonObject::class.java)
        val itemType = object : TypeToken<List<AppLoginIntroSliderModel>>() {}.type
        val itemList = gson.fromJson<List<AppLoginIntroSliderModel>>(jsonObject.get("AppLoginIntroSlider").toString(), itemType)




        val adapter = TutorilaAdapter( itemList, this)
        val activity = this
        viewPager.adapter = adapter
        viewPager.startAutoScroll(3000)
        viewPager.interval= 3000
        viewPager.isCycle = false
        pageIndicatorView.count = adapter.count
        pageIndicatorView.setAnimationType(AnimationType.SCALE)
        pageIndicatorView.addViewPager(viewPager)
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int)
            {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {


            }
            override fun onPageSelected(position: Int)
            {

            }

        })


        var foreraaParameter = ForeraaParameter(this@TutorialPage)
        if(!foreraaParameter.getString("UserID").equals(""))
        {
            ce_login.visibility = View.INVISIBLE
        }

    }


    fun login(view: View)
    {
        startActivity(Intent(this , Login::class.java))
        finish()
    }


   fun getStarted (view: View)
    {
        startActivity(Intent(this , MainActivity::class.java))
        finish()
    }




    fun signUp(view: View)
    {
        startActivity(Intent(this , SignUp::class.java))
        finish()
    }













    private fun sendFCMPush() {

        val Legacy_SERVER_KEY = "AIzaSyAoDeCH5K2XqHxx87SEqp_3NUtO0jDGP-A"
        val msg = "this is test message,.,,.,."
        val title = "my title"
        val token = "diEQiNZV7VA:APA91bErXK74zoiLI0tQb0PkGz0Tuf2JCuXkjp6qn0Dnq5GSn_HCquQ_mImayMUIbTIsmq2_ellHShUqiR2eL6DmS5bUCO_3V15d5ah6AJXUzrs2D-Il6fJfN0YW1-mAaVAamU7f4j2R"

        var obj: JSONObject? = null
        var objData: JSONObject? = null
        var dataobjData: JSONObject? = null

        try {
            obj = JSONObject()
            objData = JSONObject()

            objData.put("body", msg)
            objData.put("title", title)
            objData.put("sound", "default")
            objData.put("icon", "icon_name") //   icon_name image must be there in drawable
            objData.put("tag", token)
            objData.put("priority", "high")

            dataobjData = JSONObject()
            dataobjData.put("text", msg)
            dataobjData.put("title", title)

            obj.put("to", token)
            obj.put("priority", "high");

            //obj.put("notification", objData)
            obj.put("data", dataobjData)
            Log.e("!_@rj@_@@_PASS:>", obj.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val jsObjRequest = object : JsonObjectRequest(Request.Method.POST, "https://fcm.googleapis.com/fcm/send", obj,
                object : Response.Listener<JSONObject> {
                    override fun onResponse(response: JSONObject) {
                        Log.e("!_@@_SUCESS", response.toString() + "")
                    }
                },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError) {
                        Log.e("!_@@_Errors--", error.toString() + "")
                    }
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = "key=$Legacy_SERVER_KEY"
                params["Content-Type"] = "application/json"
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        val socketTimeout = 1000 * 60// 60 seconds
        val policy = DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        jsObjRequest.retryPolicy = policy
        requestQueue.add(jsObjRequest)
    }


}
