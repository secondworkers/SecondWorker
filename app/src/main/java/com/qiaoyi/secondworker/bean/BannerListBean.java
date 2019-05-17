package com.qiaoyi.secondworker.bean;

import cn.isif.plug.bannerview.bean.anno.Banner;

/**
 * Created on 2019/5/11
 *
 * @author Spirit
 */

public class BannerListBean {
    public String id;
    @Banner("url")
    public String bannerUrl;
    public String href;
}
