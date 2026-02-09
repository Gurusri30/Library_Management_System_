package com.kce.book.service;

import com.kce.book.bean.BookBean;
import com.kce.book.dao.BookDAO;

public class Administrator {

    public String addBook(BookBean bookBean) {

    	if(bookBean==null || bookBean.getBookName().isEmpty() ||bookBean.getCost()==0||bookBean.getBookType()!='G' && bookBean.getBookType()!='T')
			return "INVALID";
			if(bookBean.getIsbn().isEmpty() ||bookBean.getBookType()==' '||bookBean.getAuthor().getAuthorName().isEmpty())
			return "INVALID";
			

        int result = new BookDAO().createBook(bookBean);

        if (result == 1) {
            return "SUCCESS";
        }
        return "FAILURE";
    }

    public BookBean viewBook(String isbn) {
        BookDAO bookDao = new BookDAO();
        BookBean bookbean = bookDao.fetchBook(isbn);
        return bookbean;
    }
}
