# ===============================================
# SCRIPT SONARCLOUD CON CONFIGURACIÓN CORRECTA
# ===============================================

Write-Host "🌐 CONFIGURANDO Y EJECUTANDO SONARCLOUD" -ForegroundColor Green

# Configuración exacta de SonarCloud
$env:SONAR_TOKEN = "0a09fb65a369bbb1b9f9647aaf8911c47d96e357"

Write-Host "📋 Configuración SonarCloud:" -ForegroundColor Yellow
Write-Host "   Token: ✅ Configurado como variable de entorno" -ForegroundColor Green
Write-Host "   Project Key: Omarrivv_omarrivv_vg-ms-students" -ForegroundColor Green
Write-Host "   Organization: omarrivv" -ForegroundColor Green

# Paso 1: Limpiar proyecto
Write-Host "`n🧹 Limpiando proyecto..." -ForegroundColor Yellow
mvn clean -q

# Paso 2: Compilar sin tests
Write-Host "🔨 Compilando código fuente..." -ForegroundColor Yellow
mvn compile -q -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Error en compilación" -ForegroundColor Red
    pause
    exit 1
}

Write-Host "✅ Compilación exitosa" -ForegroundColor Green

# Paso 3: Ejecutar SonarCloud con configuración completa
Write-Host "`n🌐 Ejecutando análisis SonarCloud..." -ForegroundColor Yellow

mvn org.sonarsource.scanner.maven:sonar-maven-plugin:sonar `
    -Dsonar.projectKey=Omarrivv_omarrivv_vg-ms-students `
    -Dsonar.organization=omarrivv `
    -Dsonar.host.url=https://sonarcloud.io `
    -Dsonar.sources=src/main/java `
    -Dsonar.java.binaries=target/classes `
    -Dsonar.java.source=17 `
    -DskipTests `
    -X

if ($LASTEXITCODE -eq 0) {
    Write-Host "`n🎉 ¡ÉXITO! ANÁLISIS SONARCLOUD COMPLETADO" -ForegroundColor Green
    Write-Host ""
    Write-Host "📊 Dashboard: https://sonarcloud.io/project/overview?id=Omarrivv_omarrivv_vg-ms-students" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "🏆 RESUMEN DEL PROYECTO:" -ForegroundColor Magenta
    Write-Host "✅ 3 Pruebas unitarias implementadas" -ForegroundColor Green
    Write-Host "✅ Jenkins Pipeline configurado" -ForegroundColor Green
    Write-Host "✅ SonarCloud (nube) integrado" -ForegroundColor Green
    Write-Host "✅ JMeter para pruebas de carga" -ForegroundColor Green
    Write-Host "✅ Integración Slack configurada" -ForegroundColor Green
    Write-Host "✅ Documentación visual completa" -ForegroundColor Green
    Write-Host ""
    Write-Host "🚀 ¡PROYECTO CI/CD ENTERPRISE LISTO!" -ForegroundColor Magenta
} else {
    Write-Host "`n❌ Error en análisis SonarCloud" -ForegroundColor Red
    Write-Host "🔍 Revisa los logs para más detalles" -ForegroundColor Yellow
}

pause