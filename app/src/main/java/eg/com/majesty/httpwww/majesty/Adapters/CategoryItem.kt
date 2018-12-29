package eg.com.majesty.httpwww.majesty.Adapters

import android.app.Activity
import android.app.FragmentTransaction
import android.opengl.Visibility
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import eg.com.majesty.httpwww.majesty.R
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import eg.com.majesty.httpwww.majesty.Fragments.Home
import eg.com.majesty.httpwww.majesty.Fragments.LoadCatItems
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.Models.CategoryModels
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.cat_item.view.*
import java.lang.reflect.Type

class CategoryItem (val activity: Activity,val categoryItems: List<CategoryModels> , val fragment : String): RecyclerView.Adapter<CategoryItem.MyViewHolder>()

{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        if(fragment.equals("menu"))
            return MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.cat_item , parent, false))

        else
            return MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.cat_item2 , parent, false))
    }

    override fun getItemCount(): Int
    {
       return categoryItems.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        if(position%4==0)
        {
            holder.back.setImageResource(R.drawable.cat1)
        }else if(position%4==1)
        {
            holder.back.setImageResource(R.drawable.cat2)
        }else if(position%4==2)
        {
            holder.back.setImageResource(R.drawable.cat3)
        }else if(position%4==3)
        {
            holder.back.setImageResource(R.drawable.cat4)
        }


        Glide.with(activity).load(categoryItems.get(position).FoodMenuTypeImageUrl.replace("http","https")).thumbnail(.1f).into(holder.img)
        holder.name.setText(categoryItems.get(position).FoodMenuTypeName)
        holder.name.setTypeface(Utils.Exo2Bold(activity))
        Glide.with(activity).load(categoryItems.get(position).FoodMenuTypeImageUrl2.replace("http" , "https")).thumbnail(.1f).into(holder.imgh)
        holder.item.setOnClickListener( object : View.OnClickListener
        {
            override fun onClick(v: View?)
            {

                val loadCatItems = LoadCatItems()
                loadCatItems.setData(categoryItems.get(position).FoodMenuTypeID ,categoryItems.get(position).FoodMenuTypeName)
                val fragmentTransaction = activity.fragmentManager.beginTransaction()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.replace(R.id.frameContainer, loadCatItems)
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                fragmentTransaction.commit()

            }
        })

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val img = itemView.img
        val imgh = itemView.imgh
        val back = itemView.back
        val name = itemView.name
        val item = itemView.item
    }
}
