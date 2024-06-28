package com.techyourchance.mockitofundamentals.exercise5;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.techyourchance.mockitofundamentals.example7.networking.LoginHttpEndpointSync;
import com.techyourchance.mockitofundamentals.exercise5.eventbus.EventBusPoster;
import com.techyourchance.mockitofundamentals.exercise5.eventbus.UserDetailsChangedEvent;
import com.techyourchance.mockitofundamentals.exercise5.networking.NetworkErrorException;
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync;
import com.techyourchance.mockitofundamentals.exercise5.users.User;
import com.techyourchance.mockitofundamentals.exercise5.users.UsersCache;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UpdateUsernameUseCaseSyncTest {


    UpdateUsernameUseCaseSync SUT;

    private final static String USERID = "1";
    private final static String USERNAME="ali";


    @Mock
    private UpdateUsernameHttpEndpointSync updateUsernameHttpEndpointSyncMock;
    @Mock
    private UsersCache usersCacheMock;
    @Mock
    private EventBusPoster eventBusPosterMock;

    @Before
    public void setUp() throws Exception {
        SUT = new UpdateUsernameUseCaseSync(
                updateUsernameHttpEndpointSyncMock,
                usersCacheMock,
                eventBusPosterMock
        );
        success();
    }

    @Test
    public void updateUserNameSync_success_userIdAndUserNamePassedForTheEndPoint()
            throws NetworkErrorException {
        //Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        //Act
        SUT.updateUsernameSync(USERID,USERNAME);
        // Assert
        Mockito.verify(updateUsernameHttpEndpointSyncMock,Mockito.times(1))
                .updateUsername(ac.capture(),ac.capture());
        List<String> captures = ac.getAllValues();
        assertEquals(captures.get(0), USERID);
        assertEquals(captures.get(1),USERNAME);
    }


    @Test
    public void updateUserNameSync_success_userCached() {
        //Arrange
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        //Act
        SUT.updateUsernameSync(USERID,USERNAME);
        //Assert
        Mockito.verify(usersCacheMock,Mockito.times(1))
                .cacheUser(ac.capture());
        User user = ac.getValue();
        assertEquals(user.getUserId(),USERID);
        assertEquals(user.getUsername(),USERNAME);
    }


    @Test
    public void updateUserNameSync_generalError_userNotCached() throws NetworkErrorException {
        //Arrange
        generalError();
        //Act
        SUT.updateUsernameSync(USERID,USERNAME);
        //Assert
        Mockito.verifyNoMoreInteractions(usersCacheMock);
    }

    @Test
    public void updateUserNameSync_authError_userNotCached() throws NetworkErrorException {
        //Arrange
        authError();
        //Act
        SUT.updateUsernameSync(USERID,USERNAME);
        //Assert
        Mockito.verifyNoMoreInteractions(usersCacheMock);
    }

    @Test
    public void updateUserNameSync_serverError_userNotCached() throws NetworkErrorException {
        //Arrange
        serverError();
        //Act
        SUT.updateUsernameSync(USERID,USERNAME);
        //Assert
        Mockito.verifyNoMoreInteractions(usersCacheMock);
    }


    @Test
    public void updateUserNameSync_success_loggedINEventePosted() {
        //Arrange
        ArgumentCaptor<Object> ac = ArgumentCaptor.forClass(Object.class);
        //Act
        SUT.updateUsernameSync(USERID,USERNAME);
        //Assert
        Mockito.verify(eventBusPosterMock,Mockito.times(1)).postEvent(ac.capture());
        assertThat(ac.getValue(), is(instanceOf(UserDetailsChangedEvent.class)));
    }

    @Test
    public void updateUsername_generalError_noInteractionWithEventBusPoster() throws Exception {
        generalError();
        SUT.updateUsernameSync(USERID, USERNAME);
        verifyNoMoreInteractions(eventBusPosterMock);
    }

    @Test
    public void updateUsername_authError_noInteractionWithEventBusPoster() throws Exception {
        authError();
        SUT.updateUsernameSync(USERID, USERNAME);
        verifyNoMoreInteractions(eventBusPosterMock);
    }

    @Test
    public void updateUsername_serverError_noInteractionWithEventBusPoster() throws Exception {
        serverError();
        SUT.updateUsernameSync(USERID, USERNAME);
        verifyNoMoreInteractions(eventBusPosterMock);
    }

    @Test
    public void updateUsername_success_successReturned() throws Exception {
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USERID, USERNAME);
        assertThat(result, is(UpdateUsernameUseCaseSync.UseCaseResult.SUCCESS));
    }

    @Test
    public void updateUsername_serverError_failureReturned() throws Exception {
        serverError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USERID, USERNAME);
        assertThat(result, is(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void updateUsername_authError_failureReturned() throws Exception {
        authError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USERID, USERNAME);
        assertThat(result, is(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void updateUsername_generalError_failureReturned() throws Exception {
        generalError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USERID, USERNAME);
        assertThat(result, is(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void updateUsername_networkError_networkErrorReturned() throws Exception {
        networkError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USERID, USERNAME);
        assertThat(result, is(UpdateUsernameUseCaseSync.UseCaseResult.NETWORK_ERROR));
    }

    private void networkError() throws Exception {
        doThrow(new NetworkErrorException())
                .when(updateUsernameHttpEndpointSyncMock).updateUsername(anyString(), anyString());
    }

    private void generalError() throws NetworkErrorException {
        Mockito.when(updateUsernameHttpEndpointSyncMock
                .updateUsername(anyString(),anyString()))
                .thenReturn(
                        new UpdateUsernameHttpEndpointSync.EndpointResult(
                                UpdateUsernameHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR,
                                "",
                                ""));
    }

    private void authError() throws NetworkErrorException{
        Mockito.when(updateUsernameHttpEndpointSyncMock
                .updateUsername(anyString(),anyString())
        ).thenReturn(
                new UpdateUsernameHttpEndpointSync.EndpointResult(
                        UpdateUsernameHttpEndpointSync.EndpointResultStatus.SERVER_ERROR,
                        ""
                        ,""
                )
        );
    }


    private void serverError() throws NetworkErrorException{
        Mockito.when(updateUsernameHttpEndpointSyncMock
                .updateUsername(anyString(),anyString()))
                .thenReturn(
                        new UpdateUsernameHttpEndpointSync.EndpointResult(
                                UpdateUsernameHttpEndpointSync.EndpointResultStatus.SERVER_ERROR,
                                "",
                                ""
                        )
                );
    }

    private void success() throws NetworkErrorException{
        Mockito.when(updateUsernameHttpEndpointSyncMock
                .updateUsername(anyString(),anyString()))
                .thenReturn(new UpdateUsernameHttpEndpointSync
                        .EndpointResult(
                                UpdateUsernameHttpEndpointSync.EndpointResultStatus.SUCCESS,
                        USERID,
                        USERNAME));
    }
}