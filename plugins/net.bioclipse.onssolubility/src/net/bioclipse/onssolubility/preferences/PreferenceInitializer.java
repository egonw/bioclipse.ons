/*******************************************************************************
 * Copyright (c) 2009  Egon Willighagen <egonw@user.sf.net>
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.onssolubility.preferences;

import net.bioclipse.onssolubility.business.Activator;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		store.setDefault(
		    PreferenceConstants.GOOGLE_USERNAME,
		    ""
		);
        store.setDefault(
            PreferenceConstants.GOOGLE_PASSWORD,
            ""
        );
        store.setDefault(
            PreferenceConstants.ONSSOL_SPREADSHEET,
            "SolubilitiesSum"
        );
	}

}
