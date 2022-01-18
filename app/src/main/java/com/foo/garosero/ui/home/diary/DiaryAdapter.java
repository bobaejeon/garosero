package com.foo.garosero.ui.home.diary;

import static com.foo.garosero.myUtil.ImageHelper.showImage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.foo.garosero.R;
import com.foo.garosero.data.DiaryData;
import com.foo.garosero.mviewmodel.DiaryViewModel;

import java.util.ArrayList;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder>{
    private ArrayList<DiaryData> mData = new ArrayList<DiaryData>();

    public DiaryAdapter(ArrayList<DiaryData> mData) {
        this.mData = mData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_picture;
        TextView tv_schedule, tv_memo;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // 객체 참조
            iv_picture = itemView.findViewById(R.id.recyclerView_ImageView_picture);
            tv_schedule = itemView.findViewById(R.id.recyclerView_TextView_schedule);
            tv_memo = itemView.findViewById(R.id.recyclerView_TextView_memo);
            cardView = itemView.findViewById(R.id.recyclerView_CardView);

            // 클릭이벤트
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // set DiaryData
                    int pos = getAdapterPosition();
                    DiaryViewModel.setDiaryData(mData.get(pos));

                    // start ReportActivity
                    Intent intent = new Intent(itemView.getContext(), ReportActivity.class);
                    intent.putExtra("report_mode", "update");
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public DiaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recyclerview_diary, parent, false);
        DiaryAdapter.ViewHolder viewHolder = new DiaryAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryAdapter.ViewHolder holder, int position) {
        DiaryData data = mData.get(position);
        holder.tv_memo.setText(data.getMemo());
        holder.tv_schedule.setText(data.getSchedule());

        // 이미지 띄우기
        showImage(holder.itemView.getContext(), data.getPicture(), holder.iv_picture);
    }

    @Override
    public int getItemCount() {
        try {
            return mData.size();
        }catch(Exception e){
            return 0;
        }
    }
}