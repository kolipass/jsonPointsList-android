package com.example.PointsGraph.manager;

import com.example.PointsGraph.task.AbstractTask;
import com.example.PointsGraph.task.TaskStatus;
import com.example.PointsGraph.task.decoratedTask.DecoratedTaskAbstract;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * User: artem
 * Date: 19.02.13
 * Time: 12:03
 * Менеджер задач.
 */
public class TaskManager extends Observable implements Observer {
    private static volatile TaskManager instance;
    Map<String, AbstractTask> taskMap;

    private TaskManager() {
        this.taskMap = new HashMap<String, AbstractTask>();
    }

    public static TaskManager getInstance() {
        TaskManager localInstance = instance;
        if (localInstance == null) {
            synchronized (TaskManager.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new TaskManager();
                }
            }
        }
        return localInstance;
    }

    /**
     * Запуст загрузки
     *
     * @param tag тег загрузки
     */
    public void start(String tag) {
        AbstractTask task = taskMap.get(tag);
        if (task != null && task.getTaskStatus().getStatus() != TaskStatus.STATUS_WORKING) {
            task.createTreadTask();
        }
    }

    /**
     * Запуст загрузки
     *
     * @param tag  тег загрузки
     * @param task Задача, которую стартуем.
     */
    public void start(String tag, AbstractTask task) {
        if (!tag.isEmpty() && task != null) {
            if (taskMap != null) {
                if (!taskMap.containsKey(tag)) {
                    addTask(tag, task);
                }
                start(tag);
            }
        }
    }

    public void addTask(String tag, AbstractTask task) {
        task.addObserver(this);
        taskMap.put(tag, task);
    }

    public void pause(String tag) {
        AbstractTask task = getTask(tag);
        if (task != null) {
            task.pause();
        }
    }

    public AbstractTask getTask(String tag) {
        AbstractTask task = null;
        if (tag != null && !tag.isEmpty()) {
            task = taskMap.get(tag);
        }
        return task;
    }

    /**
     * Получить уровень таска
     *
     * @param tag
     * @return 0 если такого таска нет, если таск не декоррирован
     */
    public int getTaskMaxLevel(String tag) {
        int level = 0;
        AbstractTask taskAbstract = getTask(tag);
        if (taskAbstract != null && taskAbstract instanceof DecoratedTaskAbstract) {
            DecoratedTaskAbstract task = (DecoratedTaskAbstract) taskAbstract;
            level = task.taskLevel();
        }
        return level;
    }

    public void resume(String tag) {
        AbstractTask task = getTask(tag);
        if (task != null) {
            task.resume();
        }
    }

    public void cancel(String tag) {
        AbstractTask task = getTask(tag);
        if (task != null) {
            task.cancel();
        }
    }

    @Override
    public void update(Observable task, Object taskStatus) {
        if (task != null && task instanceof AbstractTask) {
            System.out.println(taskStatus);
            setChanged();
            notifyObservers(taskStatus);
        }
    }
}
