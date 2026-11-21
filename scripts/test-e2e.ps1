# ==============================================================================
# NOTA IMPORTANTE: Entorno de ejecución para pruebas E2E
# ------------------------------------------------------------------------------
# Este script apunta directamente a http://localhost:8080/vote porque la prueba
# de ráfaga (Paso 5) envía el encabezado 'X-Forwarded-For' para simular IPs de bots.
# Nginx sobrescribe 'X-Forwarded-For' con $remote_addr por seguridad anti-spoofing,
# por lo que para correr este script se debe levantar el entorno con el compose dev:
#
#   docker compose -f docker-compose.yml -f docker-compose.dev.yml up -d
# ==============================================================================
$ErrorActionPreference = "Stop"
$baseUrl = "http://localhost:8080/vote"

Write-Host "=== 1. Voto valido (VOTER-100 -> CAND-A, NORTE) ===" -ForegroundColor Cyan
$res1 = Invoke-WebRequest -Uri $baseUrl -Method Post -ContentType "application/json" `
    -Body '{"voterId":"VOTER-100","candidateId":"CAND-A","regionId":"NORTE"}' -UseBasicParsing
Write-Host "Status: $($res1.StatusCode) | $($res1.Content)"

Write-Host "`n=== 2. Voto no elegible (VOTER-999 -> HTTP 403 esperado) ===" -ForegroundColor Cyan
try {
    Invoke-WebRequest -Uri $baseUrl -Method Post -ContentType "application/json" `
        -Body '{"voterId":"VOTER-999","candidateId":"CAND-A","regionId":"NORTE"}' -UseBasicParsing | Out-Null
    Write-Error "Se esperaba HTTP 403 para VOTER-999"
} catch {
    $code = $_.Exception.Response.StatusCode.value__
    Write-Host "Status esperado recibido: $code" -ForegroundColor Green
}

Write-Host "`n=== 3. Cambio de voto (VOTER-100 -> CAND-B, SUR) ===" -ForegroundColor Cyan
$res3 = Invoke-WebRequest -Uri $baseUrl -Method Post -ContentType "application/json" `
    -Body '{"voterId":"VOTER-100","candidateId":"CAND-B","regionId":"SUR"}' -UseBasicParsing
Write-Host "Status: $($res3.StatusCode) | $($res3.Content)"

Write-Host "`n=== 4. Segundo votante valido (VOTER-200 -> CAND-A, NORTE) ===" -ForegroundColor Cyan
$res4 = Invoke-WebRequest -Uri $baseUrl -Method Post -ContentType "application/json" `
    -Body '{"voterId":"VOTER-200","candidateId":"CAND-A","regionId":"NORTE"}' -UseBasicParsing
Write-Host "Status: $($res4.StatusCode) | $($res4.Content)"

Write-Host "`n=== 5. Rafaga desde IP 203.0.113.50 (6 votos para disparar SOSPECHOSA y BLOQUEADA) ===" -ForegroundColor Cyan
1..6 | ForEach-Object {
    try {
        $r = Invoke-WebRequest -Uri $baseUrl -Method Post -ContentType "application/json" `
            -Headers @{"X-Forwarded-For"="203.0.113.50"} `
            -Body '{"voterId":"VOTER-300","candidateId":"CAND-A","regionId":"NORTE"}' -UseBasicParsing
        Write-Host "Intento $_ -> Status: $($r.StatusCode)"
    } catch {
        $code = $_.Exception.Response.StatusCode.value__
        Write-Host "Intento $_ -> Bloqueado con Status: $code" -ForegroundColor Yellow
    }
    Start-Sleep -Milliseconds 300
}

Write-Host "`n=== 6. Verificando PostgreSQL (audit_votes y security_incidents) ===" -ForegroundColor Cyan
docker exec postgres psql -U admin -d voting_audit -c "SELECT voter_id, candidate_id, region_id, vote_timestamp FROM audit_votes;"
docker exec postgres psql -U admin -d voting_audit -c "SELECT ip_address, risk_level, activity_count, detected_at FROM security_incidents;"

Write-Host "`nDashboards disponibles:" -ForegroundColor Green
Write-Host "  Global:   http://localhost:8081/dashboard.html"
Write-Host "  Regional: http://localhost:8082/dashboard.html"
