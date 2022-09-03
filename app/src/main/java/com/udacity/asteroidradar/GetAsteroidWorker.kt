package com.udacity.asteroidradar

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import kotlin.collections.ArrayList


class GetAsteroidWorker(context:Context, params:WorkerParameters, appContext: Context):
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "GetAsteroidWorker"
    }
    override suspend fun doWork(): Result {

        AsteroidApi.retrofitService.getAsteroids(Constants.API_KEY).enqueue(object :
            Callback<String> {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val asteroidList: ArrayList<Asteroid> = parseAsteroidsJsonResult(JSONObject(response.body().toString()))
                runBlocking {
                    addToDatabase(asteroidList)
                }

            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                //Log.d("TAG",t.toString())

            }
        })

        return try {



            Result.success()
        } catch (e :HttpException){
            Result.retry()
        }

    }

     suspend fun addToDatabase(asteroidList: ArrayList<Asteroid>) {
         withContext(Dispatchers.IO) {
             getInstance(applicationContext).databaseDao.insertAll(asteroidList)
         }
     }
}