package eg.com.majesty.httpwww.majesty.Dialogs
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.Window
import com.bumptech.glide.Glide
import eg.com.majesty.httpwww.majesty.R
import kotlinx.android.synthetic.main.loading_act.*


class LoadingDialog(var c: Context) : Dialog(c) {
    var d = this




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.loading_act)
        d.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        d.setCanceledOnTouchOutside(false)
        d.setCancelable(false)
        init()
    }


    private fun init() {

        Glide.with(c).asGif().load(R.drawable.loading).into(load)


    }

}