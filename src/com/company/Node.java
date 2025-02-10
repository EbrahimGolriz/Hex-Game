package com.company;

import java.util.ArrayList;
import java.util.List;

public class Node {
    char color;
    int x;
    int y;
    int shortestpathtothisnode = 1000;
    boolean visited;
    int heuristicvalue = -1000;

    public int getHeuristicvalue() {
        return heuristicvalue;
    }

    public void setHeuristicvalue(int heuristicvalue) {
        this.heuristicvalue = heuristicvalue;
    }

    List<Node> neighbors = new ArrayList<>();

    public Node() {
    }

    public int getShortestpathtothisnode() {
        return shortestpathtothisnode;
    }

    public void setShortestpathtothisnode(int shortestpathtothisnode) {
        this.shortestpathtothisnode = shortestpathtothisnode;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
