package tefloydwarshallalgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author igor
 * Last edited: 20-02-2018
 */

public class ShortestPathFinder {

    public static final long INF = 1000000000;
    private final int numOfVertices;
    private long[][] mAdj;
    private long[][] matrixOfShortestDistances;
    List<Integer> pathSequences[][];
    int[][] intermediateVertices;
    String formattedMatrixOfShortestPathes = "";
    String formattedPathChaines = "";
    private boolean hasPathMatrixChanged = true;
    
    public ShortestPathFinder(final int numOfVertices){
        this.numOfVertices = numOfVertices;
        mAdj = new long[numOfVertices][numOfVertices];
        matrixOfShortestDistances = new long[numOfVertices][numOfVertices];
        for(int i = 0; i < numOfVertices; ++i){
            for(int j = 0; j < numOfVertices; ++j){
                matrixOfShortestDistances[i][j] = mAdj[i][j] = (i == j) ? 0 : INF;
            }
        }
        pathSequences = new ArrayList[numOfVertices][numOfVertices];
        intermediateVertices = new int[numOfVertices][numOfVertices];
    }
    
    public void addEdge(final int from, final int to, final long weight,
                        boolean isBidirectional) {
        if (isVertexIndexValid(from) && isVertexIndexValid(to) && weight >= 0) {
            mAdj[from][to] = weight;
            if(isBidirectional)
                mAdj[to][from] = weight;
            hasPathMatrixChanged = true;
        }
    }
    
    public void addEdge(final int from, final int to, final long weight){
        addEdge(from, to, weight, false);
    }
    
    public void removeEdge(final int from, final int to){
        mAdj[from][to] = INF;
        hasPathMatrixChanged = true;
    }
    
    private boolean isVertexIndexValid(int index){
        return index >= 0 && index < numOfVertices;
    }
    
    public long getShortestDistance(int from, int to){
        if(hasPathMatrixChanged)
            calculateShortestPath();
        if(isVertexIndexValid(from) && isVertexIndexValid(to))
            return matrixOfShortestDistances[from][to];
        else
            return INF;
    }
    
    public List<Integer> getShortestPath(int from, int to){
        if(hasPathMatrixChanged)
            calculateShortestPath();
        if(isVertexIndexValid(from) && isVertexIndexValid(to))
            return Collections.unmodifiableList(pathSequences[from][to]);
        else
            return new ArrayList<>();
    }

    public String calculateShortestPath() {

        if (hasPathMatrixChanged) {
            //matrixOfShortestDistances = Arrays.copyOf(mAdj, numOfVertices);

            for (int i = 0; i < numOfVertices; ++i) {
                for (int j = 0; j < numOfVertices; ++j) {
                    pathSequences[i][j] = new ArrayList<>();
                    intermediateVertices[i][j] = j;
                    matrixOfShortestDistances[i][j] = mAdj[i][j];
                }
            }

            long oldPath, newPath, minimumPath;
            List<Integer> coveredPath = new ArrayList<>();

            for (int k = 0; k < numOfVertices; ++k) {
                for (int i = 0; i < numOfVertices; ++i) {                    
                    for (int j = 0; j < numOfVertices; ++j) {
                        oldPath = matrixOfShortestDistances[i][j];
                        newPath = matrixOfShortestDistances[i][k]
                                + matrixOfShortestDistances[k][j];
                        minimumPath = (long) Math.min(oldPath, newPath);
                        if (oldPath != newPath && minimumPath == newPath) {
                            intermediateVertices[i][j] = k;                            
                        }
                        matrixOfShortestDistances[i][j] = minimumPath;
                    }
                }
            }
            
            //Prints the matrix of shortest pathSequences
            
            formattedMatrixOfShortestPathes = "";
            formattedPathChaines = "";
            for (int i = 0; i < numOfVertices; ++i) {
                for (int j = 0; j < numOfVertices; ++j) {
                    if(i == 0 && j == 3){
                            System.out.println("");
                        }
                    formattedMatrixOfShortestPathes += "["
                            + (matrixOfShortestDistances[i][j] == INF ? "INF"
                                    : matrixOfShortestDistances[i][j]) + "]";
                    if (i != j && matrixOfShortestDistances[i][j] != INF) {
                        pathSequences[i][j].add(i);
                        if(matrixOfShortestDistances[i][j] != mAdj[i][j]){
                            coveredPath.clear();
                            int k = intermediateVertices[i][j];
                            pathSequences[i][j].addAll(restorePathChain(i, k,
                                    intermediateVertices, coveredPath));
                            pathSequences[i][j].add(k);
                        }
                        pathSequences[i][j].add(j);
                        
                        String formattedPath = "";
                        if(!pathSequences[i][j].isEmpty()){
                            int ultimate = pathSequences[i][j].size() - 1;
                            for (int k = 0; k < ultimate; ++k) {
                                formattedPath += (pathSequences[i][j].get(k) + 1)
                                        + ", ";
                            }
                            formattedPath += (pathSequences[i][j].get(ultimate) + 1);
                        } 
                        formattedPathChaines += "From (" + (i + 1) + "--->"
                                    + (j + 1) + ") follow the path ("
                                    + formattedPath + ")\n";
                    }

                }
                formattedMatrixOfShortestPathes += "\n";
            }

            //Prints all path chains from all pairs of vertices
            
            hasPathMatrixChanged = false;
        }

        return "The matrix of shortest pathes between all pairs of verteces:\n"
                + formattedMatrixOfShortestPathes
                + "\nAll shorstest pathes:\n" + formattedPathChaines;
    }

    private List<Integer> restorePathChain(int i, int k, int[][] intermediateVertices,
            List<Integer> coveredPath) {
        if (intermediateVertices[i][k] == k) {
            return new ArrayList<>();
        } else {
            int currentVertex = intermediateVertices[i][k];
            List<Integer> tmp = restorePathChain(i, currentVertex,
                                                intermediateVertices,
                                                coveredPath);
            if(tmp.size() > 1)
                coveredPath.addAll(tmp);
            coveredPath.add(currentVertex);
            return coveredPath;
        } 
    }
}