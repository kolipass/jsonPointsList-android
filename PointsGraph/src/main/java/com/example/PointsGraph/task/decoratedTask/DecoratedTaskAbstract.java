package com.example.PointsGraph.task.decoratedTask;

import com.example.PointsGraph.manager.AbstractStringResourceManager;
import com.example.PointsGraph.task.AbstractTask;
import com.example.PointsGraph.task.TaskStatus;

import java.util.Observable;
import java.util.Observer;

/**
 * User: artem
 * Date: 15.03.13
 * Time: 10:03
 * <p/>
 * Динамически наделяемый новыми свойствами таск. Последовательное выполнение свойств
 */
public abstract class DecoratedTaskAbstract extends AbstractTask implements Observer {
    AbstractTask preExecutableTask;

    protected DecoratedTaskAbstract(AbstractStringResourceManager resourceManager, String tag, AbstractTask preExecutableTask) {
        super(resourceManager, tag);
        this.preExecutableTask = preExecutableTask;
        taskStatus.setLevel(taskLevel());
    }

    public void setPreExecutableTask(AbstractTask preExecutableTask) {
        this.preExecutableTask = preExecutableTask;
    }

    /**
     * Тут будет выполняться задача пеализующего класса
     *
     * @return
     */
    protected abstract TaskStatus currentHeavyTask();

    /**
     * Сначало выполняем поставленную задачу, если она есть. Если ее нет, или она выполнилась корректно, то выполняем свою задачу
     *
     * @return
     */

    @Override
    public final TaskStatus heavyTask() {
        if (preExecutableTask != null) {
            preExecutableTask.addObserver(this);
            preExecutableTask.setCurrentlyStarted(true);
            publishProgress(preExecutableTask.heavyTask());
            preExecutableTask.setCurrentlyStarted(false);
            TaskStatus preStatus = preExecutableTask.getTaskStatus();
            if (!preStatus.isCorrectComplate()) {
                return null;
            }
        }
        return currentHeavyTask();

    }

    public TaskStatus getTaskStatus() {
        if (preExecutableTask != null && taskStatus.getStatus() == TaskStatus.STATUS_NONE) {
            return preExecutableTask.getTaskStatus();
        }
        return taskStatus;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof AbstractTask) {
            if (arg instanceof TaskStatus) {
                try {
                    publishProgress((TaskStatus) arg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Узнаем уровень таска. Нужно для определения колличества послежовательных задач
     *
     * @return
     */

    public int taskLevel() {
        if (preExecutableTask == null) {
            return 0;
        } else {
            if (preExecutableTask instanceof DecoratedTaskAbstract) {
                return ((DecoratedTaskAbstract) preExecutableTask).taskLevel() + 1;
            } else {
                return 1;
            }
        }
    }

    @Override
    public void cancel() {
        if (preExecutableTask != null) {
            if (preExecutableTask.isCurrentlyStarted()) {
                preExecutableTask.cancel();
            }
        }
        super.cancel();

    }

    @Override
    public void resume() {
        if (preExecutableTask != null) {
            if (preExecutableTask.isCurrentlyStarted()) {
                preExecutableTask.resume();
            }
        } else {
            super.resume();
        }
    }

    @Override
    public void pause() {
        if (preExecutableTask != null) {
            if (preExecutableTask.isCurrentlyStarted()) {
                preExecutableTask.pause();
            }
        } else {
            super.pause();
        }
    }

}
