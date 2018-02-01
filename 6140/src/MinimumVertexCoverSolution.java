import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MinimumVertexCoverSolution {
    private static final String INST = "-inst";
    private static final String ALG = "-alg";
    private static final String TIME = "-time";
    private static final String SEED = "-seed";
    private static final String BnB = "BnB";
    private static final String APPROX = "Approx";
    private static final String LS1 = "LS1";
    private static final String LS2 = "LS2";

    /**
     * The main function, it receives all the required parameters from user
     * and run corresponding algorithm to obtained the MVC from specified data
     * graph file, and then generate output files which contain all the result.
     *
     * @param args[0] the data graph file.
     * @param args[1] the random seed for local search Algorithm.
     * @param args[2] the cutoff time.
     * @param args[3] the name of the chosen algorithm to run.
     */
    public static void main(String[] args) {
        String filename = "";
        String algo = "";
        long time = 0;
        int seed = 0;
        for (int i = 0; i < args.length - 1; i = i + 2) {
            if (args[i].equalsIgnoreCase(INST)) {
                filename = args[i + 1];
            } else if (args[i].equalsIgnoreCase(ALG)) {
                algo = args[i + 1];
            } else if (args[i].equalsIgnoreCase(TIME)) {
                time = Long.parseLong(args[i + 1]);
            } else if (args[i].equalsIgnoreCase(SEED)) {
                seed = Integer.parseInt(args[i + 1]);
            }
        }

        if (filename.isEmpty()) {
            System.out.println("Specify filename");
            return;
        }
        File file = new File(filename);

        try {
            Graph g = Utilities.parseGraph(filename, algo);
            if (algo.equalsIgnoreCase(BnB)) {
                System.out.println("Running BnB" + file.getName());
                try {
                    PrintWriter output = new PrintWriter(file.getName().substring(0, file.getName().indexOf("."))
                            + "_BnB_" + time + ".sol", "UTF-8");
                    PrintWriter outputTrace = new PrintWriter(file.getName().substring(0, file.getName().indexOf("."))
                            + "_BnB_" + time + ".trace", "UTF-8");
                    BranchAndBound BNBSolver = new BranchAndBound(g, time * 1000, outputTrace);
                    ArrayList<Vertex> vertexCoverSolution = BNBSolver.runBranchAndBound();
                    output.print(vertexCoverSolution.size() + "\n");
                    for (Vertex v : vertexCoverSolution) {
                        output.print(v.getLabel() + ",");
                    }
                    output.close();
                    outputTrace.close();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            } else if (algo.equalsIgnoreCase(LS1)) {
                System.out.println("Running LS1" + file.getName());
                try {
                    PrintWriter output = new PrintWriter(file.getName().substring(0, file.getName().indexOf(".")) + "_LS1_" + time + "_" + seed + ".sol", "UTF-8");
                    PrintWriter outputTrace = new PrintWriter(file.getName().substring(0, file.getName().indexOf(".")) + "_LS1_" + time + "_" + seed + ".trace", "UTF-8");
                    RandomHillClimbing rhc = new RandomHillClimbing();
                    ArrayList<Vertex> vertexCoverSolution = rhc.rhc(g, seed, time * 1000, outputTrace);
                    output.print(vertexCoverSolution.size() + "\n");
                    for (Vertex v : vertexCoverSolution) {
                        output.print(v.getLabel() + ",");
                    }
                    output.close();
                    outputTrace.close();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            } else if (algo.equalsIgnoreCase(LS2)) {
                try {
                    PrintWriter output = new PrintWriter(file.getName().substring(0, file.getName().indexOf(".")) + "_LS2_" + time + "_" + seed + ".sol", "UTF-8");
                    PrintWriter outputTrace = new PrintWriter(file.getName().substring(0, file.getName().indexOf(".")) + "_LS2_" + time + "_" + seed + ".trace", "UTF-8");
                    SimulatedAnneahling sa = new SimulatedAnneahling();
                    ArrayList<Vertex> vertexCoverSolution = sa.simulatedAnneahling(g, seed, time * 1000, outputTrace);
                    output.print(vertexCoverSolution.size() + "\n");
                    for (Vertex v : vertexCoverSolution) {
                        output.print(v.getLabel() + ",");
                    }
                    output.close();
                    outputTrace.close();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


            } else if (algo.equalsIgnoreCase(APPROX)) {
                try {
                    PrintWriter output = new PrintWriter(file.getName().substring(0, file.getName().indexOf(".")) + "_Approx_" + time + "_.sol", "UTF-8");
                    PrintWriter outputTrace = new PrintWriter(file.getName().substring(0, file.getName().indexOf(".")) + "_Approx_" + time + "_.trace", "UTF-8");
                    Approximation appxm = new Approximation();
                    List<Vertex> approxSolution = appxm.findSet(g);
                    Collections.sort(approxSolution, new Comparator<Vertex>() {
                        @Override
                        public int compare(Vertex o1, Vertex o2) {
                            return o1.getLabel() - o2.getLabel();
                        }
                    });

                    output.print(approxSolution.size() + "\n");
                    for (Vertex v : approxSolution) {
                        output.print(v.getLabel() + ",");
                    }
                    output.close();
                    outputTrace.close();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
