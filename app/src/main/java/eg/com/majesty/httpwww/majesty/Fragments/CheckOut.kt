package eg.com.majesty.httpwww.majesty.Fragments
import android.app.Fragment
import android.app.FragmentManager
import android.app.FragmentTransaction
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import eg.com.majesty.httpwww.majesty.Activity.MainActivity
import eg.com.majesty.httpwww.majesty.Adapters.CartAdapter
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.InterFaces.RemoveFromCartUpdate
import eg.com.majesty.httpwww.majesty.Models.CartModel
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import kotlinx.android.synthetic.main.activity_check_out.*
import kotlinx.android.synthetic.main.activity_main.*

class CheckOut : Fragment()
{

    var TAG = "CheckOut"
    var ID :String =""
    var adapter = CartAdapter()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.activity_check_out, container, false)
    }





    override fun onResume() {
        activity.header.visibility = View.GONE
        activity.cart.visibility = View.GONE
        activity.bottom.visibility = View.GONE
        doneLay.visibility = View.GONE
        del.visibility = View.VISIBLE
        activity.headerText.setText(R.string.Orders)
        activity.homeIm.setImageResource(R.drawable.icon_home)
        activity.favoriteIm.setImageResource(R.drawable.favorite)
        activity.ordersIm.setImageResource(R.drawable.ordera)
        activity.menuIm.setImageResource(R.drawable.menu)

        afterViews()
        super.onResume()

    }




        fun afterViews()
    {

        catNamee.setTypeface(Utils.Exo2SemiBold(activity))
        dotxt.setTypeface(Utils.Exo2SemiBold(activity))
        totalCount.setTypeface(Utils.Exo2SemiBold(activity))
        checktxt.setTypeface(Utils.Exo2Bold(activity))
        totalPricec.setTypeface(Utils.Exo2Bold(activity))




        var foreraaParameter = ForeraaParameter(activity)

        ID = foreraaParameter.getString("UserID")
        if(ID.equals(""))
            {
                Toast.makeText(activity , "Please Login In and Try Again" , Toast.LENGTH_LONG).show()

            }else
                loadData()





        backNow.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                backto()
            }
        })
        del.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                del()
            }
        })
        doneLay.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                doneLay()
            }
        })

        footer.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                footer()
            }
        })
    }




    fun loadData()
    {
        var makeRequest = MakeRequest("GetCurrentShoppingCartItems?isArabice="+Utils.isArabic(activity)+"&userID=" + ID,"0",activity,"GetCurrentShoppingCartItems",true)
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
        var notificationNumbers = jsonObject.getAsJsonArray("NotificationNumbers").get(0).asJsonObject
        var shoppingCartItemData = jsonObject.getAsJsonArray("shoppingCartItemData")
        val itemType = object : TypeToken<List<CartModel>>() {}.type
        val itemList = gson.fromJson<MutableList<CartModel>>(shoppingCartItemData.toString(), itemType)

        if(itemList.size>0)
        {
            del.visibility = View.VISIBLE
        }else
            del.visibility = View.GONE
        cartItems.layoutManager = LinearLayoutManager(activity )
        adapter = CartAdapter(activity ,itemList , object : RemoveFromCartUpdate
        {
            override fun update(CartTotalAmount: Float , cartItemsCount : Int)
            {
                totalCount.setText(cartItemsCount.toString())
                totalPricec.setText(CartTotalAmount.toString() )


                if(cartItemsCount==0)
                {
                    var backStackCount =activity.fragmentManager.getBackStackEntryCount()
                    for (i in 0 until backStackCount)
                    {
                        var fragment = activity.fragmentManager.getBackStackEntryAt(i).name
                        if (fragment.equals("checkOut"))
                        {
                            var backStackId = activity.fragmentManager.getBackStackEntryAt(i).getId()
                            activity.fragmentManager.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        }
                    }
                }
            }

        })
        cartItems.adapter  = adapter
        cartItems.adapter.notifyDataSetChanged()
        totalCount.setText(notificationNumbers.get("CartItemsCount").toString())
        totalPricec.setText("%.2f".format(adapter.getTotalPrice()).toString())
    }


    fun del()
    {
        adapter.updateList(true)
        doneLay.visibility = View.VISIBLE
        del.visibility = View.GONE
        //footer.visibility = View.GONE
    }


    fun doneLay()
    {
        adapter.updateList(false)
        doneLay.visibility = View.GONE
        del.visibility = View.VISIBLE
        totalPricec.setText("%.2f".format(adapter.getTotalPrice()).toString())
        footer.visibility = View.VISIBLE

    }



    fun footer()
    {



        if(adapter.cartModels!!.size==0)
        {
            Toast.makeText(activity , "Your Cart Is Empty Please Add Items In It" , Toast.LENGTH_LONG).show()
        }
        else
        {
            val myPlaces = MyPlaces()
            MainActivity().activeCenterFragments.add(myPlaces)
            val fragmentTransaction = activity.fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameContainer, myPlaces)
            fragmentTransaction.addToBackStack("myPlaces")
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            fragmentTransaction.commit()
            myPlaces.setData(true)
        }
    }

    fun backto()
    {
        activity.header.visibility = View.VISIBLE
        activity.cart.visibility = View.VISIBLE
        activity.bottom.visibility = View.VISIBLE
        activity.onBackPressed()
    }


}
