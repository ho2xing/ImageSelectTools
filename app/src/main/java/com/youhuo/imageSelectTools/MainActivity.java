package com.youhuo.imageSelectTools;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.util.core.activty.MultiImageSelectorActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button clickButton;

    private ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clickButton = (Button) findViewById(R.id.test_button);
        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCheckPermiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1) {
            if (intent != null) {
                arrayList.clear();

                arrayList = new ArrayList<>(intent.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT));
                if (arrayList != null && arrayList.size() > 0) {
                    StringBuffer outText = new StringBuffer();

                    for (String address : arrayList) {
                        outText.append("图片地址:");
                        outText.append(address);
                        outText.append("\n");
                    }
                    if (outText.length() > 0) {
                        new AlertDialog.Builder(this)
                                .setTitle("图片地址：")
                                .setMessage(outText.toString())
                                .setPositiveButton("确定", null)
                                .show();
                    }

                }


            }
        }
    }

    /**
     * 获取传递的默认值
     *
     * @param list
     * @return
     */
    private Intent getSelectImagesIntent(ArrayList<String> list) {

        Intent intent = new Intent(this, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);// 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 10);// 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);// 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, list); // 默认选择
        return intent;
    }

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA

    };


    private void toCheckPermiss() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission_read = ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int permission_camera = ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED||permission_read !=PackageManager.PERMISSION_GRANTED||permission_camera !=PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    PERMISSIONS_STORAGE,
                    1
            );
        } else {
            checkPermission = true;
        }
        if (checkPermission) {
            openSelectPage();
        }
    }


    private boolean checkPermission = false;


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        if (requestCode == 1 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkPermission = true;
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("权限：")
                        .setMessage("无权限")
                        .setPositiveButton("确定", null)
                        .show();
            }
            return;
        }

        if (checkPermission) {
            openSelectPage();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void openSelectPage() {
        Intent selectMyltiImage = getSelectImagesIntent(arrayList);
        startActivityForResult(selectMyltiImage, 1);
    }

}
