package ons.solubility.rdf;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import ons.solubility.data.Measurement;
import ons.solubility.data.SolubilityData;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC_11;
import com.hp.hpl.jena.vocabulary.RDF;

public class ConvertToRDF {
    
    private Model model;
    private int measurementsProcessed;
    private int solutesProcessed;
    private int solventsProcessed;

    private Map<String,Resource> solvents;
    private Map<String,Resource> solutes;

    public ConvertToRDF() {
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
        
        SolubilityData data = new SolubilityData(username, password);
        data.download();
        for (Measurement measurement : data.getData()) {
            createResource(measurement);
        }
    }
    
    private void createResource(Measurement mData) {
        // create the resource
        String url = ONS.NS;
        Resource measurement = model.createResource(url + "measurement" + measurementsProcessed);
//        // FIXME: how can I set the rdf:type?? that is, have ons:Measurement??
        measurement.addProperty(RDF.type, ONS.Measurement);
        measurement.addProperty(ONS.experiment, model.createResource(url + "experiment" + mData.getExperiment()));
        String soluteName = removeQuotes(mData.getSolute());
        Resource solute = solutes.get(soluteName);
        if (solute == null) {
            solute = model.createResource(url + "solute" + solutesProcessed);
            solute.addProperty(RDF.type, ONS.Solute);
            if (mData.getSolute() != null)
                solute.addProperty(DC_11.title, mData.getSolute());
            if (mData.getSoluteSMILES() != null)
                solute.addProperty(BO.smiles, mData.getSoluteSMILES());
            solutes.put(soluteName, solute);
            solutesProcessed++;
        }
        measurement.addProperty(ONS.solute, solute);
        String solventName = removeQuotes(mData.getSolvent());
        Resource solvent = solutes.get(solventName);
        if (solvent == null) {
            solvent = model.createResource(url + "solvent" + solventsProcessed);
            solvent.addProperty(RDF.type, ONS.Solvent);
            solvent.addProperty(DC_11.title, mData.getSolvent().trim());
            solvent.addProperty(BO.smiles, mData.getSolventSMILES().trim());
            solvents.put(solventName, solvent);
            solventsProcessed++;
        }
        measurement.addProperty(ONS.solvent, solvent);
        measurementsProcessed++;
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

    public void write() {
        model.setNsPrefix("ons", ONS.NS);
        model.setNsPrefix("chem", BO.NS);
        try {
            model.write(System.out);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void main( String[] args ) throws Exception {
        ConvertToRDF convertor = new ConvertToRDF();
        convertor.processData();
        convertor.write();
    }

}
