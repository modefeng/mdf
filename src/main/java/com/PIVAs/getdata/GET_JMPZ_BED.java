package com.PIVAs.getdata;

import com.PIVAs.dbconnection.DatabaseConnection;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GET_JMPZ_BED {
    Connection conn=null;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    Document document=null;
    private  String  seqId,sourceSystem,messageId;
    public String GET_JMPZ_BED(Document requestxml){
        try {
            conn = DatabaseConnection.getConnection();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            return "数据库连接失败！";
        }
        Element root=requestxml.getRootElement();
        Element seqid=root.element("Body").element("SEQID");
        //获取入参的SEQID节点的值
        seqId=replaceNullString(seqid.getText());
        Element sourcesystem=root.element("Header").element("SourceSystem");
        //获取入参SourceSystem的节点的值
        sourceSystem=replaceNullString(sourcesystem.getText());
        Element messageid=root.element("Header").element("MessageID");
        //获取入参MessageID的值
        messageId=replaceNullString(messageid.getText());
        String sql="select a.科室id,a.病人id,a.主页id,b.入院病床,c.名称 from 在院病人 a, " +
                "病案主页 b,床位状况记录 d, 床位等级 c where a.病人id=b.病人id and a.主页id=b.主页id " +
                "and a.病人id=d.病人id and c.序号=d.等级id and  a.病人id=87612";
        try {
            document = DocumentHelper.createDocument();
            document.setXMLEncoding("utf-8");
            preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            Element Request = document.addElement("Request");
            Element Header = Request.addElement("Header");
            Element SourceSystem = Header.addElement("SourceSystem");
            SourceSystem.setText(sourceSystem);
            Element MessageID = Header.addElement("MessageID");
            MessageID.setText(messageId);
            Element Body = Request.addElement("Body");
            Element CODE = Body.addElement("CODE");
            CODE.setText(replaceNullString("0"));
            Element MESSAGE = Body.addElement("MESSAGE");
            MESSAGE.setText("成功");
            Element SEQID = Body.addElement("SEQID");
            SEQID.setText(seqId);
            int rows = 0;
            while (resultSet.next()){
                rows++;
                Element Rows=Body.addElement("Rows");
                //病人id
                Element PATIENT_ID=Rows.addElement("PATIENT_ID");
                PATIENT_ID.addText(replaceNullString(resultSet.getString("病人id")));
                //本次住院标识
                Element VISIT_ID=Rows.addElement("VISIT_ID");
                VISIT_ID.addText(replaceNullString(resultSet.getString("主页id")));
                //部门编号
                Element DEPARTMENT_NO=Rows.addElement("DEPARTMENT_NO");
                DEPARTMENT_NO.addText(replaceNullString(resultSet.getString("科室id")));
                //床位
                Element BED=Rows.addElement("BED");
                BED.addText(replaceNullString(resultSet.getString("入院病床")));
                //床位名称
                Element BEDNAME=Rows.addElement("BEDNAME");
                BEDNAME.addText(replaceNullString(resultSet.getString("名称")));
            }
        }catch (Exception e){
            fail();
        }
        return document.asXML();
    }
    public  String replaceNullString(String str){
        if (str==null){
            return "";
        }
        else
            return str;
    }
    public void fail(){
        document= DocumentHelper.createDocument();
        document.setXMLEncoding("utf-8");
        Element Request=document.addElement("Request");
        Element Header=Request.addElement("Header");
        Element SourceSystem=Header.addElement("SourceSystem");
        SourceSystem.setText(sourceSystem);
        Element MessageID=Header.addElement("MessageID");
        MessageID.setText(messageId);
        Element Body=Request.addElement("Body");
        Element CODE=Body.addElement("CODE");
        CODE.setText("1");
        Element MESSAGE=Body.addElement("MESSAGE");
        MESSAGE.setText("失败");
        Element Rows=Body.addElement("Rows");
        //病人id
        Element PATIENT_ID=Rows.addElement("PATIENT_ID");
        PATIENT_ID.addText(replaceNullString(""));
        //本次住院标识
        Element VISIT_ID=Rows.addElement("VISIT_ID");
        VISIT_ID.addText(replaceNullString(""));
        //部门编号
        Element DEPARTMENT_NO=Rows.addElement("DEPARTMENT_NO");
        DEPARTMENT_NO.addText(replaceNullString(""));
        //床位
        Element BED=Rows.addElement("BED");
        BED.addText(replaceNullString(""));
        //床位名称
        Element BEDNAME=Rows.addElement("BEDNAME");
        BEDNAME.addText(replaceNullString(""));
    }
}
