package eg.com.majesty.httpwww.majesty.Activity
import android.app.Activity
import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import eg.com.majesty.httpwww.majesty.Adapters.CartAdapter
import eg.com.majesty.httpwww.majesty.Adapters.PriceAdapter
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.Models.CartModel
import eg.com.majesty.httpwww.majesty.Models.GetFoodMenusModel
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_check_out.*
import kotlinx.android.synthetic.main.activity_one_item.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity
@EActivity(R.layout.activity_check_out)

class CheckOut : Activity()
{

    var ID :String =""
    var adapter = CartAdapter()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        ViewPump.init(ViewPump.builder()
                .addInterceptor(CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/alfares.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build())

    }

    override fun attachBaseContext(newBase: Context)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }



    @AfterViews
    fun afterViews()
    {
        var foreraaParameter = ForeraaParameter(applicationContext)

        ID = foreraaParameter.getString("UserID")
        if(ID.equals(""))
            {
                Toast.makeText(applicationContext , "Please Login In and Try Again" , Toast.LENGTH_LONG).show()
                finish()
            }else
                loadData()
    }




    fun loadData()
    {
        var makeRequest = MakeRequest("GetCurrentShoppingCartItems?isArabice=false&userID=" + ID,"0",this,"GetFoodMenuTypes",true)

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
        val itemType = object : TypeToken<List<CartModel>>() {}.type
        val itemList = gson.fromJson<List<CartModel>>(responce, itemType)

        cartItems.layoutManager = LinearLayoutManager(this )
        adapter = CartAdapter(this ,itemList)
        cartItems.adapter  = adapter
        cartItems.adapter.notifyDataSetChanged()
        totalCount.setText(itemList.size.toString())
        totalPricec.setText("%.2f".format(adapter.getTotalPrice()).toString())
    }


    @Click fun del()
    {
        adapter.updateList(true)
        doneLay.visibility = View.VISIBLE
        del.visibility = View.GONE
        footer.visibility = View.GONE
    }


    @Click fun doneLay()
    {
        adapter.updateList(false)
        doneLay.visibility = View.GONE
        del.visibility = View.VISIBLE
        totalPricec.setText("%.2f".format(adapter.getTotalPrice()).toString())
        footer.visibility = View.VISIBLE

    }



    @Click fun footer()
    {
        var makeRequest = MakeRequest("ConfirmCurrentShoppingCart?isArabice=false&userID=" + ID,"0",this,"GetFoodMenuTypes",true)

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


    @Click fun back()
    {
        finish()
    }


}
