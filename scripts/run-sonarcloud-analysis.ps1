# ================================================
# SCRIPT DE ANÁLISIS SONARCLOUD (NUBE)
# ================================================
# Ejecuta análisis de calidad de código usando SonarCloud
# SonarCloud es la versión gratuita en la nube de SonarQube

param(
    [string]$SonarToken = $env:SONAR_TOKEN,
    [string]$ProjectPath = ".",
    [switch]$Help
)

# ================================================
# FUNCIONES DE UTILIDAD
# ================================================

function Show-Help {
    Write-Host "================================================" -ForegroundColor Cyan
    Write-Host "🌐 SCRIPT DE ANÁLISIS SONARCLOUD (NUBE)" -ForegroundColor Yellow
    Write-Host "================================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "DESCRIPCIÓN:" -ForegroundColor Green
    Write-Host "  Ejecuta análisis completo de calidad de código usando SonarCloud"
    Write-Host "  (SonarQube en la nube - GRATUITO para proyectos públicos)"
    Write-Host ""
    Write-Host "USO:" -ForegroundColor Green
    Write-Host "  .\run-sonarcloud-analysis.ps1 [-SonarToken <token>] [-ProjectPath <path>] [-Help]"
    Write-Host ""
    Write-Host "PARÁMETROS:" -ForegroundColor Green
    Write-Host "  -SonarToken   Token de autenticación de SonarCloud (requerido)"
    Write-Host "  -ProjectPath  Ruta del proyecto (por defecto: directorio actual)"
    Write-Host "  -Help         Muestra esta ayuda"
    Write-Host ""
    Write-Host "EJEMPLOS:" -ForegroundColor Green
    Write-Host "  # Análisis básico (token desde variable de entorno)"
    Write-Host "  .\run-sonarcloud-analysis.ps1"
    Write-Host ""
    Write-Host "  # Análisis con token específico"
    Write-Host "  .\run-sonarcloud-analysis.ps1 -SonarToken 'tu_token_aqui'"
    Write-Host ""
    Write-Host "  # Análisis de proyecto en otra ubicación"
    Write-Host "  .\run-sonarcloud-analysis.ps1 -ProjectPath 'C:\path\to\project'"
    Write-Host ""
    Write-Host "CONFIGURACIÓN PREVIA NECESARIA:" -ForegroundColor Yellow
    Write-Host "  1. Crear cuenta en https://sonarcloud.io"
    Write-Host "  2. Crear organización: vallegrande-org"
    Write-Host "  3. Importar proyecto: vallegrande_vg-ms-students"
    Write-Host "  4. Generar token de acceso en SonarCloud"
    Write-Host "  5. Configurar variable de entorno: `$env:SONAR_TOKEN='tu_token'"
    Write-Host ""
    Write-Host "================================================"
}

function Write-StepHeader {
    param([string]$Message)
    Write-Host ""
    Write-Host "================================================" -ForegroundColor Cyan
    Write-Host "🚀 $Message" -ForegroundColor Yellow  
    Write-Host "================================================" -ForegroundColor Cyan
}

function Write-Success {
    param([string]$Message)
    Write-Host "✅ $Message" -ForegroundColor Green
}

function Write-Error {
    param([string]$Message)
    Write-Host "❌ $Message" -ForegroundColor Red
}

function Write-Warning {
    param([string]$Message)
    Write-Host "⚠️  $Message" -ForegroundColor Yellow
}

function Write-Info {
    param([string]$Message)
    Write-Host "ℹ️  $Message" -ForegroundColor Blue
}

# ================================================
# VALIDACIÓN DE PARÁMETROS
# ================================================

if ($Help) {
    Show-Help
    exit 0
}

if (-not $SonarToken) {
    Write-Error "Token de SonarCloud no proporcionado."
    Write-Warning "Configura la variable de entorno: `$env:SONAR_TOKEN='tu_token'"
    Write-Warning "O usa el parámetro: -SonarToken 'tu_token'"
    Write-Info "Usa -Help para más información"
    exit 1
}

# ================================================
# CONFIGURACIÓN
# ================================================

$ProjectConfig = @{
    Key = "vallegrande_vg-ms-students"
    Name = "VG MS Students - Microservicio de Estudiantes"
    Organization = "vallegrande-org"
    Version = "1.0"
    HostUrl = "https://sonarcloud.io"
}

$SourcePaths = @{
    Sources = "src/main/java"
    Tests = "src/test/java"
    Binaries = "target/classes"
    TestBinaries = "target/test-classes"
    JacocoReport = "target/site/jacoco/jacoco.xml"
    SurefireReports = "target/surefire-reports"
}

# ================================================
# INICIO DEL ANÁLISIS
# ================================================

Write-Host "🌐 INICIANDO ANÁLISIS DE SONARCLOUD" -ForegroundColor Magenta
Write-Host "Proyecto: $($ProjectConfig.Name)" -ForegroundColor Gray
Write-Host "Organization: $($ProjectConfig.Organization)" -ForegroundColor Gray
Write-Host "URL: $($ProjectConfig.HostUrl)" -ForegroundColor Gray
Write-Host ""

# Cambiar al directorio del proyecto
if ($ProjectPath -ne ".") {
    Write-Info "Cambiando a directorio: $ProjectPath"
    Set-Location $ProjectPath
}

# ================================================
# PASO 1: VALIDAR PREREQUISITOS
# ================================================

Write-StepHeader "VALIDANDO PREREQUISITOS"

# Verificar Maven
if (-not (Get-Command mvn -ErrorAction SilentlyContinue)) {
    Write-Error "Maven no está instalado o no está en PATH"
    exit 1
}
Write-Success "Maven encontrado: $(mvn --version | Select-String "Apache Maven" | Select-Object -First 1)"

# Verificar Java
if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
    Write-Error "Java no está instalado o no está en PATH"
    exit 1
}
Write-Success "Java encontrado: $(java -version 2>&1 | Select-String "version" | Select-Object -First 1)"

# Verificar estructura del proyecto
if (-not (Test-Path "pom.xml")) {
    Write-Error "Archivo pom.xml no encontrado en el directorio actual"
    exit 1
}
Write-Success "Estructura del proyecto validada"

# Verificar configuración SonarCloud
if (-not (Test-Path "sonar-project.properties")) {
    Write-Warning "Archivo sonar-project.properties no encontrado - usando configuración por línea de comandos"
} else {
    Write-Success "Configuración sonar-project.properties encontrada"
}

# ================================================
# PASO 2: LIMPIAR Y COMPILAR
# ================================================

Write-StepHeader "COMPILANDO PROYECTO"

Write-Info "Limpiando proyecto anterior..."
$cleanResult = & mvn clean 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Error "Error durante mvn clean"
    Write-Host $cleanResult -ForegroundColor Red
    exit 1
}
Write-Success "Proyecto limpiado"

Write-Info "Compilando código fuente..."
$compileResult = & mvn compile 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Error "Error durante compilación"
    Write-Host $compileResult -ForegroundColor Red
    exit 1
}
Write-Success "Código compilado exitosamente"

# ================================================
# PASO 3: EJECUTAR PRUEBAS CON COBERTURA
# ================================================

Write-StepHeader "EJECUTANDO PRUEBAS CON COBERTURA"

Write-Info "Ejecutando pruebas unitarias con JaCoCo..."
$testResult = & mvn test 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Warning "Algunas pruebas fallaron, pero continuando con análisis"
    Write-Host $testResult -ForegroundColor Yellow
}

Write-Info "Generando reporte de cobertura JaCoCo..."
$jacocoResult = & mvn jacoco:report 2>&1
if ($LASTEXITCODE -eq 0) {
    Write-Success "Reporte de cobertura generado"
    if (Test-Path $SourcePaths.JacocoReport) {
        Write-Success "Reporte JaCoCo XML encontrado: $($SourcePaths.JacocoReport)"
    }
} else {
    Write-Warning "Error generando reporte JaCoCo - continuando sin cobertura"
}

# ================================================
# PASO 4: ANÁLISIS SONARCLOUD
# ================================================

Write-StepHeader "EJECUTANDO ANÁLISIS SONARCLOUD"

Write-Info "Configurando análisis SonarCloud..."

# Construir comando SonarCloud
$sonarArgs = @(
    "sonar:sonar",
    "-Dsonar.projectKey=$($ProjectConfig.Key)",
    "-Dsonar.organization=$($ProjectConfig.Organization)",
    "-Dsonar.projectName=`"$($ProjectConfig.Name)`"",
    "-Dsonar.projectVersion=$($ProjectConfig.Version)",
    "-Dsonar.host.url=$($ProjectConfig.HostUrl)",
    "-Dsonar.login=$SonarToken",
    "-Dsonar.sources=$($SourcePaths.Sources)",
    "-Dsonar.tests=$($SourcePaths.Tests)",
    "-Dsonar.java.binaries=$($SourcePaths.Binaries)",
    "-Dsonar.java.source=17",
    "-Dsonar.sourceEncoding=UTF-8",
    "-Dsonar.qualitygate.wait=true"
)

# Agregar cobertura si está disponible
if (Test-Path $SourcePaths.JacocoReport) {
    $sonarArgs += "-Dsonar.coverage.jacoco.xmlReportPaths=$($SourcePaths.JacocoReport)"
    Write-Info "Incluyendo reporte de cobertura JaCoCo"
}

# Agregar reportes de pruebas si están disponibles
if (Test-Path $SourcePaths.SurefireReports) {
    $sonarArgs += "-Dsonar.junit.reportPaths=$($SourcePaths.SurefireReports)"
    Write-Info "Incluyendo reportes de pruebas Surefire"
}

Write-Info "Ejecutando análisis SonarCloud..."
Write-Info "Comando: mvn $($sonarArgs -join ' ')"

$sonarResult = & mvn @sonarArgs 2>&1

if ($LASTEXITCODE -eq 0) {
    Write-Success "Análisis de SonarCloud completado exitosamente"
} else {
    Write-Error "Error durante análisis de SonarCloud"
    Write-Host $sonarResult -ForegroundColor Red
    exit 1
}

# ================================================
# PASO 5: MOSTRAR RESULTADOS
# ================================================

Write-StepHeader "RESULTADOS DEL ANÁLISIS"

$dashboardUrl = "$($ProjectConfig.HostUrl)/project/overview?id=$($ProjectConfig.Key)"

Write-Success "¡Análisis completado exitosamente!"
Write-Host ""
Write-Host "📊 DASHBOARD DE SONARCLOUD:" -ForegroundColor Green
Write-Host "   $dashboardUrl" -ForegroundColor Cyan
Write-Host ""
Write-Host "🎯 MÉTRICAS DISPONIBLES:" -ForegroundColor Green
Write-Host "   • Cobertura de código"
Write-Host "   • Quality Gate Status"
Write-Host "   • Code Smells"
Write-Host "   • Bugs y Vulnerabilidades"
Write-Host "   • Duplicación de código"
Write-Host "   • Complejidad ciclomática"
Write-Host ""
Write-Host "🔗 ENLACES ÚTILES:" -ForegroundColor Green
Write-Host "   • Issues: $($ProjectConfig.HostUrl)/project/issues?resolved=false&id=$($ProjectConfig.Key)"
Write-Host "   • Coverage: $($ProjectConfig.HostUrl)/component_measures?id=$($ProjectConfig.Key)&metric=coverage"
Write-Host "   • Duplications: $($ProjectConfig.HostUrl)/component_measures?id=$($ProjectConfig.Key)&metric=duplicated_lines_density"

Write-Host ""
Write-Host "🎉 ¡ANÁLISIS DE SONARCLOUD COMPLETADO!" -ForegroundColor Magenta
Write-Host "================================================" -ForegroundColor Cyan