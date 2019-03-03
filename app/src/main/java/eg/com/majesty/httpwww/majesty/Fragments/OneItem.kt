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
import eg.com.majesty.httpwww.majesty.Adapters.PriceAdapter
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
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
    var ID :String =""
    var con = 0

    var  userIDorPassNothing = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_one_item, container, false)
    }



    fun setData(ID :String)
    {
        this.ID =ID
    }

    override fun onResume()
    {
        super.onResume()
        activity.header.visibility= View.GONE

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
               // var notificationNumbers = jsonObject.getAsJsonObject("NotificationNumbers").get(0).asJsonObject

                var jsonObject2 = jsonObject.getAsJsonArray("ItemDetails").get(0).asJsonObject

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
        val model = Gson().fromJson(jsonObject , ItemModel::class.java)
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
        val gson = Gson()
        val itemType = object : TypeToken<List<PriceModel>>() {}.type
        val itemList = gson.fromJson<List<PriceModel>>(model.MenuItemPricesData.toString(), itemType)
        rec.layoutManager = LinearLayoutManager(activity )
        adapter = PriceAdapter(activity ,itemList ,"home")
        rec.adapter  = adapter
        rec.adapter.notifyDataSetChanged()

    }


    fun back(view : View)
    {
        //super.onBackPressed()
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

                    val res = result.get("res").toString()


                    var jsonObject = Gson().fromJson(res, JsonArray::class.java).get(0).asJsonObject



                    if (jsonObject.get("Succeed").asBoolean)
                    {
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
            var map = HashMap<String , String>()

            var makeRequest = MakeRequest("AddFoodMenuToWishlist?userID=" +foreraaParameter.getString("UserID")+"&foodMenuID=" +ID,"0", map , activity,"",true)

            makeRequest.request(object  : VolleyCallback
            {
                override fun onSuccess(result: Map<String, String>)
                {

                    val res = result.get("res")
                    if (res.equals("true"))
                    {
                        Toast.makeText(activity , "Item Added To WishList Successfully" , Toast.LENGTH_LONG).show()
                    }else
                    {
                        Toast.makeText(activity , "This Item Is Already in your WishList" , Toast.LENGTH_LONG).show()
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


    fun backto()
    {
        activity.header.visibility = View.VISIBLE
    }


    override fun onDestroy()
    {
        super.onDestroy()
        activity.header.visibility = View.VISIBLE

    }
}
