package com.test;

public class State {
    private String[][] currBoard;
    int size;
    private int XPos;
    private int YPos;
    private int g;
    private double h;
    private double f;
    private State parentState;
    private String actionMade;

    public State(String[][] currBoard, int size, int XPos, int YPos, int g, double h, State parentState, String actionMade) {
        this.currBoard = new String[size][size];
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++){
                this.currBoard[i][j] = currBoard[i][j];
            }
        }
    
        this.size=size;
        this.XPos = XPos;
        this.YPos = YPos;
        this.g = g;
        this.h = h;
        this.f = this.g + this.h;
        this.parentState = parentState;
        this.actionMade = actionMade;
    }

    //GETTERS
    public String[][] getCurrBoard(){
        return this.currBoard;
    }
    public int getXPos(){
        return this.XPos;
    }
    public int getYPos(){
        return this.YPos;
    }
    public int getG(){
        return this.g;
    }
    public double getH(){
        return this.h;
    }
    public double getF(){
        return this.f;
    }
    public State getParentState(){
        return this.parentState;
    } 
    public String getActionMade(){
        return this.actionMade;
    }

    //SETTERS
    public void setG(int newG){
        this.g = newG;
    }
    public void setH(double newH){
        this.h = newH;
    }
    public void setF(double newF){
        this.f = newF;
    }
    public void setXPos(int newX){
        this.XPos = newX;
    }
    public void setYPos(int newY){
        this.YPos = newY;
    }
    public void setParent(State newParent){
        this.parentState = newParent;
    }
    public void setBoard(String[][] newBoard){
        this.currBoard = newBoard;
    }
}