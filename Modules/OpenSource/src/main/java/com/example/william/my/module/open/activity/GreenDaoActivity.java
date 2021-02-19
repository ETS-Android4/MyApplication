package com.example.william.my.module.open.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.open.greendao.DBManager;
import com.example.william.my.module.open.greendao.NoteEntity;
import com.example.william.my.module.router.ARouterPath;
import com.google.gson.Gson;

import java.util.List;

/**
 * https://greenrobot.org/greendao/features/
 */
@Route(path = ARouterPath.OpenSource.OpenSource_GreenDao)
public class GreenDaoActivity extends BaseResponseActivity {

    @Override
    public void initView() {
        super.initView();
        showNote();
    }

    @Override
    public void setOnClick() {
        super.setOnClick();
        addNote();
        showNote();
    }

    private void addNote() {
        long id = DBManager.getInstance().searchAll().size() + 1;
        NoteEntity noteEntity = new NoteEntity(id, String.valueOf(id));
        DBManager.getInstance().insertOrReplace(noteEntity);
    }

    private void showNote() {
        List<NoteEntity> noteEntities = DBManager.getInstance().searchAll();
        showResponse(listToString(noteEntities, ","));
    }

    public String listToString(List<NoteEntity> list, String separator) {
        if (list.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                sb.append(new Gson().toJson(list.get(i))).append(separator);
            }
            return sb.toString().substring(0, sb.toString().length() - 1);
        } else {
            return "";
        }
    }
}