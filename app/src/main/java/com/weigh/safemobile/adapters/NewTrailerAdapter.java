package com.weigh.safemobile.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.weigh.safemobile.R;
import com.weigh.safemobile.activities.NewTrailerActivity;
import com.weigh.safemobile.app.AppConstants;
import com.weigh.safemobile.app.MainApplication;
import com.weigh.safemobile.fragments2.TrailerSetupFragment;
import com.weigh.safemobile.helpers.PreferenceManager;
import com.weigh.safemobile.models.Pusher;
import com.weigh.safemobile.models.SetupHitchModel;
import com.weigh.safemobile.models.SetupTrailerModel;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class NewTrailerAdapter extends RecyclerView.Adapter<NewTrailerAdapter.TrailerViewHolder> {

    private TrailerSetupFragment parent;
    private List<SetupTrailerModel> mTrailerList;

    public void addItem(SetupTrailerModel model) {
        mTrailerList.add(model);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        this.mTrailerList.remove(position);
        notifyDataSetChanged();
    }

    public void updateItem(int position, SetupTrailerModel model) {
        this.mTrailerList.set(position, model);
        notifyItemChanged(position);
    }

    public void removeAll() {
        this.mTrailerList.clear();
    }

    // data is passed into the constructor
    public NewTrailerAdapter(TrailerSetupFragment _parent, List<SetupTrailerModel> data) {
        this.parent = _parent;
        this.mTrailerList = data;
    }

    @NonNull
    @Override
    public NewTrailerAdapter.TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_trailer, viewGroup, false);
        return new NewTrailerAdapter.TrailerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewTrailerAdapter.TrailerViewHolder holder, int position) {
        SetupTrailerModel item = mTrailerList.get(position);

        holder.txtTrailerName.setText(item.getName());
        holder.txtGvwr.setText(String.valueOf(item.getEmptyWeight()));
        holder.txtMcc.setText(String.valueOf(item.getLoadCapacity()));
        holder.txtCoupler.setText(item.getCoupler());
        /*
        if (item.isSampleItem())
        {
            holder.btnActive.setText("SET");
            holder.btnActive.setBackgroundResource(R.drawable.round_set_default);
        } else
        {
         */
            List<SetupHitchModel> hitchModels = MainApplication.getInstance().hitchModels;
            int currHitchIndex = PreferenceManager.getCurrentHitch(parent.getContext());
            SetupHitchModel currHitch = hitchModels.get(currHitchIndex);

            if (currHitch.getHitchType().equals("Gooseneck") || currHitch.getHitchType().equals("Fifth Wheel"))
                holder.txtCoupler.setText("n/a");

            int currIndex = PreferenceManager.getCurrentTrailer(parent.getContext());
            if (position == currIndex) {
                holder.btnActive.setText("ACTIVE");
                holder.btnActive.setBackgroundResource(R.drawable.round_set_active);
            } else {
                holder.btnActive.setText("SET");
                holder.btnActive.setBackgroundResource(R.drawable.round_set_default);
            }
        //}

        holder.btnActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_ACTIVE_TRAILER, position, item));
            }
        });

        holder.ll_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (!item.isSampleItem())
                //{
                    Intent intent = new Intent(parent.getActivity(), NewTrailerActivity.class);
                    intent.putExtra("editable", true);
                    intent.putExtra("position", position);
                    intent.putExtra("realmId", item.getnRealmId());
                    MainApplication.getInstance().setTrailerUpdateListener(parent);
                    parent.startActivity(intent);
                //}
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrailerList.size() > 0 ? mTrailerList.size() : 0;
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll_root;
        public TextView txtTrailerName;
        public Button btnActive;

        public TextView txtGvwr, txtMcc, txtCoupler;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);

            ll_root = (LinearLayout) itemView.findViewById(R.id.layout_trailer);
            txtTrailerName = (TextView) itemView.findViewById(R.id.txt_trailer_title);
            btnActive = (Button) itemView.findViewById(R.id.btn_activate);

            txtGvwr = (TextView) itemView.findViewById(R.id.val_gvwr);
            txtMcc = (TextView) itemView.findViewById(R.id.val_mcc);
            txtCoupler = (TextView) itemView.findViewById(R.id.val_coupler);

        }
    }
}
