package eg.com.majesty.httpwww.majesty.Activity

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
import eg.com.majesty.httpwww.majesty.Adapters.CityAdapter
import eg.com.majesty.httpwww.majesty.Models.CartModel
import eg.com.majesty.httpwww.majesty.Models.CityModel
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import kotlinx.android.synthetic.main.activity_add_new_place.*

class AddNewPlace : AppCompatActivity(), SearchView.OnQueryTextListener {


    lateinit var adapter :CityAdapter
    lateinit var  itemList : MutableList<CityModel>


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_place)


        citySearch.setOnQueryTextListener(this@AddNewPlace)
    }




    override fun onQueryTextSubmit(query: String?): Boolean
    {

        adapter.setFilter(query.toString())

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean
    {
        adapter.setFilter(newText.toString())

        return true
    }



    fun cityBack(view: View)
    {
        cityLayout.visibility = View.GONE
    }
    fun city(view: View)
    {

        if(itemList== null)
            getCites()
        else
            cityLayout.visibility = View.VISIBLE

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
                itemList = Gson().fromJson<MutableList<CityModel>>(jsonObject.toString(), itemType)

                adapter = CityAdapter(this@AddNewPlace , itemList)
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



}



