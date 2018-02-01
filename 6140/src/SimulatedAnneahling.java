import java.io.PrintWriter;
import java.util.*;


/**
 * This class implement the simulated anneahling algorithm which is LS2
 * by computing a initial vertex cover and
 * then explore neighbours by computing the next solution and uses a cost and temperator factor to move through sampel space
 */
public class SimulatedAnneahling {
    // Construct initial vertex cover using greedy approach to choose a vertex with high degree
    public static ArrayList<Vertex> constructVC(Graph g) {
        ArrayList<Vertex> vertices = g.getTotalVertices();
        HashSet<Vertex> initialVC = new HashSet<>();
        ArrayList<Edge> edges = new ArrayList<>(g.getTotalEdges());
        Collections.sort(vertices, new VertexDegreeComparator());
        for (int i = 0; i < vertices.size() && edges.size() > 0; i++) {
            initialVC.add(vertices.get(i));
            edges = removeEdges(vertices.get(i), edges, g);
        }
        return new ArrayList<>(initialVC);
    }

    // remove edges
    public static ArrayList<Edge> removeEdges(Vertex v, ArrayList<Edge> edges, Graph g) {
        for (Edge e : g.getAdjacency().get(v)) {
            edges.remove(e);
            edges.remove(new Edge(e.getDestination(), e.getSource()));
        }
        return edges;
    }

    //function using SA algorithm to find a vertex cover
    public static ArrayList<Vertex> simulatedAnneahling(Graph g, int seed, long time, PrintWriter outputTrace) {
        ArrayList<Vertex> initialVC = constructVC(g);
//        System.out.println(initialVC.size());
        double temperature = 10000;
        double cooling = 0.99999;
        double tempBound = 0.00001;
        Random random = new Random();
        random.setSeed(seed);
        int currentSolutionCost = initialVC.size();
        long start = System.currentTimeMillis();
        long runningDuration = 0;
        long end = 0;
        int delta = 0;
        while (runningDuration < time) {
            ArrayList<Vertex> vertexCover = new ArrayList<>(initialVC);
            int removeIndex = random.nextInt(vertexCover.size());
            Vertex removeVertex = vertexCover.get(removeIndex);
            if (isVertexCover(g, removeVertex, vertexCover)) {
                vertexCover.remove(removeVertex);
                delta = vertexCover.size() - currentSolutionCost;
                double metropolis = Math.exp(-Math.abs(delta) / temperature);
                if ((delta < 0) || (currentSolutionCost > 0 && metropolis > random.nextDouble())) {
                    currentSolutionCost = vertexCover.size();
                    initialVC = vertexCover;
                }

                end = System.currentTimeMillis();
                runningDuration = end - start;
                outputTrace.print(runningDuration / 1000F + "," + initialVC.size() + "\n");

            }
            temperature = temperature * cooling;
            end = System.currentTimeMillis();
            runningDuration = end - start;

        }

        // Sort vertices in descending order of degree
        ArrayList<Vertex> vertexCoverSolution = new ArrayList<Vertex>(initialVC);
        Collections.sort(vertexCoverSolution, new Comparator<Vertex>() {
            @Override
            public int compare(Vertex o1, Vertex o2) {
                if (o1.getLabel() == o2.getLabel()) {
                    return 0;
                }
                if (o1.getLabel() < o2.getLabel()) return -1;
                return 1;
            }
        });


        return vertexCoverSolution;
    }

    //Check if teh solution is a vertex cover
    public static boolean isVertexCover(Graph g, Vertex v, ArrayList<Vertex> vertexCover) {
        for (Edge e : g.getAdjacency().get(v)) {
            if (!vertexCover.contains(e.getDestination())) {
                return false;
            }
        }
        return true;
    }

}
