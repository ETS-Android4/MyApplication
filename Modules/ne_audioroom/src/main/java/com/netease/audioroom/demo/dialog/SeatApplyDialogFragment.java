package com.netease.audioroom.demo.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.netease.audioroom.demo.R;
import com.netease.audioroom.demo.adapter.SeatApplyAdapter;
import com.netease.yunxin.nertc.model.bean.VoiceRoomSeat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 上麦请求列表
 */
public class SeatApplyDialogFragment extends BaseDialogFragment {

    TextView tvTitle;
    TextView tvDismiss;

    RecyclerView recyclerView;
    SeatApplyAdapter adapter;

    private final List<VoiceRoomSeat> seats = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.request_dialog_fragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            ArrayList<VoiceRoomSeat> seats = getArguments().getParcelableArrayList(TAG);
            if (seats != null) {
                this.seats.addAll(seats);
            }
        } else {
            dismiss();
        }
        return inflater.inflate(R.layout.dialog_seat_apply, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getDialog() != null) {
            // 设置宽度为屏宽、靠近屏幕底部。
            final Window window = getDialog().getWindow();
            window.setBackgroundDrawableResource(R.color.color_00000000);
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.TOP;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(wlp);
        }

        initView(view);
        initListener();
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        tvTitle = view.findViewById(R.id.title);
        tvDismiss = view.findViewById(R.id.dismiss);
        buildHeadView();
        refresh();
    }

    private void buildHeadView() {
        adapter = new SeatApplyAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public void onMeasure(@NonNull RecyclerView.Recycler recycler, @NonNull RecyclerView.State state, int widthSpec, int heightSpec) {
                int count = state.getItemCount();
                if (count > 0) {
                    if (count > 4) {
                        count = 4;
                    }
                    int realHeight = 0;
                    int realWidth = 0;
                    for (int i = 0; i < count; i++) {
                        View view = recycler.getViewForPosition(0);
                        measureChild(view, widthSpec, heightSpec);
                        int measuredWidth = View.MeasureSpec.getSize(widthSpec);
                        int measuredHeight = view.getMeasuredHeight();
                        realWidth = Math.max(realWidth, measuredWidth);
                        realHeight += measuredHeight;
                        setMeasuredDimension(realWidth, realHeight);
                    }
                } else {
                    super.onMeasure(recycler, state, widthSpec, heightSpec);
                }
            }
        });
    }

    public void initListener() {
        adapter.setApplyAction(new SeatApplyAdapter.IApplyAction() {
            @Override
            public void refuse(VoiceRoomSeat seat) {
                requestAction.refuse(seat);
            }

            @Override
            public void agree(VoiceRoomSeat seat) {
                requestAction.agree(seat);
            }
        });
        tvDismiss.setOnClickListener((v) -> dismiss());
    }

    public void update(Collection<VoiceRoomSeat> seats) {
        this.seats.clear();
        this.seats.addAll(seats);
        if (isVisible()) {
            refresh();
        }
    }

    private void refresh() {
        tvTitle.setText(getString(R.string.apply_micro, seats.size()));
        adapter.setItems(seats);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        requestAction.dismiss();
    }

    IRequestAction requestAction;

    public void setRequestAction(IRequestAction requestAction) {
        this.requestAction = requestAction;
    }

    public interface IRequestAction {

        void refuse(VoiceRoomSeat seat);

        void agree(VoiceRoomSeat seat);

        void dismiss();

    }
}
