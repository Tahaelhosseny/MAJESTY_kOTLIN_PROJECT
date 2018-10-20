package eg.com.majesty.httpwww.majesty.Adapters
import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eg.com.majesty.httpwww.majesty.Models.PriceModel
import eg.com.majesty.httpwww.majesty.R
import kotlinx.android.synthetic.main.price_model.view.*

class PriceAdapter (val activity: Activity, val priceModels: List<PriceModel>, val fragment : String): RecyclerView.Adapter<PriceAdapter.MyViewHolder>()
{

    var selectedi = 0



    override fun getItemCount(): Int
    {

        return priceModels.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
       holder.price_data.setText(priceModels.get(position).FoodMenuItemName +" : " + priceModels.get(position).FoodMenuItemPrice)

        if (selectedi == position)
        {
            holder.selected.visibility = View.VISIBLE
        }else holder.selected.visibility = View.GONE


        holder.item.setOnClickListener {
            selectedi = position
            notifyDataSetChanged()
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
}
