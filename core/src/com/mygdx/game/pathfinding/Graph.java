package com.mygdx.game.pathfinding;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.MapElement;
import com.mygdx.game.MapElementData;
import com.mygdx.game.World;
import com.mygdx.game.mapElements.Game;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Translation of paths in map into graph
 * -> 'easy' pathfinding
 * -> breaks down pathfinding problem to its core elements
 * 
 * Class uses basic GraphTheory Methods
 */

public class Graph {
    
    // renderer for debugging graph
    private ShapeRenderer renderer = new ShapeRenderer();
    
    // Graph
    private int[][] edges;
    private Node[] nodes;
    
    private MapElement[][] map;
    
    public Graph(Node[] nodes, int[][] edges, MapElement[][] basis){
        this.nodes = nodes;
        this.edges = edges;
        this.map = basis;
    }

    public void printGraph(){
        for(Node n : nodes){
            n.printNode();
        }
    }
    
    public void draw(Batch batch){
        // Drawing the graph edges as red lines
        getRenderer().setProjectionMatrix(batch.getProjectionMatrix());
        getRenderer().begin(ShapeRenderer.ShapeType.Line);
        getRenderer().setColor(Color.RED);
        // iterating over all the edges
        for(int i = 0; i < getNodes().length; i++){
            for(int x = 0; x < getEdges().length; x++){
                if(getEdges()[x][i] == 1){
                    getRenderer().line(getNodes()[i].getX() * World.TILE_WIDTH + World.TILE_WIDTH/2,getNodes()[i].getY() * World.TILE_HEIGHT+World.TILE_HEIGHT/2, 
                        getNodes()[x].getX() * World.TILE_WIDTH+World.TILE_WIDTH/2, getNodes()[x].getY() * World.TILE_HEIGHT+World.TILE_HEIGHT/2);
                }
            }
        }
        
        getRenderer().end();
        
    }
    
    public static Graph generateGraphFromMap(MapElement[][] map){
        /**
         *  We take the map (contains mapelements like games)
         *  We create a graph from the map
         *  The graph contains every part of the path as a node
         *  the edge of two path parts is a edge then
         *  The adjacency matrix contains the edges
         */
        int nodeAmount = 0;
        
        // counting how many nodes we will have in the graph
        for(int x = 0; x < map.length; x++){
            for(int y = 0; y < map[0].length; y++){
                // only MapElements with Id higher than 9 will be added to the graph (-> no BuildingOversize (-2))
                // and the entrance will be added to the graph. (checking entrance extra, because entrance would be -2)
                if(map[x][y].getId() >= 10 || ( x == World.ENTRANCE_X+2 && y == World.ENTRANCE_Y)){
                    nodeAmount++;
                }
            }
        }
        
        Node[] nodes = new Node[nodeAmount];
        int[][] edges = new int[nodeAmount][nodeAmount];
        
        // external counter
        // could also use x from for loop
        int i = 0;
        
        // Finally adding nodes and their edges
        for(int x = 0; x < map.length; x++){
            for(int y = 0; y < map[0].length; y++){
                // only MapElements with Id higher than 9 will be added to the graph (-> no BuildingOversize (-2))
                // and the entrance will be added to the graph. (checking entrance extra, because entrance would be -2)
                if(map[x][y].getId() >= 10 || (x == World.ENTRANCE_X+2 && y == World.ENTRANCE_Y)){
                    // adding node to graph
                    nodes[i] = new Node(x,y, i, map[x][y].getId());
                    for(int j = 0; j < nodes.length; j++){
                        if(nodes[j] != null){
                            // Adding the edges to every node
                            if((x-1 == nodes[j].getX() && y == nodes[j].getY()) || 
                               (x+1 == nodes[j].getX() && y == nodes[j].getY()) ||
                               (x == nodes[j].getX() && y-1 == nodes[j].getY()) ||
                               (x == nodes[j].getX() && y+1 == nodes[j].getY()) ){
                                edges[i][j] = 1;
                                edges[j][i] = 1;

                            }
                        }
                    }
                    i++;
                }
            }
        }
        for(Node n : nodes){
            for(Node j : nodes){
                if(n != j){
                    if((n.getX()-1 == j.getX() && n.getY() == j.getY()) ||
                        (n.getX()+1 == j.getX() && n.getY() == j.getY()) ||
                        (n.getX() == j.getX() && n.getY()-1 == j.getY()) ||
                        (n.getX() == j.getX() && n.getY()+1 == j.getY()) ){
                        n.addNeighbour(j);
                    }
                }
            }
        }

        return new Graph(nodes, edges, map);
    }
    
    // returns Node which has position x and y from nodes Array
    public Node getNodeWith(int x, int y){
        for(int i = 0; i < getNodes().length; i++){
            if(getNodes()[i].getX() == x && getNodes()[i].getY() == y){
                return getNodes()[i];
            }
        }
        return null;
    }
    
    /** Recursive Path finding algorithm
     * 
     * The path basically is a stack of the different stept we have to take
     *  in order to reach the goal node
     * 
     * We go to the first node (start)
     * if start == goal
     *      return path with only start as point
     * else
     *      For each Node "next" the start node shares a edge
     *          call findPath with next as Start
     *              if the call returns null (-> there was no path from this node found)
     *                  continue with next edge
     *              if call returns a path (-> there was a path found)
     *                  return that path
     *      If we end up at a node that has no more edge that aren't visited yet
     *          this node removes itself from the path stack
     *          and returns null (-> because no way found here)
     * 
     *
    public ArrayList<Node> findPath(Node start, Node goal, ArrayList<Node> path, boolean[] visited){
        
        if(start == null || goal == null || path == null || visited == null){
            return null;
        }
        
        path.add(start);
        visited[start.getI()] = true;
        if(start == goal){
            if(path.size() > 15 && !isInEntrance(goal.getX(), goal.getY())){
                return null;
            }
            return path;
        }
        for(int i = 0; i < getEdges().length; i++){
            if(getEdges()[i][start.getI()] == 1 && !visited[i]){
                // pathfinding will only allow to step on entrance node or paths or on the goal node (restaurant e.g.)
                if( isInEntrance(getNodes()[i].getX(), getNodes()[i].getY()) ||
                    (getNodes()[i].getBuildingId() >= 30 && getNodes()[i].getBuildingId() <= 39) || getNodes()[i].getBuildingId() == goal.getBuildingId()
                        || isInEntrance(goal.getX(), goal.getY())
                    ){
                    ArrayList<Node> temp = findPath(getNodes()[i], goal, path, visited);
                    if(temp == null){
                    }
                    else{
                        return temp;
                    }
                }
            }
        }
        path.remove(start);
        return null;
    }
    */

    public ArrayList<Node> findPath(Node start, Node goal, ArrayList<Node> path, boolean[] visited){
        if(start == null || goal == null || path == null || visited == null){
            return null;
        }

        return (ArrayList<Node>) new ShortestPath(start, goal).bfs();
    }

    public ArrayList<Node> getAdj(Node n, Node goal){
        ArrayList<Node> adj = new ArrayList<>();
        adj.add(getNodeWith(n.getX()+1, n.getY()));
        adj.add(getNodeWith(n.getX()-1, n.getY()));
        adj.add(getNodeWith(n.getX(), n.getY()+1));
        adj.add(getNodeWith(n.getX(), n.getY()-1));

        for(Node i : adj){
            if(isInEntrance(i.getX(), i.getY()) || (i.getBuildingId() >= 30 && i.getBuildingId() <= 39)
                    || i.getBuildingId() == goal.getBuildingId() || isInEntrance(goal.getX(), goal.getY())
            ){

            }
            else{
                adj.remove(i);
            }
        }

        return adj;
    }

    // just for shortening the check whether the x and y coordinates are the entrance
    public boolean isInEntrance(int x, int y){
        if(x >= World.ENTRANCE_X && x < World.ENTRANCE_X + MapElementData.buildingWidth[40] &&
            y >= World.ENTRANCE_Y && y < World.ENTRANCE_Y + MapElementData.buildingHeight[40]){
            return true;
        }
        return false;
    }
    
    // checks a paths and returns false if the structure of the graph has changes, so that the
    // old path can't be followed any more
    public boolean pathValid(ArrayList<Node> path){
        for(Node n : path){
            // either when one node doesn't exist any more or its Id has changes, so there is standing another building now
            if(getNodeWith(n.getX(), n.getY()) == null || getNodeWith(n.getX(), n.getY()).getBuildingId() != n.getBuildingId()){
                return false;
            }
        }
        return true;
    }
    
    public Node findViableTrashcan(int xGrid, int yGrid){
        for(Node n : nodes){
            if(n.getBuildingId() == 32){
                ArrayList<Node> path = findPath(getNodeWith(xGrid, yGrid), n, new ArrayList<Node>(), new boolean[this.nodes.length]);
                if(path != null && path.size() < 10){
                    return n;
                }
            }
        }
        return null;
    }
    
    // it just returns a random node from the graph which is reachable for the player
    public Node randomGoal(int xGrid, int yGrid){
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.addAll((Arrays.asList(this.nodes)));
        Collections.shuffle(nodes);
        for(Node n : nodes){
            if(findPath(getNodeWith(xGrid, yGrid), n, new ArrayList<Node>(), new boolean[this.nodes.length]) != null){
                if(n.getX() != World.ENTRANCE_X && n.getY() != World.ENTRANCE_Y){
                    return n;
                }
            }
        }
        return null;
    }
    
    // this methods returns a node which has a certain id
    // is for guests to find a certain building
    public Node findId(int id, int xGrid, int yGrid){
        // looking for entrance?
        if(id == 40){
            return getNodeWith(World.ENTRANCE_X+2, World.ENTRANCE_Y);
        }
        
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.addAll((Arrays.asList(this.nodes)));
        Collections.shuffle(nodes);
        // just iterating over all the nodes and checking if we found a building with the id
        for(int i = 0; i < nodes.size(); i++){
            if(nodes.get(i).getBuildingId() == id){
                if(map[nodes.get(i).getX()][nodes.get(i).getY()] instanceof Game){
                    if(((Game)map[nodes.get(i).getX()][nodes.get(i).getY()]).isBroke()) continue;
                }
                return nodes.get(i);
            }
        }
        return null;
    }
    
    public Node findBrokeRide(){
        
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.addAll((Arrays.asList(this.nodes)));
        Collections.shuffle(nodes);
        // just iterating over all the nodes and checking if we found a broke game
        for(int i = 0; i < nodes.size(); i++){
            if(map[nodes.get(i).getX()][nodes.get(i).getY()] instanceof Game){
                if(((Game)map[nodes.get(i).getX()][nodes.get(i).getY()]).isBroke()){ 
                    return nodes.get(i);
                }
            }
        }
        return null;
    }
    
    // gets a path from an guest (e.g.) and just draws it as a green line
    public void drawPath(ArrayList<Node> path){
        if(path != null){
            getRenderer().begin(ShapeRenderer.ShapeType.Line);
            getRenderer().setColor(Color.GREEN);
            for(int i = 0; i < path.size(); i++){
                if(i < path.size()-1){
                    getRenderer().line(path.get(i).getX() * World.TILE_WIDTH + World.TILE_WIDTH/2,path.get(i).getY() * World.TILE_HEIGHT+World.TILE_HEIGHT/2, 
                                path.get(i+1).getX() * World.TILE_WIDTH+World.TILE_WIDTH/2, path.get(i+1).getY() * World.TILE_HEIGHT+World.TILE_HEIGHT/2);
                }
            }
            getRenderer().end();
        }
    }

    /**
     * GETTERS AND SETTERS
     */

    /**
     * @return the renderer
     */
    public ShapeRenderer getRenderer() {
        return renderer;
    }

    /**
     * @param renderer the renderer to set
     */
    public void setRenderer(ShapeRenderer renderer) {
        this.renderer = renderer;
    }

    /**
     * @return the edges
     */
    public int[][] getEdges() {
        return edges;
    }

    /**
     * @param edges the edges to set
     */
    public void setEdges(int[][] edges) {
        this.edges = edges;
    }

    /**
     * @return the nodes
     */
    public Node[] getNodes() {
        return nodes;
    }

    /**
     * @param nodes the nodes to set
     */
    public void setNodes(Node[] nodes) {
        this.nodes = nodes;
    }
    
    
}
