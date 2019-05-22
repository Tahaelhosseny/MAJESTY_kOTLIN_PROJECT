package eg.com.majesty.httpwww.majesty.Activity

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
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
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap
import android.view.View
import android.view.WindowManager
import android.widget.RadioGroup
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils

import org.json.JSONObject


class SignUp : FragmentActivity()
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
        setContentView((R.layout.activity_sign_up))
        ViewPump.init(ViewPump.builder()
                .addInterceptor(CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/Exo2-Regular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build())


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        afterViews()
    }

    fun afterViews()
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


    fun birthdate(view :View)
    {



        TimePickerFragment().show(supportFragmentManager, "timePicker")
    }



    fun signUp(view :View)
    {

        var emailt = email.text
        var fNamet = fName.text
        var sNamet = sName.text
        var pOnet = pOne.text
        var pSecondt = pSecond.text
        var birthdayt = birthdate.text
        var passwordt = Password.text
        var cPasswordt = cPassword.text
        var titleT = titleee.text
        var map = HashMap<String , String>()

        var x = 0

        if(social)
        {

            if (pOnet.length != 11)
            {
                x++
                pOne.setError(this.resources.getString(R.string.phone11))
            }
            else
            {
                map.set("phone1" , pOnet.toString())
            }

            if(pSecondt.length>0)
                if (pSecondt.length != 11)
                {

                    pSecond.setError(this.resources.getString(R.string.phone11))
                    map.set("phone2" , "")
                } else
                {
                    map.set("phone2" , pSecondt.toString())
                }



            if (titleT.length<2)
            {
                x++
                titleee.setError(this.resources.getString(R.string.title))
            }
            else
            {
                map.set("title" , titleT.toString())
            }


        }else
        {
            if(!EMAIL_ADDRESS_PATTERN.matcher(emailt).matches())
            {
                x++
                email.setError(this.resources.getString(R.string.mailnotvalid))
            }
            else
            {
                map.set("email" , emailt.toString())
            }

            if (fNamet.length<2)
            {
                x++
                fName.setError(this.resources.getString(R.string.firstName))
            }
            else
            {
                map.set("firstname" , fNamet.toString())
            }



            if (titleT.length<2)
            {
                x++
                titleee.setError(this.resources.getString(R.string.title))
            }
            else
            {
                map.set("title" , titleT.toString())
            }





            if (sNamet.length<2)
            {
                x++
                titleee.setError(this.resources.getString(R.string.SecondName))
            }
            else
            {
                map.set("secondname" , sNamet.toString())
            }



            if (pOnet.length != 11)
            {
                x++
                pOne.setError(this.resources.getString(R.string.phone11))

            }
            else
            {
                map.set("phone1" , pOnet.toString())
            }
            if(pSecondt.length>0)
                if (pSecondt.length != 11)
                {


                    pSecond.setError(this.resources.getString(R.string.phone11))
                    map.set("phone2" , "")
                }

                else
                {
                    map.set("phone2" , pSecondt.toString())
                }


            if (passwordt.length < 6)
            {
                x++
                Password.setError(this.resources.getString(R.string.pass6))
            }

            if (!cPasswordt.toString().equals(passwordt.toString()))
            {
                x++
                Password.setError(this.resources.getString(R.string.passnotmatch))
                cPassword.setError(this.resources.getString(R.string.passnotmatch))
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



    fun fbLogin(view :View)
    {
        LoginManager.getInstance().logInWithReadPermissions(this , Arrays.asList("email"))
        LoginManager.getInstance().registerCallback(callbackManager,object  : FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?)
            {

                fbId  = result!!.accessToken.userId

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
                        Toast.makeText(this@SignUp , this@SignUp.resources.getString(R.string.facebooklogindone) ,Toast.LENGTH_LONG).show()
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
        var makeRequest = MakeRequest("AddUser?isArabic=false&isMobileAppRegisteration=true&title="+ titleee.text.toString()+"&firstname=" + map.get("firstname")+"&secondname="+map.get("secondname")+"&phone1=" +map.get("phone1") +"&phone2=" +map.get("phone2")+"&birthdate="+map.get("birthdate")+"&email="+map.get("email")
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
                    foreraaParameter.setString("Title" , titleee.text.toString())




                    foreraaParameter.setString("FirstName" , map.get("firstname").toString() )
                    foreraaParameter.setString("SecondName" , map.get("secondname").toString())
                    foreraaParameter.setString("Phone1" ,map.get("phone1").toString())
                    foreraaParameter.setString("Phone2" , map.get("phone2").toString())
                    foreraaParameter.setString("Email" , map.get("email").toString())




                    startActivity(Intent(this@SignUp , MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
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
                            email.setError(this@SignUp.resources.getString(R.string.emailused))
                        }else if(jsonO.asJsonObject.get("ErrorCode").asString.equals("2"))
                        {
                            pOne.setError(this@SignUp.resources.getString(R.string.notCorrectPhone))
                        }else if(jsonO.asJsonObject.get("ErrorCode").asString.equals("3"))
                        {
                            pOne.setError(this@SignUp.resources.getString(R.string.notCorrectPhone))
                        }else if(jsonO.asJsonObject.get("ErrorCode").asString.equals("4"))
                        {
                            email.setError(this@SignUp.resources.getString(R.string.emailused))
                        }else if(jsonO.asJsonObject.get("ErrorCode").asString.equals("5"))
                        {
                            email.setError(this@SignUp.resources.getString(R.string.mailnotvalid))
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
                    foreraaParameter.setString("Title" , titleee.text.toString())

                    foreraaParameter.setBoolean("rem" , rem)
                    foreraaParameter.setBoolean("social" , social)

                    foreraaParameter.setString("FirstName" , map.get("firstname").toString() )
                    foreraaParameter.setString("SecondName" , map.get("secondname").toString())
                    foreraaParameter.setString("Phone1" ,map.get("phone1").toString())
                    foreraaParameter.setString("Phone2" , map.get("phone2").toString())
                    foreraaParameter.setString("Email" , map.get("email").toString())




                    startActivity(Intent(this@SignUp , MainActivity::class.java))
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
                            email.setError(this@SignUp.resources.getString(R.string.emailused))
                        }else if(jsonO.asJsonObject.get("ErrorCode").asString.equals("2"))
                        {
                            pOne.setError(this@SignUp.resources.getString(R.string.notCorrectPhone))
                        }else if(jsonO.asJsonObject.get("ErrorCode").asString.equals("3"))
                        {
                            pOne.setError(this@SignUp.resources.getString(R.string.notCorrectPhone))
                        }else if(jsonO.asJsonObject.get("ErrorCode").asString.equals("4"))
                        {
                            email.setError(this@SignUp.resources.getString(R.string.emailused))
                        }else if(jsonO.asJsonObject.get("ErrorCode").asString.equals("5"))
                        {
                            email.setError(this@SignUp.resources.getString(R.string.mailnotvalid))
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
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun backk(view :View)
    {
        super.onBackPressed()
    }




    class TimePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener
    {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Use the current date as the default date in the picker
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            return DatePickerDialog(activity, this, year, month, day)
        }


        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int)
        {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()

            val mYear = calendar.get(Calendar.YEAR)
            val mMonth = calendar.get(Calendar.MONTH)
            val mDay = calendar.get(Calendar.DAY_OF_MONTH)


            if(mYear - year <10 )
            {
                Toast.makeText(activity , this.resources.getString(R.string.not_valid_birth) , Toast.LENGTH_LONG).show()
                activity!!.birthdate.setText("")
            }else
            {
                activity!!.birthdate.setText(year.toString() + "/" + (month+1) + "/" + dayOfMonth)
            }
        }


    }







}