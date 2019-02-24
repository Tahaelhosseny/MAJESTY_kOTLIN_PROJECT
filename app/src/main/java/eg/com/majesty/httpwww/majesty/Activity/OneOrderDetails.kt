package eg.com.majesty.httpwww.majesty.Activity
import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import eg.com.majesty.httpwww.majesty.Adapters.FinalCartAdapter
import eg.com.majesty.httpwww.majesty.InterFaces.RemoveFromCartUpdate
import eg.com.majesty.httpwww.majesty.Models.CartModel
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import kotlinx.android.synthetic.main.activity_one_order_details.*
import org.androidannotations.annotations.Click


class OneOrderDetails : Activity()
{

    var orderId = ""

    var rat =0;
    var comment =""


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_order_details)
        orderId = intent.getStringExtra("orderId")
    }


    override fun onResume()
    {
        super.onResume()
        catNamee.setText("Order #" + orderId)
        getData()
    }



    fun getData()
    {
        var makeRequest = MakeRequest("OrderFullDetails?isArabic=false&orderNo=" + orderId,"0",this,"OrderFullDetails",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {


                var str = result.get("res")

                var jsonObject = Gson().fromJson(str, JsonObject::class.java)

                var orderHeader = jsonObject.getAsJsonArray("OrderHeader").get(0).asJsonObject


                rat = orderHeader.get("Rating").asInt

                comment = orderHeader.get("CommentAfterDelivery").asString
                date.text = orderHeader.get("OrderDate").asString
                rating(rat)
                addlin1.setText(orderHeader.get("UserAddressLine1").asString)
                addlin2.setText(orderHeader.get("UserAddressLine2").asString)
                addlin3.setText(orderHeader.get("UserAddressLine3").asString)
                addlin4.setText(orderHeader.get("UserAddressLine4").asString)
                if((orderHeader.get("UserAddressLine1").asString).length>0)
                    addlin1.visibility = View.VISIBLE
                if((orderHeader.get("UserAddressLine2").asString).length>0)
                    addlin2.visibility = View.VISIBLE
                if((orderHeader.get("UserAddressLine3").asString).length>0)
                    addlin3.visibility = View.VISIBLE
                if((orderHeader.get("UserAddressLine4").asString).length>0)
                    addlin4.visibility = View.VISIBLE


                status(orderHeader.get("StatusDesc").asString)

                totalPriceBeforeTax.text = orderHeader.get("TotalBeforeTax").asFloat.toString()
                totalTax.text = orderHeader.get("TotalTax").asFloat.toString()
                deliveryFee.text = orderHeader.get("DeliveryFees").asFloat.toString()
                discount.text = orderHeader.get("Discount").asFloat.toString()
                grandTotal.text = orderHeader.get("Total").asFloat.toString()


                val itemType = object : TypeToken<List<CartModel>>() {}.type
                val itemList = Gson().fromJson<MutableList<CartModel>>(jsonObject.getAsJsonArray("OrderDetails").toString(), itemType)
                cartItems.layoutManager = LinearLayoutManager(this@OneOrderDetails )
                cartItems.setHasFixedSize(true)
                cartItems.setNestedScrollingEnabled(false)




                var adapter = FinalCartAdapter(this@OneOrderDetails ,itemList , object : RemoveFromCartUpdate
                {
                    override fun update(CartTotalAmount: Float , cartItemsCount : Int)
                    {

                    }

                })




                cartItems.adapter  = adapter
                cartItems.adapter.notifyDataSetChanged()




            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })



    }






    fun rating(rate : Int )
    {
        if(rate == 0)
        {
            Glide.with(this).load(R.drawable.star).thumbnail(.2f).into(img1)
            Glide.with(this).load(R.drawable.star).thumbnail(.2f).into(img2)
            Glide.with(this).load(R.drawable.star).thumbnail(.2f).into(img3)
            Glide.with(this).load(R.drawable.star).thumbnail(.2f).into(img4)
            Glide.with(this).load(R.drawable.star).thumbnail(.2f).into(img5)
        }else if(rate == 1)
        {
            Glide.with(this).load(R.drawable.star1).thumbnail(.2f).into(img1)
            Glide.with(this).load(R.drawable.star).thumbnail(.2f).into(img2)
            Glide.with(this).load(R.drawable.star).thumbnail(.2f).into(img3)
            Glide.with(this).load(R.drawable.star).thumbnail(.2f).into(img4)
            Glide.with(this).load(R.drawable.star).thumbnail(.2f).into(img5)
        }else if(rate == 2)
        {
            Glide.with(this).load(R.drawable.star1).thumbnail(.2f).into(img1)
            Glide.with(this).load(R.drawable.star1).thumbnail(.2f).into(img2)
            Glide.with(this).load(R.drawable.star).thumbnail(.2f).into(img3)
            Glide.with(this).load(R.drawable.star).thumbnail(.2f).into(img4)
            Glide.with(this).load(R.drawable.star).thumbnail(.2f).into(img5)
        }else if(rate == 3)
        {
            Glide.with(this).load(R.drawable.star1).thumbnail(.2f).into(img1)
            Glide.with(this).load(R.drawable.star1).thumbnail(.2f).into(img2)
            Glide.with(this).load(R.drawable.star1).thumbnail(.2f).into(img3)
            Glide.with(this).load(R.drawable.star).thumbnail(.2f).into(img4)
            Glide.with(this).load(R.drawable.star).thumbnail(.2f).into(img5)
        }else if(rate == 4)
        {
            Glide.with(this).load(R.drawable.star1).thumbnail(.2f).into(img1)
            Glide.with(this).load(R.drawable.star1).thumbnail(.2f).into(img2)
            Glide.with(this).load(R.drawable.star1).thumbnail(.2f).into(img3)
            Glide.with(this).load(R.drawable.star1).thumbnail(.2f).into(img4)
            Glide.with(this).load(R.drawable.star).thumbnail(.2f).into(img5)
        }else if(rate == 5)
        {
            Glide.with(this).load(R.drawable.star1).thumbnail(.2f).into(img1)
            Glide.with(this).load(R.drawable.star1).thumbnail(.2f).into(img2)
            Glide.with(this).load(R.drawable.star1).thumbnail(.2f).into(img3)
            Glide.with(this).load(R.drawable.star1).thumbnail(.2f).into(img4)
            Glide.with(this).load(R.drawable.star1).thumbnail(.2f).into(img5)
        }
    }



    fun status(status:String)
    {
        sttext.setText(status)

        if(status.equals("Delivered"))
        {
            sttext.text = "Delivered"
            Glide.with(this).load(R.drawable.ic_del).thumbnail(.2f).into(statIcon)

            img1.visibility = View.VISIBLE
            img2.visibility = View.VISIBLE
            img3.visibility = View.VISIBLE
            img4.visibility = View.VISIBLE
            img5.visibility = View.VISIBLE

            if(comment.length<=0)
            {
                comentLay.visibility = View.VISIBLE
                note.isEnabled = true
            }

            else
            {
                note.setText(comment)
                note.isEnabled = false
            }

        }
        else if(status.equals("Rejected"))
        {

            note.isEnabled = false
            img1.visibility = View.INVISIBLE
            img2.visibility = View.INVISIBLE
            img3.visibility = View.INVISIBLE
            img4.visibility = View.INVISIBLE
            img5.visibility = View.INVISIBLE

            sttext.text = "Canceled"
            Glide.with(this).load(R.drawable.ic_can).thumbnail(.2f).into(statIcon)
        } else if(status.equals("Pending")) {

            note.isEnabled = false
            img1.visibility = View.INVISIBLE
            img2.visibility = View.INVISIBLE
            img3.visibility = View.INVISIBLE
            img4.visibility = View.INVISIBLE
            img5.visibility = View.INVISIBLE
            sttext.text = "Pending"
            Glide.with(this).load(R.drawable.ic_pen).thumbnail(.2f).into(statIcon)
        }else
        {
            note.isEnabled = false
            img1.visibility = View.INVISIBLE
            img2.visibility = View.INVISIBLE
            img3.visibility = View.INVISIBLE
            img4.visibility = View.INVISIBLE
            img5.visibility = View.INVISIBLE
            sttext.text = status
            Glide.with(this).load(R.drawable.ic_pen).thumbnail(.2f).into(statIcon)
        }

        }
    


    fun comentLay(view: View)
    {
        var makeRequest = MakeRequest("AddCommentToDeliveredOrder?orderId=" + orderId+"&comment=" + note.text.toString(),"0",this,"AddCommentToDeliveredOrder",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {


                var str = result.get("res")

                var jsonObject = Gson().fromJson(str, JsonArray::class.java).get(0).asJsonObject
                if(jsonObject.get("Succeed").asBoolean)
                {
                    comentLay.visibility = View.GONE
                    note.isEnabled = false
                }
            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })


    }



    fun img1(view: View)
    {
        setRating(1)
    }


    fun img2(view: View)
    {
        setRating(2)
    }


    fun img3(view: View)
    {
        setRating(3)
    }


    fun img4(view: View)
    {
        setRating(4)
    }


    fun img5(view: View)
    {
        setRating(5)
    }



    fun setRating(ratee: Int)
    {
        if (rat == 0)
        {
            var makeRequest = MakeRequest("AddRatingToDeliveredOrder?orderId=" + orderId+"&rate=" + ratee,"0",this,"AddCommentToDeliveredOrder",true)

            makeRequest.request(object  : VolleyCallback
            {
                override fun onSuccess(result: Map<String, String>)
                {


                    var str = result.get("res")

                    var jsonObject = Gson().fromJson(str, JsonArray::class.java).get(0).asJsonObject
                    if(jsonObject.get("Succeed").asBoolean)
                    {
                        rating(ratee)
                        rat= ratee
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



    fun back(view: View)
    {
        onBackPressed()
    }
}
