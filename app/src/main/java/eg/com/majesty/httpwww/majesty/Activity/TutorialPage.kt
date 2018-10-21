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
import com.jaeger.library.StatusBarUtil
import com.rd.PageIndicatorView
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.Adapters.TutorilaAdapter
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


    val titles = listOf( " title 1 ", " title 2 ", " title 3 " , " title 4 " , " title 5 ")
    val des = listOf( " des 1 ", " des 2 ", " des 3 " , " des 4 " , " des 5 ")

    @ViewById lateinit var viewPager : AutoScrollViewPager
    @ViewById lateinit var pageIndicatorView : PageIndicatorView
    @ViewById lateinit var ce_login : LinearLayout
    @ViewById lateinit var getStarted : TextView

    override fun onCreate(savedInstanceState: Bundle?)
    {

        StatusBarUtil.setTransparent(this)
        super.onCreate(savedInstanceState)
        ViewPump.init(ViewPump.builder()
                .addInterceptor(CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/alfares.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build())

    }


    override fun attachBaseContext(newBase: Context)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }



    @AfterViews fun afterViewa()
    {



        val adapter = TutorilaAdapter(titles, des)
        val activity = this
        viewPager.adapter = adapter
        viewPager.startAutoScroll(3000)
        viewPager.interval= 3000
        viewPager.isCycle = false
        pageIndicatorView.setViewPager(viewPager)
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int)
            {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {


            }
            override fun onPageSelected(position: Int)
            {

                if(position == titles.size-1)
                {
                    ce_login.visibility = View.VISIBLE
                    getStarted.visibility = View.VISIBLE
                    pageIndicatorView.visibility = View.GONE

                }else
                {
                    ce_login.visibility = View.GONE
                    getStarted.visibility = View.GONE
                    pageIndicatorView.visibility = View.VISIBLE
                }

            }

        })



    }


    @Click fun login()
    {
        startActivity(Intent(this , Login_::class.java))
    }


    @Click fun getStarted ()
    {
        startActivity(Intent(this , MainActivity_::class.java))
    }




    @Click fun signUp()
    {
        startActivity(Intent(this , SignUp_::class.java))

    }

}
