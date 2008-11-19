package ons.solubility.data;

public class Measurement {

    private String experiment;
    private String sample;
    private String reference;
    private String solute;
    private String solvent;
    private String soluteSMILES;
    private String solventSMILES;
    private String concentration;

    public String getExperiment() {
        return experiment;
    }

    public void setExperiment( String experiment ) {
        this.experiment = experiment;
    }

    public String getReference() {
        return reference;
    }

    public void setReference( String reference ) {
        this.reference = reference;
    }
    
    public String getSolute() {
        return solute;
    }
    
    public void setSolute( String solute ) {
        this.solute = solute;
    }
    
    public String getSolvent() {
        return solvent;
    }
    
    public void setSolvent( String solvent ) {
        this.solvent = solvent;
    }
    
    public String getSoluteSMILES() {
        return soluteSMILES;
    }
    
    public void setSoluteSMILES( String soluteSMILES ) {
        this.soluteSMILES = soluteSMILES;
    }

    public String getSolventSMILES() {
        return solventSMILES;
    }

    public void setSolventSMILES( String solventSMILES ) {
        this.solventSMILES = solventSMILES;
    }

    public String getConcentration() {
        return concentration;
    }

    public void setConcentration( String concentration ) {
        this.concentration = concentration;
    }

    public String getSample() {
        return sample;
    }

    public void setSample( String sample ) {    
        this.sample = sample;
    }

}
