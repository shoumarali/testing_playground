package com.techyourchance.mockitofundamentals.example7;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.techyourchance.mockitofundamentals.example7.authtoken.AuthTokenCache;
import com.techyourchance.mockitofundamentals.example7.eventbus.EventBusPoster;
import com.techyourchance.mockitofundamentals.example7.eventbus.LoggedInEvent;
import com.techyourchance.mockitofundamentals.example7.networking.LoginHttpEndpointSync;
import com.techyourchance.mockitofundamentals.example7.networking.NetworkErrorException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class LoginUseCaseSyncTest1 {

    LoginUseCaseSync SUT ;
    public static final String USER_NAME="ali";
    public static final String PASSWORD="aliali";
    public static final String AUTH_TOKEN ="authToken";
    @Mock
    LoginHttpEndpointSync mLoginHttpEndpointSyncMock;
    @Mock
    AuthTokenCache mAuthTokenCacheMock;
    @Mock
    EventBusPoster mEventBusPosterMock;


    @Before
    public void setUp() throws Exception {

        SUT = new LoginUseCaseSync(
                mLoginHttpEndpointSyncMock,
                mAuthTokenCacheMock,
                mEventBusPosterMock
        );
        success();
    }



    @Test
    public void loginSync_success_userNameAndPasswordPassedToEndPoint() throws NetworkErrorException {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.loginSync(USER_NAME,PASSWORD);
        Mockito.verify(mLoginHttpEndpointSyncMock, Mockito.times(1))
                .loginSync(ac.capture(), ac.capture());
        List<String> captures = ac.getAllValues();
        assertEquals(USER_NAME, captures.get(0));
        assertEquals(PASSWORD,captures.get(1));
    }

    @Test
    public void loginSync_success_authTokenCached() {
        ArgumentCaptor<String> ac =ArgumentCaptor.forClass(String.class);
        SUT.loginSync(USER_NAME,PASSWORD);
        Mockito.verify(mAuthTokenCacheMock).cacheAuthToken(ac.capture());
        List<String> capture = ac.getAllValues();
        assertEquals(AUTH_TOKEN, capture.get(0));
    }

    @Test
    public void loginSync_generalError_authTokenNotCached() throws NetworkErrorException {
        generalError();
        SUT.loginSync(USER_NAME, PASSWORD);
        Mockito.verifyNoMoreInteractions(mAuthTokenCacheMock);
    }

    @Test
    public void loginSync_authError_authTokenNotCached() throws Exception {
        authError();
        SUT.loginSync(USER_NAME,PASSWORD);
        Mockito.verifyNoMoreInteractions(mAuthTokenCacheMock);
    }

    @Test
    public void loginSync_serverError_authTokenNotCached() throws Exception {
        serverError();
        SUT.loginSync(USER_NAME,PASSWORD);
        Mockito.verifyNoMoreInteractions(mAuthTokenCacheMock);
    }

    @Test
    public void loginSync_success_loggedInEventPosted() throws Exception {
        ArgumentCaptor<Object> ac = ArgumentCaptor.forClass(Object.class);
        SUT.loginSync(USER_NAME, PASSWORD);
        verify(mEventBusPosterMock).postEvent(ac.capture());
        assertThat(ac.getValue(), is(instanceOf(LoggedInEvent.class)));
    }

    @Test
    public void loginSync_generalError_noInteractionWithEventBusPoster() throws Exception {
        generalError();
        SUT.loginSync(USER_NAME, PASSWORD);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void loginSync_authError_noInteractionWithEventBusPoster() throws Exception {
        authError();
        SUT.loginSync(USER_NAME, PASSWORD);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void loginSync_serverError_noInteractionWithEventBusPoster() throws Exception {
        serverError();
        SUT.loginSync(USER_NAME, PASSWORD);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    private void success() throws NetworkErrorException {
        Mockito.when(mLoginHttpEndpointSyncMock.loginSync(any(String.class), any(String.class)))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.SUCCESS, AUTH_TOKEN));
    }


    private void generalError() throws NetworkErrorException {
        Mockito.when(mLoginHttpEndpointSyncMock.loginSync(any(String.class), any(String.class)))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR, ""));
    }

    private void authError() throws Exception {
        when(mLoginHttpEndpointSyncMock.loginSync(any(String.class), any(String.class)))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.AUTH_ERROR, ""));
    }

    private void serverError() throws Exception {
        when(mLoginHttpEndpointSyncMock.loginSync(any(String.class), any(String.class)))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.SERVER_ERROR, ""));
    }
}