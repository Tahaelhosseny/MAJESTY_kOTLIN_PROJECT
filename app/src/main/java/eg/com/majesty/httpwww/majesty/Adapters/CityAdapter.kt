package eg.com.majesty.httpwww.majesty.Adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import eg.com.majesty.httpwww.majesty.InterFaces.UpdateCity
import eg.com.majesty.httpwww.majesty.Models.CityModel
import eg.com.majesty.httpwww.majesty.R
import kotlinx.android.synthetic.main.cityitem.view.*



class CityAdapter(var activity :Activity , var listCity: MutableList<CityModel> , var updateCity : UpdateCity) : RecyclerView.Adapter<CityAdapter.MyViewHolder>() , Filterable
{

    var  filterdItemList : MutableList<CityModel>

    init {
        filterdItemList = listCity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        return MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.cityitem , parent, false))

    }

    override fun getItemCount(): Int
    {
        return filterdItemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        holder.city.text = filterdItemList.get(position).CityName


        holder.itemView.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?)
            {
                updateCity.update(filterdItemList.get(position).CityID ,filterdItemList.get(position).CityName )
            }
        })

    }







    override fun getFilter(): Filter
    {
        return object : Filter()
        {
             override fun performFiltering(constraint: CharSequence?): FilterResults
             {

                 val charString = constraint.toString()
                 if (charString.isEmpty())
                 {
                     filterdItemList =listCity
                 } else {

                     val filteredList : MutableList<CityModel> = arrayListOf()
                     for (item in listCity)
                     {
                         if (item.CityName.toLowerCase().contains(charString.toLowerCase()))
                         {
                             filteredList.add(item)
                         }
                     }
                     filterdItemList= filteredList
                 }
                 val filterResults = FilterResults()
                 filterResults.values = filterdItemList
                 return filterResults
             }

             override fun publishResults(constraint: CharSequence?, results: FilterResults?)
             {
                 filterdItemList = results!!.values as MutableList<CityModel>
                 notifyDataSetChanged()
             }
         }
    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var city = itemView.city
    }
}