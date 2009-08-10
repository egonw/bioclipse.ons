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

import net.bioclipse.managers.business.IBioclipseManager;

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

}
