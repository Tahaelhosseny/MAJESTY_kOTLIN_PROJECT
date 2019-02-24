package eg.com.majesty.httpwww.majesty.Activity

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import eg.com.majesty.httpwww.majesty.Adapters.AreaAdapter
import eg.com.majesty.httpwww.majesty.Adapters.CityAdapter
import eg.com.majesty.httpwww.majesty.Adapters.SubAreaAdapter
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.InterFaces.UpdateCity
import eg.com.majesty.httpwww.majesty.Models.AreaModel
import eg.com.majesty.httpwww.majesty.Models.CartModel
import eg.com.majesty.httpwww.majesty.Models.CityModel
import eg.com.majesty.httpwww.majesty.Models.SubAreaModel
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import kotlinx.android.synthetic.main.activity_add_new_place.*



class AddNewPlace : Activity(), SearchView.OnQueryTextListener {


    lateinit var adapter :CityAdapter
    lateinit var areaadapter :AreaAdapter
    lateinit var subareaadapter : SubAreaAdapter
    var  cityItemList : MutableList<CityModel> = arrayListOf()
    var  areaItemList : MutableList<AreaModel> = arrayListOf()
    var  subareaItemList : MutableList<SubAreaModel> = arrayListOf()

    var ID :String =""

    var cityId = -1
    var areaId = -1
    var subAreaa = -1
    var StreettStr=""
    var buildingNumberStr = ""
    var floorStr = ""
    var apartmentStr = ""
    var landmarkStr = ""
    var notesStr = ""




    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_place)
        citySearch.setOnQueryTextListener(this@AddNewPlace)
        areaSearch.setOnQueryTextListener(this@AddNewPlace)
        subareaSearch.setOnQueryTextListener(this@AddNewPlace)
        ID = ForeraaParameter(applicationContext).getString("UserID")

    }




    override fun onQueryTextSubmit(query: String?): Boolean
    {
        if(cityLayout.visibility == View.VISIBLE)
            adapter.filter.filter(query)

        if(areaLayout.visibility == View.VISIBLE)
            areaadapter.filter.filter(query)

        if(subareaLayout.visibility == View.VISIBLE)
            subareaadapter.filter.filter(query)

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean
    {
        if(cityLayout.visibility == View.VISIBLE)
            adapter.filter.filter(newText)

        if(areaLayout.visibility == View.VISIBLE)
            areaadapter.filter.filter(newText)

        if(subareaLayout.visibility == View.VISIBLE)
            subareaadapter.filter.filter(newText)
        return true
    }



    fun cityBack(view: View)
    {
        cityLayout.visibility = View.GONE
    }

    fun areaBack(view: View)
    {
        areaLayout.visibility = View.GONE
    }
    fun subareaBack(view: View)
    {
        subareaLayout.visibility = View.GONE
    }



    fun saveAddress(view: View)
    {

        StreettStr = Street.text.toString()
        buildingNumberStr = buildingNumber.text.toString()
        floorStr = floor.text.toString()
        apartmentStr = apartment.text.toString()
        landmarkStr = landmark.text.toString()
        notesStr = notes.text.toString()



        if(cityId == -1)
            city.setError("Please Select City")
        else if(areaId == -1)
            area.setError("Please Select Area")
        else if(subAreaa == -1)
            subArea.setError("Please Select SubArea")
        else if(StreettStr.equals(""))
        {
            Street.setError("Please Enter Street")
        }else if (buildingNumberStr.equals(""))
        {
            buildingNumber.setError("Please Enter BuildingNumber")
        }else if (floorStr.equals(""))
        {
            floor.setError("Please Enter FloorNumber")
        }else if (apartmentStr.equals(""))
        {
            apartment.setError("Please Enter ApartmentNumber")
        }else
        {
            saveAdd()
        }



    }



    fun city(view: View)
    {
        if(cityItemList.size == 0)
            getCites()
        else
            cityLayout.visibility = View.VISIBLE

    }




    fun area(view: View)
    {


        if(cityId == -1)
        {
            Toast.makeText(applicationContext , "Please Select City First" , Toast.LENGTH_LONG).show()
        }
        else
        {
            if(areaItemList.size == 0)
                getArea()
            else
                areaLayout.visibility = View.VISIBLE
        }



    }


    fun subArea(view: View)
    {


        if(areaId == -1)
        {
            Toast.makeText(applicationContext , "Please Select Area First" , Toast.LENGTH_LONG).show()
        }
        else
        {
            if(subareaItemList.size == 0)
                getSubAreas()
            else
                subareaLayout.visibility = View.VISIBLE
        }



    }




    fun getCites()
    {
        var makeRequest = MakeRequest("GetCities?isArabic=false" ,"0",this,"GetCities",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                var str = result.get("res").toString()
                var jsonObject = Gson().fromJson(str, JsonArray::class.java)
                val itemType = object : TypeToken<List<CityModel>>() {}.type
                cityItemList = Gson().fromJson<MutableList<CityModel>>(jsonObject.toString(), itemType)
                adapter = CityAdapter(this@AddNewPlace , cityItemList , object : UpdateCity{
                    override fun update(cityId: Int ,  CityName : String)
                    {
                        cityLayout.visibility = View.GONE
                        city.setText(CityName)
                        this@AddNewPlace.cityId =cityId
                    }
                })
                cityRec.layoutManager = LinearLayoutManager(this@AddNewPlace)
                cityRec.adapter = adapter
                adapter.notifyDataSetChanged()
                cityLayout.visibility = View.VISIBLE
            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })
    }


    fun getArea()
    {
        var makeRequest = MakeRequest("GetAreas?isArabic=false&cityId=" + cityId,"0",this,"GetCities",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                var str = result.get("res").toString()
                var jsonObject = Gson().fromJson(str, JsonArray::class.java)
                val itemType = object : TypeToken<List<AreaModel>>() {}.type
                areaItemList = Gson().fromJson<MutableList<AreaModel>>(jsonObject.toString(), itemType)
                areaadapter = AreaAdapter(this@AddNewPlace , areaItemList , object : UpdateCity{
                    override fun update(cityId: Int ,  CityName : String)
                    {
                        areaLayout.visibility = View.GONE
                        area.setText(CityName)
                        this@AddNewPlace.areaId =cityId
                    }
                })
                areaRec.layoutManager = LinearLayoutManager(this@AddNewPlace)
                areaRec.adapter = areaadapter
                areaadapter.notifyDataSetChanged()
                areaLayout.visibility = View.VISIBLE
            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })
    }



    fun getSubAreas()
    {
        var makeRequest = MakeRequest("GetSubAreas?isArabic=false&areaId=" + areaId,"0",this,"GetCities",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                var str = result.get("res").toString()
                var jsonObject = Gson().fromJson(str, JsonArray::class.java)
                val itemType = object : TypeToken<List<SubAreaModel>>() {}.type
                subareaItemList = Gson().fromJson<MutableList<SubAreaModel>>(jsonObject.toString(), itemType)
                subareaadapter = SubAreaAdapter(this@AddNewPlace , subareaItemList , object : UpdateCity{
                    override fun update(cityId: Int ,  CityName : String)
                    {
                        subareaLayout.visibility = View.GONE
                        subArea.setText(CityName)
                        this@AddNewPlace.subAreaa =cityId
                    }
                })
                subareaRec.layoutManager = LinearLayoutManager(this@AddNewPlace)
                subareaRec.adapter = subareaadapter
                subareaadapter.notifyDataSetChanged()
                subareaLayout.visibility = View.VISIBLE
            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })
    }


    fun saveAdd()
    {
        var makeRequest = MakeRequest("AddUserAddress?isArabic=false&subAreaId=" + subAreaa
                + "&userId=" +ID
                + "&street=" + StreettStr
                + "&buildingNumber=" + buildingNumberStr
                + "&floor=" + floorStr
                + "&apartmentNumber=" + apartmentStr
                + "&landmark=" + landmarkStr
                + "&notes=" + notesStr,"0",this,"AddUserAddress",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                var str = result.get("res").toString()
                var jsonObject = Gson().fromJson(str, JsonObject::class.java)
                if( jsonObject.get("NotificationsNumbers").asJsonArray.get(0).asJsonObject.get("Succeed").asBoolean)
                {
                    Toast.makeText(this@AddNewPlace , "Address Add Successfully !!" , Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })
    }

}



