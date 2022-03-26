package com.glasswellapps.iact.effects

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.ContextCompat.getSystemService


class LightSaberMotionController (val activity: Activity):SensorEventListener{
    val lightSaberSwingSoundIDs = intArrayOf(
        Sounds.lightsaber_stabby_stabby,
        Sounds.lightsaber_fast,
        Sounds.lightsaber_swing);
    private val lightSaberSwingFrequencies = intArrayOf(1,0,1)
    private val lightSaberSwingSounds: SoundSelector =
        SoundSelector(
            lightSaberSwingSoundIDs,
            lightSaberSwingFrequencies
        )

    val lightSaberAttackSoundIDs = intArrayOf(
        Sounds.lightsaber_clash,
        Sounds.lightsaber_heavy_clash,
        Sounds.lightsaber_heavy_clash2);
    private val lightSaberAttackFrequencies = intArrayOf(2,1,2)
    private val lightSaberClashSounds: SoundSelector =
        SoundSelector(
            lightSaberAttackSoundIDs,
            lightSaberAttackFrequencies
        )
    var playWave = true;
    var playClash = true;

    override fun onSensorChanged(event: SensorEvent) {
        if(!isSensing){
            return
        }
        var accelerationSqMagnitude =
            event.values.get(0)*event.values.get(0) +
                    event.values.get(1)*event.values.get(1) +
                    event.values.get(2)*event.values.get(2)

        if(accelerationSqMagnitude> 140 && playWave) {
            lightSaberSwingSounds.playRandom()
            Sounds.play(Sounds.lightsaber_hum)
            playWave = false
            object : CountDownTimer(600, 100) {

                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    playWave = true
                }
            }.start()
        }
        if(accelerationSqMagnitude> 600 && playClash && !playWave) {


            val soundId = lightSaberClashSounds.playRandom()

            val v =activity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?

            var vibrateDuration = 30;
            if(soundId == Sounds.lightsaber_heavy_clash){
                vibrateDuration = 200;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v!!.vibrate(VibrationEffect.createOneShot(vibrateDuration.toLong(), VibrationEffect
                    .DEFAULT_AMPLITUDE))
            } else {
                v!!.vibrate(vibrateDuration.toLong())
            }
            Sounds.play(Sounds.lightsaber_hum)
            playClash = false
            object : CountDownTimer(300, 300) {

                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    playClash = true
                }
            }.start()
        }
        print("wave")
        // Remove the gravity contribution with the high-pass filter.
        //linear_acceleration.get(0) = event.values.get(0) - gravity.get(0)
        //linear_acceleration.get(1) = event.values.get(1) - gravity.get(1)
        //linear_acceleration.get(2) = event.values.get(2) - gravity.get(2)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    val sensorManager = activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    var isSensing = false
    fun startSound(){
        if (sensorManager.getSensorList(1).size !== 0 && !isSensing) {
            isSensing = true
            sensorManager
                .registerListener(
                    this,
                    sensorManager.getSensorList(1).get(0) as Sensor,
                    SensorManager.SENSOR_DELAY_GAME
                )
        }
    }
    fun stopSound(){
        if(!isSensing)return
        sensorManager.unregisterListener(this)
        isSensing = false
    }
}