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

import com.airbnb.lottie.LottieAnimationView;
import com.foo.garosero.R;
import com.foo.garosero.data.TreeInfo;
import com.foo.garosero.data.UserInfo;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpager_treeinfo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TreeInfo treeInfo = ud.getTreeList().get(position);
        holder.tv_tree_name.setText(treeInfo.getTree_name());
        holder.tv_tree_day.setText(getTreeDay(treeInfo.getStart_date()));
        holder.tv_level.setText(String.valueOf(treeInfo.getXp()/10+1));
        holder.progressBar.setProgress(treeInfo.getXp()%10);
        holder.tv_desc_title.setText(getDescTitle(treeInfo.getLocation(),treeInfo.getTree_type()));
        holder.tv_desc_content.setText(getDescContents(treeInfo.getTree_type()));
        setBackgroundImageview(holder.treeCharacter, treeInfo.getTree_type(), treeInfo.getXp()/10+1);

        // 캐릭터 클릭시
        holder.treeCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.lottie.getVisibility() == View.GONE){
                    holder.lottie.setVisibility(View.VISIBLE);
                    holder.lottie.playAnimation();

                } else {
                    holder.lottie.setVisibility(View.GONE);
                }
            }
        });

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
                                String newName = holder.tv_tree_name.getText().toString();
                                map.put("tree_name", newName);
                                snap.getRef().updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        treeInfo.setTree_name(newName);
                                        Toast.makeText(view.getContext(), "완료되었습니다.", Toast.LENGTH_SHORT).show();
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
        LottieAnimationView lottie;

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
            lottie = itemView.findViewById(R.id.viewPager_lottie);
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
        if (start_date==null) return "";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
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
                res = "은행나무는 살아 있는 화석이라 할 만큼 오래된 나무로 " +
                        "우리나라, 일본, 중국 등지에 분포하고 있어요. 우리나라에는 중국에서 유교와 불교가 전해질 때" +
                        " 같이 들어온 것으로 전해지고 있어요. 가을 단풍이 매우 아름답고 병충해가 없으며 " +
                        "넓고 짙은 그늘을 제공한다는 장점이 있어 가로수로도 많이 심어요.";
                break;
            case "소나무":
                res = "겨울에도 항상 푸른 빛을 유지하는 상록수로 러시아에서는 연해주에 극히 일부가 분포해 " +
                        "멸종 위기종으로 지정된 보호식물이에요. 소나무꽃은 보통 4월 하순부터 5월 상순까지 " +
                        "한 나무에 암꽃과 수꽃이 나란히 피어요. 암꽃은 가지 끝에 2, 3개씩 달려 있고, " +
                        "수꽃은 타원형 모양에 꽃밥이 있어요. 소나무 꽃말은 '변하지 않는 사랑' '불로장생' " +
                        "'영원한 푸름' 등으로 불려요.";
                break;
            case "느티나무":
                res = "느티나무는 우리나라를 비롯하여 일본, 대만, 중국 등의 따뜻한 지방에 분포하고 있어요. " +
                        "가지가 사방으로 퍼져 자라서 둥근 형태로 보이며, 꽃은 5월에 피고 열매는 원반모양으로 10월에 익어요. " +
                        "줄기가 굵고 수명이 길어서 쉼터역할을 하는 정자나무로 이용되거나 마을을 보호하고 지켜주는 당산나무로 보호를 받아왔어요.";
                break;
            case "왕벚나무":
                res = "왕벚나무는 장미과에 속하는 나무로 꽃은 4월경에 잎보다 먼저 피는데 " +
                        "백색 또는 연한 홍색을 띄어요. 지형이 높은 곳에 자라는 산벚나무와 " +
                        "그보다 낮은 곳에 자라는 올벚나무 사이에서 태어난 잡종이란 설도 있으나, " +
                        "제주도와 전라북도 대둔산에서만 자생하는 우리나라 특산종이에요.";
                break;
            case "이팝나무":
                res = "이팝나무는 나무 꽃이 밥알(이밥)을 닮았다고 하여 이팝나무라고 불러요.\n\n"+
                        "우리나라의 크고 오래된 이팝나무에는 거의 한결같은 이야기가 전해지고 있는데, " +
                        "그것은 이팝나무의 꽃이 많이 피고 적게 피는 것으로써 그해 농사의 풍년과 흉년을 점칠 수 있대요. " +
                        "이팝나무는 물이 많은 곳에서 잘 자라는 식물이므로 비의 양이 적당하면 꽃이 활짝 피고, 부족하면 잘 피지 못해요. " +
                        "물의 양은 벼농사에도 관련되는 것으로, 오랜 경험을 통한 자연관찰의 결과로서 이와 같은 전설이 생겼어요.";
                break;
            case "살구나무":
                res = "살구나무는 장미과에 속하는 낙엽소교목으로 높이 5m 정도에요. " +
                        "살구는 7월에 황색 또는 황적색으로 익으며 맛이 시고 달아요. 원산지는 동부아시아에요. " +
                        "우리 나라에서 살구가 재배되기 시작한 때는 확실하지 않으나, " +
                        "오래전부터 중부 이북지방의 산야에서 자생되어왔던 것으로 추정돼요.";
                break;
            default:
                res = "가로수는 아주 중요하답니다";
        }
        return res;
    }
}