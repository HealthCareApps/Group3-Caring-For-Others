package edu.fau.ngamarra2014.sync_care.Adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.fau.ngamarra2014.sync_care.Data.User;
import edu.fau.ngamarra2014.sync_care.PatientActivity;
import edu.fau.ngamarra2014.sync_care.R;

public class PatientRecyclerAdapter extends RecyclerView.Adapter<PatientRecyclerAdapter.ViewHolder>{

    User user = User.getInstance();

    private ArrayList<String> titles = new ArrayList<String>();
    private ArrayList<String> details = new ArrayList<String>();
    private ArrayList<String> ids = new ArrayList<String>();
    private int[] images = { R.drawable.mario_icon};

    public PatientRecyclerAdapter(){

        for(int i = 0; i < user.getNumberOfPatients(); i++){
            titles.add(user.getPatient(i).getName());
            details.add("DOB: " + user.getPatient(i).getDOB());
            ids.add("ID: " + user.getPatient(i).getID());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_patient_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemTitle.setText(titles.get(i));
        viewHolder.itemDetail.setText(details.get(i));
        viewHolder.itemID.setText(ids.get(i));
        viewHolder.itemImage.setImageResource(images[0]);
    }
    @Override
    public int getItemCount() {
        return titles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView itemImage;
        public TextView itemTitle;
        public TextView itemDetail;
        public TextView itemID;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage =
                    (ImageView) itemView.findViewById(R.id.item_image);
            itemTitle =
                    (TextView) itemView.findViewById(R.id.item_title);
            itemDetail =
                    (TextView) itemView.findViewById(R.id.item_detail);
            itemID =
                    (TextView) itemView.findViewById(R.id.item_id);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    user.setCurrentPatient(getAdapterPosition());
                    v.getContext().startActivity(new Intent(v.getContext(), PatientActivity.class));

                }
            });
        }
    }
}

