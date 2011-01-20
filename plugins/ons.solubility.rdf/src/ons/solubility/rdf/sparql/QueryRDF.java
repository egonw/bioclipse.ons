package ons.solubility.rdf.sparql;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.hp.hpl.jena.graph.query.Query;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.ResultSet;

public class QueryRDF {

    public static void main(String[] args) throws Exception {
        InputStream in = new FileInputStream(new File("ons.rdf"));
        Model model = ModelFactory.createMemModelMaker().createDefaultModel();
        model.read(in,null);
        in.close();

        String queryString = 
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
            "PREFIX ons: <http://spreadsheet.google.com/plwwufp30hfq0udnEmRD1aQ/onto#> " +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> " +
            "SELECT ?soluteTitle ?solubility " +
            "WHERE {" +
            "      ?measurement ons:solvent ?solvent . " +
            "      ?measurement ons:solubility ?solubility . " +
            "      ?measurement ons:solute ?solute . " +
            "      ?solute dc:title ?soluteTitle . " +
            "      ?solvent dc:title \"toluene\" . " +
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
