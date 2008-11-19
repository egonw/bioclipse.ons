package ons.solubility.rdf;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.client.spreadsheet.WorksheetQuery;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;

public class DownloadSolubilityData {

    private SpreadsheetService service;
    private FeedURLFactory factory;
    
    public DownloadSolubilityData() throws Exception {
        service = new SpreadsheetService("gdata-sample-spreadsheetimport");
        factory = FeedURLFactory.getDefault();
        
        Properties userInfo = new Properties();
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("userinfo.properties");
        userInfo.load(input);
        String username = userInfo.getProperty("username");
        String password = userInfo.getProperty("password");
        service.setUserCredentials(username, password);
        
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
        for (CellEntry cell : cells) {
            System.out.println("Cell: " + cell.getPlainTextContent());
        }
    }
    
    
    
    public static void main(String[] args) throws Exception {
        DownloadSolubilityData client = new DownloadSolubilityData();
    }
    
}
