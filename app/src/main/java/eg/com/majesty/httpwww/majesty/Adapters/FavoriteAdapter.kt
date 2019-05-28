/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package eg.com.majesty.httpwww.majesty.Adapters

import android.app.Activity
import android.app.FragmentTransaction
import android.app.SearchManager
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import eg.com.majesty.httpwww.majesty.Activity.MainActivity
import eg.com.majesty.httpwww.majesty.Fragments.Home
import eg.com.majesty.httpwww.majesty.Fragments.OneItem
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.Models.GetFoodMenusModel
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import kotlinx.android.synthetic.main.get_food_menues.view.*
import org.json.JSONObject
import java.util.HashMap

class FavoriteAdapter (val activity : Activity, val categoryItems: MutableList<GetFoodMenusModel>, fragmnt:String): RecyclerView.Adapter<eg.com.majesty.httpwww.majesty.Adapters.FavoriteAdapter.MyViewHolder>()
{

    var  userIDorPassNothing = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.get_food_menues , parent, false))
    }

    override fun getItemCount(): Int
    {
       return categoryItems.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        holder.FoodMenuDescription.setText(categoryItems.get(position).FoodMenuDescription)
        holder.FfoodMenuDescription.setText(categoryItems.get(position).StandardPrice)
        holder.FoodMenuName.setText(categoryItems.get(position).FoodMenuName)
        holder.FoodMenuName.setTypeface(Utils.Exo2SemiBold(activity))

        holder.FoodMenuDescription.setTypeface(Utils.Exo2SemiBold(activity))
        holder.FfoodMenuDescription.setTypeface(Utils.Exo2Medium(activity))

        if(categoryItems.get(position).IsItemInUserFavourites)
        {
            holder.fav2.visibility = View.VISIBLE
        }else
        {
            holder.fav2.visibility = View.GONE
        }


        try
        {
            Glide.with(activity).load(categoryItems.get(position).FoodMenuImageUrl.replace("http","https")).thumbnail(.1f).into(holder.FoodMenuImageUrl)
        }catch (e : Exception){}
        holder.item.setOnClickListener(object : View.OnClickListener
        {
            override fun onClick(v: View?)
            {


                val oneItem = OneItem()
                val fragmentTransaction = activity.fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frameContainer, oneItem)
                fragmentTransaction.addToBackStack("oneItem")
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                fragmentTransaction.commit()
                oneItem.setData(categoryItems.get(position).FoodMenuID)

            }
        })


        holder.fav.setOnClickListener (object :View.OnClickListener
        {
            override fun onClick(v: View?)
            {



                var foreraaParameter = ForeraaParameter(activity)

                try
                {
                    userIDorPassNothing = foreraaParameter.getString("UserID")

                }catch (e : Exception){}



                if(userIDorPassNothing.length>0)
                {
                    if(!categoryItems.get(position).IsItemInUserFavourites)
                    {
                        additemtoFavourites( holder , position )
                    }else
                    {
                        removeItemfromFavourites(holder , position )
                    }
                }else
                {
                    Toast.makeText(activity , activity.resources.getString(R.string.notLogged)  , Toast.LENGTH_LONG).show()
                }


            }

        })


    }


    fun additemtoFavourites(holder: MyViewHolder , position: Int)
    {

        var map = HashMap<String , String>()

        var makeRequest = MakeRequest("FavouritesAddItem?isArabic=false&foodMenuID=" + categoryItems.get(position).FoodMenuID + "&userID=" + userIDorPassNothing,"0"  , map,activity,"GetFoodMenuTypes",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                var str = result.get("res")

                var jsonObject = Gson().fromJson(str, JsonArray::class.java).get(0).asJsonObject
                if(jsonObject.get("Succeed").asBoolean)
                {
                    categoryItems.get(position).IsItemInUserFavourites =true
                    holder.fav2.visibility = View.VISIBLE
                    notifyDataSetChanged()
                }
            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {
                additemtoFavourites(holder , position)
            }
        })



    }

    fun removeItemfromFavourites(holder: MyViewHolder , position: Int)
    {
        var map = HashMap<String , String>()

        var makeRequest = MakeRequest("FavouritesRemoveItem?isArabic=false&foodMenuID=" + categoryItems.get(position).FoodMenuID + "&userID=" + userIDorPassNothing,"0", map, activity,"GetFoodMenuTypes",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {


                var str = result.get("res")

                var jsonObject = Gson().fromJson(str, JsonArray::class.java).get(0).asJsonObject
                if(jsonObject.get("Succeed").asBoolean)
                {
                    categoryItems.removeAt(position)
                    notifyDataSetChanged()

                }

            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {
                removeItemfromFavourites(holder, position)
            }
        })




    }



    class MyViewHolder (itemView: View): RecyclerView.ViewHolder(itemView)
    {

        val FoodMenuImageUrl = itemView.FoodMenuImageUrl
        val FoodMenuDescription = itemView.FoodMenuDescription
        val FoodMenuName = itemView.FoodMenuName
        val FfoodMenuDescription = itemView.FfoodMenuDescription
        val item = itemView.item
        val fav = itemView.fav
        val fav2 = itemView.fav2
    }
}
