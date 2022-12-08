package com.ezralee.youthcenterinfoapi;

public class YouthCenterItem {

    String polyBizSjnm, polyItcnCn, plcyTpNm, cnsgNmor,rqutUrla;
    //정책명, 정책소개, 정책유형, 신청기관명, 사이트 링크 주소

    public YouthCenterItem() {
    }

    public YouthCenterItem(String polyBizSjnm, String polyItcnCn, String plcyTpNm, String cnsgNmor, String rqutUrla) {
        this.polyBizSjnm = polyBizSjnm;
        this.polyItcnCn = polyItcnCn;
        this.plcyTpNm = plcyTpNm;
        this.cnsgNmor = cnsgNmor;
        this.rqutUrla = rqutUrla;
    }
}
