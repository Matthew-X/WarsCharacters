package com.glasswellapps.iact

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.InputStream

class BitmapLoader {
    companion object{
        open fun getBitmap(context: Context, path: String): Bitmap? {
            val assetManager = context.assets
            var inputStream: InputStream? = null
            var bitmap: Bitmap? = null
            val options = BitmapFactory.Options()
            for (i in 1..32) {
                try {
                    inputStream = assetManager.open(path)
                    options.inSampleSize = i

                    bitmap = BitmapFactory.decodeStream(inputStream, null, options)
                    break
                } catch (outOfMemoryError: OutOfMemoryError) {

                    //println("next size" + i)
                } catch (e: Exception) {
                    //e.printStackTrace()
                    break
                }
            }
            try {
                inputStream?.close()
            } catch (e: Exception) {
                //e.printStackTrace()
            }
            return bitmap
        }
    }
}