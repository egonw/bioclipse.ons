/* Copyright (C) 2008  Egon Willighagen <egon.willighagen@gmail.com>
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * All we ask is that proper credit is given for our work, which includes
 * - but is not limited to - adding the above copyright notice to the beginning
 * of your source code files, and to any copyright notice that you may distribute
 * with programs based on this work.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
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
        System.out.println(" " + m.getSample());
        System.out.println(" " + m.getReference());
        System.out.println(" Solute: " + m.getSolute());
        System.out.println("   SMILES: " + m.getSoluteSMILES());
        System.out.println(" Solvent: " + m.getSolvent());
        System.out.println("   SMILES: " + m.getSolventSMILES());
        System.out.println(" " + m.getConcentration());
    }
    
    public static void main( String[] args ) throws Exception {
        new JustDump();
    }

}
