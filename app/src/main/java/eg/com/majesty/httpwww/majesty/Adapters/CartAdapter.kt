package eg.com.majesty.httpwww.majesty.Adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import eg.com.majesty.httpwww.majesty.R
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.Models.CartModel
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import kotlinx.android.synthetic.main.cart_item.view.*


class CartAdapter (val activity: Activity?=null, val cartModels: List<CartModel>?=null): RecyclerView.Adapter<CartAdapter.MyViewHolder>()

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
        Picasso.with(activity).load(cartModels!!.get(position).FoodMenuImageUrl).into(holder.FoodMenuImageUrl)
        holder.ItemName.setText(cartModels!!.get(position).FoodMenuName)
        holder.itemSize.setText(cartModels!!.get(position).FoodMenuItemName)
        holder.count.setText(cartModels!!.get(position).Quantity.toString())
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

        var makeRequest = MakeRequest("RemoveFoodMenuFromWishlist?isArabice=false&userID=" + ID + "&foodMenuID=" + foodMenuID ,"0",activity!!.applicationContext,"GetFoodMenuTypes",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {
            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })



    }

}
