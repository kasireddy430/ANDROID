package com.Akshith.KnowYourGovernment;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class OfficialRunnable implements Runnable {

    String API_KEY = "AIzaSyAwBnPV5HhEggCNbPwbXgyQKtyQy-QrA04";
    String CIVIC_API_URL = "https://www.googleapis.com/civicinfo/v2/representatives?key="+API_KEY+"&address=";

    private MainActivity mainActivity;
    private String zipcode=null;
    private Uri.Builder buildURL = null;
    private boolean noDataFound=false;
    private StringBuilder sb1;
    private String string;
    private String currentLocationStr=null;
    private ArrayList<Official> officialArrayList= new ArrayList <Official>();
    Object[] objArray = new Object[2];
    boolean isNoDataFound =true;

    public OfficialRunnable(MainActivity ma,String string)
    {
        mainActivity = ma;
        this.string = string;
    }

    private void handleResults(Object a){
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.setOfficialList(objArray);
            }
        });
    }
//    @Override
//    protected void onPostExecute(String s) {
//
//        mainActivity.setOfficialList(objArray);
//    }

    @Override
    public void run() {
        zipcode = string;
        buildURL = Uri.parse(CIVIC_API_URL).buildUpon();
        buildURL.appendQueryParameter("address", string);
        connectToAPI();

        if(!isNoDataFound) {
           Object a =  parseJSON1(sb1.toString());
           handleResults(a);
        }
        else {
            currentLocationStr = "No Data for location";
        }
//        handleResults(a);
    }

    public void connectToAPI() {
        String urlToUse = buildURL.build().toString();
        sb1 = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if(conn.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND)
            {
                noDataFound=true;
            }
            else {
                conn.setRequestMethod("GET");
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

                String line=null;
                while ((line = reader.readLine()) != null) {
                    sb1.append(line).append('\n');
                }
                isNoDataFound=false;

            }
        }
        catch(FileNotFoundException fe){
        }
        catch (Exception e) {
            //e.printStackTrace();
        }
    }

    private Object parseJSON1(String s) {
        try {
            if(!noDataFound) {

                JSONObject jObjMain = new JSONObject(s);

                //location
                JSONObject location = jObjMain.getJSONObject("normalizedInput");
                currentLocationStr = location.getString("city")+" "+location.getString("state")+" "+location.getString("zip");
                //Log.d(TAG, "parseJSON1: ~~ "+currentLocationStr);


                JSONArray offices = jObjMain.getJSONArray("offices");
                JSONArray oficials = jObjMain.getJSONArray("officials");


                for(int i=0;i<offices.length();i++)
                {
                    JSONObject o = (JSONObject) offices.get(i);

                    String office_name = o.getString("name");

                    JSONArray idcArray = o.getJSONArray("officialIndices");

                    for(int j=0;j<idcArray.length();j++)
                    {
                        Official off = new Official();
                        off.setOffice(office_name);
                        //off.setIndex(idcArray.get(j).toString());
                        //officialArrayList.add(off);

                        JSONObject officialData = (JSONObject) oficials.get(idcArray.getInt(j));

                        //Name
                        if (officialData.getString("name") == null || officialData.getString("name").equals(""))
                            off.setName("No Data Provided");
                        else
                            off.setName(officialData.getString("name"));

                        //Address
                        if(officialData.has("address")) {
                            JSONArray addrArr = officialData.getJSONArray("address");
                            String sb_addr = "";
                            JSONObject addrObj = (JSONObject) addrArr.get(0);
                            if (addrObj.has("line1"))
                                sb_addr = sb_addr + addrObj.getString("line1").toString() + '\n';
                            if (addrObj.has("line2"))
                                sb_addr = sb_addr + addrObj.getString("line2").toString() + '\n';
                            if (addrObj.has("line3"))
                                sb_addr = sb_addr + addrObj.getString("line3").toString() + '\n';
                            off.setAddress(sb_addr);
                        }
                        else
                            off.setAddress("No Data Provided");

                        //party
                        if(officialData.has("party"))
                            off.setParty(officialData.getString("party"));
                        else
                            off.setParty("No data provided");

                        //phone
                        if (officialData.has("phones")) {
                            JSONArray phoneArr = officialData.getJSONArray("phones");
                            off.setPhone(phoneArr.get(0).toString());
                        } else
                            off.setPhone("No Data Provided");


                        //url
                        if (officialData.has("urls")) {
                            JSONArray urlArr = officialData.getJSONArray("urls");
                            off.setWebsiteURL(urlArr.get(0).toString());
                        } else
                            off.setWebsiteURL("No Data Provided");


                        //email
                        if (officialData.has("emails")) {
                            JSONArray emailArr = officialData.getJSONArray("emails");
                            off.setEmail(emailArr.get(0).toString());
                        } else
                            off.setEmail("No Data Provided");


                        //photo
                        if (officialData.has("photoUrl"))
                            off.setPhotoURL(officialData.get("photoUrl").toString());

                        //social media
                        if (officialData.has("channels")) {
                            JSONArray mediaArr = officialData.getJSONArray("channels");
                            for (int x = 0; x < mediaArr.length(); x++) {
                                JSONObject mediaObj = (JSONObject) mediaArr.get(x);
                                if (mediaObj.get("type").equals("Facebook"))
                                    off.setFacebookURL(mediaObj.get("id").toString());
                                else if (mediaObj.get("type").equals("Twitter"))
                                    off.setTwitterURL(mediaObj.get("id").toString());
                                else if (mediaObj.get("type").equals("YouTube"))
                                    off.setYouTubeURL(mediaObj.get("id").toString());
                            }

                        }

                        officialArrayList.add(off);
                    }
                }


                objArray[0] = currentLocationStr;
                objArray[1] = officialArrayList;

                return objArray;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
