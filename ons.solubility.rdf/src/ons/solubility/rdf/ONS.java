package ons.solubility.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;


public class ONS {

    private static Model model = ModelFactory.createDefaultModel();

    public static final String NS = "http://spreadsheet.google.com/plwwufp30hfq0udnEmRD1aQ/onto#";

    public static String getURI() {
        return NS;
    }

    public static final Resource NAMESPACE = model.createResource(NS);
    
    public static final Resource Measurement = model.createResource(NS+"Measurement");

    public static final Property solute = model.createProperty(NS + "solute");
    public static final Property solvent = model.createProperty(NS + "solvent");
    public static final Property experiment = model.createProperty(NS + "experiment");
    
}
