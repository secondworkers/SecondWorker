package com.qiaoyi.secondworker.utlis;

import java.util.LinkedList;

/**
 * Created on 2018/10/9
 *
 * @author Spirit
 */

public class FixSizeLinkedList<T> extends LinkedList<T> {
    private static final long serialVersionUID = 3292612616231532364L;
    // 定义缓存的容量
    private int capacity;
    public FixSizeLinkedList(int capacity) {
        super();
        this.capacity = capacity;
    }
    @Override
    public boolean add(T e) {
        // 超过长度，移除最后一个
        if (size() + 1 > capacity) {
            super.removeFirst();
        }
        return super.add(e);
    }
}
