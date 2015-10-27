package com.stratio.explorer.cassandra.gateways;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.stratio.explorer.cassandra.constants.StringConstants;
import com.stratio.explorer.cassandra.exceptions.ConnectionException;
import com.stratio.explorer.cassandra.exceptions.NotPropertyFoundException;
import com.stratio.explorer.gateways.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

/**
 * Created by afidalgo on 15/10/15.
 */
//TODO :  THIS CLASS ONLY TEST WITH REAL OR EMBBEDED CASSANDRA
public class CassandraSession implements Connector<Session> {



    private Logger logger = LoggerFactory.getLogger(CassandraDriver.class);

    private Session session;
  //  private int port = 0;
  //  private String host = "";
    private Collection<InetSocketAddress> contactPointWithPorts = new ArrayList<>();
    private boolean isNewConfiguration=true; //TODO :

    /**
     * Load configuration to Cassandra DataBase
     * @param properties with configuration
     * @return instance of CassandraSession
     */
    @Override
    public Connector loadConfiguration(Properties properties) {
        try {
            buildProperties(properties);
            return this;
        }catch (NumberFormatException e){
           String errorMessage = " Port property is not filled";
           logger.error(errorMessage);
           throw  new NotPropertyFoundException(e,errorMessage);
        }
    }

    private void buildProperties(Properties properties){

      //  int port = Integer.valueOf(properties.getProperty(StringConstants.PORT));
      //  String host =properties.getProperty(StringConstants.HOST);
        Collection<InetSocketAddress> localConcatpoint = new PropertiesReader().getListSocketAddres(properties);
        if (contactPointWithPorts.containsAll(localConcatpoint) && localConcatpoint.size() == contactPointWithPorts.size()){
            isNewConfiguration = false;
        }
        if (localConcatpoint.isEmpty() ){
            String errorMessage = " Host port property is not filled";
            logger.error(errorMessage);
            throw  new NotPropertyFoundException(new Exception(),errorMessage);
        }

        if (isNewConfiguration==true){
            this.port = port;
            this.host = host;
            contactPointWithPorts =
            isNewConfiguration = true;
        }
    }

    /**
     *
     * @return Session with cassandra
     */
    @Override
    public Session getConnector() {
        try {
            if (isNewConfiguration){
                InetSocketAddress socket = new InetSocketAddress(host,port);
                Collection<InetSocketAddress> contactPointWithPorts =  new ArrayList<>();
                contactPointWithPorts.add(socket);
                Cluster cluster = Cluster.builder().addContactPointsWithPorts(contactPointWithPorts).build();
                session = cluster.connect();
                isNewConfiguration = false;
            }
            return session;
        }catch (NoHostAvailableException e ){
            String errorMessage ="  Cassandra database is not avalaible ";
            logger.error(errorMessage);
            throw new ConnectionException(e,errorMessage);
        }catch (RuntimeException e){
            String errorMessage ="  Cassandra database is not avalaible ";
            logger.error(errorMessage);
            throw new ConnectionException(e,errorMessage);
        }
    }
}
