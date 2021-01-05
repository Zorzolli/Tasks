package com.zorzolli.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zorzolli.tasks.service.listener.APIListener
import com.zorzolli.tasks.service.listener.ValidationListener
import com.zorzolli.tasks.service.model.PriorityModel
import com.zorzolli.tasks.service.model.TaskModel
import com.zorzolli.tasks.service.repository.PriorityRepository
import com.zorzolli.tasks.service.repository.TaskRepository

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val mPriorityRepository = PriorityRepository(application)
    private val mTaskRepository = TaskRepository(application)

    private val mPriorityList = MutableLiveData<List<PriorityModel>>()
    var priorities: LiveData<List<PriorityModel>> = mPriorityList

    private val mValidation = MutableLiveData<ValidationListener>()
    var validation: LiveData<ValidationListener> = mValidation

    private val mTask = MutableLiveData<TaskModel>()
    var task: LiveData<TaskModel> = mTask

    fun listPriorities() {
        mPriorityList.value = mPriorityRepository.list()
    }

    fun save(task: TaskModel) {

        if (task.id == 0) {
            mTaskRepository.create(task, object : APIListener<Boolean> {
                override fun onSucess(model: Boolean) {
                    mValidation.value = ValidationListener()
                }

                override fun onFailure(str: String) {
                    mValidation.value = ValidationListener(str)
                }
            })
        } else {
            mTaskRepository.update(task, object : APIListener<Boolean> {
                override fun onSucess(model: Boolean) {
                    mValidation.value = ValidationListener()
                }

                override fun onFailure(str: String) {
                    mValidation.value = ValidationListener(str)
                }
            })
        }
    }

    fun load(id: Int) {
        mTaskRepository.load(id, object : APIListener<TaskModel> {
            override fun onSucess(model: TaskModel) {
                mTask.value = model
            }

            override fun onFailure(str: String) {
            }

        })
    }

}