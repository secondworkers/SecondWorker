package com.qiaoyi.secondworker.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qiaoyi.secondworker.R;

public class UpgradeDialog extends Dialog {
    private UpgradeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private String message;
        private View.OnClickListener okButtonListener;
        private View.OnClickListener cancelButtonListener;

        private View layout;
        private UpgradeDialog dialog;

        public Builder(Context context) {
            //这里传入自定义的style，直接影响此Dialog的显示效果。style具体实现见style.xml
            dialog = new UpgradeDialog(context, R.style.UpdateDialogStyle);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.new_update_dialog, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setOkButtonListener(View.OnClickListener okButtonListener) {
            this.okButtonListener = okButtonListener;
            return this;
        }
        public Builder setCancelButtonListener(View.OnClickListener cancelButtonListener) {
            this.cancelButtonListener = cancelButtonListener;
            return this;
        }

        /**
         * 创建单按钮对话框
         *
         * @return
         */
        public UpgradeDialog createDialog(String version,String fillsize,boolean cancelable,boolean canceledOnTouchOutside) {
            layout.findViewById(R.id.update_id_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelButtonListener.onClick(v);
                    dialog.dismiss();
                }
            });
            layout.findViewById(R.id.update_id_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    okButtonListener.onClick(v);
                    dialog.dismiss();
                }
            });
            create(version,fillsize,cancelable,canceledOnTouchOutside);
            return dialog;
        }

        /**
         * 单按钮对话框和双按钮对话框的公共部分在这里设置
         */
        private void create(String version,String fillsize,boolean cancelable,boolean canceledOnTouchOutside) {
            if (message != null) {      //设置提示内容
                ((TextView) layout.findViewById(R.id.update_content)).setText(message);
            }
            ((TextView) layout.findViewById(R.id.tv_new_size)).setText("新版本大小："+fillsize+"MySettingActivity");
            ((TextView) layout.findViewById(R.id.tv_new_version)).setText("最新版本："+ version);
            dialog.setContentView(layout);
            dialog.setCancelable(cancelable);     //用户可以点击手机Back键取消对话框显示
            dialog.setCanceledOnTouchOutside(canceledOnTouchOutside); //用户不能通过点击对话框之外的地方取消对话框显示
        }

    }
}
