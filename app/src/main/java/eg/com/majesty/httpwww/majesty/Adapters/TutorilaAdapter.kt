package eg.com.majesty.httpwww.majesty.Adapters

import android.app.Activity
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.R

class TutorilaAdapter (val titles : List<String> , val dess : List<String> , val activity: Activity) : PagerAdapter()
{
    override fun isViewFromObject(view: View, `object`: Any): Boolean
    {
        return view === `object` as View
    }


    override fun instantiateItem(parent: ViewGroup, position: Int): Any
    {


        val view = LayoutInflater.from(parent.context).inflate(R.layout.page_layout,parent,false)
        val titletitle = view.findViewById<TextView>(R.id.titletitle)
        val des = view.findViewById<TextView>(R.id.des)
        titletitle.text = titles.get(position)
        titletitle.setTypeface(Utils.Exo2Bold(activity))
        des.text = dess.get(position)
        des.setTypeface(Utils.setExo2Regular(activity))
        parent?.addView(view)

        return view
    }

    override fun getCount(): Int
    {
        return titles.size
    }


    override fun destroyItem(parent: ViewGroup, position: Int, `object`: Any)
    {
        parent.removeView(`object` as View)
    }


}
