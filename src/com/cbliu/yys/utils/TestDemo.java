package com.cbliu.yys.utils;

import com.cbliu.yys.utils.image.Coordinate;
import com.cbliu.yys.utils.image.ImagegUtils;



import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author bin
 * @create 2022-07-19-2:46
 */
public class TestDemo {
    public static void main(String[] args) throws AWTException, InterruptedException {
        //click(robot,LeftUpX+308+((failNum+victoryNum)%3)*332,leftUpY+208+((failNum+victoryNum)/3)*136)
//        testFind("kun28");//(1279,233)  ..(673-54=619,136)
        ArrayList<Coordinate> mnq = ImagegUtils.find("mnq");
        //mouseMove(640,88);
        Thread.sleep(3000);
        if(mnq.size()>0){
            mouseMove(mnq.get(0).x-54 + 1049 ,mnq.get(0).y+16+551);

        }
        //System.out.println("kun28");
//        System.out.println(MouseInfo.getPointerInfo().getLocation().x);
//        System.out.println(MouseInfo.getPointerInfo().getLocation().y);
//        Robot robot = new Robot();
//                    int a = 10;
//                    while((a--)>0){
//                        robot.mouseMove(1310,680);
//                    }
    }


    public void test1() throws AWTException {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        int a = 5;
//        int x = 0,y = 0;
//        while((a--)>0){
//             x = MouseInfo.getPointerInfo().getLocation().x;
//             y = MouseInfo.getPointerInfo().getLocation().y;
//        }
//        System.out.println("x:"+x);
//        System.out.println("y:"+y);
        //获取模拟器对角的坐标
        //左上角width:1280,height:720
        //mnq图标width:96,height:27;(519,47)

         //模拟器图标中心坐标需便宜(54,16),得到左上角坐标
        //左上角
        //mouseMove(465,63);
        //右下角
        //mouseMove(1745,783);

        mouseMove(1429,229);

    }


    public void getWidthAndHeightForImg(){
        String who = "jjpanel";
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("D:\\img\\"+ who +".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("width:"+img.getWidth());
        System.out.println("height:"+img.getHeight());
    }

    /**
     * 偏移量计算
     */

    public void testRgbLike(){
        while (true){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int x=0,y=0,a=5;
            while((a--)>0){
                x = MouseInfo.getPointerInfo().getLocation().x;
                y = MouseInfo.getPointerInfo().getLocation().y;
            }
            //获取全屏截屏,center1150,423
            BufferedImage img = null;
            try {
                Robot robot = new Robot();
                img = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                int rgb = img.getRGB(465+x, 63+y);
                int r =(rgb & 0xFF0000)>>16,g = (rgb & 0xFF00)>>8, b = rgb & 0xFF;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                img = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                int rgb2 = img.getRGB(465+x, 63+y);
                int r2 =(rgb & 0xFF0000)>>16,g2 = (rgb & 0xFF00)>>8, b2 = rgb & 0xFF;

                System.out.println(Math.sqrt(r * r2 + g * g2 + b * b2));
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }



    }

    public static void testFind(String who){
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long l = System.currentTimeMillis();
            ArrayList<Coordinate> tup = ImagegUtils.find(who);
            System.out.println("查找花费时间：" + (System.currentTimeMillis() - l));
            //System.out.println(tup.size());
           // (648,146)  (664,95)
            try {
                if(tup.size()>0){
                    for (Coordinate e:tup) {
                        System.out.println("(" + e.x +","+e.y+")");
                    }
                    mouseMove(tup.get(0).x,tup.get(0).y);
                }else {
                    System.out.println("未找到");
                }
            }catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }


    public void goJjtp() throws AWTException {
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
        int failNum = 0,victoryNum = 0;
        int LeftUpX,leftUpY;//模拟器左上角坐标
        Robot robot = new Robot();
        while ((failNum + victoryNum)<9 ){
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //每次循环开始需重新定位窗口
            mnq = ImagegUtils.find("mnq");
            if(mnq.isEmpty()){
                System.out.println("找不到模拟器窗口！");
                break;
            }
            LeftUpX = mnq.get(0).x-54;
            leftUpY = mnq.get(0).y+16;
            click(robot,LeftUpX+308+((failNum+victoryNum)%3)*332,leftUpY+208+((failNum+victoryNum)/3)*136);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ArrayList<Coordinate> jing = ImagegUtils.find("jing");//进攻按钮
            System.out.println(jing.size());
            if(jing.size()>0){
                click(robot,jing.get(0).x,jing.get(0).y);
                //进入战斗
                int loopTimes = 0;//战斗的循环次数,280次强制退出,大约5分钟
                while((loopTimes++)<280){
                    if(ImagegUtils.find("mnq").isEmpty()) break;
                    //循环间隔1秒
                    try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    //胜利或者失败
                    ArrayList<Coordinate> shengli = ImagegUtils.find("shengli");//胜利图标
                    ArrayList<Coordinate> fail = ImagegUtils.find("fail");//失败图标
                    if(shengli.size()>0){
                        click(robot,shengli.get(0).x,shengli.get(0).y);
                        victoryNum++;
                        break;
                    }else if(fail.size()>0){
                        click(robot,fail.get(0).x,fail.get(0).y-80);
                        failNum++;
                        break;
                    }
                }
            }
            //达到最后一个时
            if((failNum + victoryNum) == 8){
                //失败次数若小于3，则进去退相应的差值，保级(不改变failNum的值)
                int fails = failNum;
                while(fails<3){
                    if(ImagegUtils.find("mnq").isEmpty()) break;
                    //点击最后一个
                    click(robot,LeftUpX+308+((failNum+victoryNum)%3)*332,leftUpY+208+((failNum+victoryNum)/3)*136);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    jing = ImagegUtils.find("jing");//进攻按钮
                    if(jing.size()>0) {
                        click(robot, jing.get(0).x, jing.get(0).y);
                        //进入战斗,等待动画
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        int loopTimes = 0;//战斗的循环次数,280次强制退出,大约5分钟
                        while((loopTimes++)<280) {
                            //循环间隔0.5秒
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            ArrayList<Coordinate> back = ImagegUtils.find("back");//退出战斗图标
                            ArrayList<Coordinate> confirm = ImagegUtils.find("confirm");//确认图标
                            ArrayList<Coordinate> fail = ImagegUtils.find("fail");//失败图标
                            if(back.size()>0)
                                click(robot,back.get(0).x,back.get(0).y);
                            if(confirm.size()>0)
                                click(robot,confirm.get(0).x,confirm.get(0).y);
                            if(fail.size()>0){
                                click(robot,fail.get(0).x,fail.get(0).y);
                                fails++;
                                break;
                            }
                        }
                    }
                }
            }

        }
        if(victoryNum < 9){
            ArrayList<Coordinate> flush = ImagegUtils.find("flush");//刷新按钮
            click(robot,flush.get(0).x,flush.get(0).y);
        }
    }

    private static void mouseMove(int x,int y) throws AWTException {
        Robot robot = new Robot();
        int a = 5;
        while((a--)>0){
            robot.mouseMove(x,y);
        }
    }

    public static void click(Robot robot,int x,int y) throws AWTException {
        int d = 5;
        while ((d--) > 0) {
            robot.mouseMove(x +new Random().nextInt(5)+1, y+new Random().nextInt(5)+1);
        }
        robot.delay(200);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(200);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.delay(200);
    }
}
