package com.weigh.safemobile.adapters;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.weigh.safemobile.R;
import com.weigh.safemobile.app.AppConstants;
import com.weigh.safemobile.app.DatabaseManager;
import com.weigh.safemobile.helpers.AppHelper;
import com.weigh.safemobile.helpers.PreferenceManager;
import com.weigh.safemobile.models.Pusher;
import com.weigh.safemobile.models.VehiclesModel;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

public class VehicleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RealmList<VehiclesModel> mVehiclesModel;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();

    public VehicleAdapter(RealmList<VehiclesModel> mVehiclesModel) {
        this.mVehiclesModel = mVehiclesModel;
    }

    public void addItem(VehiclesModel model) {
        mVehiclesModel.add(model);
        notifyDataSetChanged();
    }

    public void updateItem(int position, VehiclesModel model) {
        this.mVehiclesModel.set(position, model);
        notifyItemChanged(position);
    }

    public void updateItem(VehiclesModel mVehiclesModel) {
        for(int i = 0; i < this.mVehiclesModel.size(); i++){
            if(this.mVehiclesModel.get(i).getnRealmId() == mVehiclesModel.getnRealmId()){
                this.mVehiclesModel.set(i, mVehiclesModel);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void removeItem(int position) {
        this.mVehiclesModel.remove(position);
        notifyItemRemoved(position);
    }

    public void saveStates(Bundle outState) {
        binderHelper.saveStates(outState);
    }
    public void restoreStates(Bundle inState) {
        binderHelper.restoreStates(inState);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_vehicles, viewGroup, false);
        return new VehicleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof VehicleViewHolder) {
            final VehicleViewHolder vehicleViewHolder = (VehicleViewHolder) viewHolder;
            final VehiclesModel vehicleModel = this.mVehiclesModel.get(position);

            if(position == PreferenceManager.getPositionID(vehicleViewHolder.rl_main.getContext())){
                vehicleViewHolder.btn_delete.setVisibility(View.INVISIBLE);
            }
            else{
                vehicleViewHolder.btn_delete.setVisibility(View.VISIBLE);
            }

            binderHelper.bind(vehicleViewHolder.row_vehicle_item, Integer.toString(position));

            vehicleViewHolder.bind(vehicleModel);

            vehicleViewHolder.setTitle(vehicleModel.getStrVehicle() + " + " + vehicleModel.getStrTrailer());
        }
    }

    @Override
    public int getItemCount() {
        return mVehiclesModel.size() > 0 ? mVehiclesModel.size() : 0;
    }

    public class VehicleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_vehicle)
        TextView textVehicle;
        @BindView(R.id.rl_main)
        RelativeLayout rl_main;
        @BindView(R.id.row_vehicle_item)
        SwipeRevealLayout row_vehicle_item;

        @BindView(R.id.btn_delete)
        Button btn_delete;
        @BindView(R.id.btn_edit)
        Button btn_edit;

        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            setTypeFaces();

            /*** swipe layout ***/
        }
        private void setTypeFaces() {
            textVehicle.setTypeface(AppHelper.setTypeFace(itemView.getContext(), "Roboto-Bold"));
        }

        public void bind(final VehiclesModel data) {
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(DatabaseManager.deleteVehicle(data)){
                        AppHelper.CustomToast(itemView.getContext(), "The data was successfully deleted.");
                        removeItem(getAdapterPosition());
                    }
                    else
                        AppHelper.CustomToast(itemView.getContext(), "Delete failed");
                    EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_DEL_VEHICLE, data));
                }
            });


            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_UPDATE_VEHICLE, data));
                }
            });

            rl_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("TAG" , "ss"+getAdapterPosition());
                    EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_SELECTED_VEHICLE_POSITION, getAdapterPosition(), data));
                    notifyDataSetChanged();
                }
            });
        }

        void setTitle(String title) {
            textVehicle.setText(title);
        }

    }
}
