package tech.nilu.wallet.ui.wallets.contracts;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.nilu.wallet.R;
import tech.nilu.wallet.db.entity.ContractInfo;
import tech.nilu.wallet.util.BitmapUtils;

/**
 * Created by root on 1/9/18.
 */

public class ContractsAdapter extends RecyclerView.Adapter {
    private int holderIconSize;
    private Resources resources;
    private SparseBooleanArray states;
    private List<ContractInfo> contracts;

    public ContractsAdapter(Context context) {
        this.contracts = new ArrayList<>();
        this.states = new SparseBooleanArray();
        this.resources = context.getResources();
        this.holderIconSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, resources.getDisplayMetrics());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder)
            ((ViewHolder) holder).bind(position);
    }

    @Override
    public int getItemCount() {
        return contracts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void add(int pos, ContractInfo item) {
        states.put(pos, false);
        contracts.add(pos, item);
        notifyItemInserted(pos);
    }

    public void setSelected(int pos, boolean selected) {
        states.put(pos, selected);
        notifyItemChanged(pos);
    }

    public int indexOf(ContractInfo obj) {
        return contracts.indexOf(obj);
    }

    public List<ContractInfo> getSelectedItems() {
        List<ContractInfo> result = new ArrayList<>();
        for (int i = 0; i < states.size(); i++) {
            int key = states.keyAt(i);
            if (states.get(key))
                result.add(contracts.get(i));
        }
        return result;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(android.R.id.icon)
        ImageView icon;
        @BindView(android.R.id.text1)
        TextView nameText;
        @BindView(android.R.id.text2)
        TextView addressText;
        @BindView(android.R.id.checkbox)
        SwitchCompat selectSwitch;

        ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contract, parent, false));

            ButterKnife.bind(this, itemView);
        }

        void bind(int position) {
            ContractInfo item = contracts.get(position);
            Drawable placeholder = new BitmapDrawable(resources, BitmapUtils.getAvatar(holderIconSize, String.valueOf(item.getName().charAt(0)).toUpperCase()));
            Glide.with(itemView.getContext())
                    .asBitmap()
                    .load(item.getImage())
                    .apply(new RequestOptions()
                            .circleCrop()
                            .error(placeholder)
                            .placeholder(placeholder)
                    )
                    .into(icon);
            selectSwitch.setChecked(states.get(position));
            nameText.setText(item.getName());
            addressText.setText(item.getAddress());
            selectSwitch.setOnCheckedChangeListener((button, checked) -> states.put(position, checked));
        }
    }
}
