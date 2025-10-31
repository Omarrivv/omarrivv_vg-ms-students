# ===============================================
# SONARCLOUD - CONFIGURACIÓN ACTUALIZADA
# ===============================================

Write-Host "🔄 CONFIGURACIÓN SONARCLOUD ACTUALIZADA" -ForegroundColor Green

# Nuevo token de SonarCloud
$NEW_SONAR_TOKEN = "e6ec42646e280a4c593a5950dc3b64a0e15951b"
$PROJECT_KEY = "Omarrivv_omarrivv_vg-ms-students"

Write-Host "📋 Nueva configuración:" -ForegroundColor Yellow
Write-Host "   Token: ✅ $NEW_SONAR_TOKEN" -ForegroundColor Green
Write-Host "   Project Key: $PROJECT_KEY" -ForegroundColor Green
Write-Host "   Organization: omarrivv" -ForegroundColor Green

# Configurar variable de entorno
$env:SONAR_TOKEN = $NEW_SONAR_TOKEN

Write-Host "`n🔧 Ejecutando comando actualizado..." -ForegroundColor Yellow

# Comando exacto que muestra SonarCloud
mvn verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=Omarrivv_omarrivv_vg-ms-students -DskipTests

if ($LASTEXITCODE -eq 0) {
    Write-Host "`n🎉 ¡ÉXITO CON NUEVA CONFIGURACIÓN!" -ForegroundColor Green
    Write-Host "📊 Revisa tu dashboard en SonarCloud" -ForegroundColor Cyan
} else {
    Write-Host "`n⚠️ Intentando comando simplificado..." -ForegroundColor Yellow
    
    # Comando alternativo simplificado
    mvn clean verify sonar:sonar -DskipTests
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "`n🎉 ¡ÉXITO CON COMANDO SIMPLIFICADO!" -ForegroundColor Green
    } else {
        Write-Host "`n❌ Error en ambos comandos" -ForegroundColor Red
    }
}

Write-Host "`n📝 COMANDOS PARA USAR:" -ForegroundColor Magenta
Write-Host "# Configurar token:" -ForegroundColor Gray
Write-Host '$env:SONAR_TOKEN = "e6ec42646e280a4c593a5950dc3b64a0e15951b"' -ForegroundColor White
Write-Host "`n# Opción 1 - Comando completo:" -ForegroundColor Gray
Write-Host "mvn verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=Omarrivv_omarrivv_vg-ms-students -DskipTests" -ForegroundColor White
Write-Host "`n# Opción 2 - Comando simplificado:" -ForegroundColor Gray
Write-Host "mvn clean verify sonar:sonar -DskipTests" -ForegroundColor White

pause