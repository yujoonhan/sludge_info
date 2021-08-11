package com.greenart.api;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.greenart.service.CoronaInfoService;
import com.greenart.vo.CoronaAgeInfoVO;
import com.greenart.vo.CoronaInfoVO;
import com.greenart.vo.CoronaSidoInfoVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@RestController
public class CoronaAPIController {
    @Autowired
    CoronaInfoService service;

    @GetMapping("/api/corona")
    public Map<String, Object> getCoronaInfo(
        @RequestParam String startDt, @RequestParam String endDt
    ) throws Exception{
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        StringBuilder urlBuilder = new StringBuilder("http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19InfStateJson"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=3CID6KRU4kjF4jvHanoFBLwycg6Htt86aVfgEOgBmAecshZIcO5EC9UM9FhVGwAX2Zf%2B%2FrxgsJeUfled1zNS0w%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("startCreateDt","UTF-8") + "=" + URLEncoder.encode("startDt", "UTF-8")); /*검색할 생성일 범위의 시작*/
        urlBuilder.append("&" + URLEncoder.encode("endCreateDt","UTF-8") + "=" + URLEncoder.encode("endDt", "UTF-8")); /*검색할 생성일 범위의 종료*/
        
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(urlBuilder.toString());

        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("item");
        if(nList.getLength() <= 0) {
            resultMap.put("status", false);
            resultMap.put("message", "데이터가 없습니다");
            return resultMap;
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
            Date dt = new Date();
            SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dt = dtFormat.parse(getTagValue("createDt", elem));
            vo.setStateTime(dt);
            service.insertCoronaInfo(vo);
        }
        resultMap.put("status", true);
        resultMap.put("message", "데이터가 입력되었습니다");
        return resultMap;
    }

    @GetMapping("/api/coronaInfo/{date}")
    public Map<String, Object> getCoronaInfo(
        @PathVariable String date
    ){
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        CoronaInfoVO data = null;
        // /api/coronaInfo/today
        if(date.equals("today")){
            data = service.selectTodayCoronaInfo();
        }

        resultMap.put("status", true);
        resultMap.put("data", data);

        return resultMap;
    }

    public static String getTagValue(String tag, Element elem) {
        NodeList nList = elem.getElementsByTagName(tag).item(0).getChildNodes();
        if(nList == null) return null;
        Node node = (Node) nList.item(0);
        if(node == null) return null;
        return node.getNodeValue();
    }

    @GetMapping("/api/corona/sido")
    public Map<String, Object> getCoronaInfoSido(
        @RequestParam String startDt, @RequestParam String endDt
    ) throws Exception {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

        // 1. 데이터를 가져올 URL을 만드는 과정
        StringBuilder urlBuilder = new StringBuilder("http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=3CID6KRU4kjF4jvHanoFBLwycg6Htt86aVfgEOgBmAecshZIcO5EC9UM9FhVGwAX2Zf%2B%2FrxgsJeUfled1zNS0w%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("100000", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("startCreateDt","UTF-8") + "=" + URLEncoder.encode(startDt, "UTF-8")); /*검색할 생성일 범위의 시작*/
        urlBuilder.append("&" + URLEncoder.encode("endCreateDt","UTF-8") + "=" + URLEncoder.encode(endDt, "UTF-8")); /*검색할 생성일 범위의 종료*/
        
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
        return resultMap;
    }
    
    @GetMapping("/api/coronaSidoInfo/{date}")
    public Map<String, Object> getCoronaSidoInfo(@PathVariable String date) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        if(date.equals("today")){
            List<CoronaSidoInfoVO> list = service.selectTodayCoronaSidoInfo();
            resultMap.put("status", true);
            resultMap.put("data", list);
        }
        else {
            List<CoronaSidoInfoVO> list = service.selectCoronaSidoInfo(date);
            resultMap.put("status", true);
            resultMap.put("data", list);
        }
        return resultMap;
    }

    // http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19GenAgeCaseInfJson
    // ?serviceKey=3CID6KRU4kjF4jvHanoFBLwycg6Htt86aVfgEOgBmAecshZIcO5EC9UM9FhVGwAX2Zf%2B%2FrxgsJeUfled1zNS0w%3D%3D
    // &pageNo=1&numOfRows=10&startCreateDt=20200310&endCreateDt=20200414
    @GetMapping("/api/corona/age")
    public Map<String, Object> getCoronaInfoAge(
        @RequestParam String startDt, @RequestParam String endDt
    ) throws Exception {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        StringBuilder urlBuilder = new StringBuilder("http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19GenAgeCaseInfJson"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=3CID6KRU4kjF4jvHanoFBLwycg6Htt86aVfgEOgBmAecshZIcO5EC9UM9FhVGwAX2Zf%2B%2FrxgsJeUfled1zNS0w%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("100000", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("startCreateDt","UTF-8") + "=" + URLEncoder.encode(startDt, "UTF-8")); /*검색할 생성일 범위의 시작*/
        urlBuilder.append("&" + URLEncoder.encode("endCreateDt","UTF-8") + "=" + URLEncoder.encode(endDt, "UTF-8")); /*검색할 생성일 범위의 종료*/
        
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
        return resultMap;
    }

    @GetMapping("/api/coronaAgeInfo/{date}")
    public Map<String, Object> getCoronaAgeInfo(@PathVariable String date) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        if(date.equals("today")){
            List<CoronaAgeInfoVO> list = service.selectTodayCoronaAgeInfo();
            resultMap.put("status", true);
            resultMap.put("data", list);
        }
        else {
            List<CoronaAgeInfoVO> list = service.selectCoronaAgeInfo(date);
            resultMap.put("status", true);
            resultMap.put("data", list);
        }
        return resultMap;
    }
}
