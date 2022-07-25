package com.cbliu.yys;

import com.cbliu.yys.utils.image.Coordinate;
import com.cbliu.yys.utils.image.ImagegUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * 阴阳师脚本主界面,仅适用于雷电模拟器（分辨率手机版720*1280）
 *
 * @author bin
 * @version 1.0
 * @create 2022-07-18-22:27
 */
public class YysFrame extends JFrame {
    JLabel state;
    String errorDesc = "";
    //本次运行数据记录-------------
    JLabel CountTsWin;//探索战斗胜利次数
    JLabel CountJJWin;//结界突破胜利次数
    JLabel CountJJFail;//结界突破失败次数
    JLabel CountHunTuWin;//魂土胜利次数
    //----------------------------
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    ArrayList<String> showList = new ArrayList<String>();//展示在下方面板的信息集合

    int tpjNum;//掉落突破卷次数,计数
    JLabel stateLabel;
    JTextArea jTextArea;
    JButton start;
    JButton pause;
    JComboBox<String> type;
    JTextField victory;
    JTextField fail;
    JLabel tpjLabel;
    JTextField tpjField;//显示突破卷数量的控件

    public YysFrame(String title) {
        super(title);

        JPanel root = new JPanel();
        BorderLayout borderLayout = new BorderLayout();//边界布局上下结构
        root.setLayout(borderLayout);
        this.setContentPane(root);
        //上方
        initUp(root);
        //下方
        initDown(root);

        showListToPanel("初始化成功！");
    }

    private void goTansuo() throws InterruptedException, AWTException {
        //开始之前先判断，阴阳师窗口是否存在
        ArrayList<Coordinate> mnq = ImagegUtils.find("mnq");
        if (mnq.isEmpty()) {
            stopShell();
        }
        tpjNum = Integer.valueOf(tpjField.getText());
        boolean goJJ = false;
        //窗口存在且状态不为停止，循环
        while (mnq.size() > 0 && !(state.getText().equals("停止"))) {
            Robot robot = new Robot();
            if (tpjNum >= 10 && type.getSelectedItem().equals("探索-突破")) {
                if (!goJJ) {
                    showListToPanel("准备进入结界突破");
                }
                goJJ = true;
            }
            try {
                Thread.sleep(new Random().nextInt(1000) + 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mnq = ImagegUtils.find("mnq");
            ArrayList<Coordinate> coordinates = ImagegUtils.find("normal");//普通怪
            ArrayList<Coordinate> boss = ImagegUtils.find("boss");//boss
            ArrayList<Coordinate> tansuo = ImagegUtils.find("tansuo");//探索按钮
            ArrayList<Coordinate> kun28 = ImagegUtils.find("kun28");//困28入口
            //ArrayList<Coordinate> shengli = ImagegUtils.find("shengli");
            ArrayList<Coordinate> zlp = ImagegUtils.find("zlp");
            if (ImagegUtils.find("tpj").size() > 0) {
                showListToPanel("掉落突破卷");
                tpjNum++;//突破卷掉落次数加1
                tpjField.setText(String.valueOf(tpjNum));
            }
            if (mnq.isEmpty()) {
                //每次循环开始前判断窗口是否存在，不存在则break
                stopShell();
                break;
            }
            //探索战斗胜利次数
            if (ImagegUtils.find("shengli").size() > 0) {
                showListToPanel("探索战斗胜利！");
                CountTsWin.setText(String.valueOf((Integer.valueOf(CountTsWin.getText()) + 1)));
            }

            if (tansuo.size() > 0) {
                if (goJJ) {
                    ArrayList<Coordinate> exit = ImagegUtils.find("exit");
                    if (exit.size() > 0) {
                        click(robot, exit.get(0).x, exit.get(0).y);
                    }
                    Thread.sleep(1000);
                    jumpToJJ(robot);
                    goJJ = false;
                } else {
                    click(robot, tansuo.get(0).x, tansuo.get(0).y);
                }

            } else if (kun28.size() > 0) {
                if (goJJ) {
                    jumpToJJ(robot);
                    goJJ = false;
                } else {
                    click(robot, kun28.get(0).x, kun28.get(0).y);
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    tansuo = ImagegUtils.find("tansuo");
                    if (tansuo.size() > 0) {
                        click(robot, tansuo.get(0).x, tansuo.get(0).y);
                    }
                }


            } else if (zlp.size() > 0) {
                click(robot, zlp.get(0).x, zlp.get(0).y);
            } else if (coordinates.size() > 0 || boss.size() > 0) {
                click(robot, boss.size() > 0 ? boss.get(0).x : coordinates.get(0).x, boss.size() > 0 ? boss.get(0).y : coordinates.get(0).y);
            } else {
                click(robot, mnq.get(0).x + 727, mnq.get(0).y + 549);
            }

        }


    }

    private void jumpToJJ(Robot robot) throws InterruptedException, AWTException {
        ArrayList<Coordinate> jjtp;
        int loopTimes = 20;//20次内找不到结界突破入口，则结束
        while ((loopTimes--) > 0) {
            jjtp = ImagegUtils.find("jjtp");//突破入口
            if (jjtp.size() > 0) {
                click(robot, jjtp.get(0).x, jjtp.get(0).y);
                showListToPanel("进入结界突破");
                goJjtp();//结界突破
                if (ImagegUtils.find("jjpanel").size() > 0) {
                    //结界突破结束后若还在突破界面，点击关闭
                    ArrayList<Coordinate> exit = ImagegUtils.find("exit");
                    if (exit.size() > 0) {
                        click(robot, exit.get(0).x, exit.get(0).y);
                    }
                }
                break;
            }
        }

    }

    private void goJjtp() throws InterruptedException, AWTException {
        /**
         *结界数量共九个，绝对坐标分别为
         *(773,271),(1104,271),(1437,271)
         *(773,407),(1104,407),(1437,407)
         *(772,543),(1104,543),(1437,543)
         * ---------------------------------
         * 减去窗口左上角的起始坐标(465,63)后：
         * (308+x,208+y),(639+x,208+y),(972+x,208+y)
         * (308+x,344+y),(639+x,344+y),(972+x,344+y)
         * (308+x,480+y),(639+x,480+y),(972+x,480+y)
         * 横跨：332,垂直：136
         * ----------------------------------
         * 结界的状态：未攻打 --> 战胜(战神后处于不可点击状态)/战败(可继续攻打)
         * 分别设置状态码0,1,2
         */
        ArrayList<Coordinate> mnq;//模拟器
        int failNum = Integer.valueOf(fail.getText());
        int victoryNum = Integer.valueOf(victory.getText());
        int LeftUpX, leftUpY;//模拟器左上角坐标
        Robot robot = new Robot();
        do {
            Thread.sleep(2000);//等待动画
            ArrayList<Coordinate> tpj0;//突破卷为0图标
            ArrayList<Coordinate> shengli = ImagegUtils.find("shengli");//打完3/6/9个后的奖励
            if (shengli.size() > 0) {
                click(robot, shengli.get(0).x, shengli.get(0).y);
            }

            long startTime = System.currentTimeMillis();

            while ((failNum + victoryNum) < 9) {

                Thread.sleep(1500);
                //每次循环开始需重新定位窗口
                mnq = ImagegUtils.find("mnq");
                if (mnq.isEmpty()) {
                    stopShell();
                    break;
                }
                if (state.getText().equals("停止")) break;
                LeftUpX = mnq.get(0).x - 54;
                leftUpY = mnq.get(0).y + 16;

                tpj0 = ImagegUtils.find("tpj0");//突破卷为0图标
                if (tpj0.size() > 0) {
                    ArrayList<Coordinate> exit = ImagegUtils.find("exit");
                    if (exit.size() > 0) {
                        click(robot, exit.get(0).x, exit.get(0).y);
                    }
                    tpjNum = 0;
                    tpjField.setText(String.valueOf(tpjNum));
                    showListToPanel("突破卷为0,突破结束");
                    if (type.getSelectedItem().equals("突破")) {
                        state.setText("停止");
                        start.setEnabled(true);
                        pause.setEnabled(false);
                        type.setEnabled(true);
                        victory.setEnabled(true);
                        fail.setEnabled(true);
                        tpjField.setEnabled(true);
                    }
                    return;
                }
                ArrayList<Coordinate> jjpanel = ImagegUtils.find("jjpanel");
                if (jjpanel.size() > 0)
                    click(robot, LeftUpX + 308 + ((failNum + victoryNum) % 3) * 332, leftUpY + 208 + ((failNum + victoryNum) / 3) * 136);
                else click(robot, LeftUpX + 640, leftUpY + 88);//结界突破的相对位置
                Thread.sleep(1500);
                ArrayList<Coordinate> jing = ImagegUtils.find("jing");//进攻按钮
                //System.out.println(jing.size());
                if (jing.size() > 0) {
                    click(robot, jing.get(0).x, jing.get(0).y);
                    //进入战斗
                    int loopTimes = 0;//战斗的循环次数,280次强制退出,大约5分钟
                    while ((loopTimes++) < 280) {
                        if (ImagegUtils.find("mnq").isEmpty() || state.getText().equals("停止")) break;
                        //循环间隔1秒
                        Thread.sleep(1000);
                        //胜利或者失败
                        shengli = ImagegUtils.find("shengli");//胜利图标
                        ArrayList<Coordinate> failIcon = ImagegUtils.find("fail");//失败图标
                        if (shengli.size() > 0) {
                            click(robot, shengli.get(0).x, shengli.get(0).y);
                            victoryNum++;//本轮计数
                            CountJJWin.setText(String.valueOf((Integer.valueOf(CountJJWin.getText()) + 1)));//总计
                            victory.setText(String.valueOf(victoryNum));
                            showListToPanel("结界战斗胜利！");
                            break;
                        } else if (failIcon.size() > 0) {
                            click(robot, failIcon.get(0).x, failIcon.get(0).y - 80);
                            failNum++;
                            CountJJFail.setText(String.valueOf((Integer.valueOf(CountJJFail.getText()) + 1)));//总计
                            fail.setText(String.valueOf(failNum));
                            showListToPanel("结界战斗失败！");
                            break;
                        }
                    }
                }
                //达到最后一个且突破卷不为0时
                if ((failNum + victoryNum) == 8 && ImagegUtils.find("tpj0").isEmpty()) {
                    //失败次数若小于3，则进去退相应的差值，保级(不改变failNum的值)
                    int fails = failNum;
                    while (fails < 4) {
                        if (ImagegUtils.find("mnq").isEmpty() || state.getText().equals("停止")) break;
                        //点击最后一个
                        click(robot, LeftUpX + 308 + ((failNum + victoryNum) % 3) * 332, leftUpY + 208 + ((failNum + victoryNum) / 3) * 136);
                        Thread.sleep(1500);
                        jing = ImagegUtils.find("jing");//进攻按钮
                        if (jing.size() > 0) {
                            click(robot, jing.get(0).x, jing.get(0).y);
                            //进入战斗,等待动画
                            Thread.sleep(200);
                            int loopTimes = 0;//战斗的循环次数,280次强制退出,大约5分钟
                            boolean backDone = false;
                            while ((loopTimes++) < 280) {
                                if (ImagegUtils.find("mnq").isEmpty() || state.getText().equals("停止")) break;
                                //循环间隔0.5秒
                                Thread.sleep(200);
                                mnq = ImagegUtils.find("mnq");//退出战斗图标
                                ArrayList<Coordinate> confirm = ImagegUtils.find("confirm");//确认图标
                                ArrayList<Coordinate> fail = ImagegUtils.find("fail");//失败图标
                                if (mnq.size() > 0 && !backDone)
                                    click(robot, mnq.get(0).x - 16, mnq.get(0).y + 51);
                                if (confirm.size() > 0) {
                                    click(robot, confirm.get(0).x, confirm.get(0).y);
                                    backDone = true;
                                }
                                if (fail.size() > 0) {
                                    System.out.println("fail?");
                                    click(robot, fail.get(0).x, fail.get(0).y - 80);
                                    fails++;
                                    break;
                                }
                            }
                        }
                    }
                }

            }

            long endTime = System.currentTimeMillis();
            if (victoryNum < 9) { //胜利次数小于9,需手动刷新，且考虑时间是否达到5分钟
                if (ImagegUtils.find("mnq").isEmpty()) break;
                ArrayList<Coordinate> flush = ImagegUtils.find("flush");//刷新按钮
                ArrayList<Coordinate> confirmFlush = ImagegUtils.find("confirmFlush");//确认按钮
                if (flush.size() > 0) {
                    click(robot, flush.get(0).x, flush.get(0).y);
                }
                if (confirmFlush.size() > 0) {
                    Thread.sleep(500);
                    click(robot, confirmFlush.get(0).x, confirmFlush.get(0).y);
                    victoryNum = 0;
                    failNum = 0;
                    victory.setText(String.valueOf(victoryNum));
                    fail.setText(String.valueOf(failNum));
                    showListToPanel("手动刷新结界");
                }
            } else {
                victoryNum = 0;
                failNum = 0;
                victory.setText(String.valueOf(victoryNum));
                fail.setText(String.valueOf(failNum));
                showListToPanel("自动刷新结界");
            }
        } while (true);
    }

    private void goHunTu() throws InterruptedException, AWTException {
        Robot robot = new Robot();
        showListToPanel("魂11开始");
        while (true) {
            ArrayList<Coordinate> hun11 = ImagegUtils.find("hun11");//挑战按钮
            ArrayList<Coordinate> shengli = ImagegUtils.find("shengli");//挑战按钮
            ArrayList<Coordinate> mnq = ImagegUtils.find("mnq");
            if (mnq.isEmpty() || state.getText().equals("停止")) {
                stopShell();
                break;
            }
            if (hun11.size() > 0) {
                click(robot, hun11.get(0).x, hun11.get(0).y);
            } else if (shengli.size() > 0) {
                click(robot, shengli.get(0).x, shengli.get(0).y);
                showListToPanel("魂11战斗胜利");
                CountHunTuWin.setText(String.valueOf((Integer.valueOf(CountHunTuWin.getText()) + 1)));//总计
            }
            Thread.sleep(new Random().nextInt(2000) + 2000);
        }


    }

    /**
     * to do when press stop
     */
    private void stopShell() {
        state.setText("停止");
        start.setEnabled(true);
        pause.setEnabled(false);
        showListToPanel("找不到模拟器或阴阳师窗口！");
        victory.setEnabled(true);
        fail.setEnabled(true);
        tpjField.setEnabled(true);
        type.setEnabled(true);
    }

    private void click(Robot robot, int x, int y) {
        if (state.getText().equals("停止")) {
            return;
        }
        int d = 5;
        while ((d--) > 0) {
            robot.mouseMove(x + new Random().nextInt(5) + 1, y + new Random().nextInt(5) + 1);
        }
        robot.delay(200);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(200);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.delay(200);
    }

    private void initUp(JPanel root) {
        JPanel north = new JPanel();
        root.add(north, BorderLayout.NORTH);
        north.setOpaque(true);
        north.setPreferredSize(new Dimension(0, 120));
//        north.setLayout(new FlowLayout(FlowLayout.LEFT));//流式布局居左
        north.setLayout(new GridLayout(0, 1));
        Font font = new Font("正楷", Font.PLAIN, 14);

        //-----------第一行------------
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        north.add(row1);
        type = new JComboBox<>();
        type.addItem("探索-突破");
        type.addItem("探索");
        type.addItem("突破");
        type.addItem("魂土");
        type.setFont(font);
        type.setPreferredSize(new Dimension(100, 30));
        row1.add(type);

        start = new JButton();
        start.setText("启动");
        start.setPreferredSize(new Dimension(60, 30));
        start.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        row1.add(start);
        //启动监听器
        start.addActionListener(e -> {

            if (type.getSelectedItem().equals("探索-突破")) {
                showListToPanel("选择探索-突破，启动成功！");
                start.setEnabled(false);
                pause.setEnabled(true);
                type.setEnabled(false);
                state.setText("运行中");

                victory.setEnabled(false);
                fail.setEnabled(false);
                tpjField.setEnabled(false);

                new Thread(() -> {
                    try {
                        goTansuo();
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    } catch (AWTException awtException) {
                        awtException.printStackTrace();
                    }
                }).start();
            } else if (type.getSelectedItem().equals("突破")) {
                showListToPanel("选择突破，启动成功！");
                start.setEnabled(false);
                pause.setEnabled(true);
                type.setEnabled(false);
                state.setText("运行中");
                victory.setEnabled(false);
                fail.setEnabled(false);
                tpjField.setEnabled(false);
                new Thread(() -> {
                    try {
                        goJjtp();
                    } catch (InterruptedException | AWTException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }).start();
            } else if (type.getSelectedItem().equals("探索")) {
                showListToPanel("选择探索，启动成功！");
                start.setEnabled(false);
                pause.setEnabled(true);
                type.setEnabled(false);
                state.setText("运行中");
                victory.setEnabled(false);
                fail.setEnabled(false);
                tpjField.setEnabled(false);
                new Thread(() -> {
                    try {
                        goTansuo();
                    } catch (InterruptedException | AWTException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }).start();

            } else if (type.getSelectedItem().equals("魂土")) {
                showListToPanel("选择魂土，启动成功！");
                state.setText("运行中");
                victory.setEnabled(false);
                fail.setEnabled(false);
                tpjField.setEnabled(false);

                pause.setEnabled(true);
                start.setEnabled(false);
                type.setEnabled(false);


                new Thread(() -> {
                    try {
                        goHunTu();
                    } catch (InterruptedException | AWTException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }).start();
            }
        });

        pause = new JButton();
        pause.setText("停止");
        pause.setPreferredSize(new Dimension(60, 30));
        pause.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        row1.add(pause);
        //停止监听器
        pause.addActionListener(e -> {
            state.setText("停止");
            start.setEnabled(true);
            pause.setEnabled(false);
            type.setEnabled(true);
            victory.setEnabled(true);
            fail.setEnabled(true);
            tpjField.setEnabled(true);
            showListToPanel("停止成功！");
        });

        stateLabel = new JLabel("运行状态:");
        stateLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        row1.add(stateLabel);

        state = new JLabel("停止");
        state.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        row1.add(state);


        //-----------第二行------------
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        north.add(row2);
        //结界突破状态
        JLabel jjLabel = new JLabel("当前结界突破状态 -> 胜利:");
        jjLabel.setFont(font);
        row2.add(jjLabel);

        victory = new JTextField(2);
        victory.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        victory.setText("0");
        row2.add(victory);

        JLabel jjLabel2 = new JLabel("结界失败:");
        jjLabel2.setFont(font);
        row2.add(jjLabel2);

        fail = new JTextField(2);
        fail.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        fail.setText("0");
        row2.add(fail);

        tpjLabel = new JLabel("突破卷:");
        tpjLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        row2.add(tpjLabel);

        tpjField = new JTextField(2);
        tpjField.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        tpjField.setText("0");
        row2.add(tpjField);
        //-----------第三行------------
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        north.add(row3);
        //1
        JLabel countTsWinLabel = new JLabel("总计 -> 探索:");
        countTsWinLabel.setFont(font);
        row3.add(countTsWinLabel);

        CountTsWin = new JLabel("0");
        CountTsWin.setFont(font);
        row3.add(CountTsWin);
        //2
        JLabel countJJWinLabel = new JLabel(" 结界突破胜利:");
        countJJWinLabel.setFont(font);
        row3.add(countJJWinLabel);

        CountJJWin = new JLabel("0");
        CountJJWin.setFont(font);
        row3.add(CountJJWin);
        //3
        JLabel CountJJFailLabel = new JLabel(" 结界突破失败:");
        CountJJFailLabel.setFont(font);
        row3.add(CountJJFailLabel);

        CountJJFail = new JLabel("0");
        CountJJFail.setFont(font);
        row3.add(CountJJFail);
        //4
        JLabel CountHunTuWinLabel = new JLabel(" 魂土:");
        CountHunTuWinLabel.setFont(font);
        row3.add(CountHunTuWinLabel);

        CountHunTuWin = new JLabel("0");
        CountHunTuWin.setFont(font);
        row3.add(CountHunTuWin);


    }

    private void initDown(JPanel root) {

        JPanel south = new JPanel();
        root.add(south, BorderLayout.CENTER);
        south.setOpaque(true);
        south.setLayout(new GridLayout(0, 1));


        jTextArea = new JTextArea();
        jTextArea.setEditable(false);
        jTextArea.setLineWrap(true);
        jTextArea.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        JScrollPane jScrollPane = new JScrollPane(jTextArea);
        south.add(jScrollPane);
    }

    private void showListToPanel(String str) {
        String newStr = df.format(new Date()) + " ->> " + str;
        showList.add(newStr);
        try {
            FileOutputStream fo = new FileOutputStream(new File("D:\\yys\\log.txt"), true);
            fo.write(newStr.getBytes(StandardCharsets.UTF_8));
            fo.write("\r\n".getBytes(StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (showList.size() > 20) {
            showList.remove(0);
        }
        jTextArea.setText("");
        for (String s : showList) {
            jTextArea.append(s + "\n");
        }

    }
}
