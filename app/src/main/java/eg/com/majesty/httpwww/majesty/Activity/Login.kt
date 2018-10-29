package eg.com.majesty.httpwww.majesty.Activity
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity
import org.json.JSONObject
import java.util.*

@EActivity(R.layout.activity_login)
class Login : Activity()
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


    @AfterViews fun afterViews()
    {

        if(Build.VERSION.SDK_INT<23)
            actionBar.hide()
    }




    lateinit var callbackManager : CallbackManager
    var social = false
    var fbId = ""

    @Click fun facebookLogin()
    {
        FacebookSdk.sdkInitialize(this)
        AppEventsLogger.activateApp(this)
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logOut()

        LoginManager.getInstance().logInWithReadPermissions(this , Arrays.asList("email"))
        LoginManager.getInstance().registerCallback(callbackManager,object  : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?)
            {
                fbId = Profile.getCurrentProfile().id
                fbLogin()
            }

            override fun onCancel()
            {
                Toast.makeText(applicationContext , "Login Cancel , Please Try Again" , Toast.LENGTH_LONG)
            }

            override fun onError(error: FacebookException?)
            {
                Toast.makeText(applicationContext , "Failed To Login With Facebook , Please Try Again" , Toast.LENGTH_LONG)
            }
        })

        }




    fun fbLogin()
    {
        var map = HashMap<String , String>()
        var makeRequest = MakeRequest("UserLoginWithFacebook?isLoginThroughMobileApp=true&isArabic=false&facebookUserId=" + fbId,"0", map , this,"UserLoginWithFacebook",true)

                makeRequest.request(object  : VolleyCallback
                {
                    override fun onSuccess(result: Map<String, String>)
                    {

                    }
                } ,object : ONRetryHandler
                {
                    override fun onRetryHandler(funName: String)
                    {

                    }
                })
    }


    override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent)
    {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}