package ons.solubility.rdf;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

public class ConvertToRDF {
    
    private Model model;
    private int measurementsProcessed;
    
    public ConvertToRDF() {
        model = ModelFactory.createDefaultModel();
        measurementsProcessed = 0;
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
        Resource measurement = model.createResource(url + measurementsProcessed);
        // FIXME: how can I set the rdf:type??
        measurement.addProperty(RDF.type, ONS.Measurement);
        measurement.addProperty(ONS.experiment, model.createResource(field[2]));
        measurementsProcessed++;
    }

    public void write() {
        model.setNsPrefix("ons", ONS.NS);
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
