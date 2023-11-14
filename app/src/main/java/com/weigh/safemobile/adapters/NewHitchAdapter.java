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
import com.weigh.safemobile.activities.NewHitchActivity;
import com.weigh.safemobile.app.AppConstants;
import com.weigh.safemobile.app.MainApplication;
import com.weigh.safemobile.fragments2.HitchSetupFragment;
import com.weigh.safemobile.helpers.PreferenceManager;
import com.weigh.safemobile.models.Pusher;
import com.weigh.safemobile.models.SetupHitchModel;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class NewHitchAdapter extends RecyclerView.Adapter<NewHitchAdapter.HitchViewHolder> {

    private HitchSetupFragment parent;
    private List<SetupHitchModel> mHitchList;

    public void addItem(SetupHitchModel model) {
        mHitchList.add(model);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        this.mHitchList.remove(position);
        notifyDataSetChanged();
    }

    public void updateItem(int position, SetupHitchModel model) {
        this.mHitchList.set(position, model);
        notifyItemChanged(position);
    }

    public void removeAll() {
        this.mHitchList.clear();;
        notifyDataSetChanged();
    }

    // data is passed into the constructor
    public NewHitchAdapter(HitchSetupFragment _parent, List<SetupHitchModel> data) {
        this.parent = _parent;
        this.mHitchList = data;
    }

    @NonNull
    @Override
    public NewHitchAdapter.HitchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_hitch, viewGroup, false);
        return new NewHitchAdapter.HitchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewHitchAdapter.HitchViewHolder holder, int position) {
        SetupHitchModel item = mHitchList.get(position);

        holder.txtHitchName.setText(item.getBrand());
        holder.txtMaxTongue.setText(String.valueOf(item.getmMaxTongue()));
        holder.txtShank.setText(item.getmShank());

        /*
        if (item.isSampleItem())
        {
            if (item.getHitchType().equals("Weight Distribution"))
                holder.txtBall.setText("n/a");
            else
                holder.txtBall.setText(item.getmBall());

            holder.btnActive.setText("SET");
            holder.btnActive.setBackgroundResource(R.drawable.round_set_default);
        } else
        {
         */
            if (item.isOnSafeHitch() == false && item.getBrand().contains(item.getHitchType()) == false)
            {
                holder.txtHitchName.setText(item.getBrand() + " " + item.getHitchType());
            }

            holder.txtBall.setText(item.getmBall());
            if (item.getHitchType().equals("Gooseneck")) {
                holder.txtBall.setText("n/a");
                holder.txtShank.setText("n/a");
            }

            if (item.isOnSafeHitch() && (item.getBrand().equals("Fixed Height Ball Mount") || item.getBrand().equals("Universal Tow Ball")))
            {
                holder.txtBall.setText("n/a");
                holder.txtShank.setText("n/a");
            }

            if (item.isOnSafeHitch() && (item.getBrand().equals("True Tow Weight Distribution Hitch"))) {
                holder.txtBall.setText("n/a");
                holder.txtShank.setText(item.getmShank());
            }

            int currIndex = PreferenceManager.getCurrentHitch(parent.getContext());
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
                EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_ACTIVE_HITCH, position, item));
            }
        });

        holder.ll_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if (!item.isSampleItem())
                {
                 */
                    Intent intent = new Intent(parent.getActivity(), NewHitchActivity.class);
                    intent.putExtra("editable", true);
                    intent.putExtra("position", position);
                    intent.putExtra("realmId", item.getnRealmId());
                    MainApplication.getInstance().setHitchUpdateListener(parent);
                    parent.startActivity(intent);
                //}
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHitchList.size() > 0 ? mHitchList.size() : 0;
    }

    public class HitchViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll_root;
        public TextView txtHitchName;
        public Button btnActive;

        public TextView txtMaxTongue, txtBall, txtShank;

        public HitchViewHolder(@NonNull View itemView) {
            super(itemView);

            ll_root = (LinearLayout) itemView.findViewById(R.id.layout_hitch);
            txtHitchName = (TextView) itemView.findViewById(R.id.txt_hitch_title);
            btnActive = (Button) itemView.findViewById(R.id.btn_activate);

            txtMaxTongue = (TextView) itemView.findViewById(R.id.val_max_tonguelbs);
            txtBall = (TextView) itemView.findViewById(R.id.val_ball);
            txtShank = (TextView) itemView.findViewById(R.id.val_shank);
        }
    }
}
