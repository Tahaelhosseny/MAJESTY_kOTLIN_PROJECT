package eg.com.majesty.httpwww.majesty.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import eg.com.majesty.httpwww.majesty.R
import org.androidannotations.annotations.Click

class MyPlaces : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_places)
    }



    fun addNewAddress(view : View)
    {
        startActivity(Intent(this , AddNewPlace::class.java))
    }
}
