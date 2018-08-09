package com.example.seyoung.whisperer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Navigation {
    //디폴트 값
    private static int DEFAULT_HV_COST = 10; // Horizontal - Vertical Cost
    private static int DEFAULT_DIAGONAL_COST = 14;

    private int hvCost;
    private int diagonalCost;

    //지도 노드 배열
    private Node[][] searchArea;

    //열린 목록, 닫힌 목록 리스트
    private PriorityQueue<Node> openList;
    private List<Node> closedList;

    //초기노드와 목적노드
    private Node initialNode;
    private Node finalNode;

    //생성자 함수
    public Navigation(int rows, int cols, Node initialNode, Node finalNode, int hvCost, int diagonalCost) {
        this.hvCost = hvCost;
        this.diagonalCost = diagonalCost;

        setInitialNode(initialNode);
        setFinalNode(finalNode);

        //지도 그리기
        this.searchArea = new Node[rows][cols];

        //열린 목록을 우선순위큐 리스트로 만듦
        this.openList = new PriorityQueue<Node>(new Comparator<Node>() {
            @Override
            public int compare(Node node0, Node node1) {
                return node0.getF() < node1.getF() ? -1 : node0.getF() > node1.getF() ? 1 : 0;
            }
        });

        setNodes();

        this.closedList = new ArrayList<Node>();
    }

    //생성사 오버로딩
    public Navigation(int rows, int cols, Node initialNode, Node finalNode) {
        this(rows, cols, initialNode, finalNode, DEFAULT_HV_COST, DEFAULT_DIAGONAL_COST);
    }

    //노드로 지도 그리기
    private void setNodes() {
        for (int i = 0; i < searchArea.length; i++) {
            for (int j = 0; j < searchArea[0].length; j++) {
                Node node = new Node(i, j);
                node.calculateHeuristic(getFinalNode());
                this.searchArea[i][j] = node;
            }
        }
    }

    //지도에서 막힌 부분 설정하는 함수
    public void setBlocks(int[][] blocksArray) {
        for (int i = 0; i < blocksArray.length; i++) {
            int row = blocksArray[i][0];
            int col = blocksArray[i][1];
            setBlock(row, col);
        }
    }

    //경로찾는 함수
    public List<Node> findPath() {
        //처음 노드 열린목록에 넣기
        openList.add(initialNode);

        //열린목록이 빌때까지
        while (!isEmpty(openList)) {
            //열린목록에서 확인하는 노드를 닫힌 목록에 넣기
            Node currentNode = openList.poll();
            closedList.add(currentNode);

            //현재노드가 목적노드라면
            if (isFinalNode(currentNode)) {
                return getPath(currentNode);
            } else {        //현재노드가 목적노드가 아니라면
                addAdjacentNodes(currentNode);
            }
        }
        return new ArrayList<Node>();
    }

    //현재 노드까지의 경로 얻는 함수
    private List<Node> getPath(Node currentNode) {
        List<Node> path = new ArrayList<Node>();
        path.add(currentNode);
        Node parent;
        //현재 노드의 부모가 NULL 일때까지
        while ((parent = currentNode.getParent()) != null) {
            path.add(0, parent);
            currentNode = parent;
        }
        return path;
    }

    //인접한 노드 추가하는 함수
    private void addAdjacentNodes(Node currentNode) {
        //위의 행 노드 추가하기
        addAdjacentUpperRow(currentNode);
        //중간의 행 노드 추가하기
        addAdjacentMiddleRow(currentNode);
        //밑에 있는 행 노드 추가하기
        addAdjacentLowerRow(currentNode);
    }

    //밑에 있는 행 노드 추가하는 함수
    private void addAdjacentLowerRow(Node currentNode) {
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int lowerRow = row + 1;     //현재노드보다 밑에 있는 행

        //현재노드보다 밑에 있는 행이 맵 내에 있다면
        if (lowerRow < getSearchArea().length) {
            //현재노드의 열보다 왼쪽에 있는 열이 0이상 (즉, 맵안에있다면)
            if (col - 1 >= 0) {
                checkNode(currentNode, col - 1, lowerRow, getDiagonalCost()); // Comment this line if diagonal movements are not allowed
            }
            //현재노드의 열보다 오른쪽에 있는 열이 맵 내에 있다면
            if (col + 1 < getSearchArea()[0].length) {
                checkNode(currentNode, col + 1, lowerRow, getDiagonalCost()); // Comment this line if diagonal movements are not allowed
            }
            //현재노드에 바로 밑에 있는 노드 확인함
            checkNode(currentNode, col, lowerRow, getHvCost());
        }
    }

    //중앙에 있는 행 노드 추가하는 함수
    private void addAdjacentMiddleRow(Node currentNode) {
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int middleRow = row;
        if (col - 1 >= 0) {
            checkNode(currentNode, col - 1, middleRow, getHvCost());
        }
        if (col + 1 < getSearchArea()[0].length) {
            checkNode(currentNode, col + 1, middleRow, getHvCost());
        }
    }

    //위에 있는 행 노드 추가하는 함수
    private void addAdjacentUpperRow(Node currentNode) {
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int upperRow = row - 1;
        if (upperRow >= 0) {
            if (col - 1 >= 0) {
                checkNode(currentNode, col - 1, upperRow, getDiagonalCost()); // Comment this if diagonal movements are not allowed
            }
            if (col + 1 < getSearchArea()[0].length) {
                checkNode(currentNode, col + 1, upperRow, getDiagonalCost()); // Comment this if diagonal movements are not allowed
            }
            checkNode(currentNode, col, upperRow, getHvCost());
        }
    }

    //노드 체크하는 함수
    private void checkNode(Node currentNode, int col, int row, int cost) {
        Node adjacentNode = getSearchArea()[row][col];
        if (!adjacentNode.isBlock() && !getClosedList().contains(adjacentNode)) {
            if (!getOpenList().contains(adjacentNode)) {
                adjacentNode.setNodeData(currentNode, cost);
                getOpenList().add(adjacentNode);
            } else {
                boolean changed = adjacentNode.checkBetterPath(currentNode, cost);
                if (changed) {
                    // Remove and Add the changed node, so that the PriorityQueue can sort again its
                    // contents with the modified "finalCost" value of the modified node
                    getOpenList().remove(adjacentNode);
                    getOpenList().add(adjacentNode);
                }
            }
        }
    }

    //층 수가 같은지 확인하는 함수
    public boolean isEqualFloor(Node initialNode,Node finalNode){
        if(initialNode.getFloor() == finalNode.getFloor())
            return true;
        else
            return false;
    }


    //층 수 체크하는 함수
    public void checkFloor(Node startNode,Node finalNode){
        if(startNode.getFloor() != finalNode.getFloor()){
            if(startNode.getFloor() == -1 && finalNode.getFloor() == -2){

            }
            else if(startNode.getFloor() == -1 && finalNode.getFloor() == -3){

            }
            else if(startNode.getFloor() == -2 && finalNode.getFloor() == -1){

            }
            else if(startNode.getFloor() == -2 && finalNode.getFloor() == -3){

            }
            else if(startNode.getFloor() == -3 && finalNode.getFloor() == -1){

            }
            else if(startNode.getFloor() == -3 && finalNode.getFloor() == -2){

            }
        }
        else{

        }
    }

    private boolean isFinalNode(Node currentNode) {
        return currentNode.equals(finalNode);
    }

    private boolean isEmpty(PriorityQueue<Node> openList) {
        return openList.size() == 0;
    }

    private void setBlock(int row, int col) {
        this.searchArea[row][col].setBlock(true);
    }

    public Node getInitialNode() {
        return initialNode;
    }

    public void setInitialNode(Node initialNode) {
        this.initialNode = initialNode;
    }

    public Node getFinalNode() {
        return finalNode;
    }

    public void setFinalNode(Node finalNode) {
        this.finalNode = finalNode;
    }

    public Node[][] getSearchArea() {
        return searchArea;
    }

    public void setSearchArea(Node[][] searchArea) {
        this.searchArea = searchArea;
    }

    public PriorityQueue<Node> getOpenList() {
        return openList;
    }

    public void setOpenList(PriorityQueue<Node> openList) {
        this.openList = openList;
    }

    public List<Node> getClosedList() {
        return closedList;
    }

    public void setClosedList(List<Node> closedList) {
        this.closedList = closedList;
    }

    public int getHvCost() {
        return hvCost;
    }

    public void setHvCost(int hvCost) {
        this.hvCost = hvCost;
    }

    private int getDiagonalCost() {
        return diagonalCost;
    }

    private void setDiagonalCost(int diagonalCost) {
        this.diagonalCost = diagonalCost;
    }
}
