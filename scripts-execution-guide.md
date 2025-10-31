# Scripts de Configuración y Ejecución

## 🚀 Scripts de Inicio Rápido

### 1. Configurar Entorno Completo

**Archivo: `setup-environment.ps1`**
```powershell
# Script para configurar todo el entorno de CI/CD
Write-Host "🚀 Configurando entorno CI/CD completo..." -ForegroundColor Green

# Verificar Java
Write-Host "☕ Verificando Java..." -ForegroundColor Yellow
java -version
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Java no encontrado. Por favor instalar JDK 17" -ForegroundColor Red
    exit 1
}

# Verificar Maven
Write-Host "📦 Verificando Maven..." -ForegroundColor Yellow
mvn -version
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Maven no encontrado. Instalando..." -ForegroundColor Yellow
    # Instalar Maven aquí o indicar cómo instalarlo
}

# Ejecutar tests
Write-Host "🧪 Ejecutando pruebas..." -ForegroundColor Yellow
mvn clean test jacoco:report

# Verificar SonarQube
Write-Host "🔍 Verificando SonarQube..." -ForegroundColor Yellow
$sonarResponse = Invoke-RestMethod -Uri "http://localhost:9000/api/system/status" -Method GET -ErrorAction SilentlyContinue
if ($sonarResponse.status -eq "UP") {
    Write-Host "✅ SonarQube está corriendo" -ForegroundColor Green
    
    # Ejecutar análisis SonarQube
    Write-Host "📊 Ejecutando análisis SonarQube..." -ForegroundColor Yellow
    mvn sonar:sonar -Dsonar.projectKey=vg-ms-students -Dsonar.host.url=http://localhost:9000
} else {
    Write-Host "⚠️ SonarQube no está corriendo. Iniciando..." -ForegroundColor Yellow
    # Aquí podrías agregar comandos para iniciar SonarQube
}

# Verificar JMeter
Write-Host "⚡ Verificando JMeter..." -ForegroundColor Yellow
if (Test-Path "C:\JMeter\bin\jmeter.bat") {
    Write-Host "✅ JMeter encontrado" -ForegroundColor Green
} else {
    Write-Host "❌ JMeter no encontrado en C:\JMeter\" -ForegroundColor Red
}

Write-Host "✅ Configuración completada!" -ForegroundColor Green
```

### 2. Ejecutar Tests Completos

**Archivo: `run-tests.ps1`**
```powershell
Write-Host "🧪 Ejecutando suite completa de pruebas..." -ForegroundColor Green

# Limpiar y compilar
Write-Host "🔧 Compilando proyecto..." -ForegroundColor Yellow
mvn clean compile
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Error en compilación" -ForegroundColor Red
    exit 1
}

# Ejecutar tests unitarios
Write-Host "🔬 Ejecutando tests unitarios..." -ForegroundColor Yellow
mvn test
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Tests unitarios fallaron" -ForegroundColor Red
    exit 1
}

# Generar reporte de cobertura
Write-Host "📊 Generando reporte de cobertura..." -ForegroundColor Yellow
mvn jacoco:report

# Mostrar resultados
Write-Host "📈 Resultados:" -ForegroundColor Green
Write-Host "- Tests: target/surefire-reports/"
Write-Host "- Cobertura: target/site/jacoco/index.html"

# Abrir reportes automáticamente
Start-Process "target/site/jacoco/index.html"

Write-Host "✅ Tests completados!" -ForegroundColor Green
```

### 3. Ejecutar Análisis SonarQube

**Archivo: `run-sonar.ps1`**
```powershell
Write-Host "🔍 Ejecutando análisis SonarQube..." -ForegroundColor Green

# Verificar que SonarQube esté corriendo
$sonarResponse = Invoke-RestMethod -Uri "http://localhost:9000/api/system/status" -Method GET -ErrorAction SilentlyContinue
if ($sonarResponse.status -ne "UP") {
    Write-Host "❌ SonarQube no está corriendo. Por favor iniciarlo primero." -ForegroundColor Red
    Write-Host "💡 Ejecutar: C:\SonarQube\bin\windows-x86-64\StartSonar.bat" -ForegroundColor Yellow
    exit 1
}

# Ejecutar tests primero
Write-Host "🧪 Ejecutando tests para cobertura..." -ForegroundColor Yellow
mvn clean test jacoco:report

# Ejecutar análisis SonarQube
Write-Host "📊 Iniciando análisis SonarQube..." -ForegroundColor Yellow
mvn sonar:sonar `
    -Dsonar.projectKey=vg-ms-students `
    -Dsonar.projectName="VG MS Students" `
    -Dsonar.host.url=http://localhost:9000 `
    -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Análisis SonarQube completado!" -ForegroundColor Green
    Write-Host "🌐 Ver resultados: http://localhost:9000/dashboard?id=vg-ms-students" -ForegroundColor Cyan
    Start-Process "http://localhost:9000/dashboard?id=vg-ms-students"
} else {
    Write-Host "❌ Error en análisis SonarQube" -ForegroundColor Red
}
```

### 4. Ejecutar Pruebas de Performance

**Archivo: `run-performance-tests.ps1`**
```powershell
Write-Host "⚡ Ejecutando pruebas de performance con JMeter..." -ForegroundColor Green

# Verificar JMeter
if (-not (Test-Path "C:\JMeter\bin\jmeter.bat")) {
    Write-Host "❌ JMeter no encontrado. Instalar en C:\JMeter\" -ForegroundColor Red
    exit 1
}

# Compilar aplicación
Write-Host "📦 Compilando aplicación..." -ForegroundColor Yellow
mvn clean package -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Error en compilación" -ForegroundColor Red
    exit 1
}

# Iniciar aplicación en background
Write-Host "🚀 Iniciando aplicación..." -ForegroundColor Yellow
$app = Start-Process java -ArgumentList "-jar", "target/vg-ms-students-1.0.jar", "--server.port=8081" -PassThru -WindowStyle Hidden

# Esperar a que la aplicación se inicie
Write-Host "⏳ Esperando que la aplicación se inicie..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

# Verificar que la aplicación esté corriendo
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8081/actuator/health" -Method GET -TimeoutSec 10
    if ($response.status -eq "UP") {
        Write-Host "✅ Aplicación iniciada correctamente" -ForegroundColor Green
    }
} catch {
    Write-Host "❌ Error: Aplicación no responde" -ForegroundColor Red
    Stop-Process -Id $app.Id -Force
    exit 1
}

# Ejecutar pruebas JMeter
Write-Host "⚡ Ejecutando pruebas JMeter..." -ForegroundColor Yellow
Set-Location performance-tests
& "C:\JMeter\bin\jmeter.bat" -n -t student-api-test.jmx -l results.jtl -e -o jmeter-report

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Pruebas de performance completadas!" -ForegroundColor Green
    Write-Host "📊 Ver reporte: performance-tests/jmeter-report/index.html" -ForegroundColor Cyan
    Start-Process "jmeter-report/index.html"
} else {
    Write-Host "❌ Error en pruebas de performance" -ForegroundColor Red
}

# Detener aplicación
Write-Host "🛑 Deteniendo aplicación..." -ForegroundColor Yellow
Stop-Process -Id $app.Id -Force

Set-Location ..
Write-Host "✅ Pruebas de performance finalizadas!" -ForegroundColor Green
```

### 5. Pipeline Local Completo

**Archivo: `run-full-pipeline.ps1`**
```powershell
Write-Host "🚀 Ejecutando pipeline completo local..." -ForegroundColor Green

$startTime = Get-Date

try {
    # 1. Build
    Write-Host "`n🔧 ETAPA 1: BUILD" -ForegroundColor Cyan
    mvn clean compile
    if ($LASTEXITCODE -ne 0) { throw "Error en build" }

    # 2. Tests Unitarios
    Write-Host "`n🧪 ETAPA 2: TESTS UNITARIOS" -ForegroundColor Cyan
    mvn test jacoco:report
    if ($LASTEXITCODE -ne 0) { throw "Error en tests unitarios" }

    # 3. SonarQube Analysis
    Write-Host "`n🔍 ETAPA 3: ANÁLISIS SONARQUBE" -ForegroundColor Cyan
    mvn sonar:sonar -Dsonar.projectKey=vg-ms-students -Dsonar.host.url=http://localhost:9000
    if ($LASTEXITCODE -ne 0) { Write-Host "⚠️ SonarQube no disponible, continuando..." -ForegroundColor Yellow }

    # 4. Package
    Write-Host "`n📦 ETAPA 4: PACKAGE" -ForegroundColor Cyan
    mvn package -DskipTests
    if ($LASTEXITCODE -ne 0) { throw "Error en package" }

    # 5. Performance Tests (opcional)
    Write-Host "`n⚡ ETAPA 5: PERFORMANCE TESTS" -ForegroundColor Cyan
    if (Test-Path "C:\JMeter\bin\jmeter.bat") {
        .\run-performance-tests.ps1
    } else {
        Write-Host "⚠️ JMeter no disponible, omitiendo performance tests..." -ForegroundColor Yellow
    }

    # 6. Docker Build (opcional)
    Write-Host "`n🐳 ETAPA 6: DOCKER BUILD" -ForegroundColor Cyan
    if (Get-Command docker -ErrorAction SilentlyContinue) {
        docker build -t vg-ms-students:local .
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ Imagen Docker creada: vg-ms-students:local" -ForegroundColor Green
        }
    } else {
        Write-Host "⚠️ Docker no disponible, omitiendo build..." -ForegroundColor Yellow
    }

    $endTime = Get-Date
    $duration = $endTime - $startTime
    
    Write-Host "`n✅ PIPELINE COMPLETADO EXITOSAMENTE! 🎉" -ForegroundColor Green
    Write-Host "⏱️ Duración total: $($duration.Minutes)m $($duration.Seconds)s" -ForegroundColor Cyan
    
    # Mostrar reportes disponibles
    Write-Host "`n📊 REPORTES DISPONIBLES:" -ForegroundColor Yellow
    Write-Host "• Tests: target/surefire-reports/" -ForegroundColor White
    Write-Host "• Cobertura: target/site/jacoco/index.html" -ForegroundColor White
    Write-Host "• SonarQube: http://localhost:9000/dashboard?id=vg-ms-students" -ForegroundColor White
    if (Test-Path "performance-tests/jmeter-report/index.html") {
        Write-Host "• Performance: performance-tests/jmeter-report/index.html" -ForegroundColor White
    }

} catch {
    Write-Host "`n❌ PIPELINE FALLÓ: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}
```

## 📋 Comandos de Uso Rápido

### Comandos Básicos
```powershell
# Ejecutar solo tests
mvn clean test

# Tests con cobertura
mvn clean test jacoco:report

# SonarQube análisis
mvn sonar:sonar

# Pipeline completo local
.\run-full-pipeline.ps1
```

### Comandos Avanzados
```powershell
# Tests específicos
mvn test -Dtest=StudentServiceTest

# Tests con perfil específico
mvn test -Dspring.profiles.active=test

# Package sin tests
mvn package -DskipTests

# Clean y reinstall dependencias
mvn clean install -U
```

### Verificaciones de Salud
```powershell
# Verificar aplicación
curl http://localhost:8080/actuator/health

# Verificar SonarQube
curl http://localhost:9000/api/system/status

# Ver logs de la aplicación
docker logs vg-ms-students
```