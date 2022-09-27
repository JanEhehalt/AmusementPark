package com.mygdx.game.pathfinding;

import java.util.*;

class ShortestPath{

    Node start;
    Node end;

    ShortestPath(Node start, Node end){
          this.start = start;
          this.end = end;
      }

    public List<Node> bfs(){
        Queue<Node> queue = new LinkedList<>();// queue to store nodes to be visited along the breadth

        start.visited = true;   // mark source node as visitedi
        queue.add(start); // push src node to queue

        while(!queue.isEmpty()){
        Node current_node = queue.poll();// traverse all nodes along the breadth
        // traverse along the node's breadth
        for(Node node: current_node.neighbours){
            if(!node.visited){
                node.visited =true;// // mark it visited
                queue.add(node);
                node.prev = current_node;
                if(node==end){
                    queue.clear();
                    break;
                }
            }
        }
        }
        return trace_route();
        }

// function that computes the shortest path and prints it
    private List<Node> trace_route(){
        Node node = end;
        List<Node> route = new ArrayList<>();
        //Loop until node is null to reach start node
        //because start.prev == null
        while(node != null){
            route.add(node);
            node = node.prev;
        }
        //Reverse the route - bring start to the front
        Collections.reverse(route);
        //Output the route
        return route;
    }
}