package eg.com.majesty.httpwww.majesty.Activity
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback

import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import java.util.*
import eg.com.majesty.httpwww.majesty.R


class Login : Activity()
{

    var finish = false
    lateinit var callbackManager : CallbackManager
    var social = false
    var fbId = ""










    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        try {
            finish = intent.getBooleanExtra("finish", false)
        }catch (e :Exception){}


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        afterViews()
    }


    override fun attachBaseContext(newBase: Context)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }


   fun afterViews()
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
            try {
                actionBar.hide()
            }catch (e : Exception){}

    }





    fun facebookLogin(views: View)
    {
        FacebookSdk.sdkInitialize(this)
        AppEventsLogger.activateApp(this)
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logOut()
        LoginManager.getInstance().logInWithReadPermissions(this , Arrays.asList("email"))
        LoginManager.getInstance().registerCallback(callbackManager,object  : FacebookCallback<LoginResult>
        {

            override fun onSuccess(result: LoginResult?)
            {

                fbId = result!!.accessToken.userId
                fbLogin()

            }

            override fun onCancel()
            {
                Toast.makeText(applicationContext , this@Login.resources.getString(R.string.login_cancel) , Toast.LENGTH_LONG)
            }
            override fun onError(error: FacebookException?)
            {
                Toast.makeText(applicationContext , this@Login.resources.getString(R.string.login_cancel) , Toast.LENGTH_LONG)
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



                        var jsonObject = JSONObject(result.get("res").toString())
                        var _UserLoginInfo = jsonObject.getJSONObject("_UserLoginInfo")
                        var _UserInfoData = jsonObject.getJSONObject("_UserInfoData")

                        if(_UserLoginInfo.getBoolean("IsSucceed"))
                        {
                            var rem = remember.isChecked
                            foreraaParameter.setString("UserID" , _UserInfoData.getString("UserID"))
                            foreraaParameter.setString("Title" , _UserInfoData.getString("Title"))
                            foreraaParameter.setString("FirstName" , _UserInfoData.getString("FirstName"))
                            foreraaParameter.setString("SecondName" , _UserInfoData.getString("SecondName"))
                            foreraaParameter.setString("Phone1" , _UserInfoData.getString("Phone1"))
                            foreraaParameter.setString("Phone2" , _UserInfoData.getString("Phone2"))
                            foreraaParameter.setString("Email" , _UserInfoData.getString("Email"))
                            foreraaParameter.setBoolean("IsVerified" , _UserInfoData.getBoolean("IsVerified"))
                            foreraaParameter.setBoolean("IsMale" , _UserInfoData.getBoolean("IsMale"))
                            foreraaParameter.setBoolean("rem" , rem)
                            foreraaParameter.setBoolean("social" , true)
                            startActivity(Intent(this@Login , MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        }else
                        {
                            Toast.makeText(this@Login , this@Login.resources.getString(R.string.not_registered) , Toast.LENGTH_LONG).show()
                        }
                    }

                } ,object : ONRetryHandler
                {
                    override fun onRetryHandler(funName: String)
                    {

                    }
                })
    }




    fun backBtn(views: View)
    {
        onBackPressed()
    }


    fun loginnnn(views: View)
    {
        val userName = email.getText()
        val passwordd = password.getText()

        var foreraaParameter = ForeraaParameter(applicationContext)

        var map = HashMap<String , String>()

                var makeRequest = MakeRequest("UserLogin?isLoginThroughMobileApp=true&isArabic=false&username=" +userName +"&password="+passwordd,"0", map , this,"UserLogin",true)

                        makeRequest.request(object  : VolleyCallback
                        {
                            override fun onSuccess(result: Map<String, String>)
                            {

                                var jsonObject = JSONObject(result.get("res"))
                                var _UserLoginInfo = jsonObject.getJSONObject("_UserLoginInfo")
                                var _UserInfoData = jsonObject.getJSONObject("_UserInfoData")

                                if(_UserLoginInfo.getBoolean("IsSucceed"))
                                {
                                    var rem = remember.isChecked

                                    foreraaParameter.setString("UserID" , _UserInfoData.getString("UserID"))
                                    foreraaParameter.setString("Title" , _UserInfoData.getString("Title"))
                                    foreraaParameter.setString("FirstName" , _UserInfoData.getString("FirstName"))
                                    foreraaParameter.setString("SecondName" , _UserInfoData.getString("SecondName"))
                                    foreraaParameter.setString("Phone1" , _UserInfoData.getString("Phone1"))
                                    foreraaParameter.setString("Phone2" , _UserInfoData.getString("Phone2"))
                                    foreraaParameter.setString("Email" , _UserInfoData.getString("Email"))
                                    foreraaParameter.setBoolean("IsVerified" , _UserInfoData.getBoolean("IsVerified"))
                                    foreraaParameter.setBoolean("IsMale" , _UserInfoData.getBoolean("IsMale"))
                                    foreraaParameter.setBoolean("rem" , rem)
                                    foreraaParameter.setBoolean("social" , false)
                                    startActivity(Intent(this@Login , MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))

                                }else
                                {
                                    email.setError(this@Login.resources.getString(R.string.not_registered))
                                    password.setError(this@Login.resources.getString(R.string.PasswordError))
                                    Toast.makeText(this@Login , this@Login.resources.getString(R.string.not_registered) , Toast.LENGTH_LONG).show()
                                }

                            }
                        } ,object : ONRetryHandler
                        {
                            override fun onRetryHandler(funName: String)
                            {

                            }
                        })


    }



    fun signUp(views: View)
    {
        startActivity(Intent(this , SignUp::class.java))
    }



    override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent)
    {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }













}