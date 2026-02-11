package com.wipro.bank.service;

import com.wipro.bank.bean.TransferBean;
import com.wipro.bank.dao.BankDao;
import com.wipro.bank.util.InsufficientFundsException;

public class BankService {

    private BankDao bankDAO = new BankDao();

   
    public String checkBalance(String accountNumber) {

        if (bankDAO.validateAccount(accountNumber)) {
            float balance = bankDAO.findBalance(accountNumber);
            return "BALANCE IS : " + balance;
        } else {
            return "ACCOUNT NUMBER IS INVALID";
        }
    }

  
    public String transfer(TransferBean transferBean) {

        if (transferBean == null) {
            return "INVALID INPUT";
        }

        String fromAcc = transferBean.getFromAccountNumber();
        String toAcc = transferBean.getToAccountNumber();
        float amount = transferBean.getAmount();

       
        if (!bankDAO.validateAccount(fromAcc) || !bankDAO.validateAccount(toAcc)) {
            return "INVALID ACCOUNT";
        }

        float fromBalance = bankDAO.findBalance(fromAcc);

        try {
         
            if (fromBalance < amount) {
                throw new InsufficientFundsException("INSUFFICIENT FUNDS");
            }

           
            bankDAO.updateBalance(fromAcc, fromBalance - amount);
            bankDAO.updateBalance(toAcc,
                    bankDAO.findBalance(toAcc) + amount);

        
            bankDAO.transferMoney(transferBean);

            return "SUCCESS";

        } catch (InsufficientFundsException e) {
            return e.getMessage();
        }
    }
}
