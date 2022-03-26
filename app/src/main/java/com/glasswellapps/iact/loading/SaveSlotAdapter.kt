package com.glasswellapps.iact.loading

import android.annotation.SuppressLint
import android.app.Activity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.glasswellapps.iact.LoadScreen
import com.glasswellapps.iact.NameGetter
import com.glasswellapps.iact.PortraitLoader
import com.glasswellapps.iact.R
import com.glasswellapps.iact.database.CharacterData
import com.glasswellapps.iact.effects.Sounds

class SaveSlotAdapter(
    private val dataSet: List<CharacterData>, val context: Activity, val
    loadScreen: LoadScreen
) :
    RecyclerView
    .Adapter<SaveSlotAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fileName: TextView
        val characterName: TextView
        val level: TextView
        val portrait: ImageView
        val time: TextView
        val editData: View
        val loadData: View
        val deleteToggle: ImageView
        val editFileName : EditText
        init {
            fileName = view.findViewById(R.id.fileName)
            characterName = view.findViewById(R.id.characterName)
            level = view.findViewById(R.id.level)
            portrait = view.findViewById(R.id.portrait)
            time = view.findViewById(R.id.time)

            editData = view.findViewById(R.id.edit_data)
            loadData = view.findViewById(R.id.load_data)
            deleteToggle = view.findViewById(R.id.delete_toggle)
            editFileName = view.findViewById(R.id.edit_file_name)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.save_load_item,
            viewGroup, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val saveData = dataSet[position]
        viewHolder.fileName.text = saveData.fileName
        viewHolder.editFileName.setText(saveData.fileName)

        viewHolder.characterName.text = NameGetter.getFullName(saveData.characterName)

        var level = 5
        if (saveData.xpSpent <= 1) {
            level = 1
        } else if (saveData.xpSpent <= 4) {
            level = 2
        } else if (saveData.xpSpent <= 7) {
            level = 3
        } else if (saveData.xpSpent <= 10) {
            level = 4
        }
        viewHolder.level.text = "Lv " + level
        var portrait = PortraitLoader.getCharacterPortrait(saveData.characterName,context)
        if(portrait!=null) {
            viewHolder.portrait.setImageDrawable(portrait)
        }
        var timeSinceLastSave =  (System.currentTimeMillis()-saveData.date).toFloat()

        var days = (timeSinceLastSave/86400000).toInt()
        var hours = ((timeSinceLastSave - days*86400000)/3600000).toInt()
        var min = ((timeSinceLastSave - days*86400000 - hours*3600000)/60000).toInt()

        var timeAgo = ""
        if(days > 0){
            timeAgo = ""+days+" d ago"
        }
        else if(hours > 0){
            timeAgo = ""+hours+" h " +min +" m ago"
        }
        else {
            timeAgo = ""+min +" m ago"
        }
        viewHolder.time.text = timeAgo

        if(loadScreen.isEditing){
            viewHolder.editData.visibility = View.VISIBLE
            viewHolder.loadData.visibility = View.INVISIBLE
        }
        else{
            viewHolder.editData.visibility = View.INVISIBLE
            viewHolder.loadData.visibility = View.VISIBLE
        }
        setDeleteToggleVisibility(viewHolder.deleteToggle, position)
        viewHolder.deleteToggle.setOnClickListener {
            toggleDelete(it, position)
        }

        viewHolder.loadData.setOnClickListener{
            loadScreen.onSaveSelected(position)
            true
        }

        viewHolder.editFileName.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(view: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (view != null) {
                        loadScreen.onFileNameEdited("" + view.text, position)
                    }
                }
                return false
            }
        })
    }

    fun setDeleteToggleVisibility(view: View, position: Int){
        if(loadScreen.selectedFiles.contains(loadScreen.page * 5 + position)){
            view.alpha = 1f
        }
        else{
            view.alpha = 0.3f
        }
    }


    fun toggleDelete(it: View, position: Int){
        Sounds.selectSound()
        val saveDataIndex = loadScreen.page*5 + position

        if(loadScreen.selectedFiles.contains(saveDataIndex)){
            loadScreen.selectedFiles.remove(saveDataIndex)
        }
        else{
            loadScreen.selectedFiles.add(saveDataIndex)
        }

        setDeleteToggleVisibility(it, position)
        loadScreen.setDeleteButtonVisibility()

        loadScreen.updateToggleAll()
        true
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size






}