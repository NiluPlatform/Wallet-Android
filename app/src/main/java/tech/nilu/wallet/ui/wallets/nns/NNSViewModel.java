package tech.nilu.wallet.ui.wallets.nns;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple3;

import java.math.BigInteger;

import javax.inject.Inject;

import tech.nilu.wallet.model.LiveResponse;
import tech.nilu.wallet.repository.NNSRepository;

public class NNSViewModel extends ViewModel {
    private final NNSRepository nnsRepository;

    private final MutableLiveData<Tuple2<Long, String>> queryData = new MutableLiveData<>();
    public final LiveData<LiveResponse<Tuple2<String, String>>> query = Transformations.switchMap(queryData,
            new Function<Tuple2<Long, String>, LiveData<LiveResponse<Tuple2<String, String>>>>() {
                @Override
                public LiveData<LiveResponse<Tuple2<String, String>>> apply(Tuple2<Long, String> input) {
                    return nnsRepository.queryDomains(input.getValue1(), input.getValue2());
                }
            }
    );

    private final MutableLiveData<Long> addressData = new MutableLiveData<>();
    public final LiveData<LiveResponse<String>> address = Transformations.switchMap(addressData,
            new Function<Long, LiveData<LiveResponse<String>>>() {
                @Override
                public LiveData<LiveResponse<String>> apply(Long input) {
                    return nnsRepository.reverseResolution(input);
                }
            }
    );

    private final MutableLiveData<Tuple3<Long, String, String>> registerDomainData = new MutableLiveData<>();
    public final LiveData<LiveResponse<Boolean>> registerDomain = Transformations.switchMap(registerDomainData,
            new Function<Tuple3<Long, String, String>, LiveData<LiveResponse<Boolean>>>() {
                @Override
                public LiveData<LiveResponse<Boolean>> apply(Tuple3<Long, String, String> input) {
                    return nnsRepository.registerDomain(input.getValue1(), input.getValue2(), input.getValue3());
                }
            }
    );

    private final MutableLiveData<Tuple2<Long, String>> releaseDomainData = new MutableLiveData();
    public final LiveData<LiveResponse<Boolean>> releaseDomain = Transformations.switchMap(releaseDomainData,
            new Function<Tuple2<Long, String>, LiveData<LiveResponse<Boolean>>>() {
                @Override
                public LiveData<LiveResponse<Boolean>> apply(Tuple2<Long, String> input) {
                    return nnsRepository.releaseDomain(input.getValue1(), input.getValue2());
                }
            }
    );

    @Inject
    public NNSViewModel(NNSRepository nnsRepository) {
        this.nnsRepository = nnsRepository;
    }

    public LiveData<LiveResponse<BigInteger>> getGasPrice() {
        return nnsRepository.getGasPrice();
    }

    public String getDomain() {
        return queryData.getValue() != null ? queryData.getValue().getValue2().split("\\.")[0] : "";
    }

    public void setQueryData(long walletId, String query) {
        this.queryData.setValue(new Tuple2<>(walletId, query));
    }

    public void setRegisterDomainData(long walletId, String subnode, String rootNode) {
        this.registerDomainData.setValue(new Tuple3<>(walletId, subnode, rootNode));
    }

    public void setReleaseDomainData(long walletId, String domainToRelease) {
        this.releaseDomainData.setValue(new Tuple2<>(walletId, domainToRelease));
    }

    public void setAddressData(long walletId) {
        this.addressData.setValue(walletId);
    }
}
