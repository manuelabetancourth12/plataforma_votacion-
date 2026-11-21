package com.voting.infrastructure.websocket;

import org.junit.jupiter.api.Test;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import static org.mockito.Mockito.*;

class GlobalWebSocketHandlerTest {

    @Test
    void givenOpenSession_whenBroadcast_thenSendTextMessage() throws Exception {
        // Arrange
        GlobalWebSocketHandler handler = new GlobalWebSocketHandler();
        WebSocketSession sessionMock = mock(WebSocketSession.class);
        when(sessionMock.isOpen()).thenReturn(true);
        handler.afterConnectionEstablished(sessionMock);

        // Act
        handler.broadcast("{\"total\":5}");

        // Assert
        verify(sessionMock, times(1)).sendMessage(new TextMessage("{\"total\":5}"));
    }
}
