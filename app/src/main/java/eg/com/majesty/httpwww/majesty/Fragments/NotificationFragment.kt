package eg.com.majesty.httpwww.majesty.Fragments


import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import eg.com.majesty.httpwww.majesty.Adapters.NotificationAdapter
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.Models.NotificationModel
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.notification_layout.*
import org.json.JSONArray
import org.json.JSONObject


class NotificationFragment : Fragment()
{

    var TAG = "NotificationFragment"
    var  isArabic = false
    lateinit var notificationList : MutableList<NotificationModel>
    var ID :String =""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.notification_layout, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)
    }



    override fun onResume() {
        super.onResume()



        activity.header.visibility = View.GONE
        activity.cart.visibility = View.GONE
        activity.bottom.visibility = View.GONE
        activity.homeIm.setImageResource(R.drawable.icon_home)
        activity.favoriteIm.setImageResource(R.drawable.favorite)
        activity.ordersIm.setImageResource(R.drawable.ordera)
        activity.menuIm.setImageResource(R.drawable.menu)
        backNow.setOnClickListener { activity.onBackPressed() }


        getData()






    }


    override fun onDestroy()
    {
        super.onDestroy()


    }



    fun getData()
    {
        if(ForeraaParameter(activity).getInt("language" , 0) ==0)
        {
            isArabic = true
        }
        else
        {
            isArabic = false
        }

        var foreraaParameter = ForeraaParameter(activity)

        try
        {
            ID = foreraaParameter.getString("UserID")
        }catch (e : Exception){}


        var makeRequest = MakeRequest("GetUserNotifications?isArabic="+isArabic+"&userId=" + ID,"0",activity,"GetUserNotifications",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {
                var jsonArray = JSONObject(result.get("res").toString()).getJSONArray("UserNotifications").toString()

                var itemType = object : TypeToken<MutableList<NotificationModel>>() {}.type
                notificationList = Gson().fromJson<MutableList<NotificationModel>>(jsonArray, itemType)
                var notificationAdapter  = NotificationAdapter(activity , notificationList)

                notificationrecycle.layoutManager = LinearLayoutManager(activity)
                notificationrecycle.adapter = notificationAdapter
                notificationAdapter.notifyDataSetChanged()
            }
        } ,object :ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })
    }

}



