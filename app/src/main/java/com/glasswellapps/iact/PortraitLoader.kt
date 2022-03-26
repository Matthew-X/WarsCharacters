package com.glasswellapps.iact

import android.app.Activity
import android.graphics.drawable.Drawable

class PortraitLoader {
    companion object {
        fun getCharacterPortrait(characterName: String?, context: Activity): Drawable? {
            when (characterName) {
                "biv" -> {
                    return context.resources.getDrawable(R.drawable.portrait_biv)
                }
                "davith" -> {
                    return context.resources.getDrawable(R.drawable.portrait_davith)
                }
                "diala" -> {
                    return context.resources.getDrawable(R.drawable.portrait_diala)
                }
                "drokkatta" -> {
                    return context.resources.getDrawable(R.drawable.portrait_drokkatta)
                }
                "fenn" -> {
                    return context.resources.getDrawable(R.drawable.portrait_fenn)
                }
                "gaarkhan" -> {
                    return context.resources.getDrawable(R.drawable.portrait_gaarkhan)
                }
                "gideon" -> {
                    return context.resources.getDrawable(R.drawable.portrait_gideon)
                }
                "jarrod" -> {
                    return context.resources.getDrawable(R.drawable.portrait_jarrod)
                }
                "jyn" -> {
                    return context.resources.getDrawable(R.drawable.portrait_jyn)
                }
                "loku" -> {
                    return context.resources.getDrawable(R.drawable.portrait_loku)
                }
                "kotun" -> {
                    return context.resources.getDrawable(R.drawable.portrait_kotun)
                }
                "mak" -> {
                    return context.resources.getDrawable(R.drawable.portrait_mak)
                }
                "mhd19" -> {
                    return context.resources.getDrawable(R.drawable.portrait_mhd)
                }
                "murne" -> {
                    return context.resources.getDrawable(R.drawable.portrait_murne)
                }
                "onar" -> {
                    return context.resources.getDrawable(R.drawable.portrait_onar)
                }
                "saska" -> {
                    return context.resources.getDrawable(R.drawable.portrait_saska)
                }
                "shyla" -> {
                    return context.resources.getDrawable(R.drawable.portrait_shyla)
                }
                "verena" -> {
                    return context.resources.getDrawable(R.drawable.portrait_verena)
                }
                "vinto" -> {
                    return context.resources.getDrawable(R.drawable.portrait_vinto)
                }
                "ct1701" -> {
                    return context.resources.getDrawable(R.drawable.portrait_ct)
                }
                "tress" -> {
                    return context.resources.getDrawable(R.drawable.portrait_tress)
                }
            }
            return null
        }
    }
}