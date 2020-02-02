package bearmaps;

import bearmaps.utils.graph.WeightedEdge;
import bearmaps.utils.graph.streetmap.StreetMapGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import bearmaps.utils.graph.streetmap.Node;
import bearmaps.utils.ps.KDTree;
import bearmaps.utils.ps.Point;
import bearmaps.utils.ps.WeirdPointSet;

import java.util.HashMap;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, Priscila Amorim, Shawn Hua
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {

    List<Node> nodes;
    Map<Long, Point> nodeIDPointMap = new HashMap<>();
    Map<Point, Long> pointNodeIDMap = new HashMap<>();
    List<Point> points = new ArrayList<>();
    WeirdPointSet kdTree;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        nodes = this.getNodes();
        for (Node node : nodes) {
            if (!neighbors(node.id()).isEmpty()) {
                Point pt = new Point(node.lon(), node.lat());
                nodeIDPointMap.put(node.id(), pt);
                pointNodeIDMap.put(pt, node.id());
                points.add(pt);
            }
        }
        kdTree = new WeirdPointSet(points);
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        //Point input = new Point(lon, lat);
        /*
        long inputID = 0;
        for (Node node : nodes) {
            if (node.lon() == lon && node.lat() == lat) {
                inputID = node.id();
            }
        }
        */

        /*
        List<Point> allNeighborPoints = new ArrayList<>();
        List<WeightedEdge<Long>> neighbors = neighbors(inputID);
        for (WeightedEdge<Long> neighbor : neighbors) {
            Point neighborPoint = nodeIDPointMap.get(neighbor);
            allNeighborPoints.add(neighborPoint);
        }
         */

        Point closestPoint = kdTree.nearest(lon, lat);
        long rtn = pointNodeIDMap.get(closestPoint);
        return rtn;
    }


    /**
     * For Project Part III (extra credit)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        return new LinkedList<>();
    }

    /**
     * For Project Part III (extra credit)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        return new LinkedList<>();
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
