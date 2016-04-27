package com.android.vicky.taskalarm;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import static android.support.v4.content.ContextCompat.getColor;

/**
 * Created by Vicky on 3/15/2016.
 */
public class VivzAdapter extends RecyclerView.Adapter<VivzAdapter.MyViewHolder> {
    List<TaskInfo> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    public VivzAdapter(Context context, List<TaskInfo> _data){
        this.context = context;
        inflater = LayoutInflater.from(context);
        data = _data;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row, parent, false);
        MyViewHolder holderCurrent = new MyViewHolder(view);

        return holderCurrent;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TaskInfo current = data.get(position);
        holder.title.setText(current.getTaskTitle());
        holder.icon.setImageResource(current.getIconID());
        holder.taskTime.setText(current.getTaskTime());
        if(!current.getTaskSendNo().equals("")){
            holder.taskSendPhNo.setText(current.getTaskSendNo());
        }else {
            holder.taskSendPhNo.setText("");
        }
        if(current.getTaskStatus().equals("on")){
            holder.taskTime.setTextColor(getColor(context, R.color.color_time_active));
        }else if(current.getTaskStatus().equals("complete")){
            holder.taskTime.setTextColor(getColor(context, R.color.color_completed));
        }
        holder.priorityIcon.setImageResource(current.getPriorityIconID());
        holder.taskDate.setText(MakeDateTimePattern.setDatePattern(current.getTaskDate()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,taskTime,taskSendPhNo,taskDate;
        ImageView icon,priorityIcon;
        View listRow;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.list_title);
            icon = (ImageView) itemView.findViewById(R.id.list_logo);
            listRow = itemView.findViewById(R.id.list_row);
            taskTime = (TextView) itemView.findViewById(R.id.list_time);
            taskSendPhNo = (TextView) itemView.findViewById(R.id.list_status);
            priorityIcon = (ImageView) itemView.findViewById(R.id.list_priority);
            taskDate = (TextView) itemView.findViewById(R.id.list_date);


            //itemView.setOnCreateContextMenuListener(this);
            /*icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"Clicked on icon of "+ data.get(getPosition()).getTaskTitle(),Toast.LENGTH_SHORT).show();
                }
            });*/

            listRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getPosition();
//                    Toast.makeText(context, "Opening " + data.get(position).getTaskTitle(), Toast.LENGTH_SHORT).show();
                    Intent viewTaskIntent = new Intent(context, TaskView.class);
                    viewTaskIntent.putExtra("row_ID", String.valueOf(data.get(position).getTaskId()));
                    context.startActivity(viewTaskIntent);
                }
            });

            listRow.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    DBTaskToDo dbTask = new DBTaskToDo(context);
                    int position = getPosition();
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View dialogView = inflater.inflate(R.layout.context_dialog, null);
                    final AlertDialog alert = new AlertDialog.Builder(context).create();
                    alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    alert.setView(dialogView);
                    alert.show();
                    /*try{
                        dbTask.open();
                        dbTask.deleteTask(data.get(position).getTaskId());
                        dbTask.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }*/
                    /*Toast.makeText(context, data.get(position).getTaskTitle()+" task is deleted ", Toast.LENGTH_SHORT).show();
                    data.remove(position);
                    notifyItemRemoved(position);*/
                    return true;
                }
            });
        }

        /*@Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select The Action");
            menu.add(0, v.getId(), 0, "Update Task");
            menu.add(0, v.getId(), 0, "Delete Task");
        }*/


        /*@Override
        public boolean onContextClick(View v) {
            Toast.makeText(context,"Its Clicked",Toast.LENGTH_SHORT).show();
            return true;
        }*/
    }
}
