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

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Preference page for the ONS Solubility data.
 */
public class SolubilityPreferencePage extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

    private StringFieldEditor googleUserName;
    private StringFieldEditor googlePassword;
    private StringFieldEditor spreadsheet;
    
    public SolubilityPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("ONS Solubility Preferences");
	}
	
	/**
	 * Creates the field editors.
	 */
	public void createFieldEditors() {
	    googleUserName = new StringFieldEditor(
	        PreferenceConstants.GOOGLE_USERNAME,
	        "Google User name",
	        getFieldEditorParent()
	    );
		addField(googleUserName);

        googlePassword = new StringFieldEditor(
            PreferenceConstants.GOOGLE_PASSWORD,
            "Google Password",
            getFieldEditorParent()
        );
        addField(googlePassword);

        spreadsheet = new StringFieldEditor(
            PreferenceConstants.ONSSOL_SPREADSHEET,
            "ONS Solubility Spreadsheet Name",
            getFieldEditorParent()
        );
        addField(spreadsheet);
}

	public void init(IWorkbench workbench) {}
	
}