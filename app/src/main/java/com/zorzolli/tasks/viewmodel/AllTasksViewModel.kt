package com.zorzolli.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zorzolli.tasks.service.constants.TaskConstants
import com.zorzolli.tasks.service.listener.APIListener
import com.zorzolli.tasks.service.listener.ValidationListener
import com.zorzolli.tasks.service.model.PriorityModel
import com.zorzolli.tasks.service.model.TaskModel
import com.zorzolli.tasks.service.repository.TaskRepository

class AllTasksViewModel(application: Application) : AndroidViewModel(application) {

    private val mTaskRepository = TaskRepository(application)
    private var mTaskFilter = 0

    private val mValidation = MutableLiveData<ValidationListener>()
    var validation: LiveData<ValidationListener> = mValidation

    private val mList = MutableLiveData<List<TaskModel>>()
    var tasks: LiveData<List<TaskModel>> = mList

    fun list(taskFilter: Int) {
        mTaskFilter = taskFilter

        val listener = object : APIListener<List<TaskModel>> {
            override fun onSucess(model: List<TaskModel>) {
                mList.value = model
            }

            override fun onFailure(str: String) {
                mList.value = arrayListOf()
                mValidation.value = ValidationListener(str)
            }
        }

        if (mTaskFilter == TaskConstants.FILTER.ALL) {
            mTaskRepository.all(listener)
        } else if (mTaskFilter == TaskConstants.FILTER.NEXT) {
            mTaskRepository.nextWeek(listener)
        } else {
            mTaskRepository.overdue(listener)
        }
    }

    fun delete(id: Int) {
        mTaskRepository.delete(id, object : APIListener<Boolean> {
            override fun onSucess(model: Boolean) {
                list(mTaskFilter)
                mValidation.value = ValidationListener()
            }

            override fun onFailure(str: String) {
                mValidation.value = ValidationListener(str)
            }
        })
    }

    fun complete(id: Int) {
        updateStatus(id, true)
    }

    fun undo(id: Int) {
        updateStatus(id, false)
    }

    private fun updateStatus(id: Int, complete: Boolean) {
        mTaskRepository.updateStatus(id, complete, object : APIListener<Boolean> {
            override fun onSucess(model: Boolean) {
                list(mTaskFilter)
            }

            override fun onFailure(str: String) {
            }
        })
    }


}