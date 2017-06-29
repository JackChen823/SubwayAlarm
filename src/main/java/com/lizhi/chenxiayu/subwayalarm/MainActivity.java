package com.lizhi.chenxiayu.subwayalarm;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lizhi.chenxiayu.subwayalarm.activity.CellInfoActivity;
import com.lizhi.chenxiayu.subwayalarm.activity.CellVerifyActivity;
import com.lizhi.chenxiayu.subwayalarm.activity.LoginActivity;
import com.lizhi.chenxiayu.subwayalarm.utils.CrashHandle;
import com.lizhi.chenxiayu.subwayalarm.utils.util;

public class MainActivity extends Activity implements View.OnClickListener{

    private Button subwayTest;
    private Button subwayVerify;
    private Button submitData;
    private Intent intent;
    private TextView versionNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //CrashHandle CrashHandle= com.lizhi.chenxiayu.subwayalarm.utils.CrashHandle.getInstance();
        //CrashHandle.init(getApplicationContext());
        initView();
        initData();
        Log.i("chenxiayu","版本号："+util.getVersion(getBaseContext()));
    }

    private void initData() {
        versionNumber.setText(versionNumber.getText()+getVersion());
    }

    private String getVersion() {
        String msg = null;
        try {
            ActivityInfo appInfo = this.getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            msg = appInfo.metaData.getString("major.minor.maintenance.build");
            return msg;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return msg;
    }

    private void initView() {
        subwayTest = (Button) findViewById(R.id.subway_test);
        subwayVerify = (Button) findViewById(R.id.subway_verify);
        submitData = (Button) findViewById(R.id.submit_data);

        versionNumber = (TextView) findViewById(R.id.version);

        subwayTest.setOnClickListener(this);
        subwayVerify.setOnClickListener(this);
        submitData.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.subway_test:
                intent = new Intent(MainActivity.this, CellInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.subway_verify:
                intent = new Intent(MainActivity.this, CellVerifyActivity.class);
                startActivity(intent);
                break;
            case R.id.submit_data:
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(getBaseContext(),"submit data",Toast.LENGTH_LONG).show();
                break;
        }
    }
}
