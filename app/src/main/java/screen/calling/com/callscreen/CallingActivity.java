package screen.calling.com.callscreen;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import screen.calling.com.R;

public class CallingActivity extends AppCompatActivity {
    
    private static final String TAG = "CallingActivity";
    
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
    private SensorManager mSensorManager;
    
    public View mFlOverlay;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);
        mFlOverlay = findViewById(R.id.fl_overlay);
    
        // begin to define power relate variable
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(
            PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);
        
        // 监听手机距离人员距离
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(sensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(sensorEventListener);
    }
    
    private SensorEventListener sensorEventListener = new SensorEventListener(){
    
        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] its = event.values;
            // Log.d(TAG,"its array:"+its+"sensor type :"+event.sensor.getType()+" proximity type:"+Sensor.TYPE_PROXIMITY);
            if (mWakeLock != null && its != null && event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
               
                // 经过测试，当手贴近距离感应器的时候its[0]返回值为0.0，当手离开时返回1.0
                if (its[0] == 0.0) {// 贴近手机
                    
                    Log.d(TAG, "mWakeLock.isHeld:"+mWakeLock.isHeld()+" ,nearby the mobile");
                    
                    if (mWakeLock.isHeld()) {
                        // 取消计数,防止多次held，无法完全释放
                        mWakeLock.setReferenceCounted(false);
                        mWakeLock.release(); // 释放设备电源锁
                        
                        // 调节屏幕亮度
                        WindowManager.LayoutParams params = getWindow().getAttributes();
                        params.screenBrightness = 0;
                        getWindow().setAttributes(params);
                        // 显示遮罩层
                        mFlOverlay.setVisibility(View.VISIBLE);
                        
                        // getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }
                } else {// 远离手机
                    
                    Log.d(TAG, "mWakeLock.isHeld:"+mWakeLock.isHeld()+" ,far away from the mobile");
                    
                    if (!mWakeLock.isHeld()) {
                        mWakeLock.acquire();// 申请设备电源锁
                        
                        // 调节屏幕亮度
                        WindowManager.LayoutParams params = getWindow().getAttributes();
                        params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
                        //params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                        getWindow().setAttributes(params);
                        // 隐藏遮罩层
                        mFlOverlay.setVisibility(View.GONE);
                    }
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
        if(mFlOverlay.getVisibility() == View.VISIBLE){
            return;
        }
        
        super.onBackPressed();
    }
}
