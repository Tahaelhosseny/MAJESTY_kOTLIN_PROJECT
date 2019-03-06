package eg.com.majesty.httpwww.majesty.Activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
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
import android.graphics.Color
import android.os.Build
import android.support.v7.app.ActionBarDrawerToggle
import android.widget.Toast
import eg.com.majesty.httpwww.majesty.Fragments.*
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import org.androidannotations.annotations.AfterViews
import android.support.v4.view.GravityCompat
import android.support.v7.widget.ViewUtils
import android.view.View
import android.view.WindowManager


var isHistory = false



@EActivity(R.layout.activity_main)
class MainActivity : Activity()
{


    var savedInstanceStateA :Bundle? = null
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        ViewPump.init(ViewPump.builder()
                .addInterceptor(CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/Exo2-Regular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build())
        savedInstanceStateA = savedInstanceState


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)




    }

    override fun attachBaseContext(newBase: Context)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }



    @AfterViews fun AfterViews() {


            try {
                isHistory = intent.getBooleanExtra("isHistory", false)
            } catch (e: java.lang.Exception) {
            }


            if (Build.VERSION.SDK_INT < 23)
                actionBar.hide()

            drawerLayout.setScrimColor(Color.TRANSPARENT)
            drawerLayout.setDrawerElevation((0).toFloat())

            val actionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close) {

                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    super.onDrawerSlide(drawerView, slideOffset)
                    val slideX = drawerView!!.width * slideOffset
                    content.translationX = slideX
                    content.translationY = slideX / 4


                }


                override fun onDrawerClosed(drawerView: View) {
                    super.onDrawerClosed(drawerView)
                    content.setBackgroundResource(R.color.fragmentBack)
                }


                override fun onDrawerOpened(drawerView: View)
                {
                    super.onDrawerOpened(drawerView)
                    content.setBackgroundResource(R.color.white)
                }
            }

            drawerLayout.addDrawerListener(actionBarDrawerToggle)






            notiNum.setTypeface(Utils.Exo2SemiBold(this))
            cartTxt.setTypeface(Utils.Exo2SemiBold(this))
            headerText.setTypeface(Utils.Exo2SemiBold(this))
            userName.setTypeface(Utils.Exo2SemiBold(this))
            logouttxt.setTypeface(Utils.Exo2SemiBold(this))
            htxt.setTypeface(Utils.Exo2SemiBold(this))
            ftxt.setTypeface(Utils.Exo2SemiBold(this))
            mtxt.setTypeface(Utils.Exo2SemiBold(this))
            otxt.setTypeface(Utils.Exo2SemiBold(this))


        if(savedInstanceStateA==null)
            if (isHistory)
            {
                headerText.setText(R.string.Orders)
                homeIm.setImageResource(R.drawable.icon_home)
                favoriteIm.setImageResource(R.drawable.favorite)
                ordersIm.setImageResource(R.drawable.ic_orderb
                )
                menuIm.setImageResource(R.drawable.menu)


                val orders = Orders()
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frameContainer, orders)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                fragmentTransaction.commit()
            }else
            {
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


        var foreraaParameter = ForeraaParameter(this@MainActivity)

        if(foreraaParameter.getString("UserID").equals(""))
        {
            userName.setText("User Name")
            editProfile.visibility = View.INVISIBLE
            logOutLayout.visibility = View.INVISIBLE
        }else
        {
            userName.setText(foreraaParameter.getString("Title")+" " +foreraaParameter.getString("FirstName")+" " +foreraaParameter.getString("SecondName"))
            editProfile.visibility = View.VISIBLE
            logOutLayout.visibility = View.VISIBLE
            LogIn.visibility = View.INVISIBLE
        }




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



        var foreraaParameter = ForeraaParameter(this@MainActivity)

        if(!foreraaParameter.getString("UserID").equals(""))
        {
            startActivity(Intent(this , CheckOut_::class.java))

        }else
        {
            Toast.makeText(this , "You Must Login First" , Toast.LENGTH_LONG).show()
        }




    }


    @Click fun favorite ()
    {



        var ID = ""


        var foreraaParameter = ForeraaParameter(this)

        try
        {
            ID = foreraaParameter.getString("UserID")

        }catch (e : Exception){}





        if(ID.equals(""))
        {
            Toast.makeText(this , "You Must Login First" , Toast.LENGTH_LONG).show()
        }else
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





    }


    @Click fun orders ()
    {




        var foreraaParameter = ForeraaParameter(this@MainActivity)

        if(!foreraaParameter.getString("UserID").equals(""))
        {
            headerText.setText(R.string.Orders)

            homeIm.setImageResource(R.drawable.icon_home)
            favoriteIm.setImageResource(R.drawable.favorite)
            ordersIm.setImageResource(R.drawable.ic_orderb)
            menuIm.setImageResource(R.drawable.menu)


            val orders = Orders()
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameContainer, orders)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            fragmentTransaction.commit()
        }else
        {
            Toast.makeText(this , "You Must Login First" , Toast.LENGTH_LONG).show()
        }





    }


    @Click fun menu()
    {
        manageLay()

    }






    @Click fun addressBok()
    {
        //manageLay()
        var foreraaParameter = ForeraaParameter(this@MainActivity)

        if(!foreraaParameter.getString("UserID").equals(""))
        {
            startActivity(Intent(this , MyPlaces::class.java))
        }else
        {
            Toast.makeText(this , "You Must Login First" , Toast.LENGTH_LONG).show()
        }
    }

    
    @Click fun branches()
    {
        startActivity(Intent(this , Branches::class.java))
    }


    @Click fun Contact_Us()
    {
        startActivity(Intent(this , ContactUs::class.java))
    }


    @Click fun logOutLayout()
    {
        var foreraaParameter = ForeraaParameter(this@MainActivity)
        foreraaParameter.setString("UserID" , "")
        cartTxt.setText("0")
        userName.setText("User Name")
        editProfile.visibility = View.INVISIBLE
        logOutLayout.visibility = View.INVISIBLE
        LogIn.visibility = View.VISIBLE




    }




    @Click fun LogIn()
    {
        startActivity(Intent(this , Login_::class.java))
    }


    fun manageLay()
    {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            {
                drawerLayout.openDrawer(GravityCompat.END)
            }
        else
        {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

}




