import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class BranchAndBound {
    private Graph graph;
    private long cutoffTime;
    private long duration;
    private long startTime;
    private int upperBound;

    private ArrayList<Vertex> coveredVertices;
    private ArrayList<Vertex> uncoveredVertices;
    private ArrayList<Edge> coveredEdges;
    private ArrayList<Edge> uncoveredEdges;
    private HashSet<Edge> uncoveredEdgesDirected;
    private ArrayList<Edge> uncoveredEdgesDirectedList;
    private ArrayList<Vertex> candidateList;
    private PrintWriter output;


    /**
     * Construct an new Branch and Bound class
     *
     * @param g      originally parsed graph
     * @param time   cut off time defined by users
     * @param output trace file
     *               <p>
     *               This construct construct a new graph based on the parsed graph
     *               Without duplicated counted edges
     */
    public BranchAndBound(Graph g, long time, PrintWriter outputTrace) {
        cutoffTime = time;
        output = outputTrace;
        startTime = System.currentTimeMillis();
        this.graph = new Graph(g);
        this.upperBound = twoApprox(new Configuration(new HashSet<>(), new HashSet<>(),
                new HashSet<>(this.graph.getTotalVertices()), new HashSet<>(this.graph.getTotalEdges()))).size();
        coveredVertices = new ArrayList<Vertex>();
        uncoveredVertices = g.getTotalVertices();
        coveredEdges = new ArrayList<Edge>();
        uncoveredEdges = g.getTotalEdges();
        uncoveredEdgesDirected = new HashSet<Edge>();

        for (Edge e : uncoveredEdges) {
            uncoveredEdgesDirected.add(e);
        }
        uncoveredEdgesDirectedList = new ArrayList<Edge>(uncoveredEdgesDirected);
        this.graph.setTotalVertices(g.getTotalVertices());
        this.graph.setTotalEdges(uncoveredEdgesDirectedList);
        this.graph.setNumberOfEdges(uncoveredEdgesDirectedList.size());
        this.graph.setNumberOfVertices(g.getNumberOfVertices());

    }


    /**
     * Private class for storing partical graph
     */
    private class Configuration {
        public final Set<Vertex> coveredVertices;
        public final Set<Edge> coveredEdges;
        public final Set<Vertex> uncoveredVertices;
        public final Set<Edge> uncoveredEdges;

        public Configuration(Set<Vertex> cvs, Set<Edge> ces, Set<Vertex> uvs, Set<Edge> ues) {
            this.coveredVertices = cvs;
            this.coveredEdges = ces;
            this.uncoveredVertices = uvs;
            this.uncoveredEdges = ues;
        }

        public Set<Edge> getUnCoveredEdges() {
            return this.uncoveredEdges;
        }

        public boolean isCover() {
            return this.uncoveredEdges.size() == 0;
        }


        /**
         * Switch status of vertex from unused to used
         *
         * @param v Initially picked vertex
         *          <p>
         *          Move the picked vertex from uncoveredVertices set
         *          Add the picked vertex to coveredVertices set
         *          Move all the edges which are connected to the selected vertex from uncoveredEdges set
         *          Add the same edge set into coveredEdge set
         * @return original configuration
         */
        private Configuration useVertex(Vertex v) {
            this.uncoveredVertices.remove(v);
            this.coveredVertices.add(v);
            for (Edge e : BranchAndBound.this.graph.getAdjacency().get(v)) {
                if (this.uncoveredEdges.contains(e)) {
                    this.uncoveredEdges.remove(e);
                }
                this.coveredEdges.add(e);
            }
            return this;
        }

        /**
         * Get configuration of subgraph with selected vertex
         *
         * @return left configuration
         */
        public Configuration getLeftConfig(Vertex v) {
            Configuration configToReturn = this.configCopy();
            configToReturn.useVertex(v);
            return configToReturn;
        }


        /**
         * Get configuration of subgraph without selected vertex
         *
         * @return right configuration
         */
        public Configuration getRightConfig(Vertex v) {
            Configuration configToReturn = this.configCopy();
            for (Edge e : BranchAndBound.this.graph.getAdjacency().get(v)) {
                Vertex z = e.other(v);
                configToReturn.useVertex(z);
            }
            return configToReturn;
        }


        /**
         * Sort all uncoveredVertices decendingly according to their connecting degree
         *
         * @return a vertex with highest degree among all uncovered vertices
         */
        public Vertex getVertexWithHighestDegree() {
            Vertex ret = null;
            int highestDegree = -1;
            for (Vertex v : this.uncoveredVertices) {
                int numUncoveredVertices = 0;
                for (Edge e : BranchAndBound.this.graph.getAdjacency().get(v)) {
                    Vertex z = e.other(v);
                    if (this.uncoveredVertices.contains(z)) {
                        numUncoveredVertices++;
                    }
                }
                if (numUncoveredVertices > highestDegree) {
                    ret = v;
                    highestDegree = numUncoveredVertices;
                }
            }

            return ret;
        }

        /**
         * Copy a configuration
         *
         * @return a new configuration same as original one
         */
        public Configuration configCopy() {
            Configuration configCopy = new Configuration(new HashSet<>(this.coveredVertices),
                    new HashSet<>(this.coveredEdges), new HashSet<>(this.uncoveredVertices),
                    new HashSet<>(this.uncoveredEdges));
            return configCopy;
        }

    }

    public ArrayList<Vertex> runBranchAndBound() {
        candidateList = new ArrayList<>();
        startTime = System.currentTimeMillis();
        runBranchAndBound(new Configuration(new HashSet<>(), new HashSet<>(),
                new HashSet<>(this.graph.getTotalVertices()), new HashSet<>(this.graph.getTotalEdges())));
        return candidateList;

    }

    /**
     * Test to see if time is up
     *
     * @return true if duration reaches the speicified cutoff time
     */
    public boolean meetCutoff() {
        return (duration >= cutoffTime);
    }

    private void runBranchAndBound(Configuration config) {

        duration = System.currentTimeMillis() - startTime;

        if (meetCutoff()) {
            output.print(duration / 1000F + "," + candidateList.size() + "\n");
            output.close();
            return;
        }


        // If number of covered vertices of a given subgraph
        // Is larger than the size of best solution found so for
        // Trim this branch
        if (config.coveredVertices.size() >= upperBound) {
            return;
        }

        // If number of covered vertices of a given subgraph
        // Is differ from the size of best solution found so for
        // exactly by one, and not a vertex cover yet
        // Trim this branch
        if ((config.coveredVertices.size() == upperBound - 1) && (!config.isCover())) {
            return;
        }


        if (config.isCover()) {
            candidateList = new ArrayList<>(config.coveredVertices);
            upperBound = candidateList.size();
            output.print(duration / 1000F + "," + candidateList.size() + "\n");
            return;
        }


        Vertex v = config.getVertexWithHighestDegree();

        // Branch into left and branch into right
        // Upperbound: size of used vertices + (two Approx of remaining graph).size / 2
        // Lowerbound: best solution that has been found so far

        Configuration leftConfig = config.getLeftConfig(v);
        int leftLowerBound = twoApprox(leftConfig).size() / 2 + leftConfig.coveredVertices.size();
        int leftUpperBound = candidateList.size();

        Configuration rightConfig = config.getRightConfig(v);
        int rightLowerBound = twoApprox(rightConfig).size() / 2 + rightConfig.coveredVertices.size();
        int rightUpperBound = candidateList.size();

        if (leftLowerBound >= upperBound && rightLowerBound >= upperBound) {
            return;
        } else if (leftLowerBound < upperBound && rightLowerBound >= upperBound) {
            runBranchAndBound(leftConfig);
        } else if (rightLowerBound < upperBound && leftLowerBound >= upperBound) {
            runBranchAndBound(rightConfig);
//        } else if (leftLowerBound < rightLowerBound) {
//            runBranchAndBound(leftConfig);
//            runBranchAndBound(rightConfig);
//        } else {
//            runBranchAndBound(rightConfig);
//            runBranchAndBound(leftConfig);
//        }
        } else {
            runBranchAndBound(leftConfig);
            runBranchAndBound(rightConfig);
        }
        return;
    }


    /**
     * Return initial vertex cover using two-approx algorithm
     *
     * @param config Partial configuration / subgraph
     * @return initialVertexCover
     */
    public ArrayList<Vertex> twoApprox(Configuration config) {

        ArrayList<Vertex> initialVertexCover = new ArrayList<Vertex>();
        Set<Edge> uncoverdEdges = new HashSet<>(config.getUnCoveredEdges());
        while (uncoverdEdges.size() > 0) {
            Edge e = uncoverdEdges.iterator().next();
            Vertex u = e.getSource();
            Vertex v = e.getDestination();
            initialVertexCover.add(u);
            initialVertexCover.add(v);
            ArrayList<Edge> edgesToRemove = new ArrayList<Edge>();
            for (Edge edge : uncoverdEdges) {
                if ((edge.getSource().getLabel() == u.getLabel()) || (edge.getSource().getLabel() == v.getLabel())
                        || (edge.getDestination().getLabel() == u.getLabel())
                        || (edge.getDestination().getLabel() == v.getLabel())) {
                    edgesToRemove.add(edge);
                }
            }
            uncoverdEdges.removeAll(edgesToRemove);
        }
        return initialVertexCover;
    }

}
