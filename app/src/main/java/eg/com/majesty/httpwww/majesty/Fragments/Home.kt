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
import eg.com.majesty.httpwww.majesty.Activity.MainActivity
import eg.com.majesty.httpwww.majesty.Adapters.CategoryItem
import eg.com.majesty.httpwww.majesty.Adapters.MenuFoodDataAdapter
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
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
    var TAG = "Home"


    var ID :String =""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }



    override fun onResume() {
        super.onResume()


        activity.headerText.setText("Home")
        activity.back.visibility = View.GONE
        activity.menu.visibility = View.VISIBLE
        activity.header.visibility = View.VISIBLE
        activity.cart.visibility = View.VISIBLE
        activity.bottom.visibility = View.VISIBLE


        activity.headerText.setText(R.string.Home)
        activity.homeIm.setImageResource(R.drawable.icon_home1)
        activity.favoriteIm.setImageResource(R.drawable.favorite)
        activity.ordersIm.setImageResource(R.drawable.ordera)
        activity.menuIm.setImageResource(R.drawable.menu)


        var foreraaParameter = ForeraaParameter(activity)

        try
        {
            ID = foreraaParameter.getString("UserID")

        }catch (e : Exception){}



        fresh.setOnClickListener( object : View.OnClickListener
        {
            override fun onClick(v: View?)
            {
                var freshOffers = FreshOffers()
                activity.headerText.setText(activity.resources.getString(R.string.fresh_offers))
                var fragmentTransaction = activity.fragmentManager.beginTransaction()
                fragmentTransaction.addToBackStack("freshOffers")
                fragmentTransaction.replace(R.id.frameContainer,freshOffers )
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                fragmentTransaction.commit()
            }
        })



        popular.setOnClickListener( object : View.OnClickListener
        {
            override fun onClick(v: View?)
            {
                var popularFood = PopularFood()
                MainActivity().activeCenterFragments.add(popularFood)
                activity.headerText.setText(activity.resources.getString(R.string.popular_food))
                var fragmentTransaction = activity.fragmentManager.beginTransaction()
                fragmentTransaction.addToBackStack("popularFood")
                fragmentTransaction.replace(R.id.frameContainer,popularFood )
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                fragmentTransaction.commit()

            }
        })

        requestData()


    }


    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)



    }






    fun requestData()
    {
        var mapt = HashMap<String , String>()

        var makeRequest = MakeRequest("GetHomeScreenData?isArabic="+Utils.isArabic(activity)+"&categoryCount=5&itemCount=5&userIDorPassNothing=" +ID,"0",mapt ,activity,"_Categories",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                var str = result.get("res")



                var jsonArray = Gson().fromJson(str, JsonArray::class.java)
                var jsonObject = jsonArray.get(0).asJsonObject
                var notificationNumbers = jsonObject.getAsJsonArray("NotificationNumbers").get(0).asJsonObject

                activity.notiNum.text = notificationNumbers.get("NotificationsCount").toString()
                activity.cartTxt.text = notificationNumbers.get("CartItemsCount").toString()

                var gson = Gson()
                var itemType = object : TypeToken<List<CategoryModels>>() {}.type
                var itemList = gson.fromJson<List<CategoryModels>>(jsonObject.getAsJsonArray("_Categories").toString(), itemType)

                if(ForeraaParameter(activity).getInt("language")==0)
                {
                    popularRec.layoutManager = LinearLayoutManager(activity , LinearLayoutManager.HORIZONTAL , true)

                }else
                {
                    popularRec.layoutManager = LinearLayoutManager(activity , LinearLayoutManager.HORIZONTAL , false)

                }


                popularRec.adapter = CategoryItem(activity ,itemList ,"home")
                popularRec.adapter.notifyDataSetChanged()




                var itemType2 = object : TypeToken<List<MenuFoodDataModel>>() {}.type
                var itemList2 = gson.fromJson<List<MenuFoodDataModel>>(jsonObject.getAsJsonArray("_HotItems").toString(), itemType2)
                if(ForeraaParameter(activity).getInt("language")==0)
                {
                    freshRec.layoutManager = LinearLayoutManager(activity , LinearLayoutManager.HORIZONTAL , true)

                }else
                {
                    freshRec.layoutManager = LinearLayoutManager(activity , LinearLayoutManager.HORIZONTAL , false)

                }
                freshRec.adapter = MenuFoodDataAdapter(activity ,itemList2 ,"home")
                freshRec.adapter.notifyDataSetChanged()
            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {
                requestData()
            }
        })







    }
}
