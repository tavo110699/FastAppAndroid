package lunainc.com.mx.fastdelivery.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Connect.ApiUtils;
import lunainc.com.mx.fastdelivery.Model.Cesta;
import lunainc.com.mx.fastdelivery.Model.Product;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FastApp.db";
    private static final String CARRITO_TABLE_NAME = "cesta";
    private static final String CARRITO_COLUMN_UID = "id";
    private static final String CARRITO_COLUMN_UID_PRODUCT = "id_product";
    private static final String CARRITO_COLUMN_STATIC_PRICE_PRODUCT = "static_price_product";
    private static final String CARRITO_COLUMN_PRODUCT_NAME = "product_name";
    private static final String CARRITO_COLUMN_PRODUCT_IMAGE = "product_image";
    private static final String CARRITO_COLUMN_UID_PARTNER = "id_partner";
    private static final String CARRITO_COLUMN_NAME_PARTNER = "name_partner";
    private static final String CARRITO_COLUMN_TYPE_PRODUCT = "type_product";
    private static final String CARRITO_COLUMN_DESC = "descripcion";
    private static final String CARRITO_COLUMN_CANTIDAD = "cantidad";
    private static final String CARRITO_COLUMN_EXTRA = "extra";
    private APIService apiService;
    private String token = "";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
        apiService = ApiUtils.getAPIService();
        token = new Constants().getToken(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "CREATE TABLE " + CARRITO_TABLE_NAME +
                        "(id integer primary key autoincrement, id_product text, product_name text, product_image text, id_partner text, name_partner text,type_product text, descripcion text, cantidad text, extra text, static_price_product text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS cesta");
        onCreate(db);
    }


    public boolean insertProduct(String id_product, String product_name, String product_image, String static_price_product ,String id_partner, String name_partner ,String type_product, String descripcion, String cantidad, String extra){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_product", id_product);
        contentValues.put("product_name", product_name);
        contentValues.put("product_image", product_image);
        contentValues.put("static_price_product", static_price_product);
        contentValues.put("id_partner", id_partner);
        contentValues.put("name_partner", name_partner);
        contentValues.put("type_product", type_product);
        contentValues.put("descripcion", descripcion);
        contentValues.put("cantidad", cantidad);
        contentValues.put("extra", extra);

        long rowInserted = db.insert(CARRITO_TABLE_NAME, null, contentValues);
        return rowInserted != -1;
    }

    public Integer updateProduct(String attr, String value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(attr, value);
        return db.update(CARRITO_TABLE_NAME, contentValues, null, null);
    }



    public Cursor getProduct(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM cesta WHERE id="+id+"", null);
    }


    public int numberRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, CARRITO_TABLE_NAME);
    }

    public Integer deleteProduct(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CARRITO_TABLE_NAME, "id = ?", new String[]{id});
    }


    public void deleteAllProducts(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CARRITO_TABLE_NAME, null, null);
    }


    public Integer getNumOrder(){
        SQLiteDatabase db = this.getReadableDatabase();
       return db.rawQuery("SELECT DISTINCT(id_partner) FROM "+CARRITO_TABLE_NAME, null).getColumnCount();
    }

    public ArrayList<String> getPartnerID(){
        ArrayList<String> partnersID = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        @SuppressLint("Recycle")
        Cursor res = db.rawQuery("SELECT DISTINCT(id_partner) FROM "+CARRITO_TABLE_NAME, null);
        res.moveToFirst();

        while (!res.isAfterLast()){
            partnersID.add(res.getString(res.getColumnIndex(CARRITO_COLUMN_UID_PARTNER)));
            res.moveToNext();
        }


        return partnersID;
    }

    public ArrayList<String> getPartnerName(){
        ArrayList<String> partnersID = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        @SuppressLint("Recycle")
        Cursor res = db.rawQuery("SELECT DISTINCT(name_partner) FROM "+CARRITO_TABLE_NAME, null);
        res.moveToFirst();

        while (!res.isAfterLast()){
            partnersID.add(res.getString(res.getColumnIndex(CARRITO_COLUMN_NAME_PARTNER)));
            res.moveToNext();
        }


        return partnersID;
    }


    public ArrayList<Cesta> getProductsByPartnerID(String partner_id){

        ArrayList<Cesta> cestas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        @SuppressLint("Recycle")
        Cursor res = db.rawQuery("SELECT * FROM "+CARRITO_TABLE_NAME+" WHERE "+CARRITO_COLUMN_UID_PARTNER+ " = ?", new String[]{partner_id});
        res.moveToFirst();


        while (!res.isAfterLast()){
            Cesta cesta = new Cesta();
            cesta.setId(res.getInt(res.getColumnIndex(CARRITO_COLUMN_UID)));
            cesta.setId_product(res.getString(res.getColumnIndex(CARRITO_COLUMN_UID_PRODUCT)));
            cesta.setProduct_name(res.getString(res.getColumnIndex(CARRITO_COLUMN_PRODUCT_NAME)));
            cesta.setProduct_image(res.getString(res.getColumnIndex(CARRITO_COLUMN_PRODUCT_IMAGE)));
            cesta.setStatic_price_product(res.getString(res.getColumnIndex(CARRITO_COLUMN_STATIC_PRICE_PRODUCT)));
            cesta.setId_partner(res.getString(res.getColumnIndex(CARRITO_COLUMN_UID_PARTNER)));
            cesta.setName_partner(res.getString(res.getColumnIndex(CARRITO_COLUMN_NAME_PARTNER)));
            cesta.setType_product(res.getString(res.getColumnIndex(CARRITO_COLUMN_TYPE_PRODUCT)));
            cesta.setDescripcion(res.getString(res.getColumnIndex(CARRITO_COLUMN_DESC)));
            cesta.setCantidad(res.getString(res.getColumnIndex(CARRITO_COLUMN_CANTIDAD)));
            cesta.setExtra(res.getString(res.getColumnIndex(CARRITO_COLUMN_EXTRA)));
            cestas.add(cesta);
            res.moveToNext();
        }

        return cestas;


    }

    public ArrayList<Cesta> getProducts(){
        ArrayList<Cesta> cestas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        @SuppressLint("Recycle")
        Cursor res = db.rawQuery("SELECT * FROM "+CARRITO_TABLE_NAME, null);
        res.moveToFirst();


        while (!res.isAfterLast()){
            Cesta cesta = new Cesta();
            cesta.setId(res.getInt(res.getColumnIndex(CARRITO_COLUMN_UID)));
            cesta.setId_product(res.getString(res.getColumnIndex(CARRITO_COLUMN_UID_PRODUCT)));
            cesta.setProduct_name(res.getString(res.getColumnIndex(CARRITO_COLUMN_PRODUCT_NAME)));
            cesta.setProduct_image(res.getString(res.getColumnIndex(CARRITO_COLUMN_PRODUCT_IMAGE)));
            cesta.setStatic_price_product(res.getString(res.getColumnIndex(CARRITO_COLUMN_STATIC_PRICE_PRODUCT)));
            cesta.setId_partner(res.getString(res.getColumnIndex(CARRITO_COLUMN_UID_PARTNER)));
            cesta.setName_partner(res.getString(res.getColumnIndex(CARRITO_COLUMN_NAME_PARTNER)));
            cesta.setType_product(res.getString(res.getColumnIndex(CARRITO_COLUMN_TYPE_PRODUCT)));
            cesta.setDescripcion(res.getString(res.getColumnIndex(CARRITO_COLUMN_DESC)));
            cesta.setCantidad(res.getString(res.getColumnIndex(CARRITO_COLUMN_CANTIDAD)));
            cesta.setExtra(res.getString(res.getColumnIndex(CARRITO_COLUMN_EXTRA)));
            cestas.add(cesta);
            res.moveToNext();
        }

        return cestas;
    }




}
