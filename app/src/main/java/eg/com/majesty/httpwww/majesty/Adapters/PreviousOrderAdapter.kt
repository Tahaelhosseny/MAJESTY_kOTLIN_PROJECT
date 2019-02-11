package eg.com.majesty.httpwww.majesty.Adapters

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import eg.com.majesty.httpwww.majesty.Activity.OneOrderDetails
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.Models.PreviousOrdersModel
import eg.com.majesty.httpwww.majesty.R
import kotlinx.android.synthetic.main.previous_orders_lay.view.*

class PreviousOrderAdapter (var activity : Activity, var upcommingOrdersModels: List<PreviousOrdersModel>): RecyclerView.Adapter<PreviousOrderAdapter.MyViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        return MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.previous_orders_lay , parent, false))

    }

    override fun getItemCount(): Int
    {
        return upcommingOrdersModels.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {

        holder.orderId.setTypeface(Utils.Exo2SemiBold(activity))
        holder.ItemName.setTypeface(Utils.Exo2SemiBold(activity))
        holder.date.setTypeface(Utils.Exo2SemiBold(activity))
        holder.sttext.setTypeface(Utils.Exo2SemiBold(activity))


        rating(upcommingOrdersModels.get(position).Rating , holder)


         Glide.with(activity).load(upcommingOrdersModels.get(position).OrderImageUrl).thumbnail(.2f).into(holder.FoodMenuImageUrl)
         holder.orderId.text = upcommingOrdersModels.get(position).OrderNo.toString()
         holder.totalPrice.text = upcommingOrdersModels.get(position).Total.toString()

        holder.date.text = Utils.getDate(upcommingOrdersModels.get(position).OrderDateTS.toLong())

        if(upcommingOrdersModels.get(position).StatusDesc.equals("Delivered"))
        {
            holder.sttext.text = "Delivered"
            Glide.with(activity).load(R.drawable.ic_del).thumbnail(.2f).into(holder.statIcon)
            holder.statIcon
        }
        else if(upcommingOrdersModels.get(position).StatusDesc.equals("Rejected"))
        {
            holder.sttext.text = "Canceled"
            Glide.with(activity).load(R.drawable.ic_can).thumbnail(.2f).into(holder.statIcon)
        }



        holder.itemView.setOnClickListener( object :View.OnClickListener
        {
            override fun onClick(v: View?)
            {
                activity.startActivity(Intent(activity , OneOrderDetails::class.java).putExtra("orderId" , upcommingOrdersModels.get(position).OrderNo.toString()))
            }
        })

    }



    fun rating(rate : Int , holder: MyViewHolder)
    {
        if(rate == 0)
        {
            Glide.with(activity).load(R.drawable.star).thumbnail(.2f).into(holder.img1)
            Glide.with(activity).load(R.drawable.star).thumbnail(.2f).into(holder.img2)
            Glide.with(activity).load(R.drawable.star).thumbnail(.2f).into(holder.img3)
            Glide.with(activity).load(R.drawable.star).thumbnail(.2f).into(holder.img4)
            Glide.with(activity).load(R.drawable.star).thumbnail(.2f).into(holder.img5)
        }else if(rate == 1)
        {
            Glide.with(activity).load(R.drawable.star1).thumbnail(.2f).into(holder.img1)
            Glide.with(activity).load(R.drawable.star).thumbnail(.2f).into(holder.img2)
            Glide.with(activity).load(R.drawable.star).thumbnail(.2f).into(holder.img3)
            Glide.with(activity).load(R.drawable.star).thumbnail(.2f).into(holder.img4)
            Glide.with(activity).load(R.drawable.star).thumbnail(.2f).into(holder.img5)
        }else if(rate == 2)
        {
            Glide.with(activity).load(R.drawable.star1).thumbnail(.2f).into(holder.img1)
            Glide.with(activity).load(R.drawable.star1).thumbnail(.2f).into(holder.img2)
            Glide.with(activity).load(R.drawable.star).thumbnail(.2f).into(holder.img3)
            Glide.with(activity).load(R.drawable.star).thumbnail(.2f).into(holder.img4)
            Glide.with(activity).load(R.drawable.star).thumbnail(.2f).into(holder.img5)
        }else if(rate == 3)
        {
            Glide.with(activity).load(R.drawable.star1).thumbnail(.2f).into(holder.img1)
            Glide.with(activity).load(R.drawable.star1).thumbnail(.2f).into(holder.img2)
            Glide.with(activity).load(R.drawable.star1).thumbnail(.2f).into(holder.img3)
            Glide.with(activity).load(R.drawable.star).thumbnail(.2f).into(holder.img4)
            Glide.with(activity).load(R.drawable.star).thumbnail(.2f).into(holder.img5)
        }else if(rate == 4)
        {
            Glide.with(activity).load(R.drawable.star1).thumbnail(.2f).into(holder.img1)
            Glide.with(activity).load(R.drawable.star1).thumbnail(.2f).into(holder.img2)
            Glide.with(activity).load(R.drawable.star1).thumbnail(.2f).into(holder.img3)
            Glide.with(activity).load(R.drawable.star1).thumbnail(.2f).into(holder.img4)
            Glide.with(activity).load(R.drawable.star).thumbnail(.2f).into(holder.img5)
        }else if(rate == 5)
        {
            Glide.with(activity).load(R.drawable.star1).thumbnail(.2f).into(holder.img1)
            Glide.with(activity).load(R.drawable.star1).thumbnail(.2f).into(holder.img2)
            Glide.with(activity).load(R.drawable.star1).thumbnail(.2f).into(holder.img3)
            Glide.with(activity).load(R.drawable.star1).thumbnail(.2f).into(holder.img4)
            Glide.with(activity).load(R.drawable.star1).thumbnail(.2f).into(holder.img5)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val FoodMenuImageUrl = itemView.FoodMenuImageUrl
        val orderId = itemView.orderId
        val ItemName = itemView.ItemName
        val sttext = itemView.sttext
        val statIcon = itemView.statIcon
        val date = itemView.date
        val totalPrice = itemView.totalPrice
        val img1 = itemView.img1
        val img2 = itemView.img2
        val img3 = itemView.img3
        val img4 = itemView.img4
        val img5 = itemView.img5



    }

}