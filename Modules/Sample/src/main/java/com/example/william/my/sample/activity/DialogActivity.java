package com.example.william.my.sample.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.sample.R;
import com.example.william.my.sample.adapter.LAdapter;

import java.util.Arrays;
import java.util.Calendar;

@Route(path = ARouterPath.Sample.Sample_Dialog)
public class DialogActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private final String[] mData = new String[]{"普通对话框", "列表对话框", "单选对话框", "日期对话框", "自定义对话框"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_dialog);

        ListView mListView = findViewById(R.id.dialog_listView);
        LAdapter<String> mAdapter = new LAdapter<String>(this, Arrays.asList(mData), R.layout.sample_item_recycler) {
            @Override
            public void convert(ViewHolder holder, String data) {
                ((TextView) holder.findView(R.id.item_textView)).setText(data);
            }
        };
        mListView.setAdapter(mAdapter);
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
                View view = getLayoutInflater().inflate(R.layout.basics_layout_response, (ViewGroup) getWindow().getDecorView(), false);
                TextView textView = view.findViewById(R.id.basics_response);
                textView.setBackgroundColor(ContextCompat.getColor(DialogActivity.this, R.color.colorPrimary));
                //AlertDialog dialog = new AlertDialog.Builder(DialogActivity.this)
                //        .create();
                //dialog.show();
                //dialog.setContentView(dialogView);
                AlertDialog dialog = new AlertDialog.Builder(DialogActivity.this)
                        .setView(view)
                        .create();
                dialog.show();
                //注意：需要在show()之后，才能再设置宽高属性
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.height = dp2px(180);
                dialog.getWindow().setAttributes(params);
                break;
        }
    }

    public static int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}