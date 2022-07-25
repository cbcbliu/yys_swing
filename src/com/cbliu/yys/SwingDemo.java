package com.cbliu.yys;

import javax.swing.*;

/**
 * @author bin
 * @create 2022-07-18-19:44
 */
public class SwingDemo {
    public static void main(String[] args) {
        //新建窗口
        JFrame frame = new YysFrame("阴阳师脚本");
        //关闭窗口时退出程序
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //设置窗口参数
        frame.setSize(600,600);
        //显示窗口
        frame.setVisible(true);


    }



}

