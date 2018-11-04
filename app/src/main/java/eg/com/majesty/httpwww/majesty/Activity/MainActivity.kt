package eg.com.majesty.httpwww.majesty.Activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.jaeger.library.StatusBarUtil
import eg.com.majesty.httpwww.majesty.R
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_main.*
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity
import android.app.FragmentTransaction
import android.content.Intent
import android.os.Build
import eg.com.majesty.httpwww.majesty.Fragments.*
import org.androidannotations.annotations.AfterViews
@EActivity(R.layout.activity_main)
class MainActivity : Activity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {

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



    @AfterViews fun AfterViews()
    {


        if(Build.VERSION.SDK_INT<23)
            actionBar.hide()



        homeIm.setImageResource(R.drawable.icon_home1)
        favoriteIm.setImageResource(R.drawable.favorite)
        ordersIm.setImageResource(R.drawable.ordera)
        menuIm.setImageResource(R.drawable.menu)
        headerText.setText(R.string.Home)

        val home = Home()
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameContainer, home)
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction.commit()
    }



    @Click fun back ()
    {
        super.onBackPressed()
    }
    @Click fun home ()
    {
        homeIm.setImageResource(R.drawable.icon_home1)
        favoriteIm.setImageResource(R.drawable.favorite)
        ordersIm.setImageResource(R.drawable.ordera)
        menuIm.setImageResource(R.drawable.menu)
        headerText.setText(R.string.Home)
        val home = Home()
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameContainer, home)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction.commit()
    }


    @Click fun menuu ()
    {
        homeIm.setImageResource(R.drawable.icon_home)
        favoriteIm.setImageResource(R.drawable.favorite)
        ordersIm.setImageResource(R.drawable.ordera)
        menuIm.setImageResource(R.drawable.menu1)
        headerText.setText(R.string.Menu)

        val menu = Menu()
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameContainer, menu)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction.commit()
    }


    @Click fun cart ()
    {

       /* headerText.setText(R.string.Cart)

        val cart = Cart()
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameContainer, cart)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction.commit()*/
        startActivity(Intent(this , CheckOut_::class.java))
    }


    @Click fun favorite ()
    {
        homeIm.setImageResource(R.drawable.icon_home)
        favoriteIm.setImageResource(R.drawable.favorite1)
        ordersIm.setImageResource(R.drawable.ordera)
        menuIm.setImageResource(R.drawable.menu)

        headerText.setText(R.string.Favorite)

        val favorite = Favorite()
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameContainer, favorite)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction.commit()
    }


    @Click fun orders ()
    {
        headerText.setText(R.string.Orders)

        homeIm.setImageResource(R.drawable.icon_home)
        favoriteIm.setImageResource(R.drawable.favorite)
        ordersIm.setImageResource(R.drawable.ordera)
        menuIm.setImageResource(R.drawable.menu)


        val orders = Orders()
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameContainer, orders)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction.commit()
    }


}




