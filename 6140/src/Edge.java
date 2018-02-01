public class Edge {

    private Vertex v; // starting vertex
    private Vertex w; // ending vertex
    private boolean edgeHasBeenVisited;


    /**
     * Initializes an edge between vertices {@code v} and {@code w}
     *
     * @param v one vertex
     * @param w the other vertex
     * @throws IllegalArgumentException if either {@code v} or {@code w} is a negative integer
     * @Mark the edge as not been visited
     */
    public Edge(Vertex v, Vertex w) {
        if (v.label < 0)
            throw new IllegalArgumentException("vertex index must be a nonnegative integer");
        if (w.label < 0)
            throw new IllegalArgumentException("vertex index must be a nonnegative integer");
        this.v = v;
        this.w = w;
        edgeHasBeenVisited = false;
    }

    /**
     * Get source (v) of a give edge
     *
     * @return source
     */
    public Vertex getSource() {
        return v;
    }

    /**
     * Get destination (w) of a give edge
     *
     * @return destination
     */
    public Vertex getDestination() {
        return w;
    }

    /**
     * Obatain status of whether an edge has been visited
     *
     * @return edgeHasBeenVisited
     */
    public boolean hasVisited() {
        return edgeHasBeenVisited;
    }

    /**
     * Set status of whether an edge has been visited
     *
     * @param visited decide whether or not an edge has been visited
     */
    public void setVisited(boolean visited) {
        this.edgeHasBeenVisited = visited;
    }


    /*
     * @Override
     */
    public String toString() {
        return "<" + v.getLabel() + ", " + w.getLabel() + ">";
    }

    /**
     * Returns the other vertex of this edge that is different from the given
     * vertex.
     *
     * @param vertex one end point of this edge
     * @return the other end point of this edge
     * @throws IllegalArgumentException if the vertex is not one of the end points of this edge
     */
    public Vertex other(Vertex vertex) {
        if (vertex.equals(v))
            return w;
        else if (vertex.equals(w))
            return v;
        else
            throw new IllegalArgumentException("Illegal endpoint");
    }

    /**
     * Compares two edges to see if they are the same edge.
     * If the endpoints of two edges has same vertex id, then these two edges are the same
     *
     * @param that Object o
     * @return a boolean. If true, then two edges are the same edge, otherwise false
     */

    public boolean sameEdge(Edge o) {
        int v1 = this.getSource().getLabel();
        int v2 = this.getDestination().getLabel();
        int v3 = o.getSource().getLabel();
        int v4 = o.getDestination().getLabel();
        return (v1 == v3 && v2 == v4) || (v1 == v4 && v2 == v3);
    }


    public boolean equals(Object o) {
        if (o instanceof Edge) {
            Edge e = (Edge) o;
            return sameEdge(e);
        }
        return false;
    }

    /*
     * @Override
     */
    public int hashCode() {
        return this.getDestination().hashCode() + this.getSource().hashCode();
    }
}
