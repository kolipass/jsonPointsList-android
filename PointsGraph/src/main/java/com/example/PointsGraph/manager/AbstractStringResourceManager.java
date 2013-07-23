package com.example.PointsGraph.manager;

/**
 * Менеджер строковых ресурсов для тасков
 */
public abstract class AbstractStringResourceManager {
    abstract public String getKeyManagementError() ;

    abstract public String getSendRequestError() ;

    abstract public String getParseRequestError() ;

    abstract public String getServerReturnError() ;
}
