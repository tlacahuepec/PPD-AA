package mx.tlacahuepec.ppd_aa.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.tlacahuepec.ppd_aa.BR;

/**
 * Created by santi on 1/11/2018.
 */

public abstract class DataBindingBaseAdapter<T>
        extends RecyclerView.Adapter<DataBindingBaseAdapter<T>.DataBindingViewHolder> {

    private final OnItemClickListener<T> itemClickListener;

    DataBindingBaseAdapter(OnItemClickListener<T> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public DataBindingBaseAdapter<T>.DataBindingViewHolder onCreateViewHolder(ViewGroup parent,
                                                                              int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(
                layoutInflater, viewType, parent, false);
        return new DataBindingBaseAdapter<T>.DataBindingViewHolder(binding);
    }

    public void onBindViewHolder(DataBindingBaseAdapter<T>.DataBindingViewHolder holder,
                                 int position) {
        final T item = getItemForPosition(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(item);
            }
        });
        holder.bind(item);
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    protected abstract T getItemForPosition(int position);


    protected abstract int getLayoutIdForPosition(int position);

    public interface OnItemClickListener<T> {
        void onItemClick(T item);
    }

    public class DataBindingViewHolder extends RecyclerView.ViewHolder {

        private final ViewDataBinding binding;

        public DataBindingViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(T item) {
            binding.setVariable(BR.itemViewModel, item);
            binding.executePendingBindings();
        }
    }

}
