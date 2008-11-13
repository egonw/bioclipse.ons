package ons.solubility.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;


public class BO {

    private static Model model = ModelFactory.createDefaultModel();

    public static final String NS = "http://blueobelisk.sourceforge.net/chemistryblogs/";

    public static String getURI() {
        return NS;
    }

    public static final Resource NAMESPACE = model.createResource(NS);
    
    public static final Property smiles = model.createProperty(NS + "smiles");
    
}
