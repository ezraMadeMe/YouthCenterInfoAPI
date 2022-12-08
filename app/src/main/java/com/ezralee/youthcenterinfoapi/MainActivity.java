package com.ezralee.youthcenterinfoapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerType, spinnerDetailType; //대분류/소분류
    GridView region;    //지역 그리드뷰
    Button search;      //검색 버튼

    String typeSel, typeSelNm, detailTypeSel, detailTypeSelNm, regionSel;    //선택한 정책 대분류/소분류 분류코드/지역명 저장

    HashMap<String, String> typeSelected = new HashMap<>();   //정책 대분류명/분류코드 해시맵
    HashMap<String, String> detailTypeSelected = new HashMap<>(); //정책 소분류명/분류코드 해시맵
    HashMap<String, String> regionSelected = new HashMap<>();       //선택된 지역 해시맵
    HashMap<String, String> regionMap = new HashMap<>();    //전체 지역 해시맵
    //정책분류코드
    String[] bizTycdSel = {"대분류 선택", "취업 지원", "창업 지원", "주거/금융", "생활/복지", "정책 참여", "코로나19"};
    String[] bizTycdSelNum = {"", "004001", "004002", "004003", "004004", "004005", "004006"};
    String[] detailbizTycdSelNum = {"", "001", "002", "003", "004", "005", "006"};
    //지역분류코드/지역명
    String[] regions = {"서울", "부산", "대구", "인천", "광주", "대전", "울산", "경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주", "세종"};
    String[] regionNum = {"003002001", "003002002", "003002003", "003002004", "003002005", "003002006", "003002007", "003002008", "003002009", "003002010", "003002011",
            "003002012", "003002013", "003002014", "003002015", "003002016", "003002017"};
    String[] type;


    RegionGridAdapter regionGridAdapter;    //지역 그리드뷰 어댑터
    ArrayAdapter<String> typeArrayAdapter;  //정책분류 스피너 어댑터

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //findViewById(대분류 스피너, 소분류 스피너, 지역 그리드뷰, 검색 버튼)
        spinnerType = findViewById(R.id.type_spinner);
        spinnerDetailType = findViewById(R.id.type_detail_spinner);
        region = findViewById(R.id.region_detail);
        search = findViewById(R.id.btn_search);

        for (int i = 0; i < regions.length; i++) { //해시맵에 지역명/지역코드 묶어서 그룹화
            regionMap.put(regions[i], regionNum[i]);
        }
        for (int i = 0; i < bizTycdSel.length; i++) { //해시맵에 대분류/대분류코드 묶어서 그룹화
            typeSelected.put(bizTycdSel[i], bizTycdSelNum[i]);
        }

        //대분류 스피너에 어레이아답터 세팅
        typeArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, (String[]) getResources().getStringArray(R.array.bizTycdSel));
        typeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeArrayAdapter);

        //정책 대분류에 따라 다르게 나오는 소분류 스피너 메서드
        selectType();

        //그리드뷰에 텍스트뷰로 지역명 뿌려넣고 아답터로 세팅
        regionGridAdapter = new RegionGridAdapter(this);
        region.setAdapter(regionGridAdapter);

        //그리드뷰의 아이템 클릭을 듣는 리스너
        region.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                boolean checked = Boolean.parseBoolean(view.getTag().toString());

                if (regionSelected.size() <1) {
                    if (!checked) { //뷰의 백그라운드 색상이 "#FF018786"이 아니면
                        view.setBackgroundColor(Color.parseColor("#FF018786"));
                        regionSel = regionGridAdapter.getItem(i).toString();
                        regionSelected.put(regions[i], regionNum[i]);
                        view.setTag(true);

                    } else { //한번더 클릭하면 원상복구
                        view.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                        regionSel = regionGridAdapter.getItem(i).toString();
                        regionSelected.remove(regions[i]);
                        view.setTag(false);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "지역은 한 곳만 선택 가능합니다", Toast.LENGTH_SHORT).show();
                    if (checked){
                        view.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                        regionSel = regionGridAdapter.getItem(i).toString();
                        regionSelected.remove(regions[i]);
                        view.setTag(false);
                    }
                }


                //클릭한 그리드뷰의 값이 잘 들어오는지 테스트
                //Toast.makeText(MainActivity.this, regionSelected.get(regions[i]) + "", Toast.LENGTH_SHORT).show();
            }
        });

        //버튼을 클릭하면 다음 페이지로 이동
        search.setOnClickListener(view -> setInfo());

    }//onCreate

    void setDetailTypeSpinnerAdapterItem(int typeArray) {
        if (typeArrayAdapter != null) {
            spinnerDetailType.setAdapter(null);
            typeArrayAdapter = null;
        }

        if (spinnerType.getSelectedItemPosition() > 1) {
            spinnerDetailType.setAdapter(null);
        }

        typeArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, (String[]) getResources().getStringArray(typeArray));
        typeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDetailType.setAdapter(typeArrayAdapter);


    }//setDetailTypeSpinnerAdapterItem - 대분류 스피너에 따라 다르게 반응하는 소분류 스피너의 아답터

    void selectType() {
        //정책 대분류 스피너 리스너
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                switch (i) {

                    case 0:
                        setDetailTypeSpinnerAdapterItem(R.array.type00);
                        //Toast.makeText(MainActivity.this, "정책 대분류를 선택하세요", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        typeSel = typeSelected.get(bizTycdSel[1]);    //선택한 대분류의 분류코드 값 저장
                        typeSelNm = bizTycdSel[1];
                        type = getResources().getStringArray(R.array.type01);  //xml의 소분류 목록 불러오기
                        for (int x = 0; x < type.length; x++) {
                            detailTypeSelected.put(detailbizTycdSelNum[x], type[x]);    //소분류 해시맵에 값 넣기
                        }
                        setDetailTypeSpinnerAdapterItem(R.array.type01);
                        break;
                    case 2:
                        typeSel = typeSelected.get(bizTycdSel[2]);    //선택한 대분류의 분류코드 값 저장
                        typeSelNm = bizTycdSel[2];
                        type = getResources().getStringArray(R.array.type02);  //xml의 소분류 목록 불러오기
                        for (int x = 0; x < type.length; x++) {
                            detailTypeSelected.put(detailbizTycdSelNum[x], type[x]);    //소분류 해시맵에 값 넣기
                        }
                        setDetailTypeSpinnerAdapterItem(R.array.type02);
                        break;
                    case 3:
                        typeSel = typeSelected.get(bizTycdSel[3]);    //선택한 대분류의 분류코드 값 저장
                        typeSelNm = bizTycdSel[3];
                        type = getResources().getStringArray(R.array.type03);  //xml의 소분류 목록 불러오기
                        for (int x = 0; x < type.length; x++) {
                            detailTypeSelected.put(detailbizTycdSelNum[x], type[x]);    //소분류 해시맵에 값 넣기
                        }
                        setDetailTypeSpinnerAdapterItem(R.array.type03);
                        break;
                    case 4:
                        typeSel = typeSelected.get(bizTycdSel[4]);    //선택한 대분류의 분류코드 값 저장
                        typeSelNm = bizTycdSel[4];
                        type = getResources().getStringArray(R.array.type04);  //xml의 소분류 목록 불러오기
                        for (int x = 0; x < type.length; x++) {
                            detailTypeSelected.put(detailbizTycdSelNum[x], type[x]);    //소분류 해시맵에 값 넣기
                        }
                        setDetailTypeSpinnerAdapterItem(R.array.type04);
                        break;
                    case 5:
                        typeSel = typeSelected.get(bizTycdSel[5]);    //선택한 대분류의 분류코드 값 저장
                        typeSelNm = bizTycdSel[5];
                        type = getResources().getStringArray(R.array.type05);  //xml의 소분류 목록 불러오기
                        for (int x = 0; x < type.length; x++) {
                            detailTypeSelected.put(detailbizTycdSelNum[x], type[x]);    //소분류 해시맵에 값 넣기
                        }
                        setDetailTypeSpinnerAdapterItem(R.array.type05);
                        break;
                    case 6:
                        typeSel = typeSelected.get(bizTycdSel[6]);    //선택한 대분류의 분류코드 값 저장
                        typeSelNm = bizTycdSel[6];
                        type = getResources().getStringArray(R.array.type06);  //xml의 소분류 목록 불러오기
                        for (int x = 0; x < type.length; x++) {
                            detailTypeSelected.put(detailbizTycdSelNum[x], type[x]);    //소분류 해시맵에 값 넣기
                        }
                        setDetailTypeSpinnerAdapterItem(R.array.type06);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //정책 소분류 스피너
        spinnerDetailType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(MainActivity.this, "정책 소분류를 선택하세요", Toast.LENGTH_SHORT).show();
                switch (i) {
                    case 0:
                        //Toast.makeText(MainActivity.this, "정책 대분류를 선택하세요", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        detailTypeSel = detailTypeSelected.get(detailbizTycdSelNum[1]);
                        detailTypeSelNm = detailbizTycdSelNum[1];
                        Toast.makeText(MainActivity.this, detailTypeSel, Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        detailTypeSel = detailTypeSelected.get(detailbizTycdSelNum[2]);
                        detailTypeSelNm = detailbizTycdSelNum[2];
                        Toast.makeText(MainActivity.this, detailTypeSel, Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        detailTypeSel = detailTypeSelected.get(detailbizTycdSelNum[3]);
                        detailTypeSelNm = detailbizTycdSelNum[3];
                        Toast.makeText(MainActivity.this, detailTypeSel, Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        detailTypeSel = detailTypeSelected.get(detailbizTycdSelNum[4]);
                        detailTypeSelNm = detailbizTycdSelNum[4];
                        Toast.makeText(MainActivity.this, detailTypeSel, Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        detailTypeSel = detailTypeSelected.get(detailbizTycdSelNum[5]);
                        detailTypeSelNm = detailbizTycdSelNum[5];
                        Toast.makeText(MainActivity.this, detailTypeSel, Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        detailTypeSel = detailTypeSelected.get(detailbizTycdSelNum[6]);
                        detailTypeSelNm = detailbizTycdSelNum[6];
                        Toast.makeText(MainActivity.this, detailTypeSel, Toast.LENGTH_SHORT).show();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(MainActivity.this, "정책 소분류를 선택하세요", Toast.LENGTH_SHORT).show();
            }
        });
    }//selectType-대분류 선택에 따른 소분류 스피너 엔트리 선택


    void setInfo() {

        //선택한 대/소분류 정책분류와 지역명 가져오기

        if (typeSel != null && detailTypeSel != null && regionSelected.size() > 0) {

            ArrayList regionArr = new ArrayList();
            ArrayList regionKeyArr = new ArrayList();
            regionArr.addAll(regionSelected.values());  //지역코드값 어레이
            regionKeyArr.addAll(regionSelected.keySet());   //지역명값 어레이


            Intent intent = new Intent(MainActivity.this, ResultActivity.class);

            intent.putExtra("typeSel", typeSel);    //대분류 코드
            intent.putExtra("typeSelNm", typeSelNm); //대분류 이름
            intent.putExtra("detailTypeSel", detailTypeSel);    //소분류 코드
            intent.putExtra("detailTypeSelNm", detailTypeSelNm); //소분류 이름
            intent.putStringArrayListExtra("regionSel", regionArr);   //지역코드
            intent.putStringArrayListExtra("regionSelNm", regionKeyArr);   //지역이름


            startActivity(intent);

        } else if (regionSelected.size() == 0) {
            Toast.makeText(this, "지역을 선택하세요", Toast.LENGTH_SHORT).show();

        } else if (spinnerType.getSelectedItemPosition()==0 || spinnerDetailType.getSelectedItemPosition()==0) {
            Toast.makeText(this, "정책 분류를 선택하세요", Toast.LENGTH_SHORT).show();
        }


    }//searchInfo
}