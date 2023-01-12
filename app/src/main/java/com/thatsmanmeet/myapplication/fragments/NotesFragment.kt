package com.thatsmanmeet.myapplication.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.thatsmanmeet.myapplication.activities.AddEditActivity
import com.thatsmanmeet.myapplication.R
import com.thatsmanmeet.myapplication.adapter.INotesRVAdapter
import com.thatsmanmeet.myapplication.adapter.NotesAdapter
import com.thatsmanmeet.myapplication.databinding.FragmentNotesBinding
import com.thatsmanmeet.myapplication.room.note.Note
import com.thatsmanmeet.myapplication.room.note.NoteViewModel
import com.thatsmanmeet.myapplication.room.trash.Trash
import com.thatsmanmeet.myapplication.room.trash.TrashViewModel

class NotesFragment : Fragment(), INotesRVAdapter {

    private var _binding: FragmentNotesBinding? = null
    private lateinit var viewModel : NoteViewModel
    private lateinit var trashViewModel: TrashViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater,container,false)
        setHasOptionsMenu(true) // this will allow the fragment to have the menu in top bar

        // setup shared preferences
        sharedPreferences = requireActivity().getSharedPreferences("NoteSharedPref",Context.MODE_PRIVATE)

        // setting up the recyclerview
        when (sharedPreferences.getString("Layout","")) {
            "Linear" -> {
                binding.rvShowNotes.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
            }
            "Grid" -> {
                binding.rvShowNotes.layoutManager = GridLayoutManager(requireActivity().applicationContext,2,GridLayoutManager.VERTICAL,false)
            }
            else -> {
                binding.rvShowNotes.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
            }
        }
        val adapter = NotesAdapter(requireContext(), this,R.layout.notes_item_new)
        binding.rvShowNotes.adapter = adapter

        // Initializing view model and observe the livedata
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[NoteViewModel::class.java]
        viewModel.allNotes.observe(requireActivity()) { list ->
            list?.let {
                adapter.updateList(it)
            }
            if (adapter.allNotes.isEmpty()) {
                binding.tvNoNotes.visibility = View.VISIBLE
            } else {
                binding.tvNoNotes.visibility = View.INVISIBLE
            }
        }

        // Initializing the trash view model
        trashViewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[TrashViewModel::class.java]

        // Add a new note on clicking the FAB button - currently launches a new activity.
        binding.fabAddNote.setOnClickListener {
            Intent(requireContext(), AddEditActivity::class.java).also {
                startActivity(it)
            }
        }

        return binding.root
    }
    // function to show alert dialog when you click on the delete button on a certain note.
    override fun onNoteDeleteButtonClicked(note: Note) {
        MaterialAlertDialogBuilder(requireActivity())
            .setIcon(R.drawable.ic_delete_button)
            .setTitle("Delete Note")
            .setMessage("Do you want to delete this note ?")
            .setPositiveButton("Yes"){_,_->
                trashViewModel.insertTrash(
                    Trash(
                        id = note.id,
                        title = note.title,
                        description = note.description,
                        date = note.date
                    )
                )
                viewModel.deleteNote(note)
                deleteSound()
            }
            .setNegativeButton("No"){_,_->
            }.show()
    }

    override fun onCardClicked(note: Note) {
        Intent(requireContext(), AddEditActivity::class.java).also {
            it.putExtra("id", note.id)
            it.putExtra("title", note.title)
            it.putExtra("desc", note.description)
            it.putExtra("date",note.date)
            it.putExtra("note_bg_color",note.backgroundColor)
            startActivity(it)
        }
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

    override fun onConfigurationChanged(newConfig: Configuration) {
        when(requireContext().resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)){
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.tvNoNotesText.setTextColor(ResourcesCompat.getColor(resources,R.color.white,null))
            }

            Configuration.UI_MODE_NIGHT_NO -> {
                binding.tvNoNotesText.setTextColor(ResourcesCompat.getColor(resources,R.color.black,null))
            }
        }
        super.onConfigurationChanged(newConfig)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.miView)!!.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        when (sharedPreferences.getString("Layout","")) {
            "Grid" -> {
                menu.findItem(R.id.miView).setIcon(R.drawable.ic_horizontal)
            }
            "Linear" -> {
                menu.findItem(R.id.miView).setIcon(R.drawable.ic_vertical)
            }
            else -> {
                menu.findItem(R.id.miView).setIcon(R.drawable.ic_vertical)
            }
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        MenuInflater(requireActivity().applicationContext).inflate(R.menu.layout_change,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.miView -> {
               val layoutManager = binding.rvShowNotes.layoutManager
                if(layoutManager is GridLayoutManager){
                    item.setIcon(R.drawable.ic_vertical)
                    binding.rvShowNotes.layoutManager = LinearLayoutManager(requireContext())
                    with(sharedPreferences.edit()){
                       this.putString("Layout","Linear")
                       apply()
                    }
                }else if(layoutManager is LinearLayoutManager){
                    item.setIcon(R.drawable.ic_horizontal)
                    binding.rvShowNotes.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
                    with(sharedPreferences.edit()){
                        this.putString("Layout","Grid")
                        apply()
                    }
                }
            }
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.allNotes.removeObservers(requireActivity())
        trashViewModel.allTrashNotes.removeObservers(requireActivity())
    }
}