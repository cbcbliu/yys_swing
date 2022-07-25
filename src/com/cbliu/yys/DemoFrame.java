package com.cbliu.yys;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义窗口
 * @author bin
 * @create 2022-07-18-20:02
 */
public class DemoFrame extends JFrame {

    public DemoFrame(String title){
        super(title);
        JPanel root = new JPanel();
        this.setContentPane(root);
//        JButton button = new JButton("测试");
//        root.add(button);
//        JLabel label = new JLabel("yys");
//        root.add(label);

//        JLabel time = new JLabel();
//        root.add(time);

//        JTextField textField = new JTextField(20);//文本框
//        textField.setText("yys");
//        root.add(textField);
//        JCheckBox agree = new JCheckBox("agree");//复选框
//        root.add(agree);

//        JComboBox<String> objectJComboBox = new JComboBox<>();//下拉框
//        objectJComboBox.addItem("hhh");
//        objectJComboBox.addItem("xxx");
//        root.add(objectJComboBox);

//        button.addActionListener(e -> {
//            SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
//            String format = sf.format(new Date());
//            time.setText(format);
//            System.out.println(format);
//        });
/*-------------------字体，颜色，背景，对齐方式---------------------------------*/
//        JLabel label = new JLabel();
//        label.setText("阴阳师");
//        //字体，(字体名称,风格,大小)，Font.PLAIN普通，Font.BOLD粗体
//        label.setFont(new Font("微软雅黑",Font.BOLD,24));
//        //设置文字颜色(前景色)
//        label.setForeground(new Color(153, 217, 139));
//        //设置不透明，背景色才能生效
//        label.setOpaque(true);
//        label.setBackground(new Color(45, 58, 168));
//        //设置控件大小
//        label.setPreferredSize(new Dimension(200,100));
//        //设置水平对齐方式，居中
//        label.setHorizontalAlignment(SwingConstants.CENTER);
//        root.add(label);
        /*---------------------布局----------------------------*/
        //流式布局，从左至右，排不下即换行，默认布局，不常用
        //FlowLayout flowLayout = new FlowLayout();

        //边界布局器，将容器分为东、西、南、北、中5个区域
        BorderLayout borderLayout = new BorderLayout();
        root.setLayout(borderLayout);
        root.add(new JLabel("北"),BorderLayout.NORTH);
        root.add(new JLabel("中心"),BorderLayout.CENTER);
        root.add(new JLabel("东"),BorderLayout.EAST);
        root.add(new JLabel("西"),BorderLayout.WEST);
        root.add(new JLabel("南"),BorderLayout.SOUTH);




    }



}
