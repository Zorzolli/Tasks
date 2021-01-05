package com.zorzolli.tasks.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zorzolli.tasks.R
import com.zorzolli.tasks.service.listener.TaskListener
import com.zorzolli.tasks.service.model.TaskModel
import com.zorzolli.tasks.view.viewholder.TaskViewHolder

class TaskAdapter : RecyclerView.Adapter<TaskViewHolder>() {

    private var mList: List<TaskModel> = arrayListOf()
    private lateinit var mListener: TaskListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val item =
            LayoutInflater.from(parent.context).inflate(R.layout.row_task_list, parent, false)
        return TaskViewHolder(item, mListener)
    }

    override fun getItemCount(): Int {
        return mList.count()
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bindData(mList[position])
    }

    fun attachListener(listener: TaskListener) {
        mListener = listener
    }

    fun updateList(list: List<TaskModel>) {
        mList = list
        notifyDataSetChanged()
    }

}