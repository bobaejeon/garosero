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
import java.util.Random;

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
        holder.tv_level.setText(String.valueOf(treeInfo.getXp()/10+1));
        holder.progressBar.setProgress(treeInfo.getXp()%10);
        holder.tv_desc_title.setText(getDescTitle(treeInfo.getRoad(),treeInfo.getKind()));
        holder.tv_desc_content.setText(getDescContents(treeInfo.getKind()));
        setBackgroundImageview(holder.treeCharacter, treeInfo.getKind(), treeInfo.getXp()/10+1);

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
                res = "가을이 되면 낙엽이 지기 전에 잎사귀가 샛노랗게 물들어 아름답고, " +
                        "다른 여러 장점이 있어 가로수로 많이 심어요.\n\n" +
                        "은행나무에서 은행은 그 열매가 '은빛이 나는 살구'라는 뜻에서 은행나무라고 불리게 되었다고 합니다.";
                break;
            case "소나무":
                res = "소나무라는 이름은 ‘으뜸’이라는 ‘수리’라는 말이 변한 우리말 ‘솔’에서 유래되었어요.\n\n" +
                        "소나무는 성장이 더딘 편이고, 손이 많이 가요. " +
                        "하지만 바늘로 되어있는 침엽수 덕분에 미세먼지 절감에 아주 탁월해 가로수로 많이 쓰이고 있어요.";
                break;
            case "느티나무":
                res = "느티나무는 우리나라 거의 모든 지역에서 무성하게 자라고 시원한 나무그늘을 만들어줘요.\n\n"+
                        "느티나무는 내한성이 강해 동절기 월동작업을 진행하지 않아도 돼요. " +
                        "그러나 수분공급이 적절하게 이뤄지지 않을 경우 스트레스를 받을 수 있어 수분관리가 중요해요.";
                break;
            case "왕벚나무":
                res = "봄에 눈이 부실 듯 피어나는 화려한 꽃이 아름다운 왕벚나무는 대표적인 봄나무에요.\n\n" +
                        "왕벚나무는 우리나라 전역에서 재배가 가능해요. 하지만 각종 해충이 많이 발생하는 편이기 때문에 적당한 살충제로 관리가 필요해요.";
                break;
            case "이팝나무":
                res = "이팝나무는 나무 꽃이 밥알(이밥)을 닮았다고 하여 이팝나무라고 불러요\n\n"+
                        "습한 곳에서도 잘 자라고 꽃이 아름다워 가로수나 정원수로 많이 사용되지만 가지가 약해 쉽게 갈라져요. 따라서 정원수로 심을 경우 너무 집 근처에 심지 않는 것이 좋아요.";
                break;
            case "살구나무":
                res = "살구는 '살(솔)고'라고 표기했던 순수한 우리말에서 유래되었어요. 꽃이 아름다워 옛날부터 우리 주변 야산에서 흔히 볼 수 있는 나무였다고 합니다. \n\n"+
                        "살구나무는 어디서든 잘 자라지만 건조한 환경에는 약한 편이라 주의를 기울여주세요.";
                break;
            default:
                res = "가로수는 아주 중요하답니다";
        }
        return res;
    }
}