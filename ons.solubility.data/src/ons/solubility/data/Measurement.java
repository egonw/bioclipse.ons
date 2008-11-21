package ons.solubility.data;

public class Measurement
    {
    private String experiment=null;
    private String sample=null;
    private String reference=null;
    private String solute=null;
    private String solvent=null;
    private String soluteSMILES=null;
    private String solventSMILES=null;
    private String concentration=null;

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
