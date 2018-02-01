
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Queue;
import java.util.PriorityQueue;

public class Graph {
    private HashMap<Vertex, ArrayList<Edge>> adjacency;
    private Map<Vertex, List<Vertex>> edges;
    private ArrayList<Vertex> totalVertices;
    private ArrayList<Edge> totalEdges;
    private Queue<Vertex> vertices;
    private int numberOfEdges;
    private int numberOfVertices;

    /**
     * Creates an Graph with default values.
     */
    public Graph() {
        adjacency = new HashMap<>();
        totalVertices = new ArrayList<>();
        totalEdges = new ArrayList<>();
        this.numberOfVertices = 0;
        this.numberOfEdges = 0;
    }

    /**
     * Creates an Graph with an existing graph.
     *
     * @param g the existing graph.
     */
    public Graph(Graph g) {
        this.numberOfVertices = g.numberOfVertices;
        this.numberOfEdges = g.numberOfEdges;
        totalEdges = g.getTotalEdges();
        totalVertices = g.getTotalVertices();
        adjacency = g.getAdjacency();
    }

    /**
     * Creates an Graph with given oaramaters.
     *
     * @param: numberOfVertices the total number of vertices in the graph.
     * @param: numberOfEdges the total number of edges in the graph.
     * @param: vertexSet the set of all the vertices of the graph.
     * @param: eSet the set of all the edges of the graph.
     */
    public Graph(int numberOfVertices, int numberOfEdges, ArrayList<Vertex> vertexSet, ArrayList<Edge> eSet) {
        this.numberOfVertices = numberOfVertices;
        this.numberOfEdges = numberOfEdges;
        totalEdges = new ArrayList<Edge>();
        totalVertices = new ArrayList<Vertex>();
        adjacency = new HashMap<Vertex, ArrayList<Edge>>();
    }

    /**
     * Creates an Graph with given oaramaters.
     *
     * @param: numberOfVertices the total number of vertices in the graph.
     * @param: numberOfEdges the total number of edges in the graph.
     * @param: vertexSet the set of all the vertices of the graph.
     * @param: edges the hashmap that represents all the vertices and their neighbors in the graph.
     */
    public Graph(int numberOfVertices, int numberOfEdges, Queue<Vertex> vertices, Map<Vertex, List<Vertex>> edges) {
        this.numberOfVertices = numberOfVertices;
        this.numberOfEdges = numberOfEdges;
        this.vertices = vertices;
        this.edges = edges;
    }

    /**
     * @return a hashmap representing all the vertives and their neighboring vertices.
     */
    public Map<Vertex, List<Vertex>> getEdges() {
        return edges;
    }

    /**
     * @param: edges to set.
     */
    public void setEdges(Map<Vertex, List<Vertex>> edges) {
        this.edges = edges;
    }

    /**
     * @return all the vertices of the graph in a queue form.
     */
    public Queue<Vertex> getVertices() {
        return vertices;
    }

    /**
     * @param: vertices to set.
     */
    public void setVertices(Queue<Vertex> vertices) {
        this.vertices = vertices;
    }

    /**
     * create and add an edge between two vertices to the graph.
     *
     * @param v: the vertex of an edge.
     * @param w: the other vertex of the edge.
     */
    public void addEdge(Vertex v, Vertex w) {
        ArrayList<Edge> outgoingEdges = adjacency.get(v);
        if (outgoingEdges != null) {
            Edge edge = new Edge(v, w);
            outgoingEdges.add(edge);
            v.setEdgeCount(outgoingEdges.size());
            adjacency.put(v, outgoingEdges);
            totalEdges.add(edge);

        } else {
            ArrayList<Edge> priorityQueue = new ArrayList<>();
            Edge edge = new Edge(v, w);
            priorityQueue.add(edge);
            v.setEdgeCount(1);
            adjacency.put(v, priorityQueue);
            totalEdges.add(edge);
        }
        totalVertices.add(v);
        totalVertices.add(w);
    }

    /**
     * add an edge to the graph.
     *
     * @param: e the existing edge.
     */
    public void addEdge(Edge e) {
        addEdge(e.getSource(), e.getDestination());
    }

    /**
     * override toString() method.
     */
    @Override
    public String toString() {
        return "Graph{" + "edges=" + adjacency + '}';
    }

    /**
     * @return a hashmap representing all the vertives and their edges.
     */
    public HashMap<Vertex, ArrayList<Edge>> getAdjacency() {
        return adjacency;
    }

    /**
     * @param: adjacency to set.
     */
    public void setAdjacency(HashMap<Vertex, ArrayList<Edge>> adjacency) {
        this.adjacency = adjacency;
    }

    /**
     * @return all the vertices of the graph.
     */
    public ArrayList<Vertex> getTotalVertices() {
        return totalVertices;
    }

    /**
     * @param: totalVertices to set.
     */
    public void setTotalVertices(ArrayList<Vertex> totalVertices) {
        this.totalVertices = totalVertices;
    }

    /**
     * @return all the edges of the graph.
     */
    public ArrayList<Edge> getTotalEdges() {
        return totalEdges;
    }

    /**
     * @param: totalEdges to set.
     */
    public void setTotalEdges(ArrayList<Edge> totalEdges) {
        this.totalEdges = totalEdges;
    }

    /**
     * @return the total number of edges of the graph.
     */
    public int getNumberOfEdges() {
        return numberOfEdges;
    }

    /**
     * @param: numberOfEdges to set.
     */
    public void setNumberOfEdges(int numberOfEdges) {
        this.numberOfEdges = numberOfEdges;
    }

    /**
     * @return the total number of vertices of the graph.
     */
    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    /**
     * @param: numberOfVertices to set.
     */
    public void setNumberOfVertices(int numberOfVertices) {
        this.numberOfVertices = numberOfVertices;
    }

}
