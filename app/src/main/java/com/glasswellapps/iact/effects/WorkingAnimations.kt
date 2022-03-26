package com.glasswellapps.iact.effects
import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Handler
import android.view.View
import android.view.animation.Animation

class WorkingAnimations {
    companion object {
        lateinit var  working_icon:View
        lateinit var animator: ObjectAnimator
        fun startSpinningAnimation(working_icon:View) {
            working_icon.animate().setDuration(200).alpha(1f)
            this.working_icon = working_icon
            animator = ObjectAnimator.ofFloat(
                working_icon, "scaleX", 0f, 0.8f, 1f, 1f, 1f, 0.8f,
                0f
            )
            animator.repeatCount =  Animation.INFINITE
            animator.duration = 500
            animator.start()
        }
        fun startGearAnimation(working_icon: View){
            working_icon.animate().setDuration(200).alpha(1f)
            this.working_icon = working_icon
            animator = ObjectAnimator.ofFloat(working_icon,"rotation",
                0f,
                60f,60f,60f,
                120f,120f,120f,
                180f,180f,180f,
                270f,270f,270f,
                360f, 360f, 360f)
            animator.repeatCount = Animation.INFINITE
            animator.duration = 3000
            animator.start()
        }

        fun stopAnimation(){
            if(animator == null || working_icon == null){
                return
            }
            val handler = Handler()
            handler.postDelayed(Runnable {
                working_icon.animate().setDuration(1000).alpha(0f)
                handler.postDelayed(Runnable {
                    animator.cancel()
                }, 1000)

            }, 100)
        }
    }
}