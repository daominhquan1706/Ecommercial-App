package com.example.test1706;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.example.test1706.model.Product;
import com.example.test1706.mongodb.Code_mongodb;

import java.util.ArrayList;
import java.util.List;
import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;


public class Test_mongodb extends AppCompatActivity {
    private EditText edtname, edtsdt;
    private Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_mongodb);

        btn = (Button) findViewById(R.id.btn_add_mongodb);
        edtname = (EditText) findViewById(R.id.name);
        edtsdt = (EditText) findViewById(R.id.sdt);

        Code_mongodb db = new Code_mongodb("Product");
        db.layDanhSachDongHo();
    }


}
