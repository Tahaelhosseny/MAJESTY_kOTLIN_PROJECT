package eg.com.majesty.httpwww.majesty.Dialogs
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import eg.com.majesty.httpwww.majesty.netHelper.ONRetryHandler
import kotlinx.android.synthetic.main.conection_lost.*
import android.content.DialogInterface
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.finishAffinity
import android.view.KeyEvent
import eg.com.majesty.httpwww.majesty.GeneralUtils.ForeraaParameter
import eg.com.majesty.httpwww.majesty.GeneralUtils.Utils
import eg.com.majesty.httpwww.majesty.R


class ConnectionLost(var c: Context ,var onRetryHandler: ONRetryHandler) : Dialog(c) {
    var d = this




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Utils.changeLocale(c, c.resources.getStringArray(R.array.languages_tag)[ForeraaParameter(c).getInt( "language" ,0)])

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.conection_lost)
        d.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        d.setCanceledOnTouchOutside(false)
        d.setCancelable(false)
        init()
    }


    private fun init()
    {


       d.setOnKeyListener(object : DialogInterface.OnKeyListener{
            override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean
            {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                {
                    System.exit(0)
                }
                return true

            }
        })

        retry.setOnClickListener({
            onRetryHandler.onRetryHandler("hi")
            d.cancel()
        })
    }

}