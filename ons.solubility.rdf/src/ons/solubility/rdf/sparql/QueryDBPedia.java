package ons.solubility.rdf.sparql;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class QueryDBPedia {

    public static void main(String[] args) throws Exception {
        Model model = ModelFactory.createMemModelMaker().createDefaultModel();
        InputStream in = new FileInputStream(new File("ons.rdf"));
        model.read(in,null);
        in.close();
        in = new FileInputStream(new File("src/org/dbpedia/Acetonitrile.rdf"));
        model.read(in,null);
        in.close();
        in = new FileInputStream(new File("src/org/dbpedia/Chloroform.rdf"));
        model.read(in,null);
        in.close();
        in = new FileInputStream(new File("src/org/dbpedia/Ethanol.rdf"));
        model.read(in,null);
        in.close();
        in = new FileInputStream(new File("src/org/dbpedia/Methanol.rdf"));
        model.read(in,null);
        in.close();
        in = new FileInputStream(new File("src/org/dbpedia/N-Butanol.rdf"));
        model.read(in,null);
        in.close();
        in = new FileInputStream(new File("src/org/dbpedia/Tetrahydrofuran.rdf"));
        model.read(in,null);
        in.close();
        in = new FileInputStream(new File("src/org/dbpedia/Toluene.rdf"));
        model.read(in,null);
        in.close();

        String queryString = 
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
            "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> " +
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> " +
            "SELECT ?solventName ?category " +
            "WHERE {" +
            "      ?solvent dc:title ?solventName . " +
            "      ?solvent owl:sameAs ?solventDB . " +
            "      ?solventDB skos:subject ?category " +
            "        FILTER regex(str(?category), \".*solvent.*\", \"i\") . " +
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
