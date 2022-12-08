package com.ezralee.youthcenterinfoapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResultActivity extends AppCompatActivity {

    String apikey = "0be07999b1992489a03d18de";

    ArrayList<YouthCenterItem> youthCenterItems = new ArrayList<>();    //리사이클러 뷰 내의 아이템 어레이리스트
    RecyclerView recyclerView;  //리사이클러뷰
    ResultAdapter adapter;      //결과 xml과 붙여주는 어댑터
    TextView resultTitle;       //결과창 제목/바로가기 텍스트뷰

    //선택된 대분류/소분류 이름과 코드
    String typeSel, detailTypeSel, typeSelNm, detailTypeSelNm;
    //선택된 지역코드/이름
    ArrayList<String> regionArr = new ArrayList<>();
    ArrayList<String> regionKeyArr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultTitle = findViewById(R.id.result_title);

        recyclerView = findViewById(R.id.recyclerview);
        //rqutUrla = findViewById(R.id.rqutUrla);
        adapter = new ResultAdapter(this, youthCenterItems);
        recyclerView.setAdapter(adapter);

        getInfo();

        resultTitle.setText(regionKeyArr + "지역의" + "\n" + detailTypeSel + " 정책 검색 결과");

        Thread thread = new Thread() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        youthCenterItems.clear();
                        adapter.notifyDataSetChanged();
                    }
                });

                String address = "https://www.youthcenter.go.kr/opi/empList.do"
                        + "?pageIndex=1" + "&display=50"
                        + "&openApiVlak=" + apikey
                        + "&bizTycdSel=" + typeSel + detailTypeSelNm
                        + "&srchPolyBizSecd=" + regionArr.get(0);


                try {
                    URL url = new URL(address);
                    InputStream inputStream = url.openStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(inputStreamReader);

                    int eventType = xpp.getEventType();

                    YouthCenterItem item = null;

                    while (eventType != XmlPullParser.END_DOCUMENT) {  //eventType != XmlPullParser.END_DOCUMENT
                        xpp.next();
                        eventType = xpp.getEventType();

                        switch (eventType) {
                            case XmlPullParser.START_DOCUMENT:
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ResultActivity.this, "Parsing started", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;

                            case XmlPullParser.START_TAG:
                                String tagName = xpp.getName();
                                //polyBizSjnm, polyItcnCn, plcyTpNm, cnsgNmor,rqutUrla;
                                //정책명, 정책소개, 정책유형, 신청기관명, 사이트 링크 주소

                                if (tagName.equals("emp")) {
                                    item = new YouthCenterItem();

                                } else if (tagName.equals("polyBizSjnm")) {
                                    xpp.next();
                                    item.polyBizSjnm = xpp.getText();

                                } else if (tagName.equals("polyItcnCn")) {
                                    xpp.next();
                                    item.polyItcnCn = xpp.getText();

                                } else if (tagName.equals("plcyTpNm")) {
                                    xpp.next();
                                    item.plcyTpNm = xpp.getText();

                                } else if (tagName.equals("cnsgNmor")) {
                                    xpp.next();
                                    if (xpp.getText().equals("null")) {
                                        item.cnsgNmor = "-";
                                    } else {
                                        item.cnsgNmor = xpp.getText();
                                    }

                                } else if (tagName.equals("rqutUrla")) {
                                    xpp.next();
                                    item.rqutUrla = xpp.getText();
                                }
                                break;

                            case XmlPullParser.TEXT:

                                break;

                            case XmlPullParser.END_TAG:
                                if (xpp.getName().equals("emp")) {
                                    youthCenterItems.add(item);
                                }
                                break;
                        }
                    }//while


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //xml이 제대로 읽히고 있는지 확인
                            Toast.makeText(ResultActivity.this, youthCenterItems.size() + "", Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }//try-catch
            }//run
        };//Thread
        thread.start();
    }//onCreate

    void getInfo() { //MainActivity에서 사용자가 선택한 정보 가져오기

        Intent intent2 = getIntent();

        typeSel = intent2.getStringExtra("typeSel");
        typeSelNm = intent2.getStringExtra("typeSelNm");

        detailTypeSel = intent2.getStringExtra("detailTypeSel");
        detailTypeSelNm = intent2.getStringExtra("detailTypeSelNm");

        regionArr = intent2.getStringArrayListExtra("regionSel");
        regionKeyArr = intent2.getStringArrayListExtra("regionSelNm");

    }
}