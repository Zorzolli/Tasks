package com.zorzolli.tasks.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zorzolli.tasks.R
import com.zorzolli.tasks.service.constants.TaskConstants
import com.zorzolli.tasks.service.listener.TaskListener
import com.zorzolli.tasks.view.adapter.TaskAdapter
import com.zorzolli.tasks.viewmodel.AllTasksViewModel

class AllTasksFragment : Fragment() {

    private lateinit var mViewModel: AllTasksViewModel
    private lateinit var mListener: TaskListener
    private val mAdapter = TaskAdapter()
    private var mTaskFilter = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        mViewModel = ViewModelProvider(this).get(AllTasksViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_all_tasks, container, false)

        mTaskFilter = requireArguments().getInt(TaskConstants.BUNDLE.TASKFILTER, 0)

        val recycler = root.findViewById<RecyclerView>(R.id.recycler_all_tasks)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = mAdapter

        // Eventos disparados ao clicar nas linhas da RecyclerView
        mListener = object : TaskListener {
            override fun onListClick(id: Int) {
                val intent = Intent(context, TaskFormActivity::class.java)
                val bundle = Bundle()
                bundle.putInt(TaskConstants.BUNDLE.TASKID, id)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            override fun onDeleteClick(id: Int) {
                mViewModel.delete(id)
            }

            override fun onCompleteClick(id: Int) {
                mViewModel.complete(id)
            }

            override fun onUndoClick(id: Int) {
                mViewModel.undo(id)
            }
        }

        // Cria os observadores
        observe()

        // Retorna view
        return root
    }

    override fun onResume() {
        super.onResume()
        mAdapter.attachListener(mListener)
        mViewModel.list(mTaskFilter)
    }

    private fun observe() {
        mViewModel.tasks.observe(viewLifecycleOwner, Observer {
            if (it.count() > 0) {
                mAdapter.updateList(it)
            }
        })

        mViewModel.validation.observe(viewLifecycleOwner, Observer {
            if (it.success()) {
                Toast.makeText(context, getString(R.string.task_removed), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.failure(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}
