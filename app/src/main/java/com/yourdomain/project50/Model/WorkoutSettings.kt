package com.yourdomain.project50.Model

import java.util.*

/**
 * Created by apple on 11/20/18.
 */
class WorkoutSettings(var mute: Boolean = false
                      , var voiceGuide: Boolean = true
                      , var CoachTips: Boolean = true
                      , var remindMeWordOut: ArrayList<String> =ArrayList()
                      , var restTimeInSeconds: Int = 30
                      , var watingCoutDownTime: Int = 30
                       )