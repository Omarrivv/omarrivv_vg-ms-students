# ===============================================
# SCRIPT AUTOMATIZADO: VERIFICACIÓN JENKINS + SLACK
# ===============================================

Write-Host "🏗️ VERIFICANDO CONFIGURACIÓN JENKINS + SLACK" -ForegroundColor Green
Write-Host ""

# Función para verificar si un servicio está corriendo
function Test-ServiceStatus {
    param($ServiceName)
    try {
        $service = Get-Service -Name $ServiceName -ErrorAction Stop
        return $service.Status -eq "Running"
    }
    catch {
        return $false
    }
}

# Función para verificar URL
function Test-WebService {
    param($Url)
    try {
        $response = Invoke-WebRequest -Uri $Url -Method Head -TimeoutSec 5 -ErrorAction Stop
        return $response.StatusCode -eq 200
    }
    catch {
        return $false
    }
}

# 1. Verificar Java (prerequisito para Jenkins)
Write-Host "☕ VERIFICANDO JAVA..." -ForegroundColor Yellow
try {
    $javaVersion = & java -version 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "   ✅ Java instalado correctamente" -ForegroundColor Green
        Write-Host "   📋 Versión detectada: $($javaVersion[0])" -ForegroundColor Gray
    } else {
        throw "Java no encontrado"
    }
} catch {
    Write-Host "   ❌ Java no está instalado" -ForegroundColor Red
    Write-Host "   💡 Descargar desde: https://adoptium.net/" -ForegroundColor Yellow
}

Write-Host ""

# 2. Verificar si Jenkins está instalado y corriendo
Write-Host "🏗️ VERIFICANDO JENKINS..." -ForegroundColor Yellow

$jenkinsRunning = Test-ServiceStatus -ServiceName "Jenkins"
if ($jenkinsRunning) {
    Write-Host "   ✅ Servicio Jenkins está corriendo" -ForegroundColor Green
    
    # Verificar acceso web
    $jenkinsWeb = Test-WebService -Url "http://localhost:8080"
    if ($jenkinsWeb) {
        Write-Host "   ✅ Jenkins web accesible en http://localhost:8080" -ForegroundColor Green
        Write-Host "   🌐 Abriendo Jenkins en navegador..." -ForegroundColor Cyan
        Start-Process "http://localhost:8080"
    } else {
        Write-Host "   ⚠️ Jenkins servicio corriendo pero web no accesible" -ForegroundColor Yellow
        Write-Host "   💡 Verificar que Jenkins haya iniciado completamente" -ForegroundColor Gray
    }
} else {
    Write-Host "   ❌ Jenkins no está corriendo o no está instalado" -ForegroundColor Red
    Write-Host "   📥 DESCARGAR JENKINS:" -ForegroundColor Yellow
    Write-Host "      • URL: https://www.jenkins.io/download/" -ForegroundColor White
    Write-Host "      • Versión recomendada: LTS (Long Term Support)" -ForegroundColor White
    Write-Host "   🔧 DESPUÉS DE INSTALAR:" -ForegroundColor Yellow
    Write-Host "      • Ejecutar este script nuevamente" -ForegroundColor White
    Write-Host "      • Seguir configuración en JENKINS_SLACK_SETUP_GUIDE.md" -ForegroundColor White
}

Write-Host ""

# 3. Información para configuración Slack
Write-Host "📢 CONFIGURACIÓN SLACK REQUERIDA:" -ForegroundColor Yellow
Write-Host "   🔗 Crear Slack App: https://api.slack.com/apps" -ForegroundColor Cyan
Write-Host "   📱 Nombre sugerido: Jenkins CI/CD Bot" -ForegroundColor White
Write-Host "   🔑 Permisos necesarios:" -ForegroundColor White
Write-Host "      • chat:write" -ForegroundColor Gray
Write-Host "      • chat:write.public" -ForegroundColor Gray  
Write-Host "      • channels:read" -ForegroundColor Gray
Write-Host ""

# 4. Información de configuración para Jenkins
Write-Host "⚙️ CONFIGURACIÓN JENKINS NECESARIA:" -ForegroundColor Yellow
Write-Host "   📂 Plugins requeridos:" -ForegroundColor White
Write-Host "      • Git Plugin ✅" -ForegroundColor Green
Write-Host "      • Pipeline Plugin ✅" -ForegroundColor Green
Write-Host "      • Slack Notification Plugin 📋 Instalar" -ForegroundColor Yellow
Write-Host "      • SonarQube Scanner Plugin 📋 Instalar" -ForegroundColor Yellow
Write-Host "      • Maven Integration Plugin 📋 Instalar" -ForegroundColor Yellow
Write-Host ""

# 5. Credenciales necesarias
Write-Host "🔐 CREDENCIALES PARA CONFIGURAR:" -ForegroundColor Yellow
Write-Host "   🌐 Token SonarCloud:" -ForegroundColor White
Write-Host "      ID: sonarcloud-token" -ForegroundColor Gray
Write-Host "      Valor: e6ec42646e20dadc593a99506c3b64a81e1595ba" -ForegroundColor Gray
Write-Host ""
Write-Host "   📢 Token Slack Bot:" -ForegroundColor White
Write-Host "      ID: slack-token" -ForegroundColor Gray
Write-Host "      Valor: xoxb-[tu-bot-token-aqui]" -ForegroundColor Gray
Write-Host ""

# 6. Pipeline configuration
Write-Host "🚀 CONFIGURACIÓN DEL PIPELINE:" -ForegroundColor Yellow
Write-Host "   📁 Nombre del Job: vg-ms-students-pipeline" -ForegroundColor White
Write-Host "   🔗 Repositorio: https://github.com/Omarrivv/omarrivv_vg-ms-students.git" -ForegroundColor White
Write-Host "   📋 Jenkinsfile: Ya está en el repositorio ✅" -ForegroundColor Green
Write-Host ""

# 7. Próximos pasos
Write-Host "📋 PRÓXIMOS PASOS:" -ForegroundColor Magenta
if ($jenkinsRunning) {
    Write-Host "   1. ⚙️ Instalar plugins necesarios en Jenkins" -ForegroundColor White
    Write-Host "   2. 🔐 Configurar credenciales SonarCloud y Slack" -ForegroundColor White
    Write-Host "   3. 🚀 Crear pipeline 'vg-ms-students-pipeline'" -ForegroundColor White
    Write-Host "   4. ✅ Ejecutar build de prueba" -ForegroundColor White
    Write-Host "   5. 📢 Verificar notificaciones en Slack" -ForegroundColor White
} else {
    Write-Host "   1. 📥 Instalar Jenkins desde https://www.jenkins.io/download/" -ForegroundColor White
    Write-Host "   2. 🔄 Ejecutar este script nuevamente" -ForegroundColor White
    Write-Host "   3. 📚 Seguir guía completa en JENKINS_SLACK_SETUP_GUIDE.md" -ForegroundColor White
}

Write-Host ""
Write-Host "📖 DOCUMENTACIÓN COMPLETA: JENKINS_SLACK_SETUP_GUIDE.md" -ForegroundColor Cyan

pause