package eg.com.majesty.httpwww.majesty.Fragments
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import eg.com.majesty.httpwww.majesty.Activity.Login_
import eg.com.majesty.httpwww.majesty.Adapters.GetFoodMenus
import eg.com.majesty.httpwww.majesty.Adapters.PriceAdapter
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.InterFaces.UpdateAreaSpinner
import eg.com.majesty.httpwww.majesty.Models.ItemModel
import eg.com.majesty.httpwww.majesty.Models.PriceModel
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_one_item.*


class OneItem : Fragment()
{


    var TAG = "OneItem"

    var ID :String =""
    var selectedFoodMenuID :Int =-1
    var con = 1
    var rateee = 0
    var  userIDorPassNothing = ""
    var IsItemInUserFavourites = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_one_item, container, false)
    }



    fun setData(ID :String)
    {
        this.ID =ID
    }
 fun setData(ID :String ,selectedFoodMenuID :Int , con : Int)
    {
        this.ID =ID
        this.selectedFoodMenuID = selectedFoodMenuID
        this.con = con
    }

    override fun onResume()
    {
        super.onResume()
        activity.header.visibility= View.GONE
        activity.cart.visibility = View.VISIBLE
        activity.bottom.visibility = View.VISIBLE
        activity.homeIm.setImageResource(R.drawable.icon_home)
        activity.favoriteIm.setImageResource(R.drawable.favorite)
        activity.ordersIm.setImageResource(R.drawable.ordera)
        activity.menuIm.setImageResource(R.drawable.menu)
        var foreraaParameter = ForeraaParameter(activity)

        try
        {
            userIDorPassNothing = foreraaParameter.getString("UserID")

        }catch (e : Exception){}




        plus.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                plus()
            }
        })


        min.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                min()
            }
        })



        fav.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                fav()
            }
        })

        addToCartt.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                addToCartt()
            }
        })


        backkk.setOnClickListener(object :View.OnClickListener
        {
            override fun onClick(v: View?)
            {
                backto()
            }
        })




        img1.setOnClickListener(object :View.OnClickListener
        {
            override fun onClick(v: View?)
            {
                setRating((1).toFloat())
                rateItem()
            }
        })
        img2.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?)
            {
                setRating((2).toFloat())
                rateItem()

            }
        })
        img3.setOnClickListener(object :View.OnClickListener
        {
            override fun onClick(v: View?) {
                setRating((3).toFloat())
                rateItem()
            }
        })
        img4.setOnClickListener(object :View.OnClickListener
        {
            override fun onClick(v: View?) {
                setRating((4).toFloat())
                rateItem()
            }
        })
        img5.setOnClickListener(object :View.OnClickListener
        {
            override fun onClick(v: View?) {
                setRating((5).toFloat())
                rateItem()
            }
        })

        loadData()



    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)


    }




    fun loadData()
    {
        var makeRequest = MakeRequest("GetItemDetails?isArabic=false&foodMenuID=" + ID + "&userIDorPassNothing=" + userIDorPassNothing,"0",activity,"GetFoodMenuTypes",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {


                var str = result.get("res")

                var jsonObject = Gson().fromJson(str, JsonObject::class.java)

                var jsonObject2 = jsonObject.getAsJsonArray("ItemDetails").get(0).asJsonObject
                var notificationNumbers = jsonObject.getAsJsonArray("NotificationNumbers").get(0).asJsonObject
                activity.notiNum.text = notificationNumbers.get("NotificationsCount").toString()
                activity.cartTxt.text = notificationNumbers.get("CartItemsCount").toString()

                setData(jsonObject2)
            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })



    }


    var adapter = PriceAdapter()

    fun setData(jsonObject : JsonObject)
    {
        var model = Gson().fromJson(jsonObject , ItemModel::class.java)
        Glide.with(activity).load(model.FoodMenuImageUrl.replace("http" , "https")).thumbnail(.1f).into(foodImage)
        nameeee.setText(model.FoodMenuName)
        deseee.setText(model.FoodMenuDescription)
        setRating(model.Rating)
        catName.setTypeface(Utils.Exo2SemiBold(activity))
        nameeee.setTypeface(Utils.Exo2SemiBold(activity))
        deseee.setTypeface(Utils.Exo2Medium(activity))
        adtxteee.setTypeface(Utils.Exo2Bold(activity))
        counteee.setTypeface(Utils.Exo2Bold(activity))
        ratetxteee.setTypeface(Utils.Exo2SemiBold(activity))
        catName.setText(model.FoodMenuName)
        IsItemInUserFavourites = model.IsItemInUserFavourites


        if(IsItemInUserFavourites)
            fav2.visibility = View.VISIBLE
        else
            fav2.visibility = View.GONE

        var gson = Gson()
        var itemType = object : TypeToken<List<PriceModel>>() {}.type
        var itemList = gson.fromJson<List<PriceModel>>(model.MenuItemPricesData.toString(), itemType)
        rec.layoutManager = LinearLayoutManager(activity )


        adapter = PriceAdapter(activity ,itemList ,"home" , object : UpdateAreaSpinner
        {
            override fun updateAreaSpinner(cityId: Int)
            {
                con = 1
                counteee.text = "" + con
            }
        })



        if(selectedFoodMenuID!=-1)
        {
            for(i in 0 until itemList.size)
            {
                if(itemList[i].FoodMenuItemID==selectedFoodMenuID)
                {
                    adapter.selectedi = i
                    counteee.text = "" + con
                    adapter.notifyDataSetChanged()
                    break
                }
            }
        }



        rec.adapter  = adapter
        rec.adapter.notifyDataSetChanged()

    }


    fun back(view : View)
    {
        activity.onBackPressed()
    }



    fun plus()
    {
        con++
        counteee.setText(con.toString())
    }
    fun min()
    {

        con--
        if(con == 0)
            con++
        counteee.setText(con.toString())
    }




    fun setRating(rate :Float)
    {
        var rateI = rate.toInt()
        rateee = rateI

        ratetxteee.setText(rateI.toString() + " Of 5")

        if(rateI ==0)
        {
            img1.setImageResource(R.drawable.star)
            img2.setImageResource(R.drawable.star)
            img3.setImageResource(R.drawable.star)
            img4.setImageResource(R.drawable.star)
            img5.setImageResource(R.drawable.star)

        }

        if(rateI ==1)
        {
            img1.setImageResource(R.drawable.star1)
            img2.setImageResource(R.drawable.star)
            img3.setImageResource(R.drawable.star)
            img4.setImageResource(R.drawable.star)
            img5.setImageResource(R.drawable.star)
        }

        if(rateI ==2)
        {
            img1.setImageResource(R.drawable.star1)
            img2.setImageResource(R.drawable.star1)
            img3.setImageResource(R.drawable.star)
            img4.setImageResource(R.drawable.star)
            img5.setImageResource(R.drawable.star)

        }

        if(rateI ==3)
        {
            img1.setImageResource(R.drawable.star1)
            img2.setImageResource(R.drawable.star1)
            img3.setImageResource(R.drawable.star1)
            img4.setImageResource(R.drawable.star)
            img5.setImageResource(R.drawable.star)
        }

        if(rateI ==4)
        {
            img1.setImageResource(R.drawable.star1)
            img2.setImageResource(R.drawable.star1)
            img3.setImageResource(R.drawable.star1)
            img4.setImageResource(R.drawable.star1)
            img5.setImageResource(R.drawable.star)
        }

        if(rateI ==5)
        {
            img1.setImageResource(R.drawable.star1)
            img2.setImageResource(R.drawable.star1)
            img3.setImageResource(R.drawable.star1)
            img4.setImageResource(R.drawable.star1)
            img5.setImageResource(R.drawable.star1)
        }
    }



    fun addToCartt()
    {
        var foreraaParameter = ForeraaParameter(activity)

        var x = counteee.text


        if(foreraaParameter.getString("UserID").length==0)
        {
            startActivity(Intent(activity , Login_::class.java).putExtra("finish" , true))
        }
        else
        {
            var map = HashMap<String , String>()
            var makeRequest = MakeRequest("AddItemToCart?userID=" +foreraaParameter.getString("UserID")+"&foodMenuItemID=" +adapter.getSize() + "&quantity=" + x ,"0", map , activity,"",true)
            makeRequest.request(object  : VolleyCallback
            {
                override fun onSuccess(result: Map<String, String>)
                {

                    var res = result.get("res").toString()


                    var jsonObject = Gson().fromJson(res, JsonArray::class.java).get(0).asJsonObject



                    if (jsonObject.get("Succeed").asBoolean)
                    {


                        activity.notiNum.text = jsonObject.get("NotificationsCount").toString()
                        activity.cartTxt.text = jsonObject.get("CartItemsCount").toString()

                        Toast.makeText(activity , "Item Added To Cart Successfully" , Toast.LENGTH_LONG).show()
                    }else
                    {
                        Toast.makeText(activity , "This Item Failed To Add To Cart Please Tray Again Later" , Toast.LENGTH_LONG).show()
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



    fun fav()
    {

        var foreraaParameter = ForeraaParameter(activity)


        if(foreraaParameter.getString("UserID").length==0)
        {

            startActivity(Intent(activity , Login_::class.java).putExtra("finish" , true))
        }
        else
        {
            if(IsItemInUserFavourites)
            {
                removeItemfromFavourites()
            }
            else
            {
                additemtoFavourites()
            }
        }


    }






    fun additemtoFavourites()
    {

        var map = java.util.HashMap<String, String>()

        var makeRequest = MakeRequest("FavouritesAddItem?isArabic=false&foodMenuID=" + ID + "&userID=" + userIDorPassNothing,"0"  , map,activity,"GetFoodMenuTypes",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                var str = result.get("res")

                var jsonObject = Gson().fromJson(str, JsonArray::class.java).get(0).asJsonObject
                if(jsonObject.get("Succeed").asBoolean)
                {
                    IsItemInUserFavourites =true
                    fav2.visibility = View.VISIBLE
                }
            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })



    }

    fun removeItemfromFavourites()
    {
        var map = java.util.HashMap<String, String>()

        var makeRequest = MakeRequest("FavouritesRemoveItem?isArabic=false&foodMenuID=" + ID + "&userID=" + userIDorPassNothing,"0", map, activity,"GetFoodMenuTypes",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {


                var str = result.get("res")

                var jsonObject = Gson().fromJson(str, JsonArray::class.java).get(0).asJsonObject
                if(jsonObject.get("Succeed").asBoolean)
                {

                    IsItemInUserFavourites = false
                    fav2.visibility = View.GONE

                }

            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })




    }


    fun backto()
    {
        activity.onBackPressed()
    }





    fun rateItem()
    {
        var map = java.util.HashMap<String, String>()

        var makeRequest = MakeRequest("AddItemRating?foodMenuID=" + ID + "&userIDorPassNothing=" + userIDorPassNothing+"&rating=" + rateee,"0", map, activity,"AddItemRating",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {
                var str = result.get("res")
                var jsonObject = Gson().fromJson(str, JsonArray::class.java).get(0).asJsonObject
                if(jsonObject.get("Succeed").asBoolean)
                {


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
