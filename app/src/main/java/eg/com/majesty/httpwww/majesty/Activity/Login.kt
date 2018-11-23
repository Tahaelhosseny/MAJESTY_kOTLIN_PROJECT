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
import com.google.gson.Gson
import com.google.gson.JsonObject
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_login.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity
import org.json.JSONObject
import java.util.*

@EActivity(R.layout.activity_login)
class Login : Activity()
{

    var finish = false
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        try {
            finish = intent.getBooleanExtra("finish", false)
        }catch (e :Exception){}
    }


    override fun attachBaseContext(newBase: Context)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }


    @AfterViews fun afterViews()
    {

        flogin.setTypeface(Utils.Exo2Bold(this))
        ortext.setTypeface(Utils.setExo2Regular(this))
        mtxt.setTypeface(Utils.Exo2Bold(this))
        email.setTypeface(Utils.setExo2Regular(this))
        password.setTypeface(Utils.setExo2Regular(this))
        remMe.setTypeface(Utils.setExo2Regular(this))
        forget.setTypeface(Utils.setExo2Regular(this))
        dont.setTypeface(Utils.setExo2Regular(this))
        sign.setTypeface(Utils.setExo2Regular(this))
        loginnnn.setTypeface(Utils.Exo2Bold(this))


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

        var foreraaParameter = ForeraaParameter(applicationContext)
                makeRequest.request(object  : VolleyCallback
                {
                    override fun onSuccess(result: Map<String, String>)
                    {

                        val res = result.get("res").toString()

                        val jasonObject = Gson().fromJson(res , JsonObject::class.java)
                        if(jasonObject.get("IsSucceed").asBoolean)
                        {
                            var rem = remember.isChecked
                            var UserID = jasonObject.get("UserID").asString
                            foreraaParameter.setString("UserID" , UserID)
                            foreraaParameter.setBoolean("rem" , rem)
                            foreraaParameter.setBoolean("social" , social)
                            startActivity(Intent(this@Login , MainActivity_::class.java))
                            finish()
                        }else
                            Toast.makeText(this@Login , "This Account Never Register " , Toast.LENGTH_LONG ).show()
                    }

                } ,object : ONRetryHandler
                {
                    override fun onRetryHandler(funName: String)
                    {

                    }
                })
    }




    @Click fun login()
    {
        val userName = email.getText()
        val password = password.getText()

        var foreraaParameter = ForeraaParameter(applicationContext)

        var map = HashMap<String , String>()

                var makeRequest = MakeRequest("UserLogin?isLoginThroughMobileApp=true&isArabic=false&username=" +userName +"&password="+password,"0", map , this,"UserLogin",true)

                        makeRequest.request(object  : VolleyCallback
                        {
                            override fun onSuccess(result: Map<String, String>)
                            {
                                var ss = map.get("res").toString().subSequence(1,5)
                                if(map.get("res").toString().length<5)
                                {
                                    Toast.makeText(this@Login , ss.toString() +"\nThis Account Never Register " , Toast.LENGTH_LONG ).show()
                                }else
                                {
                                    var rem = remember.isChecked
                                    var UserID = map.get("res").toString()
                                    foreraaParameter.setString("UserID" , UserID)
                                    foreraaParameter.setBoolean("rem" , rem)
                                    foreraaParameter.setBoolean("social" , false)
                                    startActivity(Intent(this@Login , MainActivity_::class.java))
                                    finish()
                                }
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