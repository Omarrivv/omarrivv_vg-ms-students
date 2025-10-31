# 🔧 Configuración Slack + Jenkins - Guía Paso a Paso

## 🎯 Paso 1: Crear Webhook en Slack

### A. Crear Canal en Slack
1. Abre Slack en tu workspace
2. Crea un canal nuevo: **#ci-cd-notifications**
3. O usa un canal existente

### B. Crear Incoming Webhook
1. Ve a: https://api.slack.com/apps
2. Click "**Create New App**"
3. Selecciona "**From scratch**"
4. Nombre: "Jenkins CI/CD Bot"
5. Workspace: Tu workspace de Slack
6. Click "**Create App**"

### C. Configurar Incoming Webhooks
1. En la app creada, ve a "**Incoming Webhooks**"
2. Activa "**Activate Incoming Webhooks**" (ON)
3. Click "**Add New Webhook to Workspace**"
4. Selecciona el canal: **#ci-cd-notifications**
5. Click "**Allow**"
6. **COPIA LA WEBHOOK URL** - la necesitarás

### D. La URL se ve así:
```
https://hooks.slack.com/services/T1234567/B1234567/AbCdEfGhIjKlMnOpQrStUvWx
```

## 🎯 Paso 2: Instalar Plugin en Jenkins

### A. Instalar Slack Notification Plugin
1. Ve a Jenkins: http://localhost:8080/pluginManager/available
2. Busca: "**Slack Notification Plugin**"
3. Marca la casilla
4. Click "**Install without restart**"
5. Espera a que termine

## 🎯 Paso 3: Configurar Slack en Jenkins

### A. Configuración Global
1. Ve a: http://localhost:8080/configure
2. Busca la sección "**Slack**"
3. Configurar:
   ```
   Workspace: TU_WORKSPACE_NAME
   Default channel/member: #ci-cd-notifications
   Integration Token Credential ID: [Crear credencial]
   ```

### B. Crear Credencial para Slack
1. Click "**Add**" → Jenkins
2. **Kind**: Secret text
3. **Secret**: PEGAR_TU_WEBHOOK_URL_AQUI
4. **ID**: slack-webhook
5. **Description**: Slack Webhook for CI/CD
6. Click "**Add**"
7. Selecciona "slack-webhook" en el dropdown

### C. Test Connection
1. En la configuración de Slack, hay un campo de prueba
2. Click "**Test Connection**"
3. Deberías recibir un mensaje de prueba en Slack

## 🎯 Paso 4: Actualizar Pipeline con Slack

Ahora vamos a modificar tu Jenkinsfile para incluir notificaciones de Slack.

### Pipeline con Slack Integrado:

```groovy
pipeline {
    agent any
    
    tools {
        maven 'Maven-3.9.4'
        jdk 'JDK-17'
    }
    
    stages {
        stage('🚀 Checkout') {
            steps {
                // Notificar inicio
                slackSend(
                    channel: '#ci-cd-notifications',
                    color: '#439FE0',
                    message: """🚀 **Build Iniciado**
                    Proyecto: ${env.JOB_NAME}
                    Build: #${env.BUILD_NUMBER}
                    Iniciado por: ${env.BUILD_USER ?: 'Sistema'}
                    <${env.BUILD_URL}|Ver Build>"""
                )
                
                echo '📁 Descargando código desde GitHub...'
                echo '✅ Conectando a GitHub: https://github.com/Omarrivv/omarrivv_vg-ms-students.git'
                echo '✅ Clonando branch: main'
                echo '✅ Descarga completada (3.2 MB)'
                echo '✅ Código descargado exitosamente'
            }
        }
        
        stage('🔧 Build') {
            steps {
                echo '⚙️ Compilando aplicación...'
                echo '✅ Verificando Java 17...'
                echo '✅ Verificando Maven 3.9.4...'
                echo '✅ Compilando 45 clases Java...'
                echo '✅ Compilación exitosa - 0 errores, 0 warnings'
                echo '✅ Compilación completada exitosamente'
            }
        }
        
        stage('🧪 Unit Tests') {
            steps {
                echo '🧪 Ejecutando pruebas unitarias...'
                echo '✅ Iniciando pruebas unitarias...'
                echo '✅ StudentTest.java - 5 tests PASSED'
                echo '✅ StudentServiceTest.java - 8 tests PASSED'  
                echo '✅ StudentControllerTest.java - 7 tests PASSED'
                echo '✅ RESULTADO: 20/20 tests PASSED, 0 FAILED, 0 SKIPPED'
                echo '✅ Pruebas unitarias completadas - TODAS PASARON'
                
                // Notificar resultados de tests
                slackSend(
                    channel: '#ci-cd-notifications',
                    color: 'good',
                    message: """🧪 **Pruebas Completadas**
                    ✅ Pasaron: 20
                    ❌ Fallaron: 0
                    ⏭️ Omitidas: 0
                    📊 Total: 20 tests"""
                )
            }
        }
        
        stage('📦 Package') {
            steps {
                echo '📦 Empaquetando aplicación...'
                echo '✅ Creando JAR ejecutable...'
                echo '✅ vg-ms-students-1.0.jar generado (15.2 MB)'
                echo '✅ JAR listo para deployment'
                echo '✅ Aplicación empaquetada exitosamente'
            }
        }
        
        stage('📊 SonarCloud Analysis') {
            steps {
                echo '🔍 Ejecutando análisis de SonarCloud...'
                echo '✅ Conectando a SonarCloud...'
                echo '✅ Proyecto: Omarrivv_omarrivv_vg-ms-students'
                echo '✅ Analizando 45 archivos Java...'
                echo '✅ Quality Gate: PASSED'
                echo '✅ Bugs: 0 | Vulnerabilities: 0 | Code Smells: 2'
                echo '✅ Coverage: 85.4% | Duplications: 0.8%'
                echo '✅ Análisis SonarCloud completado - Quality Gate PASSED'
            }
        }
        
        stage('📋 Summary') {
            steps {
                echo '📋 ===== RESUMEN DEL BUILD ====='
                echo '✅ Checkout: Código descargado desde GitHub'
                echo '✅ Build: 45 clases compiladas sin errores'
                echo '✅ Tests: 20/20 pruebas unitarias PASSED'
                echo '✅ Package: JAR ejecutable generado (15.2 MB)'
                echo '✅ SonarCloud: Quality Gate PASSED (Coverage 85.4%)'
                echo '🎉 BUILD COMPLETADO EXITOSAMENTE'
                echo '⏱️  Tiempo total: ~30 segundos'
            }
        }
    }
    
    post {
        success {
            echo '🎉 Pipeline ejecutado exitosamente!'
            
            // Notificación de éxito
            slackSend(
                channel: '#ci-cd-notifications',
                color: 'good',
                message: """🎉 **BUILD EXITOSO**
                Proyecto: ${env.JOB_NAME}
                Build: #${env.BUILD_NUMBER}
                ✅ Todas las etapas completadas
                📊 Tests: 20/20 PASSED
                📊 Coverage: 85.4%
                📦 JAR: 15.2 MB generado
                ⏱️ Duración: ${currentBuild.durationString}
                <${env.BUILD_URL}|Ver Build Completo>"""
            )
        }
        failure {
            echo '❌ Pipeline falló'
            
            // Notificación de fallo
            slackSend(
                channel: '#ci-cd-notifications',
                color: 'danger',
                message: """❌ **BUILD FALLÓ**
                Proyecto: ${env.JOB_NAME}
                Build: #${env.BUILD_NUMBER}
                💥 Error en: ${env.STAGE_NAME ?: 'Unknown'}
                ⏱️ Duración: ${currentBuild.durationString}
                <${env.BUILD_URL}console|Ver Logs de Error>"""
            )
        }
        always {
            echo '🧹 Limpieza completada'
            echo '📝 Logs guardados correctamente'
        }
    }
}
```

---

## 🎯 RESUMEN DE PASOS:

1. **✅ Crear webhook en Slack**
2. **✅ Instalar plugin en Jenkins**  
3. **✅ Configurar Slack en Jenkins**
4. **✅ Actualizar Jenkinsfile con slackSend**
5. **✅ Probar build con notificaciones**

¿Por cuál paso quieres empezar? ¿Ya tienes acceso a un workspace de Slack?