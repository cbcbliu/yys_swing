package com.cbliu.yys.utils.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 图片工具类
 * @author bin
 * @create 2022-07-18-22:15
 */
public class ImagegUtils {

    /**
     * 传入指定目录图片名字（.peng格式）,在当前全部屏幕上寻找匹配的区域，返回该区域中心点的坐标集合
     * @param who
     * @return
     */
    public static ArrayList<Coordinate> find(String who){
        int likes = 0;
        int matches =0;
        ArrayList list = new ArrayList<Coordinate>();
        BufferedImage imgByPath = getImgByPath("D:\\img\\"+ who +".png");
        BufferedImage fullScreen = getFullScreen();
        long l2 = System.currentTimeMillis();
        RgbInfo[][] bigData = getImgRGB(fullScreen);
        RgbInfo[][] smallData = getImgRGB(imgByPath);
        System.out.println("准备数据耗时：" + (System.currentTimeMillis()-l2));
        int height = fullScreen.getHeight();
        int width = fullScreen.getWidth();
        int sheight = imgByPath.getHeight();
        int swidth = imgByPath.getWidth();
        int targetX;
        int targetY;
        long l = System.currentTimeMillis();
        for(int y = 0;y<height-sheight;y++){
            for (int x = 0;x<width-swidth;x++){
                if(compareRgb(bigData[x][y] , smallData[0][0],true) && compareRgb(bigData[x + swidth -1][y] , smallData[swidth -1][0],true)
                        && compareRgb(bigData[x][y + sheight -1],smallData[0][sheight-1],true) && compareRgb(bigData[x + swidth -1][y + sheight -1],smallData[swidth-1][sheight-1],true)
                        &&compareRgb(bigData[x + swidth/2][y + sheight/2],smallData[swidth/2][sheight/2],true)){
                    likes++;
                    if(allMatch(x,y,sheight,swidth,bigData,smallData)){
                        matches++;
                        targetX = x + swidth/2;
                        targetY = y + sheight/2;
                        //System.out.println("W："+swidth + ",H" + sheight);
                        list.add(new Coordinate(targetX,targetY));
                    }
                }
            }
        }
        System.out.println("循环数据耗时：" + (System.currentTimeMillis()-l));
        //System.out.println("相似目标数量："+likes + ",匹配目标数量" + matches);
        return list;
    }

    private static boolean allMatch(int x,int y,int sh,int sw,RgbInfo [][] bigData,RgbInfo[][] smallData){
        boolean flag = true;
        int wrongNum=0;
        int all = sh*sw;
        for(int sY = 0;sY < sh;sY ++){
            for (int sX = 0;sX < sw;sX++){
                if(!compareRgb(bigData[x + sX][y+sY],smallData[sX][sY],false)){
                    flag = false;
                    //wrongNum++;
                }
            }
        }
        return flag;
        //System.out.println("相似度：" + (wrongNum * 1.0) / all);
        //return (wrongNum*1.0)/all < 0.2;
    }

    private static boolean compareRgb(RgbInfo a,RgbInfo b,boolean isLike){
        int absR = a.r -b.r;
        int absG = a.g - b.g;
        int absB = a.b - b.b;
        double sqrt = Math.sqrt(absR * absR + absG * absG + absB * absB);
        // System.out.println(sqrt);
        return sqrt < (isLike?30:200);
    }

    private static BufferedImage getFullScreen(){
        BufferedImage img = null;
        try {
            Robot robot = new Robot();
            img = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

        } catch (AWTException e) {
            e.printStackTrace();
        }
        return img;
    }

    private static BufferedImage getImgByPath(String path){
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    private static RgbInfo[][] getImgRGB(BufferedImage img){
        int height = img.getHeight();
        int width = img.getWidth();
        RgbInfo [][] result = new RgbInfo[width][height];
        for (int y = 0;y<height;y++){
            for (int x = 0;x<width;x++){
                int a = img.getRGB(x,y);
                result[x][y] =new RgbInfo((a & 0xFF0000)>>16,(a & 0xFF00)>>8,a & 0xFF);

            }
        }
        return result;
    }
}
