package eg.com.majesty.httpwww.majesty.FCM

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import eg.com.majesty.httpwww.majesty.Activity.MainActivity
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.R


class MyFirebaseMessagingService : FirebaseMessagingService()
{


    val TAG = "FirebaseMessagingService"
    @SuppressLint("LongLogTag")
    override fun onMessageReceived(remoteMessage: RemoteMessage)
    {
        Log.d(TAG, "Dikirim dari: ${remoteMessage.from}")
        if (remoteMessage.data != null)
        {

            val params = remoteMessage.data
            val jsonObject = JSONObject(params)

            if(Utils.isArabicBoolean(this))
            {
                showNotification(jsonObject.getString("titleAr"), jsonObject.getString("messageAr"))

            }else
            {
                showNotification(jsonObject.getString("titleEn"), jsonObject.getString("messageEn"))

            }
        }
    }








    private fun showNotification(message: String , body : String) {

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 112, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(applicationContext, "default")
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(message)
                .setContentText(body)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
        Log.d("Hay11", "DCM11")


        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        Log.d("Hay12", "DCM12")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("com.myApp")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    "com.myApp",
                    "My App",
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager?.createNotificationChannel(channel)
        }
        notificationManager.notify(112, builder.build())
    }


}