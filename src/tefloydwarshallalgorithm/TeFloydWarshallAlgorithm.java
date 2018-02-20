package tefloydwarshallalgorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author igor
 */
public class TeFloydWarshallAlgorithm {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TeFloydWarshallAlgorithm fwa = new TeFloydWarshallAlgorithm();
        fwa.test();
        
    }

    public void test() {
        try (InputStream ins = this.getClass().getResourceAsStream("graph.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(ins, "UTF-8"))) {
            System.out.println("The file with input graph was successfully opened!\n");
            int numOfVerteces = Integer.parseInt(br.readLine());
            System.out.println("numberOfVertices = " + numOfVerteces);
            ShortestPathFinder spf = new ShortestPathFinder(numOfVerteces);
            String str;
            while((str = br.readLine()) != null){
                String[] line = str.split("\\s+");
                spf.addEdge(Integer.parseInt(line[0]) - 1,
                            Integer.parseInt(line[1]) - 1,
                            Long.parseLong(line[2]));
                
            }
            System.out.println(spf.calculateShortestPath() + "\n Added one adge \n");

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TeFloydWarshallAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TeFloydWarshallAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
