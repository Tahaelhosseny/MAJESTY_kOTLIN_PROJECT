package eg.com.majesty.httpwww.majesty.Adapters
import android.app.Activity
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.InterFaces.UpdateAreaSpinner
import eg.com.majesty.httpwww.majesty.Models.PriceModel
import eg.com.majesty.httpwww.majesty.R
import kotlinx.android.synthetic.main.price_model.view.*

class PriceAdapter (val activity: Activity?=null, val priceModels: List<PriceModel>?=null, val fragment : String?=null , var updateAreaSpinner: UpdateAreaSpinner?=null): RecyclerView.Adapter<PriceAdapter.MyViewHolder>()
{

    public var selectedi = 0


    override fun getItemCount(): Int
    {

        return priceModels!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
       holder.price_data.setText(priceModels!!.get(position).FoodMenuItemName +" : " + priceModels.get(position).FoodMenuItemPrice)

        holder.price_data.setTypeface(Utils.Exo2SemiBold(activity!!.applicationContext))
        if (selectedi == position)
        {
            holder.selected.visibility = View.VISIBLE
            holder.price_data.setTextColor(Color.parseColor("#000000"))
        }else
        {
            holder.price_data.setTextColor(Color.parseColor("#73000000"))
            holder.selected.visibility = View.GONE
        }


        holder.item.setOnClickListener {
            selectedi = position
            notifyDataSetChanged()
            updateAreaSpinner!!.updateAreaSpinner(4)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.price_model , parent, false))
    }


    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        var price_data = itemView.price_data
        var item = itemView.item
        var selected = itemView.selected
    }


    public fun getSize(): Int {
        return priceModels!!.get(selectedi).FoodMenuItemID
    }

}
