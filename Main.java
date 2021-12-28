package com.test;

// Another State.java file is included in the same package named as State
// Which includes Getters and Setters Required in the Main.java file
import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.lang.Math;
import java.util.Collections;
import java.util.Random;

public class Main{

    public static void main(String [] args){
        
        int initXPos = 0;
        int initYPos = 0;
        int goalXPos = 0;
        int goalYPos = 0;
        int g = 0;
        double h;
        State finalState;
        ArrayList<String> moves = new ArrayList<String>();
       
        /*propmts the user for the size of Maze*/

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter size of board: ");
        int size = sc.nextInt();

        
        /*Initiate the board with Random values*/
        String [][] initConfig = new String[size][size];
        
        Random rand;
        int Gcount = 0;
        int Pcount = 0;
        int randValue=0;
        //Creation of board
        
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                rand = new Random();
                randValue = rand.nextInt(4); //min = 0, max = 3
                while((randValue == 2 && Gcount!=0) || (randValue==3 && Pcount!=0)){        
                	//Monitors goal count and person count.

                    rand = new Random();
                    randValue = rand.nextInt(3);                                            //min = 0, max = 3
                }
                if(i==size-1 && j==size-1 && Pcount==0){                                    //if last index  is again person, make last index the person
                    randValue = 3;
                }
                if(i==size-1 && j==size-1 && Gcount==0){                                    //if last index  is again goal, make last index the goal
                    randValue = 2;
                }
                
                switch(randValue){
                    case 0: //WALL
                        initConfig[i][j] = "W";
                        break;
                    case 1: //TILE
                        initConfig[i][j] = "t";
                        break;
                    case 2: //GOAL
                        initConfig[i][j] = "G";
                        goalXPos = j;
                        goalYPos = i;
                        Gcount++;
                        break;
                    case 3: //PERSON
                        initConfig[i][j] = "P";
                        initXPos = j;
                        initYPos = i;
                        Pcount++;
                        break;

                }   
            } 
        }

        //Print board
        for(int i=0;i<size;i++){
            for(int j=0; j<size; j++){
                System.out.print(initConfig[i][j] + " ");
            }
            System.out.println();
        }

        h = computeH(initXPos, initYPos, goalXPos, goalYPos);
        State initState = new State(initConfig, size, initXPos, initYPos, 0, h, null, "0");
        finalState = AStar(initState, goalXPos, goalYPos, size);

        //traverse backwards while parentState != null,
      	//get current state action made 
        try{
            while(finalState.getParentState()!=null){
            
                // System.out.println(finalState.getActionMade());
            	
            	
                moves.add(finalState.getActionMade()); 
                finalState = finalState.getParentState();
            }
            
            Collections.reverse(moves);
            
            try{
                BufferedWriter writer = new BufferedWriter(new FileWriter("maze.out"));
                System.out.println("\nMOVES: ");
                for(int i=0; i<moves.size(); i++){
                    System.out.println(moves.get(i));
                    writer.write(moves.get(i));
                }
                writer.close();
            } catch(FileNotFoundException e){
            	
                System.out.println("File not found");
            } catch(Exception e){
            	
                System.out.println(e.getMessage());
            }
        }
        catch(Exception e){
            System.out.println("Puzzle Not Solvable");
        }
    }

    private static double computeH(int XPos, int YPos, int goalXPos, int goalYPos){
        //Used the Manhattan distance formula 
        double h =  Math.abs(goalXPos-XPos) + Math.abs(goalYPos-YPos);
        return h;

        /***
            
            Euclidean distance formula computes for the diagonal distance of two points while the Manhattan distance
            formula computes for the distance based on the horizontal and vertical paths towards the goal.
            
            And in the maze we used to calculate the grids so it is better to use the manhattan distance
        ***/

    }

    private static State AStar(State initState, int goalXPos, int goalYPos, int sizeOfBoard) {
        ArrayList<State> openList = new ArrayList<State>();
        ArrayList<State> exploredList = new ArrayList<State>();
        ArrayList<String> actions = new ArrayList<String>();
        State bestNode, resState;
        int duplicateIndex;

        openList.add(initState);
     
        while(!openList.isEmpty()){
            
            bestNode = openList.remove(removeMinF(openList));
            
            exploredList.add(bestNode);

            if(GoalTest(bestNode, goalXPos, goalYPos))
                return bestNode;
            
            actions = Actions(bestNode, sizeOfBoard);
            
            for(String action: actions){
               
                resState = Result(bestNode, action, goalXPos, goalYPos, sizeOfBoard);
                
                if( (!isDuplicate(openList, resState) || !isDuplicate(exploredList, resState)) || 
                    (isDuplicate(openList, resState) && resState.getG() < dupl(openList,resState).getG()) ||
                    (isDuplicate(exploredList, resState) && resState.getG() < dupl(exploredList, resState).getG())) {
                    
                        openList.add(resState);
                }
            }
        }
        return null;
    }
 
    private static int removeMinF(ArrayList<State> stateList){
        double minF = stateList.get(0).getF();
        int index=0;
        for(int i=0; i<stateList.size(); i++){
           
            if(stateList.get(i).getF() < minF){
                index = i;
            }
        }
      
        return index;
    }
    private static boolean isDuplicate(ArrayList<State> stateList, State state){
        //checks if may state in a state list has same XPOS and YPOS ng currentState
        for(int i=0; i<stateList.size(); i++){
            if(stateList.get(i).getXPos() == state.getXPos() && stateList.get(i).getYPos() == state.getYPos()){
                return true;
            }
        }
        return false;
    }

    private static State dupl(ArrayList<State> stateList, State state) {
         //checks if may state in a state list has  same XPOS and YPOS ng currentState
         //if may same, return the state instance
        for(int i=0; i<stateList.size(); i++){
            if(stateList.get(i).getXPos() == state.getXPos() && stateList.get(i).getYPos() == state.getYPos()){
                return stateList.get(i);
            }
        }
        return null;
    }

    private static State Result(State state, String action, int goalXPos, int goalYPos, int sizeOfBoard){
        // Copy board
        //update X,Y, h, f and board

        int currX,currY;
        int newX=0;
        int newY= 0;
        int newG=0;
        double newH, newF;
        String[][] newBoard = new String[sizeOfBoard][sizeOfBoard];
        String actionMade="";
        State resultingState;
        
        //Copy board
        for(int i=0; i<sizeOfBoard; i++){
            for(int j=0; j<sizeOfBoard; j++){
                newBoard[i][j] = state.getCurrBoard()[i][j];
            }
        }
        currX = state.getXPos();
        currY = state.getYPos();

        if (action.equals("L")){
            newBoard[currY][currX-1] = "P";
            newBoard[currY][currX] = "v";
            newY = currY;
            newX = currX-1;
            actionMade = "L";
        }

        if(action.equals("R")){
            newBoard[currY][currX+1] = "P";
            newBoard[currY][currX] = "v";
            newY = currY;
            newX = currX+1;
            actionMade = "R";
        }

        if(action.equals("U")){
            newBoard[currY-1][currX] = "P";
            newBoard[currY][currX] = "v";
            newY = currY-1;
            newX = currX;
            actionMade = "U";
        }

        if(action.equals("D")){
            newBoard[currY+1][currX] = "P";
            newBoard[currY][currX] = "v";
            newY = currY+1;
            newX = currX;
            actionMade = "D";
        }
        newG = state.getG()+1;
        newH = computeH(newX, newY, goalXPos, goalYPos);
        resultingState = new State(newBoard, sizeOfBoard, newX, newY, newG, newH, state, actionMade);

        //Update F value          
        newF = newH + newG;
       
        resultingState.setF(newF);

        return resultingState;
    }
    private static ArrayList<String> Actions(State state, int sizeOfBoard){
        int X = state.getXPos();
        int Y = state.getYPos();
      
        ArrayList<String> actions = new ArrayList<String>();

        //MOVE UP
        if(Y > 0){
           
            if(state.getCurrBoard()[Y-1][X].equals("t") || state.getCurrBoard()[Y-1][X].equals("G") )
                actions.add("U");
        }

        //MOVE DOWN
        if(Y < sizeOfBoard-1){
         
            if(state.getCurrBoard()[Y+1][X].equals("t") || state.getCurrBoard()[Y+1][X].equals("G"))
                actions.add("D");
        }
          
        // MOVE LEFT
        if(X > 0){   
           
            if(state.getCurrBoard()[Y][X-1].equals("t") || state.getCurrBoard()[Y][X-1].equals("G"))
                actions.add("L");
        }

        //MOVE RIGHT
        if(X < sizeOfBoard-1){
          
            if(state.getCurrBoard()[Y][X+1].equals("t") || state.getCurrBoard()[Y][X+1].equals("G"))
                actions.add("R");
        }

        return actions;

    }

    private static boolean GoalTest(State state, int goalXPos, int goalYPos){
    	// checks the current state is a Goal or not
        if(state.getXPos() == goalXPos && state.getYPos() == goalYPos){
            return true;
        }
        return false;
    }    
}