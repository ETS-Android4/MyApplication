package com.example.william.my.module.opensource.bean;

import com.chad.library.adapter.base.entity.JSectionEntity;

public class SectionBean extends JSectionEntity {

    private final int position;

    public SectionBean(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public boolean isHeader() {
        return position % 10 == 0;
    }
}
