package com.kce.book.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.kce.book.bean.AuthorBean;
import com.kce.book.util.DBUtil;

public class AuthorDAO {
	public AuthorBean getAuthor(int authorCode) {
		Connection connection=DBUtil.getDBConnection();
		String query= "select * FROM Author_Tbl1 WHERE Author_code=?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, authorCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
            	AuthorBean authorBean=new AuthorBean();
            	authorBean.setAuthorCode(rs.getInt("Author_code"));
            	authorBean.setAuthorName(rs.getString("Author_name"));
            	authorBean.setContactNo(rs.getLong("Contact_no"));
            	return authorBean;
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public AuthorBean getAuthor(String authorName) {
		Connection connection=DBUtil.getDBConnection();
		String query= "SELECT * FROM Author_Tbl1 WHERE Author_name=?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, authorName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
            	AuthorBean authorBean=new AuthorBean();
            	authorBean.setAuthorCode(rs.getInt("Author_code"));
            	authorBean.setAuthorName(rs.getString("Author_name"));
            	authorBean.setContactNo(rs.getLong("Contact_no"));
            	return authorBean;
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}