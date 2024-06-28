package com.techyourchance.testdrivendevelopment.example9;

import static org.mockito.ArgumentMatchers.any;

import com.techyourchance.testdrivendevelopment.example9.networking.AddToCartHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.example9.networking.CartItemScheme;
import com.techyourchance.testdrivendevelopment.example9.networking.NetworkErrorException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class addToCartUseCaseSyncTest1 {

    public final static String OFFER_ID = "offerId";
    public final static int AMOUNT = 4;


    AddToCartUseCaseSync SUT;

    @Mock
    private AddToCartHttpEndpointSync addToCartHttpEndpointSyncMock;

    @Before
    public void setUp() throws Exception {
        SUT = new AddToCartUseCaseSync(addToCartHttpEndpointSyncMock);
        success();
    }


    // correct parameters passed to the endpoint

    @Test
    public void addToCartSync_parametersPassedToEndPoint() throws NetworkErrorException {
        //Arrange
        ArgumentCaptor<CartItemScheme> ac = ArgumentCaptor.forClass(CartItemScheme.class);
        //Act
        SUT.addToCartSync(OFFER_ID,AMOUNT);
        //Assert
        Mockito.verify(addToCartHttpEndpointSyncMock, Mockito.times(1))
                .addToCartSync(ac.capture());
        Assert.assertEquals(ac.getValue().getOfferId(),OFFER_ID);
        Assert.assertEquals(ac.getValue().getAmount(),AMOUNT);
    }

    // endpoint success - success returned

    @Test
    public void addToCartSync_success_successReturned() {
        //Act
        AddToCartUseCaseSync.UseCaseResult result = SUT.addToCartSync(OFFER_ID,AMOUNT);
        //Assert
        Assert.assertSame(result, AddToCartUseCaseSync.UseCaseResult.SUCCESS);
    }
    
    // endpoint authError - failure returned

    @Test
    public void addToCartSync_authError_failureReturned() throws NetworkErrorException {
        //Arrange
        authError();
        //Act
        AddToCartUseCaseSync.UseCaseResult result = SUT.addToCartSync(OFFER_ID,AMOUNT);
        //Assert
        Assert.assertSame(result, AddToCartUseCaseSync.UseCaseResult.FAILURE);
    }

    // endpoint generalError - failure returned
    @Test
    public void addToCartSync_generalError_failureReturned() throws NetworkErrorException {
        //Arrange
        generalError();
        //Act
        AddToCartUseCaseSync.UseCaseResult result = SUT.addToCartSync(OFFER_ID,AMOUNT);
        //Assert
        Assert.assertSame(result, AddToCartUseCaseSync.UseCaseResult.GENERAL_ERROR);
    }


    // network error - network error returned
    @Test
    public void addToCartSync_networkError_networkErrorReturned() throws NetworkErrorException {
        //Arrange
        networkError();
        //Act
        AddToCartUseCaseSync.UseCaseResult result = SUT.addToCartSync(OFFER_ID,AMOUNT);
        //Assert
        Assert.assertSame(result, AddToCartUseCaseSync.UseCaseResult.NETWORK_ERROR);
    }


    private void authError() throws NetworkErrorException {
        Mockito.when(addToCartHttpEndpointSyncMock.addToCartSync(any(CartItemScheme.class)))
                .thenReturn(AddToCartHttpEndpointSync.EndpointResult.AUTH_ERROR);
    }
    private void generalError() throws NetworkErrorException {
        Mockito.when(addToCartHttpEndpointSyncMock.addToCartSync(any(CartItemScheme.class)))
                .thenReturn(AddToCartHttpEndpointSync.EndpointResult.GENERAL_ERROR);
    }

    private void networkError() throws NetworkErrorException {
        Mockito.when(addToCartHttpEndpointSyncMock.addToCartSync(any(CartItemScheme.class)))
                .thenThrow(new NetworkErrorException());
    }
    private void success() throws NetworkErrorException {
        Mockito.when(addToCartHttpEndpointSyncMock.addToCartSync(any(CartItemScheme.class)))
                .thenReturn(AddToCartHttpEndpointSync.EndpointResult.SUCCESS);
    }
}