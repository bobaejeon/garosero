package com.foo.garosero.ui.home.treeinfo;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.foo.garosero.R;
import com.foo.garosero.data.TreeInfo;
import com.foo.garosero.data.UserInfo;
import com.foo.garosero.myUtil.ServerHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class TreeInfoAdapter extends RecyclerView.Adapter<TreeInfoAdapter.ViewHolder> {
    UserInfo ud;


    public TreeInfoAdapter(UserInfo ud){
        this.ud = ud;
    }
    @NonNull
    @Override
    public TreeInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tree_info_view_pager, parent, false);
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
        setBackgroundImageview(holder.treeCharacter, treeInfo.getKind(), treeInfo.getLevel());

        // 나무이름 수정 버튼
        holder.btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. 현재 객체와 treeid가 같은 db 항목의 ref를 얻는다
                // 2. 나무 이름 업데이트 3. 업데이트 성공시 토스트 띄우고 데이터 다시 불러오기
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Trees_taken");

                Query query = ref.orderByChild("tree_id").equalTo(treeInfo.getTree_id());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot snap:snapshot.getChildren()){
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("tree_name", holder.tv_tree_name.getText().toString());
                                snap.getRef().updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        ServerHelper.initServer();
                                        Toast.makeText(view.getContext(), "완료되었습니다.", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }else {
                            Log.e("TreeInfoAdapter", "no data");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Getting Post failed, log a message
                        Log.w("TreeInfoAdapter", "onCancelled", error.toException());
                    }
                });
            }
        });
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
        Button btn_submit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            treeCharacter = itemView.findViewById(R.id.treeManagement_ImageView_treeCharacter);
            tv_tree_name = itemView.findViewById(R.id.treeManagement_TextView_treeName);
            tv_tree_day = itemView.findViewById(R.id.treeManagement_TextView_treeDay);
            tv_level = itemView.findViewById(R.id.treeManagement_TextView_count);
            progressBar = itemView.findViewById(R.id.treeManagement_ProgressBar);
            tv_desc_title = itemView.findViewById(R.id.treeManagement_TextView_treeTitle);
            tv_desc_content = itemView.findViewById(R.id.treeManagement_TextView_treeDesc);
            btn_submit = itemView.findViewById(R.id.bt_submit);
        }
    }


    // 이미지 뷰 채우기
    public void setBackgroundImageview(ImageView imageView, String kind, int level){
        int source;
        if (level == 1){
            source = R.drawable.tree_lv1;
        } else if(level == 2){
            source = R.drawable.tree_lv2;
        } else{
            switch (kind) {
                case "은행나무":
                    source = R.drawable.tree_lv3_ginkgo;
                    break;
                case "소나무":
                    source = R.drawable.tree_lv3_pine;
                    break;
                case "왕벚나무":
                    source = R.drawable.tree_lv3_cherry;
                    break;
                case "살구나무":
                    source = R.drawable.tree_lv3_apricot;
                    break;
                case "느티나무":
                    source = R.drawable.tree_lv3_zelkova;
                    break;
                case "이팝나무":
                    source = R.drawable.tree_lv3_popular;
                    break;
                default:
                    source = R.drawable.big_tree;
                    break;
            }
        }
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

    private String getDescTitle(String road, String kind){
        return road+"에 있는 "+kind;
    }

    private String getDescContents(String kind){
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