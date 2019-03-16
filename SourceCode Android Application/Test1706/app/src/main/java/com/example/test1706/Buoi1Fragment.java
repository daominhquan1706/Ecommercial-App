package com.example.test1706;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import static android.support.constraint.Constraints.TAG;

public class Buoi1Fragment extends Fragment {

    private Button btn_hello,btn_chao,btn_clear,btn_cong,btn_tru,btn_nhan,btn_chia;
    private EditText edt_hello,edt_A,edt_B;
    private TextView tv_hello,tv_dapan;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.buoi_1,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tv_dapan = (TextView) getView().findViewById(R.id.tv_dapan);
        btn_hello = (Button) getView().findViewById(R.id.btn_hello);
        btn_chao= (Button) getView().findViewById(R.id.btn_chao);
        btn_clear= (Button) getView().findViewById(R.id.btn_clear);
        btn_cong= (Button) getView().findViewById(R.id.btn_cong);
        btn_tru= (Button) getView().findViewById(R.id.btn_tru);
        btn_nhan= (Button) getView().findViewById(R.id.btn_nhan);
        btn_chia= (Button) getView().findViewById(R.id.btn_chia);
        edt_A = (EditText) getView().findViewById(R.id.edt_A);
        edt_B = (EditText) getView().findViewById(R.id.edt_B);


        edt_hello = (EditText) getView().findViewById(R.id.edt_hello);
        tv_hello = (TextView) getView().findViewById(R.id.tv_hello);
        btn_hello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_hello.setText("Hello "+ edt_hello.getText());
            }
        });
        btn_chao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_hello.setText("Chào "+ edt_hello.getText());
            }
        });
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_hello.setText("");
            }
        });
        btn_cong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double a=Double.parseDouble(edt_A.getText().toString());
                double b=Double.parseDouble(edt_B.getText().toString());

                tv_dapan.setText("A + B = "+(a+b));
            }
        });
        btn_tru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double a=Double.parseDouble(edt_A.getText().toString());
                double b=Double.parseDouble(edt_B.getText().toString());

                tv_dapan.setText("A + B = "+(a-b));
            }
        });
        btn_nhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double a=Double.parseDouble(edt_A.getText().toString());
                double b=Double.parseDouble(edt_B.getText().toString());

                tv_dapan.setText("A + B = "+(a*b));
            }
        });
        btn_chia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double a=Double.parseDouble(edt_A.getText().toString());
                double b=Double.parseDouble(edt_B.getText().toString());
                if(b!=0){
                    tv_dapan.setText("A + B = "+(a/b));
                }
                else{
                    Toast.makeText(getActivity(), "B phãi khác 0", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}
