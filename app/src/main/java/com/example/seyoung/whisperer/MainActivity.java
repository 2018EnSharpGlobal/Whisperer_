package com.example.seyoung.whisperer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int B1 = -1;
    public static final int B2 = -2;
    public static final int B3 = -3;

    //벽
    public static final int block = 0;

    //출구
    public static final int exit_1 = 1;
    public static final int exit_2 = 2;
    public static final int exit_3 = 3;
    public static final int exit_4 = 4;
    public static final int exit_5 = 5;
    public static final int exit_6 = 6;

    //엘레베이터
    public static final int elevator_1 = 7;
    public static final int elevator_2 = 8;
    public static final int elevator_3 = 9 ;
    public static final int elevator_4 = 10;

    //계단
    public static final int stair_1 = 11;
    public static final int stair_2 = 12;

    //화장실
    public static final int men_bathroom = 13;
    public static final int women_bathroom = 14;

    //개찰구
    public static final int ticke_barrier1 = 15;
    public static final int ticke_barrier2 = 16;




    SubWayMap map_underGround_1;
    SubWayMap map_underGround_2;
    SubWayMap map_underGround_3;

    //위도 경도
    double user_latitude;
    double user_longitude;

    int user_floor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Node initialNode = new Node(6, 1);
        initialNode.setFloor(-1);
        Node finalNode = new Node(0, 3);
        finalNode.setFloor(-2);

        map_underGround_1 = new UnderGround_1();
        map_underGround_2 = new UnderGround_2();
        map_underGround_3 = new UnderGround_3();

        List<Node> path= null;

        List<Node> floor = null;
        List<Node> other_floor = null;

        Navigation underGround_1;
        Navigation underGround_2;
        Navigation underGround_3;

        if(initialNode.getFloor() != finalNode.getFloor()){
            Node better_transportation = null;

            switch(initialNode.getFloor()){
                case B1:
                    better_transportation = map_underGround_1.better_Means_Transportation(initialNode,finalNode);
                    underGround_1 = new Navigation(map_underGround_1.underGround_rows, map_underGround_1.underGround_cols, initialNode, better_transportation,B1);
                    underGround_1.setBlocks(map_underGround_1.underGround_isBlock);
                    floor = underGround_1.findPath();
                    break;
                case B2:
                    better_transportation = map_underGround_2.better_Means_Transportation(initialNode,finalNode);
                    underGround_2 = new Navigation(map_underGround_2.underGround_rows, map_underGround_2.underGround_cols, initialNode, better_transportation,B2);
                    underGround_2.setBlocks(map_underGround_2.underGround_isBlock);
                    floor = underGround_2.findPath();
                    break;
                case B3:
                    better_transportation =map_underGround_3.better_Means_Transportation(initialNode,finalNode);
                    underGround_3 = new Navigation(map_underGround_3.underGround_rows, map_underGround_3.underGround_cols, initialNode, better_transportation,B3);
                    underGround_3.setBlocks(map_underGround_3.underGround_isBlock);
                    floor = underGround_3.findPath();
                    break;
                default:
                    break;
            }

            switch(finalNode.getFloor()){
                case B1:
                    underGround_1 = new Navigation(map_underGround_1.underGround_rows, map_underGround_1.underGround_cols, better_transportation, finalNode,B1);
                    underGround_1.setBlocks(map_underGround_1.underGround_isBlock);
                    other_floor = underGround_1.findPath();
                    break;
                case B2:
                    underGround_2 = new Navigation(map_underGround_2.underGround_rows, map_underGround_2.underGround_cols, better_transportation, finalNode,B2);
                    underGround_2.setBlocks(map_underGround_2.underGround_isBlock);
                    other_floor = underGround_2.findPath();
                    break;
                case B3:
                    underGround_3 = new Navigation(map_underGround_3.underGround_rows, map_underGround_3.underGround_cols, better_transportation, finalNode,B3);
                    underGround_3.setBlocks(map_underGround_3.underGround_isBlock);
                    other_floor = underGround_3.findPath();
                    break;
                default:
                    break;
            }

            path = floor;
            path.addAll(other_floor);
        }
        else{
            switch (initialNode.getFloor()){
                case B1:
                    underGround_1 = new Navigation(map_underGround_1.underGround_rows,map_underGround_1.underGround_cols,initialNode,finalNode,B1);
                    underGround_1.setBlocks(map_underGround_1.underGround_isBlock);
                    path = underGround_1.findPath();
                    break;
                case B2:
                    underGround_2 = new Navigation(map_underGround_2.underGround_rows,map_underGround_2.underGround_cols,initialNode,finalNode,B2);
                    underGround_2.setBlocks(map_underGround_1.underGround_isBlock);
                    path = underGround_2.findPath();
                    break;
                case B3:
                    underGround_3 = new Navigation(map_underGround_3.underGround_rows,map_underGround_3.underGround_cols,initialNode,finalNode,B3);
                    underGround_3.setBlocks(map_underGround_1.underGround_isBlock);
                    path = underGround_3.findPath();
                    break;
                default:
                    break;
            }
        }

        //경로 로그에 출력하기
        for (Node node : path){
            Log.e("Floor",String.valueOf(node.getFloor())+node.toString());
        }


        setContentView(R.layout.activity_main);
    }

    //노드가 가는 경로 체크하는 함수
    public boolean user_CheckPosition(List<Node> path,int user_row,int user_col){
        boolean check_Position = false;
        for(Node node : path) {
            if (node.getRow() - 2 >= 0 &&
                    node.getCol() - 2 >= 0 &&
                    node.getRow() + 2 <= map_underGround_1.underGround_rows &&
                    node.getCol() + 2 <= map_underGround_1.underGround_cols) {
                if (node.getRow() - 2 <= user_row ||
                        user_row <= node.getRow() + 2 ||
                        node.getCol() - 2 <= user_col ||
                        user_col <= node.getCol() + 2) {
                    check_Position = true;
                }
            }
        }
        if(check_Position){
            return true;
        }
        else{
            return false;
        }
    }
}
