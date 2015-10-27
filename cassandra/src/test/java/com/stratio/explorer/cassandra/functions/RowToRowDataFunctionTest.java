package com.stratio.explorer.cassandra.functions;


import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

import com.stratio.explorer.cassandra.doubles.DoubleRow;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.models.RowData;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by afidalgo on 14/10/15.
 */
public class RowToRowDataFunctionTest {

    private List<String> header ;
    private final String header1 ="header1";
    private final String header2 ="header2";
    private DoubleRow doubleRow;

    @Before
    public void setUp(){
        header = new ArrayList<>();
        header.add(header1);
        header.add(header2);
        doubleRow = new DoubleRow(header1,header2);
    }


    @Test
    public void whenTransformRowReturnListCells(){

        RowToRowDataFunction function = new RowToRowDataFunction(header);
        RowData row = function.transform(doubleRow.bildRow());
        assertThat(row.cells().size(),is(2));

    }


    @Test
    public void whenTransformRowReturnCells(){

        RowToRowDataFunction function = new RowToRowDataFunction(header);
        RowData row = function.transform(doubleRow.bildRow());
        List<CellData> cells = row.cells();
        assertThat(cells.get(0).getValue(),is((Object)"header1"));
        assertThat(cells.get(1).getValue(),is((Object)"header2"));
    }
}
