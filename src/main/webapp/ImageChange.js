// 打开文件资源管理器
function openFileExplorer() {
    document.getElementById("fileInput").click();
}

// 显示用户选择的图片
function displaySelectedImage() {
    var fileInput = document.getElementById("fileInput");
    var dynamicImage = document.getElementById("dynamicImage");

    // 确保有选择文件，并且文件是图片类型
    if (fileInput.files.length > 0 && fileInput.files[0].type.startsWith("image/")) {
        var selectedImage = fileInput.files[0];

        // 通过 FileReader 读取图片数据
        var reader = new FileReader();
        reader.onload = function (e) {
            dynamicImage.src = e.target.result;
        };
        reader.readAsDataURL(selectedImage);
    } else {
        alert("请选择有效的图片文件！");
    }

    // 清空文件输入框，以便下次选择同一文件触发 change 事件
    fileInput.value = null;
}