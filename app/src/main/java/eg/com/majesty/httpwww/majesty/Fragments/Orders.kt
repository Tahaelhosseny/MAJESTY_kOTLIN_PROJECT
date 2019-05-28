package eg.com.majesty.httpwww.majesty.Fragments
import android.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import eg.com.majesty.httpwww.majesty.Adapters.PreviousOrderAdapter
import eg.com.majesty.httpwww.majesty.Adapters.UpcommingOrderAdapter
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.Models.CartModel
import eg.com.majesty.httpwww.majesty.Models.PreviousOrdersModel
import eg.com.majesty.httpwww.majesty.Models.UpcommingOrdersModel
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_orders.*
import kotlinx.android.synthetic.main.fragment_orders.view.*




class Orders : Fragment() {


    var clear :Boolean = false
    var TAG = "Orders"

    var ID :String =""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }



    fun setClearr(clearr : Boolean)
    {
        this.clear = clearr
    }


    override fun onResume() {
        activity.headerText.setText(R.string.Orders)
        activity.homeIm.setImageResource(R.drawable.icon_home)
        activity.favoriteIm.setImageResource(R.drawable.favorite)
        activity.ordersIm.setImageResource(R.drawable.ic_orderb)
        activity.menuIm.setImageResource(R.drawable.menu)



        super.onResume()
        activity.back.visibility = View.VISIBLE
        activity.menu.visibility = View.INVISIBLE
        activity.header.visibility = View.VISIBLE
        activity.cart.visibility = View.VISIBLE
        activity.bottom.visibility = View.VISIBLE
        pasttext.setTypeface(Utils.Exo2SemiBold(activity))
        uptext.setTypeface(Utils.Exo2SemiBold(activity))
        GetAllUserOrders()
        view.pastLay.setOnClickListener { view->
            uptext.setTextColor(Color.parseColor("#aaaaaa"))
           pasttext.setTextColor(Color.BLACK)
           pastline.visibility = View.VISIBLE
           upline.visibility = View.GONE
           upRec.visibility = View.GONE
           pastRec.visibility = View.VISIBLE
        }
        view.upLay.setOnClickListener { view->

            uptext.setTextColor(Color.BLACK)
            pasttext.setTextColor(Color.parseColor("#aaaaaa"))
            pastline.visibility = View.GONE
            upline.visibility = View.VISIBLE
            upRec.visibility = View.VISIBLE
            pastRec.visibility = View.GONE

        }



        if(clear)
        {



        }




    }


    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)
    }


    fun GetAllUserOrders()
    {

        try
        {
            var foreraaParameter = ForeraaParameter(activity)

            ID = foreraaParameter.getString("UserID")

        }catch (e : Exception){}


        var makeRequest = MakeRequest("GetAllUserOrders?userID=" + ID,"0",activity,"GetAllUserOrders",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {


                try
                {
                    val gson = Gson()

                    var str = result.get("res")


                    var jsonObject = Gson().fromJson(str, JsonObject::class.java)

                    var notificationNumbers = jsonObject.getAsJsonArray("NotificationNumbers").get(0).asJsonObject

                    var UpcommingOrders = jsonObject.getAsJsonArray("UpcommingOrders").toString()
                    var PreviousOrders = jsonObject.getAsJsonArray("PreviousOrders").toString()


                    activity.notiNum.text = notificationNumbers.get("NotificationsCount").toString()
                    activity.cartTxt.text = notificationNumbers.get("CartItemsCount").toString()

                    val itemTypeUpcommingOrders = object : TypeToken<List<UpcommingOrdersModel>>() {}.type
                    val itemTypePreviousOrders = object : TypeToken<List<PreviousOrdersModel>>() {}.type
                    val itemListUpcommingOrders = gson.fromJson<List<UpcommingOrdersModel>>(UpcommingOrders, itemTypeUpcommingOrders)
                    val itemListPreviousOrders = gson.fromJson<List<PreviousOrdersModel>>(PreviousOrders, itemTypePreviousOrders)

                    val upcommingOrderAdapter = UpcommingOrderAdapter(activity  ,itemListUpcommingOrders )
                    upRec.adapter = upcommingOrderAdapter
                    upRec.layoutManager=LinearLayoutManager(activity)
                    upcommingOrderAdapter.notifyDataSetChanged()
                    val perviousOrderAdapter = PreviousOrderAdapter(activity  ,itemListPreviousOrders )
                    pastRec.adapter = perviousOrderAdapter
                    pastRec.layoutManager=LinearLayoutManager(activity)
                    perviousOrderAdapter.notifyDataSetChanged()


                }catch (e:java.lang.Exception)
                {

                }

            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {
                GetAllUserOrders()
            }
        })






    }



}
