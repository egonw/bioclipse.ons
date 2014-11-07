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

import java.net.URL;
import java.util.List;

import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.StringMatrix;
import net.bioclipse.managers.business.IBioclipseManager;

import org.eclipse.core.runtime.IProgressMonitor;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.client.spreadsheet.WorksheetQuery;
import com.google.gdata.data.spreadsheet.Cell;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;

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

    public StringMatrix downloadSpreadsheet(
    		String username, String password, String spreadsheet, String sheet, IProgressMonitor monitor)
    	throws BioclipseException {
    	StringMatrix strMatrix = new StringMatrix();
        try {
        	if(username==null) throw new NullPointerException("undefined username");
        	if(password==null) throw new NullPointerException("undefined password");
        	SpreadsheetService service = new SpreadsheetService("net.bioclipse.google.client");
        	System.out.println("Google user name: " + username);
        	System.out.println("Google user name: " + password);
        	service.setUserCredentials(username, password);
        	URL metafeedUrl = new URL(
                "http://spreadsheets.google.com/feeds/spreadsheets/private/full"
        	);
        	SpreadsheetFeed feed = service.getFeed(metafeedUrl, SpreadsheetFeed.class);
        	List<SpreadsheetEntry> spreadsheets = feed.getEntries();

        	SpreadsheetEntry spreadsheetEntry = null;
        	for (int i = 0; i < spreadsheets.size(); i++) {
        		SpreadsheetEntry entry = spreadsheets.get(i);
        		if (spreadsheet.equals(entry.getTitle().getPlainText())) {
        			spreadsheetEntry = entry;
        		}
        	}

        	if (spreadsheet == null) {
        		throw new Exception("No spreadsheets with the name: " + spreadsheet);
        	}

        	WorksheetQuery worksheetQuery = new WorksheetQuery(spreadsheetEntry.getWorksheetFeedUrl());

        	worksheetQuery.setTitleQuery(sheet);
        	WorksheetFeed worksheetFeed = service.query(worksheetQuery,
        			WorksheetFeed.class);
        	List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
        	if (worksheets.isEmpty()) {
        		throw new Exception("No worksheets with that name in spreadhsheet "
        				+ spreadsheetEntry.getTitle().getPlainText());
        	}

        	WorksheetEntry worksheet = worksheets.get(0);

        	CellFeed cellFeed = service.getFeed(worksheet.getCellFeedUrl(), 
        			CellFeed.class);

        	List<CellEntry> cells = cellFeed.getEntries();
        	System.out.println("#Cells: " + cells.size());
        	for (CellEntry cellEntry : cells) {
        		Cell cell = cellEntry.getCell();
        		System.out.println("Cell: " + cell.getRow() + ", " + cell.getCol() + ", " + cell.getValue());
    			strMatrix.set(cell.getRow(), cell.getCol(), cell.getValue());        			
        	}
        } catch (Exception exception) {
            throw new BioclipseException(
                "Error while creating the StringMatrix.",
                exception
            );
        }
        
        return strMatrix;
    }
}
