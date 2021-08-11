package com.greenart.component;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.greenart.service.CoronaInfoService;
import com.greenart.vo.CoronaAgeInfoVO;
import com.greenart.vo.CoronaInfoVO;
import com.greenart.vo.CoronaSidoInfoVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Component
public class CoronaInfoComponent {
    @Autowired
    CoronaInfoService service;
    // 매일 10시 30분에 한 번 호출
    @Scheduled(cron="0 30 10 * * *")
    public void getCoronaInfo() throws Exception {
        System.out.println("cron schedule");
        Date dt = new Date();   // 현재시간
        SimpleDateFormat dtFormatter = new SimpleDateFormat("YYYYMMdd");
        String today = dtFormatter.format(dt);

        StringBuilder urlBuilder = new StringBuilder("http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19InfStateJson"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=3CID6KRU4kjF4jvHanoFBLwycg6Htt86aVfgEOgBmAecshZIcO5EC9UM9FhVGwAX2Zf%2B%2FrxgsJeUfled1zNS0w%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("startCreateDt","UTF-8") + "=" + URLEncoder.encode(today, "UTF-8")); /*검색할 생성일 범위의 시작*/
        urlBuilder.append("&" + URLEncoder.encode("endCreateDt","UTF-8") + "=" + URLEncoder.encode(today, "UTF-8")); /*검색할 생성일 범위의 종료*/
        
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(urlBuilder.toString());

        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("item");
        if(nList.getLength() <= 0) {
            return;
        }
        for(int i=0; i<nList.getLength(); i++){
            Node node = nList.item(i);
            Element elem = (Element) node;

            CoronaInfoVO vo = new CoronaInfoVO();
            vo.setAccExamCnt(Integer.parseInt(getTagValue("accExamCnt", elem)));
            vo.setAccExamCompCnt(Integer.parseInt(getTagValue("accExamCompCnt", elem)));
            vo.setCareCnt(Integer.parseInt(getTagValue("careCnt", elem)));
            vo.setClearCnt(Integer.parseInt(getTagValue("clearCnt", elem)));
            vo.setDeathCnt(Integer.parseInt(getTagValue("deathCnt", elem)));
            vo.setDecideCnt(Integer.parseInt(getTagValue("decideCnt", elem)));
            vo.setExamCnt(Integer.parseInt(getTagValue("examCnt", elem)));
            vo.setResultNegCnt(Integer.parseInt(getTagValue("resutlNegCnt", elem)));
            // String to Date
            Date createDt = new Date();
            SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            createDt = dtFormat.parse(getTagValue("createDt", elem));
            vo.setStateTime(createDt);
            service.insertCoronaInfo(vo);
        }
    }

    public static String getTagValue(String tag, Element elem) {
        NodeList nList = elem.getElementsByTagName(tag).item(0).getChildNodes();
        if(nList == null) return null;
        Node node = (Node) nList.item(0);
        if(node == null) return null;
        return node.getNodeValue();
    }

    // 매일 10:30:10에 한 번 실행
    @Scheduled(cron="10 30 10 * * *")
    public void getCoronaSidoInfo() throws Exception{
        Date dt = new Date();   // 현재시간
        SimpleDateFormat dtFormatter = new SimpleDateFormat("YYYYMMdd");
        String today = dtFormatter.format(dt);

        StringBuilder urlBuilder = new StringBuilder("http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=3CID6KRU4kjF4jvHanoFBLwycg6Htt86aVfgEOgBmAecshZIcO5EC9UM9FhVGwAX2Zf%2B%2FrxgsJeUfled1zNS0w%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("100000", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("startCreateDt","UTF-8") + "=" + URLEncoder.encode(today, "UTF-8")); /*검색할 생성일 범위의 시작*/
        urlBuilder.append("&" + URLEncoder.encode("endCreateDt","UTF-8") + "=" + URLEncoder.encode(today, "UTF-8")); /*검색할 생성일 범위의 종료*/
        
        System.out.println(urlBuilder.toString());
        
        // 2. 데이터 요청 (Request)
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(urlBuilder.toString());
        
        // 3. XML파싱
        // text -> Node로 변환
        doc.getDocumentElement().normalize();
        System.out.println(doc.getDocumentElement().getNodeName());
        
        NodeList nList = doc.getElementsByTagName("item");
        System.out.println("데이터 수 : "+nList.getLength());

        for(int i=0; i<nList.getLength(); i++){
            Node node = nList.item(i);
            Element elem = (Element) node;

            String createDt = getTagValue("createDt", elem);
            String deathCnt = getTagValue("deathCnt", elem);
            String defCnt = getTagValue("defCnt", elem);
            String gubun = getTagValue("gubun", elem);
            String incDec = getTagValue("incDec", elem);
            String isolClearCnt = getTagValue("isolClearCnt", elem);
            String isolIngCnt = getTagValue("isolIngCnt", elem);
            String localOccCnt = getTagValue("localOccCnt", elem);
            String overFlowCnt = getTagValue("overFlowCnt", elem);

            CoronaSidoInfoVO vo = new CoronaSidoInfoVO();
            // 문자열로 표현된 날짜를 java util date 타입으로 변환
            Date cDt = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            cDt = formatter.parse(createDt); // 문자열로부터 날짜를 유추한다
            vo.setCreateDt(cDt);
            vo.setDeathCnt(Integer.parseInt(deathCnt));
            vo.setDefCnt(Integer.parseInt(defCnt));
            vo.setGubun(gubun);
            vo.setIncDec(Integer.parseInt(incDec));
            vo.setIsolClearCnt(Integer.parseInt(isolClearCnt));
            vo.setIsolIngCnt(Integer.parseInt(isolIngCnt));
            vo.setLocalOccCnt(Integer.parseInt(localOccCnt));
            vo.setOverFlowCnt(Integer.parseInt(overFlowCnt));

            // System.out.println(vo);
            service.insertCoronaSidoInfo(vo);
        }
    }

    @Scheduled(cron="20 30 10 * * *")
    public void getCoronaAgeInfo() throws Exception {
        Date dt = new Date();
        SimpleDateFormat dtFormatter = new SimpleDateFormat("YYYYMMdd");
        String today = dtFormatter.format(dt);

        StringBuilder urlBuilder = new StringBuilder("http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19GenAgeCaseInfJson"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=3CID6KRU4kjF4jvHanoFBLwycg6Htt86aVfgEOgBmAecshZIcO5EC9UM9FhVGwAX2Zf%2B%2FrxgsJeUfled1zNS0w%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("100000", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("startCreateDt","UTF-8") + "=" + URLEncoder.encode(today, "UTF-8")); /*검색할 생성일 범위의 시작*/
        urlBuilder.append("&" + URLEncoder.encode("endCreateDt","UTF-8") + "=" + URLEncoder.encode(today, "UTF-8")); /*검색할 생성일 범위의 종료*/
        
        System.out.println(urlBuilder.toString());

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(urlBuilder.toString());
        
        // 3. XML파싱
        // text -> Node로 변환
        doc.getDocumentElement().normalize();
        System.out.println(doc.getDocumentElement().getNodeName());
        
        NodeList nList = doc.getElementsByTagName("item");
        System.out.println("데이터 수 : "+nList.getLength());

        for(int i=0; i<nList.getLength(); i++){
            Node node = nList.item(i);
            Element elem = (Element) node;
            
            String createDt = getTagValue("createDt", elem);
            String confCase = getTagValue("confCase", elem);
            String confCaseRate = getTagValue("confCaseRate", elem);
            String criticalRate = getTagValue("criticalRate", elem);
            String death = getTagValue("death", elem);
            String deathRate = getTagValue("deathRate", elem);
            String gubun = getTagValue("gubun", elem);

            if(gubun.equals("남성")) gubun = "남성";
            else if (gubun.equals("여성")) gubun = "여성";
            else if(gubun.equals("0-9")) gubun = "0";
            else if(gubun.equals("10-19")) gubun = "10";
            else if(gubun.equals("20-29")) gubun = "20";
            else if(gubun.equals("30-39")) gubun = "30";
            else if(gubun.equals("40-49")) gubun = "40";
            else if(gubun.equals("50-59")) gubun = "50";
            else if(gubun.equals("60-69")) gubun = "60";
            else if(gubun.equals("70-79")) gubun = "70";
            else gubun = "80";

            CoronaAgeInfoVO vo = new CoronaAgeInfoVO();
            // 문자열로 표현된 날짜를 java util date 타입으로 변환
            Date cDt = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            cDt = formatter.parse(createDt); // 문자열로부터 날짜를 유추한다
            vo.setCreateDt(cDt);
            vo.setConfCase(Integer.parseInt(confCase));
            vo.setConfCaseRate(Double.parseDouble(confCaseRate));
            vo.setCriticalRate(Double.parseDouble(criticalRate));
            vo.setDeath(Integer.parseInt(death));
            vo.setDeathRate(Double.parseDouble(deathRate));
            vo.setGubun(gubun);

            service.insertCoronaAgeInfo(vo);
        }
    }
}
