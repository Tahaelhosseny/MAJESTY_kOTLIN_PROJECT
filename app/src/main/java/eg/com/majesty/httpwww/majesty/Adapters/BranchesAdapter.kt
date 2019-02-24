package eg.com.majesty.httpwww.majesty.Adapters

import android.app.Activity
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.util.Util
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.InterFaces.UpdateAreaSpinner
import eg.com.majesty.httpwww.majesty.Models.BranchModel
import eg.com.majesty.httpwww.majesty.R
import kotlinx.android.synthetic.main.branches.view.*
import java.util.*

class BranchesAdapter (var activity: Activity , var branches : MutableList<BranchModel> , var updateAreaSpinner: UpdateAreaSpinner): RecyclerView.Adapter<BranchesAdapter.MyViewHolder>()
{





    var lastSelected = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder
    {
        return MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.branches , parent, false))

    }

    override fun getItemCount(): Int
    {
        return branches.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        holder.govName.text = branches.get(position).CityName
        holder.govName.setTypeface(Utils.Exo2SemiBold(activity))


        if(branches.get(position).Selected)
        {
            holder.govName.setTextColor(Color.parseColor("#000000"))
            holder.govBack.setBackgroundColor(Color.parseColor("#ff3366"))
            lastSelected = position
        }else
        {
            holder.govName.setTextColor(Color.parseColor("#c0000000"))
            holder.govBack.background  = null
        }


        holder.itemView.setOnClickListener(object :View.OnClickListener
        {
            override fun onClick(v: View?)
            {
                holder.govName.setTextColor(Color.parseColor("#000000"))
                holder.govBack.setBackgroundColor(Color.parseColor("#ff3366"))
                branches.get(position).Selected = true
                branches.get(lastSelected).Selected = false
                lastSelected = position
                notifyDataSetChanged()
                updateAreaSpinner.updateAreaSpinner(position)

            }
        })

    }






    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var govName = itemView.govName
        var govBack = itemView.govBack


    }
}