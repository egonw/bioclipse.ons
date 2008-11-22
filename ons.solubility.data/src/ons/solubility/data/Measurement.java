/* Copyright (C) 2008  Egon Willighagen <egon.willighagen@gmail.com>
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * All we ask is that proper credit is given for our work, which includes
 * - but is not limited to - adding the above copyright notice to the beginning
 * of your source code files, and to any copyright notice that you may distribute
 * with programs based on this work.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package ons.solubility.data;

import java.io.Serializable;

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
