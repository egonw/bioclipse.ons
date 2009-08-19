package ons.solubility.rdf.sparql;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.hp.hpl.jena.graph.query.Query;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.ResultSet;

public class QueryRON {

    public static void main(String[] args) throws Exception {
        Model model = ModelFactory.createMemModelMaker().createDefaultModel();
        InputStream in = new FileInputStream(new File("ons.rdf"));
        model.read(in,null);
        in.close();
        in = new FileInputStream(new File("src/net/openmolecules/rdf/chloroform.rdf"));
        model.read(in,null);
        in.close();

        String queryString = 
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
            "PREFIX cb: <http://cb.openmolecules.net/#> " +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> " +
            "SELECT ?name ?blog " +
            "WHERE {" +
            "      ?compound dc:title ?name . " +
            "      ?compound owl:sameAs ?compoundRON . " +
            "      ?compoundRON cb:discussedBy ?blog . " +
            "}";

        Query query = QueryFactory.create(queryString);

        // Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet results = qe.execSelect();

        // Output query results 
        ResultSetFormatter.out(System.out, results, query);

        // Important - free up resources used running the query
        qe.close();
    }

}
