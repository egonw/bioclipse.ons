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
package ons.solubility.rdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.bioclipse.cdk.business.ICDKManager;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IMolecule;
import net.bioclipse.inchi.InChI;
import net.bioclipse.inchi.business.Activator;
import net.bioclipse.inchi.business.IInChIManager;
import ons.solubility.data.Measurement;
import ons.solubility.data.SolubilityData;

import org.dbpedia.rdf.ONS2DBPediaMappings;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC_11;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class ConvertToRDF {
    
    private final String RON = "http://rdf.openmolecules.net/?";

    private Model model;
    private int measurementsProcessed;
    private int solutesProcessed;
    private int solventsProcessed;

    private Map<String,Resource> solvents;
    private Map<String,Resource> solutes;

    public ConvertToRDF() throws Exception {
        model = ModelFactory.createDefaultModel();
        measurementsProcessed = 0;
        solvents = new HashMap<String,Resource>();
        solutes = new HashMap<String,Resource>();
        solutesProcessed = 0;
        solventsProcessed = 0;
    }

    public void processData() throws Exception {
        Properties userInfo = new Properties();
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("userinfo.properties");
        userInfo.load(input);
        String username = userInfo.getProperty("username");
        String password = userInfo.getProperty("password");
        processData(username, password);
    }

    public void processData(String username, String password) throws Exception {
        SolubilityData data = new SolubilityData(username, password);
        data.download();
        for (Measurement measurement : data.getData()) {
            if (measurement.getExperiment().trim().equals("0")) continue;
            if (measurement.getSolute().contains("DONOTUSE")) continue;

            createResource(measurement);
        }
    }
    
    private void createResource(Measurement mData) {
        // create the resource
        String url = ONS.NS;
        Resource measurement = model.createResource(url + "measurement" + measurementsProcessed);
        measurement.addProperty(RDF.type, ONS.Measurement);
        measurement.addProperty(ONS.experiment, model.createResource(url + "experiment" + mData.getExperiment()));
        measurement.addProperty(ONS.solute, getSoluteResource(mData));
        measurement.addProperty(ONS.solvent, getSolventResource(mData));
        measurement.addProperty(ONS.solubility, mData.getConcentration());
        measurementsProcessed++;
    }

    private Resource getSolventResource(Measurement mData) {
        String solventName = removeQuotes(mData.getSolvent());
        Resource solvent = solvents.get(solventName);
        if (solvent == null) {
            solvent = model.createResource(ONS.NS + "solvent" + solventsProcessed);
            solvent.addProperty(RDF.type, ONS.Solvent);
            if (mData.getSolvent() != null) {
                solventName = solventName.trim();
                solvent.addProperty(DC_11.title, solventName);
                String dbpResource = ONS2DBPediaMappings.getDBPediaResource(solventName);
                if (dbpResource != null) {
                    solvent.addProperty(OWL.sameAs, model.createResource(dbpResource));
                }
            }
            String SMILES = mData.getSolventSMILES().trim();
            if (SMILES != null) {
                solvent.addProperty(BO.smiles, SMILES);
                String inchi = getInChI(SMILES);
                if (inchi != null) {
                    solvent.addProperty(BO.inchi, inchi);
                    solvent.addProperty(OWL.sameAs,
                            model.createResource(RON + inchi));
                }
            }
            solvents.put(solventName, solvent);
            solventsProcessed++;
        }
        return solvent;
    }

    private String getInChI(String SMILES) {
        try {
        	ICDKManager cdk = net.bioclipse.cdk.business.Activator.getDefault().getJavaCDKManager();
        	IMolecule container = cdk.fromSMILES(SMILES);
            IInChIManager inchi = Activator.getDefault().getJavaInChIManager();
            InChI inchiVal = inchi.generate(container);
            if (inchiVal == InChI.FAILED_TO_CALCULATE) {
                // InChI generated, but with warning message
                System.out.println("InChI failed to calculate");
            }
            return inchiVal.getValue();
        } catch (BioclipseException e) {
            System.out.println("Error in parsing SMILES: " + SMILES);
            // e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error in creating InChI for SMILES: " + SMILES);
            // e.printStackTrace();
        }
        return null;
    }

    private Resource getSoluteResource(Measurement mData) {
        String soluteName = removeQuotes(mData.getSolute());
        Resource solute = solutes.get(soluteName);
        if (solute == null) {
            solute = model.createResource(ONS.NS + "solute" + solutesProcessed);
            solute.addProperty(RDF.type, ONS.Solute);
            if (mData.getSolute() != null)
                solute.addProperty(DC_11.title, mData.getSolute());
            String SMILES = mData.getSoluteSMILES().trim();
            if (SMILES != null) {
                solute.addProperty(BO.smiles, SMILES);
                String inchi = getInChI(SMILES);
                if (inchi != null) {
                    solute.addProperty(BO.inchi, inchi);
                    solute.addProperty(RDFS.isDefinedBy,
                            model.createResource(RON + inchi));
                }
            }
            solutes.put(soluteName, solute);
            solutesProcessed++;
        }
        return solute;
    }

    private String removeQuotes(String string) {
        if (string == null || string.length() == 0) return string;
        String result = string;
        if (result.charAt(0) == '"') {
            result = result.substring(1);
        }
        if (result.charAt(result.length()-1) == '"') {
            result = result.substring(0, result.length()-1);
        }
        return result;
    }

    public void write() throws Exception {
        File outputFile = new File("ons.rdf");
        FileOutputStream out = new FileOutputStream(outputFile);
        write(out);
    }

    public void write(OutputStream out) throws Exception {
        model.setNsPrefix("ons", ONS.NS);
        model.setNsPrefix("chem", BO.NS);
        try {
            model.write(out, "RDF/XML-ABBREV");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        out.close();
    }

    public static void main( String[] args ) throws Exception {
        ConvertToRDF convertor = new ConvertToRDF();
        convertor.processData();
        convertor.write();
    }

}
