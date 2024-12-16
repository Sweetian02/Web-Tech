{
var xhr = new XMLHttpRequest();
xhr.onreadystatechange = function () {
    if (xhr.readyState === XMLHttpRequest.DONE) {
        if (xhr.status === 200) {
            var responseData = JSON.parse(xhr.responseText);//获取的数据是json格式的

            document.getElementById("phoneNumber").innerHTML = responseData.phoneNumber;
            document.getElementById("email").innerHTML = responseData.email;
            document.getElementById("description").innerHTML = responseData.description;
            document.getElementById("count").innerHTML="访问次数："+responseData.count;
            //技能列表
            // 将技能显示为列表项
            var skillsList = document.getElementById("skills");

            var lenthSkills = responseData.skills.length;//列表长度
            //遍历查出来的数据
            for (var i = 0; i <lenthSkills ; i++) {
                var skillItem = document.createElement("li");
                skillItem.textContent = responseData.skills[i];//<li>中加内容
                skillsList.appendChild(skillItem);//<ul>中加<li>
            }

        } else {
            console.error('Failed to load data');
        }
    }
};

//xhr.open('POST', 'data.json', true);//修改url可以找servlet
xhr.open('POST', '/WebLab_war_exploded/DisplayServletDB', true);

xhr.send();
}

function changeImage(){
    let headphoto=document.getElementById("headphoto");
    let fileInput = document.getElementById('fileInput');
    if (fileInput.files.length > 0 && fileInput.files[0].type.startsWith('image/')) {
        var reader = new FileReader();
        reader.onload = function (e) {
            headphoto.src = e.target.result;
        };
        reader.readAsDataURL(fileInput.files[0]);
    } else {
        alert('请选择有效的图片文件！');
    }

    // 清空文件输入框，以便下次选择同一文件触发 change 事件
    fileInput.value = null;
}

