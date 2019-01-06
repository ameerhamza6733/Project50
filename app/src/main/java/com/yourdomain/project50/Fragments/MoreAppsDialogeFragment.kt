package com.yourdomain.project50.Fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yourdomain.project50.Activitys.EachPlanActivity
import com.yourdomain.project50.R
import com.yourdomain.project50.Utils

/**
 * Created by hamza rafiq on 12/1/18.
 */
class MoreAppsDialogeFragment : DialogFragment() {
    private lateinit var btLatter: TextView
    private lateinit var btyas: TextView
    private var intdex = -1
    private var moreAppsDevIdorAppId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            intdex = arguments!!.getInt(ARG_EXTRA_INDEX)
            moreAppsDevIdorAppId = arguments!!.getString(ARG_EXTRA_DEV_ID_OR_APP_ID)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.more_app_dialoge_framgment, container, false)
        btLatter = view.findViewById(R.id.btLatter)
        btyas = view.findViewById(R.id.btYasNow)
        btLatter.setOnClickListener {
            val intent = Intent(activity, EachPlanActivity::class.java)
            intent.putExtra(EachPlanActivity.EXTRA_PLAN, intdex)
            activity?.startActivity(intent)
            activity?.finish()
        }
        btyas.setOnClickListener {
            moreAppsDevIdorAppId?.let {
                Utils.openBrowser(activity?.application,it)
            }

        }
        return view
    }




    companion object {

        private val ARG_EXTRA_INDEX = "ARG_EXTRA_INDEX"
        private val ARG_EXTRA_DEV_ID_OR_APP_ID = "ARG_EXTRA_DEV_ID_OR_APP_ID"

        fun newInstance(index: Int, devOrAppId: String?): MoreAppsDialogeFragment {
            val fragment = MoreAppsDialogeFragment()
            val args = Bundle()
            args.putInt(ARG_EXTRA_INDEX, index)
            args.putString(ARG_EXTRA_DEV_ID_OR_APP_ID, devOrAppId)
            fragment.arguments = args
            return fragment
        }
    }
}