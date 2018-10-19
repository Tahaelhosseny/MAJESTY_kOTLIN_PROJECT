package eg.com.majesty.httpwww.majesty.Fragments

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import eg.com.majesty.httpwww.majesty.Adapters.GetFoodMenus
import eg.com.majesty.httpwww.majesty.Models.GetFoodMenusModel

import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_menu.*


class FreshOffers : Fragment()
{




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_load_cat_items, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)
        loadData()

    }



    fun loadData()
    {
        var makeRequest = MakeRequest("GetFoodMenusHomeScreenAll?isArabic=false","0",activity,"GetFoodMenuTypes",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                Log.e("responce" , result.get("res"))
                val gson = Gson()
                val itemType = object : TypeToken<List<GetFoodMenusModel>>() {}.type
                val itemList = gson.fromJson<List<GetFoodMenusModel>>(result.get("res").toString(), itemType)
                cat_item.layoutManager = LinearLayoutManager(activity)
                cat_item.adapter = GetFoodMenus(activity ,itemList ,"n")
                cat_item.adapter.notifyDataSetChanged()
            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })








    }


    override fun onDestroy()
    {
        super.onDestroy()
        activity.back.visibility = View.GONE
        activity.menu.visibility = View.VISIBLE

    }


    override fun onResume() {
        super.onResume()
        activity.back.visibility = View.VISIBLE
        activity.menu.visibility = View.INVISIBLE
        activity.headerText.setText("Fresh Offers")
    }


}
