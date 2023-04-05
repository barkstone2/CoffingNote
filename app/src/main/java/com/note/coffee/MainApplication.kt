package com.note.coffee

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.android.gms.ads.*
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import dagger.hilt.android.HiltAndroidApp
import java.util.*

@HiltAndroidApp
class MainApplication: Application(), Application.ActivityLifecycleCallbacks {

    private lateinit var appOpenAdManager: AppOpenAdManager
    private var currentActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
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
        onShowAdCompleteListener: OnShowAdCompleteListener,
        onAdLoaded: () -> Unit = {}
    ) {
        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener, onAdLoaded)
    }

    interface OnShowAdCompleteListener {
        fun onShowAdComplete()
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
                if(isAdAvailable()) onAdLoaded()
                return
            }

            isLoadingAd = true
            val request = AdRequest.Builder().build()
            AppOpenAd.load(
                context,
                context.getString(R.string.ad_mob_test_opening_id),
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
            onShowAdCompleteListener: OnShowAdCompleteListener,
            onAdLoaded: () -> Unit = {}
        ) {
            // If the app open ad is already showing, do not show the ad again.

            if (isShowingAd) {
                Log.d("main", "The app open ad is already showing.")
                return
            }

            // If the app open ad is not available yet, invoke the callback then load the ad.
            if (!isAdAvailable()) {
                Log.d("main", "The app open ad is not ready yet.")
                onShowAdCompleteListener.onShowAdComplete()
                loadAd(activity, onAdLoaded)
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

                        onShowAdCompleteListener.onShowAdComplete()
                        loadAd(activity)
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        // Called when fullscreen content failed to show.
                        // Set the reference to null so isAdAvailable() returns false.
                        Log.d("main", adError.message)
                        appOpenAd = null
                        isShowingAd = false

                        onShowAdCompleteListener.onShowAdComplete()
                        loadAd(activity)
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

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
        // Updating the currentActivity only when an ad is not showing.
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity
        }
    }

    override fun onActivityResumed(p0: Activity) {
    }

    override fun onActivityPaused(p0: Activity) {
    }

    override fun onActivityStopped(p0: Activity) {
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityDestroyed(p0: Activity) {
    }

}