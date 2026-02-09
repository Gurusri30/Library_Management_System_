package com.kce.book.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.kce.book.bean.BookBean;
import com.kce.book.util.DBUtil;

public class BookDAO {
	
	public int createBook(BookBean bookBean) {
		Connection connection=DBUtil.getDBConnection();
		String query= "INSERT into Book_Tbl1 values(?,?,?,?,?)";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, bookBean.getIsbn());
			ps.setString(2,bookBean.getBookName());
			ps.setInt(4,bookBean.getAuthor().getAuthorCode());
			ps.setString(3,String.valueOf(bookBean.getBookType()));
			ps.setFloat(5, bookBean.getCost());
			int row =ps.executeUpdate();
			if(row>0) {
				return 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public BookBean fetchBook(String isbn) {
		Connection connection=DBUtil.getDBConnection();
		String query= "select * from Book_Tbl1 where ISBN =?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1,isbn);
			ResultSet rs=ps.executeQuery();
			if(rs.next()) {
				BookBean bookBean=new BookBean();
				bookBean.setIsbn(rs.getString("ISBN"));
				bookBean.setBookName(rs.getString("Book_title"));
				bookBean.setBookType(rs.getString("Book_type").charAt(0));
				bookBean.setAuthor( new AuthorDAO().getAuthor(rs.getInt("Author_code")));
				bookBean.setCost(rs.getFloat("Book_cost"));
				return bookBean;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}