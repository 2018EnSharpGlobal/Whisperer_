package com.example.seyoung.whisperer;

import android.util.Log;

public class UnderGround_1 extends SubWayMap {

    Node elevator_1;
    Node elevator_2;
    Node elevator_3;
    Node elevator_4;

    Node stair_1;
    Node stair_2;

    Node exit_1;
    Node exit_2;
    Node exit_3;
    Node exit_4;
    Node exit_5;
    Node exit_6;

    int[][] underGround1_isBlock = new int[][]{{0,0},{1,0},{1,1},{2,1},{3,1},{4,1},{5,0},{5,1},{6,0},{3,4},{4,4},{5,3},{5,4},{6,3},{3,6},{4,6},{5,6},{6,6}};

    private int checkElevator;
    private int checkStair;

    private int[] check_means_Transportation;

    private int[] check_Elevator;
    private int[] check_Stair;

    public UnderGround_1(){
        this.underGround_isBlock = underGround1_isBlock;

        elevator_1 = new Node(6,4);
        stair_1 = new Node(2,6);

        checkElevator = 0 ;
        checkStair = 0;

        check_means_Transportation= new int[6];
        check_Elevator = new int[4];
        check_Stair = new int[2];

        for(int i=0;i<check_means_Transportation.length;i++){
            check_means_Transportation[i] = 0;
        }

        for(int i=0;i< check_Elevator.length;i++){
            check_Elevator[i] = 0;
        }

        for(int i=0; i<check_Stair.length;i++){
            check_Stair[i] = 0;
        }
    }

    @Override
    public Node better_Means_Transportation(Node initalNode, Node finalNode){
        int find_means_Transportation;

        //엘레베이터
        Navigation find_path = new Navigation(underGround_rows,underGround_cols,initalNode,elevator_1,MainActivity.B1);

        for(Node node : find_path.findPath()){
            check_means_Transportation[0]  += node.getF();
        }
        find_path.setInitialNode(elevator_1);
        find_path.setInitialNode(finalNode);
        for(Node node : find_path.findPath()){
            check_means_Transportation[0] += node.getF();
        }

//        find_path = new Navigation(underGround_rows,underGround_cols,initalNode,elevator_2);
//        for(Node node : find_path.findPath()){
//            check_means_Transportation[1] += node.getF();
//        }
//        find_path = new Navigation(underGround_rows,underGround_cols,elevator_2,finalNode);
//        for(Node node : find_path.findPath()){
//            check_means_Transportation[1] += node.getF();
//        }
//
//        find_path = new Navigation(underGround_rows,underGround_cols,initalNode,elevator_3);
//        for(Node node : find_path.findPath()){
//            check_means_Transportation[2] += node.getF();
//        }
//        find_path = new Navigation(underGround_rows,underGround_cols,elevator_3,finalNode);
//        for(Node node : find_path.findPath()){
//            check_means_Transportation[2] += node.getF();
//        }
//
//        find_path = new Navigation(underGround_rows,underGround_cols,initalNode,elevator_4);
//        for(Node node : find_path.findPath()){
//            check_means_Transportation[3] += node.getF();
//        }
//        find_path = new Navigation(underGround_rows,underGround_cols,elevator_4,finalNode);
//        for(Node node : find_path.findPath()){
//            check_means_Transportation[3] += node.getF();
//        }

        //계단

        find_path.setInitialNode(initalNode);
        find_path.setFinalNode(stair_1);
        for(Node node : find_path.findPath()){
            check_means_Transportation[4]  += node.getF();
        }
        find_path.setInitialNode(stair_1);
        find_path.setFinalNode(finalNode);
        for(Node node : find_path.findPath()){
            check_means_Transportation[4] += node.getF();
        }
//        find_path = new Navigation(underGround_rows,underGround_cols,initalNode,stair_2);
//        for(Node node : find_path.findPath()){
//            check_means_Transportation[5] += node.getF();
//        }
//        find_path = new Navigation(underGround_rows,underGround_cols,stair_2,finalNode);
//        for(Node node : find_path.findPath()){
//            check_means_Transportation[5] += node.getF();
//        }

        check_means_Transportation[1] = 2000;
        check_means_Transportation[2] = 2000;
        check_means_Transportation[3] = 2000;
        check_means_Transportation[5] = 2000;

        find_means_Transportation = compare_Minimum(check_means_Transportation);

        if(find_means_Transportation==0){
            return elevator_1;
        }
        else if(find_means_Transportation == 1){
            return elevator_2;
        }
        else if(find_means_Transportation == 2){
            return elevator_3;
        }
        else if(find_means_Transportation == 3){
            return elevator_4;
        }
        else if(find_means_Transportation == 4){
            return stair_1;
        }
        else{
            return stair_2;
        }
    }

//    @Override
//    public Node stair_Means_Transportation(Node initalNode, Node finalNode) {
//        Navigation find_path = new Navigation(underGround_rows,underGround_cols,initalNode,stair_1);
//        for(Node node : find_path.findPath()){
//            check_Stair[0]  += node.getF();
//        }
//        find_path = new Navigation(underGround_rows,underGround_cols,stair_1,finalNode);
//        for(Node node : find_path.findPath()){
//            check_Stair[0] += node.getF();
//        }
//        find_path = new Navigation(underGround_rows,underGround_cols,initalNode,stair_2);
//        for(Node node : find_path.findPath()){
//            check_Stair[1] += node.getF();
//        }
//        find_path = new Navigation(underGround_rows,underGround_cols,stair_2,finalNode);
//        for(Node node : find_path.findPath()){
//            check_Stair[1] += node.getF();
//        }
//
//        if(check_Stair[0] >= check_Stair[1]){
//            return stair_1;
//        }
//        else{
//            return stair_2;
//        }
//    }

//    @Override
//    public Node elevator_Means_Transportation(Node initalNode, Node finalNode) {
//        int find_elevator;
//        Navigation find_path = new Navigation(underGround_rows,underGround_cols,initalNode,elevator_1);
//        for(Node node : find_path.findPath()){
//            check_Elevator[0]  += node.getF();
//        }
//        find_path = new Navigation(underGround_rows,underGround_cols,elevator_1,finalNode);
//        for(Node node : find_path.findPath()){
//            check_Elevator[0] += node.getF();
//        }
//
//        find_path = new Navigation(underGround_rows,underGround_cols,initalNode,elevator_2);
//        for(Node node : find_path.findPath()){
//            check_Elevator[1] += node.getF();
//        }
//        find_path = new Navigation(underGround_rows,underGround_cols,elevator_2,finalNode);
//        for(Node node : find_path.findPath()){
//            check_Elevator[1] += node.getF();
//        }
//
//        find_path = new Navigation(underGround_rows,underGround_cols,initalNode,elevator_3);
//        for(Node node : find_path.findPath()){
//            check_Elevator[2] += node.getF();
//        }
//        find_path = new Navigation(underGround_rows,underGround_cols,elevator_3,finalNode);
//        for(Node node : find_path.findPath()){
//            check_Elevator[2] += node.getF();
//        }
//
//        find_path = new Navigation(underGround_rows,underGround_cols,initalNode,elevator_4);
//        for(Node node : find_path.findPath()){
//            check_Elevator[3] += node.getF();
//        }
//        find_path = new Navigation(underGround_rows,underGround_cols,elevator_4,finalNode);
//        for(Node node : find_path.findPath()){
//            check_Elevator[3] += node.getF();
//        }
//
//        find_elevator = compare_Minimum(check_Elevator);
//
//        if(find_elevator==0){
//            return elevator_1;
//        }
//        else if(find_elevator == 1){
//            return elevator_2;
//        }
//        else if(find_elevator == 2){
//            return elevator_3;
//        }
//        else{
//            return elevator_4;
//        }
//    }
}
