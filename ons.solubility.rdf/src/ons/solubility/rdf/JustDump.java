package ons.solubility.rdf;

import java.io.InputStream;
import java.util.Properties;

import ons.solubility.data.Measurement;
import ons.solubility.data.SolubilityData;


public class JustDump {
    
    public JustDump() throws Exception {
        Properties userInfo = new Properties();
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("userinfo.properties");
        userInfo.load(input);
        String username = userInfo.getProperty("username");
        String password = userInfo.getProperty("password");
        
        SolubilityData data = new SolubilityData(username, password);
        data.download();
        for (Measurement measurement : data.getData()) {
            dumpResource(measurement);
        }
    }

    private void dumpResource( Measurement m) {
        System.out.println("Measurement: " + m.getExperiment());
        System.out.println(" " + m.getReference());
    }
    
    public static void main( String[] args ) throws Exception {
        new JustDump();
    }

}
