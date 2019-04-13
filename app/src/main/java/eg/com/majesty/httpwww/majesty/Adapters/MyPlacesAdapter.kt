package eg.com.majesty.httpwww.majesty.Adapters

import android.app.Activity
import android.app.FragmentTransaction
import android.content.Intent
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eg.com.majesty.httpwww.majesty.InterFaces.UpdateCity
import eg.com.majesty.httpwww.majesty.Models.UserAddressAsLines
import eg.com.majesty.httpwww.majesty.R

import kotlinx.android.synthetic.main.address_lines_layout.view.*
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import eg.com.majesty.httpwww.majesty.Activity.EditAddress
import eg.com.majesty.httpwww.majesty.Activity.MainActivity
import eg.com.majesty.httpwww.majesty.Fragments.FinalCheckOut
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback


class MyPlacesAdapter(var activity : Activity, var userAddressAsLines : MutableList<UserAddressAsLines>,var isAddressBok :Boolean, var updateCity : UpdateCity) : RecyclerView.Adapter<MyPlacesAdapter.MyViewHolder>()
{


    var ID :String =""

    init {
        ID = ForeraaParameter(activity).getString("UserID")

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.address_lines_layout , parent, false))
    }

    override fun getItemCount(): Int
    {
        return userAddressAsLines.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        if(userAddressAsLines.get(position).IsMainAddress)
        {
            holder.mainnlay.visibility = View.VISIBLE
            holder.notMain.visibility = View.GONE
        }else
        {
            holder.mainnlay.visibility = View.GONE
            holder.notMain.visibility = View.VISIBLE

        }

        holder.line1.text = userAddressAsLines.get(position).AddressLine1
        holder.line2.text = userAddressAsLines.get(position).AddressLine2
        holder.line3.text = userAddressAsLines.get(position).AddressLine3
        holder.line4.text = userAddressAsLines.get(position).AddressLine4
        holder.mline1.text = userAddressAsLines.get(position).AddressLine1
        holder.mline2.text = userAddressAsLines.get(position).AddressLine2
        holder.mline3.text = userAddressAsLines.get(position).AddressLine3
        holder.mline4.text = userAddressAsLines.get(position).AddressLine4


        holder.min1.setOnClickListener(object :View.OnClickListener
        {
            override fun onClick(v: View?)
            {
                openMenu(holder.min1 , position)
            }
        })





        holder.min2.setOnClickListener(object :View.OnClickListener
        {
            override fun onClick(v: View?)
            {
                openMenu(holder.min2 , position)
            }
        })





        holder.itemView.setOnClickListener(object : View.OnClickListener
        {
            override fun onClick(v: View?)
            {
                if(isAddressBok)
                {
                    val finalCheckOut = FinalCheckOut()
                    MainActivity().activeCenterFragments.add(finalCheckOut)
                    val fragmentTransaction = activity.fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.frameContainer, finalCheckOut)
                    fragmentTransaction.addToBackStack("finalCheckOut")
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    fragmentTransaction.commit()
                    finalCheckOut.setDataaa(userAddressAsLines.get(position).UseAddressID.toString())
                }

            }
        } )

    }




    fun openMenu(view :View , position: Int)
    {
        var popup = PopupMenu(activity, view)
        popup.getMenuInflater().inflate(R.menu.address_adapter_menu, popup.getMenu());
        popup.show()

        popup.setOnMenuItemClickListener { item ->


            if(item.title.equals("Main Address"))
            {
                SetAddressAsDefault(position)
            }else if(item.title.equals("Edit Address"))
            {
                activity.startActivity(Intent(activity , EditAddress::class.java).putExtra("userAddressID" , userAddressAsLines.get(position).UseAddressID.toString()))
            }else if(item.title.equals("Delete Address"))
            {
                RemoveUserAddress(position)
            }

            true
        }


    }










    fun SetAddressAsDefault(position: Int)
    {
        var makeRequest = MakeRequest("SetAddressAsDefault?isArabic=false&userId=" + ID +"&userAddressID="+userAddressAsLines.get(position).UseAddressID,"0",activity,"GetCities",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                var str = result.get("res").toString()
                var jsonObject = Gson().fromJson(str, JsonArray::class.java).get(0).asJsonObject

                if(jsonObject.get("Succeed").asBoolean)
                {
                    for (item in userAddressAsLines)
                    {
                        item.IsMainAddress = false
                    }

                    userAddressAsLines.get(position).IsMainAddress = true


                    notifyDataSetChanged()
                }


            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })
    }


    fun RemoveUserAddress(position: Int)
    {
        var makeRequest = MakeRequest("RemoveUserAddress?isArabic=false&userId=" + ID +"&userAddressID="+userAddressAsLines.get(position).UseAddressID,"0",activity,"GetCities",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                var str = result.get("res").toString()
                var jsonObject = Gson().fromJson(str, JsonObject::class.java).get("NotificationsNumbers").asJsonArray.get(0).asJsonObject

                if(jsonObject.get("Succeed").asBoolean)
                {
                    userAddressAsLines.removeAt(position)
                    notifyDataSetChanged()
                }


            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })
    }



    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var notMain = itemView.notMain
        var mainnlay = itemView.mainnlay
        var line1 = itemView.line1
        var line2 = itemView.line2
        var line3 = itemView.line3
        var line4 = itemView.line4
        var mline1 = itemView.mline1
        var mline2 = itemView.mline2
        var mline3 = itemView.mline3
        var mline4 = itemView.mline4
        var min1 = itemView.min1
        var min2 = itemView.min2

    }
}