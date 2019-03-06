package eg.com.majesty.httpwww.majesty.Activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap
import android.view.View
import android.view.WindowManager
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.WakeLock
import org.json.JSONObject


@EActivity(R.layout.activity_sign_up)
class SignUp : Activity()
{


    lateinit var callbackManager : CallbackManager
    var social = false
    var fbId = ""

    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
             "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
             "\\@" +
             "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
             "(" +
             "\\." +
             "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
             ")+"
)

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



       /* val info: PackageInfo
        try {
            info = packageManager.getPackageInfo("eg.com.majesty.httpwww.majesty", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val something = Base64.getEncoder().encodeToString(md.digest())
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something)
            }
        } catch (e1: PackageManager.NameNotFoundException) {
            Log.e("name not found", e1.toString())
        } catch (e: NoSuchAlgorithmException) {
            Log.e("no such an algorithm", e.toString())
        } catch (e: Exception) {
            Log.e("exception", e.toString())
        }*/



        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

    }


    @AfterViews fun afterViews()
    {

        sign.setTypeface(Utils.Exo2Bold(this))
        ftxt.setTypeface(Utils.Exo2Bold(this))
        signUp.setTypeface(Utils.Exo2Bold(this))
        sign.setTypeface(Utils.Exo2Bold(this))


        FacebookSdk.sdkInitialize(this)
        AppEventsLogger.activateApp(this)
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logOut()
    }

    override fun attachBaseContext(newBase: Context)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }


    @Click fun birthdate()
    {
        SpinnerDatePickerDialogBuilder()
                .context(this)

                .callback(object : DatePickerDialog.OnDateSetListener, com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(view: com.tsongkha.spinnerdatepicker.DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int)
                    {

                        val calendar = Calendar.getInstance()
                        calendar.timeInMillis = System.currentTimeMillis()

                        val mYear = calendar.get(Calendar.YEAR)
                        val mMonth = calendar.get(Calendar.MONTH)
                        val mDay = calendar.get(Calendar.DAY_OF_MONTH)


                        if(mYear - year <10 )
                        {
                            Toast.makeText(this@SignUp , "Not Valid BirthDay" , Toast.LENGTH_LONG).show()
                            birthdate.setText("")
                        }else
                        {
                            birthdate.setText(year.toString() + "/" + (monthOfYear+1) + "/" + dayOfMonth)
                        }
                    }

                    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int)
                    {
                    }
                })
                .spinnerTheme(R.style.NumberPickerStyle)
                .showTitle(true)
                .showDaySpinner(true)
                .defaultDate(1993, 4, 12)
                .maxDate(2050, 0, 1)
                .minDate(1950, 0, 1)
                .build()
                .show()

    }



    @Click fun signUp()
    {

        var emailt = email.text
        var fNamet = fName.text
        var sNamet = sName.text
        var pOnet = pOne.text
        var pSecondt = pSecond.text
        var birthdayt = birthdate.text
        var passwordt = Password.text
        var cPasswordt = cPassword.text
        var map = HashMap<String , String>()

        var x = 0

        if(social)
        {

            if (pOnet.length != 11)
            {
                x++
                pOne.setError("Phone Must be 11 numbre")
            }
            else
            {
                map.set("phone1" , pOnet.toString())
            }

            if (pSecondt.length != 11)
            {


                pSecond.setError("Phone Must be 11 numbre")
                map.set("phone2" , "")
            }

            else
            {
                map.set("phone2" , pSecondt.toString())
            }


        }else
        {
            if(!EMAIL_ADDRESS_PATTERN.matcher(emailt).matches())
            {
                x++
                email.setError("Not Valid Mail")
            }
            else
            {
                map.set("email" , emailt.toString())
            }

            if (fNamet.length<2)
            {
                x++
                fName.setError("First Name Must be At Least 3 Letters")
            }
            else
            {
                map.set("firstname" , fNamet.toString())
            }


            if (sNamet.length<2)
            {
                x++
                sName.setError("Second Name Must be At Least 3 Letters")
            }
            else
            {
                map.set("secondname" , sNamet.toString())
            }



            if (pOnet.length != 11)
            {
                x++
                pOne.setError("Phone Must be 11 numbre")
            }
            else
            {
                map.set("phone1" , pOnet.toString())
            }

            if (pSecondt.length != 11)
            {


                pSecond.setError("Phone Must be 11 numbre")
                map.set("phone2" , "")
            }

            else
            {
                map.set("phone2" , pSecondt.toString())
            }


            if (passwordt.length < 6)
            {
                x++
                Password.setError("Passowrd Must be At Least 6 Letters")
            }

            if (!cPasswordt.toString().equals(passwordt.toString()))
            {
                x++
                Password.setError("Password Not Match")
                cPassword.setError("Password Not Match")
            }else
            {
                map.set("password" , cPasswordt.toString())
            }

        }


        map.set("birthdate" , birthdayt.toString())
        if(x==0&& !social )
        {
            confirmSign(map)
        }else if (x==0 && social)
        {
            confirmFbSign(map)
        }
    }



    @Click fun fbLogin()
    {
        LoginManager.getInstance().logInWithReadPermissions(this , Arrays.asList("email"))
        LoginManager.getInstance().registerCallback(callbackManager,object  : FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?)
            {

                fbId = Profile.getCurrentProfile().id

                val parameters = Bundle()
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location") // Parámetros que pedimos a facebook

                val graphRequest = GraphRequest.newMeRequest(result?.accessToken ,object  : GraphRequest.GraphJSONObjectCallback
                {
                    override fun onCompleted(`object`: JSONObject?, response: GraphResponse?)
                    {
                        if(`object`!!.has("email"))
                        {
                            email.setText(`object`?.getString("email"))
                        }

                        if(`object`!!.has("first_name"))
                        {
                            fName.setText(`object`?.getString("first_name"))
                        }

                        if(`object`!!.has("last_name"))
                        {
                            sName.setText(`object`?.getString("last_name"))
                        }
                        if(`object`!!.has("birthday"))
                        {
                            birthdate.setText(`object`?.getString("birthday"))
                        }


                        ePassword.visibility = View.GONE
                        ecPassword.visibility = View.GONE

                        social = true
                        Toast.makeText(this@SignUp , "Facebook Login Success , Please Complete Your Data" ,Toast.LENGTH_LONG).show()
                    }
                })
                graphRequest.setParameters(parameters)
                graphRequest.executeAsync()


                /*var id = Profile.getCurrentProfile().id
                var fname = Profile.getCurrentProfile().firstName
                var lname = Profile.getCurrentProfile().lastName
                var photo = Profile.getCurrentProfile().getProfilePictureUri(400 , 400)*/
            }

            override fun onCancel()
            {
            }

            override fun onError(error: FacebookException?)
            {
            }
        })

    }

    fun confirmSign(map: HashMap<String , String>)
    {

        var foreraaParameter = ForeraaParameter(applicationContext)
        var makeRequest = MakeRequest("AddUser?isArabic=false&isMobileAppRegisteration=true&title=&firstname=" + map.get("firstname")+"&secondname="+map.get("secondname")+"&phone1=" +map.get("phone1") +"&phone2=" +map.get("phone2")+"&birthdate="+map.get("birthdate")+"&email="+map.get("email")
                +"&username="+map.get("email")+"&password="+map.get("password"),"0", map , this,"GetFoodMenuTypes",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {
                var str = result.get("res")
                var jsonArray = Gson().fromJson(str, JsonArray::class.java)
                var jsonObject = jsonArray.get(0).asJsonObject
                if(jsonObject.get("IsRegisterationSucceed").asBoolean)
                {
                    var rem = remember.isChecked
                    var UserID = jsonObject.get("UserID").asString
                    foreraaParameter.setString("UserID" , UserID)
                    foreraaParameter.setBoolean("rem" , rem)
                    foreraaParameter.setBoolean("social" , social)





                    foreraaParameter.setString("FirstName" , map.get("firstname").toString() )
                    foreraaParameter.setString("SecondName" , map.get("secondname").toString())
                    foreraaParameter.setString("Phone1" ,map.get("phone1").toString())
                    foreraaParameter.setString("Phone2" , map.get("phone2").toString())
                    foreraaParameter.setString("Email" , map.get("email").toString())




                    startActivity(Intent(this@SignUp , MainActivity_::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                    finish()


                }else
                {
                    var jsonArray2 = jsonObject.get("Remarks").asJsonArray
                    for (jsonO in jsonArray2)
                    {
                        if(jsonO.asJsonObject.get("ErrorCode").asString.equals("0"))
                        {
                        }else if(jsonO.asJsonObject.get("ErrorCode").asString.equals("1"))
                        {
                            email.setError("Email is already used before")
                        }else if(jsonO.asJsonObject.get("ErrorCode").asString.equals("2"))
                        {
                            pOne.setError("Please enter a valid phone number")
                        }else if(jsonO.asJsonObject.get("ErrorCode").asString.equals("3"))
                        {
                            pOne.setError("Phone number is already used before")
                        }else if(jsonO.asJsonObject.get("ErrorCode").asString.equals("4"))
                        {
                            email.setError("Email is already used before")
                        }else if(jsonO.asJsonObject.get("ErrorCode").asString.equals("5"))
                        {
                            email.setError("Please enter a valid email address")
                        }else if(jsonO.asJsonObject.get("ErrorCode").asString.equals("6"))
                        {

                        }

                    }
                }
            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })


    }

    fun confirmFbSign(map: HashMap<String , String>)
    {

        var foreraaParameter = ForeraaParameter(applicationContext)
        var makeRequest = MakeRequest("AddUserWithFacebook?isArabic=false&isMobileAppRegisteration=true&&title="+""+"&facebookUserId="+fbId+"&firstname=" + fName.text+"&secondname="+sName.getText()+"&phone1=" +pOne.text +"&phone2=" +pSecond.getText()+"&birthdate="+birthdate.getText()+"&email="+email.getText()
                +"&username="+email.getText()+"&password="+"","0", map , this,"GetFoodMenuTypes",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {
                var str = result.get("res")
                var jsonArray = Gson().fromJson(str, JsonArray::class.java)
                var jsonObject = jsonArray.get(0).asJsonObject
                if(jsonObject.get("IsRegisterationSucceed").asBoolean)
                {
                    var rem = remember.isChecked
                    var UserID = jsonObject.get("UserID").asString
                    foreraaParameter.setString("UserID" , UserID)
                    foreraaParameter.setBoolean("rem" , rem)
                    foreraaParameter.setBoolean("social" , social)

                    foreraaParameter.setString("FirstName" , map.get("firstname").toString() )
                    foreraaParameter.setString("SecondName" , map.get("secondname").toString())
                    foreraaParameter.setString("Phone1" ,map.get("phone1").toString())
                    foreraaParameter.setString("Phone2" , map.get("phone2").toString())
                    foreraaParameter.setString("Email" , map.get("email").toString())




                    startActivity(Intent(this@SignUp , MainActivity_::class.java))
                    finish()

                }else
                {
                    var jsonArray2 = jsonObject.get("Remarks").asJsonArray
                    for (jsonO in jsonArray2)
                    {
                        if(jsonO.asJsonObject.get("ErrorCode").asString.equals("0"))
                        {
                        }else if(jsonO.asJsonObject.get("ErrorCode").asString.equals("1"))
                        {
                            email.setError("Email is already used before")
                        }else if(jsonO.asJsonObject.get("ErrorCode").asString.equals("2"))
                        {
                            pOne.setError("Please enter a valid phone number")
                        }else if(jsonO.asJsonObject.get("ErrorCode").asString.equals("3"))
                        {
                            pOne.setError("Phone number is already used before")
                        }else if(jsonO.asJsonObject.get("ErrorCode").asString.equals("4"))
                        {
                            email.setError("Email is already used before")
                        }else if(jsonO.asJsonObject.get("ErrorCode").asString.equals("5"))
                        {
                            email.setError("Please enter a valid email address")
                        }else if(jsonO.asJsonObject.get("ErrorCode").asString.equals("6"))
                        {
                            Toast.makeText(this@SignUp , "This Facebook account has been used before"  , Toast.LENGTH_LONG).show()

                        }
                    }
                }
            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })
    }



    override fun onActivityResult(requestCode : Int, resultCode : Int, data :Intent)
   {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    @Click fun backk(view :View)
    {
        super.onBackPressed()
    }
}