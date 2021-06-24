package com.bachelorshelter.tanvir.cutmytask.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bachelorshelter.tanvir.cutmytask.R;
import com.bachelorshelter.tanvir.cutmytask.model.Task;
import com.bachelorshelter.tanvir.cutmytask.activities.TaskEditActivity;
import com.bachelorshelter.tanvir.cutmytask.helper.DatabaseHandler;
import com.bachelorshelter.tanvir.cutmytask.helper.OnSwipeTouchListener;
import java.util.List;

/**
 * Created by Tanvir on 2017-06-01.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    //private Vibrator vib;
    //private MediaPlayer mp;
    private DatabaseHandler db;
    private List<Task> listData;
    private LayoutInflater inflater;


    public TaskAdapter(List<Task> listData, Context c){
        this.inflater = LayoutInflater.from(c);
        this.listData = listData;
    }

    @Override
    public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_for_main,parent,false);
        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(final TaskHolder holder, int position) {


        final Task item = listData.get(position);
        db = new DatabaseHandler(inflater.getContext());

        if(item.getTaskDesc().length()>42){
            String f = item.getTaskDesc().substring(0,40)+"...";
            holder.taskDesc.setText(f);
        }
        else {
            holder.taskDesc.setText(item.getTaskDesc());
        }
        holder.time.setText(item.getDate().substring(11,16));
        //Set Background Color
        holder.container.setBackgroundColor(Color.parseColor(item.getBackColor()));

        if(item.getAction()==1){
            holder.taskDesc.setPaintFlags(holder.taskDesc.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else if(item.getAction()==0){
            holder.taskDesc.setPaintFlags(holder.taskDesc.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        holder.container.setOnTouchListener(new OnSwipeTouchListener(inflater.getContext()) {

            @Override
            public void onClick() {
                super.onClick();
                Intent intent = new Intent(inflater.getContext(), TaskEditActivity.class);
                intent.putExtra("origin","forEdit");
                intent.putExtra("editDate",item.getDate().substring(0,10));
                intent.putExtra("editTime",item.getDate().substring(11,16));
                intent.putExtra("editTaskDesc",item.getTaskDesc());
                intent.putExtra("editColor",item.getBackColor());
                intent.putExtra("editAction",item.getAction());
                intent.putExtra("editId",item.getId());
                inflater.getContext().startActivity(intent);
            }

            @Override
            public void onLongClick() {
                super.onLongClick();
                Toast.makeText(inflater.getContext(),"It's a long click",Toast.LENGTH_SHORT).show();
                // your on onLongClick here
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                // your swipe right to left here.
                // Make normal your text


                db.updateAction(item.getId(),0);
                item.setAction(0);
                holder.taskDesc.setPaintFlags(holder.taskDesc.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                notifyDataSetChanged();
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                // your swipe left to right here.
                // Make strikethrough your text


                //mp = MediaPlayer.create(inflater.getContext(), R.raw.draw_line);
                //vib = (Vibrator) inflater.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                //vib.vibrate(10);
                //mp.start();

                db.updateAction(item.getId(),1);
                item.setAction(1);
                holder.taskDesc.setPaintFlags(holder.taskDesc.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }



    class TaskHolder extends RecyclerView.ViewHolder{

        private TextView taskDesc;
        private TextView time;

        private View container;

        public TaskHolder(View itemView) {
            super(itemView);

            taskDesc = (TextView)itemView.findViewById(R.id.task_desc);
            Typeface tf = Typeface.createFromAsset(taskDesc.getContext().getAssets(),"fonts/Delius-Regular.ttf");
            taskDesc.setTypeface(tf);
            time = (TextView)itemView.findViewById(R.id.task_time);
            time.setTypeface(tf);


            container = itemView.findViewById(R.id.cont_item_task);

            //container.setOnTouchListener(this);
            //container.setOnClickListener(this);

        }

    }
}