# Configuración de Integración Slack + Jenkins

## 🔧 Configuración Paso a Paso

### 1. Crear Slack App

1. **Ir a Slack API:** https://api.slack.com/apps
2. **Create New App → From scratch**
3. **Nombre:** `Jenkins CI/CD Bot`
4. **Workspace:** Tu workspace de Slack

### 2. Configurar Permisos del Bot

**OAuth & Permissions → Scopes → Bot Token Scopes:**
- `chat:write` - Enviar mensajes
- `chat:write.public` - Enviar a canales públicos
- `files:write` - Subir archivos
- `incoming-webhook` - Webhooks entrantes

### 3. Instalar App en Workspace

1. **Install App to Workspace**
2. **Copiar Bot User OAuth Token** (empieza con `xoxb-`)

### 4. Crear Canal de Notificaciones

```bash
# En Slack, crear canal
/create #ci-cd-notifications

# Invitar al bot
/invite @Jenkins CI/CD Bot
```

### 5. Configurar Jenkins

#### 5.1 Instalar Plugin Slack
**Manage Jenkins → Manage Plugins → Available:**
- Buscar: `Slack Notification Plugin`
- Instalar y reiniciar Jenkins

#### 5.2 Configurar Credenciales
**Manage Jenkins → Manage Credentials → Global → Add Credentials:**
- **Kind:** Secret text
- **Secret:** `xoxb-tu-bot-token`
- **ID:** `slack-bot-token`
- **Description:** Slack Bot Token

#### 5.3 Configurar Sistema Slack
**Manage Jenkins → Configure System → Slack:**
- **Workspace:** `tu-workspace-name`
- **Credential:** Seleccionar `slack-bot-token`
- **Default Channel:** `#ci-cd-notifications`
- **Test Connection** ✅

## 📨 Tipos de Notificaciones

### 1. Notificación de Inicio
```groovy
slackSend(
    channel: '#ci-cd-notifications',
    color: '#439FE0',
    message: """🚀 *Build Iniciado*
    Proyecto: ${env.JOB_NAME}
    Build: #${env.BUILD_NUMBER}
    Branch: ${env.BRANCH_NAME ?: 'main'}
    Iniciado por: ${env.BUILD_USER ?: 'Sistema'}
    <${env.BUILD_URL}|Ver Build>"""
)
```

### 2. Notificación de Éxito
```groovy
slackSend(
    channel: '#ci-cd-notifications',
    color: 'good',
    message: """✅ *BUILD EXITOSO* 🎉
    Proyecto: ${env.JOB_NAME}
    Build: #${env.BUILD_NUMBER}
    Duración: ${currentBuild.durationString}
    
    📊 Reportes:
    • <${env.BUILD_URL}testReport|Tests>
    • <${env.BUILD_URL}jacoco|Cobertura>
    • <${env.SONAR_HOST_URL}/dashboard?id=vg-ms-students|SonarQube>
    
    <${env.BUILD_URL}|Ver Build Completo>"""
)
```

### 3. Notificación de Fallo
```groovy
slackSend(
    channel: '#ci-cd-notifications',
    color: 'danger',
    message: """❌ *BUILD FALLIDO* 💥
    Proyecto: ${env.JOB_NAME}
    Build: #${env.BUILD_NUMBER}
    
    <${env.BUILD_URL}console|Ver Console Log>
    
    @channel Por favor revisar."""
)
```

### 4. Notificación de Tests
```groovy
script {
    def testResults = currentBuild.rawBuild.getAction(hudson.tasks.test.AbstractTestResultAction.class)
    if (testResults != null) {
        def total = testResults.totalCount
        def failed = testResults.failCount
        def passed = total - failed
        
        slackSend(
            channel: '#ci-cd-notifications',
            color: failed > 0 ? 'warning' : 'good',
            message: """🧪 *Resultados Tests*
            ✅ Pasaron: ${passed}
            ❌ Fallaron: ${failed}
            📊 Total: ${total}
            <${env.BUILD_URL}testReport|Ver Detalles>"""
        )
    }
}
```

### 5. Notificación de Cobertura
```groovy
script {
    // Leer cobertura de JaCoCo
    if (fileExists('target/site/jacoco/index.html')) {
        def coverage = sh(
            script: "grep -o 'Total[^%]*%' target/site/jacoco/index.html | head -1 | grep -o '[0-9]*%'",
            returnStdout: true
        ).trim()
        
        slackSend(
            channel: '#ci-cd-notifications',
            color: 'good',
            message: """📊 *Cobertura de Código*
            📈 Coverage: ${coverage}
            <${env.BUILD_URL}jacoco|Ver Reporte Detallado>"""
        )
    }
}
```

### 6. Notificación de SonarQube
```groovy
script {
    def qg = waitForQualityGate()
    def color = qg.status == 'OK' ? 'good' : 'danger'
    def emoji = qg.status == 'OK' ? '✅' : '❌'
    
    slackSend(
        channel: '#ci-cd-notifications',
        color: color,
        message: """${emoji} *Quality Gate ${qg.status}*
        
        <${env.SONAR_HOST_URL}/dashboard?id=vg-ms-students|Ver Análisis SonarQube>"""
    )
}
```

### 7. Notificación de Performance
```groovy
slackSend(
    channel: '#ci-cd-notifications',
    color: 'good',
    message: """⚡ *Pruebas Performance Completadas*
    📊 Métricas disponibles
    <${env.BUILD_URL}JMeter_20Performance_20Report|Ver Reporte JMeter>"""
)
```

## 🔔 Configuración Avanzada

### 1. Notificaciones Condicionales
```groovy
// Solo notificar en main branch
when {
    branch 'main'
}
steps {
    slackSend(...)
}

// Solo notificar fallos
post {
    failure {
        slackSend(...)
    }
}
```

### 2. Menciones Específicas
```groovy
// Mencionar usuario específico
slackSend(
    message: "<@usuario123> Build falló"
)

// Mencionar canal completo
slackSend(
    message: "@channel Build crítico falló"
)

// Mencionar grupo
slackSend(
    message: "@here Revisar build urgente"
)
```

### 3. Attachments Enriquecidos
```groovy
slackSend(
    channel: '#ci-cd-notifications',
    color: 'good',
    message: "Build Completado",
    attachments: [
        [
            title: "Detalles del Build",
            titleLink: env.BUILD_URL,
            fields: [
                [title: "Proyecto", value: env.JOB_NAME, short: true],
                [title: "Build", value: env.BUILD_NUMBER, short: true],
                [title: "Duración", value: currentBuild.durationString, short: true],
                [title: "Estado", value: "SUCCESS", short: true]
            ]
        ]
    ]
)
```

### 4. Subir Archivos
```groovy
// Subir reporte como archivo
slackUploadFile(
    channel: '#ci-cd-notifications',
    file: 'target/surefire-reports/TEST-*.xml',
    initialComment: 'Reporte de Tests Detallado'
)
```

## 🎨 Personalización de Mensajes

### Emojis y Formato
```groovy
// Emojis para diferentes estados
def statusEmoji = [
    'SUCCESS': '✅',
    'FAILURE': '❌',
    'UNSTABLE': '⚠️',
    'ABORTED': '🛑'
]

// Colores para estados
def statusColor = [
    'SUCCESS': 'good',
    'FAILURE': 'danger',
    'UNSTABLE': 'warning',
    'ABORTED': '#808080'
]

slackSend(
    color: statusColor[currentBuild.currentResult],
    message: """${statusEmoji[currentBuild.currentResult]} *Build ${currentBuild.currentResult}*"""
)
```

### Templates de Mensajes
```groovy
def buildMessage(status, details = [:]) {
    return """${getStatusEmoji(status)} *Build ${status}*
    
    🏗️ **Proyecto:** ${env.JOB_NAME}
    🔢 **Build:** #${env.BUILD_NUMBER}
    🌿 **Branch:** ${env.BRANCH_NAME ?: 'main'}
    ⏱️ **Duración:** ${currentBuild.durationString}
    👤 **Iniciado por:** ${env.BUILD_USER ?: 'Sistema'}
    
    ${details.coverage ? "📊 **Cobertura:** ${details.coverage}" : ""}
    ${details.tests ? "🧪 **Tests:** ${details.tests}" : ""}
    
    <${env.BUILD_URL}|Ver Build Completo>"""
}
```

## 🚨 Configuración de Alertas

### 1. Alertas por Severidad
```groovy
// Canal general para info
def infoChannel = '#ci-cd-notifications'

// Canal específico para errores críticos
def criticalChannel = '#dev-alerts'

// Canal para performance
def performanceChannel = '#performance-monitoring'

if (currentBuild.currentResult == 'FAILURE') {
    slackSend(channel: criticalChannel, color: 'danger', message: "...")
} else {
    slackSend(channel: infoChannel, color: 'good', message: "...")
}
```

### 2. Frecuencia de Notificaciones
```groovy
// Solo notificar primer fallo y recuperación
script {
    def lastBuild = currentBuild.previousBuild
    def shouldNotify = false
    
    if (currentBuild.currentResult == 'FAILURE' && 
        (lastBuild == null || lastBuild.result == 'SUCCESS')) {
        shouldNotify = true // Primer fallo
    }
    
    if (currentBuild.currentResult == 'SUCCESS' && 
        lastBuild != null && lastBuild.result == 'FAILURE') {
        shouldNotify = true // Recuperación
    }
    
    if (shouldNotify) {
        slackSend(...)
    }
}
```

## 🔧 Troubleshooting

### Problemas Comunes

1. **Bot no puede enviar mensajes:**
   - Verificar que el bot esté invitado al canal
   - Verificar permisos `chat:write`

2. **Token inválido:**
   - Regenerar token en Slack App
   - Actualizar credenciales en Jenkins

3. **Canal no encontrado:**
   - Verificar nombre del canal (con #)
   - Verificar que el canal existe

### Logs de Debug
```groovy
// Activar logs detallados
slackSend(
    channel: '#ci-cd-notifications',
    message: "Test message",
    teamDomain: 'tu-workspace',
    token: env.SLACK_TOKEN,
    verbose: true
)
```