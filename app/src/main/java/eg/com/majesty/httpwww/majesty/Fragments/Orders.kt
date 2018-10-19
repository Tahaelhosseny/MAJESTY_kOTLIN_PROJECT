package eg.com.majesty.httpwww.majesty.Fragments


import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import eg.com.majesty.httpwww.majesty.R
import kotlinx.android.synthetic.main.activity_main.*


class Orders : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }


    override fun onResume() {
        super.onResume()
        activity.back.visibility = View.GONE
        activity.menu.visibility = View.VISIBLE
    }
}
