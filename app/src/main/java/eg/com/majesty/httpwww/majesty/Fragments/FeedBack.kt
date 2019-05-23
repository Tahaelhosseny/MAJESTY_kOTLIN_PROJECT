package eg.com.majesty.httpwww.majesty.Fragments

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.Models.NotificationModel
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.feed_back.*
import org.json.JSONArray
import java.util.regex.Pattern


class FeedBack : Fragment()
{

    var TAG = "FeedBack"
    var  isArabic = false
    lateinit var notificationList : MutableList<NotificationModel>
    var ID :String =""
    var data = ""
    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    )


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.feed_back, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)
    }



    override fun onResume() {
        super.onResume()

        var foreraaParameter = ForeraaParameter(activity)

        if(foreraaParameter.getString("UserID").equals(""))
        {
            eemail.visibility = View.VISIBLE
            phoneee.visibility = View.VISIBLE
            nameee.visibility = View.VISIBLE
        }else
        {
            eemail.visibility = View.GONE
            phoneee.visibility = View.GONE
            nameee.visibility = View.GONE
        }

        activity.header.visibility = View.GONE
        activity.cart.visibility = View.GONE
        activity.bottom.visibility = View.GONE
        activity.homeIm.setImageResource(R.drawable.icon_home)
        activity.favoriteIm.setImageResource(R.drawable.favorite)
        activity.ordersIm.setImageResource(R.drawable.ordera)
        activity.menuIm.setImageResource(R.drawable.menu)
        backNow.setOnClickListener { activity.onBackPressed() }
        comentLay.setOnClickListener{


            if(foreraaParameter.getString("UserID").equals(""))
            {

                if(!EMAIL_ADDRESS_PATTERN.matcher(email.text.toString()).matches())
                {
                    email.setError(activity.resources.getString(R.string.mailnotvalid))
                }else if (pOne.text.toString().length!=11)
                {
                     pOne.setError(activity.resources.getString(R.string.phone11))
                }else if(fName.text.toString().length<2)
                {
                    fName.setError(activity.resources.getString(R.string.firstName))
                }else
                {
                    data ="userid=" +"&name="+fName.text.toString()+"&phone="+pOne.text.toString()+"&email="+email.text.toString()+"&message="+note.text.toString()
                    send()
                }

            }else
            {
                data = "userid=" + foreraaParameter.getString("UserID")+"&message="+note.text.toString()+"&name="+"&phone="+"&email="
                send()
            }
        }

    }




    fun send()
    {
        var makeRequest = MakeRequest("SendFeedback?"+data ,"0",activity,"SendFeedback",true)
        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {
                var jsonObject = JSONArray(result.get("res").toString()).getJSONObject(0)
                if(jsonObject.getBoolean("Succeed"))
                {
                    Toast.makeText(activity , activity.resources.getString(R.string.sendok) , Toast.LENGTH_LONG).show()
                    email.setText("")
                    fName.setText("")
                    pOne.setText("")
                    note.setText("")
                }else
                {
                    Toast.makeText(activity , activity.resources.getString(R.string.sendnotok) , Toast.LENGTH_LONG).show()
                }

            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {
            }
        })
    }


    override fun onDestroy()
    {
        super.onDestroy()
    }






}



