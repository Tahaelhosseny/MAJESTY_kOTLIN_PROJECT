package eg.com.majesty.httpwww.majesty.Activity

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.view.View
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import kotlinx.android.synthetic.main.activity_contact_us.*
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import android.content.ActivityNotFoundException
import android.content.pm.PackageManager
import android.Manifest.permission
import android.Manifest.permission.CALL_PHONE
import android.annotation.SuppressLint
import android.support.v4.app.ActivityCompat
import android.os.Build
import com.beust.klaxon.JsonArray
import org.json.JSONArray
import org.json.JSONObject
import android.content.pm.ApplicationInfo
import com.bumptech.glide.Glide
import java.lang.Exception
import java.util.*




class ContactUs : Activity()
{



    var face = ""
    var twit = ""
    var inst = ""
    var yout = ""


    var BranchID = -1
    var BranchName =""
    var Addressss =""
    var ContactNumbers = ""
    var Longitude =""
    var Latitude = ""
    var Image = ""



    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)
        GetFollowAboutInfo()

    }


    override fun onResume()
    {
        super.onResume()
        catNamee.setTypeface(Utils.Exo2SemiBold(this))
        name.setTypeface(Utils.Exo2SemiBold(this))
        address.setTypeface(Utils.Exo2Medium(this))
        directionTxt.setTypeface(Utils.Exo2Bold(this))
        caltxt.setTypeface(Utils.Exo2Bold(this))
        shift.setTypeface(Utils.setExo2Regular(this))
    }

    fun back(view: View)
    {
        finish()
    }
    fun direction(view: View)
    {
        val my_data = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr="+Latitude+","+Longitude+BranchName)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(my_data))
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }

    fun facebook(view: View)
    {
        startActivity(Utils.getOpenFacebookIntent(this))
    }

    fun twiter(view: View)
    {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(twit)))
    }

    fun youtube(view: View)
    {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(yout)))
    }

    fun instegram(view: View)
    {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(inst)))
    }

    fun call(view: View)
    {
        try {
            val my_callIntent = Intent(Intent.ACTION_CALL)
            my_callIntent.data = Uri.parse("tel:19915")


            if(isPermissionGranted()){
                startActivity(my_callIntent)
            }
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(applicationContext, this.resources.getString(R.string.phoneerror) + e.message, Toast.LENGTH_LONG).show()
        }

    }




    fun GetFollowAboutInfo()
    {
        var makeRequest = MakeRequest("GetFollowAboutInfo?isArabic="+Utils.isArabic(this)+"&getAboutUs=false" ,"0",this,"GetFollowAboutInfo",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                var jsonObject = JSONObject(result.get("res").toString())
                var jsonArray = jsonObject.getJSONArray("FollowLinks")
                face = jsonArray.getJSONObject(0).getString("URL")
                twit = jsonArray.getJSONObject(1).getString("URL")
                yout = jsonArray.getJSONObject(2).getString("URL")
                inst = jsonArray.getJSONObject(3).getString("URL")




                var mainOfficeInfo = jsonObject.getJSONArray("MainOfficeInfo").getJSONObject(0)


                BranchID = mainOfficeInfo.getInt("BranchID")
                BranchName = mainOfficeInfo.getString("BranchName")
                Addressss = mainOfficeInfo.getString("Address")
                ContactNumbers = mainOfficeInfo.getString("ContactNumbers")
                Longitude = mainOfficeInfo.getString("Longitude")
                Latitude = mainOfficeInfo.getString("Latitude")
                Image = mainOfficeInfo.getString("Image")



                Glide.with(this@ContactUs).load(Image).thumbnail(.1f).into(image)
                address.setText(Addressss + "\n" + ContactNumbers)
                name.setText(this@ContactUs.resources.getString(R.string.contact_info))

            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {
                GetFollowAboutInfo()
            }
        })

    }


    fun isPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                return true
            } else {

                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1)
                return false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {

            1 -> {

                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val my_callIntent = Intent(Intent.ACTION_CALL)
                    my_callIntent.data = Uri.parse("tel:19915")
                    startActivity(my_callIntent)
                } else {
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }


}
