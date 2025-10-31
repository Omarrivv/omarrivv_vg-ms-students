# Script de Pipeline Local Completo
# Ejecutar con: .\run-full-pipeline.ps1

param(
    [switch]$SkipTests,
    [switch]$SkipSonar,
    [switch]$SkipPerformance,
    [switch]$SkipDocker
)

Write-Host "🚀 INICIANDO PIPELINE COMPLETO LOCAL" -ForegroundColor Green
Write-Host "=" * 50 -ForegroundColor Cyan

$startTime = Get-Date
$success = $true

try {
    # Verificar prerrequisitos
    Write-Host "`n✅ VERIFICANDO PRERREQUISITOS" -ForegroundColor Cyan
    
    # Java
    java -version 2>&1 | Out-Null
    if ($LASTEXITCODE -ne 0) {
        throw "Java no encontrado. Instalar JDK 17"
    }
    Write-Host "  ☕ Java: OK" -ForegroundColor Green
    
    # Maven
    mvn -version 2>&1 | Out-Null
    if ($LASTEXITCODE -ne 0) {
        throw "Maven no encontrado"
    }
    Write-Host "  📦 Maven: OK" -ForegroundColor Green

    # 1. ETAPA BUILD
    Write-Host "`n🔧 ETAPA 1: BUILD Y COMPILACIÓN" -ForegroundColor Cyan
    Write-Host "  Limpiando proyecto..." -ForegroundColor Yellow
    mvn clean compile -q
    if ($LASTEXITCODE -ne 0) { 
        throw "Error en compilación" 
    }
    Write-Host "  ✅ Compilación exitosa" -ForegroundColor Green

    # 2. ETAPA TESTS
    if (-not $SkipTests) {
        Write-Host "`n🧪 ETAPA 2: PRUEBAS UNITARIAS" -ForegroundColor Cyan
        Write-Host "  Ejecutando tests..." -ForegroundColor Yellow
        mvn test jacoco:report -q
        if ($LASTEXITCODE -ne 0) { 
            throw "Tests unitarios fallaron" 
        }
        
        # Mostrar resultados de tests
        if (Test-Path "target/surefire-reports") {
            $testFiles = Get-ChildItem "target/surefire-reports/TEST-*.xml"
            $totalTests = 0
            $failures = 0
            
            foreach ($file in $testFiles) {
                $xml = [xml](Get-Content $file)
                $totalTests += [int]$xml.testsuite.tests
                $failures += [int]$xml.testsuite.failures
            }
            
            Write-Host "  📊 Tests ejecutados: $totalTests" -ForegroundColor White
            Write-Host "  ✅ Tests exitosos: $($totalTests - $failures)" -ForegroundColor Green
            Write-Host "  ❌ Tests fallidos: $failures" -ForegroundColor $(if ($failures -gt 0) { "Red" } else { "Green" })
        }
        
        Write-Host "  ✅ Tests unitarios completados" -ForegroundColor Green
    } else {
        Write-Host "`n⏭️ ETAPA 2: TESTS OMITIDOS" -ForegroundColor Yellow
    }

    # 3. ETAPA SONARQUBE
    if (-not $SkipSonar) {
        Write-Host "`n🔍 ETAPA 3: ANÁLISIS SONARQUBE" -ForegroundColor Cyan
        
        # Verificar si SonarQube está corriendo
        try {
            $response = Invoke-RestMethod -Uri "http://localhost:9000/api/system/status" -Method GET -TimeoutSec 5
            if ($response.status -eq "UP") {
                Write-Host "  Ejecutando análisis SonarQube..." -ForegroundColor Yellow
                mvn sonar:sonar `
                    -Dsonar.projectKey=vg-ms-students `
                    -Dsonar.projectName="VG MS Students" `
                    -Dsonar.host.url=http://localhost:9000 `
                    -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml `
                    -q
                
                if ($LASTEXITCODE -eq 0) {
                    Write-Host "  ✅ Análisis SonarQube completado" -ForegroundColor Green
                    Write-Host "  🌐 Ver en: http://localhost:9000/dashboard?id=vg-ms-students" -ForegroundColor Cyan
                } else {
                    Write-Host "  ⚠️ Warning: Error en análisis SonarQube" -ForegroundColor Yellow
                }
            }
        } catch {
            Write-Host "  ⚠️ SonarQube no disponible, omitiendo análisis..." -ForegroundColor Yellow
        }
    } else {
        Write-Host "`n⏭️ ETAPA 3: SONARQUBE OMITIDO" -ForegroundColor Yellow
    }

    # 4. ETAPA PACKAGE
    Write-Host "`n📦 ETAPA 4: EMPAQUETADO" -ForegroundColor Cyan
    Write-Host "  Generando JAR..." -ForegroundColor Yellow
    mvn package -DskipTests -q
    if ($LASTEXITCODE -ne 0) { 
        throw "Error en empaquetado" 
    }
    
    $jarFile = Get-ChildItem "target/*.jar" | Where-Object { $_.Name -notlike "*original*" } | Select-Object -First 1
    if ($jarFile) {
        $jarSize = [math]::Round($jarFile.Length / 1MB, 2)
        Write-Host "  ✅ JAR generado: $($jarFile.Name) ($jarSize MB)" -ForegroundColor Green
    }

    # 5. ETAPA PERFORMANCE
    if (-not $SkipPerformance -and (Test-Path "C:\JMeter\bin\jmeter.bat")) {
        Write-Host "`n⚡ ETAPA 5: PRUEBAS DE PERFORMANCE" -ForegroundColor Cyan
        
        if (Test-Path "performance-tests/student-api-test.jmx") {
            Write-Host "  Iniciando aplicación para tests..." -ForegroundColor Yellow
            $app = Start-Process java -ArgumentList "-jar", $jarFile.FullName, "--server.port=8081" -PassThru -WindowStyle Hidden
            
            Start-Sleep -Seconds 20
            
            # Verificar que la aplicación esté corriendo
            try {
                $healthCheck = Invoke-RestMethod -Uri "http://localhost:8081/actuator/health" -Method GET -TimeoutSec 10
                if ($healthCheck.status -eq "UP") {
                    Write-Host "  ✅ Aplicación iniciada" -ForegroundColor Green
                    
                    Write-Host "  Ejecutando pruebas JMeter..." -ForegroundColor Yellow
                    Set-Location performance-tests
                    & "C:\JMeter\bin\jmeter.bat" -n -t student-api-test.jmx -l results-$(Get-Date -Format 'yyyyMMdd-HHmmss').jtl -e -o jmeter-report 2>&1 | Out-Null
                    
                    if ($LASTEXITCODE -eq 0) {
                        Write-Host "  ✅ Pruebas de performance completadas" -ForegroundColor Green
                    } else {
                        Write-Host "  ⚠️ Warning: Error en pruebas de performance" -ForegroundColor Yellow
                    }
                    Set-Location ..
                }
            } catch {
                Write-Host "  ❌ Error: Aplicación no responde" -ForegroundColor Red
            }
            
            # Detener aplicación
            if ($app -and !$app.HasExited) {
                Stop-Process -Id $app.Id -Force -ErrorAction SilentlyContinue
                Write-Host "  🛑 Aplicación detenida" -ForegroundColor Yellow
            }
        } else {
            Write-Host "  ⚠️ No se encontró plan de pruebas JMeter" -ForegroundColor Yellow
        }
    } elseif (-not $SkipPerformance) {
        Write-Host "`n⏭️ ETAPA 5: PERFORMANCE - JMeter no disponible" -ForegroundColor Yellow
    } else {
        Write-Host "`n⏭️ ETAPA 5: PERFORMANCE OMITIDA" -ForegroundColor Yellow
    }

    # 6. ETAPA DOCKER
    if (-not $SkipDocker -and (Get-Command docker -ErrorAction SilentlyContinue)) {
        Write-Host "`n🐳 ETAPA 6: BUILD DOCKER" -ForegroundColor Cyan
        Write-Host "  Construyendo imagen..." -ForegroundColor Yellow
        docker build -t vg-ms-students:local . 2>&1 | Out-Null
        if ($LASTEXITCODE -eq 0) {
            Write-Host "  ✅ Imagen Docker creada: vg-ms-students:local" -ForegroundColor Green
        } else {
            Write-Host "  ⚠️ Warning: Error en build Docker" -ForegroundColor Yellow
        }
    } else {
        Write-Host "`n⏭️ ETAPA 6: DOCKER OMITIDO" -ForegroundColor Yellow
    }

} catch {
    $success = $false
    Write-Host "`n❌ ERROR EN PIPELINE: $($_.Exception.Message)" -ForegroundColor Red
}

# Resumen final
$endTime = Get-Date
$duration = $endTime - $startTime

Write-Host "`n" + ("=" * 50) -ForegroundColor Cyan

if ($success) {
    Write-Host "✅ PIPELINE COMPLETADO EXITOSAMENTE! 🎉" -ForegroundColor Green
} else {
    Write-Host "❌ PIPELINE FALLÓ 💥" -ForegroundColor Red
}

Write-Host "⏱️ Duración total: $($duration.Minutes)m $($duration.Seconds)s" -ForegroundColor Cyan

# Mostrar reportes disponibles
Write-Host "`n📊 REPORTES DISPONIBLES:" -ForegroundColor Yellow
if (Test-Path "target/surefire-reports") {
    Write-Host "  • Tests: target/surefire-reports/" -ForegroundColor White
}
if (Test-Path "target/site/jacoco/index.html") {
    Write-Host "  • Cobertura: target/site/jacoco/index.html" -ForegroundColor White
}
Write-Host "  • SonarQube: http://localhost:9000/dashboard?id=vg-ms-students" -ForegroundColor White
if (Test-Path "performance-tests/jmeter-report/index.html") {
    Write-Host "  • Performance: performance-tests/jmeter-report/index.html" -ForegroundColor White
}
if ($jarFile) {
    Write-Host "  • Artefacto: $($jarFile.FullName)" -ForegroundColor White
}

# Abrir reportes principales
Write-Host "`n🌐 ¿Abrir reportes principales? (y/N): " -NoNewline -ForegroundColor Cyan
$response = Read-Host

if ($response -eq 'y' -or $response -eq 'Y') {
    if (Test-Path "target/site/jacoco/index.html") {
        Start-Process "target/site/jacoco/index.html"
    }
    if (Test-Path "performance-tests/jmeter-report/index.html") {
        Start-Process "performance-tests/jmeter-report/index.html"
    }
}

Write-Host "`n🚀 Pipeline finalizado!" -ForegroundColor Green

if (-not $success) {
    exit 1
}