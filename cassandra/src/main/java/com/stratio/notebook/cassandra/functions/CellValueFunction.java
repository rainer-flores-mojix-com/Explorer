package com.stratio.notebook.cassandra.functions;

import com.datastax.driver.core.Row;
import com.stratio.notebook.cassandra.models.CellData;
import com.stratio.notebook.functions.TransformFunction;

/**
 * Created by afidalgo on 14/10/15.
 */
public class CellValueFunction implements TransformFunction<String,CellData>{

    private Row row;


    /**
     * Constructor
     * @param row row to transforms cell in cellData
     */
    public CellValueFunction(Row row){
        this.row = row;
    }



    /**
     * Transform cell with header name in CellData
     * @param header neme in ros
     * @return CellDataObject
     */
    @Override
    public CellData transform(String header) {
        return new CellData(row.getObject(header));
    }
}