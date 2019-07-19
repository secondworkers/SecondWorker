package com.qiaoyi.secondworker.ui.center.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.ShareBean;
import com.qiaoyi.secondworker.utlis.VwUtils;

/**
 * Created on 2019/6/24
 *
 * @author Spirit
 */

public class ShareListAdapter extends BaseQuickAdapter<ShareBean,BaseViewHolder>{
    public ShareListAdapter(int item_invitation_details_purple_background) {
        super(item_invitation_details_purple_background);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShareBean item) {
        helper.setText(R.id.tv_phone,VwUtils.midleReplaceStar(item.phone));
        helper.setText(R.id.tv_date,item.createTime);
    }
}
