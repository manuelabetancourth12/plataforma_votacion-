package com.voting.application;

import com.voting.domain.SecurityAlertPublisher;
import com.voting.domain.SecurityIncidentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class FraudDetectionServiceTest {

    private SecurityAlertPublisher alertPublisherMock;
    private SecurityIncidentRepository incidentRepositoryMock;
    private FraudDetectionService fraudDetectionService;

    @BeforeEach
    void setUp() {
        alertPublisherMock = mock(SecurityAlertPublisher.class);
        incidentRepositoryMock = mock(SecurityIncidentRepository.class);
        fraudDetectionService = new FraudDetectionService(alertPublisherMock, incidentRepositoryMock);
    }

    @Test
    void givenSuspiciousBurstFromIp_whenAnalyzingStream_thenEmitAlertAndPersistIncidentOncePerEscalation() {
        // Arrange
        String ip = "10.0.0.99";

        // Act: 3 votos normales -> no alerta; 4to voto -> SOSPECHOSA; 5to -> sigue SOSPECHOSA; 6to -> BLOQUEADA
        for (int i = 0; i < 6; i++) {
            fraudDetectionService.analyzeVoteStream(ip);
        }

        // Assert
        verify(alertPublisherMock, times(1)).publishAlert(ip, "SOSPECHOSA");
        verify(alertPublisherMock, times(1)).publishAlert(ip, "BLOQUEADA");
        verify(incidentRepositoryMock, times(1)).saveIncident(ip, "SOSPECHOSA", 4);
        verify(incidentRepositoryMock, times(1)).saveIncident(ip, "BLOQUEADA", 6);
        verifyNoMoreInteractions(alertPublisherMock, incidentRepositoryMock);
    }
}
