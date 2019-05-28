package eg.com.majesty.httpwww.majesty.Adapters

import android.app.Activity
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.Models.NotificationModel
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import kotlinx.android.synthetic.main.notification_item.view.*
import org.json.JSONArray
import org.json.JSONObject

class NotificationAdapter (var activity: Activity, var notificationList : MutableList<NotificationModel>): RecyclerView.Adapter<NotificationAdapter.MyViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder
    {
        return MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.notification_item , parent, false))
    }

    override fun getItemCount(): Int
    {
        return notificationList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        holder.elapsedTime.text = Utils.getDate(notificationList.get(position).InDateTS.toLong())
        holder.txtNotificationTitle.text = notificationList.get(position).Title
        holder.txtNotificationContent.text = notificationList.get(position).Notification


        if(notificationList.get(position).IsRead)
        {
            holder.linNotificationContent.setBackgroundColor(Color.WHITE)
        }else
        {
            holder.linNotificationContent.setBackgroundColor(Color.parseColor("#E0E0E0"))
        }

        holder.linNotificationContent.setOnClickListener({


            if(!notificationList.get(position).IsRead)
                setUsRead(position)
        })

    }



    fun setUsRead( position: Int)
    {
        var makeRequest = MakeRequest("SetNotificationAsRead?userId=" + ForeraaParameter(activity).getString("UserID") +"&notificationId="+ notificationList.get(position).NotificationID , "0", activity, "SetNotificationAsRead", false)
        makeRequest.request(object : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                var str = result.get("res")
                var jsonObject = JSONArray(str).getJSONObject(0)

                if(jsonObject.getBoolean("Succeed"))
                {
                    notificationList.get(position).IsRead = true
                    notifyDataSetChanged()
                }

            }
        }, object : ONRetryHandler {
            override fun onRetryHandler(funName: String) {
                setUsRead( position)
            }
        })
    }





    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var elapsedTime = itemView.elapsedTime
        var txtNotificationTitle = itemView.txtNotificationTitle
        var txtNotificationContent = itemView.txtNotificationContent
        var linNotificationContent = itemView.linNotificationContent
    }
}