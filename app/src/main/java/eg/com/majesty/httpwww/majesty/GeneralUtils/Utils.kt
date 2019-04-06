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
        return Typeface.createFromAsset(context.assets, "fonts/fontawesome-webfont.ttf")
    }

    fun setExo2Regular(context: Context): Typeface {
        return Typeface.createFromAsset(context.assets, "fonts/Exo2-Regular.ttf")
    }


    fun Exo2Bold(context: Context): Typeface {
        return Typeface.createFromAsset(context.assets, "fonts/Exo2-Bold.ttf")
    }

    fun Exo2SemiBold(context: Context): Typeface {
        return Typeface.createFromAsset(context.assets, "fonts/Exo2-SemiBold.ttf")
    }


    fun Exo2Medium(context: Context): Typeface {
        return Typeface.createFromAsset(context.assets, "fonts/Exo2-Medium.ttf")
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


}


