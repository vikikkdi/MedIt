package com.example.vikikkdi.askit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by vikikkdi on 10/19/17.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 12;
    private static final String DATABASE_NAME = "Login";
    private static final String  TABLE_NAME = "customerloginDetails";
    private static final String TABLE_NAME_TWO = "shopkeeperDetails";
    private static final String USER_NAME = "username";
    private static final String EMAID_ID = "emailid";
    private static final String PHONE_NUMBER = "phonenumber";
    private static final String PASSWORD = "password";
    private static final String LOCATION = "location_log_lat";
    private static final String IVENTORY_TABLE = "inventory";
    private static final String MEDICINE = "medicines";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_CUSTOMER = "CREATE TABLE customerloginDetails( username TEXT PRIMARY KEY, emailid TEXT, phonenumber TEXT, password TEXT, location_log_lat TEXT)";
        String CREATE_TABLE_SHOPKEEPER = "CREATE TABLE shopkeeperDetails( username TEXT PRIMARY KEY, emailid TEXT, phonenumber TEXT, password TEXT, location_log_lat TEXT)";
        String CREATE_TABLE_INVENTORIES = "CREATE TABLE inventory( username TEXT PRIMARY KEY, medicines TEXT)";
        String CREATE_TABLE_ORDERS = "CREATE TABLE orders(customerusername TEXT PRIMARY KEY, shopusername TEXT, medicines TEXT, location TEXT)";
        db.execSQL(CREATE_TABLE_CUSTOMER);
        db.execSQL(CREATE_TABLE_SHOPKEEPER);
        db.execSQL(CREATE_TABLE_INVENTORIES);
        db.execSQL(CREATE_TABLE_ORDERS);
        int[] cost = {7,49,73,58,30,72,44,78,23,9,40,65,92,42,87,3,27,29,40,12,3,69,9,57,60,33,99,78,16,35,97,26,12,67,10,33,79,49,79,21,67,72,93,36,85,45,28,91,94,57};
        String[] medicine = {"Abilify","Nexium","Humira","Crestor","Advair Diskus","Enbrel","Remicade","Cymbalta","Copaxone","Neulasta","Lantus Solostar","Rituxan","Spiriva Handihaler","Januvia","Atripla","Lantus","Avastin","Lyrica","Oxycontin","Epogen","Celebrex","Truvada","Diovan","Gleevec","Herceptin","Lucentis","Namenda","Vyvanse","Zetia","Levemir","Symbicort","Sovaldi","Novolog Flexpen","Novolog","Tecfidera","Suboxone","Humalog","Xarelto","Seroquel XR","Viagra","Alimta","Victoza 3-Pak","Avonex","Nasonex","Cialis","Gilenya","Stelara","Flovent HFA","Prezista","Procrit"};
    }

    String getOrderLocation(String userName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("orders",new String[]{"location"},"customerusername"+"=?",new String[]{userName},null,null,null,null);
        String res="0:0";
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            res = cursor.getString(cursor.getColumnIndex("location"));
        }
        return res;
    }

    String getOrders(String shopkeeper){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("orders",new String[]{"customerusername","shopusername","medicines"},"shopusername"+"=?",new String[]{shopkeeper},null,null,null,null);
        String result = "";
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String ans = cursor.getString(cursor.getColumnIndex("customerusername")) + "-" + cursor.getString(cursor.getColumnIndex("medicines"));
                if(result.equals(""))   result = ans;
                else    result = result + "," + ans;
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return result;
    }

    void deleteOrder(String userName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from orders where customerusername='"+userName+"'");
        db.close();
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TWO);
        db.execSQL("DROP TABLE IF EXISTS " + IVENTORY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + "orders");
        // Create tables again
        onCreate(db);
    }

    String getLocation(String userName){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME_TWO, new String[]{LOCATION},USER_NAME+"=?",new String[]{String.valueOf(userName)},null,null,null,null);
        cursor.moveToFirst();
        String loc = cursor.getString(cursor.getColumnIndex(LOCATION));
        db.close();
        return loc;
    }

    boolean checkDetails(String userName, boolean type){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;

        if(type) {
            cursor = db.query(TABLE_NAME, new String[]{USER_NAME,
                            EMAID_ID, PHONE_NUMBER, PASSWORD}, USER_NAME + "=?",
                    new String[]{String.valueOf(userName)}, null, null, null, null);
        }else{
            cursor = db.query(TABLE_NAME_TWO, new String[]{USER_NAME,
                            EMAID_ID, PHONE_NUMBER, PASSWORD}, USER_NAME + "=?",
                    new String[]{String.valueOf(userName)}, null, null, null, null);
        }

        if(cursor.getCount() > 0 ){
            db.close();
            return true;
        }

        db.close();
        return false;
    }

    boolean addUser(String username, String emailid, String phoneNo, String pass, boolean type, String loc) {
        if(checkDetails(username,type)) return false;

        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(USER_NAME, username);
            values.put(EMAID_ID, emailid);
            values.put(PHONE_NUMBER, phoneNo);
            values.put(PASSWORD, pass);
            values.put(LOCATION, loc);

            // Inserting Row
            if (type)
                db.insert(TABLE_NAME, null, values);
            else
                db.insert(TABLE_NAME_TWO, null, values);
            db.close(); // Closing database connection
        }catch(Exception e){    return false;   }
        return true;
    }

    String[] getMedicineDetails(String userName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;

        cursor = db.query("inventory", new String[]{USER_NAME,
                        MEDICINE}, USER_NAME + "=?",new String[]{String.valueOf(userName)}, null, null, null, null);
        if(cursor.getCount() > 0 ){
            cursor.moveToFirst();
            String user = cursor.getString(cursor.getColumnIndex(USER_NAME));
            String medicine = cursor.getString(cursor.getColumnIndex(MEDICINE));
            String [] medi = medicine.split(">");
            db.close();
            return medi;
        }

        db.close();
        String[] aa = new String[1];
        aa[0] = "NO";
        return aa;
    }

    boolean addInventories(String userName, String medicines){
        String[] medi = getMedicineDetails(userName);
        String[] presMed = medicines.split(">");
        Log.d("NVIKRAMA",medi[0]);
        String result = "";
        for(int i=0;i<presMed.length;i++){
            Log.d("NVIKRAMA",presMed[i]);
        }

        if(!medi[0].equals("NO")){

            int[] count1 = new int[medi.length];
            String[] mediName1 = new String[medi.length];
            for(int i=0;i<medi.length;i++){
                String[] parts = medi[i].split(":");
                if(parts.length>1) {
                    count1[i] = Integer.parseInt(parts[1]);
                    mediName1[i] = parts[0];
                }
            }

            int[] count2 = new int[presMed.length];
            String[] mediName2 = new String[presMed.length];
            for(int i=0;i<presMed.length;i++){
                String[] parts = presMed[i].split(":");
                count2[i] = Integer.parseInt(parts[1]);
                mediName2[i] = parts[0];
            }

            HashMap<String, Integer> hashMap = new HashMap<>();
            for(int i=0;i<medi.length;i++){
                hashMap.put(mediName1[i],(hashMap.get(mediName1[i])==null?0:hashMap.get(mediName1[i]))+count1[i]);
            }
            for(int i=0;i<presMed.length;i++){
                hashMap.put(mediName2[i],(hashMap.get(mediName2[i])==null?0:hashMap.get(mediName2[i]))+count2[i]);
            }


            Iterator it = hashMap.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry pair = (Map.Entry)it.next();
                if(pair.getKey() != null) {
                    String medicine = pair.getKey().toString();
                    String count = pair.getValue().toString();
                    if (result.equals("")) {
                        result = medicine + ":" + count;
                    } else {
                        result = result + ">" + medicine + ":" + count;
                    }
                }
                it.remove();
            }


        }else{
            int[] count2 = new int[presMed.length];
            String[] mediName2 = new String[presMed.length];
            for(int i=0;i<presMed.length;i++){
                String[] parts = presMed[i].split(":");
                count2[i] = Integer.parseInt(parts[1]);
                mediName2[i] = parts[0];
            }
            HashMap<String, Integer> hashMap = new HashMap<>();
            for(int i=0;i<presMed.length;i++){
                hashMap.put(mediName2[i],(hashMap.get(mediName2[i])==null?0:hashMap.get(mediName2[i]))+count2[i]);
            }
            Iterator it = hashMap.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry pair = (Map.Entry)it.next();
                String medicine = pair.getKey().toString();
                String count = pair.getValue().toString();
                if(result.equals("")){
                    result = medicine+":"+count;
                }
                else{
                    result = result + ">" + medicine + ":" + count;
                }
                it.remove();
            }
            Log.d("NVIKRAMA","OK GUYS");
        }
        Log.d("NVIKRAMA",result);
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(USER_NAME, userName);
            values.put(MEDICINE, result);
            db.insert(IVENTORY_TABLE, null, values);
            db.close(); // Closing database connection
        }catch(Exception e){    return false;   }

        return true;
    }

    String getDetails(String userName, boolean type) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;

        if(type) {
            cursor = db.query(TABLE_NAME, new String[]{USER_NAME,
                            EMAID_ID, PHONE_NUMBER, PASSWORD}, USER_NAME + "=?",
                    new String[]{String.valueOf(userName)}, null, null, null, null);
        }else{
            cursor = db.query(TABLE_NAME_TWO, new String[]{USER_NAME,
                            EMAID_ID, PHONE_NUMBER, PASSWORD}, USER_NAME + "=?",
                    new String[]{String.valueOf(userName)}, null, null, null, null);
        }
        if(cursor.getCount() < 1)  return null;
        if (cursor != null)
            cursor.moveToFirst();
        String username = cursor.getString(cursor.getColumnIndex(USER_NAME));
        String emailId = cursor.getString(cursor.getColumnIndex(EMAID_ID));
        String phno = cursor.getString(cursor.getColumnIndex(PHONE_NUMBER));
        String pass = cursor.getString(cursor.getColumnIndex(PASSWORD));

        String result = username+":"+emailId+":"+phno+":"+pass;
        db.close();
        return result;
    }

    boolean makeOrder(String userName, String loc, String medicinesNeeded){
        SQLiteDatabase db = this.getWritableDatabase();

        String[] part = loc.split(":");
        double lattitudeCust = Double.parseDouble(part[0]);
        double longitudeCust = Double.parseDouble(part[1]);

        Cursor cursor = db.query(IVENTORY_TABLE,new String[]{USER_NAME,MEDICINE},null,null,null,null,null);
        Log.d("NVIKRAMA",String.valueOf(cursor.getCount()));

        ArrayList<String> possibleUserNames = new ArrayList<>();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String user = cursor.getString(cursor.getColumnIndex(USER_NAME));
            String medicine = cursor.getString(cursor.getColumnIndex(MEDICINE));
            Log.d("NVIKRAMA",user + "::" + medicine);
            String [] medi = medicine.split(">");
            int[] count1 = new int[medi.length];
            String[] mediName1 = new String[medi.length];
            for(int i=0;i<medi.length;i++) {
                String[] parts1 = medi[i].split(":");
                if (parts1.length > 1){
                    count1[i] = Integer.parseInt(parts1[1]);
                    mediName1[i] = parts1[0];
               }
            }
            HashMap<String, Integer> hashMap1 = new HashMap<>();
            for(int i=0;i<medi.length;i++){
                hashMap1.put(mediName1[i],(hashMap1.get(mediName1[i])==null?0:hashMap1.get(mediName1[i]))+count1[i]);
            }

            String[] presMed = medicinesNeeded.split(">");
            int[] count2 = new int[presMed.length];
            String[] mediName2 = new String[presMed.length];
            for(int i=0;i<presMed.length;i++){
                String[] parts = presMed[i].split(":");
                count2[i] = Integer.parseInt(parts[1]);
                mediName2[i] = parts[0];
            }
            HashMap<String, Integer> hashMap2 = new HashMap<>();
            for(int i=0;i<presMed.length;i++){
                hashMap2.put(mediName2[i],(hashMap2.get(mediName2[i])==null?0:hashMap2.get(mediName2[i]))+count2[i]);
            }

            boolean flag = true;

            Iterator itNeeded = hashMap2.entrySet().iterator();
            while(itNeeded.hasNext()){
                Map.Entry pair = (Map.Entry)itNeeded.next();
                String medicines = pair.getKey().toString();
                int count = Integer.parseInt(pair.getValue().toString());

                int coun1 = hashMap1.get(medicines)==null?0:hashMap1.get(medicines);
                if(coun1< count){
                    flag = false;
                    break;
                }
                itNeeded.remove();
            }

            if(flag)    possibleUserNames.add(user);

            cursor.moveToNext();
        }
        cursor.close();

        Log.d("NVIKRAMA",String.valueOf(possibleUserNames.size()));

        if(possibleUserNames.size()==0) return false;
        Cursor cur = db.query("orders", new String[]{"customerusername","shopusername","medicines"}, "customerusername"+"=?", new String[]{userName},null,null,null,null);

        if(cur.getCount()>0)    return false;
        double minDiff = Double.MAX_VALUE;
        int index = 0;

        for(int i=0;i<possibleUserNames.size();i++){
            String shopLoc = getLocation(possibleUserNames.get(i));
            String[] par = shopLoc.split(":");
            double lattitude = Double.parseDouble(par[0]);
            double longitude = Double.parseDouble(par[1]);
            double diff = Math.abs(lattitude-lattitudeCust) + Math.abs(longitude-longitudeCust);
            if(diff<minDiff){
                minDiff = diff;
                index = i;
            }
        }
        db.close();
        Log.d("NVIKRAMA",possibleUserNames.get(index));

        String shopUserName = possibleUserNames.get(index);
        String[] medicineDetails = getMedicineDetails(shopUserName);
        int[] count1 = new int[medicineDetails.length];
        String[] mediName1 = new String[medicineDetails.length];
        for(int i=0;i<medicineDetails.length;i++){
            String[] parts = medicineDetails[i].split(":");
            count1[i] = Integer.parseInt(parts[1]);
            mediName1[i] = parts[0];
        }
        HashMap<String,Integer> hashMap = new HashMap<>();
        for(int i=0;i<medicineDetails.length;i++){
            hashMap.put(mediName1[i],(hashMap.get(mediName1[i])==null?0:hashMap.get(mediName1[i]))+count1[i]);
        }

        String[] presMed = medicinesNeeded.split(">");
        int[] count2 = new int[presMed.length];
        String[] mediName2 = new String[presMed.length];
        for(int i=0;i<presMed.length;i++){
            String[] parts = presMed[i].split(":");
            count2[i] = Integer.parseInt(parts[1]);
            mediName2[i] = parts[0];
        }
        HashMap<String, Integer> hashMap2 = new HashMap<>();
        for(int i=0;i<presMed.length;i++){
            hashMap2.put(mediName2[i],(hashMap2.get(mediName2[i])==null?0:hashMap2.get(mediName2[i]))+count2[i]);
        }

        //hashMap contains the details of the allocated shopkeeper
        // hashMap2 contains the details of the user

        Iterator it = hashMap2.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            String medicines = pair.getKey().toString();
            int count = Integer.parseInt(pair.getValue().toString());
            if(hashMap.get(medicines)-count == 0){
                hashMap.remove(medicines);
            }else
                hashMap.put(medicines,hashMap.get(medicines)-count);
            it.remove();
        }

        String result="";
        it = hashMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            String medicine = pair.getKey().toString();
            String count = pair.getValue().toString();
            if(result.equals("")){
                result = medicine+":"+count;
            }
            else{
                result = result + ">" + medicine + ":" + count;
            }

            it.remove();
        }
        db = this.getWritableDatabase();
        db.execSQL("delete from "+IVENTORY_TABLE+" where username='"+shopUserName+"'");

        ContentValues values = new ContentValues();
        values.put(USER_NAME, shopUserName);
        values.put(MEDICINE, result);
        db.insert(IVENTORY_TABLE, null, values);



        values = new ContentValues();
        values.put("customerusername",userName);
        values.put("shopusername",shopUserName);
        values.put("medicines",medicinesNeeded);
        values.put("location",loc);
        db.insert("orders",null,values);


        db.close();
        return true;
    }

}
