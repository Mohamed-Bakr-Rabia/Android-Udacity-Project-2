package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.database.AsteroidDatabaseDao
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel(
    private val databaseDao: AsteroidDatabaseDao,
     application: Application) : AndroidViewModel(application) {
        var date: Date = Date()
        var formatter: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        var currentDate: String = formatter.format(date)
        val asteroids = databaseDao.getAllAsteroid(currentDate)
        private var asteroidList: ArrayList<Asteroid>? = null

        private val _pictureOfDay = MutableLiveData<PictureOfDay>()
        val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

        private val retrofitService = AsteroidApi.retrofitService


    init {
            initialize()
        }

    private fun initialize() {
        retrofitService.getAsteroids(Constants.API_KEY).enqueue(object :
            Callback<String> {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(call: Call<String>, response: Response<String>) {
                asteroidList = parseAsteroidsJsonResult(JSONObject(response.body().toString()))
                addToDatabase()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                //Log.d("TAG",t.toString())

            }
        })

        retrofitService.getImageOfDay(Constants.API_KEY).enqueue(object :Callback<PictureOfDay>{
            override fun onResponse(call: Call<PictureOfDay>, response: Response<PictureOfDay>) {
                _pictureOfDay.value = response.body()
            }

            override fun onFailure(call: Call<PictureOfDay>, t: Throwable) {

            }
        })
    }


    private val _navigateToDetailFragment = MutableLiveData<Asteroid?>()
    val navigateToDetailFragment get() = _navigateToDetailFragment

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetailFragment.value = asteroid
    }
    fun onNavigateDone(){
        _navigateToDetailFragment.value = null
    }

    fun addToDatabase(){
        viewModelScope.launch {
            asteroidList?.let {
                databaseDao.insertAll(it)
            }
        }
    }
}



