package com.example.weblab;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

@WebServlet("/DisplayServletDB")
public class DisplayServletDB extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            //1.获取connection对象
            //加载配置文件
            // 使用类加载器加载配置文件
            InputStream input = getClass().getClassLoader().getResourceAsStream("druid.properties");
            Properties prop = new Properties();
            prop.load(input);

            //获取连接池对象
            DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);

            //获取数据库连接Connection
            Connection connection= dataSource.getConnection();

            // 查 visit 表
            String sqlCount = "select * from visit;";
            PreparedStatement psCount = connection.prepareStatement(sqlCount);
            ResultSet rs = psCount.executeQuery();

            int count = 0;
            if (rs.next()) {
                count = rs.getInt("count");
            }
            // 删除
            String sqlVisitDelete = "delete from visit;";
            PreparedStatement psDelete = connection.prepareStatement(sqlVisitDelete);
            psDelete.executeUpdate();
            // 更新
            String sqlUpdate = "insert into visit values(?);";
            PreparedStatement psUpdate = connection.prepareStatement(sqlUpdate);
            psUpdate.setInt(1, count + 1);
            psUpdate.executeUpdate();


            //查表info
            String sql1="select * from info;";
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
            ResultSet rs1=preparedStatement1.executeQuery();

            //查表skills
            String sql2="select * from skills;";
            PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
            ResultSet rs2=preparedStatement2.executeQuery();

            // 处理结果
            // 创建 ObjectMapper 对象，它负责将 Java 对象转换为 JSON 和反向转换
            ObjectMapper objectMapper = new ObjectMapper();

            // 遍历 ResultSet
            // 创建一个 ObjectNode 用于存储每一行的数据
            ObjectNode rowNode = objectMapper.createObjectNode();

            while (rs1.next()) {

                // 获取列的数据，并将数据放入 ObjectNode
                rowNode.put("phoneNumber", rs1.getString("phoneNumber"));
                rowNode.put("email", rs1.getString("email"));
                rowNode.put("description", rs1.getString("description"));
                //还要放一个技能列表["111","222"]这种格式的

                // 将当前行的 ObjectNode 添加到结果集的 ArrayNode 中
                //resultArray.add(rowNode);
            }
            // 创建一个 ArrayNode 用于存储skills数组
            ArrayNode skillsArray = objectMapper.createArrayNode();
            while(rs2.next()){
                skillsArray.add(rs2.getString("skill"));
            }
            //skills 数据放入 ObjectNode
            rowNode.put("skills",skillsArray);
            rowNode.put("count",count);

            // 将 objectNode 转换为 JSON 字符串
            String jsonString = rowNode.toString();

            System.out.println(jsonString);

            //设置响应的字符编码
            response.setCharacterEncoding("UTF-8");
            //设置响应的内容类型为JSON
            response.setContentType("application/json;charset=UTF-8");
            //向前端发送数据
            response.getWriter().write(jsonString);

            //7.释放资源
            rs1.close();
            preparedStatement1.close();
            rs2.close();
            preparedStatement2.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("处理有异常");
            e.printStackTrace();  // 打印异常信息，以便调试
            throw new RuntimeException(e);
        }
        }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        this.doGet(request, response);
    }
}
