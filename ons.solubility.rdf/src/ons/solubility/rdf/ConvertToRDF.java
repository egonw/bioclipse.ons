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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sf.jniinchi.INCHI_RET;
import ons.solubility.data.Measurement;
import ons.solubility.data.SolubilityData;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC_11;
import com.hp.hpl.jena.vocabulary.RDF;

public class ConvertToRDF {
    
    private SmilesParser smilesParser;
    private InChIGeneratorFactory inchiFactory;
    
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

        smilesParser = new SmilesParser(NoNotificationChemObjectBuilder.getInstance());
        inchiFactory = new InChIGeneratorFactory();
    }

    public void processData() throws Exception {
        Properties userInfo = new Properties();
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("userinfo.properties");
        userInfo.load(input);
        String username = userInfo.getProperty("username");
        String password = userInfo.getProperty("password");
        
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
        measurementsProcessed++;
    }

    private Resource getSolventResource(Measurement mData) {
        String solventName = removeQuotes(mData.getSolvent());
        Resource solvent = solvents.get(solventName);
        if (solvent == null) {
            solvent = model.createResource(ONS.NS + "solvent" + solventsProcessed);
            solvent.addProperty(RDF.type, ONS.Solvent);
            if (mData.getSolvent() != null) {
                solvent.addProperty(DC_11.title, mData.getSolvent().trim());
            }
            String SMILES = mData.getSolventSMILES().trim();
            if (SMILES != null) {
                solvent.addProperty(BO.smiles, SMILES);
                String inchi = getInChI(SMILES);
                if (inchi != null) solvent.addProperty(BO.inchi, inchi);
            }
            solvents.put(solventName, solvent);
            solventsProcessed++;
        }
        return solvent;
    }

    private String getInChI(String SMILES) {
        try {
            IAtomContainer container = smilesParser.parseSmiles(SMILES);
            InChIGenerator inchiGenerator =
                inchiFactory.getInChIGenerator(container);
            INCHI_RET ret = inchiGenerator.getReturnStatus();
            if (ret == INCHI_RET.WARNING) {
                // InChI generated, but with warning message
                System.out.println("InChI warning: " + inchiGenerator.getMessage());
            } else if (ret != INCHI_RET.OKAY) {
                // InChI generation failed
                throw new CDKException("InChI failed: " + ret.toString()
                                       + " [" + inchiGenerator.getMessage() + "]");
            }
            return inchiGenerator.getInchi();
        } catch ( InvalidSmilesException e ) {
            System.out.println("Error in parsing SMILES: " + SMILES);
            // e.printStackTrace();
        } catch ( CDKException e ) {
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
                if (inchi != null) solute.addProperty(BO.inchi, inchi);
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

        model.setNsPrefix("ons", ONS.NS);
        model.setNsPrefix("chem", BO.NS);
        try {
            model.write(out, "N-TRIPLE");
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
