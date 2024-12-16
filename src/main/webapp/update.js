function loadData() {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                var responseData = JSON.parse(xhr.responseText);
                document.getElementById("phoneNumber").value = responseData.phoneNumber;
                document.getElementById("email").value = responseData.email;
                document.getElementById("description").value = responseData.description;

                // 技能列表
                var table = document.getElementById("table");

                //clear
                // 清空技能列表

                // 添加技能行
                for (var i = 0; i < responseData.skills.length; i++) {
                    addSkillRow(responseData.skills[i],i);
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

function addSkillRow(skill,i) {
    // 技能列表
    var table = document.getElementById("table");

    // 添加新技能行
    var newRow = table.insertRow(i+1);

    var newSkillCell = newRow.insertCell(0);
    newSkillCell.textContent = skill;
    newSkillCell.contentEditable = true;

    var deleteCell = newRow.insertCell(1);
    var deleteButton = document.createElement("button");
    deleteButton.textContent = "删除";
    deleteButton.onclick = function () {
        table.deleteRow(this.parentNode.parentNode.rowIndex);
    };
    deleteCell.appendChild(deleteButton);
}

// 添加空白行（放到最后）
function addBlankRow(){

    var table = document.getElementById("table");
    var newRow = table.insertRow();
    var skillCell = newRow.insertCell();
    skillCell.contentEditable = true;
    skillCell.id="newContent";//新内容
    var addButtonCell = newRow.insertCell(1);
    var addButton = document.createElement("button");
    addButton.textContent = "添加";
    addButton.onclick = function () {
        addRow(); // 传递table参数
    };
    addButtonCell.appendChild(addButton);
}

//添加新的一行
function addRow(){

    var table = document.getElementById("table");
    var newRow = table.insertRow(table.rows.length - 1);//倒数第二行插入新行
    var cell1 = newRow.insertCell(0);
    var cell2 = newRow.insertCell(1);
    var newContent = document.getElementById("newContent").innerText;
    cell1.innerHTML = '<td>' + newContent + '</td>';
    cell1.contentEditable = true;

    var deleteButton = document.createElement("button");
    deleteButton.textContent = "删除";
    deleteButton.onclick = function () {
        table.deleteRow(this.parentNode.parentNode.rowIndex);
    };
    cell2.appendChild(deleteButton);

    // 清空输入
    document.getElementById("newContent").innerText = "";
}

//更新
function update() {
    var xhr = new XMLHttpRequest();
    var data = {};

    // 获取电话号码
    data.phoneNumber = document.getElementById("phoneNumber").value;

    // 获取邮箱
    data.email = document.getElementById("email").value;

    // 获取经历描述
    data.description = document.getElementById("description").value;

    // 获取个人技能表格的数据
    data.skills = [];
    var table = document.getElementById("table");

    // 遍历表格的每一行，忽略第一行（表头）
    for (var i = 1; i < table.rows.length; i++) {
        var skillCell = table.rows[i].cells[0];
        var skillValue = skillCell.textContent.trim();
        if (skillValue !== "") {
            data.skills.push(skillValue);
        }
    }

    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                // 在这里处理响应，如果有需要的话
            } else {
                console.error('Failed to update data');
            }
        }
    };

    xhr.open('POST', '/WebLab_war_exploded/UpdateServletDB', true);
    //xhr.open('POST', '/WebLab_war_exploded/UpdateServlet', true);
    // 设置请求头，告诉服务器发送的是 JSON 数据
    xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');
    // 返回 JSON 格式的数据
    xhr.send(JSON.stringify(data));
    xhr.onreadystatechange=()=>{
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                alert("修改成功，请刷新主页查看最新信息！")
            }
        }
    }
}


// 调用加载数据的函数
loadData();
