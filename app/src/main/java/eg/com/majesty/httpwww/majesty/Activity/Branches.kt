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
import java.util.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*


class Branches :  FragmentActivity(), OnMapReadyCallback , GoogleMap.OnMarkerClickListener
{



    private lateinit var mMap: GoogleMap
    lateinit   var subTownModelSpiners :ArrayList<AreaSpinnerModel>
    lateinit var branchestemp: ArrayList<BranchDataList>
    lateinit var branchDialog :BranchDialog



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


       val builder = LatLngBounds.Builder()








       branchestemp = branches
       mMap.clear()
       for (branch in branches)
       {

           var marker = mMap.addMarker(MarkerOptions().title(branch.BranchName).position(LatLng(branch.Latitude.toDouble(),branch.Longitude.toDouble())).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)))
           marker.tag = branch.BranchID.toString()
           builder.include(marker.getPosition())
       }


       val bounds = builder.build()
       var  cu = CameraUpdateFactory.newLatLngZoom(bounds.center, 12f)
       mMap.moveCamera(cu)
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

                direction.setOnClickListener(View.OnClickListener
                {

                    val my_data = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr="+branch.Latitude+","+branch.Longitude+branch.BranchName)

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(my_data))
                    intent.setPackage("com.google.android.apps.maps")
                    startActivity(intent)

                })




                break
            }
        }

        return  true
    }




    fun backImg(view: View)
    {
        dialog.visibility = View.GONE

    }
}
