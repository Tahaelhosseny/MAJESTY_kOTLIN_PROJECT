package eg.com.majesty.httpwww.majesty.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlin.collections.HashMap
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.edit_uiser_data.*
import kotlinx.android.synthetic.main.edit_uiser_data.email
import kotlinx.android.synthetic.main.edit_uiser_data.fName
import kotlinx.android.synthetic.main.edit_uiser_data.pOne
import kotlinx.android.synthetic.main.edit_uiser_data.pSecond
import kotlinx.android.synthetic.main.edit_uiser_data.sName
import kotlinx.android.synthetic.main.edit_uiser_data.sign
import kotlinx.android.synthetic.main.edit_uiser_data.titleee
import org.json.JSONArray
import org.json.JSONObject


class EditUserData : FragmentActivity()
{


    var mapppp = HashMap<String,String>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView((R.layout.edit_uiser_data))
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

        afterViews()
    }

     fun afterViews()
    {

        sign.setTypeface(Utils.Exo2Bold(this))
        editProfile.setTypeface(Utils.Exo2Bold(this))
        sign.setTypeface(Utils.Exo2Bold(this))
        getUserData()

    }

    override fun attachBaseContext(newBase: Context)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }


     fun backk(view :View)
    {
        super.onBackPressed()
    }



    fun getUserData()
    {
        var makeRequest = MakeRequest("GetUserInformation?userID=" + ForeraaParameter(applicationContext).getString("UserID"),"0", mapppp , this,"GetFoodMenuTypes",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {
                var jsonObject = JSONObject(result.get("res").toString())
                email.text = jsonObject.getString("Email")
                titleee.text = jsonObject.getString("Title")
                fName.text = jsonObject.getString("FirstName")
                sName.text = jsonObject.getString("SecondName")
                pOne.setText( jsonObject.getString("Phone1"))
                pSecond.setText(jsonObject.getString("Phone2"))

            }},object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })
    }


    fun editProfile(view: View)
    {
        if (pOne.text.length != 11)
        {
            pOne.setError(this.resources.getString(R.string.phone11))
            return
        }


        if(pSecond.text.length>0)
            if (pSecond.text.length != 11)
            {
                pSecond.setError(this.resources.getString(R.string.phone11))
                return
            }



        var makeRequest = MakeRequest("ChangeUserInformation?userID=" + ForeraaParameter(applicationContext).getString("UserID") + "&phone1=" + pOne.text.toString()+"&phone2="+pSecond.text.toString(),"0", mapppp , this,"GetFoodMenuTypes",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {
                var jsonObject = JSONArray(result.get("res").toString()).getJSONObject(0)

                if(!jsonObject.getBoolean("IsRegisterationSucceed"))
                {
                    pOne.setError(this@EditUserData.resources.getString(R.string.notCorrectPhone))
                    if(pSecond.text.length>0)
                        pSecond.setError(this@EditUserData.resources.getString(R.string.notCorrectPhone))

                }else
                {
                    Toast.makeText(this@EditUserData , this@EditUserData.resources.getString(R.string.dataUpdate),Toast.LENGTH_LONG).show()
                    finish()
                }

            }},object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })
    }


}