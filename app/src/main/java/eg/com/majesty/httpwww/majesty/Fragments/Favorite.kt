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
import eg.com.majesty.httpwww.majesty.Activity.Login_
import eg.com.majesty.httpwww.majesty.Adapters.CategoryItem
import eg.com.majesty.httpwww.majesty.Adapters.GetFoodMenus
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.Models.CategoryModels
import eg.com.majesty.httpwww.majesty.Models.GetFoodMenusModel
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_favorite.*

class Favorite : Fragment()
{


    var ID :String =""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)


        var foreraaParameter = ForeraaParameter(activity)

        try
        {
            ID = foreraaParameter.getString("UserID")

        }catch (e : Exception){}



            var makeRequest = MakeRequest("FavouritesGetAllItems?isArabice=false&userID=" + ID, "0", activity, "GetFoodMenuTypes", true)
            makeRequest.request(object : VolleyCallback {
                override fun onSuccess(result: Map<String, String>)
                {

                    val gson = Gson()

                    var str = result.get("res")


                    var jsonObject = Gson().fromJson(str, JsonObject::class.java)
                    var notificationNumbers = jsonObject.getAsJsonArray("NotificationNumbers").get(0).asJsonObject

                    activity.notiNum.text = notificationNumbers.get("NotificationsCount").toString()
                    activity.cartTxt.text = notificationNumbers.get("CartItemsCount").toString()

                    val itemType = object : TypeToken<List<GetFoodMenusModel>>() {}.type
                    val itemList = gson.fromJson<List<GetFoodMenusModel>>(jsonObject.get("FavouriteItems").toString(), itemType)

                    fav_item.layoutManager = LinearLayoutManager(activity)
                    fav_item.adapter = GetFoodMenus(activity, itemList, "Favorites")
                    fav_item.adapter.notifyDataSetChanged()
                }
            }, object : ONRetryHandler {
                override fun onRetryHandler(funName: String) {

                }
            })


        }












    override fun onResume() {
        super.onResume()
        activity.back.visibility = View.GONE
        activity.menu.visibility = View.VISIBLE
    }
}
