package eg.com.majesty.httpwww.majesty.netHelper

import android.app.Application
import android.content.Context

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleySinglton private constructor(context: Context) : Application()
{
    private var mRequestQueue: RequestQueue? = null

    // If RequestQueue is null the initialize new RequestQueue
    // Return RequestQueue
    val requestQueue: RequestQueue?
        get() {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(mContext.applicationContext)
            }
            return mRequestQueue
        }

    init {
        // Specify the application context
        mContext = context
        // Get the request queue
        mRequestQueue = requestQueue
    }

    fun <T> addToRequestQueue(request: Request<T>) {
        // Add the specified request to the request queue
        requestQueue?.add(request)
    }

    companion object {
        private var mInstance: VolleySinglton? = null
        private lateinit var mContext: Context

        @Synchronized
        fun getInstance(context: Context): VolleySinglton {
            // If Instance is null then initialize new Instance
            if (mInstance == null) {
                mInstance = VolleySinglton(context)
            }
            // Return MySingleton new Instance
            return mInstance as VolleySinglton
        }
    }
}

