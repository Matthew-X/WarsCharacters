package com.glasswellapps.iact.character_screen

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.BaseAdapter
import android.widget.ImageView
import com.glasswellapps.iact.R
import com.glasswellapps.iact.inventory.Items
import kotlinx.android.synthetic.main.dialog_show_card.*
import kotlinx.android.synthetic.main.grid_item.view.*

class ModListAdapter constructor(
    private val mContext: Activity, private var modIndices:
    ArrayList<Int>
) :
    BaseAdapter() {

    var items = arrayListOf<View>()
    private var showCardDialog: Dialog? = null
    init {
        showCardDialog = Dialog(mContext)
        showCardDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)

        showCardDialog!!.setContentView(R.layout.dialog_show_card)
        showCardDialog!!.setCancelable(true)
        showCardDialog!!.setCanceledOnTouchOutside(true)
        showCardDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        showCardDialog!!.show_card_dialog.setOnClickListener {
            showCardDialog!!.dismiss()
            true
        }

        for (i in 0 until modIndices.size) {
            val item = mContext.layoutInflater.inflate(R.layout.grid_item, null, true)
            val modIndex = modIndices[i]
            item.grid_image.setImageResource(Items.itemsArray!![modIndex].resourceId)

            item.setOnClickListener {
                onShowCard(item.grid_image)
            }
            items.add(item)
        }
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    fun onShowCard(view: ImageView) {
        val image = ((view).drawable as BitmapDrawable).bitmap
        println(image)
        showCardDialog!!.card_image.setImageBitmap(image)
        showCardDialog!!.show()
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // Create a new ImageView for each item referenced by the Adapter
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return items[position]
    }
}
