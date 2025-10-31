# ===============================================
# SCRIPT PARA ANÁLISIS SONARCLOUD
# ===============================================

Write-Host "🔍 EJECUTANDO ANÁLISIS SONARCLOUD..." -ForegroundColor Green

# Variables de configuración
$SONAR_TOKEN = "737c58758f1a16ee4d96751041a9aaba7283c3a4"
$PROJECT_KEY = "omarrivv_omarrivv_vg-ms-students"
$ORGANIZATION = "omarrivv"
$SONAR_HOST = "https://sonarcloud.io"

Write-Host "📋 Configuración:" -ForegroundColor Yellow
Write-Host "   Project Key: $PROJECT_KEY"
Write-Host "   Organization: $ORGANIZATION"
Write-Host "   Host: $SONAR_HOST"

# Paso 1: Limpiar proyecto
Write-Host "`n🧹 Limpiando proyecto..." -ForegroundColor Yellow
mvn clean

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Error en clean" -ForegroundColor Red
    exit 1
}

# Paso 2: Compilar
Write-Host "`n🔨 Compilando proyecto..." -ForegroundColor Yellow
mvn compile

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Error en compilación" -ForegroundColor Red
    exit 1
}

# Paso 3: Ejecutar pruebas
Write-Host "`n🧪 Ejecutando pruebas..." -ForegroundColor Yellow
mvn test

if ($LASTEXITCODE -ne 0) {
    Write-Host "⚠️ Hay errores en las pruebas, pero continuamos..." -ForegroundColor Yellow
}

# Paso 4: Generar reporte JaCoCo
Write-Host "`n📊 Generando reporte de cobertura..." -ForegroundColor Yellow
mvn jacoco:report

# Paso 5: Ejecutar análisis SonarCloud
Write-Host "`n🌐 Ejecutando análisis SonarCloud..." -ForegroundColor Yellow
$sonarCommand = @"
mvn org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121:sonar `
  -Dsonar.projectKey=$PROJECT_KEY `
  -Dsonar.organization=$ORGANIZATION `
  -Dsonar.host.url=$SONAR_HOST `
  -Dsonar.login=$SONAR_TOKEN `
  -X
"@

Write-Host "Ejecutando comando:" -ForegroundColor Cyan
Write-Host $sonarCommand -ForegroundColor Gray

Invoke-Expression $sonarCommand

if ($LASTEXITCODE -eq 0) {
    Write-Host "`n🎉 ¡ANÁLISIS COMPLETADO EXITOSAMENTE!" -ForegroundColor Green
    Write-Host "📊 Ver resultados en: https://sonarcloud.io/project/overview?id=$PROJECT_KEY" -ForegroundColor Cyan
} else {
    Write-Host "`n❌ Error en el análisis SonarCloud" -ForegroundColor Red
    Write-Host "🔍 Revisa los logs arriba para más detalles" -ForegroundColor Yellow
}

pause