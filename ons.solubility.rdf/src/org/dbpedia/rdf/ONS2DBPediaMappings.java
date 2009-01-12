package org.dbpedia.rdf;

import java.util.HashMap;
import java.util.Map;

import ons.solubility.rdf.DBPedia;

public class ONS2DBPediaMappings {

    private static Map<String,String> ons2dbpedia = new HashMap<String, String>();
    
    static {
        ons2dbpedia.put("acetonitril", DBPedia.NAMESPACE + "Acetonitrille");
        ons2dbpedia.put("chloroform", DBPedia.NAMESPACE + "Chloroform");
        ons2dbpedia.put("ethanol", DBPedia.NAMESPACE + "Ethanol");
        ons2dbpedia.put("methanol", DBPedia.NAMESPACE + "Methanol");
        ons2dbpedia.put("1-butanol", DBPedia.NAMESPACE + "N-Butanol");
        ons2dbpedia.put("thf", DBPedia.NAMESPACE + "Tetrahydrofuran");
        ons2dbpedia.put("toluene", DBPedia.NAMESPACE + "Toluene");
    }
    
    public static String getDBPediaResource(String onsName) {
        return ons2dbpedia.get(onsName.toLowerCase());
    }
    
}
