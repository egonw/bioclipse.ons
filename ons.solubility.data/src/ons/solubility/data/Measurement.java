package ons.solubility.data;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Measurement
 */
@XmlRootElement(name="Measurement",namespace=Measurement.NS)
public class Measurement
	implements Serializable
    {
	private static final long serialVersionUID = 1L;
	public static final String NS="http://spreadsheet.google.com/plwwufp30hfq0udnEmRD1aQ/onto#";
    private String experiment=null;
    private String sample=null;
    private String reference=null;
    private String solute=null;
    private String solvent=null;
    private String soluteSMILES=null;
    private String solventSMILES=null;
    private String concentration=null;

    public Measurement()
    	{
    	}
    
    
    @XmlElement(name="experiment",namespace=Measurement.NS,nillable=true)
    public String getExperiment() {
        return experiment;
    }

    public void setExperiment( String experiment ) {
        this.experiment = experiment;
    }

    @XmlElement(name="reference",namespace=Measurement.NS,nillable=true)
    public String getReference() {
        return reference;
    }

    public void setReference( String reference ) {
        this.reference = reference;
    }
    
    @XmlElement(name="solute",namespace=Measurement.NS,nillable=true)
    public String getSolute() {
        return solute;
    }
    
    public void setSolute( String solute ) {
        this.solute = solute;
    }
    
    @XmlElement(name="solvent",namespace=Measurement.NS,nillable=true)
    public String getSolvent() {
        return solvent;
    }
    
    public void setSolvent( String solvent ) {
        this.solvent = solvent;
    }
    
    @XmlElement(name="soluteSMILES",namespace=Measurement.NS,nillable=true)
    public String getSoluteSMILES() {
        return soluteSMILES;
    }
    
    public void setSoluteSMILES( String soluteSMILES ) {
        this.soluteSMILES = soluteSMILES;
    }

    @XmlElement(name="solventSMILES",namespace=Measurement.NS,nillable=true)
    public String getSolventSMILES() {
        return solventSMILES;
    }

    public void setSolventSMILES( String solventSMILES ) {
        this.solventSMILES = solventSMILES;
    }

    @XmlElement(name="concentration",namespace=Measurement.NS,nillable=true)
    public String getConcentration() {
        return concentration;
    }

    public void setConcentration( String concentration ) {
        this.concentration = concentration;
    }

    @XmlElement(name="sample",namespace=Measurement.NS,nillable=true)
    public String getSample() {
        return sample;
    }

    public void setSample( String sample ) {    
        this.sample = sample;
    }

}
