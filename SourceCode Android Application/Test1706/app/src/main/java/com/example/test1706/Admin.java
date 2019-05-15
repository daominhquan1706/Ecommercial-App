package com.example.test1706;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.test1706.Config.Session;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;

public class Admin extends AppCompatActivity {

    CardView cv_sanpham, cv_tinnhan, cv_doanhthu, cv_donhang, cv_orders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        //Slidr.attach(this);

        initAdmin();
        cv_sanpham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, AdminSanPham_Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
        cv_tinnhan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Admin.this, Admin_Message_Activity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        cv_doanhthu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(Admin.this, chart.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        cv_donhang.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(Admin.this, MapBoxActivity.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        cv_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(Admin.this, Admin_HoaDon_Activity.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        session = new Session(getApplicationContext());
        if (session.getSwitchHuongDan()) {
            HuongDan();
        }

    }

    private Session session;

    private void HuongDan() {
        final TapTargetSequence sequence = new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(findViewById(R.id.img_ic_chinhsuasanpham), getString(R.string.quanlysanpham), getString(R.string.admin_motahuongdan1))
                                .tintTarget(false)
                                .id(1),
                        TapTarget.forView(findViewById(R.id.img_nhantinvoikhach), getString(R.string.nhantinkhachhang), getString(R.string.admin_motahuongdan2))
                                .tintTarget(false)
                                .cancelable(false)
                                .id(2),
                        TapTarget.forView(findViewById(R.id.img_ic_BaoCaoDoanhThu), getString(R.string.baocao), getString(R.string.admin_motahuongdan3))
                                .tintTarget(false)

                                .cancelable(false)
                                .id(3),

                        TapTarget.forView(findViewById(R.id.img_ic_accountmanager), getString(R.string.danhmuchoadon), getString(R.string.admin_motahuongdan5))
                                .tintTarget(false)

                                .cancelable(false)
                                .id(4))
                        /*TapTarget.forView(findViewById(R.id.img_thongTinGiaoHang), getString(R.string.bando), getString(R.string.admin_motahuongdan4))
                                        .tintTarget(false)

                                        .cancelable(false)
                                        .id(5),*/
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        // Executes when sequence of instruction get completes.
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        final AlertDialog dialog = new AlertDialog.Builder(Admin.this)
                                .setTitle("Uh oh")
                                .setMessage("You canceled the sequence")
                                .setPositiveButton("OK", null).show();
                        TapTargetView.showFor(dialog,
                                TapTarget.forView(dialog.getButton(DialogInterface.BUTTON_POSITIVE), "Uh oh!", "You canceled the sequence at step " + lastTarget.id())
                                        .cancelable(false)
                                        .tintTarget(false), new TapTargetView.Listener() {
                                    @Override
                                    public void onTargetClick(TapTargetView view) {
                                        super.onTargetClick(view);
                                        dialog.dismiss();
                                    }
                                });
                    }
                });
        sequence.start();
    }

    protected void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void initAdmin() {
        cv_doanhthu = (CardView) findViewById(R.id.cv_doanhthu);
        cv_donhang = (CardView) findViewById(R.id.cv_donhang);
        cv_sanpham = (CardView) findViewById(R.id.cv_sanpham);
        cv_tinnhan = (CardView) findViewById(R.id.cv_tinnhan);
        cv_orders = (CardView) findViewById(R.id.cv_orders);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
