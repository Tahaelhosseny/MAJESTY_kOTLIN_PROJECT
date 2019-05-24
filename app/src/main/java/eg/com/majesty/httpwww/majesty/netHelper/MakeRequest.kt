package eg.com.majesty.httpwww.majesty.netHelper

import android.app.AlertDialog
import android.content.Context

import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.HashMap

import dmax.dialog.SpotsDialog
import eg.com.majesty.httpwww.majesty.Dialogs.ConnectionLost
import eg.com.majesty.httpwww.majesty.Dialogs.LoadingDialog
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.R

/**
 * Created by Taha on 3/10/2018.
 */

class MakeRequest {


    internal var BaseUrl = Utils.kiwihatParentURL //www.example.com

    internal var Sub_Url: String //.../example/example

    internal var Type: String    // post Or Get   1 for POST 0 For GET

    internal lateinit var cUrl: String  //Base+Sub

    internal var map: Map<String, String> = HashMap()  //parameter send to server

    internal var context: Context
    internal var Responce: MutableMap<String, String> = HashMap()

    internal var funName: String
    internal var flag: Boolean? = false


    constructor(sub_Url: String, Type: String, map: Map<String, String>, context: Context, funName: String, flag: Boolean?) {

        this.Sub_Url = sub_Url
        this.Type = Type
        this.map = map
        this.context = context
        cUrl = BaseUrl + Sub_Url

        this.flag = flag
        this.funName = funName


    }


    constructor(sub_Url: String, Type: String, context: Context, funName: String, flag: Boolean?) {


        this.Sub_Url = sub_Url
        this.Type = Type
        this.map = map
        this.context = context
        this.funName = funName
        this.flag = flag
        cUrl = BaseUrl + sub_Url

    }


    fun request(callback: VolleyCallback, onRetryHandler: ONRetryHandler)
    {


        if (!Utils.isOnline(context))
        {
            ConnectionLost(context , onRetryHandler).show()
        } else {
           var loadingDialog =  LoadingDialog(context)
            if (flag!!)
                loadingDialog.show()
            val stringRequest = object : StringRequest(Integer.valueOf(Type), cUrl, Response.Listener
            { response ->
                Responce["status"] = "ok"
                Responce["res"] = response
                loadingDialog.dismiss()
                callback.onSuccess(Responce)
            }, Response.ErrorListener { error ->
                Responce["status"] = "not"
                Responce["res"] = error.toString()
                loadingDialog.dismiss()
                callback.onSuccess(Responce)
            }){}

            stringRequest.retryPolicy = DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            VolleySinglton.getInstance(context).addToRequestQueue(stringRequest)

        }


    }



}
