package eg.com.majesty.httpwww.majesty.Activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import eg.com.majesty.httpwww.majesty.Adapters.AreaAdapter
import eg.com.majesty.httpwww.majesty.Adapters.CityAdapter
import eg.com.majesty.httpwww.majesty.Adapters.SubAreaAdapter
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.InterFaces.UpdateCity
import eg.com.majesty.httpwww.majesty.Models.AreaModel
import eg.com.majesty.httpwww.majesty.Models.CityModel
import eg.com.majesty.httpwww.majesty.Models.SubAreaModel
import eg.com.majesty.httpwww.majesty.R
import eg.com.majesty.httpwww.majesty.netHelper.MakeRequest
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import eg.com.majesty.httpwww.majesty.netHelper.VolleyCallback
import kotlinx.android.synthetic.main.activity_add_new_place.*
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.*
import java.io.IOException
import java.util.*
import android.location.Geocoder
import android.provider.Settings
import android.support.multidex.MultiDex
import android.widget.ProgressBar
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener


class AddNewPlace : Activity(), SearchView.OnQueryTextListener  , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener
{



    private lateinit var mGoogleApiClient: GoogleApiClient
    private var mLocationManager: LocationManager? = null
    lateinit var mLocation: Location
    private var mLocationRequest: LocationRequest? = null
    private val listener: com.google.android.gms.location.LocationListener? = null
    private val UPDATE_INTERVAL = (2 * 1000).toLong()  /* 10 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */
    lateinit var locationManager: LocationManager
    lateinit var adapter :CityAdapter
    lateinit var areaadapter :AreaAdapter
    lateinit var subareaadapter : SubAreaAdapter
    var  cityItemList : MutableList<CityModel> = arrayListOf()
    var  areaItemList : MutableList<AreaModel> = arrayListOf()
    var  subareaItemList : MutableList<SubAreaModel> = arrayListOf()
    var ID :String =""
    var cityId = -1
    var areaId = -1
    var subAreaa = -1
    var StreettStr=""
    var buildingNumberStr = ""
    var floorStr = ""
    var apartmentStr = ""
    var landmarkStr = ""
    var notesStr = ""
    var progress : ProgressBar ? =null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_place)
        citySearch.setOnQueryTextListener(this@AddNewPlace)
        areaSearch.setOnQueryTextListener(this@AddNewPlace)
        subareaSearch.setOnQueryTextListener(this@AddNewPlace)
        ID = ForeraaParameter(applicationContext).getString("UserID")
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)



    }




    override fun onQueryTextSubmit(query: String?): Boolean
    {
        if(cityLayout.visibility == View.VISIBLE)
            adapter.filter.filter(query)

        if(areaLayout.visibility == View.VISIBLE)
            areaadapter.filter.filter(query)

        if(subareaLayout.visibility == View.VISIBLE)
            subareaadapter.filter.filter(query)

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean
    {
        if(cityLayout.visibility == View.VISIBLE)
            adapter.filter.filter(newText)

        if(areaLayout.visibility == View.VISIBLE)
            areaadapter.filter.filter(newText)

        if(subareaLayout.visibility == View.VISIBLE)
            subareaadapter.filter.filter(newText)
        return true
    }



    fun cityBack(view: View)
    {
        cityLayout.visibility = View.GONE
    }

    fun areaBack(view: View)
    {
        areaLayout.visibility = View.GONE
    }
    fun subareaBack(view: View)
    {
        subareaLayout.visibility = View.GONE
    }



    fun saveAddress(view: View)
    {

        StreettStr = Street.text.toString()
        buildingNumberStr = buildingNumber.text.toString()
        floorStr = floor.text.toString()
        apartmentStr = apartment.text.toString()
        landmarkStr = landmark.text.toString()
        notesStr = notes.text.toString()



        if(cityId == -1)
            city.setError("Please Select City")
        else if(areaId == -1)
            area.setError("Please Select Area")
        else if(subAreaa == -1)
            subArea.setError("Please Select SubArea")
        else if(StreettStr.equals(""))
        {
            Street.setError("Please Enter Street")
        }else if (buildingNumberStr.equals(""))
        {
            buildingNumber.setError("Please Enter BuildingNumber")
        }else if (floorStr.equals(""))
        {
            floor.setError("Please Enter FloorNumber")
        }else if (apartmentStr.equals(""))
        {
            apartment.setError("Please Enter ApartmentNumber")
        }else
        {
            saveAdd()
        }



    }



    fun city(view: View)
    {
        if(cityItemList.size == 0)
            getCites()
        else
            cityLayout.visibility = View.VISIBLE

    }




    fun area(view: View)
    {


        if(cityId == -1)
        {
            Toast.makeText(applicationContext , "Please Select City First" , Toast.LENGTH_LONG).show()
        }
        else
        {
            if(areaItemList.size == 0)
                getArea()
            else
                areaLayout.visibility = View.VISIBLE
        }



    }


    fun subArea(view: View)
    {


        if(areaId == -1)
        {
            Toast.makeText(applicationContext , "Please Select Area First" , Toast.LENGTH_LONG).show()
        }
        else
        {
            if(subareaItemList.size == 0)
                getSubAreas()
            else
                subareaLayout.visibility = View.VISIBLE
        }



    }




    fun getCites()
    {
        var makeRequest = MakeRequest("GetCities?isArabic=false" ,"0",this,"GetCities",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                var str = result.get("res").toString()
                var jsonObject = Gson().fromJson(str, JsonArray::class.java)
                val itemType = object : TypeToken<List<CityModel>>() {}.type
                cityItemList = Gson().fromJson<MutableList<CityModel>>(jsonObject.toString(), itemType)
                adapter = CityAdapter(this@AddNewPlace , cityItemList , object : UpdateCity{
                    override fun update(cityId: Int ,  CityName : String)
                    {
                        cityLayout.visibility = View.GONE
                        city.setText(CityName)
                        this@AddNewPlace.cityId =cityId
                    }
                })
                cityRec.layoutManager = LinearLayoutManager(this@AddNewPlace)
                cityRec.adapter = adapter
                adapter.notifyDataSetChanged()
                cityLayout.visibility = View.VISIBLE
            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })
    }


    fun getArea()
    {
        var makeRequest = MakeRequest("GetAreas?isArabic=false&cityId=" + cityId,"0",this,"GetCities",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                var str = result.get("res").toString()
                var jsonObject = Gson().fromJson(str, JsonArray::class.java)
                val itemType = object : TypeToken<List<AreaModel>>() {}.type
                areaItemList = Gson().fromJson<MutableList<AreaModel>>(jsonObject.toString(), itemType)
                areaadapter = AreaAdapter(this@AddNewPlace , areaItemList , object : UpdateCity{
                    override fun update(cityId: Int ,  CityName : String)
                    {
                        areaLayout.visibility = View.GONE
                        area.setText(CityName)
                        this@AddNewPlace.areaId =cityId
                    }
                })
                areaRec.layoutManager = LinearLayoutManager(this@AddNewPlace)
                areaRec.adapter = areaadapter
                areaadapter.notifyDataSetChanged()
                areaLayout.visibility = View.VISIBLE
            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })
    }



    fun getSubAreas()
    {
        var makeRequest = MakeRequest("GetSubAreas?isArabic=false&areaId=" + areaId,"0",this,"GetCities",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                var str = result.get("res").toString()
                var jsonObject = Gson().fromJson(str, JsonArray::class.java)
                val itemType = object : TypeToken<List<SubAreaModel>>() {}.type
                subareaItemList = Gson().fromJson<MutableList<SubAreaModel>>(jsonObject.toString(), itemType)
                subareaadapter = SubAreaAdapter(this@AddNewPlace , subareaItemList , object : UpdateCity{
                    override fun update(cityId: Int ,  CityName : String)
                    {
                        subareaLayout.visibility = View.GONE
                        subArea.setText(CityName)
                        this@AddNewPlace.subAreaa =cityId
                    }
                })
                subareaRec.layoutManager = LinearLayoutManager(this@AddNewPlace)
                subareaRec.adapter = subareaadapter
                subareaadapter.notifyDataSetChanged()
                subareaLayout.visibility = View.VISIBLE
            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })
    }


    fun saveAdd()
    {
        var makeRequest = MakeRequest("AddUserAddress?isArabic=false&subAreaId=" + subAreaa
                + "&userId=" +ID
                + "&street=" + StreettStr
                + "&buildingNumber=" + buildingNumberStr
                + "&floor=" + floorStr
                + "&apartmentNumber=" + apartmentStr
                + "&landmark=" + landmarkStr
                + "&notes=" + notesStr,"0",this,"AddUserAddress",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                var str = result.get("res").toString()
                var jsonObject = Gson().fromJson(str, JsonObject::class.java)
                if( jsonObject.get("NotificationsNumbers").asJsonArray.get(0).asJsonObject.get("Succeed").asBoolean)
                {
                    Toast.makeText(this@AddNewPlace , "Address Add Successfully !!" , Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        } ,object : ONRetryHandler
        {
            override fun onRetryHandler(funName: String)
            {

            }
        })
    }



    fun back(view: View)
    {
        onBackPressed()
    }

    fun latLong(view: View)
    {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        checkLocation()

    }






    override fun onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    override fun onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    override fun onConnectionSuspended(p0: Int)
    {
        mGoogleApiClient.connect()
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
    }

    override fun onLocationChanged(location: Location)
    {
        onLocationChangeeeed(location)
    }

    override fun onConnected(p0: Bundle?) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }


        startLocationUpdates();

        var fusedLocationProviderClient :
                FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient .getLastLocation()
                .addOnSuccessListener(this, OnSuccessListener<Location> { location ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null)
                    {
                        // Logic to handle location object
                        mLocation = location

                    }
                })
    }


    private fun checkLocation(): Boolean {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private fun isLocationEnabled(): Boolean {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " + "use this app")
                .setPositiveButton("Location Settings", DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                    val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(myIntent)
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { paramDialogInterface, paramInt -> })
        dialog.show()
    }

    protected fun startLocationUpdates() {

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this)
    }








    fun  onLocationChangeeeed(location : Location)
    {
    var geocoder =  Geocoder(this, Locale.getDefault())
    var latitude = location.getLatitude()
    var longitude = location.getLongitude()


    try {
        var addresses = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses != null && addresses.size > 0) {
            var address = addresses.get(0).getAddressLine(0)
            var knownName = addresses.get(0).getFeatureName()
            Street.setText(address + " , " + knownName)
        }
    } catch (e : IOException) {
        // TODO Auto-generated catch block
        e.printStackTrace()
    }
}










}



