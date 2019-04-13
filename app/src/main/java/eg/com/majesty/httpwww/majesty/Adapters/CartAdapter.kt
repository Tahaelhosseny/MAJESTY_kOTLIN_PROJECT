package eg.com.majesty.httpwww.majesty.Adapters

import android.app.Activity
import android.app.FragmentTransaction
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import eg.com.majesty.httpwww.majesty.R
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import eg.com.majesty.httpwww.majesty.Activity.MainActivity
import eg.com.majesty.httpwww.majesty.Activity.MainActivity_
import eg.com.majesty.httpwww.majesty.Fragments.OneItem
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.InterFaces.RemoveFromCartUpdate
import eg.com.majesty.httpwww.majesty.Models.CartModel
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import kotlinx.android.synthetic.main.cart_item.view.*


class CartAdapter (val activity: Activity?=null, val cartModels: MutableList<CartModel>?=null , var removeFromCartUpdate : RemoveFromCartUpdate?=null): RecyclerView.Adapter<CartAdapter.MyViewHolder>()

{
    var delete = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        return MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.cart_item , parent, false))
    }

    override fun getItemCount(): Int
    {
       return cartModels!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        Glide.with(activity!!.applicationContext).load(cartModels!!.get(position).FoodMenuImageUrl.replace("http" , "https")).thumbnail(.2f).into(holder.FoodMenuImageUrl)
        holder.ItemName.setText(cartModels!!.get(position).FoodMenuName)
        holder.ItemName.setTypeface(Utils.Exo2SemiBold(activity!!.applicationContext))



        holder.itemSize.setText(cartModels!!.get(position).FoodMenuItemName)
        holder.itemSize.setTypeface(Utils.Exo2SemiBold(activity!!.applicationContext))




        holder.count.setText(cartModels!!.get(position).Quantity.toString())
        holder.count.setTypeface(Utils.Exo2Bold(activity))


        holder.itemPrice.setTypeface(Utils.Exo2SemiBold(activity))
        holder.totalPrice.setTypeface(Utils.Exo2SemiBold(activity))






        holder.itemPrice.setText(cartModels!!.get(position).ItemPrice.toString())
        holder.totalPrice.setText(cartModels!!.get(position).TotalAmount.toString())

        holder.FoodMenuImageAvatar.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?)
            {
                loadData(cartModels.get(position).FoodMenuItemID , position)
            }
        })


        if(delete)
        {
            holder.FoodMenuImageDel.visibility = View.VISIBLE
            holder.FoodMenuImageAvatar.visibility = View.VISIBLE
        }
        else
        {
            holder.FoodMenuImageDel.visibility = View.GONE
            holder.FoodMenuImageAvatar.visibility = View.GONE
        }

        holder.itemView.setOnClickListener(object : View.OnClickListener
        {
            override fun onClick(v: View?)
            {
                val oneItem = OneItem()
                MainActivity().activeCenterFragments.add(oneItem)
                val fragmentTransaction = activity.fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frameContainer, oneItem)
                fragmentTransaction.addToBackStack("oneItem")
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                fragmentTransaction.commit()
                oneItem.setData(""+cartModels.get(position).FoodMenuID)
            }
        })


    }


    public fun updateList(delete : Boolean)
    {
        this.delete = delete
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val FoodMenuImageUrl = itemView.FoodMenuImageUrl
        val FoodMenuImageAvatar = itemView.FoodMenuImageAvatar
        val FoodMenuImageDel = itemView.FoodMenuImageDel
        val ItemName = itemView.ItemName
        val itemSize = itemView.itemSize
        val min = itemView.min
        val plus = itemView.plus
        val itemPrice = itemView.itemPrice
        val totalPrice = itemView.totalPrice
        val count = itemView.count
    }

    public fun getTotalPrice () : Double
    {
        var totalPrice =.0 as Double
        if (cartModels != null)
        {
            for(item in cartModels)
            {
                totalPrice +=  item.TotalAmount
            }
        }



        return totalPrice
    }




    fun loadData(foodMenuID : Int , position: Int)
    {

        var foreraaParameter = ForeraaParameter(activity!!.applicationContext)

        var ID = foreraaParameter.getString("UserID")

        var makeRequest = MakeRequest("RemoveItemFromCart?isArabice=false&userID=" + ID + "&foodMenuItemID=" + foodMenuID ,"0",activity,"GetFoodMenuTypes",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                var jsonObject = Gson().fromJson(result.get("res"), JsonArray::class.java).get(0).asJsonObject

                if(jsonObject.get("Succeed").asBoolean)
                {
                    Toast.makeText(activity!!.applicationContext , "Item Removed Successfully from your cart" ,Toast.LENGTH_LONG).show()
                    cartModels!!.removeAt(position)
                    notifyDataSetChanged()
                    removeFromCartUpdate!!.update(jsonObject.get("CartTotalAmount").asFloat , cartModels.size)
                }
            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })



    }

}
