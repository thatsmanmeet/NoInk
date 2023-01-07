package com.thatsmanmeet.myapplication.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.thatsmanmeet.myapplication.R
import com.thatsmanmeet.myapplication.databinding.FragmentTrashBinding
import com.thatsmanmeet.myapplication.adapter.ITrashRVAdapter
import com.thatsmanmeet.myapplication.adapter.TrashAdapter
import com.thatsmanmeet.myapplication.helpers.MusicHelper
import com.thatsmanmeet.myapplication.room.note.Note
import com.thatsmanmeet.myapplication.room.note.NoteViewModel
import com.thatsmanmeet.myapplication.room.trash.Trash
import com.thatsmanmeet.myapplication.room.trash.TrashViewModel


class TrashFragment : Fragment(), ITrashRVAdapter {

    private lateinit var binding: FragmentTrashBinding
    private lateinit var viewModel: TrashViewModel
    private lateinit var notesViewModel: NoteViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrashBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)
        // Setting up the recyclerview
        binding.rvShowTrash.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        val adapter = TrashAdapter(requireContext(), this)
        binding.rvShowTrash.adapter = adapter

        // Initializing view model and observe the livedata
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[TrashViewModel::class.java]
        viewModel.allTrashNotes.observe(requireActivity()) { list ->
            list?.let {
                adapter.updateList(it)
            }
            if (adapter.allTrashNotes.isEmpty()) {
                binding.tvNoTrash.visibility = View.VISIBLE
            } else {
                binding.tvNoTrash.visibility = View.INVISIBLE
            }
        }

        notesViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[NoteViewModel::class.java]

        return binding.root
    }
    // function to either restore or delete a note stored in the trash when the card is clicked.
    override fun onNoteCardClicked(trash: Trash) {
        MaterialAlertDialogBuilder(requireActivity())
            .setIcon(R.drawable.ic_delete)
            .setTitle("Delete/Restore")
            .setMessage("Choose an option to either delete or restore this note")
            .setPositiveButton("Restore"){_,_->
                notesViewModel.insertNote(
                    Note(
                        id = trash.id,
                        title = trash.title,
                        description = trash.description,
                        date = trash.date,
                        backgroundColor = trash.backgroundColor
                    )
                )
                viewModel.deleteTrash(trash)
            }.setNegativeButton("Delete"){_,_->
                viewModel.deleteTrash(trash)
                MusicHelper(requireActivity().applicationContext).deleteSound()
            }.show()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        when (requireContext().resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.tvNoTrashText.setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.white, null
                    )
                )
            }

            Configuration.UI_MODE_NIGHT_NO -> {
                binding.tvNoTrashText.setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.black, null
                    )
                )
            }
        }
        super.onConfigurationChanged(newConfig)
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.miEmptyTrash)!!.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        MenuInflater(requireActivity().applicationContext).inflate(R.menu.trash_menu_bar,menu)
    }

    // function to delete all notes. Clicks on the delete icon in menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.miEmptyTrash -> {
                MaterialAlertDialogBuilder(requireActivity())
                    .setIcon(R.drawable.ic_delete_forever)
                    .setTitle("Empty the trash ?")
                    .setMessage("Do you want to delete all notes stored in the trash permanently ?")
                    .setPositiveButton("Yes, Delete Everything"){_,_->
                        viewModel.allTrashNotes.observe(this@TrashFragment){
                            if(it.isNotEmpty()){
                                viewModel.clearTrash()
                                MusicHelper(requireActivity().applicationContext).deleteSound()
                            }
                        }
                    }.setNegativeButton("No"){_,_->

                    }.show()
            }
        }
        return true
    }

}