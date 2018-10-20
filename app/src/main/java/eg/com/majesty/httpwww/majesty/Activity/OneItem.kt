package eg.com.majesty.httpwww.majesty.Activity
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.jaeger.library.StatusBarUtil
import com.squareup.picasso.Picasso
import eg.com.majesty.httpwww.majesty.Adapters.CategoryItem
import eg.com.majesty.httpwww.majesty.Adapters.GetFoodMenus
import eg.com.majesty.httpwww.majesty.Adapters.PriceAdapter
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
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_menu.*
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

        ViewPump.init(ViewPump.builder()
                .addInterceptor(CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/alfares.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build())
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




    fun setData(jsonObject : JsonObject)
    {
        val model = Gson().fromJson(jsonObject , ItemModel::class.java)
        Picasso.with(this).load(model.FoodMenuImageUrl).into(foodImage)
        name.setText(model.FoodMenuName)
        des.setText(model.FoodMenuDescription)
        setRating(model.Rating)


        val gson = Gson()
        val itemType = object : TypeToken<List<PriceModel>>() {}.type
        val itemList = gson.fromJson<List<PriceModel>>(model.MenuItemPricesData.toString(), itemType)
        rec.layoutManager = LinearLayoutManager(this )
        rec.adapter = PriceAdapter(this ,itemList ,"home")
        rec.adapter.notifyDataSetChanged()

    }


    @Click fun back(view : View)
    {
        super.onBackPressed()
    }

    @Click fun addToCart()
    {

    }

    @Click fun plus()
    {
        con++
        count.setText(con.toString())
    }

    @Click fun min()
    {

        con--
        if(con == 0)
            con++
        count.setText(con.toString())
    }




    fun setRating(rate :Float)
    {
        var rateI = rate.toInt()

        ratetxt.setText(rateI.toString() + " Of 5")

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
}
