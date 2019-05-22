package eg.com.majesty.httpwww.majesty.GeneralUtils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.format.DateFormat
import android.util.Log
import eg.com.majesty.httpwww.majesty.BuildConfig
import java.util.*
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import java.security.MessageDigest

import android.util.Base64
import android.R.attr.label
import android.content.ClipData
import android.text.ClipboardManager


/**
 * Created by Taha on 4/3/2018.
 */
object Utils {

    val TAG = "Utils"

    var IsDev: Boolean? = false


    var DevUrl = "https://demo.majesty.com.eg/ws/mjws.asmx/"
    var ReleaseUrl = "https://demo.majesty.com.eg/ws/mjws.asmx/"


    val kiwihatParentURL: String
        get() {


            if (IsDev!!) {
                if (BuildConfig.DEBUG) {
                    Log.e("link is state 1 ", DevUrl)
                    return DevUrl
                } else {
                    Log.e("link is state 2 ", ReleaseUrl)

                    return ReleaseUrl
                }
            } else {
                if (BuildConfig.DEBUG) {
                    Log.e("link is state 3 ", DevUrl)

                    return DevUrl
                } else {
                    Log.e("link is state 4 ", ReleaseUrl)

                    return ReleaseUrl
                }
            }
        }


    fun isOnline(con: Context): Boolean {
        var connected = false
        val connectivityManager = con.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        if (netInfo != null) {

            /* if (netInfo.getTypeName().equalsIgnoreCase("WIFI")) {
                connected = true;
            } else if (netInfo.getTypeName().equalsIgnoreCase("MOBILE")) {
                connected = true;
            }*/
            if (netInfo.isConnected) {
                connected = true
            }
        }
        return connected
    }


    fun permissionCheck(activity: Activity, permission: String): Boolean {
        return if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            true
        } else
            false
    }

    fun permissionGrant(activity: Activity, permission: String, permissionCode: Int) {
        ActivityCompat.requestPermissions(activity, arrayOf(permission), permissionCode)
    }

    fun changeFont(con: Context, loc: Int): Typeface? {
        var typeface: Typeface? = null
        if (loc == 0) {
            typeface = Typeface.createFromAsset(con.assets, "fonts/alfares.ttf")
        }
        return typeface
    }

    fun setFontawesome(context: Context): Typeface {


        if(ForeraaParameter(context).getInt("language" ,0) ==0)
        {
            return Typeface.createFromAsset(context.assets, "fonts/fontawesome-webfont.ttf")
        }else
        {
            return Typeface.createFromAsset(context.assets, "fonts/fontawesome-webfont.ttf")
        }



    }

    fun setExo2Regular(context: Context): Typeface
    {



        if(ForeraaParameter(context).getInt("language" ,0) ==0)
        {
            return Typeface.createFromAsset(context.assets, "fonts/cairo_regular.ttf")
        }else
        {
            return Typeface.createFromAsset(context.assets, "fonts/Exo2-Regular.ttf")
        }



    }


    fun Exo2Bold(context: Context): Typeface {


        if(ForeraaParameter(context).getInt("language" ,0) ==0)
        {
            return Typeface.createFromAsset(context.assets, "fonts/cairo_black.ttf")
        }else
        {
            return Typeface.createFromAsset(context.assets, "fonts/Exo2-Bold.ttf")
        }



    }

    fun Exo2SemiBold(context: Context): Typeface {



        if(ForeraaParameter(context).getInt("language" ,0) ==0)
        {
            return Typeface.createFromAsset(context.assets, "fonts/cairo_bold.ttf")
        }else
        {
            return Typeface.createFromAsset(context.assets, "fonts/Exo2-SemiBold.ttf")
        }



    }



    fun isArabic(con: Context) :String
    {
        if(ForeraaParameter(con).getInt("language" , 0) ==0)
        {
            return "true"
        }
        else
        {
            return  "false"
        }
    }



    fun isArabicBoolean(con: Context) :Boolean
    {
        if(ForeraaParameter(con).getInt("language" , 0) ==0)
        {
            return true
        }
        else
        {
            return  false
        }
    }


    fun Exo2Medium(context: Context): Typeface {


        if(ForeraaParameter(context).getInt("language" ,0) ==0)
        {
            return Typeface.createFromAsset(context.assets, "fonts/cairo_semibold.ttf")
        }else
        {
            return Typeface.createFromAsset(context.assets, "fonts/Exo2-Medium.ttf")
        }



    }



    fun changNumToArabic(input: String): String {
        /* char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (Character.isDigit(input.charAt(i))) {
                try {
                    builder.append(arabicChars[(int) (input.charAt(i)) - 48]);
                } catch (Exception e) {
                }

            }

        }
            if (builder.toString().length() > 0)
                return builder.toString();
            else*/
        return input

    }

    fun getDate(time: Long): String {
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = time * 1000L
        val date = DateFormat.format("dd-MM-yyyy", cal).toString()
        return date.replace("-","/")
    }


    fun getDateHHMM(time: Long): String {
        val cal = Calendar.getInstance()
        cal.timeInMillis = time * 1000L
        val date = DateFormat.format("hh:mm a", cal).toString()
        return date
    }
    fun getWatingTime(time: Long): String
    {
        var min = time / 60
        var sec = time %60

        var minstr = min.toString()
        var secstr = sec.toString()


        if(min<10)
        {
            minstr = "0"+min
        }


        if(sec<10)
        {
            secstr = "0"+sec
        }

        return minstr + ":" +secstr
    }




    fun showOkDialoge(activity: Activity, msg: String, listenerPos: DialogInterface.OnClickListener, txtbtn: String) {
        try {
            val builder = AlertDialog.Builder(activity)
            builder.setMessage(msg)
                    .setCancelable(true)
                    .setPositiveButton(txtbtn, listenerPos)
            val alert = builder.create()
            alert.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun show2OptionsDialoge(activity: Activity, msg: String, listenerPos: DialogInterface.OnClickListener, listenerNeg: DialogInterface.OnClickListener, txtbtnP: String, txtbtnN: String) {
        try {
            val builder = AlertDialog.Builder(activity)
            builder.setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton(txtbtnP, listenerPos)
                    .setNegativeButton(txtbtnN, listenerNeg)
            val alert = builder.create()
            alert.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun getOpenFacebookIntent(context: Context): Intent
    {
        val FACEBOOK_URL = "https://www.facebook.com/Majesty.19915eg"
        val FACEBOOK_PAGE_ID = "Majesty.19915eg"
        val facebookIntent = Intent(Intent.ACTION_VIEW)
        val packageManager = context.packageManager
        try {
            val versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode
            return if (versionCode >= 3002850)
            { //newer versions of fb app
                facebookIntent.setData(Uri.parse("fb://facewebmodal/f?href=$FACEBOOK_URL"))
            } else { //older versions of fb app
                facebookIntent.setData(Uri.parse("fb://page/$FACEBOOK_PAGE_ID"))

            }
        } catch (e: PackageManager.NameNotFoundException)
        {
            return facebookIntent.setData(Uri.parse(FACEBOOK_URL))  //normal web url
        }
    }




    fun getOpenTwiterrIntent(context: Context): Intent
    {
        val FACEBOOK_URL = "https://www.facebook.com/Majesty.19915eg"
        val FACEBOOK_PAGE_ID = "Majesty.19915eg"
        val facebookIntent = Intent(Intent.ACTION_VIEW)
        val packageManager = context.packageManager
        try {
            val versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode
            return if (versionCode >= 3002850)
            { //newer versions of fb app
                facebookIntent.setData(Uri.parse("fb://facewebmodal/f?href=$FACEBOOK_URL"))
            } else { //older versions of fb app
                facebookIntent.setData(Uri.parse("fb://page/$FACEBOOK_PAGE_ID"))

            }
        } catch (e: PackageManager.NameNotFoundException)
        {
            return facebookIntent.setData(Uri.parse(FACEBOOK_URL))  //normal web url
        }
    }


    fun changeLocale(con: Context, language: String?) {
        val res = con.resources
        val config = res.configuration
        if (language == null || language.length == 0) {
            config.locale = Locale.getDefault()
        } else {
            config.locale = Locale(language)
        }
        res.updateConfiguration(config, null)
    }


    fun getKeyHash(context: Context): String?
    {
        val info = context.packageManager.getPackageInfo("eg.com.majesty.httpwww.majesty", PackageManager.GET_SIGNATURES)
        for (signature in info.signatures)
        {
            val md = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.text.ClipboardManager
                clipboard.text =  Base64.encodeToString(md.digest(), Base64.DEFAULT)
            } else {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                val clip = ClipData.newPlainText("Copied Text",  Base64.encodeToString(md.digest(), Base64.DEFAULT))
                clipboard.primaryClip = clip
            }

        }
        return null
    }

}
