package eg.com.majesty.httpwww.majesty.Adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.Models.NotificationModel
import eg.com.majesty.httpwww.majesty.R
import kotlinx.android.synthetic.main.notification_item.view.*

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
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var elapsedTime = itemView.elapsedTime
        var txtNotificationTitle = itemView.txtNotificationTitle
        var txtNotificationContent = itemView.txtNotificationContent
    }
}