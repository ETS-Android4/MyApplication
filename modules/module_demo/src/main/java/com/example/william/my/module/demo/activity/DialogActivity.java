package com.example.william.my.module.demo.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.demo.R;
import com.example.william.my.module.demo.dialog.MyBottomSheetDialog;
import com.example.william.my.module.demo.dialog.MyCreateDialogDialog;
import com.example.william.my.module.demo.dialog.MyCreateViewDialog;
import com.example.william.my.module.router.ARouterPath;

import java.util.Calendar;

@Route(path = ARouterPath.Demo.Demo_Dialog)
public class DialogActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private final String[] mData = new String[]{"普通对话框", "列表对话框", "单选对话框", "日期对话框",
            "自定义对话框", "自定义对话框2", "DialogFragment1", "DialogFragment2", "BottomSheetDialog"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_dialog);

        ListView mListView = findViewById(R.id.dialog_listView);
        mListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mData));
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
        switch (i) {
            case 0:
                new AlertDialog.Builder(DialogActivity.this)
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle("标题")
                        .setMessage("内容")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create()
                        .show();
                break;
            case 1:
                String[] items = {"item1", "item2"};
                new AlertDialog.Builder(DialogActivity.this)
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle("标题")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
                break;
            case 2:
                final String[] items2 = {"item1", "item2"};
                new AlertDialog.Builder(DialogActivity.this)
                        .setSingleChoiceItems(items2, 0,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                break;
            case 3:
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog =
                        new DatePickerDialog(DialogActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());//设置最大日期
                datePickerDialog.show();
                break;
            case 4:
                /*
                 * setContentView 为 Dialog 的方法 ，对应整个对话框窗口的view ，在dialog.show之后使用
                 * setView 是 AlertDialog 的方法 ，对应的是 CustomView 的部分而不是整个窗体 ，在dialog.show之前使用
                 */
                View view1 = getLayoutInflater().inflate(R.layout.basics_layout_response, (ViewGroup) getWindow().getDecorView(), false);
                TextView text1 = view1.findViewById(R.id.basics_response);
                text1.setBackgroundColor(ContextCompat.getColor(DialogActivity.this, R.color.colorPrimary));

                AlertDialog dialog1 = new AlertDialog.Builder(DialogActivity.this)
                        .create();
                dialog1.show();
                dialog1.setContentView(view1);
                break;
            case 5:
                View view2 = getLayoutInflater().inflate(R.layout.basics_layout_response, (ViewGroup) getWindow().getDecorView(), false);
                TextView text2 = view2.findViewById(R.id.basics_response);
                text2.setBackgroundColor(ContextCompat.getColor(DialogActivity.this, R.color.colorPrimary));

                AlertDialog dialog2 = new AlertDialog.Builder(DialogActivity.this)
                        .setView(view2)
                        .create();
                dialog2.show();
                //注意：需要在show()之后，才能再设置宽高属性
                WindowManager.LayoutParams params2 = dialog2.getWindow().getAttributes();
                params2.height = dp2px(180);
                dialog2.getWindow().setAttributes(params2);
                break;
            case 6:
                MyCreateDialogDialog dialogFragment = new MyCreateDialogDialog();
                dialogFragment.show(getSupportFragmentManager(), "dialog");
                break;
            case 7:
                MyCreateViewDialog dialogFragment2 = new MyCreateViewDialog();
                dialogFragment2.show(getSupportFragmentManager(), "dialog");
                break;
            case 8:
                MyBottomSheetDialog dialogFragment3 = new MyBottomSheetDialog();
                dialogFragment3.show(getSupportFragmentManager(), "dialog");
                break;
            default:
                break;
        }
    }

    public static int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}