# 🔔 Guía Rápida: Configuración de Slack para Jenkins

## 🚀 Pasos Rápidos para Integrar Slack

### 1️⃣ Crear Slack App
1. Ve a: https://api.slack.com/apps
2. **"Create New App"** → **"From scratch"**
3. Configurar:
   ```
   App Name: Jenkins CI/CD Bot
   Workspace: [Tu workspace de Slack]
   ```

### 2️⃣ Configurar Permisos
En tu app → **"OAuth & Permissions"** → **"Bot Token Scopes"**:
```
✅ chat:write
✅ chat:write.public  
✅ channels:read
✅ groups:read
```

### 3️⃣ Instalar App
1. **"Install to Workspace"** → **"Allow"**
2. **COPIAR** el token que empieza con `xoxb-`

### 4️⃣ Configurar Canal
1. En Slack, crear: **#ci-cd-notifications**
2. Invitar bot: `/invite @Jenkins CI/CD Bot`

### 5️⃣ Configurar Jenkins
1. **Manage Jenkins** → **Manage Plugins** → Instalar **"Slack Notification Plugin"**
2. **Manage Credentials** → **Add Credentials**:
   ```
   Kind: Secret text
   Secret: xoxb-tu-token-aquí
   ID: slack-token
   ```
3. **Configure System** → **Slack**:
   ```
   Workspace: tu-workspace
   Default channel: #ci-cd-notifications
   Credential: slack-token
   ```

### 6️⃣ Test de Conexión
Click **"Test Connection"** → Debe aparecer **"Success"** ✅

---

## 📝 Variables a Personalizar en el Pipeline

**En el Jenkinsfile, cambiar estas líneas**:
```groovy
// Línea ~9: Tu organización SonarCloud
SONAR_ORGANIZATION = 'TU-ORGANIZACION-AQUI'

// Línea ~10: Tu project key de SonarCloud  
SONAR_PROJECT_KEY = 'TU-USUARIO_TU-REPOSITORIO'

// Línea ~11: Tu canal de Slack
SLACK_CHANNEL = '#tu-canal-aqui'

// En TODAS las funciones slackSend, cambiar:
teamDomain: 'TU-WORKSPACE-SLACK'  // ⚠️ CRÍTICO
```

---

## ✅ Verificación Rápida

### ¿Funciona correctamente?
1. **Jenkins**: Build ejecuta sin errores
2. **Slack**: Aparecen notificaciones en el canal
3. **Enlaces**: Links a SonarCloud y Jenkins funcionan

### Si NO funciona:
1. **Verificar token**: Debe empezar con `xoxb-`
2. **Verificar canal**: Bot debe estar invitado
3. **Verificar workspace**: Nombre exacto sin espacios
4. **Test conexión**: En Configure System

---

## 🎯 Ejemplo de Notificación Exitosa

Deberías ver en Slack:
```
🚀 INICIANDO CI/CD PIPELINE
📁 Proyecto: MS Students Microservice  
🔧 Build: #15
👤 Usuario: Sistema
⏰ Iniciado: 31/10/2025 14:30:25

✅ BUILD COMPLETADO - Compilación exitosa para build #15

🧪 UNIT TESTS COMPLETADOS
📊 Total: 15 tests
✅ Pasados: 14
⚠️ Fallidos: 1
📈 Cobertura: 85.4%

🎉 PIPELINE COMPLETADO EXITOSAMENTE 🎉
```

---

**¡Listo!** Ahora tienes Jenkins + SonarCloud + Slack completamente integrados 🚀