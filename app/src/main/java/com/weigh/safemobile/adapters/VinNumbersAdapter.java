package com.weigh.safemobile.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.weigh.safemobile.R;
import com.weigh.safemobile.app.MainApplication;
import com.weigh.safemobile.models.VinModel;

import java.util.List;

public class VinNumbersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_ITEM = 0;
    public static final int VIEW_TYPE_HEADER = 1;

    private List<VinModel> mVinList;
    private Activity parent;

    public VinNumbersAdapter(List<VinModel> _list, Activity mParent) {
        this.mVinList = _list;
        this.parent = mParent;
    }

    public void addItem(VinModel model) {
        mVinList.add(model);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        this.mVinList.remove(position);
        notifyDataSetChanged();
    }

    public void updateItem(int position, VinModel model) {
        this.mVinList.set(position, model);
        notifyItemChanged(position);
    }

    public void removeAll() {
        this.mVinList.clear();;
        notifyDataSetChanged();
    }

    public void setListData(List<VinModel> _list) {
        this.mVinList.clear();
        this.mVinList.addAll(_list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_header_item, parent, false);
            return new VHHeader(view);
        } else if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_child_item, parent, false);
            return new VHItem(view);
        }
        throw new RuntimeException("There is no type that matches the type " + viewType + ". Make sure you are using view types correctly!");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VinModel item = mVinList.get(position);

        if (holder instanceof VHHeader) {
            ((VHHeader) holder).tvHeader.setText(item.getHeaderString());
        } else if (holder instanceof VHItem) {
            ((VHItem) holder).tvItem.setText(item.getName());
            ((VHItem) holder).ll_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainApplication.getInstance().getVindataListener().onVinDataUpdate(item);
                    parent.finish();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mVinList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mVinList.get(position).getViewType();
    }

    class VHItem extends RecyclerView.ViewHolder {
        private LinearLayout ll_root;
        private TextView tvItem;

        public VHItem(View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.txt_vin_name);
            ll_root = itemView.findViewById(R.id.ll_root);
        }
    }

    class VHHeader extends RecyclerView.ViewHolder {
        private TextView tvHeader;

        public VHHeader(View itemView) {
            super(itemView);
            tvHeader = itemView.findViewById(R.id.txt_vin_title);
        }
    }
}
