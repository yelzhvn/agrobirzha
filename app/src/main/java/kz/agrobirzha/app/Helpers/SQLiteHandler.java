/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package kz.agrobirzha.app.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kz.agrobirzha.app.Classes.CartItemsProduct;
import kz.agrobirzha.app.Classes.FavItemsProduct;
import kz.agrobirzha.app.Classes.Product;

public class SQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = SQLiteHandler.class.getSimpleName();

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 2;

	// Database Name
	private static final String DATABASE_NAME = "agrobirzha";

	// Login table name
	private static final String TABLE_USER = "user";
	private static final String TABLE_FAVPRODUCTS = "favproducts";
	private static final String TABLE_CARTPRODUCTS = "cartproducts";

	// Login Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_UID = "uid";
	private static final String KEY_CREATED_AT = "created_at";

	// Favorite products Columns names

	private static final String KEY_FAV_ID = "fav_id";
	private static final String KEY_FAV_ITEM_ID = "fav_item_id";
	private static final String KEY_FAV_UNIQUE_ID = "fav_unique_id";
	private static final String KEY_FAV_TITLE = "fav_title";
	private static final String KEY_FAV_SHORTDESC = "fav_shortdesc";
	private static final String KEY_FAV_PRICE = "fav_price";
	private static final String KEY_FAV_IMAGE = "fav_image";

	// Cart products Columns names

	private static final String KEY_CART_ID = "cart_id";
	private static final String KEY_CART_UNIQUE_ID = "cart_unique_id";
	private static final String KEY_CART_TITLE = "cart_title";
	private static final String KEY_CART_NUMBER = "cart_number";
	private static final String KEY_CART_PRICE = "cart_price";
	private static final String KEY_CART_IMAGE = "cart_image";

	public SQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
				+ KEY_CREATED_AT + " TEXT" + ")";
		String CREATE_FAVORITE_TABLE = "CREATE TABLE " + TABLE_FAVPRODUCTS + "("
				+ KEY_FAV_ID + " INTEGER PRIMARY KEY," + KEY_FAV_ITEM_ID + " INTEGER," + KEY_FAV_UNIQUE_ID + " TEXT,"
				+ KEY_FAV_TITLE + " TEXT," + KEY_FAV_SHORTDESC + " TEXT," + KEY_FAV_PRICE + " TEXT,"
				+ KEY_FAV_IMAGE + ")";
		String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CARTPRODUCTS + "("
				+ KEY_CART_ID + " INTEGER PRIMARY KEY," + KEY_CART_UNIQUE_ID + " TEXT,"
				+ KEY_CART_TITLE + " TEXT," + KEY_CART_NUMBER + " INTEGER DEFAULT NULL," + KEY_CART_PRICE + " TEXT,"
				+ KEY_CART_IMAGE + ")";
		db.execSQL(CREATE_LOGIN_TABLE);
		db.execSQL(CREATE_FAVORITE_TABLE);
		db.execSQL(CREATE_CART_TABLE);

		Log.d(TAG, "Database tables created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVPRODUCTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARTPRODUCTS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
	public void addtoFav(int itemID, String unique_id, String title, String shortdesc, int price, String image) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_FAV_ITEM_ID, itemID);
		values.put(KEY_FAV_UNIQUE_ID, unique_id);
		values.put(KEY_FAV_TITLE, title); // Title
		values.put(KEY_FAV_SHORTDESC, shortdesc); // ShortDesc
		values.put(KEY_FAV_PRICE, price); // Price
		values.put(KEY_FAV_IMAGE, image); // Image

		// Inserting Row
		long id = db.insert(TABLE_FAVPRODUCTS, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New user favorite product into sqlite: " + id);
	}
	/**
	 * Storing user details in database
	 * */
	public void addtoCart(String unique_id, String title, int number, int price, String image) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_CART_UNIQUE_ID, unique_id);
		values.put(KEY_CART_TITLE, title); // Title
		values.put(KEY_CART_NUMBER, number); // ShortDesc
		values.put(KEY_CART_PRICE, price); // Price
		values.put(KEY_CART_IMAGE, image); // Image

		// Inserting Row
		long id = db.insert(TABLE_CARTPRODUCTS, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New user cart product into sqlite: " + id);
	}
	public List<CartItemsProduct> allCartProducts() {
		List<CartItemsProduct> cartItemsProducts = new ArrayList<>();
		String selectQuery = "SELECT  * FROM " + TABLE_CARTPRODUCTS;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				CartItemsProduct product = new CartItemsProduct();
				product.setId(cursor.getInt(0));
				product.setUnique_id(cursor.getString(1));
				product.setTitle(cursor.getString(2));
				product.setNumber(cursor.getInt(3));
				product.setPrice(cursor.getInt(4));
				product.setImage(cursor.getString(5));
				cartItemsProducts.add(product);
			} while (cursor.moveToNext());
		}

		db.close();
		return cartItemsProducts;
	}
	public boolean isItemOnCart(String unique_id) {
		String Query = "SELECT * FROM " + TABLE_CARTPRODUCTS + " WHERE " + KEY_CART_UNIQUE_ID + "=\"" + unique_id + "\"";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(Query, null);
		if(cursor.getCount() <= 0){
			cursor.close();
			return false;
		}
		cursor.close();
		return true;
	}
	public int getNumber(String unique_id) {
		int ty;
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_CARTPRODUCTS + " WHERE " + KEY_CART_UNIQUE_ID + "=\"" + unique_id + "\"", null);

		if (c.moveToFirst()) {
			ty = c.getInt(c.getColumnIndex("cart_number"));
		} else {
			ty = 0;
		}

		c.close();
		db.close();

		return ty;
	}
	public int updateNumber(String unique_id, int number) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_CART_NUMBER, number);

		// updating row
		return db.update(TABLE_CARTPRODUCTS, values, KEY_CART_UNIQUE_ID + " = ?",
				new String[] { String.valueOf(unique_id) });
	}

	public void addUser(String name, String email, String uid, String created_at) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name); // Name
		values.put(KEY_EMAIL, email); // Email
		values.put(KEY_UID, uid); // Email
		values.put(KEY_CREATED_AT, created_at); // Created At

		// Inserting Row
		long id = db.insert(TABLE_USER, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id);
	}
	public List<FavItemsProduct> allFavProducts() {
		List<FavItemsProduct> students = new ArrayList<>();
		String selectQuery = "SELECT  * FROM " + TABLE_FAVPRODUCTS;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				FavItemsProduct product = new FavItemsProduct();
				product.setId(cursor.getInt(1));
				product.setUnique_id(cursor.getString(2));
				product.setTitle(cursor.getString(3));
				product.setShortdesc(cursor.getString(4));
				product.setPrice(cursor.getInt(5));
				product.setImage(cursor.getString(6));
				students.add(product);
			} while (cursor.moveToNext());
		}

		db.close();
		return students;
	}

	/**
	 * Getting user data from database
	 * */
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_USER;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			user.put("name", cursor.getString(1));
			user.put("email", cursor.getString(2));
			user.put("uid", cursor.getString(3));
			user.put("created_at", cursor.getString(4));
		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

		return user;
	}

	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void deleteUsers() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_USER, null, null);
		db.close();

		Log.d(TAG, "Deleted all user info from sqlite");
	}
	/*
	 * Deleting a todo
	 */
	public void deleteFavItem(String favitemid) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_FAVPRODUCTS, KEY_FAV_UNIQUE_ID + " = ?",
				new String[] { String.valueOf(favitemid) });
		db.close();
		Log.d(TAG, "Deleted favorite product info from sqlite");
	}
	public void deleteCartItem(String favitemid) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CARTPRODUCTS, KEY_CART_UNIQUE_ID + " = ?",
				new String[] { String.valueOf(favitemid) });
		db.close();
		Log.d(TAG, "Deleted favorite product info from sqlite");
	}
	public ArrayList<Cursor> getData(String Query){
		//get writable database
		SQLiteDatabase sqlDB = this.getWritableDatabase();
		String[] columns = new String[] { "message" };
		//an array list of cursor to save two cursors one has results from the query
		//other cursor stores error message if any errors are triggered
		ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
		MatrixCursor Cursor2= new MatrixCursor(columns);
		alc.add(null);
		alc.add(null);

		try{
			String maxQuery = Query ;
			//execute the query results will be save in Cursor c
			Cursor c = sqlDB.rawQuery(maxQuery, null);

			//add value to cursor2
			Cursor2.addRow(new Object[] { "Success" });

			alc.set(1,Cursor2);
			if (null != c && c.getCount() > 0) {

				alc.set(0,c);
				c.moveToFirst();

				return alc ;
			}
			return alc;
		} catch(SQLException sqlEx){
			Log.d("printing exception", sqlEx.getMessage());
			//if any exceptions are triggered save the error message to cursor an return the arraylist
			Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
			alc.set(1,Cursor2);
			return alc;
		} catch(Exception ex){
			Log.d("printing exception", ex.getMessage());

			//if any exceptions are triggered save the error message to cursor an return the arraylist
			Cursor2.addRow(new Object[] { ""+ex.getMessage() });
			alc.set(1,Cursor2);
			return alc;
		}
	}
}
