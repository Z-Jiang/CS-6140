import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;


public class Approximation {

    /**
     * This function finds the MVC of the given graph
     * using approximation algorithm.
     *
     * @param graph, the graph whose MVC needs to be found.
     * @return an ArrayList of the MVC.
     */
    public List<Vertex> findSet(Graph graph) {
        List<Vertex> res = new ArrayList<>();
        if (graph == null) {
            return res;
        }
        Map<Vertex, List<Vertex>> edges = graph.getEdges();
        Queue<Vertex> vertices = graph.getVertices();


        while (!edges.isEmpty() && !vertices.isEmpty()) {
            Vertex current = vertices.poll();
            res.add(current);
            while (current.getNeighbors().size() != 0) {
                Vertex neighbor = current.getNeighbors().get(0);
                // removing two corresponding edges

                edges.get(current).remove(neighbor);
                if (edges.get(neighbor).size() != 0) {
                    edges.get(neighbor).remove(current);
                }
                current.getNeighbors().remove(neighbor);
                neighbor.getNeighbors().remove(current);

                if (edges.get(current).size() == 0) {
                    edges.remove(current);

                }
                if (edges.get(neighbor).size() == 0) {
                    edges.remove(neighbor);
                }

                // updating neighbor nodes' degree
                if (vertices.remove(neighbor)) {
                    neighbor.setEdgeCount(neighbor.getEdgeCount() - 1);
                    if (neighbor.getEdgeCount() != 0) vertices.offer(neighbor);
                }

            }
        }

        return res;

    }

}
