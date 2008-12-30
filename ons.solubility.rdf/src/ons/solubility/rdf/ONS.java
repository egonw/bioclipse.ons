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
    public static final Resource Solute = model.createResource(NS+"Solute");
    public static final Resource Solvent = model.createResource(NS+"Solvent");

    public static final Property solute = model.createProperty(NS + "solute");
    public static final Property solvent = model.createProperty(NS + "solvent");
    public static final Property experiment = model.createProperty(NS + "experiment");
    public static final Property solubility = model.createProperty(NS + "solubility");
    
}
