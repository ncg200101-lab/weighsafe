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
import com.weigh.safemobile.activities.NewVehicleActivity;
import com.weigh.safemobile.app.AppConstants;
import com.weigh.safemobile.app.MainApplication;
import com.weigh.safemobile.fragments2.VehicleSetupFragment;
import com.weigh.safemobile.helpers.PreferenceManager;
import com.weigh.safemobile.models.Pusher;
import com.weigh.safemobile.models.SetupVehicleModel;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class NewVehicleAdapter extends RecyclerView.Adapter<NewVehicleAdapter.VehicleViewHolder> {
    private VehicleSetupFragment parent;
    private List<SetupVehicleModel> mVehicleList;

    public void addItem(SetupVehicleModel model) {
        mVehicleList.add(model);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        this.mVehicleList.remove(position);
        notifyDataSetChanged();
    }

    public void updateItem(int position, SetupVehicleModel model) {
        this.mVehicleList.set(position, model);
        notifyItemChanged(position);
    }

    public void removeAll() {
        this.mVehicleList.clear();
        notifyDataSetChanged();
    }


    // data is passed into the constructor
    public NewVehicleAdapter(VehicleSetupFragment _parent, List<SetupVehicleModel> data) {
        this.parent = _parent;
        this.mVehicleList = data;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_vehicle, viewGroup, false);
        return new VehicleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        SetupVehicleModel item = mVehicleList.get(position);

        holder.txtVehicleName.setText(item.getStrTitle());
        holder.txtGvwr.setText(String.valueOf(item.getmGVWR()));
        holder.txtGcwr.setText(String.valueOf(item.getmGCWR()));
        holder.txtMcc.setText(String.valueOf(item.getmMCC()));

        /*
        if (item.isSampleItem())
        {
            holder.txtVinNumber.setText("VIN: ---");
            holder.btnActive.setText("SELECT");
            holder.btnActive.setBackgroundResource(R.drawable.round_set_default);
        } else
        {

         */
            int currIndex = PreferenceManager.getCurrentVehicle(parent.getContext());
            if (position == currIndex) {
                holder.btnActive.setText("ACTIVE");
                holder.btnActive.setBackgroundResource(R.drawable.round_set_active);
            } else {
                holder.btnActive.setText("SET");
                holder.btnActive.setBackgroundResource(R.drawable.round_set_default);
            }

            if (item.getVin().isEmpty() || item.getVin().equals(""))
                holder.txtVinNumber.setText("VIN: ---");
            else
                holder.txtVinNumber.setText("VIN: " + item.getVin());
        //}

        holder.btnActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_ACTIVE_VEHICLE, position, item));
            }
        });

        holder.ll_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (!item.isSampleItem())
                //{
                    Intent intent = new Intent(parent.getActivity(), NewVehicleActivity.class);
                    intent.putExtra("editable", true);
                    intent.putExtra("position", position);
                    intent.putExtra("realmId", item.getnRealmId());
                    MainApplication.getInstance().setVehicleUpdateListener(parent);
                    parent.startActivity(intent);
                //}
            }
        });

    }

    @Override
    public int getItemCount() {
        return mVehicleList.size() > 0 ? mVehicleList.size() : 0;
    }

    public class VehicleViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll_root;
        public TextView txtVehicleName, txtVinNumber;
        public Button btnActive;

        public TextView txtGvwr, txtGcwr, txtMcc;

        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);

            ll_root = (LinearLayout) itemView.findViewById(R.id.layout_vehicle);
            txtVehicleName = (TextView) itemView.findViewById(R.id.txt_vehicle_title);
            btnActive = (Button) itemView.findViewById(R.id.btn_activate);
            txtVinNumber = (TextView) itemView.findViewById(R.id.txt_vin_number);

            txtGvwr = (TextView) itemView.findViewById(R.id.val_gvwr);
            txtGcwr = (TextView) itemView.findViewById(R.id.val_gcwr);
            txtMcc = (TextView) itemView.findViewById(R.id.val_mcc);

        }
    }
}
