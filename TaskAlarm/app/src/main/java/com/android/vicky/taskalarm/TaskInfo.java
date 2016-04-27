package com.android.vicky.taskalarm;

/**
 * Created by Vicky on 3/15/2016.
 */
public class TaskInfo {
    private long taskId;
    private int iconID,priorityIconID;
    private String taskTitle,taskType,taskStatus,taskTime,taskDate,taskSendNo;

    private final int[] icons = {R.drawable.alarm_icon_64n, R.drawable.msg_icon_64n, R.drawable.speech_icon_64n};

    private final int[] priorityIcons = {R.drawable.mat_icon_0_24, R.drawable.mat_icon_1_24, R.drawable.mat_icon_2_24,
            R.drawable.mat_icon_3_24, R.drawable.mat_icon_4_24, R.drawable.mat_icon_5_24
            , R.drawable.mat_icon_6_24, R.drawable.mat_icon_7_24, R.drawable.mat_icon_8_24
            , R.drawable.mat_icon_9_24, R.drawable.mat_icon_10_24};

    public TaskInfo(long _taskID, String _taskTitle, String _taskType,String taskDate, String _taskTime,String _priorityIconID, String _taskStatus, String _taskSendNo){
        setTaskId(_taskID);
        setPriorityIconID(Integer.parseInt(_priorityIconID));
        setTaskTitle(_taskTitle);
        setTaskStatus(_taskStatus);
        setTaskTime(_taskTime);
        setTaskType(_taskType);
        setTaskDate(taskDate);
        setTaskSendNo(_taskSendNo);

        if(taskType.equals("Alarm"))
            setIconID(1);
        else if(taskType.equals("Send Task"))
            setIconID(2);
        else if(taskType.equals("Speech text"))
            setIconID(3);
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = icons[(iconID-1)];
    }

    public int getPriorityIconID() {
        return priorityIconID;
    }

    public void setPriorityIconID(int priorityIconID) {
        this.priorityIconID = priorityIcons[priorityIconID];
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getTaskSendNo() {
        return taskSendNo;
    }

    public void setTaskSendNo(String taskSendNo) {
        this.taskSendNo = taskSendNo;
    }
}
