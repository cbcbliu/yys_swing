package com.cbliu.yys.model;

/**
 * 结界突破面板上的结界
 * @author bin
 * @create 2022-07-19-23:41
 */
public class Enchantment {
    private int state;//状态
    private int failNum;//失败次数

    public Enchantment(int state){
        this.state=state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getFailNum() {
        return failNum;
    }

    public void setFailNum(int failNum) {
        this.failNum = failNum;
    }
}
