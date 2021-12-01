package com.foo.garosero.ui.home.treemanagement;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.foo.garosero.R;
import com.foo.garosero.data.TreeInfo;
import com.foo.garosero.data.UserInfo;

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
        TreeInfo treeInfo = ud.getTreeList().get(position);
        holder.tv_tree_name.setText(treeInfo.getTree_name());
        holder.tv_tree_day.setText(getTreeDay(treeInfo.getStart_date()));
        holder.tv_level.setText(String.valueOf(treeInfo.getLevel()));
        holder.progressBar.setProgress(treeInfo.getXp()%10);
        holder.tv_desc_title.setText(getDescTitle(treeInfo.getRoad(),treeInfo.getKind()));
        holder.tv_desc_content.setText(getDescContents(treeInfo.getKind()));
        /* todo 수정 */
//        setBackgroundImageview(holder.treeCharacter, R.drawable.mid_tree);

    }

    @Override
    public int getItemCount() {
        try{
            return ud.getTreeList().size();
        } catch (Exception e){
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_tree_name, tv_tree_day, tv_level, tv_desc_title, tv_desc_content;
        ImageView treeCharacter;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            treeCharacter = itemView.findViewById(R.id.treeManagement_ImageView_treeCharacter);
            tv_tree_name = itemView.findViewById(R.id.treeManagement_TextView_treeName);
            tv_tree_day = itemView.findViewById(R.id.treeManagement_TextView_treeDay);
            tv_level = itemView.findViewById(R.id.treeManagement_TextView_count);
            progressBar = itemView.findViewById(R.id.treeManagement_ProgressBar);
            tv_desc_title = itemView.findViewById(R.id.treeManagement_TextView_treeTitle);
            tv_desc_content = itemView.findViewById(R.id.treeManagement_TextView_treeDesc);
        }
    }


    // 이미지 뷰 채우기
    public void setBackgroundImageview(ImageView imageView, int source){
        imageView.setBackground(ContextCompat.getDrawable(imageView.getContext(), source));
    }

    private String getTreeDay(String start_date){
        if (start_date==null) return "";
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

    private String getDescTitle(String road, String kind){
        return road+"에 있는 "+kind;
    }

    private String getDescContents(String kind){
        if (kind==null) return "";
        String res;
        switch (kind){
            case "은행나무":
                res = "가을이 되면 낙엽이 지기 전에 잎사귀가 샛노랗게 물들어 아름답고 다른 여러 장점이 있어 가로수로 많이 심는다. 은행나무에서 은행은 그 열매가 '은빛이 나는 살구'라는 뜻에서 은행나무라고 불리게 되었다고 합니다.";
                break;
            case "소나무":
                res = "소나무는 소나무입니당ㅇㅇㅇㄹ알";
                break;
            case "느티나무":
                res = "느티나무는 좋앙ㅅ";
                break;
            default:
                res = "나무는 나무";
        }
        return res;
    }
}