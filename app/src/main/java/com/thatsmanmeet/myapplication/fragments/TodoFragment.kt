package com.thatsmanmeet.myapplication.fragments


import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.thatsmanmeet.myapplication.R
import com.thatsmanmeet.myapplication.databinding.FragmentTodoBinding
import com.thatsmanmeet.myapplication.adapter.TodoAdapter
import com.thatsmanmeet.myapplication.adapter.TodoInterface
import com.thatsmanmeet.myapplication.room.todo.Todo
import com.thatsmanmeet.myapplication.room.todo.TodoViewModel


class TodoFragment : Fragment(), TodoInterface {

    private lateinit var binding : FragmentTodoBinding
    private lateinit var viewModel : TodoViewModel
    private lateinit var adapter: TodoAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoBinding.inflate(inflater,container,false)
        setHasOptionsMenu(true)

        // setup the recyclerview
        binding.rvTodos.layoutManager = LinearLayoutManager(requireContext())
        adapter = TodoAdapter(requireContext(), this)
        binding.rvTodos.adapter = adapter

        // setup the view model
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[TodoViewModel::class.java]
        viewModel.allTodos.observe(viewLifecycleOwner) { list ->
            adapter.updateList(list)

            if (adapter.allTodo.isEmpty()) {
                binding.tvNoTodos.visibility = View.VISIBLE
            } else {
                binding.tvNoTodos.visibility = View.INVISIBLE
            }

        }
        // add a to-do when clicked on fab button
        binding.fabAddTodo.setOnClickListener {
            addTodo()
        }

        return binding.root
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_delete)!!.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        MenuInflater(requireActivity().applicationContext).inflate(R.menu.menu_bar,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                var isDeleted = false
                MaterialAlertDialogBuilder(requireActivity())
                    .setIcon(R.drawable.ic_delete)
                    .setTitle("Delete")
                    .setMessage("Do you want to delete all completed todos ?")
                    .setPositiveButton("Yes"){_,_->
                        for(todo in adapter.allTodo){
                            if(todo.isChecked){
                                viewModel.deleteTodo(todo)
                                isDeleted = true
                            }
                    }
                        if(isDeleted){
                            deleteSound()
                        }
                    }.setNegativeButton("No"){_,_->

                    }.show()
            }
        }
        return true
    }


    private fun addTodo() {
        val todoText = TextInputEditText(requireActivity())
        todoText.apply {
            this.setPadding(30)
            this.hint = "What's your task ?"
        }
        MaterialAlertDialogBuilder(requireActivity())
            .setIcon(R.drawable.ic_add_blue)
            .setTitle("Add Todo")
            .setView(todoText)
            .setPositiveButton("Add"){_,_->
                if (todoText.text.toString().isNotEmpty() || todoText.text.toString()
                        .isNotBlank()
                ) {
                    val todo = Todo(null, todoText.text.toString(), false)
                    viewModel.insertTodo(todo)
                } else {
                    Snackbar.make(binding.tvNoTodos,"Todo cannot be empty!",Snackbar.LENGTH_SHORT).apply {
                        this.anchorView = binding.hiddenPixel
                        this.show()
                    }
                }
            }
            .setNegativeButton("Cancel"){_,_->

            }
            .show()
    }

    override fun onTodoClicked(todo: Todo) {
        val id = todo.ID
        val text = todo.todoText
        val status = todo.isChecked
        val todoTextField = TextInputEditText(requireActivity()).apply {
            this.setPadding(30)
            this.hint = "Task Name"
            this.setText(text)
        }
        MaterialAlertDialogBuilder(requireActivity())
            .setIcon(R.drawable.ic_edit)
            .setTitle("Edit Todo")
            .setView(todoTextField)
            .setPositiveButton("Update"){_,_->
                if(todoTextField.text.toString().isNotEmpty() || todoTextField.text.toString().isNotBlank()){
                    viewModel.updateTodo(Todo(id,todoTextField.text.toString(),status))
                }
            }.setNegativeButton("Delete"){_,_->
                viewModel.deleteTodo(Todo(id,text,status))
            }
            .show()
    }

    private fun deleteSound() {
        val mp = MediaPlayer.create(requireContext(), R.raw.delete)
        mp.start()
        mp.setOnCompletionListener{
            it.stop()
            it.reset()
            it.release()
        }
    }

    override fun onCheckBoxClicked(todo: Todo) {
        val id = todo.ID
        val text = todo.todoText
        val status = todo.isChecked
        viewModel.updateTodo(Todo(id, text, !status))
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
       when(requireContext().resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)){
           Configuration.UI_MODE_NIGHT_YES -> {
               binding.tvNoTodosText.setTextColor(ResourcesCompat.getColor(resources,R.color.white,null))
               setTodoColor(R.color.white)
           }

           Configuration.UI_MODE_NIGHT_NO -> {
               binding.tvNoTodosText.setTextColor(ResourcesCompat.getColor(resources,R.color.black,null))
               setTodoColor(R.color.black)
           }
        }
        super.onConfigurationChanged(newConfig)
    }

    private fun setTodoColor(color:Int) {
        val x = binding.rvTodos.childCount
        var i = 0
        while (i < x) {
            binding.rvTodos.getChildAt(i).findViewById<TextView>(R.id.tvTodoText)
                .setTextColor(ResourcesCompat.getColor(resources, color, null))
            i++
        }
    }

}

