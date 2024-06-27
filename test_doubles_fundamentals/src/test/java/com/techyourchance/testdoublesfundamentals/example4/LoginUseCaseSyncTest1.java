package com.techyourchance.testdoublesfundamentals.example4;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import com.techyourchance.testdoublesfundamentals.example4.authtoken.AuthTokenCache;
import com.techyourchance.testdoublesfundamentals.example4.eventbus.EventBusPoster;
import com.techyourchance.testdoublesfundamentals.example4.eventbus.LoggedInEvent;
import com.techyourchance.testdoublesfundamentals.example4.networking.LoginHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class LoginUseCaseSyncTest1 {


    public final String USERNAME="username";
    public final String PASSWORD="password";

    public final static String AUTH_TOKEN ="AuthToken";

    LoginUseCaseSync SUT;
    LoginHttpEndpointSyncTd mLoginHttpEndpointSyncTd;
    AuthTokenCacheTd mAuthTokenCacheTd;
    EventBusPosterTd mEventBusPosterTd;


    @Before
    public void setUp() throws Exception {
        mLoginHttpEndpointSyncTd = new LoginHttpEndpointSyncTd();
        mAuthTokenCacheTd = new AuthTokenCacheTd();
        mEventBusPosterTd = new EventBusPosterTd();


        SUT = new LoginUseCaseSync(
                mLoginHttpEndpointSyncTd,
                mAuthTokenCacheTd,
                mEventBusPosterTd
        );
    }

    // username and password passed to endpoint
    @Test
    public void loginSync_success_usernameAndPasswordPassedToEndpoint() {
        SUT.loginSync(USERNAME,PASSWORD);
        Assert.assertEquals(mLoginHttpEndpointSyncTd.mUsername, USERNAME);
        Assert.assertEquals(mLoginHttpEndpointSyncTd.mPassword, PASSWORD);
    }

    // if login succeeds auth token must be cached
    @Test
    public void loginSync_success_authTokenMustBeCached() {
        SUT.loginSync(USERNAME,PASSWORD);
        Assert.assertEquals(mAuthTokenCacheTd.getAuthToken(), AUTH_TOKEN);
    }

    // if login fails auth token must not be changed
    @Test
    public void loginSync_generalError_authTokenNotCached() {
        mLoginHttpEndpointSyncTd.mIsGeneralError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        Assert.assertEquals(mAuthTokenCacheTd.getAuthToken(),"");
    }
    @Test
    public void loginSync_AuthError_authTokenNotCached() {
        mLoginHttpEndpointSyncTd.mIsAuthError=true;
        SUT.loginSync(USERNAME, PASSWORD);
        Assert.assertEquals(mAuthTokenCacheTd.getAuthToken(), "");
    }

    @Test
    public void loginSync_serverError_authTokenNotCached() {
        mLoginHttpEndpointSyncTd.mIsServerError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        Assert.assertEquals(mAuthTokenCacheTd.getAuthToken(), "");
    }

    // if login succeeds - login event posted to event bus
    @Test
    public void loginSync_success_loginEventPosted() {
        SUT.loginSync(USERNAME,PASSWORD);
        assertTrue(mEventBusPosterTd.mEvent instanceof LoggedInEvent);
    }

    // if login failed - no login event posted
    @Test
    public void loginSync_generalError_noInteractionWithEventBusPoster() {
        mLoginHttpEndpointSyncTd.mIsGeneralError=true;
        SUT.loginSync(USERNAME,PASSWORD);
        assertEquals(0, mEventBusPosterTd.interActionCount);
    }

    @Test
    public void loginSync_authError_noInteractionWithEventBusPoster() {
        mLoginHttpEndpointSyncTd.mIsAuthError = true;
        SUT.loginSync(USERNAME,PASSWORD);
        assertEquals(0,mEventBusPosterTd.interActionCount);
    }
    @Test
    public void loginSync_serverError_noInteractionWithEventBusPoster() {
        mLoginHttpEndpointSyncTd.mIsServerError = true;
        SUT.loginSync(USERNAME,PASSWORD);
        assertEquals(0,mEventBusPosterTd.interActionCount);
    }
    // if login succeeds- success returns
    @Test
    public void loginSync_success_successReturned() {
        LoginUseCaseSync.UseCaseResult result =  SUT.loginSync(USERNAME,PASSWORD);
        assertEquals(result, LoginUseCaseSync.UseCaseResult.SUCCESS);
    }

    // if fails fails returns
    @Test
    public void loginSync_serverError_failureReturned() {
        mLoginHttpEndpointSyncTd.mIsServerError = true;
        LoginUseCaseSync.UseCaseResult result =  SUT.loginSync(USERNAME,PASSWORD);
        assertEquals(result, LoginUseCaseSync.UseCaseResult.FAILURE);
    }
    @Test
    public void loginSync_authError_failureReturned() {
        mLoginHttpEndpointSyncTd.mIsAuthError = true;
        LoginUseCaseSync.UseCaseResult result =  SUT.loginSync(USERNAME,PASSWORD);
        assertEquals(result, LoginUseCaseSync.UseCaseResult.FAILURE);
    }
    @Test
    public void loginSync_generalError_failureReturned() {
        mLoginHttpEndpointSyncTd.mIsGeneralError = true;
        LoginUseCaseSync.UseCaseResult result =  SUT.loginSync(USERNAME,PASSWORD);
        assertEquals(result, LoginUseCaseSync.UseCaseResult.FAILURE);
    }

    // network - network error returned
    @Test
    public void loginSync_networkError_networkErrorReturned() {
        mLoginHttpEndpointSyncTd.mIsNetworkError = true;
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME,PASSWORD);
        assertEquals(result, LoginUseCaseSync.UseCaseResult.NETWORK_ERROR);
    }
    private static class LoginHttpEndpointSyncTd implements LoginHttpEndpointSync{
        public String mUsername="";
        private String mPassword="";
        public boolean mIsGeneralError;
        public boolean mIsAuthError;
        public boolean mIsServerError;
        public boolean mIsNetworkError;

        @Override
        public EndpointResult loginSync(String username, String password) throws NetworkErrorException {
            mUsername = username;
            mPassword = password;

            if(mIsGeneralError){
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR,"");
            }else if(mIsAuthError){
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR,"");
            }else if(mIsServerError){
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, "");
            }else if(mIsNetworkError){
                throw new NetworkErrorException();
            }
            else {
                return new EndpointResult(EndpointResultStatus.SUCCESS, AUTH_TOKEN);
            }
        }
    }




    private static class AuthTokenCacheTd implements AuthTokenCache {

        String mAuthToken= "";
        @Override
        public void cacheAuthToken(String authToken) {
            mAuthToken = authToken;
        }

        @Override
        public String getAuthToken() {
            return mAuthToken;
        }
    }

    private static class EventBusPosterTd implements EventBusPoster{
        public Object mEvent;
        public int interActionCount = 0;
        @Override
        public void postEvent(Object event) {
            mEvent = event;
            interActionCount++;
        }
    }
}