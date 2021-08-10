package com.giko.cryptotracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> {

    private ArrayList<CurrencyModel> currencyModelArrayList;
    private Context context;
    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public CurrencyAdapter(ArrayList<CurrencyModel> currencyModelArrayList, Context context) {
        this.currencyModelArrayList = currencyModelArrayList;
        this.context = context;
    }

    public void filterList(ArrayList<CurrencyModel> filteredList){
        currencyModelArrayList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CurrencyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.currency_rv_item, parent, false);
        return new CurrencyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyAdapter.ViewHolder holder, int position) {
        CurrencyModel currencyModel = currencyModelArrayList.get(position);
        holder.tvCurrencyName.setText(currencyModel.getName());
        holder.tvSymbol.setText(currencyModel.getSymbol());
        holder.tvRate1.setText("$ " + decimalFormat.format(currencyModel.getPrice()));
    }

    @Override
    public int getItemCount() {
        return currencyModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvCurrencyName, tvSymbol, tvRate1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCurrencyName = itemView.findViewById(R.id.tvCurrencyName);
            tvSymbol = itemView.findViewById(R.id.tvSymbol);
            tvRate1 = itemView.findViewById(R.id.tvCurrencyRate1);
        }
    }
}
