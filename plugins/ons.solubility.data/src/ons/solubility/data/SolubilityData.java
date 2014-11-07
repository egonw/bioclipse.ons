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
package ons.solubility.data;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.client.spreadsheet.WorksheetQuery;
import com.google.gdata.data.spreadsheet.Cell;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;

public class SolubilityData {

    private SpreadsheetService service;
    
    private Map<Integer,Measurement> measurements;
    
    public SolubilityData(String username, String password) throws Exception {
        if(username==null) throw new NullPointerException("undefined username");
    	if(password==null) throw new NullPointerException("undefined password");
    	this.service = new SpreadsheetService("ons-solubility-javaclient");
    	System.out.println("Google user name: " + username);
    	System.out.println("Google user name: " + password);
    	this.service.setUserCredentials(username, password);
    	this.measurements = new HashMap<Integer,Measurement>();
    }
    
    public void download() throws Exception {
        URL metafeedUrl = new URL(
            "http://spreadsheets.google.com/feeds/spreadsheets/private/full"
        );
        SpreadsheetFeed feed = service.getFeed(metafeedUrl, SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = feed.getEntries();

        SpreadsheetEntry spreadsheet = null;
        for (int i = 0; i < spreadsheets.size(); i++) {
            SpreadsheetEntry entry = spreadsheets.get(i);
            if ("SolubilitiesSum".equals(entry.getTitle().getPlainText())) {
                spreadsheet = entry;
            }
        }

        if (spreadsheet == null) {
            throw new Exception("No spreadsheets with the name: SolubilitiesSum");
        }
        
        WorksheetQuery worksheetQuery
        = new WorksheetQuery(spreadsheet.getWorksheetFeedUrl());

        worksheetQuery.setTitleQuery("Sheet1");
        WorksheetFeed worksheetFeed = service.query(worksheetQuery,
                                                    WorksheetFeed.class);
        List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
        if (worksheets.isEmpty()) {
            throw new Exception("No worksheets with that name in spreadhsheet "
                                + spreadsheet.getTitle().getPlainText());
        }

        WorksheetEntry worksheet = worksheets.get(0);
        
        CellFeed cellFeed = service.getFeed(worksheet.getCellFeedUrl(), 
                                            CellFeed.class);

        List<CellEntry> cells = cellFeed.getEntries();
        Measurement measurement = null;
        int lastRow = 0;
        for (CellEntry cellEntry : cells) {
            Cell cell = cellEntry.getCell();
            int row = cell.getRow();
            if (row != lastRow) {
                // new row :)
                if (measurement != null && measurement.getReference() != null)
                    measurements.put(row, measurement);
                lastRow = row;
                measurement = new Measurement();
            }
            if (row > 1) {
                switch (cell.getCol()) {
                    case 1: measurement.setExperiment(cell.getValue()); break;
                    case 2: measurement.setSample(cell.getValue()); break;
                    case 3: measurement.setReference(cell.getValue()); break;
                    case 4: measurement.setSolute(cell.getValue()); break;
                    case 5: measurement.setSoluteSMILES(cell.getValue()); break;
                    case 6: measurement.setSolvent(cell.getValue()); break;
                    case 7: measurement.setSolventSMILES(cell.getValue()); break;
                    case 8: measurement.setConcentration(cell.getValue()); break;
                    default: break;
                }
            }
        }
    }
    
    public Collection<Measurement> getData() {
        return measurements.values();
    }

}
