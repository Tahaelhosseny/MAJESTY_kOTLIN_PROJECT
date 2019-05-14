package eg.com.majesty.httpwww.majesty.Fragments
import android.app.Fragment
import android.app.FragmentManager
import android.app.FragmentTransaction
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import eg.com.majesty.httpwww.majesty.Adapters.FinalCartAdapter
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.InterFaces.RemoveFromCartUpdate
import eg.com.majesty.httpwww.majesty.Models.CartModel
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import kotlinx.android.synthetic.main.activity_check_out2.*
import kotlinx.android.synthetic.main.activity_main.*
import eg.com.majesty.httpwww.majesty.R.id.first
import java.lang.Exception


class FinalCheckOut : Fragment()
{

    var TAG = "FinalCheckOut"

    var ID :String =""
    var adapter = FinalCartAdapter()
    var UseAddressID : String =""
    var noteStr : String =""



    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.activity_check_out2, container, false)
    }




    fun setDataaa(UseAddressID :String)
    {
        this.UseAddressID =UseAddressID

    }

    override fun onResume()
    {
        super.onResume()
        activity.header.visibility = View.GONE
        activity.cart.visibility = View.GONE
        activity.bottom.visibility = View.GONE
        activity.headerText.setText(R.string.Orders)
        activity.homeIm.setImageResource(R.drawable.icon_home)
        activity.favoriteIm.setImageResource(R.drawable.favorite)
        activity.ordersIm.setImageResource(R.drawable.ordera)
        activity.menuIm.setImageResource(R.drawable.menu)
        ID = ForeraaParameter(activity).getString("UserID")

        del.visibility = View.GONE
        backLay.setOnClickListener({back()})
        footerLayy.setOnClickListener({footer()})
        loadData()
    }





    fun loadData()
    {
        var makeRequest = MakeRequest("OrderFullDetailsByUserID?isArabic=false&userID=" + ID + "&userAddressId="+UseAddressID,"0",activity,"OrderFullDetailsByUserID",true)
        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {
                var str = result.get("res").toString()
                setData(str)
            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {
            }
        })



    }


    fun setData(responce : String)
    {

        try
        {
            val gson = Gson()
            var jsonObject = Gson().fromJson(responce, JsonObject::class.java)
            // var NotificationNumbers = jsonObject.getAsJsonArray("NotificationNumbers").get(0).asJsonObject
            var shoppingCartItemData = jsonObject.getAsJsonArray("OrderDetails")
            var OrderHeader = jsonObject.getAsJsonArray("OrderHeader").get(0).asJsonObject

            totalPriceBeforeTax.text = OrderHeader.get("TotalBeforeTax").asFloat.toString()
            totalTax.text = OrderHeader.get("TotalTax").asFloat.toString()
            deliveryFee.text = OrderHeader.get("DeliveryFees").asFloat.toString()
            discount.text = OrderHeader.get("Discount").asFloat.toString()
            grandTotal.text = OrderHeader.get("Total").asFloat.toString()
            time.text = OrderHeader.get("ExpectedDeliveryMinues").asFloat.toString() + "Minutes"


            val itemType = object : TypeToken<List<CartModel>>() {}.type
            val itemList = gson.fromJson<MutableList<CartModel>>(shoppingCartItemData.toString(), itemType)
            cartItems.layoutManager = LinearLayoutManager(activity )
            cartItems.setHasFixedSize(true)
            cartItems.setNestedScrollingEnabled(false)





            adapter = FinalCartAdapter(activity ,itemList , object : RemoveFromCartUpdate
            {
                override fun update(CartTotalAmount: Float , cartItemsCount : Int)
                {

                }

            })




            cartItems.adapter  = adapter
            cartItems.adapter.notifyDataSetChanged()




        }catch (e:Exception)
        {

        }


    }


    fun footer()
    {


        noteStr = note.text.toString()


        var makeRequest = MakeRequest("ConfirmCurrentShoppingCart?isArabic=false&userID=" + ID +"&notes="+noteStr+"&userAddressId="+UseAddressID,"0",activity,"ConfirmCurrentShoppingCart",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                var str = result.get("res").toString()
                var jsonObject = Gson().fromJson(str, JsonObject::class.java)
                if(jsonObject.get("NewOrderStatus").asJsonObject.get("IsOrderAdded").asBoolean)
                {
                    Toast.makeText(activity ,"Order Added Successfully , Thank You" , Toast.LENGTH_LONG ).show()
                    activity.headerText.setText(R.string.Orders)
                    activity.homeIm.setImageResource(R.drawable.icon_home)
                    activity.favoriteIm.setImageResource(R.drawable.favorite)
                    activity.ordersIm.setImageResource(R.drawable.ic_orderb)
                    activity.menuIm.setImageResource(R.drawable.menu)


                    var backStackCount =activity.fragmentManager.getBackStackEntryCount()-1
                    for (i in 0 until backStackCount)
                    {
                        var fragment = activity.fragmentManager.getBackStackEntryAt(i).name
                        if (fragment.equals("myPlaces") ||fragment.equals("checkOut") || fragment.equals("finalCheckOut")||fragment.equals("oneItem"))
                        {
                            var backStackId = activity.fragmentManager.getBackStackEntryAt(i).getId()
                            activity.fragmentManager.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        }

                    }


                    val orders = Orders()
                    val fragmentTransaction = activity.fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.frameContainer, orders)
                    fragmentTransaction.addToBackStack("orders")
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    fragmentTransaction.commit()
                    orders.setClearr(true)


                }
            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })
    }



    fun back()
    {
        activity.onBackPressed()
    }



}
