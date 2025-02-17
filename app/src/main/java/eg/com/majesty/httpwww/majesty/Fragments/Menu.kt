package eg.com.majesty.httpwww.majesty.Fragments


import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import eg.com.majesty.httpwww.majesty.Adapters.CategoryItem
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.Models.CategoryModels
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_menu.*
import org.json.JSONObject


class Menu : Fragment()
{

    var TAG = "Menu"
    var  isArabic = false

    var ID :String =""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)
    }



    override fun onResume() {
        super.onResume()
        if(ForeraaParameter(activity).getInt("language" , 0) ==0)
        {
            isArabic = true
        }
        else
        {
            isArabic = false
        }
        activity.back.visibility = View.VISIBLE
        activity.menu.visibility = View.INVISIBLE
        activity.header.visibility = View.VISIBLE
        activity.cart.visibility = View.VISIBLE
        activity.bottom.visibility = View.VISIBLE
        activity.headerText.setText(R.string.Menu)
        activity.homeIm.setImageResource(R.drawable.icon_home)
        activity.favoriteIm.setImageResource(R.drawable.favorite)
        activity.ordersIm.setImageResource(R.drawable.ordera)
        activity.menuIm.setImageResource(R.drawable.menu1)

        loadData()





    }



    override fun onDestroy()
    {
        super.onDestroy()
        activity.back.visibility = View.GONE
        activity.menu.visibility = View.VISIBLE

    }




    fun loadData()
    {
        var foreraaParameter = ForeraaParameter(activity)

        try
        {
            ID = foreraaParameter.getString("UserID")

        }catch (e : Exception){}


        var makeRequest = MakeRequest("GetCategories?isArabic="+isArabic+"&userIDorPassNothing=" + ID,"0",activity,"GetCategories",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                val gson = Gson()

                var str = result.get("res")


                var jsonObject = Gson().fromJson(str, JsonObject::class.java)

                var notificationNumbers = jsonObject.getAsJsonArray("NotificationNumbers").get(0).asJsonObject

                activity.notiNum.text = notificationNumbers.get("NotificationsCount").toString()
                activity.cartTxt.text = notificationNumbers.get("CartItemsCount").toString()

                val itemType = object : TypeToken<List<CategoryModels>>() {}.type
                val itemList = gson.fromJson<List<CategoryModels>>(jsonObject.get("Categiries").toString(), itemType)
                cat_item.layoutManager = LinearLayoutManager(activity)
                cat_item.adapter = CategoryItem(activity ,itemList ,"menu")
                cat_item.adapter.notifyDataSetChanged()
            }
        } ,object :ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {
                loadData()
            }
        })



    }







}



