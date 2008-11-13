package ons.solubility.rdf;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

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

    public void processStream(InputStream stream) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        /*String header = */reader.readLine();
        String line = reader.readLine();
        while (line != null) {
            createResource(line);
            line = reader.readLine();
        }
    }
    
    private void createResource(String line) {
        // create the resource
        String[] field = line.split(",");
        String url = ONS.NS;
        Resource measurement = model.createResource(url + "measurement" + measurementsProcessed);
        // FIXME: how can I set the rdf:type?? that is, have ons:Measurement??
        measurement.addProperty(RDF.type, ONS.Measurement);
        measurement.addProperty(ONS.experiment, model.createResource(field[2]));
        String soluteName = removeQuotes(field[3]);
        Resource solute = solutes.get(soluteName);
        if (solute == null) {
            solute = model.createResource(url + "solute" + solutesProcessed);
            solute.addProperty(RDF.type, ONS.Solute);
            solute.addProperty(DC_11.title, field[3].trim());
            solute.addProperty(BO.smiles, field[4].trim());
            solutes.put(soluteName, solute);
            solutesProcessed++;
        }
        measurement.addProperty(ONS.solute, solute);
        String solventName = removeQuotes(field[5]);
        Resource solvent = solutes.get(solventName);
        if (solvent == null) {
            solvent = model.createResource(url + "solvent" + solventsProcessed);
            solvent.addProperty(RDF.type, ONS.Solvent);
            solvent.addProperty(DC_11.title, field[5].trim());
            solvent.addProperty(BO.smiles, field[6].trim());
            solvents.put(solventName, solvent);
            solventsProcessed++;
        }
        measurement.addProperty(ONS.solvent, solvent);
        measurementsProcessed++;
    }

    private String removeQuotes(String string) {
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
        InputStream stream = ConvertToRDF.class.getResourceAsStream("/data.csv");
        convertor.processStream(stream);
        convertor.write();
    }

}
