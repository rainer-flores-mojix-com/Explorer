package com.stratio.explorer.cassandra.gateways;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.TableMetadata;
import org.junit.Test;

import com.stratio.explorer.reader.PropertiesReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by afidalgo on 4/11/15.
 */
public class CassandraRealDriverTest {





    @Test
    public void whenUseDescribe(){
        CassandraSession sesion = new CassandraSession();
        sesion.loadConfiguration(new PropertiesReader().readConfigFrom("cassandra"));

        mockDescribeKeySpace(sesion.getConnector().getCluster());
        mockDescribeKeySpacedemo(sesion.getConnector().getCluster());
        mockDescribeTables(sesion.getConnector().getCluster());
        mockDescribeTableDemo(sesion.getConnector().getCluster());

    }



    private void mockDescribeKeySpace(Cluster cluster){
        Metadata metaData = cluster.getMetadata();
        List<KeyspaceMetadata> list = metaData.getKeyspaces();
        KeyspaceMetadata first = list.get(0);
        String firstName = first.getName();

        KeyspaceMetadata second = list.get(1);
        String secondName = second.getName();

        KeyspaceMetadata third = list.get(2);
        String thirdName = third.getName();

        KeyspaceMetadata fourth = list.get(3);
        String fourthName = fourth.getName();

        int b=0;
    }



    private void mockDescribeKeySpacedemo(Cluster cluster){
        Metadata metaData = cluster.getMetadata();
        KeyspaceMetadata metaDataDemo = metaData.getKeyspace("demo");

        String createDemo = metaDataDemo.toString();

        List<TableMetadata> list =  new ArrayList<TableMetadata>( metaDataDemo.getTables());

        TableMetadata second = list.get(0);
        String secondName = second.toString();



        TableMetadata third = list.get(1);
        String thirdName = third.toString();

        TableMetadata fourth = list.get(2);
        String fourthName = fourth.toString();
        int c =0;

    }



    private void mockDescribeTables(Cluster cluster){
        Metadata metaData = cluster.getMetadata();

        List<KeyspaceMetadata> list = metaData.getKeyspaces();


        /********** Keyspace demo ****/
        KeyspaceMetadata demoMetaData = list.get(0);
        String nameKeySpace = demoMetaData.getName();
        List<TableMetadata> tables = new ArrayList<TableMetadata> (demoMetaData.getTables());

        TableMetadata tableOne = tables.get(0);
        String tableOneName = tableOne.getName();

        TableMetadata tableTwo = tables.get(1);
        String tableTwoName = tableTwo.getName();


        int a =0;
    }


    private void mockDescribeTableDemo(Cluster cluster){
        Metadata metaData = cluster.getMetadata();
        KeyspaceMetadata metaDataDemo = metaData.getKeyspace("demo");

        TableMetadata table = metaDataDemo.getTable("users");

        String data = table.toString();

        int a = 0;
    }
}
