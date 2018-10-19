/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package eg.com.majesty.httpwww.majesty.Adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import eg.com.majesty.httpwww.majesty.Models.GetFoodMenusModel
import eg.com.majesty.httpwww.majesty.R
import kotlinx.android.synthetic.main.get_food_menues.view.*

class GetFoodMenus (val activity : Activity, val categoryItems: List<GetFoodMenusModel> ,  fragmnt:String): RecyclerView.Adapter<eg.com.majesty.httpwww.majesty.Adapters.GetFoodMenus.MyViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.get_food_menues , parent, false))
    }

    override fun getItemCount(): Int
    {
       return categoryItems.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        holder.FoodMenuDescription.setText(categoryItems.get(position).FoodMenuDescription)
        holder.FfoodMenuDescription.setText(categoryItems.get(position).StandardPrice)
        holder.FoodMenuName.setText(categoryItems.get(position).FoodMenuName)
        Picasso.with(activity).load(categoryItems.get(position).FoodMenuImageUrl).into(holder.FoodMenuImageUrl)
    }


    class MyViewHolder (itemView: View): RecyclerView.ViewHolder(itemView)
    {

        val FoodMenuImageUrl = itemView.FoodMenuImageUrl
        val FoodMenuDescription = itemView.FoodMenuDescription
        val FoodMenuName = itemView.FoodMenuName
        val FfoodMenuDescription = itemView.FfoodMenuDescription
    }
}
