package com.example.nammametromvvm.splashscreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.nammametromvvm.R
import com.example.nammametromvvm.databinding.ActivitySplashScreenBinding
import com.example.nammametromvvm.utility.ApiError
import com.example.nammametromvvm.utility.ApiException
import com.example.nammametromvvm.utility.GeneralException
import com.example.nammametromvvm.utility.NoInternetException
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.net.SocketTimeoutException

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private lateinit var viewModel: SplashViewModel
    private val factory: SplashScreenViewModelFactory by instance()
    private val loggerClass: com.example.nammametromvvm.utility.logs.LoggerClass by instance()

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this, factory).get(
            SplashViewModel::class.java
        )
        viewModel.setUpLogs()
        viewModel.saveToDataStore("none")

       /* viewModel.readFromDataStore.observe(this, {
            Log.d("test151", "readFromDataStore: $it")

        })*/
        lifecycleScope.launch {
         // val lastUpdateTime=  viewModel.getLastUpdateTime();
           // Log.d("test152", "lastUpdateTime "+lastUpdateTime.value)


        }
        lifecycleScope.launch {
            try {
                if(viewModel.isUpdateCheckNeeded()){
                    checkForAppUpdate()
                }else{
                    Log.d("test156", "No Need to Check For update")
                }
            } catch (e: GeneralException) {
                loggerClass.error(e);
                Log.d("test151", "GeneralException $e")
            }


        }
    }

    private fun checkForAppUpdate() {
        lifecycleScope.launch {
            Log.d("test151", "onCreate: ")
            try {
                appUpdateInfo(true)
                val ticketModel = viewModel.checkForUpdate()
                Log.d("test151", "onCreate: " + ticketModel.getString("status"))
                appUpdateInfo(false)
            } catch (e: ApiException) {
                loggerClass.error(e);
                Log.d("test151", "ApiException: ")
            } catch (e: NoInternetException) {
                loggerClass.error(e);
                Log.d("test151", "NoInternetException: ")
            } catch (e: ApiError) {
                loggerClass.error(e);
                Log.d("test151", "ApiError: ")
            } catch (e: SocketTimeoutException) {
                loggerClass.error(e);
                Log.d("test151", "SocketTimeoutException: ")
            }

        }
    }

    private fun appUpdateInfo(showAppUpdateInfo: Boolean) {
        if (showAppUpdateInfo) {
            binding.avLoading.smoothToShow()
            binding.tvInfo.text = getString(R.string.checking_for_update)
        } else {
            binding.avLoading.smoothToHide()
            binding.tvInfo.visibility = View.INVISIBLE
        }
    }


}