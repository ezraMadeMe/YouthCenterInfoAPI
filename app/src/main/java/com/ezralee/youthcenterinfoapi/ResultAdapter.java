package com.ezralee.youthcenterinfoapi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.VH> {

    Context context;
    ArrayList<YouthCenterItem> youthCenterItems;

    public ResultAdapter(Context context, ArrayList<YouthCenterItem> youthCenterItems) {
        this.context = context;
        this.youthCenterItems = youthCenterItems;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.recycler_item, parent, false);
        VH holder = new VH(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        YouthCenterItem youthCenterItem = youthCenterItems.get(position);
        holder.polyBizSjnm.setText(youthCenterItem.polyBizSjnm);
        holder.polyItcnCn.setText(youthCenterItem.polyItcnCn);
        holder.plcyTpNm.setText(youthCenterItem.plcyTpNm);
        holder.cnsgNmor.setText(youthCenterItem.cnsgNmor);
        holder.rqutUrla.setText(youthCenterItem.rqutUrla);



        holder.rqutUrla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlSel=holder.rqutUrla.getText().toString();

                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(urlSel));
                context.startActivity(intent);

            }
        });



        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlSel = holder.rqutUrla.getText().toString();

                if (urlSel.contains("http")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlSel));
                    context.startActivity(intent);

                } else {
                    Toast.makeText(context, "링크가 존재하지 않습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return youthCenterItems.size();
    }


    class VH extends RecyclerView.ViewHolder {

        TextView polyBizSjnm, polyItcnCn, plcyTpNm, cnsgNmor, rqutUrla;
        Button btn;

        public VH(@NonNull View itemView) {
            super(itemView);

            polyBizSjnm = itemView.findViewById(R.id.polyBizSjnm);
            polyItcnCn = itemView.findViewById(R.id.polyItcnCn);
            plcyTpNm = itemView.findViewById(R.id.plcyTpNm);
            cnsgNmor = itemView.findViewById(R.id.cnsgNmor);
            rqutUrla = itemView.findViewById(R.id.rqutUrla);
            btn = itemView.findViewById(R.id.btn);
        }
    }//inner class
}
