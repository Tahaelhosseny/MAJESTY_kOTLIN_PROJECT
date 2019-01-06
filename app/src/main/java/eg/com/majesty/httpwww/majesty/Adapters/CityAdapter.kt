package eg.com.majesty.httpwww.majesty.Adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import eg.com.majesty.httpwww.majesty.Models.CityModel
import eg.com.majesty.httpwww.majesty.R
import kotlinx.android.synthetic.main.cat_item.view.*
import kotlinx.android.synthetic.main.cityitem.view.*

class CityAdapter(var activity :Activity , var listCity: MutableList<CityModel>) : RecyclerView.Adapter<CityAdapter.MyViewHolder>()
{



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        return MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.cityitem , parent, false))

    }

    override fun getItemCount(): Int
    {
        return listCity.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        holder.city.text = listCity.get(position).CityName
    }





    public fun setFilter(filterString : String)
    {
        Toast.makeText(activity , filterString , Toast.LENGTH_LONG).show()
    }



    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var city = itemView.city
    }
}