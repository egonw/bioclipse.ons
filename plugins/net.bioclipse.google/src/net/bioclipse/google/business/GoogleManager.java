/*******************************************************************************
 * Copyright (c) 2007-2009  Jonathan Alvarsson
 *                    2011  Egon Willighagen <egonw@users.sf.net>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * www.eclipse.org—epl-v10.html <http://www.eclipse.org/legal/epl-v10.html>
 * 
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.google.business;

import net.bioclipse.managers.business.IBioclipseManager;

public class GoogleManager implements IBioclipseManager {

    protected GoogleManagerFactory factory;

    protected GoogleManagerFactory getFactory() throws Exception {
        if (factory == null) {
            factory = new GoogleManagerFactory();
        }
        return factory;
    }

    public String getManagerName() {
        return "google";
    }

}
