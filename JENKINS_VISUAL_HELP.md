# 🎯 CONFIGURACIÓN VISUAL JENKINS - MAVEN Y JDK
# ==============================================

## 📍 UBICACIÓN EXACTA EN JENKINS

### **Ya estás en la página correcta:**
```
✅ Dashboard → Manage Jenkins → Tools
✅ URL: localhost:8080/manage/configureTools/
```

## 🔧 CONFIGURAR MAVEN

### **Buscar sección "Maven":**
1. 📊 **Scroll down** en la página actual
2. 🔍 **Buscar**: "Maven installations" 
3. ➕ **Click**: "Add Maven"

### **Configuración Maven:**
```
📝 Name: Maven-3.9.4
✅ Install automatically: ☑️ CHECKED
📦 Version: 3.9.4 (seleccionar de dropdown)
```

## ☕ CONFIGURAR JDK  

### **Buscar sección "JDK":**
1. 📊 **Continue scrolling down** 
2. 🔍 **Buscar**: "JDK installations"
3. ➕ **Click**: "Add JDK"

### **Configuración JDK:**
```
📝 Name: JDK-17
✅ Install automatically: ☑️ CHECKED  
🏷️ Add Installer: Eclipse Temurin
📦 Version: jdk-17.0.9+9 (o similar disponible)
```

## 💾 GUARDAR Y PROBAR

### **Guardar cambios:**
```
💾 Scroll to bottom → Click "Save"
```

### **Verificar configuración:**
```
🔄 Dashboard → ms-students-pipeline
🚀 Click "Build Now"  
📊 Monitorear "Console Output"
```

## 🆘 SOLUCIÓN DE PROBLEMAS

### **Si no encuentras las secciones:**

#### **Maven installations:**
- Debe aparecer después de "Maven Configuration"
- Si no aparece, instalar "Maven Integration Plugin"

#### **JDK installations:**  
- Generalmente está en la parte superior
- Si no aparece, puede estar bajo "Java"

### **Si sigues con problemas:**
1. 🔌 **Verificar plugins instalados**:
   ```
   Dashboard → Manage Jenkins → Plugins
   Buscar: "Maven Integration Plugin"
   ```

2. 🔄 **Reiniciar Jenkins**:
   ```
   Dashboard → Manage Jenkins → Restart Safely
   ```

## ⚡ ALTERNATIVA RÁPIDA

### **Si quieres probar inmediatamente:**
Podemos modificar el Jenkinsfile para usar herramientas por defecto:

```groovy
// Cambiar esto:
tools {
    maven 'Maven-3.9.4'  
    jdk 'JDK-17'
}

// Por esto (temporalmente):
tools {
    maven tool: 'Maven', type: 'maven'
    jdk tool: 'Java', type: 'jdk'  
}
```

## 🎯 PRÓXIMOS PASOS

Una vez configurado Maven y JDK:
1. ✅ **Build exitoso**
2. ✅ **Configurar Slack** (siguiente paso)
3. ✅ **JMeter integration**

---
💡 **¿En qué paso exacto necesitas ayuda?** 
- 🔍 ¿No encuentras las secciones Maven/JDK?
- ⚙️ ¿Problemas al configurar?
- 🚀 ¿Listo para ejecutar build?