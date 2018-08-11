package com.example.seyoung.whisperer;

public class Node {
    private int g;
    private int f;
    private int h;

    private int row;
    private int col;

    private int floor;

    private boolean isBlock;

    //지하철역에 대한 정보
    private int inforamtion;

    //특정 위치
    private int certain_location;

    private Node parent;

    public Node(int row, int col) {
        super();
        this.row = row;
        this.col = col;
    }

    //휴리스틱 값 계산하는 함수
    public void calculateHeuristic(Node finalNode) {
        this.h = Math.abs(finalNode.getRow() - getRow()) + Math.abs(finalNode.getCol() - getCol());
    }

    //노드의 정보를 설정하는 함수
    public void setNodeData(Node currentNode, int cost) {
        int gCost = currentNode.getG() + cost;
        setParent(currentNode);
        setG(gCost);
        calculateFinalCost();
    }

    //더 나은 경로인지 확인하는 함수
    public boolean checkBetterPath(Node currentNode, int cost) {
        int gCost = currentNode.getG() + cost;
        if (gCost < getG()) {
            setNodeData(currentNode, cost);
            return true;
        }
        return false;
    }

    //평가함수 f 계산하는 함수
    private void calculateFinalCost() {
        int finalCost = getG() + getH();
        setF(finalCost);
    }

    //노드가 같은지 확인하는 함수
    @Override
    public boolean equals(Object arg0) {
        Node other = (Node) arg0;
        return this.getRow() == other.getRow() && this.getCol() == other.getCol();
    }

    //노드의 열과행을 문자열로 바꾸는 함수
    @Override
    public String toString() {
        return "Node [row=" + row + ", col=" + col + "]";
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public boolean isBlock() {
        return isBlock;
    }

    public void setBlock(boolean isBlock) {
        this.isBlock = isBlock;
    }

    public int getInforamtion() {return inforamtion;}

    public void setInforamtion(int inforamtion) {this.inforamtion = inforamtion;}

    public void setCertain_location(int certain_location){
        this.certain_location = certain_location;
    }

    public int getCertain_location(){
        return certain_location;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getFloor(){
        return floor;
    }

    public void setFloor(int floor){
        this.floor = floor;
    }

}
