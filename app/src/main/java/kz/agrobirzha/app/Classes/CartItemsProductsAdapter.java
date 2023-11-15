package kz.agrobirzha.app.Classes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import kz.agrobirzha.app.Helpers.SQLiteHandler;
import kz.agrobirzha.app.R;


public class CartItemsProductsAdapter extends RecyclerView.Adapter<CartItemsProductsAdapter.ProductViewHolder> {


    private Context mCtx;
    private List<CartItemsProduct> productList;
    SQLiteHandler db;
    public CartItemsProductsAdapter(Context mCtx, List<CartItemsProduct> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cartitems_product_list, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {
        final CartItemsProduct product = productList.get(position);

        //loading the image
        Glide.with(mCtx)
                .load(product.getImage())
                .into(holder.imageView);

        holder.textViewTitle.setText(product.getTitle());
        holder.textViewPrice.setText(String.valueOf(product.getPrice()) + product.getCurr());
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = new SQLiteHandler(mCtx);
                db.deleteCartItem(product.getUnique_id());
                CartItemsProduct itemLabel = productList.get(position);

                productList.remove(position);

                notifyItemRemoved(position);

                notifyItemRangeChanged(position,productList.size());
                notifyDataSetChanged();
                Toast.makeText(mCtx,"Вы успешно удалили товар из корзины!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewPrice, summdetail;
        ImageView imageView;
        TextView numberOfItemts;
        ImageButton remove;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            imageView = itemView.findViewById(R.id.imageView);
            numberOfItemts = itemView.findViewById(R.id.numberofitems);
            remove = itemView.findViewById(R.id.removeButton);
            summdetail = itemView.findViewById(R.id.summdetail);
        }
    }
}