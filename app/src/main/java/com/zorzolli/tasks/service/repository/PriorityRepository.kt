package com.zorzolli.tasks.service.repository

import android.content.Context
import com.zorzolli.tasks.service.constants.TaskConstants
import com.zorzolli.tasks.service.listener.APIListener
import com.zorzolli.tasks.service.model.PriorityModel
import com.zorzolli.tasks.service.repository.local.TaskDatabase
import com.zorzolli.tasks.service.repository.remote.PriorityService
import com.zorzolli.tasks.service.repository.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(val context: Context) : BaseRepository(context) {

    private val mRemote = RetrofitClient.createService(PriorityService::class.java)
    private val mPriorityDatabase = TaskDatabase.getDatabase(context).priorityDao()

    fun all(param: APIListener<List<PriorityModel>>) {

        if (!isConnectionAvailable(context)) {
            return
        }

        val call: Call<List<PriorityModel>> = mRemote.list()
        call.enqueue(object : Callback<List<PriorityModel>>{
            override fun onFailure(call: Call<List<PriorityModel>>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<List<PriorityModel>>,
                response: Response<List<PriorityModel>>
            ) {
                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    mPriorityDatabase.clear()
                    response.body()?.let { mPriorityDatabase.save(it) }
                }
            }

        })
    }

    fun list() = mPriorityDatabase.list()

    fun save(list: List<PriorityModel>) {
        mPriorityDatabase.clear()
        mPriorityDatabase.save(list)
    }

    fun getDescription(id: Int) = mPriorityDatabase.getDescription(id)



}