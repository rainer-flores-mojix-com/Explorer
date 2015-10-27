package com.stratio.explorer.writer;


import com.stratio.explorer.conf.ConstantsFolder;
import com.stratio.explorer.exceptions.FileConfNotExisException;
import com.stratio.explorer.reader.PathFileCalculator;
import com.stratio.explorer.reader.PropertiesReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

public class PropertiesFileUpdaterTest {


    private Properties result;
    private static final String CT_FILE="writeble_file";
    private PropertiesFileUpdater updater;


    @Before public void setUp(){
        result = new Properties();
        updater = new PropertiesFileUpdater();
    }

    @Test public void whenFileContainsPropertyThenActualizeValue() throws IOException {
        result.put("prop1", "prop8");
        updater.updateFileWithProperties(CT_FILE, "prop1:prop8");
        Properties prop = new PropertiesReader().readConfigFrom(CT_FILE);
        assertThat(prop, is(result));
    }


    @Test(expected = FileConfNotExisException.class)
    public void whenfilenotExistThenthrowexception(){
        String not_exist_file = "no_exist_file";
        updater.updateFileWithProperties(not_exist_file, "prop1:prop8");
    }



}
