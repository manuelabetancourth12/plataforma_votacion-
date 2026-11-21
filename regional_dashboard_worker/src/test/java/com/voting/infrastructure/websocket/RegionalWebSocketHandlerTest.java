package com.voting.infrastructure.websocket;

import org.junit.jupiter.api.Test;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import static org.mockito.Mockito.*;

class RegionalWebSocketHandlerTest {

    @Test
    void givenOpenSession_whenBroadcast_thenSendRegionalTextMessage() throws Exception {
        // Arrange
        RegionalWebSocketHandler handler = new RegionalWebSocketHandler();
        WebSocketSession sessionMock = mock(WebSocketSession.class);
        when(sessionMock.isOpen()).thenReturn(true);
        handler.afterConnectionEstablished(sessionMock);

        // Act
        handler.broadcast("{\"region\":\"norte\"}");

        // Assert
        verify(sessionMock, times(1)).sendMessage(new TextMessage("{\"region\":\"norte\"}"));
    }
}
