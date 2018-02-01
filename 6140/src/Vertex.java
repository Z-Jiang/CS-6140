import java.util.ArrayList;
import java.util.List;


public class Vertex {
    int label;
    private int edgeCount;
    private boolean vertexHasBeenVisited;
    private List<Vertex> neighbors;

    /**
     * Constructor for vertex class
     *
     * @param label: vertex number
     * @param edgeCount: number of edges that one vertex has
     * @param vertexHasBeenVisited: whether or not a vertex has been visited
     * @param neighbors： adjacentVertices list of all neighbors
     */

    public Vertex(int label) {
        this.label = label;
        int edgeCount = -1;
        boolean vertexHasBeenVisited = false;
        this.neighbors = new ArrayList<>();
    }

    /**
     * Check if one vertex euqal to the other
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Vertex) {
            Vertex v = (Vertex) o;
            return (this.label == v.label);
        } else {
            return false;
        }
    }


    /**
     * @Override
     */
    public int hashCode() {
        return this.label;
    }

    /**
     * Obtain the vertex ID
     *
     * @return label
     * An integer representative of a given vertex
     */
    public int getLabel() {
        return label;
    }


    /**
     * Obtain number of edges that has been connected to one particular vertex
     *
     * @return edgeCount
     * number of edges that one vertex have
     */
    public int getEdgeCount() {
        return edgeCount;
    }


    /**
     * Obtain the status of a vertex on whether or not it has been visited
     */
    public boolean hasVisited() {
        return vertexHasBeenVisited;
    }


    /**
     * Set status of whether a vertex has been visited
     *
     * @param visited decide whether or not a vertex has been visited
     */
    public void setVisited(boolean visited) {
        this.vertexHasBeenVisited = visited;
    }


    /**
     * Set the number of edges that is connected to one particular vertex
     *
     * @param edgeCount number of edges that one vertex have
     */
    public void setEdgeCount(int edgeCount) {
        this.edgeCount = edgeCount;
    }


    /**
     * Set the vertex neighbors of one particular vertex
     */
    public void setNeighbors(List<Vertex> neighbors) {
        this.neighbors = neighbors;
    }

    /**
     * Obtain the vertex neighbors of one particular vertex
     *
     * @return neighbors
     * a set of vertices that is connect to one particular vertex
     */
    public List<Vertex> getNeighbors() {
        return neighbors;
    }
}
