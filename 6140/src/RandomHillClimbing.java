import java.io.PrintWriter;
import java.util.*;


public class RandomHillClimbing {
    // Global variable for the RHC loss, gain and cedges
    ArrayList<Edge> coveredEdges = new ArrayList<>();
    ArrayList<Edge> uncoveredEdges = new ArrayList<>();
    HashMap<Vertex, Integer> loss = new HashMap<>();
    HashMap<Vertex, Integer> gain = new HashMap<>();


    // Construct a initial vertex cover
    // Input Graph g
    // Output is a Vertex Cover HAshSet<Vertex>
    public HashSet<Vertex> constructVertexCover(Graph g) {
        ArrayList<Vertex> vc = new ArrayList<>();

        for (Edge e : g.getTotalEdges()) {
            if (!coveredEdges.contains(e)) {
                if (e.getSource().getEdgeCount() > e.getDestination().getEdgeCount()) {
                    vc.add(e.getSource());
                } else {
                    vc.add(e.getDestination());
                }
                coveredEdges.add(e);
                coveredEdges.add(new Edge(e.getDestination(), e.getSource()));
            }
        }
        // System.out.println(vc.size());
        for (Vertex v : vc) {
            loss.put(v, 0);
        }
        // System.out.println(loss.size());
        for (Edge e : coveredEdges) {
            if (loss.get(e.getSource()) != null && loss.get(e.getDestination()) == null) {
                int currentLoss = loss.get(e.getSource());
                loss.put(e.getSource(), currentLoss + 1);
            } else if (loss.get(e.getSource()) == null && loss.get(e.getDestination()) != null) {
                int currentLoss = loss.get(e.getDestination());
                loss.put(e.getDestination(), currentLoss + 1);
            }
        }

        Iterator<Map.Entry<Vertex, Integer>> itr = loss.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<Vertex, Integer> entry = itr.next();
            if (entry.getValue().intValue() == 0) {
                // System.out.println("Key : "+entry.getKey()+" Removed.");
                itr.remove();  // Call Iterator's remove method.
            }
            // System.out.println(loss);
            updateLoss(entry.getKey(), loss, g);
            // System.out.println("======="+loss);
        }
        // System.out.println(loss.size());
//        Collections.sort(vc,new main.VertexDegreeComparator());

        return new HashSet<>(loss.keySet());
    }

    // Update the Loss of all the vertices based on the vertex that is removed
    public void updateLoss(Vertex key, HashMap<Vertex, Integer> loss, Graph g) {
        for (Edge e : g.getAdjacency().get(key)) {
            if (loss.get(e.getDestination()) != null) {
                int currentLoss = loss.get(e.getDestination());
                loss.put(e.getDestination(), currentLoss + 1);
                // System.out.println("Updated"+e.getDestination()+": "+(currentLoss+1));
            }
        }
    }

    // This is the main rhc function that uses random hill climbing algo to compute the vertex cover
    public ArrayList<Vertex> rhc(Graph g, int seed, long time, PrintWriter outputTrace) {
        HashSet<Vertex> initialVertexCover = constructVertexCover(g);
        for (Vertex v : g.getAdjacency().keySet()) {
            if (!initialVertexCover.contains(v)) {
                gain.put(v, 0);
            }
        }

        uncoveredEdges = getUncoveredEdges(g);
        Random random = new Random(seed);
        long start = System.currentTimeMillis();
        long runningDuration = 0;
        Set<Vertex> verTexCover = new HashSet<>();
        while (runningDuration < time) {
            if (isVertexCover(initialVertexCover, g)) {
                outputTrace.print(runningDuration / 1000F + "," + initialVertexCover.size() + "\n");
                verTexCover = new HashSet<>(initialVertexCover);
                Vertex minLossVertex = removeVertexWithMinLoss(g);
                initialVertexCover.remove(minLossVertex);
                transferEdges(initialVertexCover, minLossVertex, g, gain);
                initGain(gain, uncoveredEdges, initialVertexCover);
                continue;
            }
            Vertex minLossVertex = removeVertexRandomly(new ArrayList<Vertex>(initialVertexCover), seed, 50, g);
            initialVertexCover.remove(minLossVertex);
            // System.out.println("Zer?"+initialVertexCover.size());
            if (uncoveredEdges.size() > 0) {
                int randomIndex = random.nextInt(uncoveredEdges.size());
                Edge randomlyUncoveredEdge = uncoveredEdges.get(randomIndex);
                transferEdges(initialVertexCover, minLossVertex, g, gain);
                initGain(gain, uncoveredEdges, initialVertexCover);
                transferEdges(randomlyUncoveredEdge, g);
                int gainU = gain.get(randomlyUncoveredEdge.getSource());
                int gainV = gain.get(randomlyUncoveredEdge.getDestination());
                if (gainU > gainV) {
                    if (loss.get(randomlyUncoveredEdge.getSource()) != null) {
                        int currentLoss = loss.get(randomlyUncoveredEdge.getSource());
                        loss.put(randomlyUncoveredEdge.getSource(), currentLoss + 1);

                    } else {
                        loss.put(randomlyUncoveredEdge.getSource(), 1);
                    }
                    initialVertexCover.add(randomlyUncoveredEdge.getSource());
                    updateLoss(randomlyUncoveredEdge.getSource(), loss, g);
//                    gain.remove(randomlyUncoveredEdge.getSource());
                    updateGain(randomlyUncoveredEdge.getSource(), gain, g);
                } else {
                    if (loss.get(randomlyUncoveredEdge.getDestination()) != null) {
                        int currentLoss = loss.get(randomlyUncoveredEdge.getDestination());
                        loss.put(randomlyUncoveredEdge.getDestination(), currentLoss + 1);
                    } else {
                        loss.put(randomlyUncoveredEdge.getDestination(), 1);
                    }
                    initialVertexCover.add(randomlyUncoveredEdge.getDestination());
                    updateLoss(randomlyUncoveredEdge.getDestination(), loss, g);
//                    gain.remove(randomlyUncoveredEdge.getDestination());
                    updateGain(randomlyUncoveredEdge.getDestination(), gain, g);
                }
            }
            runningDuration = System.currentTimeMillis() - start;
        }
        // System.out.println("Final main.Vertex Cover"+verTexCover.size());
        ArrayList<Vertex> vertexCoverSolution = new ArrayList<Vertex>(verTexCover);
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

    // Randomly remove a vertex from the vertex cover
    public Vertex removeVertexRandomly(ArrayList<Vertex> vertices, int seed, int k, Graph g) {
        int size = vertices.size();
        Random random = new Random(seed);
        int minLoss = Integer.MAX_VALUE;
        Vertex minLossVertex = null;
        for (int i = 0; i < k; i++) {
            int randIndex = random.nextInt(size);
            int currentLoss = loss.get(vertices.get(randIndex));
            if (currentLoss < minLoss) {
                minLossVertex = vertices.get(randIndex);
                minLoss = currentLoss;
            }
        }

        loss.remove(minLossVertex);
        updateLoss(minLossVertex, loss, g);

        return minLossVertex;


    }

    //mark edges covered to uncovered
    public void transferEdges(HashSet<Vertex> initialVertexCover, Vertex v, Graph g, HashMap<Vertex, Integer> gain) {

        for (Edge e : g.getAdjacency().get(v)) {
            if (coveredEdges.contains(e)) {
                if (!(initialVertexCover.contains(e.getSource()) || initialVertexCover.contains(e.getDestination()))) {
                    uncoveredEdges.add(e);
                    uncoveredEdges.add(new Edge(e.getDestination(), e.getSource()));
//                updateGain(e.getSource(),gain,g);
//                updateGain(e.getDestination(),gain,g);
                    coveredEdges.remove(e);
                    coveredEdges.remove(new Edge(e.getDestination(), e.getSource()));
                }

            }
        }
    }

    public void transferEdges(Edge e, Graph g) {

        coveredEdges.add(e);
        coveredEdges.add(new Edge(e.getDestination(), e.getSource()));
        uncoveredEdges.remove(e);
        uncoveredEdges.remove(new Edge(e.getDestination(), e.getSource()));

    }

    // remove vertex with min loss
    public Vertex removeVertexWithMinLoss(Graph g) {
        int minLoss = Integer.MAX_VALUE;
        Vertex minLossVertex = null;
        for (Vertex v : loss.keySet()) {
            int currentLoss = loss.get(v);
            if (currentLoss < minLoss) {
                minLossVertex = v;
                minLoss = currentLoss;
            }
        }

        loss.remove(minLossVertex);
        updateLoss(minLossVertex, loss, g);
        return minLossVertex;

    }

    // get all the uncovered edges
    public ArrayList<Edge> getUncoveredEdges(Graph g) {
        // // System.out.println(g.getTotalEdges().size() +" - "+ coveredEdges.size());
        ArrayList<Edge> uncoveredEdges = new ArrayList<>(g.getTotalEdges());
        for (Edge e : coveredEdges) {
            uncoveredEdges.remove(e);
            uncoveredEdges.remove(new Edge(e.getDestination(), e.getSource()));
        }
//        uncoveredEdges.removeAll(coveredEdges);
        // // System.out.println(uncoveredEdges.size());
        return uncoveredEdges;
    }

    //Check if it s a vertex cover

    public boolean isVertexCover(HashSet<Vertex> vertexCover, Graph g) {
//        System.out.println("Edges:"+g.getTotalEdges().size());
        for (Edge e : g.getTotalEdges()) {
            if (!vertexCover.contains(e.getSource()) && !vertexCover.contains(e.getDestination())) {
                return false;
            }
        }
        return true;
    }

    // intiliaze all gain

    public void initGain(HashMap<Vertex, Integer> gain, ArrayList<Edge> uncoveredEdges, HashSet<Vertex> initialVertexCover) {
        for (Edge e : uncoveredEdges) {
            if (!(initialVertexCover.contains(e.getSource()) || initialVertexCover.contains(e.getDestination()))) {
                if (gain.get(e.getSource()) != null) {
                    int currentGain = gain.get(e.getSource());
                    gain.put(e.getSource(), currentGain + 1);
                } else {
                    gain.put(e.getSource(), 1);
                }
                if (gain.get(e.getDestination()) != null) {
                    int currentGain = gain.get(e.getDestination());
                    gain.put(e.getDestination(), currentGain + 1);
                } else {
                    gain.put(e.getSource(), 1);
                }
            } else {
                gain.put(e.getSource(), 0);
                gain.put(e.getDestination(), 0);
            }
        }
    }

    //update gain
    public void updateGain(Vertex key, HashMap<Vertex, Integer> gain, Graph g) {
        for (Edge e : g.getAdjacency().get(key)) {
            if (gain.get(e.getDestination()) != null) {
                int currentGain = gain.get(e.getDestination());
                if (currentGain > 0) {
                    gain.put(e.getDestination(), currentGain - 1);
                    // // System.out.println("Updated"+e.getDestination()+": "+(currentGain-1));
                } else {
                    gain.remove(e.getDestination());
                }

            } else {
                gain.put(e.getDestination(), 1);
            }
        }
        gain.remove(key);
    }
}
