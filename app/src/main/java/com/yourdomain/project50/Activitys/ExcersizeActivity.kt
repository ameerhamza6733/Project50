package com.yourdomain.project50.Activitys

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.yourdomain.project50.Fragments.WatingFragment
import com.yourdomain.project50.MY_Shared_PREF
import com.yourdomain.project50.Model.ExcersizeDays
import com.yourdomain.project50.Model.Excesizes
import com.yourdomain.project50.R
import com.yourdomain.project50.Utils.CountTotalTime
import com.yourdomain.project50.ViewModle.GetFullBodyPlanceExcersizesByDayViewModle
import java.util.concurrent.TimeUnit

class ExcersizeActivity : AppCompatActivity(), WatingFragment.OnFragmentInteractionListener {
    override fun onCountDownDonw() {
        onNext()
    }


    companion object {
        val EXTRA_DAY = "ExcersizeActivity.extra day";
    }

    private lateinit var mTotalProgressBar: ProgressBar
    private lateinit var mCurrentProgressBar: ProgressBar
    private lateinit var mtotalTextView: TextView
    private lateinit var mImageVIew: ImageView
    private lateinit var mTotalSeconds: TextView
    private lateinit var mtvescription: TextView
    private lateinit var mtvTitle: TextView
    private lateinit var mbtSpeaker: ImageButton
    private lateinit var mbtStop: TextView
    private lateinit var mLayout: LinearLayout
    private lateinit var mbtNext:ImageButton
    private lateinit var mbtBack:ImageButton
    private lateinit var mbtdone:ImageButton

    private var currentDay: ExcersizeDays? = null
    private var excesizes: Excesizes? = null
    private var counter = -1
    private var countDown: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.each_full_screen_excersize)

        findViews()

        val currentDayKey = intent.getIntExtra(EXTRA_DAY, -2)
        if (currentDayKey != -2) {
            currentDay = MY_Shared_PREF.getCurrentDay(application, currentDayKey.toString())

        }
        val modle = ViewModelProviders.of(this)[GetFullBodyPlanceExcersizesByDayViewModle::class.java]
        modle.getExcersizs(currentDayKey)?.observe(this, Observer {
            if (it != null) {
                excesizes = it
                var string = ""
                if (it.viewType[0] == Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE) {
                    string = it.seconds[0].toString() + "''"
                } else if (it.viewType[0] == Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE) {
                    string = "x" + it.seconds[0].toString()
                }
                val fragmet = WatingFragment.newInstance(CountTotalTime(it.viewType,it.seconds), string, it.detail[0])
                fragmet.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                fragmet.show(supportFragmentManager, null)
            }
        })


    }

    private fun findViews() {
        mTotalProgressBar = findViewById(R.id.totalProgressBar)
        mCurrentProgressBar = findViewById(R.id.thisProgressBar)
        mImageVIew = findViewById(R.id.icon)
        mTotalSeconds = findViewById(R.id.tvSeconds)
        mtvTitle = findViewById(R.id.tvTitle)
        mtvescription = findViewById(R.id.tvDescription)
        mtotalTextView = findViewById(R.id.tvRemaning);
        mbtStop = findViewById(R.id.btPassExcersize)
        mbtSpeaker = findViewById(R.id.btSpeaker)
        mbtNext=findViewById(R.id.btNext)
        mbtBack=findViewById(R.id.btBack)
        mbtdone=findViewById(R.id.btDone)
        mLayout=findViewById(R.id.type_unlimted)


        mbtdone.setOnClickListener { onNext() }
        mbtBack.setOnClickListener { onBack() }
        mbtNext.setOnClickListener { onNext() }
    }

    private fun updateWithOutCountDownUI() {

        mTotalProgressBar.max = excesizes?.title?.size!!
        mtotalTextView.text = counter.toString() + "/" + excesizes?.icons?.size.toString()
        Glide.with(this).asGif().load(excesizes?.icons?.get(counter)).into(mImageVIew)
        if (excesizes?.viewType!![counter] == Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE) {
            mTotalSeconds.text = excesizes?.seconds?.get(counter)?.toString() + "''"
        } else if (excesizes?.viewType!![counter] == Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE) {
            mTotalSeconds.text = "x" + excesizes?.seconds?.get(counter)?.toString()

        }
        mtvescription.text = excesizes?.detail!![counter]
        mtvTitle.text = excesizes?.title!![counter]
        mTotalProgressBar.progress = counter
        mLayout.visibility=View.VISIBLE
        mCurrentProgressBar.visibility = View.INVISIBLE
        mbtStop.visibility = View.INVISIBLE


    }

    private fun onNext() {
        counter++
        if (excesizes?.viewType!![counter] == Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE) {
            updateUIWithCountDown()
        } else if (excesizes?.viewType!![counter] == Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE) {
            updateWithOutCountDownUI()

        }
    }

    private fun onBack() {
        counter--
        if (excesizes?.viewType!![counter] == Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE) {
            updateUIWithCountDown()
        } else if (excesizes?.viewType!![counter] == Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE) {
            updateWithOutCountDownUI()

        }
    }

    private fun updateUIWithCountDown() {
        mLayout.visibility=View.INVISIBLE
        mCurrentProgressBar.visibility = View.VISIBLE
        mbtStop.visibility = View.VISIBLE
        mTotalProgressBar.max = excesizes?.title?.size!!
        mtotalTextView.text = counter.toString() + "/" + excesizes?.icons?.size.toString()
        Glide.with(this).asGif().load(excesizes?.icons?.get(counter)).into(mImageVIew)
        if (excesizes?.viewType!![counter] == Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE) {
            mTotalSeconds.text = excesizes?.seconds?.get(counter)?.toString() + "''"
        } else if (excesizes?.viewType!![counter] == Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE) {
            mTotalSeconds.text = "x" + excesizes?.seconds?.get(counter)?.toString()

        }
        mtvescription.text = excesizes?.detail!![counter]
        mtvTitle.text = excesizes?.title!![counter]
        mTotalProgressBar.progress = counter
        mCurrentProgressBar.max = excesizes?.seconds!![counter].toInt()
        var seconds = TimeUnit.SECONDS.toMillis(excesizes?.seconds!![counter]?.toLong())
        countDown = object : CountDownTimer(seconds, 1000) {
            override fun onFinish() {
                onNext()
                mCurrentProgressBar.progress = 0
            }

            override fun onTick(millisUntilFinished: Long) {
                mCurrentProgressBar.progress = (millisUntilFinished / 1000).toInt()
            }

        }
        countDown?.start()
    }

    override fun onPause() {
            countDown?.cancel()
        super.onPause()
    }
}
