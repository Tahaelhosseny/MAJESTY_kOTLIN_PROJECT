package eg.com.majesty.httpwww.majesty

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.jaeger.library.StatusBarUtil
import eg.com.majesty.httpwww.majesty.Activity.MainActivity_
import eg.com.majesty.httpwww.majesty.Activity.TutorialPage_
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_splash)
class Splash : Activity()
{


    override fun onCreate(savedInstanceState: Bundle?)
    {

        StatusBarUtil.setTransparent(this)

        super.onCreate(savedInstanceState)

        var foreraaParameter = ForeraaParameter(applicationContext)
       // foreraaParameter.setString("UserID" , "")
        Handler().postDelayed(
                {
                    if(foreraaParameter.getString("UserID").equals(""))
                        startActivity(Intent(this , TutorialPage_::class.java))
                    else
                        startActivity(Intent(this , MainActivity_::class.java))
                    finish()
                },3000)


    }
}
