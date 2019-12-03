// JUSTINE AYROOR    cs610 6777 prp

import java.io.*;
import java.util.*;

class hits_6777{
    private final int iterations;
    private final int initialvalue;
    private final String filename;
    public int numEdge;
    public int numVertice;
    public int[] outDegreeMatrix;
    public File file;
    public BufferedReader br;
    public List<ArrayList<Integer>> edges = new ArrayList<ArrayList<Integer>>();
    public double[] authVector;
    public double[] hubVector;
    public double[][] AdjacencyMatrix;

    public hits_6777(int iterations, int initialvalue, String filename) {
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

    public void initializeAuth_Hub(){
        double rVal = 0;
        authVector =  new double[numVertice];
        hubVector =  new double[numVertice];
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
            authVector[i] = rVal;
            hubVector[i] = rVal;
            System.out.print("A/H["+ i + "] --> " + authVector[i] + "/" + hubVector[i] +"\t");
        }
        System.out.print("\n");
    }
    
    // RETURN NEW AUTH & HUB VALUES 
    public double[] AuthStep(double[] HubValues){
        double[] updatedAuth = new double[AdjacencyMatrix.length];
        double ANormFactor = 0;
        // AdjacencyMatrix(T) * OldHubValues
        for(int row = 0; row < AdjacencyMatrix.length; row++){
            double sumHits_related = 0;
            for(int col = 0; col < HubValues.length; col++){
                sumHits_related += (AdjacencyMatrix[row][col] * HubValues[col]);
            }
            updatedAuth[row] = sumHits_related;
            ANormFactor += updatedAuth[row];
        }
        updatedAuth = normalize(updatedAuth, ANormFactor);
        return updatedAuth;
    }
    
    public double[] HubStep(double[] AuthValues){
        double[] updatedHub = new double[AdjacencyMatrix.length];
        double HNormFactor = 0;
         // AdjacencyMatrix  * OldAuthValues
         for(int row = 0; row < AdjacencyMatrix.length; row++){
            double sumHits_related = 0;
            for(int col = 0; col < AuthValues.length; col++){
                sumHits_related += (AdjacencyMatrix[col][row] * AuthValues[col]);
            }
            updatedHub[row] = sumHits_related;
            HNormFactor += updatedHub[row];
        }
        updatedHub = normalize(updatedHub, HNormFactor);
        return updatedHub;
    }

    public double[] normalize(double[] Values, double NF){
        for(int node = 0; node < Values.length; node++){
            Values[node] = Values[node]/NF;
        }
        return Values;
    }
    // CALL AUTH AND HUB STEP ON EVERY ITERATION
    public void KHits(double[] HubValues,double[] AuthValues){
        authVector = AuthStep(HubValues);
        hubVector = HubStep(AuthValues);
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
            AdjacencyMatrix[edges.get(x).get(1)][edges.get(x).get(0)] = (m == 0)? 0 : 1;
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


    public void printAuth_Hub(int c){
        System.out.print("Iter : " + c + " : ");
        for(int i = 0; i < authVector.length ; i++){
            System.out.print("A/H["+ i + "] --> " + authVector[i] + " / " + hubVector[i] + "\t");
        }
        System.out.println("\n");
    }

    public boolean checkConvergence(double[] a,double[] h, double e){
        int AconvCount = 0;
        int HconvCount = 0;
        for(int i = 0; i < authVector.length ; i++){
           if(Math.abs(authVector[i] - a[i]) < e){
               AconvCount++;
           }
        }
        for(int i = 0; i < hubVector.length ; i++){
            if(Math.abs(hubVector[i] - h[i]) < e){
                HconvCount++;
            }
         }
        if((AconvCount == authVector.length) && (HconvCount == hubVector.length)){
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
        boolean isConverged = false;
        hits_6777 hit = new hits_6777(Integer.parseInt(args[0]), Integer.parseInt(args[1]), args[2]);
        if(hit.iterations < 0){
            iter = 1000;
            ErrorRate = Math.pow(10, hit.iterations);
        }else if(hit.iterations == 0){
            iter = 1000;
            ErrorRate = Math.pow(10, -5);
        }
        else{
            iter = hit.iterations;
            ErrorRate = Math.pow(10, -5);
        }
        
        // GET GRAPH INPUT
        hit.GetInput(hit.filename);
        // GENERATE OUTDEGREE VECTOR
        hit.getOutDegree();
        // GENERATE ALM
        hit.getAdjacencyM();
        hit.showAM();
        hit.initializeAuth_Hub();
        // LOOP TILL ITER != 0 && ERROR > ERROR_RATE
        double[] OldAuth = hit.authVector;
        double[] OldHub = hit.hubVector; 
        while(iter != 0 && !isConverged){
            // Here Goes the HITS Algo
            hit.KHits(OldHub,OldAuth);
            // Check if converged using new R MATRIX &  previous R MATRIX values to return a boolean
            isConverged = hit.checkConvergence(OldAuth,OldHub,ErrorRate);
            if(isConverged){
                isConverged = true;
            }else{
                iter--;
                count++;
                OldAuth = hit.authVector;
                OldHub = hit.hubVector; 
                hit.printAuth_Hub(count);
            }
        }
    }
}