package eg.com.majesty.httpwww.majesty.Activity
import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import eg.com.majesty.httpwww.majesty.Adapters.AreaSpinnerAdapter
import eg.com.majesty.httpwww.majesty.Adapters.BranchesAdapter
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.InterFaces.UpdateAreaSpinner
import eg.com.majesty.httpwww.majesty.Models.AreaSpinnerModel
import eg.com.majesty.httpwww.majesty.Models.BranchModel
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.SupportMapFragment
import eg.com.majesty.httpwww.majesty.Models.BranchDataList
import kotlinx.android.synthetic.main.activity_branches.*
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener



class Branches :  FragmentActivity(), OnMapReadyCallback
{



    private lateinit var mMap: GoogleMap
    lateinit   var subTownModelSpiners :ArrayList<AreaSpinnerModel>



    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_branches)

        val mapFragment =supportFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onResume()
    {
        super.onResume()
        catNamee.setTypeface(Utils.Exo2SemiBold(this))




    }



    fun GetAllBranches()
    {
        var makeRequest = MakeRequest("GetAllBranches?isArabic=false" ,"0",this,"getUserAddressesAsLines",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                var str = result.get("res").toString()
                var itemType = object : TypeToken<MutableList<BranchModel>>() {}.type
                var branches = Gson().fromJson<MutableList<BranchModel>>(str, itemType)
                setData(branches)


            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {
            }
        })

    }




    fun setData(branches: MutableList<BranchModel>)
    {

        for (  branch in branches )
        {
            branch.Selected = false
        }


        branches.get(0).Selected = true

        var branchesAdapter =  BranchesAdapter(this@Branches , branches , object : UpdateAreaSpinner{
            override fun updateAreaSpinner(cityId: Int)
            {
                subTownModelSpiners = Gson().fromJson(branches.get(cityId).BranchesInCityList.toString(), object : TypeToken<ArrayList<AreaSpinnerModel>>() {}.type) as ArrayList<AreaSpinnerModel>

                var areaSpinnerAdapter = AreaSpinnerAdapter(this@Branches, R.layout.area_name, R.id.areaName, subTownModelSpiners)

                areaSpinner.adapter = areaSpinnerAdapter

                areaSpinnerAdapter.notifyDataSetChanged()

                areaSpinner.setSelection(0)




            }
        })

        branchesRec.layoutManager = LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , false)
        branchesRec.adapter =  branchesAdapter
        branchesAdapter.notifyDataSetChanged()

        subTownModelSpiners = Gson().fromJson(branches.get(0).BranchesInCityList.toString(), object : TypeToken<ArrayList<AreaSpinnerModel>>() {}.type) as ArrayList<AreaSpinnerModel>

        var areaSpinnerAdapter = AreaSpinnerAdapter(this@Branches, R.layout.area_name, R.id.areaName, subTownModelSpiners)

        areaSpinner.adapter = areaSpinnerAdapter

        areaSpinnerAdapter.notifyDataSetChanged()

        areaSpinner.setSelection(0)

        areaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
        override fun onNothingSelected(parent: AdapterView<*>?)
        {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
        {
            var branchDataList = Gson().fromJson(subTownModelSpiners.get(position).BranchDataList.toString(), object : TypeToken<ArrayList<BranchDataList>>() {}.type) as ArrayList<BranchDataList>
            mapSetMarkers(branchDataList)
        }

    }

    }



   fun mapSetMarkers(branches: ArrayList<BranchDataList> )
   {
       mMap.clear()
       for (branch in branches)
       {
           mMap.addMarker(MarkerOptions().position(LatLng(branch.Latitude.toDouble(),branch.Longitude.toDouble())))
       }
   }

    override fun onMapReady(googleMap: GoogleMap)
    {
        mMap = googleMap
        GetAllBranches()
    }


}
