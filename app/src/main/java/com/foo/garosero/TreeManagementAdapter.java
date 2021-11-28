package com.foo.garosero;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.foo.garosero.data.UserInfo;
import com.foo.garosero.mviewmodel.myViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TreeManagementAdapter extends RecyclerView.Adapter<TreeManagementAdapter.ViewHolder> {
    UserInfo ud;


    public TreeManagementAdapter(UserInfo ud){
        this.ud = ud;
    }
    @NonNull
    @Override
    public TreeManagementAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tree_management_view_pager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_tree_name.setText(ud.getTreeList().get(position).getTree_name());
        holder.tv_tree_day.setText(getTreeDay(ud.getTreeList().get(position).getStart_date()));
        Log.d("adapter", holder.tv_tree_day.getText().toString());
        /* todo 수정 */
//        setBackgroundImageview(holder.treeCharacter, R.drawable.mid_tree);

    }

    @Override
    public int getItemCount() {
        return ud.getTreeList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_tree_name, tv_tree_day;
        ImageView treeCharacter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            treeCharacter = itemView.findViewById(R.id.treeManagement_ImageView_treeCharacter);
            tv_tree_name = itemView.findViewById(R.id.treeManagement_TextView_treeName);
            tv_tree_day = itemView.findViewById(R.id.treeManagement_TextView_treeDay);

        }
    }


    // 이미지 뷰 채우기
    public void setBackgroundImageview(ImageView imageView, int source){
        imageView.setBackground(ContextCompat.getDrawable(imageView.getContext(), source));
    }

    private String getTreeDay(String start_date){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long calDateDays = 0;
        String tree_day = "";

        if (start_date.equals("")){
            return "";
        }

        try {
            Date currDate = calendar.getTime();
            Date lastDate = sdf.parse(start_date);
            long calDate = currDate.getTime() - lastDate.getTime();
            calDateDays = calDate / ( 24*60*60*1000);
            tree_day = ud.getName()+"님과 함께한지 "+Math.abs(calDateDays)+"일째";

        } catch (ParseException e) {
            Log.e("TreeManagementFrag",e.toString());
        }
        return tree_day;
    }
}
