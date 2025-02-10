package com.company;


import java.util.*;
import java.lang.*;

import static java.lang.Math.max;

public class Main {

    public static void main(String[] args) {

        playgame();

    }

    static List<Node> nodes = new ArrayList<>();

    public static boolean redstart;
    static boolean gameover;
    static boolean win;
    static boolean loose;
    static int entered_coordinate;
    static boolean Notvalid;


    public static boolean setstarter(){
        System.out.println("Type true for you to be the starter or false for the AI to be the starter : ");
        Scanner a=new Scanner(System.in);

        return a.nextBoolean();
    }

    public static void playgame() {


        redstart = setstarter();

        for (int i = 1; i <= 7; i++) {
            for (int j = 1; j <= 7; j++) {
                Node node = new Node(i, j);
                nodes.add(node);
            }
        }

        if(redstart) {
            for (Node node1 : nodes) {


/*

            if (node1.x == 2 && node1.y != 1 && node1.y != 7 && node1.y != 6 && node1.y != 2)
                node1.color = 'B';



            if (node1.y == 6 && node1.x != 1 && node1.x != 7 && node1.x != 2)
                node1.color = 'R';
            if (node1.y == 7 && node1.x == 1)
                node1.color = 'B';
            if (node1.y == 3 && node1.x == 5)
                node1.color = 'B';

 */




            }

            render();
        }
        if(!redstart){
            update();
            render();
        }

        while (!gameover) {
            gameloop();
        }

        if(win) {
            System.out.println("You Won!");
        }
        if(loose){
            System.out.println("You Lost!");
        }

    }

    public static int heuristic(List<Node> nodeslist){

        setNeighbors(nodeslist);
        int r = shortestpathRed(nodeslist);
        resetpath(nodeslist);
        int b = shortestpathBlue(nodeslist);
        resetpath(nodeslist);


        return b - r;
    }

    public static void resetpath(List<Node> nodeslist){
        for( Node node : nodeslist){
            node.setShortestpathtothisnode(1000);
            node.setVisited(false);
        }
    }
    public static int shortestpathBlue(List<Node> nodeslist){

        for (Node node1 : nodeslist) {
            if (node1.y == 1 && node1.color != 'R')
                if (node1.color == 'B') {
                    node1.shortestpathtothisnode = 0;
                } else node1.shortestpathtothisnode = 1;
        }
        boolean remains = true;
        while(remains){

            nodeslist.sort(Comparator.comparing(Node::getShortestpathtothisnode));
            for (Node node1 : nodeslist) {
                if (!node1.visited){
                    for (Node neighbornode : node1.neighbors) {
                        if (neighbornode.color != 'R' && !neighbornode.visited){

                            if(neighbornode.color == 'B') {
                                if (node1.shortestpathtothisnode < neighbornode.shortestpathtothisnode)
                                    neighbornode.shortestpathtothisnode = node1.shortestpathtothisnode;
                            }
                            else if(node1.shortestpathtothisnode + 1 < neighbornode.shortestpathtothisnode){
                                neighbornode.shortestpathtothisnode = node1.shortestpathtothisnode + 1;
                            }
                        }
                    }
                    node1.visited = true;
                    break;
                }
            }
            for (Node node1 : nodeslist) {
                if (!node1.visited){
                    remains = true;
                    break;
                }
                remains = false;
            }

        }
        int BestCaseWinBlue = 1000;

        for (Node node1 : nodeslist) {
            if (node1.y == 7){
                if(node1.shortestpathtothisnode < BestCaseWinBlue){
                    BestCaseWinBlue = node1.shortestpathtothisnode;
                }
            }
        }



        return BestCaseWinBlue;
    }
    public static int shortestpathRed(List<Node> nodeslist){

        for (Node node1 : nodeslist) {
            if (node1.x == 7 && node1.color != 'B')
                if (node1.color == 'R') {
                    node1.shortestpathtothisnode = 0;
                } else node1.shortestpathtothisnode = 1;
        }

        boolean remains = true;
        while(remains){

            nodeslist.sort(Comparator.comparing(Node::getShortestpathtothisnode));
            for (Node node1 : nodeslist) {
                if (!node1.visited){
                    for (Node neighbornode : node1.neighbors) {
                        if (neighbornode.color != 'B' && !neighbornode.visited){

                            if(neighbornode.color == 'R') {
                                if (node1.shortestpathtothisnode < neighbornode.shortestpathtothisnode)
                                    neighbornode.shortestpathtothisnode = node1.shortestpathtothisnode;
                            }
                            else if(node1.shortestpathtothisnode + 1 < neighbornode.shortestpathtothisnode){
                                neighbornode.shortestpathtothisnode = node1.shortestpathtothisnode + 1;
                            }
                        }
                    }
                    node1.visited = true;
                    break;
                }
            }
            for (Node node1 : nodeslist) {
                if (!node1.visited){
                    remains = true;
                    break;
                }
                remains = false;
            }

        }
        int BestCaseWinRed = 1000;

        for (Node node1 : nodeslist) {
            if (node1.x == 1){
                if(node1.shortestpathtothisnode < BestCaseWinRed){
                    BestCaseWinRed = node1.shortestpathtothisnode;
                }
            }
        }



        return BestCaseWinRed;
    }
    public static void setNeighbors(List<Node> nodeslist){

        for (Node node : nodeslist) {


                for (Node node1 : nodeslist) {
                    if (node1.x == node.x + 1 && node1.y == node.y - 1)
                        node.neighbors.add(node1);
                    else if (node1.x == node.x && node1.y == node.y - 1)
                        node.neighbors.add(node1);
                    else if (node1.x == node.x + 1 && node1.y == node.y)
                        node.neighbors.add(node1);
                    else if (node1.x == node.x - 1 && node1.y == node.y)
                        node.neighbors.add(node1);
                    else if (node1.x == node.x - 1 && node1.y == node.y + 1)
                        node.neighbors.add(node1);
                    else if (node1.x == node.x && node1.y == node.y + 1)
                        node.neighbors.add(node1);
                }
            }

        }

    public static void gameloop() {
        input();
        if(!Notvalid && !gameover) {
            update();
        }
        render();
    }
    public static void input(){

        System.out.println("\nEnter the Coordinate like 11 or 27 : ");
        Scanner scanner = new Scanner(System.in);
        entered_coordinate= scanner.nextInt();
        checkInput(entered_coordinate);

    }

    public static void checkInput(int input) {


        int x = Integer.parseInt(Integer.toString(input).substring(0, 1));
        int y = Integer.parseInt(Integer.toString(input).substring(1, 2));

        Notvalid = true;
        for(Node node : nodes){

            if(node.x == x && node.y == y){
                if(node.color != 'B' && node.color != 'R'){
                    node.color = 'R';
                    Notvalid = false;
                    break;
                }

            }

        }
        winCheck(nodes);



    }

    public static void update() {

        int turnBlue = -1;
        for(Node node : nodes){
            if(node.color != 'R' && node.color != 'B'){
                List<Node> nodeList = new ArrayList<>();
                copylist(nodes,nodeList);
                for(Node node1 : nodeList){
                    if(node1.x == node.x && node1.y == node.y){
                        node1.color = 'B';
                    }
                }
                int heu = NegaMax(nodeList,3,-1000,+1000,turnBlue);
                node.setHeuristicvalue(heu);

            }
        }


        nodes.sort(Comparator.comparing(Node::getHeuristicvalue).reversed());

        List<Node> bestHEU = new ArrayList<>();

        int heu1 = nodes.get(0).heuristicvalue;

        for(Node node : nodes){
           if(node.heuristicvalue >= heu1){
               bestHEU.add(node);
           }

        }

        Node newnode = bestHEU.get(new Random().nextInt(bestHEU.size()));


        for(Node node : nodes){
            if(node.x == newnode.x && node.y == newnode.y){
                node.color = 'B';
                System.out.println("Blue chose node : " + node.x + " " + node.y);
            }

        }
        bestHEU.clear();



        for(Node node : nodes){
            System.out.println("heuristic of the node " + node.x + node.y + " is : " + node.heuristicvalue);

        }
        //resetnodesHeuristic(nodes);

        winCheck(nodes);
    }
    public static void copylist(List<Node> source , List<Node> copy){
        for( Node node : source){
            Node newnode = new Node();
            newnode.color = node.color;
            newnode.x = node.x;
            newnode.y = node.y;

            copy.add(newnode);
        }
    }

    public static void resetnodesHeuristic(List<Node> nodeList){

        for( Node node : nodeList){
            node.setHeuristicvalue(-1000);
        }

    }
    public static int NegaMax(List<Node> nodeslist, int depth, double alpha, double beta, int turnBlue){

        if(depth == 0){
            return turnBlue * heuristic(nodeslist);
        }

        List<List<Node>> childlists = generatechildlists(nodeslist,turnBlue);

        int value = -1000;

        for(List<Node> childlist : childlists){
            value = max(value, - NegaMax(childlist,depth - 1,- beta ,- alpha,-turnBlue));
            alpha = max(alpha,value);
            if(alpha >= beta){
                break;
            }
        }
        return value;
    }

    public static List<List<Node>> generatechildlists(List<Node> parentlist, int turnBlue){
        List<List<Node>> childlists = new ArrayList<>();

        for(Node node : parentlist){
            if(node.color != 'R' && node.color != 'B'){
                List<Node> nodeList = new ArrayList<>();
                copylist(parentlist,nodeList);

                for(Node node1 : nodeList){
                    if(node1.x == node.x && node1.y == node.y){
                        if(turnBlue == 1) {
                            node1.color = 'B';
                            break;
                        }
                        else if(turnBlue == -1){
                            node1.color = 'R';
                            break;
                        }
                    }
                }
                childlists.add(nodeList);

            }
        }



        return childlists;

    }

    public static void winCheck(List<Node> nodes){
        setNeighbors(nodes);
        int r = shortestpathRed(nodes);
        resetpath(nodes);
        int b = shortestpathBlue(nodes);
        resetpath(nodes);
        if(r==0||b==0){
            gameover = true;
            if(r==0){
                win = true;
            }
            if(b==0){
                loose = true;
            }
        }

    }

    public static void render() {

        toppannel();
        printboard();
        bottompannel();
    }

    public static void toppannel(){
        //to simulate the clear screen :|
        for (int i = 0; i < 50; ++i) System.out.println();
        System.out.println("{The Game of \"HEX\"}");


        System.out.println();
        if (Notvalid)
        {
            System.out.println("<<Command was not valid, try again!>>");
            Notvalid = false;
        }
    }
    public static void printboard(){

        int spacecounter = 0;
        for (int i = 1; i < 8; i++) {
            for (int j = 1; j < 8; j++) {

                if (i == 1 && j ==1) {
                    System.out.print(" ");
                    System.out.print(" ");

                    for (int k = 1; k <= 7; k++) {

                        System.out.print(k + " ");
                        if(k==7)
                            System.out.println();
                    }
                }

                if(j==1) {
                    for (int k = 0; k <= spacecounter; k++)
                        System.out.print(" ");
                }

                if (j == 1) {
                    System.out.print(i + " ");
                }

                Node foundnode = new Node();
                for (Node node1 : nodes) {
                    if (node1.y == j && node1.x == i) {
                        foundnode = node1;
                    }
                }

                if(foundnode.color == 'B' || foundnode.color == 'R' ){

                    System.out.print(foundnode.color + " ");

                }
                else {
                    System.out.print("- ");
                }



                if (j == 7) {
                    System.out.println(i);
                }
                if (i == 7 && j ==7) {
                    for (int k = 1; k <= 10; k++)
                        System.out.print(" ");

                    for (int k = 1; k <= 7; k++) {

                        System.out.print(k + " ");
                        if(k==7)
                            System.out.println();
                    }
                }
            }
            spacecounter++;
        }
    }

    public static void bottompannel() {

        for (int k = 1; k <= 50; k++)
            System.out.print("-");
        System.out.println();

    }

}