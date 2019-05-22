package eg.com.majesty.httpwww.majesty.Activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
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
import android.support.v4.app.FragmentActivity
import android.widget.AdapterView
import com.bumptech.glide.Glide
import eg.com.majesty.httpwww.majesty.Dialogs.BranchDialog
import eg.com.majesty.httpwww.majesty.Models.BranchDataList
import kotlinx.android.synthetic.main.activity_branches.*
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import java.util.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.gson.JsonArray
import kotlin.collections.ArrayList


class Branches :  FragmentActivity(), OnMapReadyCallback , GoogleMap.OnMarkerClickListener
{



    private lateinit var mMap: GoogleMap
    lateinit   var subTownModelSpiners :ArrayList<AreaSpinnerModel>
    lateinit var branchestemp: ArrayList<BranchDataList>
    var branchDataList = ArrayList<BranchDataList>()
    lateinit var branchDialog :BranchDialog
    lateinit var branches : MutableList<BranchModel>
    var lastSelected = 0
    lateinit var branchesAdapter : BranchesAdapter
    var lat = ""
    var lng = ""
    var nam = ""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_branches)

        var mapFragment =supportFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onResume()
    {
        super.onResume()
        catNamee.setTypeface(Utils.Exo2SemiBold(this))

    }



    fun GetAllBranches()
    {
        var makeRequest = MakeRequest("GetAllBranches?isArabic="+Utils.isArabic(this) ,"0",this,"getUserAddressesAsLines",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                var str = result.get("res").toString()
                var itemType = object : TypeToken<MutableList<BranchModel>>() {}.type
                branches = Gson().fromJson<MutableList<BranchModel>>(str, itemType)
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
        subTownModelSpiners = Gson().fromJson(branches.get(0).BranchesInCityList.toString(), object : TypeToken<ArrayList<AreaSpinnerModel>>() {}.type) as ArrayList<AreaSpinnerModel>

        for(subtown in subTownModelSpiners)
        {

            var temp = Gson().fromJson(subtown.BranchDataList.toString(), object : TypeToken<ArrayList<BranchDataList>>() {}.type) as ArrayList<BranchDataList>
            branchDataList.addAll(temp)
        }

        mapSetMarkers(branchDataList)

        branchesAdapter =  BranchesAdapter(this@Branches , branches , object : UpdateAreaSpinner{
            override fun updateAreaSpinner(cityId: Int)
            {


                branchDataList.clear()
                lastSelected = cityId
                subTownModelSpiners = Gson().fromJson(branches.get(cityId).BranchesInCityList.toString(), object : TypeToken<ArrayList<AreaSpinnerModel>>() {}.type) as ArrayList<AreaSpinnerModel>
                for(subtown in subTownModelSpiners)
                {

                    var temp = Gson().fromJson(subtown.BranchDataList.toString(), object : TypeToken<ArrayList<BranchDataList>>() {}.type) as ArrayList<BranchDataList>
                    branchDataList.addAll(temp)
                }
                mapSetMarkers(branchDataList)
                subTownModelSpiners.add(0 ,AreaSpinnerModel(-1 ,this@Branches.resources.getString(R.string.choose_your_area) , JsonArray()) )
                var areaSpinnerAdapter = AreaSpinnerAdapter(this@Branches, R.layout.area_name, R.id.areaName, subTownModelSpiners)

                areaSpinner.adapter = areaSpinnerAdapter

                areaSpinnerAdapter.notifyDataSetChanged()

                areaSpinner.setSelection(0)


            }
        })


        if(Utils.isArabicBoolean(this))
        {
            branchesRec.layoutManager = LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , true)

        }else
        {
            branchesRec.layoutManager = LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , false)

        }


        branchesRec.adapter =  branchesAdapter
        branchesAdapter.notifyDataSetChanged()

        subTownModelSpiners = Gson().fromJson(branches.get(0).BranchesInCityList.toString(), object : TypeToken<ArrayList<AreaSpinnerModel>>() {}.type) as ArrayList<AreaSpinnerModel>
        subTownModelSpiners.add(0 ,AreaSpinnerModel(-1 ,this@Branches.resources.getString(R.string.choose_your_area) , JsonArray()) )

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

            if(position!=0)
            {
                branchDataList.clear()
                var temp = Gson().fromJson(subTownModelSpiners.get(position).BranchDataList.toString(), object : TypeToken<ArrayList<BranchDataList>>() {}.type) as ArrayList<BranchDataList>
                branchDataList.addAll(temp)
                dialog.visibility = View.VISIBLE
                Glide.with(this@Branches).load(temp.get(0).Image).thumbnail(.2f).into(image)
                name.text = temp.get(0).BranchName
                address.text = temp.get(0).Address
                name.setTypeface(Utils.Exo2SemiBold(this@Branches))
                address.setTypeface(Utils.Exo2Medium(this@Branches))
                directionTxt.setTypeface(Utils.Exo2Bold(this@Branches))
                lat = temp.get(0).Latitude
                lng = temp.get(0).Longitude
                nam = temp.get(0).BranchName


            }else
            {
                branchDataList.clear()

                for(subtown in subTownModelSpiners)
                {
                    var temp = Gson().fromJson(subtown.BranchDataList.toString(), object : TypeToken<ArrayList<BranchDataList>>() {}.type) as ArrayList<BranchDataList>
                    branchDataList.addAll(temp)

                }


                dialog.visibility = View.GONE

            }


            mapSetMarkers(branchDataList)





        }

    }

    }



   fun mapSetMarkers(branches: ArrayList<BranchDataList> )
   {


       var builder = LatLngBounds.Builder()
       branchestemp = branches
       mMap.clear()
       for (branch in branches)
       {

           var marker = mMap.addMarker(MarkerOptions().title(branch.BranchName).position(LatLng(branch.Latitude.toDouble(),branch.Longitude.toDouble())).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)))
           marker.tag = branch.BranchID.toString()
           builder.include(marker.getPosition())
       }

       var width = resources.displayMetrics.widthPixels
       var height = resources.displayMetrics.heightPixels
       var padding = (width * 0.12).toInt()
       var bounds = builder.build()
       var  cu = CameraUpdateFactory.newLatLngBounds(bounds , padding)
       mMap.animateCamera(cu)
   }

    override fun onMapReady(googleMap: GoogleMap)
    {
        mMap = googleMap
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this , R.raw.silver_json))
        mMap.setOnMarkerClickListener(this)
        GetAllBranches()
    }


    override fun onMarkerClick(marker: Marker): Boolean
    {
        try {
            branchDialog.dismiss()
        }catch (e:Exception){}
        var id = marker.tag.toString()

        for (branch in branchestemp)
        {
            if(id.toInt() == branch.BranchID)
            {
                dialog.visibility = View.VISIBLE
                Glide.with(this).load(branch.Image).thumbnail(.2f).into(image)
                name.text = branch.BranchName
                address.text = branch.Address

                name.setTypeface(Utils.Exo2SemiBold(this))
                address.setTypeface(Utils.Exo2Medium(this))
                directionTxt.setTypeface(Utils.Exo2Bold(this))
                lat = branch.Latitude
                lng = branch.Longitude
                nam = branch.BranchName

                break
            }
        }

        return  true
    }


    fun direction (view: View)
    {
        var my_data = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr="+lat+","+lng+nam)
        var intent = Intent(Intent.ACTION_VIEW, Uri.parse(my_data))
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }



    fun backImg(view: View)
    {
        if(dialog.visibility == View.VISIBLE)
            dialog.visibility = View.GONE
        else onBackPressed()

    }


    fun back(view: View)
    {
        if(dialog.visibility == View.VISIBLE)
            dialog.visibility = View.GONE
        else onBackPressed()

    }
}
