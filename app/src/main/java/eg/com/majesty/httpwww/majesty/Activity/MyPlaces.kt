package eg.com.majesty.httpwww.majesty.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import eg.com.majesty.httpwww.majesty.Adapters.MyPlacesAdapter
import eg.com.majesty.httpwww.majesty.Adapters.SubAreaAdapter
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.InterFaces.UpdateCity
import eg.com.majesty.httpwww.majesty.Models.CityModel
import eg.com.majesty.httpwww.majesty.Models.SubAreaModel
import eg.com.majesty.httpwww.majesty.Models.UserAddressAsLines
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import kotlinx.android.synthetic.main.activity_add_new_place.*
import kotlinx.android.synthetic.main.activity_my_places.*
import org.androidannotations.annotations.Click

class MyPlaces : AppCompatActivity()
{



    var ID :String =""
    var  userAddressAsLinesList : MutableList<UserAddressAsLines> = arrayListOf()



    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_places)
        ID = ForeraaParameter(applicationContext).getString("UserID")


    }



    fun addNewAddress(view : View)
    {
        startActivity(Intent(this , AddNewPlace::class.java))
    }
    fun footer(view : View)
    {
        startActivity(Intent(this , FinalCheckOut::class.java))
    }






    fun getUserAddressesAsLines()
    {
        var makeRequest = MakeRequest("GetUserAddressesAsLines?isArabic=false&userId=" + ID,"0",this,"getUserAddressesAsLines",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                var str = result.get("res").toString()
                var jsonObject = Gson().fromJson(str, JsonObject::class.java)



                val itemType = object : TypeToken<List<UserAddressAsLines>>() {}.type
                userAddressAsLinesList = Gson().fromJson<MutableList<UserAddressAsLines>>(jsonObject.get("UserAddressAsLines").asJsonArray.toString(), itemType)
                var myPlacesAdapter = MyPlacesAdapter(this@MyPlaces ,userAddressAsLinesList , object : UpdateCity{
                    override fun update(cityId: Int, CityName: String)
                    {
                    }
                })



                places.layoutManager = LinearLayoutManager(this@MyPlaces)
                places.adapter = myPlacesAdapter
                myPlacesAdapter.notifyDataSetChanged()



            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })

    }


    override fun onResume()
    {
        super.onResume()
        getUserAddressesAsLines()

    }



}
