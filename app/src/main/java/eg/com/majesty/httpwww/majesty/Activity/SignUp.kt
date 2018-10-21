/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package eg.com.majesty.httpwww.majesty.Activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.DatePicker
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import eg.com.majesty.httpwww.majesty.Adapters.CategoryItem
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.Models.CategoryModels
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_menu.*
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap
import android.provider.SyncStateContract.Helpers.update
import android.content.pm.PackageManager
import android.content.pm.PackageInfo
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import org.androidannotations.annotations.AfterViews
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


@EActivity(R.layout.activity_sign_up)
class SignUp : Activity()
{


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
                                .setDefaultFontPath("fonts/alfares.ttf")
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

    }


    @AfterViews fun afterViews()
    {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this)
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

        if(!EMAIL_ADDRESS_PATTERN.matcher(emailt).matches())
        {
            x++
            email.setError("Not Valid Mail")
        }
        else
        {
            map.set("email" , emailt.toString())
        }

        if (fNamet.length<3)
        {
            x++
            fName.setError("First Name Must be At Least 3 Letters")
        }
        else
        {
            map.set("firstname" , fNamet.toString())
        }


        if (sNamet.length<3)
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



        map.set("birthdate" , birthdayt.toString())


        if(x==0)
        {
            confirmSign(map)
        }
    }


    lateinit var callbackManager : CallbackManager
    @Click fun fbLogin()
    {
     callbackManager = CallbackManager.Factory.create()



        LoginManager.getInstance().registerCallback(callbackManager,object  : FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?)
            {
                var accessToken = AccessToken.getCurrentAccessToken();
                var isLoggedIn = accessToken != null && !accessToken.isExpired();

            }

            override fun onCancel()
            {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onError(error: FacebookException?)
            {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

    }

    fun confirmSign(map: HashMap<String , String>)
    {


        applicationContext
        var foreraaParameter = ForeraaParameter(applicationContext)



        var makeRequest = MakeRequest("AddUser?isArabic=false&isMobileAppRegisteration=false&title=&firstname=" + map.get("firstname")+"&secondname="+map.get("secondname")+"&phone1=" +map.get("phone1") +"&phone2=" +map.get("phone2")+"&birthdate="+map.get("birthdate")+"&email="+map.get("email")
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

                    Toast.makeText(applicationContext , "SignUpAndLoged" , Toast.LENGTH_SHORT).show()



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



    override fun onActivityResult(requestCode : Int, resultCode : Int, data :Intent)
   {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}

