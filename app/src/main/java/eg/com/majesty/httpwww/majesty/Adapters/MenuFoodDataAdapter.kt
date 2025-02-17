package eg.com.majesty.httpwww.majesty.Adapters
import android.app.Activity
import android.app.FragmentTransaction
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import eg.com.majesty.httpwww.majesty.Activity.MainActivity
import eg.com.majesty.httpwww.majesty.Fragments.FreshOffers
import eg.com.majesty.httpwww.majesty.Fragments.LoadCatItems
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.Models.GetFoodMenusModel
import eg.com.majesty.httpwww.majesty.Models.MenuFoodDataModel
import eg.com.majesty.httpwww.majesty.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.get_food_menues2.view.*
import java.lang.Exception

class MenuFoodDataAdapter (val activity : Activity, val categoryItems: List<MenuFoodDataModel>, fragmnt:String): RecyclerView.Adapter<MenuFoodDataAdapter.MyViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.get_food_menues2 , parent, false))
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

        holder.FoodMenuName.setTypeface(Utils.Exo2SemiBold(activity))
        holder.FoodMenuDescription.setTypeface(Utils.setExo2Regular(activity))
        holder.FfoodMenuDescription.setTypeface(Utils.Exo2SemiBold(activity))


        try
        {
            Picasso.with(activity).load(categoryItems.get(position).FoodMenuImageUrl.replace("http" , "https")).into(holder.FoodMenuImageUrl)

        }catch (e :Exception)
        {}


        holder.item.setOnClickListener( object : View.OnClickListener
        {
            override fun onClick(v: View?)
            {
                val freshOffers = FreshOffers()
                activity.headerText.setText("Fresh Offers")
                val fragmentTransaction = activity.fragmentManager.beginTransaction()
                fragmentTransaction.addToBackStack("freshOffers")
                fragmentTransaction.replace(R.id.frameContainer,freshOffers )
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                fragmentTransaction.commit()
            }
        })
    }


    class MyViewHolder (itemView: View): RecyclerView.ViewHolder(itemView)
    {

        val FoodMenuImageUrl = itemView.FoodMenuImageUrl
        val FoodMenuDescription = itemView.FoodMenuDescription
        val FoodMenuName = itemView.FoodMenuName
        val FfoodMenuDescription = itemView.FfoodMenuDescription
        val item = itemView.item

    }
}
