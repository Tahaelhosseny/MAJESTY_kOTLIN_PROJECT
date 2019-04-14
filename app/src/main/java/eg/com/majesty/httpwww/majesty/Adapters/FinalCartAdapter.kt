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
import eg.com.majesty.httpwww.majesty.Fragments.OneItem
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.InterFaces.RemoveFromCartUpdate
import eg.com.majesty.httpwww.majesty.Models.CartModel
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import kotlinx.android.synthetic.main.final_cart_item.view.*
import kotlinx.android.synthetic.main.final_cart_item.view.*


class FinalCartAdapter (val activity: Activity?=null, val cartModels: MutableList<CartModel>?=null, var removeFromCartUpdate : RemoveFromCartUpdate?=null): RecyclerView.Adapter<FinalCartAdapter.MyViewHolder>()

{
    var delete = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        return MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.final_cart_item , parent, false))
    }

    override fun getItemCount(): Int
    {
       return cartModels!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        holder.ItemName.setText(cartModels!!.get(position).FoodMenuName)
        holder.ItemName.setTypeface(Utils.Exo2SemiBold(activity!!.applicationContext))



        holder.itemSize.setText(cartModels!!.get(position).FoodMenuItemName)
        holder.itemSize.setTypeface(Utils.Exo2SemiBold(activity!!.applicationContext))




        holder.count.setText(cartModels!!.get(position).Quantity.toString() + "X")
        holder.count.setTypeface(Utils.Exo2Bold(activity))
        holder.itemPrice.setTypeface(Utils.Exo2SemiBold(activity))
        holder.totalPrice.setTypeface(Utils.Exo2SemiBold(activity))
        holder.taxPrice.setTypeface(Utils.Exo2SemiBold(activity))
        holder.itemPrice.setText(cartModels!!.get(position).ItemPrice.toString())
        holder.totalPrice.setText(cartModels!!.get(position).TotalAmount.toString())
        holder.taxPrice.setText(cartModels!!.get(position).ItemTax.toString())
        holder.itemView.setOnClickListener(object : View.OnClickListener
        {
            override fun onClick(v: View?)
            {
                val oneItem = OneItem()
                val fragmentTransaction = activity.fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frameContainer, oneItem)
                fragmentTransaction.addToBackStack("oneItem")
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                fragmentTransaction.commit()
                oneItem.setData(""+cartModels.get(position).FoodMenuID , cartModels.get(position).FoodMenuItemID ,cartModels.get(position).Quantity )
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

        val ItemName = itemView.ItemName
        val itemSize = itemView.itemSize
        val itemPrice = itemView.itemPrice
        val totalPrice = itemView.totalPrice
        val count = itemView.count
        val taxPrice = itemView.taxPrice
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


}
