package com.example.seyoung.whisperer;

import android.util.Log;

public class UnderGround_3 extends SubWayMap {
    Node elevator_1;
    Node elevator_2;

    Node stair_1;
    Node stair_2;
    Node stair_3;
    Node stair_4;

    int[][] underGround3_isBlock= new int[][]{};

    private int checkStair;
    private int checkElevator;

    public UnderGround_3(){
        this.underGround_isBlock = underGround3_isBlock;

        checkElevator = 0 ;
        checkStair = 0;
    }

    @Override
    public Node better_Means_Transportation(Node initalNode, Node finalNode) {
//        Navigation aStar = new Navigation(underGround_rows,underGround_cols,initalNode,elevator_1);
//        for(Node node : aStar.findPath()){
//            checkElevator += node.getF();
//        }
//        aStar = new Navigation(underGround_rows,underGround_cols,elevator_1,finalNode);
//        for(Node node : aStar.findPath()){
//            checkElevator += node.getF();
//        }
//        aStar = new Navigation(underGround_rows,underGround_cols,initalNode,stair_1);
//        for(Node node : aStar.findPath()){
//            checkStair += node.getF();
//        }
//        aStar = new Navigation(underGround_rows,underGround_cols,stair_1,finalNode);
//        for(Node node : aStar.findPath()){
//            checkStair += node.getF();
//        }

        if(checkStair >= checkElevator){
            return elevator_1;
        }
        else{
            return stair_1;
        }
    }

    @Override
    public Node stair_Means_Transportation(Node initalNode, Node finalNode) {
        return super.stair_Means_Transportation(initalNode, finalNode);
    }

    @Override
    public Node elevator_Means_Transportation(Node initalNode, Node finalNode) {
        return super.elevator_Means_Transportation(initalNode, finalNode);
    }
}
