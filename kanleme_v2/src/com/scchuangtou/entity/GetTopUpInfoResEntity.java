package com.scchuangtou.entity;

import java.util.List;

/**
 * Created by SYT on 2016/4/28.
 */
public class GetTopUpInfoResEntity extends BaseResEntity {
    public boolean has_more_data;
    public List<Data> datas;

    public static class Data {
        public int type;
        public long top_up_time;
        public float price;
        public String order_no;
    }
}
