# 🎯 INSTALACIÓN JENKINS - PASO A PASO VISUAL
# ==========================================

## 🔥 INSTALACIÓN RÁPIDA DE JENKINS

### 📥 **PASO 1: DESCARGAR JENKINS**

```
🌐 URL: https://www.jenkins.io/download/
📦 Descargar: Jenkins LTS (Windows)
📂 Archivo: jenkins.msi (aproximadamente 90MB)
```

### 🔧 **PASO 2: INSTALACIÓN**

```
1. 🖱️ Ejecutar jenkins.msi como Administrador
2. 📍 Ruta de instalación: C:\Jenkins (recomendado)
3. ✅ Instalar como servicio de Windows: SÍ
4. 👤 Ejecutar como: Local System Account
5. 🔌 Puerto: 8080 (por defecto)
6. ⚡ Inicio automático: SÍ
```

### 🚀 **PASO 3: CONFIGURACIÓN INICIAL**

#### **A. Primer acceso:**
```
🌐 Abrir navegador: http://localhost:8080
⏳ Esperar: Jenkins puede tardar 2-3 minutos en iniciar completamente
```

#### **B. Unlock Jenkins:**
```
📂 Buscar archivo: C:\ProgramData\Jenkins\.jenkins\secrets\initialAdminPassword
📋 Copiar contenido del archivo
🔐 Pegar en "Administrator password"
```

#### **C. Instalar plugins:**
```
Opción recomendada: "Install suggested plugins"
⏳ Tiempo estimado: 5-10 minutos
```

#### **D. Crear usuario admin:**
```
👤 Username: admin
🔐 Password: [tu password seguro]
📧 Email: tu-email@ejemplo.com
📝 Full Name: Jenkins Admin
```

### 🔌 **PASO 4: INSTALAR PLUGINS NECESARIOS**

**Dashboard → Manage Jenkins → Plugins → Available**

```
📋 Lista de plugins a instalar:
✅ Slack Notification Plugin
✅ SonarQube Scanner Plugin  
✅ Pipeline Maven Integration Plugin
✅ JaCoCo Plugin
✅ Blue Ocean (opcional - interfaz moderna)
```

### ⚙️ **PASO 5: CONFIGURAR HERRAMIENTAS**

**Dashboard → Manage Jenkins → Global Tool Configuration**

#### **Maven:**
```
Name: Maven-3.9.4
✅ Install automatically
Version: 3.9.4
```

#### **JDK:**
```
Name: JDK-17
✅ Install automatically
Installer: Eclipse Temurin 17
```

### 🌐 **PASO 6: CONFIGURAR SONARCLOUD**

**Dashboard → Manage Jenkins → Configure System**

#### **SonarQube Servers:**
```
Name: SonarCloud
Server URL: https://sonarcloud.io
✅ Environment variables: SONAR_SCANNER_OPTS
```

#### **Crear Credencial:**
**Manage Jenkins → Credentials → System → Global → Add Credentials**
```
Kind: Secret text
Secret: e6ec42646e20dadc593a99506c3b64a81e1595ba
ID: sonarcloud-token
Description: Token SonarCloud para análisis
```

## 📢 CONFIGURACIÓN SLACK

### 🤖 **CREAR BOT DE SLACK**

#### **Paso 1: Crear App**
```
🌐 URL: https://api.slack.com/apps
🔄 "Create New App" → "From scratch"
📝 App Name: Jenkins CI/CD Bot
🏢 Workspace: [Tu workspace]
```

#### **Paso 2: Configurar Permisos**
```
📂 OAuth & Permissions → Bot Token Scopes:
✅ chat:write
✅ chat:write.public  
✅ channels:read
✅ files:write (opcional)
```

#### **Paso 3: Instalar en Workspace**
```
📦 Install App to Workspace
🔑 Copiar "Bot User OAuth Token": xoxb-xxxxx...
```

#### **Paso 4: Crear Canal**
```
📢 Crear canal: #ci-cd-notifications
🤖 Invitar bot: /invite @Jenkins CI/CD Bot
```

### 🔗 **CONFIGURAR SLACK EN JENKINS**

**Dashboard → Manage Jenkins → Configure System → Slack**

```
Workspace: tu-workspace.slack.com
✅ Use Jenkins URL: http://localhost:8080
Default channel: #ci-cd-notifications
Integration Token Credential ID: [crear credencial]
```

#### **Crear Credencial Slack:**
```
Kind: Secret text
Secret: xoxb-[tu-bot-token-completo]
ID: slack-token
Description: Slack Bot Token para notificaciones
```

#### **Probar Conexión:**
```
📋 "Test Connection" debe mostrar: ✅ Success
```

## 🚀 **CREAR PIPELINE**

### **Nuevo Job:**
```
📂 Dashboard → New Item
📝 Name: vg-ms-students-pipeline
📋 Type: Pipeline
✅ OK
```

### **Configuración Pipeline:**
```
📂 Pipeline → Definition: Pipeline script from SCM
🔗 SCM: Git
📥 Repository URL: https://github.com/Omarrivv/omarrivv_vg-ms-students.git
🌿 Branch: */main
📄 Script Path: Jenkinsfile
```

### **Ejecutar Build:**
```
🚀 "Build Now"
📊 Ver progreso en tiempo real
📢 Verificar notificaciones en Slack
```

## ✅ **VERIFICACIONES FINALES**

### **Jenkins funcionando:**
```
✅ Servicio Windows corriendo
✅ Web accesible: http://localhost:8080  
✅ Plugins instalados
✅ Herramientas configuradas
```

### **SonarCloud:**
```
✅ Token configurado
✅ Conexión establecida  
✅ Proyecto vinculado
```

### **Slack:**
```
✅ Bot creado y configurado
✅ Canal #ci-cd-notifications activo
✅ Test connection exitoso
```

### **Pipeline:**
```
✅ Job creado
✅ Repositorio conectado
✅ Jenkinsfile detectado
✅ Build ejecutándose
```

## 🎊 **¡LISTO PARA USAR!**

Una vez completados estos pasos:
- ✅ Jenkins ejecutará automáticamente el pipeline
- ✅ SonarCloud analizará el código
- ✅ Slack enviará notificaciones de estado
- ✅ JMeter podrá ejecutarse desde el pipeline

**🔄 Próximo paso: Configurar JMeter cuando Jenkins + Slack esté funcionando**