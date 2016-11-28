package cn.garymb.ygomobile.plus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapterPlus<T> extends BaseAdapter implements SpinnerAdapter {
    protected Context context;
    protected LayoutInflater mLayoutInflater;
    protected final List<T> mItems = new ArrayList<T>();

    public BaseAdapterPlus(Context context) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public Context getContext() {
        return context;
    }

    public void add(T item) {
        if (item != null) {
            mItems.add(item);
        }
    }

    public void clear() {
        mItems.clear();
    }
    public void set(List<T> items) {
        clear();
        addAll(items);
    }
    public void addAll(List<T> items) {
        if (items != null) {
            mItems.addAll(items);
        }
    }

    public boolean exist(T item) {
        if (item == null) return false;
        return mItems.contains(item);
    }

    @Override
    public final int getCount() {
        return mItems.size();
    }

    @Override
    public final T getItem(int position) {
        if (position >= 0 && position < getCount()) {
            return mItems.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = createView(position, parent);
        }
        T t = getItem(position);
        attach(convertView, t, position);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = createView(position, parent);
        }
        T t = getItem(position);
        attach(convertView, t, position);
        return convertView;
    }

    protected abstract View createView(int position, ViewGroup parent);

    protected abstract void attach(View view, T item,int position);

}
