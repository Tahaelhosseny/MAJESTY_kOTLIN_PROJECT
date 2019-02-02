package eg.com.majesty.httpwww.majesty.Activity
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import eg.com.majesty.httpwww.majesty.Adapters.CartAdapter
import eg.com.majesty.httpwww.majesty.Adapters.FinalCartAdapter
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.InterFaces.RemoveFromCartUpdate
import eg.com.majesty.httpwww.majesty.Models.CartModel
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import kotlinx.android.synthetic.main.activity_check_out2.*
import org.androidannotations.annotations.WakeLock


class FinalCheckOut : Activity()
{




    var ID :String =""
    var adapter = FinalCartAdapter()
    var UseAddressID : String =""
    var noteStr : String =""





    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        ViewPump.init(ViewPump.builder()
                .addInterceptor(CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/Exo2-Regular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build())


        setContentView(R.layout.activity_check_out2)

        ID = ForeraaParameter(this).getString("UserID")
        UseAddressID = getIntent().getStringExtra("UseAddressID")
        loadData()


    }






    fun loadData()
    {
        var makeRequest = MakeRequest("OrderFullDetailsByUserID?isArabic=false&userID=" + ID,"0",this,"OrderFullDetailsByUserID",true)

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
        cartItems.layoutManager = LinearLayoutManager(this )
        cartItems.setHasFixedSize(true)
        cartItems.setNestedScrollingEnabled(false)




        adapter = FinalCartAdapter(this ,itemList , object : RemoveFromCartUpdate
        {
            override fun update(CartTotalAmount: Float , cartItemsCount : Int)
            {

            }

        })




        cartItems.adapter  = adapter
        cartItems.adapter.notifyDataSetChanged()





    }




    fun footer(view: View)
    {


        noteStr = note.text.toString()


        var makeRequest = MakeRequest("ConfirmCurrentShoppingCart?isArabic=false&userID=" + ID +"&notes="+noteStr+"&userAddressId="+UseAddressID,"0",this,"ConfirmCurrentShoppingCart",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                var str = result.get("res").toString()
                var jsonObject = Gson().fromJson(str, JsonObject::class.java)
                if(jsonObject.get("NewOrderStatus").asJsonObject.get("IsOrderAdded").asBoolean)
                {
                    Toast.makeText(applicationContext ,"Order Added Successfully , Thank You" , Toast.LENGTH_LONG ).show()
                    startActivity(Intent(this@FinalCheckOut , MainActivity_::class.java).putExtra("isHistory" , true).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                }
            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })
    }

}
