package one_max_de;

import java.util.List;
import java.util.Random;

public class Individual implements Cloneable{
    
    // each element a value from valid range for a given dimension
    int[] dataValue;
    
    Random random;
    
    public Individual(List<int[]> dimensionIn){
    	random = new Random();
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