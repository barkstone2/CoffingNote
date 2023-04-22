package com.note.coffee

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import dagger.hilt.android.HiltAndroidApp
import java.util.Date

@HiltAndroidApp
class MainApplication: Application() {

    private lateinit var appOpenAdManager: AppOpenAdManager

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {}
        appOpenAdManager = AppOpenAdManager()
    }

    fun loadAd(
        onAdLoaded: () -> Unit,
        onAdLoadFailed: () -> Unit,
    ) {
        appOpenAdManager.loadAd(this, onAdLoaded, onAdLoadFailed)
    }

    fun showAdIfAvailable(
        activity: Activity,
        onShowAdComplete: () -> Unit,
        onNextAdLoaded: () -> Unit
    ) {
        appOpenAdManager.showAdIfAvailable(activity, onShowAdComplete, onNextAdLoaded)
    }

    /** Inner class that loads and shows app open ads. */
    private inner class AppOpenAdManager {
        private var appOpenAd: AppOpenAd? = null
        private var isLoadingAd = false
        var isShowingAd = false
        private var loadTime: Long = 0

        /** Request an ad. */
        fun loadAd(
            context: Context,
            onAdLoaded: () -> Unit = {},
            onAdLoadFailed: () -> Unit = {}
        ) {
            // Do not load ad if there is an unused ad or one is already loading.
            if (isLoadingAd || isAdAvailable()) {
                onAdLoaded()
                return
            }

            isLoadingAd = true
            val request = AdRequest.Builder().build()
            AppOpenAd.load(
                context,
                context.getString(R.string.ad_mob_opening_id),
                request,
                object : AppOpenAdLoadCallback() {

                    override fun onAdLoaded(ad: AppOpenAd) {
                        Log.d("main", "Ad was loaded.")
                        appOpenAd = ad
                        isLoadingAd = false
                        loadTime = Date().time
                        onAdLoaded()
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        Log.d("main", loadAdError.message)
                        isLoadingAd = false
                        onAdLoadFailed()
                    }
                })
        }

        fun showAdIfAvailable(
            activity: Activity,
            onShowAdComplete: () -> Unit,
            onAdLoaded: () -> Unit
        ) {
            // If the app open ad is already showing, do not show the ad again.

            if (isShowingAd) {
                Log.d("main", "The app open ad is already showing.")
                onShowAdComplete()
                return
            }

            // If the app open ad is not available yet, invoke the callback then load the ad.
            if (!isAdAvailable()) {
                Log.d("main", "The app open ad is not ready yet.")
                loadAd(activity, onAdLoaded, onAdLoaded)
                return
            }

            appOpenAd?.setFullScreenContentCallback(
                object : FullScreenContentCallback() {

                    override fun onAdDismissedFullScreenContent() {
                        // Called when full screen content is dismissed.
                        // Set the reference to null so isAdAvailable() returns false.
                        Log.d("main", "Ad dismissed fullscreen content.")
                        appOpenAd = null
                        isShowingAd = false

                        onShowAdComplete()
                        loadAd(activity, onAdLoaded, onAdLoaded)
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        // Called when fullscreen content failed to show.
                        // Set the reference to null so isAdAvailable() returns false.
                        Log.d("main", adError.message)
                        appOpenAd = null
                        isShowingAd = false

                        onShowAdComplete()
                        loadAd(activity, onAdLoaded, onAdLoaded)
                    }

                    override fun onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        Log.d("main", "Ad showed fullscreen content.")
                    }
                })
            isShowingAd = true
            appOpenAd?.show(activity)
        }

        private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
            val dateDifference: Long = Date().time - loadTime
            val numMilliSecondsPerHour: Long = 3600000
            return dateDifference < numMilliSecondsPerHour * numHours
        }

        private fun isAdAvailable(): Boolean {
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
        }

    }

}