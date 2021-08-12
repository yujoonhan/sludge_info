$(function(){
    $.ajax({
        type:"get",
        url:"/api/corona/age/today",
        success:function(r){
            let confArr = new Array();
            let confLabel = new Array();
            for(let i=0; i<r.data.length; i++){
                confArr.push(r.data[i].confCase);
                confLabel.push(r.data[i].gubun+"대");
            }
            let ageChart = new Chart($("#age_chart"), {
                type:"bar",
                options:{
                    responsive:false
                },
                data:{
                    labels:confLabel,
                    datasets:[
                        {
                            label:r.dt+"연령대 별 확진",
                            data:confArr,
                            backgroundColor:["rgba(255, 0, 0, 0.4)"]
                        }
                    ]
                }
            });
        }
    });

    $.ajax({
        type:"get",
        url:"/api/corona/gen/today",
        success:function(r){
            let confArr = new Array();
            let confLabel = new Array();
            for(let i=0; i<r.data.length; i++){
                confArr.push(r.data[i].confCase);
                confLabel.push(r.data[i].gubun);
            }
            let genChart = new Chart($("#gen_chart"), {
                type:"pie",
                options:{
                    responsive:false
                },
                data:{
                    labels:confLabel,
                    datasets:[
                        {
                            label:"성별 확진 비율",
                            data:confArr,
                            backgroundColor:["rgba(255, 0, 0, 0.4)", "rgba(0, 0, 255, 0.4)"]
                        }
                    ]
                }
            });
        }
    });

    $.ajax({
        type:"get",
        url:"/api/coronaInfo/today",
        success:function(r) {
            console.log(r);
            $("#accExamCnt").html(r.data.strAccExamCnt);
            $("#decideCnt").html(r.data.strDecideCnt);
            let ctx2 = $("#confirmed_chart");
            let confirmed_chart = new Chart(ctx2, {
                type:"pie",
                options:{
                    responsive:false
                },
                data:{
                    labels:["확진", "음성"],
                    datasets:[
                        {
                            label:"확진/음성",
                            data:[r.data.decideCnt, r.data.examCnt - r.data.decideCnt],
                            backgroundColor:["rgba(255, 0, 0, 0.4)", "rgba(0, 0, 255, 0.4)"]
                        }
                    ]
                }
            })
        }
    })

    $.ajax({
        type:"get",
        url:"/api/coronaSidoInfo/today",
        success:function(r) {
            console.log(r);
            let sidoName = new Array();
            let defCnt = new Array();
            
            for(let i=0; i<6; i++) {
                let tag = "<tbody class='region-tbody'></tbody>";
                $(".region_confirm_tbl").append(tag);
            }

            for(let i=0; i<r.data.length; i++) {
                let sido = r.data[i].gubun;
                let cnt = r.data[i].incDec;
                sidoName.push(sido);
                defCnt.push(cnt);

                let page = Math.floor(i/3);
                let tag =
                '<tr>'+
                    '<td>'+r.data[i].gubun+'</td>'+
                    '<td>'+r.data[i].defCnt+'</td>'+
                    '<td>'+r.data[i].incDec+' ▴</td>'+
                '</tr>'
                $(".region-tbody").eq(page).append(tag);
            }
            $(".region-tbody").eq(0).addClass("active");

            $("#region_next").click(function(){
                let currentPage = Number($(".current").html());
                currentPage++;
                if(currentPage > 6) currentPage = 6;
                $(".current").html(currentPage);
                $(".region-tbody").removeClass("active");
                $(".region-tbody").eq(currentPage-1).addClass("active");
            })
            $("#region_prev").click(function(){
                let currentPage = Number($(".current").html());
                currentPage--;
                if(currentPage < 1) currentPage = 1;
                $(".current").html(currentPage);
                $(".region-tbody").removeClass("active");
                $(".region-tbody").eq(currentPage-1).addClass("active");
            })

            let ctx = $("#regional_status");
            let regionalChart = new Chart(ctx, {
                type:'bar',
                options:{
                    responsive:false
                },
                data:{
                    labels:sidoName,
                    datasets:[{
                        label:"2021-08-09 신규확진",
                        data:defCnt,
                        backgroundColor:['rgba(255,30,30,0.7)']
                    }]
                }
            });
        }
    })


    // let ctx2 = $("#confirmed_chart");
    // let confirmed_chart = new Chart(ctx2, {
    //     type:"pie",
    //     options:{
    //         responsive:false
    //     },
    //     data:{
    //         labels:["확진", "음성"],
    //         datasets:[
    //             {
    //                 label:"확진/음성",
    //                 data:[100, 200],
    //                 backgroundColor:["rgba(255, 0, 0, 0.4)", "rgba(0, 0, 255, 0.4)"]
    //             }
    //         ]
    //     }
    // })

    $.ajax({
        type:"get",
        url:"/api/corona/vaccine/today",
        success:function(r){
            console.log(r);
            let vsidoName = new Array();
            let firstCnt = new Array();
            let secondCnt = new Array();
            for(let i=0; i<r.data.length; i++){

            }
            let ctx3 = $("#vaccine_chart");
            let vaccineChart = new Chart(ctx3, {
                type:'bar',
                options:{
                    responsive:false
                },
                data:{
                    labels:['서울', '경기', '대구', '인천', '부산', '경남', '경북', '충남', '강원', '대전', '충북', '광주', '울산', '전북', '전남', '제주', '세종'],
                    datasets:[{
                        label:"2021-08-09 1차 접종현황",
                        data:[415, 408, 86, 65, 123, 88, 30, 68, 24, 42, 39, 19, 25, 21, 14, 11, 1],
                        backgroundColor:['rgba(30,255,30,0.7)']
                    },
                    {
                        label:"2021-08-09 2차 접종현황",
                        data:[415, 408, 86, 65, 123, 88, 30, 68, 24, 42, 39, 19, 25, 21, 14, 11, 1],
                        backgroundColor:['rgba(30,30,255,0.7)']
                    }]
                }
            });
        }
    });
})