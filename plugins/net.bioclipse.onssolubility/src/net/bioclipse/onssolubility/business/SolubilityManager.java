/*******************************************************************************
 * Copyright (c) 2007-2009  Jonathan Alvarsson
 *               2008-2009  Egon Willighagen <egonw@users.sf.net>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * www.eclipse.org—epl-v10.html <http://www.eclipse.org/legal/epl-v10.html>
 * 
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.onssolubility.business;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import net.bioclipse.core.ResourcePathTransformer;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.onssolubility.preferences.PreferenceConstants;
import ons.solubility.rdf.ConvertToRDF;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Preferences;

public class SolubilityManager implements IBioclipseManager {

    protected SolubilityManagerFactory factory;

    protected SolubilityManagerFactory getFactory() throws Exception {
        if (factory == null) {
            factory = new SolubilityManagerFactory();
        }
        return factory;
    }

    public String getManagerName() {
        return "solubility";
    }

    public String downloadAsRDF(String filename) throws BioclipseException {
        IFile file = ResourcePathTransformer.getInstance().transform(filename);
        downloadAsRDF(file, null);
        return filename;
    }

    public IFile downloadAsRDF(IFile file, IProgressMonitor monitor)
        throws BioclipseException {
        if (file.exists()) {
            throw new BioclipseException("File already exists!");
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            ConvertToRDF convertor = new ConvertToRDF();
            Preferences prefs = Activator.getDefault().getPluginPreferences();
            convertor.processData(
                prefs.getString(PreferenceConstants.GOOGLE_USERNAME),
                prefs.getString(PreferenceConstants.GOOGLE_PASSWORD)
            );
            convertor.write(output);
            file.create(
                new ByteArrayInputStream(output.toByteArray()),
                false,
                monitor
            );
        } catch (Exception exception) {
            throw new BioclipseException(
                "Error while creating the RDF.",
                exception
            );
        }
        
        return file;
    }

}
