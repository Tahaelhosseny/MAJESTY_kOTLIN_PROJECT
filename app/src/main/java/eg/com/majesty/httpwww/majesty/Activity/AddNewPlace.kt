package eg.com.majesty.httpwww.majesty.Activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
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
import android.location.*
import java.io.IOException
import java.util.*
import android.location.Geocoder
import android.support.v4.app.ActivityCompat
import android.widget.ProgressBar
import com.google.android.gms.location.*
import android.support.v4.content.ContextCompat
import android.content.Intent
import android.content.DialogInterface
import android.provider.Settings
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import kotlinx.android.synthetic.main.previous_orders_lay.view.*


class AddNewPlace : Activity(), SearchView.OnQueryTextListener
{

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
    var addressType = "0"   //0 for home 1 for work
    var notesStr = ""
    var REQUEST_ID_MULTIPLE_PERMISSIONS = 50

    var latstr ="0"
    var lonstr ="0"


    private lateinit var userLocationClient : FusedLocationProviderClient
    private lateinit var userLocationCallback : LocationCallback
    val userLocationRequest = LocationRequest().apply {
        interval = 1000
        fastestInterval = 1000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }




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


        if(latstr.equals("0"))
        {

            if (canGetLocation())
            {
                checkPermissions()
            } else {

                showSettingsAlert()
            }

        }else
        {
            StreettStr = Street.text.toString()
            buildingNumberStr = buildingNumber.text.toString()
            floorStr = floor.text.toString()
            apartmentStr = apartment.text.toString()
            landmarkStr = landmark.text.toString()
            notesStr = notes.text.toString()



            if(cityId == -1)
                city.setError(this.resources.getString(R.string.cityfirst))
            else if(areaId == -1)
                area.setError(this.resources.getString(R.string.areafirst))
            else if(subAreaa == -1)
                subArea.setError(this.resources.getString(R.string.selectsubare))
            else if(StreettStr.equals(""))
            {
                Street.setError(this.resources.getString(R.string.enterStreet))
            }else if (buildingNumberStr.equals(""))
            {
                buildingNumber.setError(this.resources.getString(R.string.buildnum))
            }else if (floorStr.equals(""))
            {
                floor.setError(this.resources.getString(R.string.floarNum))
            }else if (apartmentStr.equals(""))
            {
                apartment.setError(this.resources.getString(R.string.ApartmentNumber))
            }else
            {
                saveAdd()
            }
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
            Toast.makeText(applicationContext , this.resources.getString(R.string.Please_select_city_first) , Toast.LENGTH_LONG).show()
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
            Toast.makeText(applicationContext , this.resources.getString(R.string.Please_select_area_first) , Toast.LENGTH_LONG).show()
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
        var makeRequest = MakeRequest("GetCities?isArabic="+Utils.isArabic(this) ,"0",this,"GetCities",true)
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
        var makeRequest = MakeRequest("GetAreas?isArabic="+Utils.isArabic(this)+"&cityId=" + cityId,"0",this,"GetCities",true)

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
        var makeRequest = MakeRequest("GetSubAreas?isArabic="+Utils.isArabic(this)+"&areaId=" + areaId,"0",this,"GetCities",true)

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
        var makeRequest = MakeRequest("AddUserAddress?isArabic="+Utils.isArabic(this)
                +"&subAreaId=" + subAreaa
                + "&userId=" +ID
                + "&street=" + StreettStr
                + "&buildingNumber=" + buildingNumberStr
                + "&floor=" + floorStr
                + "&apartmentNumber=" + apartmentStr
                + "&landmark=" + landmarkStr
                + "&notes=" + notesStr
                + "&lat="+latstr
                + "&long="+lonstr
                + "&addressType=" + addressType,"0",this,"AddUserAddress",true)

        makeRequest.request(object  : VolleyCallback
        {
            override fun onSuccess(result: Map<String, String>)
            {

                var str = result.get("res").toString()
                var jsonObject = Gson().fromJson(str, JsonObject::class.java)
                if( jsonObject.get("NotificationsNumbers").asJsonArray.get(0).asJsonObject.get("Succeed").asBoolean)
                {
                    Toast.makeText(this@AddNewPlace , this@AddNewPlace.resources.getString(R.string.addressAdded) , Toast.LENGTH_LONG).show()
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


        if (canGetLocation())
        {
            checkPermissions()
        } else {

            showSettingsAlert()
        }


    }


    fun onLocationChangeeeed(location: Location)
    {
            var geocoder = Geocoder(this, Locale.getDefault())
            var latitude = location.getLatitude()
            var longitude = location.getLongitude()

            latstr = latitude.toString()
            lonstr = latitude.toString()

            try {
                var addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (addresses != null && addresses.size > 0) {
                    var address = addresses.get(0).getAddressLine(0)
                    var knownName = addresses.get(0).getFeatureName()
                    Street.setText(address + " , " + knownName)
                }
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }


    private fun checkPermissions() {
        val permissionLocation = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
        var listPermissionsNeeded = ArrayList<String>()
        if (permissionLocation != PackageManager.PERMISSION_GRANTED)
        {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toTypedArray(), REQUEST_ID_MULTIPLE_PERMISSIONS)
            }
        } else {
            getMyLocation()
        }

    }


    fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(this)

        // Setting Dialog Title
        alertDialog.setTitle(this.resources.getString(R.string.openGps))

        // Setting Dialog Message
        alertDialog.setMessage(this.resources.getString(R.string.openGps2))

        // On pressing Settings button
        alertDialog.setPositiveButton(
                this.resources.getString(R.string.okay),
                DialogInterface.OnClickListener { dialog, which ->
                    val intent = Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivityForResult(intent , 100)
                })

        alertDialog.show()
    }






    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        val permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionLocation == PackageManager.PERMISSION_GRANTED)
        {
            getMyLocation()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==100)
        {
            checkPermissions()
        }

    }


    fun getMyLocation()
    {

        progress.visibility = View.VISIBLE

        userLocationCallback = object : LocationCallback()
        {
            override fun onLocationResult(locationResult: LocationResult?)
            {
                if(locationResult == null)
                {
                    Toast.makeText(this@AddNewPlace , this@AddNewPlace.resources.getString(R.string.cant_get_location) , Toast.LENGTH_LONG).show()
                    return

                }
                for (location in locationResult.locations)
                {
                    onLocationChangeeeed(location)
                    userLocationClient.removeLocationUpdates(userLocationCallback)
                    break
                }



                progress.visibility = View.GONE

            }
        }

        userLocationClient = LocationServices.getFusedLocationProviderClient(this)
        userLocationClient.requestLocationUpdates(userLocationRequest,userLocationCallback,null)


    }




    fun canGetLocation()  : Boolean
    {



        var lm : LocationManager?=null
        var result = false
        var gps_enabled = false
        var network_enabled = false
        if (lm == null)

            lm =  getSystemService(Context.LOCATION_SERVICE) as LocationManager

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch ( ex:Exception) {

        }
        try {
            network_enabled = lm
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch ( ex:Exception) {
        }
            if (gps_enabled == false || network_enabled == false) {
                result = false;
            } else {
                result = true;
            }

            return result
        }


    fun workkkk(view: View)
    {
        home.setChecked(false)
        work.setChecked(true)
        addressType = "1"
    }


    fun homeee(view: View)
    {

        home.setChecked(true)
        work.setChecked(false)
        addressType = "0"
    }
}