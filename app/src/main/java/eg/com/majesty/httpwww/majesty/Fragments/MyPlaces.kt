package eg.com.majesty.httpwww.majesty.Fragments

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import eg.com.majesty.httpwww.majesty.Activity.AddNewPlace
import eg.com.majesty.httpwww.majesty.Adapters.MyPlacesAdapter
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.InterFaces.UpdateCity
import eg.com.majesty.httpwww.majesty.Models.UserAddressAsLines
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_my_places.*

class MyPlaces : Fragment()
{

    var TAG = "MyPlaces"


    var ID :String =""
    var isAddressBok = false
    var  userAddressAsLinesList : MutableList<UserAddressAsLines> = arrayListOf()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_my_places, container, false)
    }








    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

    }


    fun setData(isAddressBok :Boolean)
    {
        this.isAddressBok =isAddressBok

    }








    fun addNewAddress()
    {
        activity.startActivity(Intent(activity , AddNewPlace::class.java))
    }
    fun footer(view : View)
    {
    }






    fun getUserAddressesAsLines()
    {
        var makeRequest = MakeRequest("GetUserAddressesAsLines?isArabic="+Utils.isArabic(activity)+"&userId=" + ID,"0",activity,"getUserAddressesAsLines",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                var str = result.get("res").toString()
                var jsonObject = Gson().fromJson(str, JsonObject::class.java)

                val itemType = object : TypeToken<List<UserAddressAsLines>>() {}.type
                userAddressAsLinesList = Gson().fromJson<MutableList<UserAddressAsLines>>(jsonObject.get("UserAddressAsLines").asJsonArray.toString(), itemType)
                var myPlacesAdapter = MyPlacesAdapter(activity ,userAddressAsLinesList ,isAddressBok, object : UpdateCity{
                    override fun update(cityId: Int, CityName: String)
                    {
                    }
                })



                places.layoutManager = LinearLayoutManager(activity)
                places.adapter = myPlacesAdapter
                myPlacesAdapter.notifyDataSetChanged()



            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {
                getUserAddressesAsLines()
            }
        })

    }


    override fun onResume()
    {
        super.onResume()
        activity.header.visibility = View.GONE
        activity.cart.visibility = View.GONE
        activity.bottom.visibility = View.GONE
        activity.homeIm.setImageResource(R.drawable.icon_home)
        activity.favoriteIm.setImageResource(R.drawable.favorite)
        activity.ordersIm.setImageResource(R.drawable.ordera)
        activity.menuIm.setImageResource(R.drawable.menu)
        ID = ForeraaParameter(activity).getString("UserID")
        if(isAddressBok)
        {
            catNamee.text = activity.resources.getString(R.string.select_address)
        }else
        {
            catNamee.text = activity.resources.getString(R.string.address_book)
        }
        backNow.setOnClickListener { activity.onBackPressed() }


        addNewAddress.setOnClickListener { addNewAddress() }

        getUserAddressesAsLines()

    }



}
