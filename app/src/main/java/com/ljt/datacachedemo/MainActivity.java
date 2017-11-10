package com.ljt.datacachedemo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends Activity implements View.OnClickListener {

    //缓存
    private ACache mACache;
    private EditText et;
    private Button btn;
    private TextView tv;

    /*
    星座
    * http://web.juhe.cn:8080/constellation/getAll?consName=%E7%8B%AE%E5%AD%90%E5%BA%A7&type=today&key=申请的KEY
    * key:e1eb761e405b9867f7c4e82c9c09d2b2
    * */

    String s="http://web.juhe.cn:8080/constellation/getAll?consName=双鱼座&type=today&key=e1eb761e405b9867f7c4e82c9c09d2b2";
    String url = "https://api.github.com/";
    private String string;
    String result=" ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = (EditText) findViewById(R.id.et_main);
        btn = (Button) findViewById(R.id.btn_main);
        tv = (TextView) findViewById(R.id.tv_main);
        btn.setOnClickListener(this);
        mACache = ACache.get(this);
    }

    @Override
    public void onClick(View v) {

//        loadData();
        if(null==string){
            System.out.println("第一次加载 string 为 null");

            loadData();
        }else{
            try {
                InputStream datas = mACache.get("data");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(datas));
                String line;
                while((line=bufferedReader.readLine())!=null)
                {
                    result+=line;
                    System.out.println("第二次加载 string 为 result----->>>"+result);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    if(response.isSuccessful()){
                        string = response.body().string();
                        mACache.put("data",string);
                        System.out.println("请求数据------》》》string="+string);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
