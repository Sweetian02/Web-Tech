document.addEventListener("DOMContentLoaded", function () {
    var httpRequest;

    // 自动执行 makeRequest 函数
    makeRequest();

    // 设置定时器，每隔一段时间刷新数据
    setInterval(makeRequest, 30000); // 30,000 milliseconds = 30 seconds

    function makeRequest() {
        httpRequest = new XMLHttpRequest();

        if (!httpRequest) {
            alert('Giving up :( Cannot create an XMLHTTP instance');
            return false;
        }

        httpRequest.onreadystatechange = alertContents;
        httpRequest.open('GET', 'https://restapi.amap.com/v3/weather/weatherInfo?key=8fce3198c708a252e3a74d8b05a8639d&city=430100');
        httpRequest.send();
        document.querySelector("#weather-info").innerText = "天气加载中...";
    }

    function alertContents() {
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                //document.querySelector("#weather-info").innerText = httpRequest.responseText;
                let CityWeather = JSON.parse(httpRequest.responseText);
                if (CityWeather['status'] != 1) {
                    alert('获取信息失败！');
                    return;
                }
                let msg = `城市：${CityWeather.lives[0].city}\n天气：${CityWeather.lives[0].weather}\n气温：${CityWeather.lives[0].temperature}摄氏度`;
                document.querySelector("#weather-info").innerText = msg;//规定了显示天气的位置
            } else {
                alert('There was a problem with the request.');
            }
        }
    }
});
