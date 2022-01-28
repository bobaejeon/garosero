package com.foo.garosero.myUtil;

import com.foo.garosero.data.TreeApiData;
import com.foo.garosero.mviewmodel.MapViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class ApiHelper {
    public static ArrayList<TreeApiData> getApiData(String SEOUL_GAROSU_API_KEY, Integer start_row, Integer end_row, String GU_NM) {
        ArrayList<TreeApiData> treeApiDataList = new ArrayList<TreeApiData>();

        // 쿼리 작성하기
        String dataType = "json";
        String queryUrl = "http://openAPI.seoul.go.kr:8088/"+SEOUL_GAROSU_API_KEY+
                "/"+dataType+"/GeoInfoOfRoadsideTreeW/"+start_row+"/"+(end_row-1)+"/"+GU_NM;

        try {
            // 데이터 받아오기
            URL url = new URL(queryUrl);

            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);

            StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();
            while (line != null) {
                buffer.append(line + "\n");
                line = reader.readLine();
            }

            // 데이터 파싱하기
            String jsonString = buffer.toString();
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject GeoInfoRoadsideTree = jsonObject.getJSONObject("GeoInfoOfRoadsideTreeW");
            JSONArray row = GeoInfoRoadsideTree.getJSONArray("row");

            // 데이터 담기
            for (int i=0; i<row.length();i++){
                JSONObject item = row.getJSONObject(i);
                treeApiDataList.add(new TreeApiData(
                        item.getString(TreeApiData.STRING_GU_NM),
                        item.getString(TreeApiData.STRING_TRE_IDN),
                        item.getString(TreeApiData.STRING_WDPT_NM),
                        item.getDouble(TreeApiData.STRING_LNG),
                        item.getDouble(TreeApiData.STRING_LAT)
                ));
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return treeApiDataList;
    }
}
