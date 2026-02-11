package com.wipro.bank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.wipro.bank.bean.TransferBean;
import com.wipro.bank.util.DBUtil;

public class BankDao {

    public int generateSequenceNumber() {

        int seqNumber = 0;
        Connection con = DBUtil.getDBConnection();
        String query = "SELECT transactionID_seq1.NEXTVAL FROM dual";

        try {
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                seqNumber = rs.getInt(1);
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seqNumber;
    }

    public boolean validateAccount(String accountNumber) {

        Connection connection = DBUtil.getDBConnection();
        String query = "SELECT 1 FROM ACCOUNT_TBL WHERE ACCOUNT_NUMBER = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();

            boolean exists = rs.next();

            rs.close();
            ps.close();
            connection.close();

            return exists;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public float findBalance(String accountNumber) {

        float balance = -1;
        Connection connection = DBUtil.getDBConnection();
        String query = "SELECT BALANCE FROM ACCOUNT_TBL WHERE ACCOUNT_NUMBER = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                balance = rs.getFloat(1);
            }

            rs.close();
            ps.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    public boolean updateBalance(String accountNumber, float newBalance) {

        Connection connection = DBUtil.getDBConnection();
        String query = "UPDATE ACCOUNT_TBL SET BALANCE = ? WHERE ACCOUNT_NUMBER = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setFloat(1, newBalance);
            ps.setString(2, accountNumber);

            int rows = ps.executeUpdate();

            ps.close();
            connection.close();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean transferMoney(TransferBean tb) {

        boolean status = false;
        Connection con = null;

        try {
            con = DBUtil.getDBConnection();
            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO TRANSFER_TBL " +
                "(TRANSACTION_ID, ACCOUNT_NUMBER, BENEFICIARY_ACCOUNT_NUMBER, TRANSACTION_DATE, TRANSACTION_AMOUNT) " +
                "VALUES (transfer_seq.NEXTVAL, ?, ?, ?, ?)"
            );

            ps.setString(1, tb.getFromAccountNumber());
            ps.setString(2, tb.getToAccountNumber());
            ps.setDate(3, new java.sql.Date(tb.getDateOfTransaction().getTime()));
            ps.setFloat(4, tb.getAmount());

            ps.executeUpdate();
            con.commit();

            status = true;

            ps.close();
            con.close();

        } catch (Exception e) {
            try {
                if (con != null) con.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        return status;
    }
}
