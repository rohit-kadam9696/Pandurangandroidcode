package com.twd.chitboyapp.spsskl.adapter;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.R;
import com.twd.chitboyapp.spsskl.interfaces.UpdateRemain;
import com.twd.chitboyapp.spsskl.pojo.FertProduct;
import com.twd.chitboyapp.spsskl.vh.FertProductHolder;
import com.twd.svalidation.ServerValidation;

import java.text.DecimalFormat;
import java.util.List;

public class FertProductAdapter extends RecyclerView.Adapter<FertProductHolder> {

    private final Activity activity;
    private static UpdateRemain updateRemain;
    public final List<FertProduct> fertProducts;
    DecimalFormat df = new DecimalFormat("#0.00");


    public FertProductAdapter(List<FertProduct> fertProducts, Activity context, UpdateRemain updateRemain) {
        this.fertProducts = fertProducts;
        this.activity = context;
        FertProductAdapter.updateRemain = updateRemain;
    }

    @NonNull
    @Override
    public FertProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fert_product, parent, false);
        return new FertProductHolder(v);
    }

    @Override
    public void onBindViewHolder(FertProductHolder holder, final int position) {
        holder.setIsRecyclable(false);
        FertProduct fertProduct = fertProducts.get(holder.getBindingAdapterPosition());
        holder.txtsubstoreid.setText(fertProduct.getSubstoreId());
        holder.txtproductid.setText(fertProduct.getItemId());
        holder.txtproductname.setText(fertProduct.getItemId() + " - " + fertProduct.getItemName() + " - " + fertProduct.getSubstoreName());
        holder.txtstock.setText(String.format(activity.getResources().getString(R.string.stockfert), fertProduct.getItemStock()));
        holder.edtquantity.setText(fertProduct.getQuantity());

        holder.txtunitid.setText(fertProduct.getUnitId());
        holder.txtunit.setText(fertProduct.getUnitName());
        holder.txthsnid.setText(fertProduct.getNhsnId());
        holder.txtrateval.setText(fertProduct.getItemRate());
        holder.txttotal.setText(fertProduct.getTotal());
        holder.txttaxval.setText(fertProduct.getTaxPer());
        holder.txttotaltax.setText(fertProduct.getTotalTax());

        holder.edtquantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String quantity = holder.edtquantity.getText().toString();
                ServerValidation sv = new ServerValidation();
                if (!quantity.equals("") && sv.checkFloat(quantity)) {
                    double dqty = Double.parseDouble(quantity);
                    String stock = fertProduct.getItemStock();
                    if (stock!=null && sv.checkFloat(stock)) {
                        double dstock = Double.parseDouble(stock);
                        if (dstock > 0 && dqty <= dstock) {
                            String rate = holder.txtrateval.getText().toString();
                            String taxrate = holder.txttaxval.getText().toString();
                            String total = df.format(dqty * Double.parseDouble(rate));
                            double taxamt = dqty * Double.parseDouble(rate) * Double.parseDouble(taxrate);
                            if (taxamt % 2 != 0) {
                                taxamt += 1;
                            }
                            String totaltax = df.format(taxamt / 100);
                            fertProduct.setQuantity(quantity);
                            fertProduct.setTotal(total);
                            fertProduct.setTotalTax(totaltax);
                            holder.txttotal.setText(fertProduct.getTotal());
                            holder.txttotaltax.setText(fertProduct.getTotalTax());
                            fertProducts.set(holder.getBindingAdapterPosition(), fertProduct);
                        } else {
                            holder.edtquantity.setError(activity.getResources().getString(R.string.errorstocknotavailable));
                            fertProduct.setQuantity("");
                            fertProduct.setTotal("");
                            fertProduct.setTotalTax("");
                            holder.txttotal.setText("");
                            holder.txttotaltax.setText("");
                            holder.edtquantity.setText("");

                        }
                    } else {
                        holder.edtquantity.setError(activity.getResources().getString(R.string.errorstocknotavailable));
                    }

                } else {
                    fertProduct.setQuantity("");
                    fertProduct.setTotal("");
                    fertProduct.setTotalTax("");
                    holder.txttotal.setText("");
                    holder.txttotaltax.setText("");
                }
                if (updateRemain != null) updateRemain.updateRemain();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.edtquantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                try {
                    notifyItemChanged(holder.getBindingAdapterPosition());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return fertProducts.size();
    }

}