package screen.calling.com.callscreen;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import screen.calling.com.R;

public class OverLayActivity extends AppCompatActivity {
    
    private static final String TAG = "CallingActivity";
    
    private SensorManager mSensorManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay);
        // 监听手机距离人员距离
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(sensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
            SensorManager.SENSOR_DELAY_NORMAL);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(sensorEventListener);
    }
    
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        
        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] its = event.values;
            Log.d(TAG, "its array:" + its + "sensor type :" + event.sensor.getType() + " proximity type:"
                + Sensor.TYPE_PROXIMITY);
            
            if (its != null && event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                
                // 经过测试，当手贴近距离感应器的时候its[0]返回值为0.0，当手离开时返回1.0
                if (its[0] == 0.0) {// 贴近手机
                
                }
                else {// 远离手机
                    finish();
                }
            }
        }
        
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        
        }
    };
    
    @Override
    public void onBackPressed() {
        // 黑屏期间禁用返回键
        // super.onBackPressed();
    }
    
    public static Intent create(Context context){
        return new Intent(context, OverLayActivity.class);
    }
    
}
