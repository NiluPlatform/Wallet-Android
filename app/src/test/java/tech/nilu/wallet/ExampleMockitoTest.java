package tech.nilu.wallet;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExampleMockitoTest {
    @Mock
    Context context;

    @Test
    public void readStringFromContext() {
        when(context.getString(R.string.token_deployment_caution)).thenReturn("CAUTION: ANY ATTEMPTS TO INTERACT WITH THIS TOKEN BEFORE IT\\'S ACTIVATION MAY RESULT IN LOSING ASSETS.");
    }
}
