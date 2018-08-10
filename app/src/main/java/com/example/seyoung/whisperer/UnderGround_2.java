package com.example.seyoung.whisperer;

import android.util.Log;

public class UnderGround_2 extends SubWayMap {

        Node elevator_1;

        Node stair_1;
        Node stair_2;
        Node stair_3;
        Node stair_4;
        Node stair_5;
        Node stair_6;

        Node bathroom_men;
        Node bathroom_women;

        Node ticket_barrier1;
        Node ticket_barrier2;

        int[][] underGround2_isBlock = new int[][]{{3,4},{4,4},{5,3},{5,4},{6,3},{3,6},{4,6},{5,6},{6,6}};

        private int checkStair;
        private int checkElevator;

        public UnderGround_2(){
            underGround_isBlock = underGround2_isBlock;

            elevator_1 = new Node(6,4);
            stair_1 = new Node(2,6);

            checkElevator = 0 ;
            checkStair = 0;
        }

        @Override
        public Node better_Means_Transportation(Node initalNode, Node finalNode) {
            Navigation aStar = new Navigation(underGround_rows,underGround_cols,initalNode,elevator_1);
            for(Node node : aStar.findPath()){
                checkElevator += node.getF();
            }
            aStar = new Navigation(underGround_rows,underGround_cols,elevator_1,finalNode);
            for(Node node : aStar.findPath()){
                checkElevator += node.getF();
            }
            aStar = new Navigation(underGround_rows,underGround_cols,initalNode,stair_1);
            for(Node node : aStar.findPath()){
                checkStair += node.getF();
            }
            aStar = new Navigation(underGround_rows,underGround_cols,stair_1,finalNode);
            for(Node node : aStar.findPath()){
                checkStair += node.getF();
            }

            Log.e(this.getClass().getName(),"지하 2층 checkStair: "+String.valueOf(checkStair)+"checkElevator: "+String.valueOf(checkElevator));

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
