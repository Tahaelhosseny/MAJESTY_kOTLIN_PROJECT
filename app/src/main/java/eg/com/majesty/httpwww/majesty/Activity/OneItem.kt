package eg.com.majesty.httpwww.majesty.Activity
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.jaeger.library.StatusBarUtil
import com.squareup.picasso.Picasso
import eg.com.majesty.httpwww.majesty.Adapters.CategoryItem
import eg.com.majesty.httpwww.majesty.Adapters.GetFoodMenus
import eg.com.majesty.httpwww.majesty.Adapters.PriceAdapter
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.Models.CategoryModels
import eg.com.majesty.httpwww.majesty.Models.GetFoodMenusModel
import eg.com.majesty.httpwww.majesty.Models.ItemModel
import eg.com.majesty.httpwww.majesty.Models.PriceModel
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_one_item.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity


@EActivity(R.layout.activity_one_item)
class OneItem : Activity()
{
    var ID :String =""
    var con = 0

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setTransparent(this)


        setContentView(R.layout.activity_one_item)

    }


    override fun attachBaseContext(newBase: Context)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }



    @AfterViews
    fun afterViews()
    {

        ID = intent.getStringExtra("ID")
        loadData()



    }




    fun loadData()
    {
        var makeRequest = MakeRequest("GetFoodMenuItemsAndPrices?isArabic=false&foodMenuID=" + ID,"0",this,"GetFoodMenuTypes",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                Log.e("responce" , result.get("res"))

                var str = result.get("res")
                var jsonArray = Gson().fromJson(str, JsonArray::class.java)
                var jsonObject = jsonArray.get(0).asJsonObject
                setData(jsonObject)
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
        Picasso.with(this).load(model.FoodMenuImageUrl.replace("http" , "https")).into(foodImage)
        nameeee.setText(model.FoodMenuName)
        deseee.setText(model.FoodMenuDescription)
        setRating(model.Rating)
        catName.setTypeface(Utils.Exo2SemiBold(this))
        nameeee.setTypeface(Utils.Exo2SemiBold(this))
        deseee.setTypeface(Utils.Exo2Medium(this))
        adtxteee.setTypeface(Utils.Exo2Bold(this))
        counteee.setTypeface(Utils.Exo2Bold(this))
        ratetxteee.setTypeface(Utils.Exo2SemiBold(this))

        val gson = Gson()
        val itemType = object : TypeToken<List<PriceModel>>() {}.type
        val itemList = gson.fromJson<List<PriceModel>>(model.MenuItemPricesData.toString(), itemType)
        rec.layoutManager = LinearLayoutManager(this )
        adapter = PriceAdapter(this ,itemList ,"home")
        rec.adapter  = adapter
        rec.adapter.notifyDataSetChanged()

    }


    @Click fun back(view : View)
    {
        super.onBackPressed()
    }



    @Click fun plus()
    {
        con++
        counteee.setText(con.toString())
    }

    @Click fun min()
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




    @Click fun addToCartt()
    {
        var foreraaParameter = ForeraaParameter(applicationContext)

        var x = counteee.text


        if(foreraaParameter.getString("UserID").length==0)
        {
            startActivity(Intent(this , Login_::class.java).putExtra("finish" , true))
        }
        else
        {
            var map = HashMap<String , String>()
            var makeRequest = MakeRequest("AddFoodMenuItemToCart?userID=" +foreraaParameter.getString("UserID")+"&foodMenuItemID=" +adapter.getSize() + "&quantity=" + x ,"0", map , this,"",true)
            makeRequest.request(object  : VolleyCallback
            {
                override fun onSuccess(result: Map<String, String>)
                {

                    val res = result.get("res")
                    if (res.equals("true"))
                    {
                        Toast.makeText(this@OneItem , "Item Added To Cart Successfully" , Toast.LENGTH_LONG).show()
                    }else
                    {
                        Toast.makeText(this@OneItem , "This Item Failed To Add To Cart Please Tray Again Later" , Toast.LENGTH_LONG).show()
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




    @Click fun fav()
    {

        var foreraaParameter = ForeraaParameter(applicationContext)


        if(foreraaParameter.getString("UserID").length==0)
        {

            startActivity(Intent(this , Login_::class.java).putExtra("finish" , true))
        }
        else
        {
            var map = HashMap<String , String>()

            var makeRequest = MakeRequest("AddFoodMenuToWishlist?userID=" +foreraaParameter.getString("UserID")+"&foodMenuID=" +ID,"0", map , this,"",true)

            makeRequest.request(object  : VolleyCallback
            {
                override fun onSuccess(result: Map<String, String>)
                {

                    val res = result.get("res")
                    if (res.equals("true"))
                    {
                        Toast.makeText(this@OneItem , "Item Added To WishList Successfully" , Toast.LENGTH_LONG).show()
                    }else
                    {
                        Toast.makeText(this@OneItem , "This Item Is Already in your WishList" , Toast.LENGTH_LONG).show()
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
}
