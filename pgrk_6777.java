// JUSTINE AYROOR    cs610 6777 prp

import java.io.*;
import java.util.*;

class pgrk_6777{
    private final int iterations;
    private final int initialvalue;
    private final String filename;
    public int numEdge;
    public int numVertice;
    public File file;
    public BufferedReader br;
    public List<ArrayList<Integer>> edges = new ArrayList<ArrayList<Integer>>();
    public double[] rVector;
    public int[] outDegreeMatrix;
    public double[][] AdjacencyMatrix;
    public double d = 0.85; 

    public pgrk_6777(int iterations, int initialvalue, String filename) {
        this.iterations = iterations;
        this.initialvalue = initialvalue;
        this.filename = filename;
    }

    public void GetInput(String filename){
        // File Operations 
        String st;   
        int ipCounter = 0;
        try{
            file = new File(filename); 
            br = new BufferedReader(new FileReader(file));
            while ((st = br.readLine()) != null){
                if(ipCounter == 0){
                    numVertice = Integer.parseInt(st.substring(0, 1));
                    numEdge = Integer.parseInt(st.substring(2, 3));
                }else{
                    edges.add(new ArrayList<Integer>(Arrays.asList(Integer.parseInt(st.substring(0, 1)), Integer.parseInt(st.substring(2, 3)))));
                }
                ipCounter++;
            }    
        }catch(Exception e){
            System.out.println(e);
        }
    } 

    public void initializeR(){
        double rVal = 0;
        rVector =  new double[numVertice];
        if(initialvalue == 0){
            rVal = 0;
        }
        else if(initialvalue == 1){
            rVal = 1;
        }
        else if(initialvalue == -2){
            rVal = 1/Math.sqrt(numVertice);
        }
        else{
            rVal = Math.pow(numVertice, -1);
        }
        // System.out.println("Initialized Rank Matrix");
        System.out.print("Base : 0 : ");
        for(int i = 0; i < numVertice; i++){
            rVector[i] = rVal;
            System.out.print("P["+ i + "] --> " + rVector[i] + "\t");
        }
        System.out.print("\n");
    }

    public void getOutDegree(){
        outDegreeMatrix = new int[numVertice];
        for(int x = 0; x < edges.size(); x++){
            outDegreeMatrix[edges.get(x).get(0)]++;
        }
    }

    public void getAdjacencyM(){
        AdjacencyMatrix = new double[numVertice][numVertice];
        for(int x = 0; x < edges.size(); x++){
            int m = outDegreeMatrix[edges.get(x).get(0)];
            AdjacencyMatrix[edges.get(x).get(1)][edges.get(x).get(0)] = (m == 0)? 0 : Math.pow(m, -1);
        }
    }

    public void showAM(){
        System.out.println("Adjacency List Matrix");
        for(int row = 0; row < AdjacencyMatrix.length; row++){
            for(int col = 0; col < AdjacencyMatrix.length; col++){
                System.out.print(AdjacencyMatrix[row][col] + " ");
            }
            System.out.print("\n");
        }
    }

    public double[] PageRank(double[][] AdjMat, double[] r){
        double[] result = new double[numVertice]; 
        for(int row = 0; row < AdjMat.length; row++){    
            double sumPR_related = 0;
            for(int addops = 0; addops < r.length; addops++){
                sumPR_related+= (AdjMat[row][addops] * r[addops]);         
            }
            result[row] = (((1-d)/numVertice) + (d*sumPR_related));
        }
        return result;
    }

    public void printR(int c){
        System.out.print("Iter : " + c + " : ");
        for(int i = 0; i < rVector.length ; i++){
            System.out.print("P["+ i + "] --> " + rVector[i] + "\t");
        }
        System.out.println("\n");
    }

    public boolean checkConvergence(double[] r, double e){
        int convCount = 0;
        for(int i = 0; i < rVector.length ; i++){
           if(rVector[i] - r[i] < e){
               convCount++;
           }
        }
        if(convCount == r.length){
            return true;
        }else{
            return false;
        }
    }

    public static void main(String[] args) {
        // SET-UP LOCAL VARIABLES
        int iter = 0;
        int count = 0;
        double ErrorRate = 0;
        double[] newR_MAT;
        boolean isConverged = false;
        pgrk_6777 pg = new pgrk_6777(Integer.parseInt(args[0]), Integer.parseInt(args[1]), args[2]);

        if(pg.iterations < 0){
            iter = 1000;
            ErrorRate = Math.pow(10, pg.iterations);
        }else if(pg.iterations == 0){
            iter = 1000;
            ErrorRate = Math.pow(10, -5);
        }
        else{
            iter = pg.iterations;
            ErrorRate = Math.pow(10, -5);
        }
        
        // GET GRAPH INPUT
        pg.GetInput(pg.filename);
        // GENERATE OUTDEGREE VECTOR
        pg.getOutDegree();
        // GENERATE ALM
        pg.getAdjacencyM();
        // pg.showAM();

        // INITIAL SETUP FOR PAGE RANK VECTOR
        pg.initializeR();
        // LOOP TILL ITER != 0 && ERROR > ERROR_RATE
        while(iter != 0 && !isConverged){
            // Here Goes the PageRank Algo 
            newR_MAT = pg.PageRank(pg.AdjacencyMatrix,pg.rVector);
            // Check if converged using new R MATRIX &  previous R MATRIX values to return a boolean
            isConverged = pg.checkConvergence(newR_MAT, ErrorRate);
            if(isConverged){
                isConverged = true;
            }else{
                iter--;
                count++;
                pg.rVector = newR_MAT;
                pg.printR(count);
            }
        }
    }
}