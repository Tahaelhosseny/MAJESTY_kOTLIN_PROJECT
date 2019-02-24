package eg.com.majesty.httpwww.majesty.Dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window.FEATURE_NO_TITLE
import android.os.Bundle
import android.support.v7.widget.CardView
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import eg.com.majesty.httpwww.majesty.R
import kotlinx.android.synthetic.main.branch_dialog.*


class BranchDialog(var context: Activity ,var strName :String? ,var strImage : String? ,var  strAdd :String? ,var  strLat :String? ,var  strLong :String?) : Dialog(context)
{



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.branch_dialog)
        this.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        init()
    }



    fun init()
    {
        name.text = strName
    }


}