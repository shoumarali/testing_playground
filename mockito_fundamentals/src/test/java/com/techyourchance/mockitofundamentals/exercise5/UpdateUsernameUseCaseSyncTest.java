package com.techyourchance.mockitofundamentals.exercise5;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;

import com.techyourchance.mockitofundamentals.example7.networking.LoginHttpEndpointSync;
import com.techyourchance.mockitofundamentals.exercise5.eventbus.EventBusPoster;
import com.techyourchance.mockitofundamentals.exercise5.networking.NetworkErrorException;
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync;
import com.techyourchance.mockitofundamentals.exercise5.users.User;
import com.techyourchance.mockitofundamentals.exercise5.users.UsersCache;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
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
    public void updateUserNameSync_generalError_userNotCached() {
        //Arrange
        //Act
        //Assert
    }

    @Test
    public void updateUserNameSync_authError_userNotCached() {
        //Arrange
        //Act
        //Assert
    }

    @Test
    public void updateUserNameSync_serverError_userNotCached() {
        //Arrange
        //Act
        //Assert
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