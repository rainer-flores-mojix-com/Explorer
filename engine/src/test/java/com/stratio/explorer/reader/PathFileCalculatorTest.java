package com.stratio.explorer.reader;

import com.stratio.explorer.conf.ConstantsFolder;
import com.stratio.explorer.exceptions.FolderNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.easymock.EasyMock.expect;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replayAll;

/**
 * Created by afidalgo on 21/10/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest( { EnvironmentPathCalculator.class })
public class PathFileCalculatorTest {



    private final String FILE_NAME ="anyFileName";
    private final String FILE_EXTENSION =".conf";
    private PathFileCalculator calculator;
    private String oldValueFolder = ConstantsFolder.CT_NAME_PROJECT_FOLDER;
    private String oldConf ;

    @Before
    public void setUp(){
        oldConf =  ConstantsFolder.CT_FOLDER_CONFIGURATION;
        calculator = new PathFileCalculator();
    }

    @After
    public void tearDown(){
        ConstantsFolder.CT_FOLDER_CONFIGURATION = oldConf;
        ConstantsFolder.CT_NAME_PROJECT_FOLDER =  oldValueFolder;
    }


    @Test
    public void parentFolderWillBeCalculated(){

        assertTrue(calculator.getPath(FILE_NAME,FILE_EXTENSION).endsWith(ConstantsFolder.CT_FOLDER_CONFIGURATION +"/"+FILE_NAME+FILE_EXTENSION));
    }


    @Test
    public void whenParentFolderNotExistButEnvironmentFolderExist(){
        final String expected = calculator.getPath(FILE_NAME, FILE_EXTENSION);
        ConstantsFolder.CT_NAME_PROJECT_FOLDER = "not_exist_file";
        mockStatic(System.class);
        final String key = ConstantsFolder.CT_EXPLORER_CONF_DIR_ENV;
        expect(System.getenv(key)).andReturn(expected).andReturn(expected);
        replayAll();
        assertThat(calculator.getPath(FILE_NAME, FILE_EXTENSION), is(expected));
    }

    @Test (expected = FolderNotFoundException.class)
    public void whenParentFolderNotExistAndEnvironmentFolderExist(){
        ConstantsFolder.CT_FOLDER_CONFIGURATION = "not_exist_file";
        mockStatic(System.class);
        final String key = ConstantsFolder.CT_EXPLORER_CONF_DIR_ENV;
        final String expected = "not_exist";
        expect(System.getenv(key)).andReturn(expected);
        replayAll();
        calculator.getPath(FILE_NAME, FILE_EXTENSION);;
    }
}
