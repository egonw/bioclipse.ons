package ons.solubility.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetQuery;
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
    private FeedURLFactory factory;
    
    private Map<Integer,Measurement> measurements;
    
    public SolubilityData(String username, String password) throws Exception {
        service = new SpreadsheetService("ons-solubility-javaclient");
        service.setUserCredentials(username, password);
        factory = FeedURLFactory.getDefault();
        measurements = new HashMap<Integer,Measurement>();
    }
    
    public void download() throws Exception {
        SpreadsheetQuery spreadsheetQuery 
        = new SpreadsheetQuery(factory.getSpreadsheetsFeedUrl());
        spreadsheetQuery.setTitleQuery("SolubilitesSum");
        SpreadsheetFeed spreadsheetFeed = service.query(spreadsheetQuery, 
                                                        SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = spreadsheetFeed.getEntries();
        if (spreadsheets.isEmpty()) {
            throw new Exception("No spreadsheets with that name");
        }

        SpreadsheetEntry spreadsheet = spreadsheets.get(0);
        
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
                if (measurement != null) measurements.put(row, measurement);
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
                    case 7: measurement.setSoluteSMILES(cell.getValue()); break;
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
