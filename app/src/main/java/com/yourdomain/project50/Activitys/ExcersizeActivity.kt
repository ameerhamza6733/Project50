package com.yourdomain.project50.Activitys

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.yourdomain.project50.CustomCountDownTimer
import com.yourdomain.project50.Fragments.PauseExcersizeFragment
import com.yourdomain.project50.Fragments.QuitFragment
import com.yourdomain.project50.Fragments.WatingForNextFragment
import com.yourdomain.project50.Fragments.WatingToStartExcersizeFragment
import com.yourdomain.project50.MY_Shared_PREF
import com.yourdomain.project50.Model.ExcersizeDays
import com.yourdomain.project50.Model.Excesizes
import com.yourdomain.project50.R
import com.yourdomain.project50.Utils
import com.yourdomain.project50.Utils.CountTotalTime
import com.yourdomain.project50.ViewModle.GetFullBodyPlanceExcersizesByDayViewModle
import java.util.concurrent.TimeUnit
import android.view.WindowManager
import android.os.Build



class ExcersizeActivity : AppCompatActivity(), WatingToStartExcersizeFragment.OnFragmentInteractionListener, PauseExcersizeFragment.OnResumeListener, QuitFragment.OnQuitListener,WatingForNextFragment.OnNextExcersizeDemoFragmentListener {
    override fun onSkip() {
        onNext(true)
    }

    override fun onQuit() {
        finish()
    }

    override fun onContinue() {
        countDown?.resume()
    }

    override fun onComeBacKLater() {
    }

    override fun ResumeListener() {
        countDown?.resume()
    }

    override fun onCountDownDonw() {
        onNext(true)
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
    private lateinit var mbtNext: ImageButton
    private lateinit var mbtBack: ImageButton
    private lateinit var mbtdone: ImageButton

    private var currentDay: ExcersizeDays? = null
    private var excesizes: Excesizes? = null
    private var counter = -1
    private var countDown: CustomCountDownTimer? = null
    private var currentDayKey: Int = -3

    private var TAG="ExcersizeActivity";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.each_full_screen_excersize)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        findViews()

        currentDayKey = intent.getIntExtra(EXTRA_DAY, -2)
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
                val fragmet = WatingToStartExcersizeFragment.newInstance(CountTotalTime(it.viewType, it.seconds), string, it.detail[0])
                fragmet.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                fragmet.show(supportFragmentManager, "WatingToStartExcersizeFragment")
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
        mbtNext = findViewById(R.id.btNext)
        mbtBack = findViewById(R.id.btBack)
        mbtdone = findViewById(R.id.btDone)
        mLayout = findViewById(R.id.type_unlimted)


        mbtdone.setOnClickListener { onNext(false);updateExcersizeCountInSharePref() }
        mbtBack.setOnClickListener { onBack() }
        mbtNext.setOnClickListener { onNext(false);updateExcersizeCountInSharePref() }
        mbtStop.setOnClickListener {
            try {
                onPauseExcersize()
                countDown?.pause()
            } catch (E: Exception) {
                E.printStackTrace()
            }
        }
    }

    private fun onPauseExcersize() {
        var seconds = ""
        if (excesizes?.viewType!![counter] == Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE) {
            seconds = excesizes?.seconds?.get(counter)?.toString() + "''"
        } else if (excesizes?.viewType!![counter] == Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE) {
            seconds = "x" + excesizes?.seconds?.get(counter)?.toString()

        }
        val pauseExcersizeFragment = PauseExcersizeFragment.newInstance(excesizes?.title!![counter], seconds, excesizes?.icons!![counter])
        pauseExcersizeFragment.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        pauseExcersizeFragment.show(supportFragmentManager, "pauseExcersizeFragment")

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
        mtvTitle.text = excesizes?.title!![counter].toUpperCase()
        mTotalProgressBar.progress = counter
        mLayout.visibility = View.VISIBLE
        mCurrentProgressBar.visibility = View.INVISIBLE
        mbtStop.visibility = View.INVISIBLE


    }

    private fun onNext(showWatingForNextFragment: Boolean) {
        if (showWatingForNextFragment) {
            counter++
            if (excesizes?.viewType!![counter] == Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE) {
                updateUIWithCountDown()
            } else if (excesizes?.viewType!![counter] == Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE) {
                updateWithOutCountDownUI()

            }
        } else {
            var temp = ""
            if (excesizes?.viewType!![counter + 1] == Excesizes.VIEW_TYPE_LIMTED_EXCERSIZE) {
                temp = temp + excesizes?.seconds!![counter + 1].toString() + "s"
            } else if (excesizes?.viewType!![counter + 1] == Excesizes.VIEW_TYPE_UN_LIMTED_EXCERSIZE) {
                updateWithOutCountDownUI()
                temp = temp + "x " + excesizes?.seconds!![counter + 1].toString()
            }
            Log.d(TAG,"showing data for wating fragment: "+temp)
            val watingForNextFragment = WatingForNextFragment.newInstance(excesizes?.title!![counter + 1], temp, "NEXT " + (counter + 1).toString() +"/"+(excesizes!!.icons.size).toString(), excesizes?.icons!![counter + 1])
            watingForNextFragment.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            watingForNextFragment.show(supportFragmentManager, "watingForNextFragment")

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
        mLayout.visibility = View.INVISIBLE
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
        mtvTitle.text = excesizes?.title!![counter].toUpperCase()
        mTotalProgressBar.progress = counter
        mCurrentProgressBar.max = excesizes?.seconds!![counter].toInt()
        var seconds = TimeUnit.SECONDS.toMillis(excesizes?.seconds!![counter]?.toLong())
        countDown = object : CustomCountDownTimer(seconds, 1000) {
            override fun onFinish() {

                onNext(false)
                mCurrentProgressBar.progress = 0
                updateExcersizeCountInSharePref()
            }

            override fun onTick(millisUntilFinished: Long) {
                mCurrentProgressBar.progress = (millisUntilFinished / 1000).toInt()
            }

        }
        countDown?.start()
    }

    private fun updateExcersizeCountInSharePref() {
        if (currentDayKey == -3) return
        MY_Shared_PREF.saveCurrentDay(application, ExcersizeDays(currentDayKey + 1, ExcersizeDays.VIEW_TYPE_DAY, excesizes?.title?.size?.toLong()!!, counter.toLong(), Utils.toPersentage(counter, excesizes?.title?.size!!)))
    }

    override fun onPause() {
        Log.d(TAG,"onPause");
        countDown?.cancel()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBackPressed() {
        countDown?.pause()
        val quitFragment = QuitFragment()
        quitFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
        quitFragment.show(supportFragmentManager, "quitFragment")

    }

}
