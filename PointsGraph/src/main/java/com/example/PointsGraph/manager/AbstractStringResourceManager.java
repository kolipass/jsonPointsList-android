package com.example.PointsGraph.manager;

/**
 * Менеджер строковых ресурсов. Нужен для того. чтобы не таскать везде контекст.
 * Здесь будут обертки к функциям получения ресурса по
 */
public abstract class AbstractStringResourceManager {
    abstract public String getKeyManagementError() ;

    abstract public String getSendRequestError() ;

    abstract public String getParseRequestError() ;

    abstract public String getServerReturnError() ;
}
