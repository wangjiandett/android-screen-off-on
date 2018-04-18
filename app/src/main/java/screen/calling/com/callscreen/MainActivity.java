package screen.calling.com.callscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import screen.calling.com.R;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    /**
     * 第一种控制显示黑屏的方法，是在靠近手机时在当前页面上显示一个黑色的遮罩层
     * 并调节当前window的亮度，达到黑屏的效果
     *
     * @param view
     */
    public void doClick(View view) {
        startActivity(new Intent(this, CallingActivity.class));
    }
    
    /**
     * 第二种控制黑屏的方法，是在靠近手机时（或遮挡传感器时）
     * 跳转到一个黑色全屏的界面，并调节window的亮度，达到黑屏的效果
     *
     * @param view
     */
    public void doClick2(View view) {
        startActivity(new Intent(this, CallingActivity2.class));
    }
}
