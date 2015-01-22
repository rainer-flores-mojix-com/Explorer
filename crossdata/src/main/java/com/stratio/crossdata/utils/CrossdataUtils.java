package com.stratio.crossdata.utils;

import java.util.Map;

import com.stratio.crossdata.common.data.Cell;
import com.stratio.crossdata.common.data.ResultSet;
import com.stratio.crossdata.common.data.Row;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.result.CommandResult;
import com.stratio.crossdata.common.result.ConnectResult;
import com.stratio.crossdata.common.result.ErrorResult;
import com.stratio.crossdata.common.result.MetadataResult;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.common.result.StorageResult;

public class CrossdataUtils {

    public static String resultToString(Result result) {
        if (ErrorResult.class.isInstance(result)) {
            return ErrorResult.class.cast(result).getErrorMessage();
        }
        if (result instanceof QueryResult) {
            QueryResult queryResult = (QueryResult) result;
            return queryResultToString(queryResult);
        } else if (result instanceof CommandResult) {
            CommandResult commandResult = (CommandResult) result;
            return String.class.cast(commandResult.getResult());
        } else if (result instanceof ConnectResult) {
            ConnectResult connectResult = (ConnectResult) result;
            return String.valueOf("Connected with SessionId=" + connectResult.getSessionId());
        } else if (result instanceof MetadataResult) {
            MetadataResult metadataResult = (MetadataResult) result;
            return metadataResult.toString();
        } else if (result instanceof StorageResult) {
            StorageResult storageResult = (StorageResult) result;
            return storageResult.toString();
        } else {
            return "Unknown result";
        }
    }

    public static String queryResultToString(QueryResult result) {
        StringBuilder sb = new StringBuilder();
        if (result.getResultSet().isEmpty()) {
            return "%text EMPTY result";
        }

        ResultSet resultSet = null;
        resultSet = result.getResultSet();
        sb.append("%table ");

        for (ColumnMetadata c : resultSet.getColumnMetadata()) {

            sb.append(c.getName().getColumnNameToShow()).append("\t");
        }
        sb.replace(sb.length() - 1, sb.length(), "\n");

        for (Row r : resultSet.getRows()) {
            for (Map.Entry<String, Cell> c : r.getCells().entrySet()) {
                sb.append(c.getValue()).append("\t");
            }
            sb.replace(sb.length() - 1, sb.length(), "\n");
        }

        return sb.toString();
    }
}
