package co.stutzen.shopzen.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import co.stutzen.shopzen.bo.ProductBO;

public class DBController  extends SQLiteOpenHelper {
  
	private static final String LOGCAT = null;
 
    public DBController(Context applicationcontext) {
      super(applicationcontext, "androidsqlite.db", null, 7);
    }
 
	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    String query = "CREATE TABLE IF NOT EXISTS cart (id INTEGER PRIMARY KEY, prodid INT, itemName TEXT, price INT, count INT, cart INT, imgurl TEXT, variationId INT, type TEXT, size TEXT, color TEXT)";
	    database.execSQL(query);
		String query1 = "CREATE TABLE IF NOT EXISTS favorites (id INTEGER PRIMARY KEY, prodid INT, itemName TEXT, price INT, imgurl TEXT)";
		database.execSQL(query1);
		String query2 = "CREATE TABLE IF NOT EXISTS customers (id INTEGER PRIMARY KEY, custid INT, custname TEXT, detail TEXT)";
		database.execSQL(query2);
	    Log.d(LOGCAT, "database created");
	  }
	  
	  @Override
	  public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
		 
		  String query = "DROP TABLE IF EXISTS cart";
		  database.execSQL(query);
		  String query1 = "DROP TABLE IF EXISTS favorites";
		  database.execSQL(query);
		  String query2 = "DROP TABLE IF EXISTS customers";
		  database.execSQL(query);
		  onCreate(database);
	  }

	public void delMyFavorite(int productId) {
		Log.i("delete cart", " started");
		SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
		database.delete("favorites", "prodid=" + productId, null);
		DatabaseManager.getInstance().closeDatabase();
		Log.i("delete cart", " ended");
	}

	public void insertFavorite(int id, String itemName, String price, String url) {
		SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
		ContentValues values = new ContentValues();
		values.put("prodid", id);
		values.put("itemName", itemName);
		values.put("price", price);
		values.put("imgurl", url);
		database.insert("favorites", null, values);
		DatabaseManager.getInstance().closeDatabase();
	}

	public void insertCustomer(int id, String name, String detail) {
		SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
		ContentValues values = new ContentValues();
		values.put("custid", id);
		values.put("custname", name);
		values.put("detail", detail);
		database.insert("customers", null, values);
		DatabaseManager.getInstance().closeDatabase();
	}

	public ArrayList<ProductBO> getFavoritesList() {
		String selectQuery = "SELECT itemName, price, prodid, imgurl FROM favorites";
		ArrayList<ProductBO> val = new ArrayList<ProductBO>();
		SQLiteDatabase db = DatabaseManager.getInstance().openReadDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				ProductBO bo = new ProductBO();
				bo.setName(c.getString(0));
				bo.setAmount(Double.parseDouble(c.getString(1)));
				bo.setId(c.getInt(2));
				bo.setImage(c.getString(3));
				val.add(bo);
			} while (c.moveToNext());
		}
		c.close();
		DatabaseManager.getInstance().closeDatabase();
		return val;
	}
	  
	  public boolean checkCartItem(int productId) {
		  SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
		  Cursor mCount= database.rawQuery("select count(*) from cart where prodid=" + productId, null);
		  mCount.moveToFirst();
		  int count= mCount.getInt(0);
		  mCount.close();
		  DatabaseManager.getInstance().closeDatabase();
		  if(count <= 0)
			  return false;
		  else
			  return true;
	  }

	  public int updateCart(int count, int productId) {
		    if(count == 0){
		    	delCart(productId);
		    	return 0;
		    }else{
		    SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
		    ContentValues values = new ContentValues();
		    values.put("count", count);
		    Log.i("update database ", " ended");
		    int resp = database.update("cart", values, "prodid="+productId, null);
		    DatabaseManager.getInstance().closeDatabase();
		    return resp;
		    }
	  }

	public int getcartId(int productId)
	{
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.query("cart", null, " prodid=?", new String[]{String.valueOf(productId)}, null, null, null);
		if(cursor.getCount()==0)
		{
			cursor.close();
			return 0;
		}
		cursor.moveToFirst();
		int id1= cursor.getInt(cursor.getColumnIndex("prodid"));
		cursor.close();
		return id1;
	}
	  
	  public void delCart(int productId) {
	        Log.i("delete cart", " started");
	        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
	        database.delete("cart", "prodid=" + productId, null);
	        DatabaseManager.getInstance().closeDatabase();
	        Log.i("delete cart", " ended");
	    }
	 
	  public void insertCart(int id, String itemName, String price, int count, String url, int variationId, String type, String size, String color) {
		  SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
		  ContentValues values = new ContentValues();
		  values.put("prodid", id);
		  values.put("itemName", itemName);
		  values.put("price", price);
		  values.put("count", count);
		  values.put("imgurl", url);
		  values.put("variationId", variationId);
		  values.put("type", type);
		  values.put("size", size);
		  values.put("color", color);
		  database.insert("cart", null, values);
		  DatabaseManager.getInstance().closeDatabase();
	  }

	  public ArrayList<ProductBO> getCartList() {
		    String selectQuery = "SELECT itemName, price, count, prodid, imgurl, variationId, type, size, color FROM cart";
		    ArrayList<ProductBO> val = new ArrayList<ProductBO>();
		    SQLiteDatabase db = DatabaseManager.getInstance().openReadDatabase();
			Cursor c = db.rawQuery(selectQuery, null);
			if (c.moveToFirst()) {
				do {
					ProductBO bo = new ProductBO();
					bo.setName(c.getString(0));
					bo.setAmount(Double.parseDouble(c.getString(1)));
					bo.setQuantity(c.getInt(2));
					bo.setId(c.getInt(3));
					bo.setImage(c.getString(4));
					bo.setVariationId(c.getInt(5));
					bo.setType(c.getString(6));
					bo.setSize(c.getString(7));
					bo.setColor(c.getString(8));
					val.add(bo);
				} while (c.moveToNext());
			}
		    c.close();
		    DatabaseManager.getInstance().closeDatabase();
		    return val;
	  }

	public String getCustomerName() {
		String selectQuery = "SELECT custname FROM customers";
		String name = "";
		SQLiteDatabase db = DatabaseManager.getInstance().openReadDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				name = c.getString(0);
			} while (c.moveToNext());
		}
		c.close();
		DatabaseManager.getInstance().closeDatabase();
		return name;
	}

	public String getCustomerDetail() {
		String selectQuery = "SELECT detail FROM customers";
		String detail = "";
		SQLiteDatabase db = DatabaseManager.getInstance().openReadDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				detail = c.getString(0);
			} while (c.moveToNext());
		}
		c.close();
		DatabaseManager.getInstance().closeDatabase();
		return detail;
	}

	public int getCustomerId() {
		String selectQuery = "SELECT custid FROM customers";
		int id = 0;
		SQLiteDatabase db = DatabaseManager.getInstance().openReadDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				id = c.getInt(0);
			} while (c.moveToNext());
		}
		c.close();
		DatabaseManager.getInstance().closeDatabase();
		return id;
	}
	  
	  public double getCartTotal() {
		    String selectQuery = "SELECT price, count FROM cart";
		    SQLiteDatabase db = DatabaseManager.getInstance().openReadDatabase();
		    double total = 0;
			Cursor c = db.rawQuery(selectQuery, null);
			if (c.moveToFirst()) {
				do {
					total += (Double.parseDouble(c.getString(0)) * c.getInt(1));
				} while (c.moveToNext());
			}
		    c.close();
		    DatabaseManager.getInstance().closeDatabase();
		    return total;
	  }
	  
	  public int getOverAll() {
		    String selectQuery = "SELECT count FROM cart";
		    SQLiteDatabase db = DatabaseManager.getInstance().openReadDatabase();
		    int total = 0;
			Cursor c = db.rawQuery(selectQuery, null);
			if (c.moveToFirst()) {
				do {
					total += c.getInt(0);
				} while (c.moveToNext());
			}
		    c.close();
		    DatabaseManager.getInstance().closeDatabase();
		    return total;
	  }
	  
	  public void dropCart() {
		   SQLiteDatabase database =  DatabaseManager.getInstance().openDatabase();
		    String query = "DROP TABLE IF EXISTS cart";
		    database.execSQL(query);
		    String query1 = "CREATE TABLE IF NOT EXISTS cart (id INTEGER PRIMARY KEY, prodid INT, itemName TEXT, price INT, count INT, cart INT, imgurl TEXT, variationId INT, type TEXT, size TEXT, color TEXT)";
		    database.execSQL(query1);
		    DatabaseManager.getInstance().closeDatabase();
	  }

	public void dropCustomer() {
		SQLiteDatabase database =  DatabaseManager.getInstance().openDatabase();
		String query = "DROP TABLE IF EXISTS customers";
		database.execSQL(query);
		String query1 = "CREATE TABLE IF NOT EXISTS customers (id INTEGER PRIMARY KEY, custid INT, custname TEXT, detail TEXT)";
		database.execSQL(query1);
		DatabaseManager.getInstance().closeDatabase();
	}


	public void dropFavorites() {
		SQLiteDatabase database =  DatabaseManager.getInstance().openDatabase();
		String query = "DROP TABLE IF EXISTS favorites";
		database.execSQL(query);
		String query1 = "CREATE TABLE IF NOT EXISTS favorites (id INTEGER PRIMARY KEY, prodid INT, itemName TEXT, price INT, imgurl TEXT)";
		database.execSQL(query1);
		DatabaseManager.getInstance().closeDatabase();
	}

	public int getCartCount() {
		  SQLiteDatabase database = DatabaseManager.getInstance().openReadDatabase();
		  Cursor mCount= database.rawQuery("select count(*) from cart", null);
		  mCount.moveToFirst();
		  int count= mCount.getInt(0);
		  mCount.close();
		  DatabaseManager.getInstance().closeDatabase();
		  return count;
	  }

	public int getCustomerCount() {
		SQLiteDatabase database = DatabaseManager.getInstance().openReadDatabase();
		Cursor mCount= database.rawQuery("select count(*) from customers", null);
		mCount.moveToFirst();
		int count= mCount.getInt(0);
		mCount.close();
		DatabaseManager.getInstance().closeDatabase();
		return count;
	}

}