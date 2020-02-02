package bearmaps.utils.graph;

import edu.princeton.cs.algs4.Stopwatch;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import bearmaps.utils.pq.PriorityQueue;
import bearmaps.utils.pq.DoubleMapPQ;
import java.util.ArrayList;
import java.util.Collections;

/**
 * In this part, we’ll be implementing an A* solver, an artificial intelligence that can solve arbitrary state
 * space traversal problems. Specifically, given a graph of possible states, your AI will find
 * the optimal route from the start state to a goal state.
 * @author Priscila Amorim, Shawn Hua
 * @param <Vertex>
 */

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {

    private SolverOutcome outcome;
    private double solutionWeight;
    private List<Vertex> solution;
    private double timeSpent;
    private int numStatesExplored;
    private Map<Vertex, Double> distTo = new HashMap<>();
    private Map<Vertex, Vertex> edgeTo = new HashMap<>();

    /**
     * Constructor which finds the solution, computing everything necessary for all other methods
     * to return their results in constant time.
     * Note that timeout passed in is in seconds.
     * @param input
     * @param start
     * @param end
     * @param timeout
     */
    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        Stopwatch sw = new Stopwatch();
        PriorityQueue<Vertex> pq = new DoubleMapPQ<>();
        solution = new ArrayList<>();

        distTo.put(start, 0.0);
        edgeTo.put(start, start);
        pq.insert(start, input.estimatedDistanceToGoal(start, end));

        while (pq.size() > 0 && !pq.peek().equals(end) && sw.elapsedTime() < timeout) {
            Vertex p = pq.poll();
            numStatesExplored++;

            for (WeightedEdge<Vertex> e : input.neighbors(p)) {
                relax(e, pq, input, end);
            }
        }

        timeSpent = sw.elapsedTime();
        if (timeSpent >= timeout) {
            outcome = SolverOutcome.TIMEOUT;
            return;
        }

        if (pq.size() == 0) {
            outcome = SolverOutcome.UNSOLVABLE;
            return;
        }

        Vertex goal = pq.poll();
        solutionWeight = distTo.get(goal);
        while (goal != start) {
            solution.add(goal);
            goal = edgeTo.get(goal);
        }
        //solution.add(start);
        Collections.reverse(solution);
        outcome = SolverOutcome.SOLVED;
    }

    /**
     * Helper method for relax.
     */
    public void relax(WeightedEdge<Vertex> e, PriorityQueue<Vertex> pq, AStarGraph<Vertex> input, Vertex end) {
        Vertex p = e.from();
        Vertex q = e.to();
        double w = e.weight();

        if (!distTo.containsKey(q)) {
            distTo.put(q, Double.MAX_VALUE);
        }

        if (distTo.get(p) + w < distTo.get(q)) {
            if (distTo.containsKey(q)) {
                distTo.replace(q, distTo.get(p) + w);
            } else {
                distTo.put(q, distTo.get(p) + w); // FIXME MAYBE
            }
            if (pq.contains(q)) {
                pq.changePriority(q, distTo.get(q) + input.estimatedDistanceToGoal(q, end));
            } else {
                pq.insert(q, distTo.get(q) + input.estimatedDistanceToGoal(q, end));
            }
            edgeTo.put(q, p);
        }
    }

    /**
     * @return one of SolverOutcome.SOLVED, SolverOutcome.TIMEOUT, or SolverOutcome.UNSOLVABLE.
     * Should be SOLVED if the AStarSolver was able to complete all work in the time given.
     * UNSOLVABLE if the priority queue became empty before finding the solution.
     * TIMEOUT if the solver ran out of time.
     * You should check to see if you have run out of time every time you dequeue.
     */
    public SolverOutcome outcome() {
        return outcome;
    }

    /**
     * @return a list of vertices corresponding to a solution.
     * Should be empty if result was TIMEOUT or UNSOLVABLE.
     */
    public List<Vertex> solution() {
        return solution;
    }

    /**
     * @return the total weight of the given solution, taking into account edge weights.
     * Should be 0 if result was TIMEOUT or UNSOLVABLE.
     */
    public double solutionWeight() {
        return solutionWeight;
    }

    /**
     * @return the total number of priority queue poll() operations.
     * Should be the number of states explored so far if result was TIMEOUT or UNSOLVABLE.
     */
    public int numStatesExplored() {
        return numStatesExplored;
    }

    /**
     * @return the total time spent in seconds by the constructor.
     */
    public double explorationTime() {
        return timeSpent;
    }
}
