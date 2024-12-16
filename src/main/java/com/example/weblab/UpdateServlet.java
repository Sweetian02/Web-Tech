package com.example.weblab;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

@WebServlet("/UpdateServlet")
public class UpdateServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 读取请求体中的 JSON 数据
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        String jsonString = sb.toString();

        // 解析 JSON 数据
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonString);

        // 从 JSON 数据中获取相应的值
        String phoneNumber = rootNode.get("phoneNumber").asText();
        String email = rootNode.get("email").asText();
        String describe = rootNode.get("describe").asText();
        JsonNode skillsNode = rootNode.get("skills");
        String[] skills = objectMapper.treeToValue(skillsNode, String[].class);


        //

        // 在现有数据中修改
        String jsonFilePath = getServletContext().getRealPath("/data.json");
        File jsonFile = new File(jsonFilePath);
        JsonNode existingData = objectMapper.readTree(jsonFile);
        ((ObjectNode) existingData).put("phoneNumber", phoneNumber);
        ((ObjectNode) existingData).put("email", email);
        ((ObjectNode) existingData).put("describe", describe);
        ArrayNode skillsArray = objectMapper.valueToTree(skills);
        ((ObjectNode) existingData).set("skills", skillsArray);

        // 将修改后的数据写回 JSON 文件
        objectMapper.writeValue(jsonFile, existingData);



    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");//解决中文乱码
        this.doGet(request, response);
    }
}
