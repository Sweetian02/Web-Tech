package com.example.weblab;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Properties;

@WebServlet("/UpdateServletDB")
public class UpdateServletDB extends HttpServlet {
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
        String description = rootNode.get("description").asText();
        JsonNode skillsNode = rootNode.get("skills");
        String[] skills = objectMapper.treeToValue(skillsNode, String[].class);

        try {
            //写入数据库
            //1.获取connection对象
            //加载配置文件
            // 使用类加载器加载配置文件
            InputStream input = getClass().getClassLoader().getResourceAsStream("druid.properties");
            Properties prop = new Properties();
            prop.load(input);
            //获取连接池对象
            DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);

            //获取数据库连接Connection
            Connection connection = dataSource.getConnection();

            //Info删除
            String sqlInfoDelete = "delete from info;";
            PreparedStatement psInfoDelete = connection.prepareStatement(sqlInfoDelete);
            psInfoDelete.executeUpdate();

            //Info更新
            String sqlInfoInsert = "insert into info values(?,?,?);";
            PreparedStatement psInfoInsert = connection.prepareStatement(sqlInfoInsert);
            psInfoInsert.setString(1, phoneNumber);
            psInfoInsert.setString(2, email);
            psInfoInsert.setString(3, description);
            psInfoInsert.executeUpdate();

            //Skills删除
            String sqlSkillsDelete = "delete from skills;";
            PreparedStatement psSkillsDelete = connection.prepareStatement(sqlSkillsDelete);
            psSkillsDelete.executeUpdate();

            // Skills更新
            String sqlSkillsInsert = "INSERT INTO skills (skill) VALUES (?)";
            PreparedStatement psSkillsInsert = connection.prepareStatement(sqlSkillsInsert);
            // 遍历技能数组，执行插入操作
            for (String skill : skills) {
                psSkillsInsert.setString(1, skill);
                psSkillsInsert.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");//解决中文乱码
        this.doGet(request, response);
    }
}
