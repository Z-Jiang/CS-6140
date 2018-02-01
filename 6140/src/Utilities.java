import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Comparator;


public class Utilities {

    /**
     * Empty Constructor
     */
    public Utilities() {
    }

    /**
     * The function that parse the data graph file.
     *
     * @param filename, the name of the data graph file.
     * @param algo,     the name of the algorithm chosen.
     * @return graph, the generated graph containing all the information from data graph file.
     */
    public static Graph parseGraph(String filename, String algo) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        Graph newGraph = new Graph();
        if (!algo.equalsIgnoreCase("Approx")) {
            try {
                String line;
                String[] generalInfo = null;

                if ((line = br.readLine()) != null) {
                    generalInfo = line.split(" ");
                }
                int numberOfVertices = Integer.parseInt(generalInfo[0]);
                int numberOfEdges = Integer.parseInt(generalInfo[1]);
                ArrayList<Vertex> vertexSet = new ArrayList<Vertex>();
                ArrayList<Edge> edgeSet = new ArrayList<Edge>();

                for (int i = 0; i < numberOfVertices; i++) {
                    vertexSet.add(new Vertex(i + 1)); // index-1 based
                }
                int count = 0;
                while ((line = br.readLine()) != null) {
                    if (line.equals("") && count < numberOfVertices) {
                        newGraph.getAdjacency().put(vertexSet.get(count), new ArrayList<>());
                        count++;
                        continue;
                    }
                    String[] dataInfo = line.split(" ");
                    ArrayList<Vertex> desVertices = new ArrayList<Vertex>(dataInfo.length);
                    for (int j = 0; j < dataInfo.length; j++) {
                        for (int k = 0; k < numberOfVertices; k++) {
                            if ((!(dataInfo[j].equals("")))
                                    && (vertexSet.get(k).label) == (Integer.parseInt(dataInfo[j]))) {
                                Edge e = new Edge(vertexSet.get(count), vertexSet.get(k));
                                edgeSet.add(e);
                                newGraph.addEdge(e);
                            }
                        }
                    }
                    count++;
                }
                newGraph.setNumberOfVertices(numberOfVertices);
                newGraph.setNumberOfEdges(numberOfEdges);
                newGraph.setTotalVertices(vertexSet);
                newGraph.setTotalEdges(edgeSet);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return newGraph;
        } else {
            try {
                String line;
                String[] generalInfo = null;

                if ((line = br.readLine()) != null) {
                    generalInfo = line.split(" ");
                }

                int numberOfVertices = Integer.parseInt(generalInfo[0]);
                int numberOfEdges = Integer.parseInt(generalInfo[1]);
                ArrayList<Vertex> vertexSet = new ArrayList<Vertex>();
                Map<Vertex, List<Vertex>> edges = new HashMap<Vertex, List<Vertex>>();

                for (int i = 0; i < numberOfVertices; i++) {
                    vertexSet.add(new Vertex(i + 1)); // index-1 based
                }

                int vertexIndex = 0;
                while ((line = br.readLine()) != null) {
                    if (line.isEmpty()) {
                        vertexIndex++;
                        continue;
                    }
                    Vertex temp = vertexSet.get(vertexIndex);
                    String[] neighborNodes = line.split(" ");


                    temp.setEdgeCount(neighborNodes.length);
                    for (int i = 0; i < neighborNodes.length; i++) {
                        int nodeLabel = Integer.parseInt(neighborNodes[i]);
                        temp.getNeighbors().add(vertexSet.get(nodeLabel - 1));
                        if (edges.get(temp) == null) {
                            List<Vertex> neighbors = new ArrayList<>();
                            neighbors.add(vertexSet.get(nodeLabel - 1));
                            edges.put(temp, neighbors);
                        } else {
                            edges.get(temp).add(vertexSet.get(nodeLabel - 1));
                        }
                    }
                    vertexIndex++;
                }
                Queue<Vertex> vertexQueue = new PriorityQueue<>(new Comparator<Vertex>() {
                    @Override
                    public int compare(Vertex o1, Vertex o2) {
                        int diff = o2.getEdgeCount() - o1.getEdgeCount();
                        if (diff != 0) {
                            return diff;
                        } else {
                            return o1.getLabel() - o2.getLabel();
                        }
                    }
                });
                for (int i = 0; i < vertexSet.size(); i++) {
                    vertexQueue.offer(vertexSet.get(i));
                }
                newGraph = new Graph(numberOfVertices, numberOfEdges, vertexQueue, edges);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return newGraph;
        }
    }
}
