package com.lizhi.chenxiayu.subwayalarm.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lizhi.chenxiayu.subwayalarm.R;
import com.lizhi.chenxiayu.subwayalarm.utils.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lizhi.chenxiayu.subwayalarm.utils.util.isPhoneNumberModel;

/**
 * Created by chenxiayu on 2017/6/16.
 */

public class LoginActivity extends Activity implements View.OnClickListener{

    private EditText phoneNumber;
    private EditText verifyCode;
    private Button getVerifyCode;
    private Button loginBtn;
    private int countSeconds = 60;

    Handler mCountSecondsHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(countSeconds>0){
                --countSeconds;
                getVerifyCode.setText("("+countSeconds+")"+"秒后重新获取");
                sendEmptyMessageDelayed(0,1000);
            }else{
                countSeconds = 60;
                getVerifyCode.setText("重新获取验证码");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_login);
        phoneNumber = (EditText) findViewById(R.id.mobile_login);
        verifyCode = (EditText) findViewById(R.id.verify_code);
        getVerifyCode = (Button) findViewById(R.id.get_verify_code);
        loginBtn = (Button) findViewById(R.id.login_btn);

        getVerifyCode.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.get_verify_code:
                if(countSeconds == 60){
                    String mPhoneNumber = phoneNumber.getText().toString();
                    getPhoneNumber(mPhoneNumber);
                }else{
                    Toast.makeText(getBaseContext(),"不能重复发送验证码！",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.login_btn:

                break;
            default:
                break;
        }
    }

    private void getPhoneNumber(String mPhoneNumber) {
        if("".equals(mPhoneNumber)){

            new AlertDialog.Builder(this).setTitle("提示").setMessage("手机号码不能为空！").setCancelable(true).create().show();
        }else if(isPhoneNumberModel(mPhoneNumber) == false){
            new AlertDialog.Builder(this).setTitle("提示").setMessage("手机号码格式不正确！").setCancelable(true).create().show();
        }else{
            requestVerifyCode(mPhoneNumber);
        }
    }

    private void requestVerifyCode(String phoneNumber) {

        startCountSecBack();//这里是用来进行请求参数的

    }

    private void startCountSecBack() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getVerifyCode.setText(countSeconds+"");
                mCountSecondsHandler.sendEmptyMessage(0);
            }
        });
    }
    public  Boolean  isPhoneNumberModel(String tle) {
        Pattern p = Pattern.compile("^(13[0-9]|15([0-3]|[5-9])|14[5,7,9]|17[1,3,5,6,7,8]|18[0-9])\\d{8}$");
        Matcher matcher = p.matcher(tle);
        return matcher.matches();
    }
}
