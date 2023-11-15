package kz.agrobirzha.app.Classes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import kz.agrobirzha.app.Helpers.SQLiteHandler;
import kz.agrobirzha.app.R;

import static java.sql.Types.NULL;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {


    private Context mCtx;
    private SQLiteHandler db;
    private List<Product> productList;
    private static final String URL_PRODUCTS = "http://agrobirzha.cf/buyitem.php";
    public ProductsAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.product_list, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        final Product product = productList.get(position);

        //loading the image
        Glide.with(mCtx)
                .load(product.getImage())
                .into(holder.imageView);

        holder.textViewTitle.setText(product.getTitle());
        holder.textViewShortDesc.setText(product.getShortdesc());
        holder.textViewPrice.setText(String.valueOf(product.getPrice()) + product.getCurr());
        holder.addtoFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    addtoFavorite(product.getId(), product.getUnique_id(), product.getTitle(), product.getShortdesc(), product.getPrice(), product.getImage());
                }else {
                    removefromFavorite(product.getUnique_id());
                }
            }
        });
        holder.buybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = new SQLiteHandler(mCtx);
                if(db.isItemOnCart(product.getUnique_id())){
                    db.updateNumber(product.getUnique_id(), getNumber(product.getUnique_id())+1);
                }else{
                    buyProduct(product.getUnique_id(), product.getTitle(), 1, product.getPrice(), product.getImage());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewShortDesc, textViewPrice;
        ImageView imageView;
        Button buybtn;
        CheckBox addtoFav;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            imageView = itemView.findViewById(R.id.imageView);
            buybtn = itemView.findViewById(R.id.buybutton);
            addtoFav = itemView.findViewById(R.id.addtoFav);
        }
    }
    private void addtoFavorite(int itemID, String unique_id, String title, String shortdesc, int price, String image) {
        db = new SQLiteHandler(mCtx);
        db.addtoFav(itemID, unique_id, title, shortdesc, price, image);
        Toast.makeText(mCtx, "Вы успешно добавили товар в список избранных", Toast.LENGTH_SHORT).show();

    }
    private void removefromFavorite(String unique_id) {
        db = new SQLiteHandler(mCtx);
        db.deleteFavItem(unique_id);
        Toast.makeText(mCtx, "Вы успешно удалили товар из списка избранных", Toast.LENGTH_SHORT).show();

    }
    private void buyProduct(String unique_id, String title, int number, int price, String image) {
        db = new SQLiteHandler(mCtx);
        db.addtoCart(unique_id, title, number, price, image);
        Toast.makeText(mCtx, "Вы успешно добавили товар в корзину", Toast.LENGTH_SHORT).show();

    }
    private int getNumber(String unique_id){
        db = new SQLiteHandler(mCtx);
        return db.getNumber(unique_id);
    }
}