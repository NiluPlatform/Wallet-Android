package tech.nilu.wallet.model;

import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import tech.nilu.wallet.db.entity.NiluTransaction;

/**
 * Created by root on 2/4/18.
 */

public class TransactionDetails {
    private final Transaction transaction;
    private final TransactionReceipt receipt;
    private final NiluTransaction niluTransaction;

    public TransactionDetails(Transaction transaction, TransactionReceipt receipt, NiluTransaction niluTransaction) {
        this.transaction = transaction;
        this.receipt = receipt;
        this.niluTransaction = niluTransaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public TransactionReceipt getReceipt() {
        return receipt;
    }

    public NiluTransaction getNiluTransaction() {
        return niluTransaction;
    }
}
