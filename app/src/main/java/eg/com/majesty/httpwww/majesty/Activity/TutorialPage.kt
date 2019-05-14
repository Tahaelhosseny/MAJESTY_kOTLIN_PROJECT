package eg.com.majesty.httpwww.majesty.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager
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

}
