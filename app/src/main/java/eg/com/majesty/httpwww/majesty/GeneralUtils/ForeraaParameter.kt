/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package eg.com.majesty.httpwww.majesty.GeneralUtils

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Taha El_hosseny on 9/12/2017.
 */
class ForeraaParameter
{

    internal var con: Context


    var ForeraaSettings: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

    constructor(con :Context)
    {
        this.con = con ;
    }




    private fun openConnection() {
        ForeraaSettings = con.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        editor = ForeraaSettings!!.edit()
    }

    private fun closeConnection() {
        ForeraaSettings = null
        editor = null
    }

    fun setString(key: String, value: String) {
        openConnection()
        editor!!.putString(key, value)
        editor!!.commit()
        closeConnection()
    }

    fun getString(key: String, strdefault: String): String {
        var result: String = strdefault
        openConnection()

        if (ForeraaSettings!!.contains(key)) {
            result = ForeraaSettings!!.getString(key, strdefault)
        }

        closeConnection()
        return result
    }

    fun getString(key: String): String {
        var result: String = ""
        openConnection()
        if (ForeraaSettings!!.contains(key)) {
            result = ForeraaSettings!!.getString(key, "")
        }
        closeConnection()
        return result
    }

    fun setInt(key: String, value: Int) {
        openConnection()
        editor!!.putInt(key, value)
        editor!!.commit()
        closeConnection()
    }

    fun getInt(key: String): Int {
        var x = 0
        openConnection()
        if (ForeraaSettings!!.contains(key)) {
            x = ForeraaSettings!!.getInt(key, 0)
        }
        closeConnection()
        return x
    }


    fun getInt(key: String , normal : Int): Int {
        var x = 0
        openConnection()
        if (ForeraaSettings!!.contains(key)) {
            x = ForeraaSettings!!.getInt(key, normal)
        }
        closeConnection()
        return x
    }

    fun setBoolean(key: String, value: Boolean) {
        openConnection()
        editor!!.putBoolean(key, value)
        editor!!.commit()
        closeConnection()
    }

    fun getBoolean(key: String): Boolean {
        var result = false
        openConnection()
        if (ForeraaSettings!!.contains(key)) {
            result = ForeraaSettings!!.getBoolean(key, false)
        }
        closeConnection()
        return result
    }


    fun isSetLanguage(): Boolean {
        var result = false
        openConnection()

        if (ForeraaSettings!!.contains("language")) {
            result = true
        }

        closeConnection()
        return result
    }


    companion object {
        val APP_PREFERENCES = "MajestyAppShared"
    }

}
