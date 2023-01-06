package com.thatsmanmeet.myapplication.activities
import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.thatsmanmeet.myapplication.R
import com.thatsmanmeet.myapplication.databinding.ActivityAddEditBinding
import com.thatsmanmeet.myapplication.helpers.DateHelper
import com.thatsmanmeet.myapplication.room.note.Note
import com.thatsmanmeet.myapplication.room.note.NoteViewModel
import com.thatsmanmeet.myapplication.room.trash.Trash
import com.thatsmanmeet.myapplication.room.trash.TrashViewModel

class AddEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditBinding
    private lateinit var viewModel: NoteViewModel
    private lateinit var trashViewModel: TrashViewModel
    private var date: String? = ""
    private var flag = 0
    private var noteID : Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[NoteViewModel::class.java]

        trashViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[TrashViewModel::class.java]

        try {
            val title = intent.getStringExtra("title")
            val desc = intent.getStringExtra("desc")
            val id = intent.getLongExtra("id", 0)
            date = intent.getStringExtra("date")
            if (title!!.isNotEmpty() && desc!!.isNotEmpty()) {
                binding.etTitle.setText(title)
                binding.etDescription.setText(desc)
                flag = 1
                noteID = id
            }
        } catch (e: Exception) {
            // If intent doesn't pass anything then it will stay empty
        }
        setActionBar()

        binding.fabSave.setOnClickListener {
            submitData()
            finish()
        }

    }

    private fun setActionBar() {
        if (flag == 0) {
            supportActionBar!!.title =
                Html.fromHtml("<font color=\"#FFFFFF\">" + "Add Note" + "</font>", 0)
        } else {
            supportActionBar!!.title =
                Html.fromHtml("<font color=\"#FFFFFF\">" + "Edit Note" + "</font>", 0)
        }

        if (isDarkTheme()) {
            supportActionBar!!.setBackgroundDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.color.grey,
                    null
                )
            )
        } else {
            supportActionBar!!.setBackgroundDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.color.blue,
                    null
                )
            )
        }
    }

    private fun isDarkTheme(): Boolean {
        return when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> false
        }
    }


    private fun submitData() {
        if (flag != 1) {
            // adding a note
            val noteText = binding.etDescription.text.toString()
            val titleText = binding.etTitle.text.toString()
            if (noteText.isNotEmpty() && titleText.isNotEmpty()) {
                viewModel.insertNote(Note(null, titleText, noteText, DateHelper().getCurrentDate()))
                binding.etTitle.text?.clear()
                binding.etDescription.text!!.clear()
            } else if (noteText.isEmpty() && titleText.isEmpty()) {
                finish()
            } else if (noteText.isEmpty() || titleText.isEmpty()) {
                if (noteText.isEmpty()) {
                    viewModel.insertNote(
                        Note(
                            null,
                            titleText,
                            "empty note",
                            DateHelper().getCurrentDate()
                        )
                    )
                    binding.etTitle.text?.clear()
                    binding.etDescription.text!!.clear()
                } else {
                    viewModel.insertNote(
                        Note(
                            null,
                            "Untitled",
                            noteText,
                            DateHelper().getCurrentDate()
                        )
                    )
                    binding.etTitle.text?.clear()
                    binding.etDescription.text!!.clear()
                }
            }
        } else {
            // updating a note
            if (binding.etTitle.text!!.isNotEmpty() && binding.etDescription.text!!.isNotEmpty()) {
                viewModel.updateNote(
                    Note(
                        noteID,
                        binding.etTitle.text.toString(),
                        binding.etDescription.text.toString(),
                        date = date
                    )
                )
            }
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        if (isDarkTheme()) {
            window.statusBarColor = getColor(R.color.grey)
            window.navigationBarColor = getColor(R.color.black)
            supportActionBar!!.setBackgroundDrawable(ResourcesCompat.getDrawable(resources,
                R.color.grey,null))
            binding.parent.setBackgroundColor(ResourcesCompat.getColor(resources,
                R.color.black, null))
            binding.etTitle.setTextColor(ResourcesCompat.getColor(resources, R.color.white, null))
            binding.etDescription.setTextColor(ResourcesCompat.getColorStateList(resources,
                R.color.white, null))
            binding.etTitle.backgroundTintList = ResourcesCompat.getColorStateList(resources,
                R.color.blue,null)
        } else {
            window.statusBarColor = getColor(R.color.blue)
            window.navigationBarColor = getColor(R.color.white)
            supportActionBar!!.setBackgroundDrawable(ResourcesCompat.getDrawable(resources,
                R.color.blue, null))
            binding.parent.setBackgroundColor(ResourcesCompat.getColor(resources,
                R.color.white, null))
            binding.etTitle.setTextColor(ResourcesCompat.getColor(resources, R.color.black, null))
            binding.etDescription.setTextColor(ResourcesCompat.getColor(resources,
                R.color.black, null))
            binding.etTitle.backgroundTintList = ResourcesCompat.getColorStateList(resources,
                R.color.blue,null)
        }
        super.onConfigurationChanged(newConfig)
    }


    override fun onPause() {
        submitData()
        super.onPause()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_delete -> {

               MaterialAlertDialogBuilder(this)
                   .setIcon(R.drawable.ic_delete)
                   .setTitle("Delete Note")
                   .setMessage("Do you want to delete this note ?")
                   .setPositiveButton("Delete"){_,_->
                       val currentNoteId = intent.getLongExtra("id",0)
                       val currentNoteTitle = intent.getStringExtra("title")
                       val currentNoteDescription = intent.getStringExtra("desc")
                       val currentNoteDate = intent.getStringExtra("date")
                       var deleteFlag = 0
                       if(!currentNoteTitle.isNullOrEmpty() && !currentNoteDescription.isNullOrEmpty()){
                           trashViewModel.insertTrash(
                               Trash(
                                   id = noteID,
                                   title = currentNoteTitle,
                                   description = currentNoteDescription,
                                   date = currentNoteDate
                               )
                           )
                           deleteFlag = 1
                       }
                       viewModel.deleteNote(Note(
                           id = currentNoteId,
                           title = currentNoteTitle,
                           description = currentNoteDescription,
                           date =  currentNoteDate
                       ))
                       if(deleteFlag == 1){
                           deleteSound()
                       }
                       finish()
                   }
                   .setNegativeButton("Cancel"){_,_->

                   }.show()
            }
        }
        return true
    }

    private fun deleteSound() {
        val mp = MediaPlayer.create(this, R.raw.delete)
        mp.start()
        mp.setOnCompletionListener{
            it.stop()
            it.reset()
            it.release()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu!!.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.menu_bar,menu)
        return true
    }

}