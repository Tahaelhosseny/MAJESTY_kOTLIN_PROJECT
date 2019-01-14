package eg.com.majesty.httpwww.majesty.Activity
import android.app.Activity
import android.content.Context
import android.content.Intent
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
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.InterFaces.RemoveFromCartUpdate
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

class FinalCheckOut : Activity()
{

    var ID :String =""
    var adapter = CartAdapter()

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


    }

    override fun attachBaseContext(newBase: Context)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }





}
