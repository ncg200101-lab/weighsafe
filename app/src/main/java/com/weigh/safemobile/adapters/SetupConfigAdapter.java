package com.weigh.safemobile.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.weigh.safemobile.R;
import com.weigh.safemobile.app.AppConstants;
import com.weigh.safemobile.app.DatabaseManager;
import com.weigh.safemobile.helpers.AppHelper;
import com.weigh.safemobile.helpers.PreferenceManager;
import com.weigh.safemobile.models.ConfigPusher;
import com.weigh.safemobile.models.Configuration;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

public class SetupConfigAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Configuration> mConfigurations;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    private Context mContext;

    public SetupConfigAdapter(RealmList<Configuration> mConfigModels, Context context) {
        this.mConfigurations = mConfigModels;
        this.mContext = context;
    }

    public void addItem(Configuration model) {
        mConfigurations.add(model);
        notifyDataSetChanged();
    }

    public void updateItem(int position, Configuration model) {
        this.mConfigurations.set(position, model);
        notifyItemChanged(position);
    }

    public void updateItem(Configuration configuration) {
        for(int i = 0; i < this.mConfigurations.size(); i++){
            if(this.mConfigurations.get(i).getnRealmId() == configuration.getnRealmId()){
                this.mConfigurations.set(i, configuration);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void removeItem(int position) {
        this.mConfigurations.remove(position);
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_vehicles, viewGroup, false);
        return new SetupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof SetupViewHolder) {
            final SetupViewHolder vehicleViewHolder = (SetupViewHolder)viewHolder;
            final Configuration configuration = this.mConfigurations.get(position);

            if(position == PreferenceManager.getPositionID(vehicleViewHolder.rl_main.getContext())){
                vehicleViewHolder.btn_delete.setVisibility(View.INVISIBLE);
            }
            else{
                vehicleViewHolder.btn_delete.setVisibility(View.VISIBLE);
            }

            binderHelper.bind(vehicleViewHolder.row_vehicle_item, Integer.toString(position));

            vehicleViewHolder.bind(configuration, position);

            vehicleViewHolder.setTitle(configuration.getName());
        }
    }

    @Override
    public int getItemCount() {
        return mConfigurations.size() > 0 ? mConfigurations.size() : 0;
    }

    public class SetupViewHolder extends RecyclerView.ViewHolder {
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

        public SetupViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            setTypeFaces();

            /*** swipe layout ***/
        }
        private void setTypeFaces() {
            textVehicle.setTypeface(AppHelper.setTypeFace(itemView.getContext(), "Roboto-Bold"));
        }

        public void bind(final Configuration data, int Position) {

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DatabaseManager.deleteConfiguration(data)){
                        int currentIndex = PreferenceManager.getPositionID(mContext);
                        if (Position < currentIndex)
                            PreferenceManager.setPositionID(mContext, currentIndex - 1);

                        AppHelper.CustomToast(itemView.getContext(), "The data was successfully deleted.");
                        removeItem(getAdapterPosition());
                    }
                    else
                        AppHelper.CustomToast(itemView.getContext(), "Delete failed");

                    EventBus.getDefault().post(new ConfigPusher(AppConstants.EVENT_BUS_DEL_VEHICLE, data));
                }
            });


            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new ConfigPusher(AppConstants.EVENT_BUS_UPDATE_VEHICLE, Position, data));
                }
            });

            rl_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new ConfigPusher(AppConstants.EVENT_BUS_SELECTED_VEHICLE_POSITION, Position, data));
                    notifyDataSetChanged();
                }
            });
        }

        void setTitle(String title) {
            textVehicle.setText(title);
        }

    }
}
