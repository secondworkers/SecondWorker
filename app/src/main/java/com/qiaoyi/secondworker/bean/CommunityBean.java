package com.qiaoyi.secondworker.bean;

/**
 * Created on 2019/7/26
 *
 * @author Spirit
 */

public class CommunityBean {

    /**
     * cityCode : 370100
     * shequName : 舜清苑社区
     * id : a69eab4df0a64bb9bcfea72c241753a2
     * cityName : 济南市
     * provinceCode : 370000
     * province : 山东省
     */

    public String cityCode;
    public String shequName;
    public String id;
    public String cityName;
    public String provinceCode;
    public String province;
    private boolean isSelected;
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
