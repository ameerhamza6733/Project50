package com.yourdomain.project50.Model

/**
 * Created by apple on 11/26/18.
 */
class Admob(var appId:String?=null,
            var bannerAds:AdmobAds?=null,
            var interstitialAds:AdmobAds?=null,
            var nativeAds:AdmobAds?=null,
            var publisherId:String?=null,
            var videoAds:AdmobAds?=null)
{
    companion object {
       val INTERSTITIAL_AD_ID="ca-app-pub-3940256099942544/1033173712"
        val REWADEDR_VIDEO_AD_ID="ca-app-pub-3940256099942544/5224354917"
        val BANNER_AD_ID="ca-app-pub-3940256099942544/6300978111"
        val APP_ID="ca-app-pub-3940256099942544~3347511713";
    }
}
