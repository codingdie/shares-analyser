<!DOCTYPE html>
<html>
<head>

    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker3.min.css">


    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/jquery.cookie.js"></script>


    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="/js/echarts.min.js"></script>


    <script>
        function query(codes, beginDate, endDate) {
            var param = new Object();
            param.shareCodes = codes;
            param.beginDate = beginDate;
            param.endDate = endDate;
            $.cookie("shareCodes",codes.toString(), {
                path: "/", expires: 360
            })
            $.cookie("beginDate",beginDate, {
                path: "/", expires: 360
            })
            $.cookie("endDate",endDate, {
                path: "/", expires: 360
            })
            $("#tips").text("请稍后,正在分析...")
            $.ajax({
                type: "POST",
                url: "/query",
                data: JSON.stringify(param),
                dataType: "json",
                contentType: "application/json",
                success: function (data) {
                    if (data instanceof Array && data.length > 0) {
                        console.log(data)
                        $(".mychart").each(function () {
                            $(this).remove();
                        })
                        showCharts(data, "closingPrice", "收盘价");
                        showCharts(data, "openingPrice", "开盘价");
                        showCharts(data, "highestPrice", "最高价");
                        showCharts(data, "volume", "成交量");

                        $("#tips").text("分析成功!")

                    } else {
                        $("#tips").text("分析失败!请确定股票代码是否有误,或者联系开发者确定是否股票数据存在")

                        alert("请确定股票代码是否有误,或者联系开发者确定是否股票数据存在")
                    }
                }
            });

        }
        var showCharts = function (datas, key, title) {
            $("#01").remove();
            var main = $("#main").clone();
            $(main).width($(window).width() - 10)
            $(main).show();
            $(main).attr("class", "mychart");
            $("body").append(main)
            var myChart = echarts.init(main[0]);

            // 指定图表的配置项和数据
            var legendData = [];
            var xAxisData = [];
            var seriesData = [];
            datas[0].dayDatas.map(function (item) {
                xAxisData.push(item.date);
            });
            datas.map(function (item) {
                legendData.push(item.name);
                var tmp = [];
                item.dayDatas.map(function (item) {
                    tmp.push(item[key])
                });
                seriesData.push({
                    name: item.name,
                    type: 'line',
                    stack: '总量',
                    data: tmp
                })
            })

            var option = {
                title: {
                    text: title
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: legendData
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                toolbox: {
                    feature: {
                        saveAsImage: {}
                    }
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: xAxisData
                },
                yAxis: {
                    type: 'value'
                },
                series: seriesData
            };


            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        };
        $(document).ready(function () {
            $("#shareCodes").val($.cookie("shareCodes"))
            $("#beginDate").val($.cookie("beginDate"))
            $("#endDate").val($.cookie("endDate"))

            $(".datepicker").each(function () {
                $(this).datepicker({
                            format: 'yyyy-mm-dd'
                        }
                );
            })
        })
        function beginAnalyse() {
            if($("#shareCodes").val().length==0){
                alert("请输入股票代码!")
                return;
            }
            if($("#beginDate").val().length==0){
                alert("请输入开始时间!")
                return;
            }
            if($("#endDate").val().length==0){
                alert("请输入结束时间!")
                return;
            }
            var arr=[];
            $("#shareCodes").val().split(",").map(function (item) {
                arr.push(item.trim())
            })
            query(arr,$("#beginDate").val(),$("#endDate").val())

        }
    </script>
</head>
<body>
<div style="width:100%;padding: 10px 20px" class="form-inline">
    <div class="form-group">
        <label for="beginDate">起始日期</label>
        <input id="beginDate" type="text" style="width: 100px" class="form-control datepicker" placeholder="起始日期">
    </div>
    <div class="form-group">
        <label for="endDate">截止日期</label>
        <input id="endDate" type="text" style="width: 100px" class="form-control datepicker" placeholder="截止日期">
    </div>

    <div class="form-group">
        <label for="shareCodes">股票代码</label>
        <input id="shareCodes" type="text" style="width: 300px" class="form-control " style="min-width: 300px"
               placeholder="请填写需要分析的股票代码用英文,隔开">
    </div>


    <button type="button" onclick="beginAnalyse()" class="btn btn-default btn-sm">分析</button><span id="tips"></span>


</div>


<div id="main" style="width: 600px;height:400px;display: none;float: left"></div>

</body>
</html>