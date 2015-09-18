package net.king.appinfo.activity;

import net.king.appinfo.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Æô¶¯ÏÔÊ¾µÄActivity
 */
public class MainActivity extends BaseActivity implements OnClickListener{
    
    private Button btnMainApp;
    private Button btnAllApp;
    private Button btnSysApp;
    private Button btnThirdApp;
    private Button btnSdCardApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        init();
    }
    
    private void init() {
        btnMainApp = (Button) findViewById(R.id.btnMainApp);
        btnAllApp = (Button) findViewById(R.id.btnAllApp);
        btnSysApp = (Button) findViewById(R.id.btnSysApp);
        btnThirdApp = (Button) findViewById(R.id.btnThirdApp);
        btnSdCardApp = (Button) findViewById(R.id.btnSdCardApp);
        
        btnMainApp.setOnClickListener(this);
        btnAllApp.setOnClickListener(this);
        btnSysApp.setOnClickListener(this);
        btnThirdApp.setOnClickListener(this);
        btnSdCardApp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btnMainApp:
            ListAppActivity.actionStart(this, ListAppActivity.GET_LAUNCH_APP);
            break;
        case R.id.btnAllApp:
            ListAppActivity.actionStart(this, ListAppActivity.GET_ALL_APP);
            break;
        case R.id.btnSysApp:
            ListAppActivity.actionStart(this, ListAppActivity.GET_SYSTEM_APP);
            break;
        case R.id.btnThirdApp:
            ListAppActivity.actionStart(this, ListAppActivity.GET_THIRD_APP);
            break;
        case R.id.btnSdCardApp:
            ListAppActivity.actionStart(this, ListAppActivity.GET_SDCARD_APP);
            break;
            
        default:
            break;
        }
    }


}
