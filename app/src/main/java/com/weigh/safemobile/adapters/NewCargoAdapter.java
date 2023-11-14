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
import com.weigh.safemobile.activities.NewCargoActivity;
import com.weigh.safemobile.app.AppConstants;
import com.weigh.safemobile.app.MainApplication;
import com.weigh.safemobile.fragments2.CargoSetupFragment;
import com.weigh.safemobile.helpers.PreferenceManager;
import com.weigh.safemobile.models.Pusher;
import com.weigh.safemobile.models.SetupCargoModel;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class NewCargoAdapter extends RecyclerView.Adapter<NewCargoAdapter.CargoViewHolder> {

    private CargoSetupFragment parent;
    private List<SetupCargoModel> mCargoList;
    //private List<Integer> defaultSelected;

    public void addItem(SetupCargoModel model) {
        mCargoList.add(model);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        this.mCargoList.remove(position);
        notifyDataSetChanged();
    }

    public void updateItem(int position, SetupCargoModel model) {
        this.mCargoList.set(position, model);
        notifyItemChanged(position);
    }

    public void removeAll()
    {
        this.mCargoList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<SetupCargoModel> models)
    {
        this.mCargoList.addAll(models);
        notifyDataSetChanged();
    }

    // data is passed into the constructor
    public NewCargoAdapter(CargoSetupFragment _parent, List<SetupCargoModel> data) {
        this.parent = _parent;
        this.mCargoList = data;
        //this.defaultSelected = defaultSelected;
    }

    @NonNull
    @Override
    public NewCargoAdapter.CargoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_cargo, viewGroup, false);
        return new NewCargoAdapter.CargoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewCargoAdapter.CargoViewHolder holder, int position) {
        SetupCargoModel item = mCargoList.get(position);

        holder.txtCargoName.setText(item.getName());
        holder.txtLbs.setText(String.valueOf(item.getCargoWeight()));
        holder.txtType.setText(item.getmType());

        /*
        if (item.isSampleItem())
        {
            if (defaultSelected.contains(position)) {
                holder.btnActive.setText("ACTIVE");
                holder.btnActive.setBackgroundResource(R.drawable.round_set_active);
            } else {
                holder.btnActive.setText("SET");
                holder.btnActive.setBackgroundResource(R.drawable.round_set_default);
            }
        } else {*/
            List<String> cargoIndexes = PreferenceManager.getCurrentCargoList(parent.getContext());
            if (cargoIndexes.contains(String.valueOf(position))) {
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
                /*
                if (item.isSampleItem())
                {
                    if (defaultSelected.contains(position))
                    {
                        defaultSelected.remove(position);
                        holder.btnActive.setText("SELECT");
                        holder.btnActive.setBackgroundResource(R.drawable.round_set_default);
                    } else
                    {
                        defaultSelected.add(position);
                        holder.btnActive.setText("ACTIVE");
                        holder.btnActive.setBackgroundResource(R.drawable.round_set_active);
                    }
                }
                else
                 */
                    EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_ACTIVE_CARGO, position, item));
            }
        });

        holder.ll_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (!item.isSampleItem())
                {
                    Intent intent = new Intent(parent.getActivity(), NewCargoActivity.class);
                    intent.putExtra("editable", true);
                    intent.putExtra("position", position);
                    intent.putExtra("realmId", item.getnRealmId());
                    MainApplication.getInstance().setCargoUpdateListener(parent);
                    parent.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCargoList.size() > 0 ? mCargoList.size() : 0;
    }

    public class CargoViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll_root;
        public TextView txtCargoName;
        public Button btnActive;

        public TextView txtLbs, txtType;

        public CargoViewHolder(@NonNull View itemView) {
            super(itemView);

            ll_root = (LinearLayout) itemView.findViewById(R.id.layout_cargo);
            txtCargoName = (TextView) itemView.findViewById(R.id.txt_cargo_title);
            btnActive = (Button) itemView.findViewById(R.id.btn_activate);

            txtLbs = (TextView) itemView.findViewById(R.id.val_lbs);
            txtType = (TextView) itemView.findViewById(R.id.val_type);
        }
    }
}