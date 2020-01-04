package com.appyhigh.feedly.ui.menu

import android.os.Bundle
import androidx.fragment.app.Fragment


abstract class BackHandledFragment : Fragment() {
    var backHandlerInterface: BackHandlerInterface? = null
    abstract val tagText: String?
    abstract fun onBackPressed(): Boolean
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backHandlerInterface = if (getActivity() !is BackHandlerInterface) {
            throw ClassCastException("Hosting activity must implement BackHandlerInterface")
        } else {
            getActivity() as BackHandlerInterface?
        }
    }

    override fun onStart() {
        super.onStart()
        // Mark this fragment as the selected Fragment.
        backHandlerInterface!!.setSelectedFragment(this)
    }

    interface BackHandlerInterface {
        fun setSelectedFragment(backHandledFragment: BackHandledFragment?)
    }
}