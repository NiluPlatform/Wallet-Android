package tech.nilu.wallet;

import org.junit.Before;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;

public class MetabankTest {
    private static final String SAFEBOX_ADDRESS = "0x041a7a98cca24c0ef865223d19bbfc30b601f16e";
    private static final String METABANK_ADDRESS = "0x2236f6786e6c9a9525bea64f6ce69a42f5e99018";
    private static final String VOTE_HANDLER_ADDRESS = "0xa6d4dcc8c6d4a72385aeec63c87fd9a8430a3b9b";

    private Tuple4<Web3j, Credentials, BigInteger, BigInteger> params;

    private Tuple4<Web3j, Credentials, BigInteger, BigInteger> getParams() {
        return params;
    }

    @Before
    public void initParams() throws Exception {
        Web3j web3j = Web3Util.getWeb3j(true);
        Credentials credentials = Web3Util.getCredential(true);
        BigInteger gasLimit = new BigInteger("4000000");
        BigInteger gasPrice = new BigInteger("20000000000");
        try {
            gasPrice = web3j.ethGasPrice().send().getGasPrice();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.params = new Tuple4<>(web3j, credentials, gasPrice, gasLimit);
    }

    @Test
    public void safeboxBalance() throws Exception {
        Web3j web3j = params.getValue1();
        System.out.println(web3j.ethGetBalance(SAFEBOX_ADDRESS, DefaultBlockParameterName.LATEST).send().getBalance().toString());
    }

    @Test
    public void withdraw() throws Exception {
        Metabank metabank = metabank();
        System.out.println(metabank.withdraw(Convert.toWei("1", Convert.Unit.ETHER).toBigInteger()).send());
    }

    @Test
    public void deposit() throws Exception {
        Web3j web3j = params.getValue1();
        Credentials credentials = params.getValue2();
        RawTransaction tx = RawTransaction.createTransaction(web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).send().getTransactionCount(),
                params.getValue3(),
                params.getValue4(),
                METABANK_ADDRESS,
                Convert.toWei("1", Convert.Unit.ETHER).toBigInteger(),
                ""
        );
        byte[] signedMessage = TransactionEncoder.signMessage(tx, credentials);
        String txHash = web3j.ethSendRawTransaction(Numeric.toHexString(signedMessage)).send().getTransactionHash();
        System.out.println(new PollingTransactionReceiptProcessor(web3j, TransactionManager.DEFAULT_POLLING_FREQUENCY, TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH).waitForTransactionReceipt(txHash));
    }

    @Test
    public void initMetabank() throws Exception {
        Safebox safebox = Safebox.deploy(params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4()
        ).send();

        Metabank metabank = Metabank.deploy(params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4(),
                safebox.getContractAddress(),
                BigInteger.valueOf(2),
                BigInteger.valueOf(5).multiply(BigInteger.valueOf(10).pow(18)),
                BigInteger.valueOf(25),
                BigInteger.valueOf(0)
        ).send();

        SafeBoxVoteHandler voteHandler = SafeBoxVoteHandler.deploy(params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4(),
                safebox.getContractAddress(),
                BigInteger.valueOf(1)
        ).send();

        safebox.setAllowedAddress(metabank.getContractAddress()).send();
        safebox.setVoteHandler(voteHandler.getContractAddress()).send();
        System.out.println("Safebox meta address has been set");
        System.out.println("Safebox: " + safebox.getContractAddress());
        System.out.println("MetaBank: " + metabank.getContractAddress());
        System.out.println("Safebox Vote Handler: " + voteHandler.getContractAddress());
    }

    private Safebox safebox() {
        return Safebox.load(SAFEBOX_ADDRESS,
                params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4()
        );
    }

    private Metabank metabank() {
        return Metabank.load(METABANK_ADDRESS,
                params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4()
        );
    }

    private SafeBoxVoteHandler voteHandler() {
        return SafeBoxVoteHandler.load(VOTE_HANDLER_ADDRESS,
                params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4()
        );
    }
}
