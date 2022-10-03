package one_max_de;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class DE {
    
    // list of generated populations
    List<Individual> population;
    
    // populiation size to generate at the begining 
    int POPULATION_SIZE = 10;
    
    // crossover probability [0,1]
    double CROSSOVER_PROBABILITY = 0.4;
    
    // differential weight [0,2]
    double DIFFERENTIAL_WEIGHT = 1;
 
    // length of chromosome = dimensions
    static int CHROMOSOME_LENGTH = 100;

    // number of iteration
    int ITERATION_NO = 50;
    
    // random number generator
    Random random;
    
   // PrintWriter pw;  
    
    public static void main(String[] args){
        
        // define lower/ upper bounds for each required dimensions
		int[] dimentionsBound = new int[2];

    	for (int d=0;d<CHROMOSOME_LENGTH;d++) {
    		dimentionsBound[0]=0;
    		dimentionsBound[1]=1;
    	}
    	
        List<int[]> dimensionList = new LinkedList<>();
        /*
        // define lower/ upper bounds for 1st dimension
        double[] dimension1Bounds = new double[2];
        dimension1Bounds[0] = 1.0d;
        dimension1Bounds[1] = 10.0d;
        
        // define lower/ upper bounds for 2nd dimension
        double[] dimension2Bounds = new double[2];
        dimension2Bounds[0] = 80.0d;
        dimension2Bounds[1] = 160.0d;
        
        // define lower/ upper bounds for 3rd dimension
        double[] dimension3Bounds = new double[2];
        dimension3Bounds[0] = 100.0d;
        dimension3Bounds[1] = 200.0d;

        // add all dimension to a list, and this will be passed to DE
        List<double[]> dimensionListt = new LinkedList<>();*/
        
        for (int d=0;d<CHROMOSOME_LENGTH;d++) {
            dimensionList.add(dimentionsBound);

    	}
        //dimensionListt.add(dimension1Bounds);
        //dimensionListt.add(dimension2Bounds);
        //dimensionListt.add(dimension3Bounds);
        
        DE de = new DE();
        
        // start optimising process and return the best candiate after number of spcecified iteration
        System.out.println("Best combination found: " + de.optimise(dimensionList));             
    }
    
    // fitness function
    public int fitFunction(Individual aCandidate){
       
    	/*
        // Rastrigin function
        double value = 10.0d * aCandidate.dataValue.length;
        
        for(int i = 0; i < aCandidate.dataValue.length; i++){
            value = value + Math.pow(aCandidate.dataValue[i], 2.0) - 10.0 * Math.cos(2 * Math.PI * aCandidate.dataValue[i]); 
        }
        
//        double[] values = aCandidate.dataValue; 
//        double value = 100 * Math.pow(Math.pow(values[0], 2) - values[1], 2) + Math.pow(1 - values[0], 2);
        */
    	int value=0;
    	for(int i = 0; i < aCandidate.dataValue.length; i++){
    		if(aCandidate.dataValue[i]==1)
    			value+=1;
    	}
        return value;
    }
        
    // DE constructor
    public DE(){ 
        random = new Random();
        population = new LinkedList<>();
    }
    
    public Individual optimise(List<int[]> dimensionList){
         
        // generate population up to the define limit
        for(int i = 0; i < POPULATION_SIZE; i++){
            Individual individual = new Individual(dimensionList);
            population.add(individual);
            
        }       
                
       // try more than one iteration 
       for(int iterationCount = 0; iterationCount < ITERATION_NO; iterationCount++){
           
            /*try {
                pw = new PrintWriter(new File("data/popoluation_" + Integer.toString(iterationCount) +".csv"));
            } catch (FileNotFoundException ex) {
                
            } */   
            
            
           /* for(int n = 0; n < dimensionList.size(); n++){      
            	System.out.println("v" + Integer.toString(n));
            	System.out.println(",");
            }
        
            System.out.println("fValue");

            System.out.println("\n");*/
        
            for (Individual individual : population) {
            	System.out.print(individual.toString());
            	//System.out.println(",");
            	System.out.println("    " +Integer.toString(fitFunction(individual)));
            	//System.out.println("\n");
            }
            
            //pw.flush();
            int loop = 0;
        
            // main loop for evolution
            while(loop < population.size()){       

            Individual original = null;
            Individual candidate = null;
            boolean boundsHappy;

            do{
                boundsHappy = true;
                // pick an agent from the the population
                int x = loop;
                int a,b,c = -1;

                // pick three random agents from the population
                // make sure that they are not identical to selected agent from
                // the population 

                do{
                    a = random.nextInt(population.size());
                }while(x == a);
                do{
                    b = random.nextInt(population.size());
                }while(b==x || b==a);
                do{
                    c = random.nextInt(population.size());
                }while(c == x || c == a || c == b);

                // create three agent individuals
                Individual individual1 = population.get(a);
                Individual individual2 = population.get(b);
                Individual individual3 = population.get(c);

                // create a noisy random candidate
                Individual noisyRandomCandicate = new Individual(dimensionList);

                // mutation process
                // if an element of the trial parameter vector is
                // found to violate the bounds after mutation and crossover, it is reset in such a way that the bounds
                // are respected (with the specific protocol depending on the implementation)
                double randomR = random.nextDouble();
                for(int n = 0; n < dimensionList.size(); n++){     
                    noisyRandomCandicate.dataValue[n] = (int) (individual1.dataValue[n] + DIFFERENTIAL_WEIGHT * (individual2.dataValue[n] - individual3.dataValue[n]));               
                    if(noisyRandomCandicate.dataValue[n]!=0 && noisyRandomCandicate.dataValue[n]!=1 )
                    {
                    	if(1/(1+Math.exp((-1)*noisyRandomCandicate.dataValue[n])) < randomR)
                    		noisyRandomCandicate.dataValue[n]=0;
                    	else
                    		noisyRandomCandicate.dataValue[n]=1;
                    }
                    	
                }           

                // Create a trial candicate 
                original = population.get(x);
                candidate = new Individual(dimensionList);

                // copy values from original agent to the candidate agent
                for(int n = 0; n < dimensionList.size(); n++){             
                    candidate.dataValue[n] = original.dataValue[n];
                }  

                // crossver process with the selected individual
                // pick a random dimension, which defintely takes the value from the noisy random candidate
                int R = random.nextInt(dimensionList.size());

                for(int n = 0; n < dimensionList.size(); n++){

                    double crossoverProbability = random.nextDouble();

                    if(crossoverProbability < CROSSOVER_PROBABILITY || n == R){
                        candidate.dataValue[n] = noisyRandomCandicate.dataValue[n];
                    }

                }
            	//System.out.println(candidate.toString());

                // check here if the trial candiate satisfies bounds for each value
                for(int n = 0; n < dimensionList.size(); n++){ 
                    if(candidate.dataValue[n] < dimensionList.get(n)[0] || candidate.dataValue[n] > dimensionList.get(n)[1]){
                       boundsHappy = false;
                    }
                }

            }while(boundsHappy == false);

                //see if the candidate is better than original, if so replace it
                if(fitFunction(original) < fitFunction(candidate)){
                        population.remove(original);
                        population.add(candidate);     
                }
                loop++;
            }        
        }
        
       Individual bestFitness = new Individual(dimensionList);
   
       // selecting the final best agent from the the population
       for(int i = 0; i < population.size(); i++){
           Individual individual = population.get(i);
           
            if(fitFunction(bestFitness) < fitFunction(individual)){
                
               try {
                   bestFitness = (Individual) individual.clone();
               } catch (CloneNotSupportedException ex) {
                  
               }
            }
       }
       
        
       return bestFitness;
    }
            
    public class Individual implements Cloneable{
      
        // each element a value from valid range for a given dimension
        int[] dataValue;
        
        public Individual(List<int[]> dimensionIn){
            int noDimension = dimensionIn.size();       
            // initialse data vector
            dataValue = new int[noDimension];
                 
            // for each dimension, create corresponding data point between given range
            for(int dimensionIndex = 0; dimensionIndex < noDimension; dimensionIndex++){
                
                int dimensionLowerBound = dimensionIn.get(dimensionIndex)[0];
                int dimensionUpperBound = dimensionIn.get(dimensionIndex)[1];        
                
                
                dataValue[dimensionIndex] = random.nextInt((dimensionUpperBound - dimensionLowerBound) + 1) + dimensionLowerBound;
            }
        }
        @Override
        public String toString(){
            
            String string = "";
            
            for (int i = 0; i < dataValue.length; i++) {
                string += Integer.toString(dataValue[i]);
                
                /*if((i + 1) != dataValue.length){
                    string += ",";
                }*/
            }
            
            return string;
        }
        
        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone(); //To change body of generated methods, choose Tools | Templates.
        }
    }

    

}
