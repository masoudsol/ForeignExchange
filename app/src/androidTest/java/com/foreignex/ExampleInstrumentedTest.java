package com.foreignex;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.foreignex.modules.viewmodels.ExchangeViewModel;
import com.foreignex.modules.views.ExchangeActivity;
import com.services.APIServices;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static android.content.Context.LOCATION_SERVICE;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private CountDownLatch lock = new CountDownLatch(1);


    @Rule
    public ActivityTestRule<ExchangeActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(ExchangeActivity.class);


    @Test
    public void requestEXRates() throws Exception{
        APIServices services =  new APIServices(mainActivityActivityTestRule.getActivity());

        services.getExchangeRate(new APIServices.CompletionListener() {
            @Override
            public void onCompletion(Boolean success, Exception error) {
                assertTrue(success);
                lock.countDown();
            }
        });

        lock.await(2000, TimeUnit.MILLISECONDS);
    }

}
