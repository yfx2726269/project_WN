package com;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Screen;


public class GenerateMainScene {
    //可调的棋盘大小参数{行列数,间隔}
    private static final double Adjust = 0. * Math.min(Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());//动态获取用户主屏幕分辨率,达到自适应显示效果
    private static final int[] LARGE_CHESSBOARD = new int[]{20, (int) Adjust / 25};
    private static final int[] SMALL_CHESSBOARD = new int[]{15, (int) Adjust / 20};
    private static final double largePaneWidth = Adjust * 0.9255;
    private static final double largePaneHeight = Adjust * 1.4362;
    private static final int size = SMALL_CHESSBOARD[0]; //行数
    private static final int gap = SMALL_CHESSBOARD[1]; //间隔
    private static final int boardSize = gap * (size + 1); //棋盘长宽
    private static final Chess[] chessboard = new Chess[size * size];//创建棋子数组
    private static boolean isBlack = true; //判断当前该谁下
    private static int step = 0;//记录当前步数

    public static Pane getChessboard() {
        //布置画板
        Pane largePane = new Pane();//背景版
        largePane.setPrefWidth(largePaneWidth);
        largePane.setPrefHeight(largePaneHeight);
        Pane chessPane = new Pane();//棋盘区
        chessPane.setPrefWidth(boardSize);
        chessPane.setPrefHeight(boardSize);
        chessPane.setLayoutX(Adjust * 0.0625);
        chessPane.setLayoutY(Adjust * 0.0625);
        Pane aim = new Pane();//落子标记
        aim.setPrefWidth(gap);
        aim.setPrefHeight(gap);
        aim.setLayoutX(-1000);
        aim.setLayoutY(-1000);

        chessPane.getChildren().add(aim);
        largePane.getChildren().add(chessPane);
        //加载图片素材
        Image imageBg = new Image("file:UiElemets/board.png");
        Image imageWc = new Image("file:UiElemets/wc.png");
        Image imageBc = new Image("file:UiElemets/bc.png");
        Image imageTbg = new Image("file:UiElemets/LagrePane.png");
        Image aimGif = new Image("file:UiElemets/Aim.gif");
        //设置背景
        largePane.setBackground(new Background(new BackgroundImage(imageTbg, null, null, null, new BackgroundSize(largePaneHeight, largePaneWidth, false, false, false, false))));
        chessPane.setBackground(new Background(new BackgroundImage(imageBg, null, null, null, new BackgroundSize(boardSize, boardSize, false, false, false, false))));
        aim.setBackground(new Background(new BackgroundImage(aimGif, null, null, null, new BackgroundSize(gap, gap, false, false, false, false))));

        //根据常量创建棋盘
        int sx = gap, sy = gap, ex = boardSize - gap, ey = gap;
        //将线条放入画板
        for (int i = 1; i <= size; ++i) {
            chessPane.getChildren().add(new Line(sx, sy, ex, ey));
            ex = ex ^ ey;
            ey = ex ^ ey;
            ex = ex ^ ey;
            sx = sx ^ sy;
            sy = sx ^ sy;
            sx = sx ^ sy;
            chessPane.getChildren().add(new Line(sx, sy, ex, ey));
            if (i % 2 == 1) {
                sx += gap;
                ex += gap;
            } else {
                sy += gap;
                ey += gap;
            }
            //绑定鼠标监听
            chessPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                boolean isPlaying = true;
                boolean isWin = false;
                @Override
                public void handle(MouseEvent event) {
                    double x = event.getX();
                    double y = event.getY();
                    //超出边界不落子
                    if ( isPlaying && x >= 0.7*gap && x <= boardSize - 0.7*gap && y >= 0.7*gap && y <= boardSize - 0.7*gap) {
                        //(x轴真实坐标 - 外边距 - 间距/2)/间距
                        int rx = (((int) x - gap + gap / 2) / gap);
                        int ry = (((int) y - gap + gap / 2) / gap);
                        //判断该位置有没有棋子
                        if (canDrop(rx, ry)) {
                            //黑白交替落子
                            Circle c;
                            Circle a;
                            if (isBlack) {
                                c = new Circle(gap + rx * gap, gap + ry * gap, gap * 0.4, new ImagePattern(imageBc));
                                //移动落子标识
                                aim.setLayoutX(gap + rx * gap - 0.49*gap);
                                aim.setLayoutY(gap + ry * gap - 0.49*gap);
                                chessboard[step] = new Chess(rx, ry, isBlack);
                                isBlack = false;
                            } else {
                                c = new Circle(gap + rx * gap, gap + ry * gap, gap * 0.4, new ImagePattern(imageWc));
                                //移动落子标识
                                aim.setLayoutX(gap + rx * gap - 0.49*gap);
                                aim.setLayoutY(gap + ry * gap - 0.49*gap);
                                chessboard[step] = new Chess(rx, ry, isBlack);
                                isBlack = true;

                            }
                            chessPane.getChildren().add(c);
//                            System.out.println(chessboard[step] + "..." + step);//测试输出
                            //判断胜负,九步之前不判断
                            if (step >= 8) {
                                isWin = isWin(1,0,chessboard[step]);//判断横向
                                if (!isWin)
                                    isWin = isWin(0,1,chessboard[step]);//判断纵向
                                if (!isWin)
                                    isWin = isWin(1,1,chessboard[step]);//判断斜向
                                if (!isWin)
                                    isWin = isWin(1,-1,chessboard[step]);//判断反斜
                                noticeWin(isWin);
                            }
                            ++step;
                        }
                    }
                    isPlaying = !isWin;
                }
            });
        }
        return largePane;
    }

    public static double getStageWeight() {
        return largePaneWidth;
    }

    public static double getStageHeight() {
        return largePaneHeight;
    }

    private static boolean canDrop(int rx, int ry) {
        for (int i = 0; i < step; ++i) {
            if (chessboard[i].getRx() == rx && chessboard[i].getRy() == ry)
                return false;
        }
        return true;
    }

    private static boolean isWin(int offsetX,int offsetY,Chess nowChess) {
        //依据设定的偏移值,获取特定方向,当前落子两侧+4的一组棋子
        int rx = nowChess.getRx();
        int ry = nowChess.getRy();
        Chess[] tempLine = new Chess[10];
        for (int i = -4; i <= 4; ++i) {
            Chess found = getChess(rx + i*offsetX, ry+i*offsetY, step);
            tempLine[4+i] = found;
        }
        int Count = 0;//计数有多少对连子
        int t = 0;
        //计算连着的棋子有多少,如果当前棋子是连子(右侧有颜色相同的棋子),计数器加1,计数器=4时,说明有连续的4对连子,胜利
        for(int i = 0 ; i < tempLine.length - 1 ;++i){
            if(tempLine[i] != null && tempLine[i + 1] != null && (tempLine[i].isBlack() == tempLine[i+1].isBlack())){
                t++;
            }else {
                t=0;
            }
            Count = Math.max(Count,t);
        }
        return Count >= 4;
        //纵向判断

//        //以当前落子位置为锚点,向8个方向依次遍历4次
//        int rx = nowChess.getRx();
//        int ry = nowChess.getRy();
//        int rowCount = 0;
//        int colCount = 0;
//        int slantCount = 0;
//        int antiCount = 0;
//        for (int i = 1; i < 5; ++i) {
//            rowCount += nowChess.equals(getChess(rx - i, ry, step)) ? 1 : 0;
//            rowCount += nowChess.equals(getChess(rx + i, ry, step)) ? 1 : 0;
//            colCount += nowChess.equals(getChess(rx, ry - i, step)) ? 1 : 0;
//            colCount += nowChess.equals(getChess(rx, ry + i, step)) ? 1 : 0;
//            slantCount += nowChess.equals(getChess(rx + i, ry + i, step)) ? 1 : 0;
//            slantCount += nowChess.equals(getChess(rx - i, ry - i, step)) ? 1 : 0;
//            antiCount += nowChess.equals(getChess(rx - i, ry + i, step)) ? 1 : 0;
//            antiCount += nowChess.equals(getChess(rx + i, ry - i, step)) ? 1 : 0;
//            if (rowCount == 4 || colCount == 4 || slantCount == 4 || antiCount == 4)
//                return true;
//        }
//        return false;
    }
//    private static int aimRecentChess(Chess chess)

    //返回指定位宫格坐标的棋子
    private static Chess getChess(int rx, int ry, int step) {
        if (rx >= 0 && rx <= size && ry >= 0 && ry <= size)
            for (int i = step; i >= 0; --i) {
                Chess chess = chessboard[i];
                if (chess.getRx() == rx && chess.getRy() == ry)
                    return chess;
            }
        return null;
    }

    private static void noticeWin(boolean isWin){
        if (isWin) {
            //给出胜利提示,创建弹出框对象
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            //添加内容
            alert.setHeaderText("你赢了");
            alert.setContentText("你赢了");
            //显示弹出框
            alert.showAndWait();
        }
    }

}

