package br.com.sensor2

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity(), SensorEventListener {
    private var tvGravity: ArrayList<TextView> = ArrayList()

    private lateinit var btnStart: Button
    private lateinit var btnStop: Button

    private lateinit var sensorManager: SensorManager
    private lateinit var sensorGravity: Sensor

    private var gravityData: SensorData? = null

    private var idGravity: ArrayList<Int> = arrayListOf(R.id.tv_a_x, R.id.tv_a_y, R.id.tv_a_z)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initiView()
        initSensor()
    }

    private fun initiView(){
        for (i in idGravity){
            tvGravity.add(findViewById(i))
        }

        btnStart= findViewById(R.id.button_start)
        btnStop= findViewById(R.id.button_stop)

        btnStart.setOnClickListener{
            registerListener()
            btnStart.isEnabled = false
            btnStop.isEnabled = true
        }

        btnStop.setOnClickListener {
            unregisterListener()
        }
    }

    private fun initSensor(){
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        if(sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null){
            sensorGravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        }
    }

    private fun registerListener(){

        if(sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null){
            sensorManager.registerListener(this,sensorGravity,SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    private fun unregisterListener(){
        sensorManager.unregisterListener(this,sensorGravity)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event!!.sensor.type == Sensor.TYPE_GRAVITY){
            getGravityData(event)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("Not yet implemented")
    }

    private fun getGravityData(e:SensorEvent?){
        if(gravityData == null){
            gravityData = SensorData(e!!.values[0],  e!!.values[1],e!!.values[2], e!!.timestamp)

        } else{
            gravityData!!.x1 =  e!!.values[0]
            gravityData!!.x2 =  e!!.values[1]
            gravityData!!.x3 =  e!!.values[2]
        }
        tvGravity[0].text= "X1: ${"%.2f".format(gravityData!!.x1)} m/s²"
        tvGravity[1].text= "X2: ${"%.2f".format(gravityData!!.x2)} m/s²"
        tvGravity[2].text= "X3: ${"%.2f".format(gravityData!!.x3)} m/s²"
    }
}