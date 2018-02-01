import java.util.Comparator;


public class VertexDegreeComparator implements Comparator<Vertex> {

    @Override
    public int compare(Vertex o1, Vertex o2) {
        if (o1.getEdgeCount() == o2.getEdgeCount())
            return 0;
        else if (o1.getEdgeCount() < o2.getEdgeCount())
            return 1;
        else
            return -1;
    }
}
