//Author: Jordan Micah Bennett
import java.util.ArrayList;
import java.io.Serializable;

public class Neuron implements Serializable
{
    //establish features
    private ArrayList <Synapse> synapses;
    private int sequentiallyHierarchicallyHorizontalInputWeightCardinality;
    private int identifierIndex;
    private double eta; //eta(?) {0.0|0.2|1.0 = slow,medium,erratic} -> overall learnment rate
    private double alpha; //alpha(?) {0.0|0.5 = none,moderate} - momentum
    private double outcome;
    private double gradient;
    
    //define constructor
    public Neuron ( int sequentiallyHierarchicallyHorizontalInputWeightCardinality, int identifierIndex, double eta, double alpha )
    {
        //define features
        this.sequentiallyHierarchicallyHorizontalInputWeightCardinality = sequentiallyHierarchicallyHorizontalInputWeightCardinality;
        this.identifierIndex = identifierIndex;
        this.eta = eta;
        this.alpha = alpha;
        gradient = 1.0;
        
        synapses = new ArrayList <Synapse> ( );
        for ( int sI = 0; sI < sequentiallyHierarchicallyHorizontalInputWeightCardinality; sI ++ ) 
        {
            synapses.add ( new Synapse ( ) );
            synapses.get ( sI ).setWeight ( new java.util.Random ( ).nextDouble ( ) + -1 );
        }
    }
    
    
    //define methods
        //define accessors
        public double getOutcome ( )
        {
            return outcome;
        }
        
        public ArrayList <Synapse> getSynapses ( )
        {
            return synapses;
        }
        
        public double getGradient ( )
        {
            return gradient;
        }
        
        public int getIdentifierIndex ( )
        {
            return identifierIndex;
        }
        
        public double getDistributedWeightSigma ( CorticalColumn subsequentCorticalColumn )
        {
            double returnValue = 0.0;
            
            //{sCCI - subsequent cortical column neuron iterator}
            for ( int sCCI = 0; sCCI < subsequentCorticalColumn.size ( ) - 1; sCCI ++ ) //...for [structure].size ( ) - 1 maintains bias neuron exclusion internally. This enables threshold neuron constant-ness. [bias neuron computation exclusion requires hereafter, hither-to global Neuron neuron bias exclusion, as hierarchically later references (maintain this event)]
                returnValue += getSynapses ( ).get ( sCCI ).getWeight ( ) * subsequentCorticalColumn.get ( sCCI ).getGradient ( ); //Absent threshold constantness aligned network control, the network surjectively non-generalizes, as priorly computed sigmas of calculations effectively generate linearly surjective, non varying outcomes. A non-differentiable network is futile.
                
            return returnValue;
        }
        
        public double getActivation ( double value ) //activation -> hyperbolic tangent function -> tanh ( value ) { ( e^x - e^-x ) / ( e^x + e^-x ) }
        {
            return Math.tanh ( value );
        }
        
        public double getPrimeActivation ( double value ) //differentiated activation -> hyperbolic tangent function -> 1 - ( tanh ( value ) * tanh ( value ) )
        {
            return 1 - ( Math.tanh ( value ) * Math.tanh ( value ) );
        }
        
        //define mutators
        public void setOutcome ( double value )
        {
            outcome = value;
        }
        
        public void setGradient ( double value )
        {
            gradient = value;
        }

        public void computeHiddenGradient ( CorticalColumn subsequentCorticalColumn )
        {
            double distributedWeightSigma = getDistributedWeightSigma ( subsequentCorticalColumn );

            setGradient ( distributedWeightSigma * getPrimeActivation ( getOutcome ( ) ) ); //chain-rule aligned partial differentiation par net error computation, par eta mutation, {composite over progressivePropagation partial differentiation at bottom}
        }
        
        public void computeOutcomeGradient ( double value )
        {
            double delta = value - getOutcome ( );
            setGradient ( delta * getPrimeActivation ( getOutcome ( ) ) ); //chain-rule aligned partial differentiation par net error computation, par eta mutation {composite over progressivePropagation partial differentiation at bottom}
        }
        
        public void update ( CorticalColumn priorCorticalColumn )
        {
            //{pCCNI - prior cortical column neuron iterator}
            try
            {
                for ( int pCCNI = 0; pCCNI < priorCorticalColumn.size ( ); pCCNI ++ )
                {
                    double priorDeltaWeight = priorCorticalColumn.get ( pCCNI ).getSynapses ( ).get ( identifierIndex ).getDeltaWeight ( );
                    
                    double newDeltaWeight = ( eta * getGradient ( ) * priorCorticalColumn.get ( pCCNI ).getOutcome ( ) ) + ( alpha * priorDeltaWeight );
                    
                    priorCorticalColumn.get ( pCCNI ).getSynapses ( ).get ( identifierIndex ).setDeltaWeight ( newDeltaWeight );
                    priorCorticalColumn.get ( pCCNI ).getSynapses ( ).get ( identifierIndex ).setWeight ( priorCorticalColumn.get ( pCCNI ).getSynapses ( ).get ( identifierIndex ).getWeight ( ) + newDeltaWeight );
                }
            }
            catch ( Exception error )
            {
            }
        } 
        
        public void propagateProgressively ( CorticalColumn priorCorticalColumn )
        {
            double sigma = 0.0;
            
            //{pCCNI - prior cortical column neuron iterator}
            for ( int pCCNI = 0; pCCNI < priorCorticalColumn.size ( ); pCCNI ++ )
                sigma += priorCorticalColumn.get ( pCCNI ).getOutcome ( ) * priorCorticalColumn.get ( pCCNI ).getSynapses ( ).get ( identifierIndex ).getWeight ( );
     
            setOutcome ( getActivation ( sigma ) ); //partial differentiation par net error computation, par eta mutation {regressivePropagation partial differentiation at top}
        }
}