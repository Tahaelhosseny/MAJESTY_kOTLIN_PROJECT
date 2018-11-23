package eg.com.majesty.httpwww.majesty.Fragments

import android.app.Fragment
import android.app.FragmentTransaction
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import eg.com.majesty.httpwww.majesty.Adapters.CategoryItem
import eg.com.majesty.httpwww.majesty.Adapters.MenuFoodDataAdapter
import eg.com.majesty.httpwww.majesty.Models.CategoryModels
import eg.com.majesty.httpwww.majesty.Models.MenuFoodDataModel
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONArray
import java.util.HashMap

class Home : Fragment()
{



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }



    override fun onResume() {
        super.onResume()
        activity.headerText.setText("Home")
        activity.back.visibility = View.GONE
        activity.menu.visibility = View.VISIBLE
    }


    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)


        fresh.setOnClickListener( object : View.OnClickListener
        {
            override fun onClick(v: View?)
            {
                val freshOffers = FreshOffers()
                activity.headerText.setText("Fresh Offers")
                val fragmentTransaction = activity.fragmentManager.beginTransaction()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.replace(R.id.frameContainer,freshOffers )
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                fragmentTransaction.commit()
            }
        })



        popular.setOnClickListener( object : View.OnClickListener
        {
            override fun onClick(v: View?)
            {
                val popularFood = PopularFood()
                activity.headerText.setText("Popular Food")
                val fragmentTransaction = activity.fragmentManager.beginTransaction()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.replace(R.id.frameContainer,popularFood )
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                fragmentTransaction.commit()

            }
        })

        requestData()


    }





    fun requestData()
    {
        var mapt = HashMap<String , String>()

        var makeRequest = MakeRequest("GetHomeScreenData?isArabic=false&categoryCount=5&itemCount=5","0",mapt ,activity,"GetFoodMenuTypes",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                var str = result.get("res")
                var jsonArray = Gson().fromJson(str, JsonArray::class.java)
                var jsonObject = jsonArray.get(0).asJsonObject

                val gson = Gson()
                val itemType = object : TypeToken<List<CategoryModels>>() {}.type
                val itemList = gson.fromJson<List<CategoryModels>>(jsonObject.getAsJsonArray("MenuFoodTypeData").toString(), itemType)
                popularRec.layoutManager = LinearLayoutManager(activity , LinearLayoutManager.HORIZONTAL , false)
                popularRec.adapter = CategoryItem(activity ,itemList ,"home")
                popularRec.adapter.notifyDataSetChanged()




                val itemType2 = object : TypeToken<List<MenuFoodDataModel>>() {}.type
                val itemList2 = gson.fromJson<List<MenuFoodDataModel>>(jsonObject.getAsJsonArray("MenuFoodData").toString(), itemType2)
                freshRec.layoutManager = LinearLayoutManager(activity , LinearLayoutManager.HORIZONTAL , false)
                freshRec.adapter = MenuFoodDataAdapter(activity ,itemList2 ,"home")
                freshRec.adapter.notifyDataSetChanged()
            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })







    }
}
