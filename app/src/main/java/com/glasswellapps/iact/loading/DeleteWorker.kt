package com.glasswellapps.iact.loading

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.glasswellapps.iact.database.AppDatabase

class DeleteWorker(val context: Context, params: WorkerParameters) : Worker
    (context, params) {
    override fun doWork(): Result {
        var selectedFiles = inputData.getIntArray("selectedFiles")
        if (selectedFiles != null) {
            for(i in 0..selectedFiles.size-1) {
                val database = AppDatabase.getInstance(context)
                LoadingController.loadedData[selectedFiles[i]].deleted = true
                //database!!.getCharacterDAO().update(MainActivity.data!![positionEditing])
                database!!.getCharacterDAO().deleteById(LoadingController.loadedData[selectedFiles[i]].id)
            }
        }
        return Result.success()
    }

}