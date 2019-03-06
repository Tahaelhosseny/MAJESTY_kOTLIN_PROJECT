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
import eg.com.majesty.httpwww.majesty.Models.GetFoodMenusModel
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ViewById


@EActivity(R.layout.activity_tutorial_page)
class TutorialPage : Activity()
{


    @ViewById lateinit var viewPager : AutoScrollViewPager
    @ViewById lateinit var pageIndicatorView : PageIndicatorView
    @ViewById lateinit var ce_login : LinearLayout
    @ViewById lateinit var getStarted : TextView
    @ViewById lateinit var login : TextView
    @ViewById lateinit var signUp : TextView


    override fun onCreate(savedInstanceState: Bundle?)
    {

        StatusBarUtil.setTransparent(this)
        super.onCreate(savedInstanceState)
    }


    override fun attachBaseContext(newBase: Context)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }



    @AfterViews fun afterViewa()
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


    @Click fun login()
    {
        startActivity(Intent(this , Login_::class.java))
        finish()
    }


    @Click fun getStarted ()
    {
        startActivity(Intent(this , MainActivity_::class.java))
        finish()
    }




    @Click fun signUp()
    {
        startActivity(Intent(this , SignUp_::class.java))
        finish()
    }

}
