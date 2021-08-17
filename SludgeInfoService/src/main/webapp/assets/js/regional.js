$.datepicker.setDefaults({
    dateFormat: 'yy-mm-dd',
    prevText: '이전 달',
    nextText: '다음 달',
    monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
    monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
    dayNames: ['일', '월', '화', '수', '목', '금', '토'],
    dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
    dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
    showMonthAfterYear: true,
    yearSuffix: '년'
});

$(function(){
    var coronaWeeksChart = new Chart($("#accDecideChart"), {
        type:"line",
        options:{
            responsive:false,
            // showValue:{
            //     fontSize:12
            // }
            // scales:{
            //     y:{
            //         min:0,
            //         max:100000
            //     }
            // }
        },
        data:{
            labels:null,
            datasets:[{
                label:'코로나 누적 확진',
                data:null,
                backgroundColor:[
                    'rgba(30, 30, 255, 0.7)'
                ]
            }]
        }
    })

    var vaccineWeeksChart = new Chart($("#accVaccineChart"), {
        type:"line",
        options:{
            responsive:false,
        },
        data:{
            labels:null,
            datasets:null
        }
    })

    $("#date").datepicker();
    $("#date").datepicker("setDate", new Date());
    $("#date").change(function(){
        // console.log($(this).val());
        let region = $("#region_select").find("option:selected").val();
        let date = $("#date").val();
        getCoronaSidoInfo(region, date);
        getCoronaVaccineInfo(region, date);
    })

    $("#region_select").change(function(){
        let region = $("#region_select").find("option:selected").val();
        let date = $("#date").val();
        getCoronaSidoInfo(region, date);
        getCoronaVaccineInfo(region, date);
    });

    getCoronaSidoInfo("서울")
    getCoronaVaccineInfo("서울")

    function getCoronaSidoInfo(sido, date){
        let url = "http://localhost:8077/api/regional?region="+sido;
        if(date != undefined && date != null && date !='') {
            url += "&date="+date;
        }
        $.ajax({
            type:"get",
            url:url,
            success:function(r){
                console.log(r);
                if(r.coronaWeeksList != null){
                    let coronaWeeksLabel = new Array();
                    let coronaWeeksData = new Array();
                    for(let i=0; i<r.coronaWeeksList.length; i++){
                        coronaWeeksLabel.push(r.coronaWeeksList[i].date);
                        coronaWeeksData.push(r.coronaWeeksList[i].defCnt);
                    }
                    coronaWeeksChart.data.datasets = new Array();   // 데이터 셋 초기화
                    coronaWeeksChart.data.labels = coronaWeeksLabel;    // 레이블 교체
                    // 데이터 셋 추가
                    coronaWeeksChart.data.datasets.push({
                        label:'코로나 누적 확진', data:coronaWeeksData,
                        backgroundColor:['rgba(30, 30, 255, 0.7)']
                    });
                    // 차트 업데이트
                    coronaWeeksChart.update();
                }

                if(r.vaccineWeekList != null){
                    let coronaWeeksLabel = new Array();
                    let coronaWeeksData1 = new Array();
                    let coronaWeeksData2 = new Array();
                    for(let i=0; i<r.vaccineWeekList.length; i++){
                        coronaWeeksLabel.push(r.vaccineWeekList[i].date);
                        coronaWeeksData1.push(r.vaccineWeekList[i].accFirstCnt);
                        coronaWeeksData2.push(r.vaccineWeekList[i].accSecondCnt);
                    }
                    vaccineWeeksChart.data.datasets = new Array();   
                    vaccineWeeksChart.data.labels = coronaWeeksLabel;    
                    vaccineWeeksChart.data.datasets.push({
                        label:'백신 1차 접종', data:coronaWeeksData1,
                        backgroundColor:['rgba(255, 30, 30, 0.7)']
                    });
                    vaccineWeeksChart.data.datasets.push({
                        label:'백신 2차 접종', data:coronaWeeksData2,
                        backgroundColor:['rgba(30, 30, 255, 0.7)']
                    });
                    vaccineWeeksChart.update();
                }

                if(r.dangerAge == null){
                    $("#dangerAge").html("-");
                }
                else {
                    $("#dangerAge").html(r.dangerAge+"대");
                }
                if(r.data == null){
                    $("#accDecideCnt").html("-");
                    $("#newDecideCnt").html("-");
                    $("#isolateCnt").html("-");
                    $("#clearIsolateCnt").html("-");
                    $("#covidDanger span").css("display", "none");
                    $("#covidDanger span").eq(0).css("display", "inline").css("color", "#66ff99");
                    return;
                }
                $("#accDecideCnt").html(r.data.defCnt);
                $("#newDecideCnt").html(r.data.incDec);
                $("#isolateCnt").html(r.data.isolIngCnt);
                $("#clearIsolateCnt").html(r.data.isolClearCnt);
                $("#covidDanger span").css("display", "none");
                let danger = r.data.incDec + r.data.diff;
                if(danger >= 200) {
                    $("#covidDanger span").eq(3).css("display", "inline").css("color", "#ff0000");
                }
                else if(danger >= 100) {
                    $("#covidDanger span").eq(2).css("display", "inline").css("color", "#ffcc66");
                }
                else if(danger >= 10) {
                    $("#covidDanger span").eq(1).css("display", "inline").css("color", "#ffcc66");
                }
                else {
                    $("#covidDanger span").eq(0).css("display", "inline").css("color", "#66ff99");
                }
            }
        })
    }

    function getCoronaVaccineInfo(sido, date) {
        let url = "/api/regional/vaccine?region="+sido;
        if(date != undefined && date != null && date != '') {
            url += "&date="+date;
        }
        $.ajax({
            type:"get",
            url:url,
            success:function(r){
                console.log(r);
                if(r.status == false){
                    $("#vaccineFirstCnt").html("-");
                    $("#vaccineSecondCnt").html("-");
                    return;
                }
                $("#vaccineFirstCnt").html(r.formattedFirstCnt);
                $("#vaccineSecondCnt").html(r.formattedSecondCnt);
            }
        })
    }
})