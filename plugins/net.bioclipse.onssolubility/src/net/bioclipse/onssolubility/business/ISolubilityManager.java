/*******************************************************************************
 * Copyright (c) 2009  Egon Willighagen <egonw@users.sf.net>
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * www.eclipse.org—epl-v10.html <http://www.eclipse.org/legal/epl-v10.html>
 * 
 * Contact: http://www.bioclipse.net/    
 ******************************************************************************/
package net.bioclipse.onssolubility.business;

import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;

@PublishedClass ("Manager for accessing the ONS Solubility Data.")
public interface ISolubilityManager extends IBioclipseManager {

    @PublishedMethod(
        params="String filename, String account, String password",
        methodSummary="Download the ONS Solubility data into a RDF file " +
        		"with the given filename."
    )
    public String downloadAsRDF(String filename, String account, String password) throws BioclipseException;
    
}
