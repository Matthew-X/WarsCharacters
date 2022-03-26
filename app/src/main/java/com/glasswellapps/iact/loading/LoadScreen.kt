package com.glasswellapps.iact
import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.glasswellapps.iact.character_screen.CharacterScreen
import com.glasswellapps.iact.database.AppDatabase
import com.glasswellapps.iact.database.CharacterData
import com.glasswellapps.iact.effects.Sounds
import com.glasswellapps.iact.effects.WorkingAnimations
import com.glasswellapps.iact.loading.DeleteWorker
import com.glasswellapps.iact.loading.CharacterHolder
import com.glasswellapps.iact.loading.LoadingController
import com.glasswellapps.iact.loading.SaveSlotAdapter
import kotlinx.android.synthetic.main.activity_load_screen.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoadScreen : AppCompatActivity() {
    var height = 0f
    var slotsPerPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_screen)
        Sounds.reset(this)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels.toFloat()/displayMetrics.density

        slotsPerPage = ((height-130)/70).toInt()
        System.out.println("SLOTS "+slotsPerPage + " " + height)

        LoadingController.loadData(this)
        initSaveSlots()
        getCurrentSaveData()
        showNoSavesFoundToast()
    }

    var selectedFiles = arrayListOf<Int>()

    var currentSaveData = arrayListOf<CharacterData>()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter:SaveSlotAdapter
    fun initSaveSlots() {
        linearLayoutManager = LinearLayoutManager(this)
        saveSlots_recyclerView.layoutManager = linearLayoutManager
        adapter = SaveSlotAdapter(currentSaveData, this, this)
        saveSlots_recyclerView.adapter = adapter
        saveSlots_recyclerView.animate().alpha(1f)
    }

    var page = 0

    private fun getCurrentSaveData() {
        currentSaveData.clear()
        getMaxPages()
        if(page>=maxPages){
            page = maxPages-1;
        }

        pageNumber.text = ""+(page+1) +" / "+maxPages
        for(i in 0..slotsPerPage-1) {
            var dataIndex = page*slotsPerPage+i
            if(dataIndex < LoadingController.loadedData.size) {
                currentSaveData.add(LoadingController.loadedData[dataIndex])
            }
        }
        adapter.notifyDataSetChanged()
    }
    fun onPreviousPage(view: View) {
        Sounds.selectSound()
        saveSlots_recyclerView.animate().translationX(saveSlots_recyclerView.width.toFloat()*2)
            .duration=100

        if(page > 0){
            page--
        }
        else{
            page = maxPages-1
        }
        val handler = Handler()
        handler.postDelayed(Runnable {
            getCurrentSaveData()
            var slideInAnim = ObjectAnimator.ofFloat(saveSlots_recyclerView, "translationX",
                -saveSlots_recyclerView.width.toFloat()*2f,0f)
            slideInAnim.duration = 100
            slideInAnim.start()
        }, 100)
    }
    fun onNextPage(view: View) {
        Sounds.selectSound()
        saveSlots_recyclerView.animate().translationX(-saveSlots_recyclerView.width.toFloat()*2)
            .duration = 100
        if(page<maxPages-1){
            page++
        }
        else{
            page = 0;
        }
        val handler = Handler()
        handler.postDelayed(Runnable {
            getCurrentSaveData()
            var slideInAnim = ObjectAnimator.ofFloat(saveSlots_recyclerView, "translationX",
                saveSlots_recyclerView.width.toFloat()*2f,0f)
            slideInAnim.duration = 100
            slideInAnim.start()
        },100)
    }

    var maxPages = 1
    fun getMaxPages(){
        maxPages = LoadingController.loadedData.size/slotsPerPage
        if(LoadingController.loadedData.size%slotsPerPage!=0){
            maxPages++
        }
        if(maxPages <1){
            maxPages = 1;
        }
    }


    fun onSaveSelected(saveDataIndex: Int) {
        val saveData = currentSaveData[saveDataIndex];
        var selectedCharacter = LoadingController.loadCharacter(saveData, this)
        if(CharacterHolder.isInParty(selectedCharacter.name_short)) {
            Sounds.negativeSound();
            ShortToast.show(this, "ALREADY IN PARTY")
            return
        }
        Sounds.selectSound()
        for (i in 0..adapter.itemCount) {
            if (i != saveDataIndex) {
                var saveSlot = saveSlots_recyclerView.findViewHolderForAdapterPosition(i)

                if (saveSlot != null) {
                    saveSlot.itemView.visibility = View.INVISIBLE
                }
            }
        }

        CharacterHolder.setActiveCharacter(selectedCharacter)
        if (CharacterHolder.getActiveCharacter() != null) {
            var from: String = intent.getStringExtra("from").toString()

            when(from){
                "imperial"-> onBackPressed()
                "main"-> toCharacterScreen()
            }
        }
    }
    fun toCharacterScreen(){
        val intent = Intent(this, CharacterScreen::class.java)
        startActivity(intent)
        finish()

    }
    fun onFileNameEdited(editedFileName: String, position: Int) {
        Sounds.selectSound()
        LoadingController.loadedData[position + page * 5].fileName = editedFileName
        val database = AppDatabase.getInstance(this)
        database!!.getCharacterDAO().update(LoadingController.loadedData[position + page * 5])
        hideSoftKeyboard()
    }
    fun hideSoftKeyboard(){
        (getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

    fun startWorkingAnimation(){
        working_overlay.alpha=0f
        working_overlay.visibility = View.VISIBLE
        working_overlay.animate().alpha(1f)
        WorkingAnimations.startSpinningAnimation(working_icon)
    }
    fun stopWorkingAnimation(){
        WorkingAnimations.stopAnimation()
        working_overlay.animate().alpha(0f)
    }
    fun onDelete(view: View) {
        startWorkingAnimation()
        Sounds.selectSound()

        val deleteWorkRequestBuilder = OneTimeWorkRequest.Builder(DeleteWorker::class.java)
        val data = Data.Builder()

        data.putIntArray("selectedFiles", selectedFiles.toIntArray())
        deleteWorkRequestBuilder.setInputData(data.build())
        deleteWorkRequestBuilder.addTag("delete")

        val deleteWorkRequest = deleteWorkRequestBuilder.build()

        WorkManager.getInstance(this).enqueue(deleteWorkRequest)

        WorkManager.getInstance(this)
            .getWorkInfosByTagLiveData("delete")
            .observe(this, Observer<List<WorkInfo>> { workStatusList ->
                val currentWorkStatus = workStatusList?.getOrNull(0)
                if (currentWorkStatus?.state?.isFinished == true) {
                    WorkManager.getInstance(this)
                        .getWorkInfosByTagLiveData("delete").removeObservers(this)
                    println("DELETE FINISHED")
                    val handler = Handler()
                    handler.postDelayed(Runnable {
                        stopWorkingAnimation()
                        LoadingController.loadData(this)
                        selectedFiles.clear()
                        setDeleteButtonVisibility()
                        getCurrentSaveData()
                    }, 500)
                }
            })
    }


    private fun showNoSavesFoundToast() {
        if (LoadingController.loadedData.isEmpty()) {
            val noSavesFoundToast = Dialog(this)
            noSavesFoundToast.requestWindowFeature(Window.FEATURE_NO_TITLE)
            noSavesFoundToast.setCancelable(false)
            noSavesFoundToast.setContentView(R.layout.dialog_no_savefiles_found)
            noSavesFoundToast.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            noSavesFoundToast!!.show()
            MainScope().launch {
                delay(1000)
                noSavesFoundToast.cancel()
            }
        }
    }

    var isEditing = false
    fun onToggleEdit(view: View) {
        Sounds.selectSound()
        isEditing = !isEditing
        if(isEditing){
            edit_toggle.text = "CANCEL"
        }
        else{
            selectedFiles.clear()
            delete_button_holder.visibility=View.INVISIBLE
            edit_toggle.text = "EDIT"
        }

        updateToggleAll()
        adapter.notifyDataSetChanged()
    }


    fun onToggleAll(view: View) {
        Sounds.selectSound()
        if(selectedFiles.size <  LoadingController.loadedData.size){
            selectedFiles.clear()
            for(i in 0..  LoadingController.loadedData.size-1){
                selectedFiles.add(i)
            }
            all_toggle.text = "NONE"
        }
        else{
            selectedFiles.clear()
            all_toggle.text = "ALL"
        }
        setDeleteButtonVisibility()
        adapter.notifyDataSetChanged()
    }
    fun updateToggleAll(){
        if(isEditing){
            all_toggle.visibility = View.VISIBLE
        }
        else{
            all_toggle.visibility = View.GONE
        }
        if(selectedFiles.size <   LoadingController.loadedData.size){
            all_toggle.text = "ALL"
        }
        else{
            all_toggle.text = "NONE"
        }
    }

    fun setDeleteButtonVisibility(){
        val deleteButton = delete_button as TextView
        if(selectedFiles.size > 0) {
            delete_button_holder.visibility = View.VISIBLE
            var plural = "S"
            if(selectedFiles.size == 1){plural = ""}
            deleteButton.text = "DELETE "+ selectedFiles.size + " FILE" +plural
        }
        else{
            delete_button_holder.visibility = View.INVISIBLE
        }
    }
}






