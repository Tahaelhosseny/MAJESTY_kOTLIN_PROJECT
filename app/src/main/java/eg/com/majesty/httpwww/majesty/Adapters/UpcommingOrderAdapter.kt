package eg.com.majesty.httpwww.majesty.Adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.Models.UpcommingOrdersModel
import eg.com.majesty.httpwww.majesty.R
import kotlinx.android.synthetic.main.upcomming_orders_lay.view.*

class UpcommingOrderAdapter (var activity : Activity , var upcommingOrdersModels: List<UpcommingOrdersModel>): RecyclerView.Adapter<UpcommingOrderAdapter.MyViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        return MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.upcomming_orders_lay , parent, false))

    }

    override fun getItemCount(): Int
    {
        return upcommingOrdersModels.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {

        if(upcommingOrdersModels.get(position).StatusDesc.equals("Pending"))
        {

            holder.first.visibility = View.GONE
            holder.second.visibility = View.VISIBLE

            holder.sttext.text = "Pending"
            Glide.with(activity).load(R.drawable.ic_pen).thumbnail(.2f).into(holder.statIcon)
            holder.statIcon



            holder.orderId2.setTypeface(Utils.Exo2SemiBold(activity))
            holder.ItemName2.setTypeface(Utils.Exo2SemiBold(activity))
            holder.date2.setTypeface(Utils.Exo2SemiBold(activity))
            holder.sttext.setTypeface(Utils.Exo2SemiBold(activity))




            Glide.with(activity).load(upcommingOrdersModels.get(position).OrderImageUrl).thumbnail(.2f).into(holder.FoodMenuImageUrl2)
            holder.orderId2.text = upcommingOrdersModels.get(position).OrderNo.toString()
            holder.totalPrice2.text = upcommingOrdersModels.get(position).Total.toString()

            holder.date2.text = Utils.getDate(upcommingOrdersModels.get(position).OrderDateTS.toLong())




        }else
        {
            holder.orderId.setTypeface(Utils.Exo2SemiBold(activity))
            holder.ItemName.setTypeface(Utils.Exo2SemiBold(activity))
            holder.estimatedArrival.setTypeface(Utils.Exo2SemiBold(activity))
            holder.time.setTypeface(Utils.Exo2SemiBold(activity))
            holder.totalPrice.setTypeface(Utils.Exo2SemiBold(activity))
            holder.itemSize.setTypeface(Utils.setExo2Regular(activity))
            holder.tot.setTypeface(Utils.setExo2Regular(activity))


            Glide.with(activity).load(upcommingOrdersModels.get(position).OrderImageUrl).thumbnail(.2f).into(holder.FoodMenuImageUrl)
            holder.orderId.text = upcommingOrdersModels.get(position).OrderNo.toString()
            holder.totalPrice.text = upcommingOrdersModels.get(position).Total.toString()




            if( upcommingOrdersModels.get(position).COnfirmDateTimeTS.equals(""))
                holder.progress.setCurrentProgress(100)
            else
            {
                var current = ( upcommingOrdersModels.get(position).COnfirmDateTimeTS.toLong() + upcommingOrdersModels.get(position).ExpectedDeliveryMinues*60) -(System.currentTimeMillis()/1000)

                if(current.toInt()<0)
                {
                    current = (0).toFloat()
                    holder.time.text = "00:00"
                    holder.progress.setCurrentProgress(current.toInt())

                }
                else
                {

                    holder.time.text
                    current = (current * 100) /  upcommingOrdersModels.get(position).ExpectedDeliveryMinues*60
                    holder.progress.setCurrentProgress(current.toInt())
                }

            }

        }












    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val FoodMenuImageUrl = itemView.FoodMenuImageUrl
        val orderId = itemView.orderId
        val ItemName = itemView.ItemName
        val itemSize = itemView.itemSize
        val estimatedArrival = itemView.estimatedArrival
        val time = itemView.time
        val tot = itemView.tot
        val totalPrice = itemView.totalPrice
        val progress = itemView.progress




        val FoodMenuImageUrl2 = itemView.FoodMenuImageUrl2
        val orderId2 = itemView.orderId2
        val ItemName2 = itemView.ItemName2
        val sttext = itemView.sttext
        val statIcon = itemView.statIcon
        val date2 = itemView.date2
        val totalPrice2 = itemView.totalPrice2
        val first = itemView.first
        val second = itemView.second



    }

}