package eg.com.majesty.httpwww.majesty.Fragments
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import eg.com.majesty.httpwww.majesty.Activity.Login_
import eg.com.majesty.httpwww.majesty.Adapters.CategoryItem
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.Models.CategoryModels
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_favorite.*

class Favorite : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)


        var foreraaParameter = ForeraaParameter(activity)



        if(foreraaParameter.getString("UserID").length==0)
        {
            startActivity(Intent(activity , Login_::class.java).putExtra("finish" , true))
        }
        else {

            var makeRequest = MakeRequest("GetFoodMenuWishlist?isArabic=false&userID=" + foreraaParameter.getString("UserID"), "0", activity, "GetFoodMenuTypes", true)
            makeRequest.request(object : VolleyCallback {
                override fun onSuccess(result: Map<String, String>)
                {

                    val gson = Gson()
                    val itemType = object : TypeToken<List<CategoryModels>>() {}.type
                    val itemList = gson.fromJson<List<CategoryModels>>(result.get("res").toString(), itemType)
                    fav_item.layoutManager = LinearLayoutManager(activity)
                    fav_item.adapter = CategoryItem(activity, itemList, "menu")
                    fav_item.adapter.notifyDataSetChanged()
                }
            }, object : ONRetryHandler {
                override fun onRetryHandler(funName: String) {

                }
            })


        }




    }







    override fun onResume() {
        super.onResume()
        activity.back.visibility = View.GONE
        activity.menu.visibility = View.VISIBLE
    }
}
