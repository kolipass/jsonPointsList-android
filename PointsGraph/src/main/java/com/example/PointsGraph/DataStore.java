package com.example.PointsGraph;

import com.example.PointsGraph.model.ServerResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kolipass
 * Date: 22.07.13
 * Time: 23:48
 * Singleton-Хранилище для данных, полученных от сервера.
 * Будет хранить послених countLimit запросов
 */
public class DataStore {
    private static volatile DataStore instance;
    List<ServerResponse> serverResponses = new ArrayList<ServerResponse>();
    private static final int countLimit = 10;

    private DataStore() {
    }

    public static DataStore getInstance() {
        if (instance == null) {
            synchronized (DataStore.class) {
                if (instance == null) {
                    instance = new DataStore();
                }
            }
        }
        return instance;
    }

    /**
     * Добавить ответ от сервера
     * Добавит ответ крайним в список и вытолкнен первый посупивший элемент, если достигнут лимит
     *
     * @param serverResponse Ответ от сервера
     */
    public void addServerResponse(ServerResponse serverResponse) {
        if (serverResponses.size() == (countLimit - 1)) {
            serverResponses.remove(0);
        } else {
            serverResponses.add(serverResponse);
        }
    }

    public ServerResponse getServerResponse(int id) throws IndexOutOfBoundsException {
        return serverResponses.get(id);
    }

    /**
     * По аналогии со стеком - взять крайний элемент списка не вытесняя его
     *
     * @return крайний элемет списка
     * @throws IndexOutOfBoundsException произайдет при запросе крайнего из пустой коллекции
     */

    public ServerResponse peekServerResponse() throws IndexOutOfBoundsException {
        return serverResponses.get(serverResponses.size() - 1);
    }
}
