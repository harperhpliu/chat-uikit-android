package com.tencent.qcloud.tuikit.tuicustomerserviceplugin.classicui.widget;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import com.tencent.qcloud.tuikit.timcommon.component.CustomLinearLayoutManager;
import com.tencent.qcloud.tuikit.tuicustomerserviceplugin.bean.BotBranchBean;
import com.tencent.qcloud.tuikit.tuicustomerserviceplugin.presenter.TUICustomerServicePresenter;

import java.util.List;

public class BotBranchListLayout extends RecyclerView {
    private BotBranchListAdapter adapter;

    public BotBranchListLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public BotBranchListLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BotBranchListLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setItemViewCacheSize(0);
        setHasFixedSize(true);
        CustomLinearLayoutManager linearLayoutManager = new CustomLinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(linearLayoutManager);
        SimpleItemAnimator animator = (SimpleItemAnimator) getItemAnimator();
        if (animator != null) {
            animator.setSupportsChangeAnimations(false);
        }

        adapter = new BotBranchListAdapter();
        super.setAdapter(adapter);
    }

    public void setPresenter(TUICustomerServicePresenter presenter) {
        if (presenter != null) {
            adapter.setPresenter(presenter);
        }
    }

    public void setBotBranchItemList(List<BotBranchBean.Item> itemList) {
        adapter.setBotBranchItemList(itemList);
    }

    public void refreshData() {
        adapter.refreshData();
    }
}

