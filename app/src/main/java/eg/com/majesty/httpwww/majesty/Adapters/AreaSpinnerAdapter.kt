package eg.com.majesty.httpwww.majesty.Adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import eg.com.majesty.httpwww.majesty.Models.AreaSpinnerModel
import android.view.View
import android.view.ViewGroup
import eg.com.majesty.httpwww.majesty.R
import android.widget.TextView

class AreaSpinnerAdapter(context: Context, resource: Int, textViewResourceId: Int, objects:  ArrayList<AreaSpinnerModel>) : ArrayAdapter<AreaSpinnerModel>(context, resource, textViewResourceId, objects)
{


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        return rowview(convertView, position)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return rowview(convertView, position)
    }

    private fun rowview(convertView: View?, position: Int): View {

        val rowItem = getItem(position)

        val holder: viewHolder
        var rowview = convertView
        if (rowview == null)
        {

            holder = viewHolder()
            var flater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            rowview = flater.inflate(R.layout.area_name, null, false)

            holder.txtTitle = rowview!!.findViewById(R.id.areaName) as TextView
            rowview.tag = holder
        } else {
            holder = rowview.tag as viewHolder
        }

        holder.txtTitle.setText(rowItem.AreaName)

        return rowview
    }





    inner class viewHolder
    {
        lateinit var txtTitle :TextView
    }


}