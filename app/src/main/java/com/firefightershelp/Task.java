package com.firefightershelp;

/**
 * Created by NaveenT on 6/21/14.
 */
public class Task {
    private String taskName;
    private int taskId;
    private int taskStatus;
    private String assignedTo;
    private int priority;
    private boolean needsAttention;

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getTaskId() {
        return taskId;
    }

    public boolean isNeedsAttention() {
        return needsAttention;
    }

    public void setNeedsAttention(boolean needsAttention) {
        this.needsAttention = needsAttention;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }
}
