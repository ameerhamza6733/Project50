package com.yourdomain.project50.Model

/**
 * Created by apple on 11/26/18.
 */
class Admob(var appId:String?=null,
            var bannerAds5:AdmobAds?=null,
            var bannerAds7:AdmobAds?=null,
            var interstitialAds2:AdmobAds?=null,
            var interstitialAds5: AdmobAds?=null,
            var nativeAds3:AdmobAds?=null,
            var nativeAds8:AdmobAds?=null,
            var nativeAds9:AdmobAds?=null,
            var nativeAds11:AdmobAds?=null,
            var nativeAds13:AdmobAds?=null,
            var publisherId:String?=null,
            var videoAds10:AdmobAds?=null,
            var videoAds12:AdmobAds?=null,
            var videoAds4:AdmobAds?=null
             )
{
    companion object {
       val INTERSTITIAL_AD_ID="ca-app-pub-3940256099942544/1033173712"
        val REWADEDR_VIDEO_AD_ID="ca-app-pub-3940256099942544/5224354917"
        val BANNER_AD_ID="ca-app-pub-3940256099942544/6300978111"
        val NATIVE_AD_ID="ca-app-pub-3940256099942544/2247696110"

    }
}
