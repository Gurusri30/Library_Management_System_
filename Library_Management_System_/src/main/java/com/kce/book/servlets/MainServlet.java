package com.kce.book.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.kce.book.bean.AuthorBean;
import com.kce.book.bean.BookBean;
import com.kce.book.dao.AuthorDAO;
import com.kce.book.service.Administrator;

@WebServlet("/MainServlet")
public class MainServlet extends HttpServlet {
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String operation=request.getParameter("operation");
		if(operation.equals("AddBook")) {
			String result=addBook(request);
			if(result.equals("SUCCESS")) {
				response.sendRedirect("menu.html");
			}
			else if(result.equals("INVALID")) {
				response.sendRedirect("Invalid.html");
			}
			else if(result.equals("FAILURE")) {
				response.sendRedirect("failure.html");
			}
		}
		else if(operation.equals("Search")) {
			String isbn=request.getParameter("isbn");
			BookBean bookbean=viewBook(isbn);
			if(bookbean==null) {
				response.sendRedirect("Invalid.html");
			}
			else {
				HttpSession session=request.getSession();
				session.setAttribute("book", bookbean);
				RequestDispatcher rd=request.getRequestDispatcher("ViewServlet");
				rd.forward(request, response);
			}
		}
	}
	
	public String addBook(HttpServletRequest request) {
		String isbn=request.getParameter("isbn");
		String bookName=request.getParameter("bookName");
		String bookType=request.getParameter("bookType");
		String authorName=request.getParameter("authorName");
		String cost=request.getParameter("cost");
		
		BookBean bookbean=new BookBean();
		bookbean.setIsbn(isbn);
		bookbean.setBookName(bookName);
		bookbean.setBookType(bookType.charAt(0));
		bookbean.setCost(Float.parseFloat(cost));
		bookbean.setAuthor(new AuthorDAO().getAuthor(authorName));
		
		String result=new Administrator().addBook(bookbean);
		return result;
	}
    public BookBean viewBook(String isbn) {
    	Administrator administrator=new Administrator();
    	return administrator.viewBook(isbn);
    }
}