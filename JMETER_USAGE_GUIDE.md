# 🚀 **Guía Completa de JMeter para MS Students**

## 📁 **Archivos Generados**
```
MS-Students-LoadTest.jmx           # Plan principal de pruebas de carga
MS-Students-Validation-Test.jmx    # Plan específico para validaciones
jmeter-test-data.csv               # Datos de prueba (15 estudiantes)
```

---

## 🔧 **1. Instalación de JMeter**

### **Descarga e Instalación:**
1. **Descargar:** https://jmeter.apache.org/download_jmeter.cgi
2. **Extraer:** `apache-jmeter-5.5.zip` a tu directorio preferido
3. **Ejecutar:** `bin/jmeter.bat` (Windows) o `bin/jmeter.sh` (Linux/Mac)

### **Requisitos:**
- ✅ Java 8 o superior
- ✅ Mínimo 2GB RAM
- ✅ Puerto 8080 libre para el microservicio

---

## 🎯 **2. Configuración del Proyecto**

### **Paso 1: Preparar el Entorno**
```bash
# 1. Iniciar tu microservicio
mvn spring-boot:run

# 2. Verificar que está funcionando
curl http://localhost:8080/actuator/health
```

### **Paso 2: Copiar Archivos de Prueba**
```bash
# Asegúrate de que estos archivos estén en el mismo directorio:
MS-Students-LoadTest.jmx
MS-Students-Validation-Test.jmx
jmeter-test-data.csv
```

---

## 🎮 **3. Uso de JMeter GUI**

### **Abrir Plan de Pruebas Principal:**
1. **Iniciar JMeter:** `jmeter.bat`
2. **File > Open:** Seleccionar `MS-Students-LoadTest.jmx`
3. **Verificar configuración:**
   - Variable `base_url`: http://localhost:8080
   - CSV Data Set: ruta a `jmeter-test-data.csv`

### **Configurar Variables:**
```
Test Plan > User Defined Variables:
├── base_url: http://localhost:8080
├── user_id: user123
└── institution_id: inst456
```

### **Ajustar Cargas de Trabajo:**
```
Secretary Operations:
├── Threads: 10 usuarios
├── Ramp-up: 30 segundos
└── Loops: 5 iteraciones

Teacher Operations:
├── Threads: 5 usuarios
├── Ramp-up: 15 segundos
└── Loops: 3 iteraciones

Reports Operations:
├── Threads: 3 usuarios
├── Ramp-up: 10 segundos
└── Loops: 2 iteraciones
```

---

## 🔍 **4. Endpoints Incluidos en las Pruebas**

### **Plan Principal (MS-Students-LoadTest.jmx):**

#### **🏫 Secretary Operations:**
- `GET /students/secretary` - Obtener todos los estudiantes
- `POST /students/secretary/create` - Crear nuevo estudiante
- `GET /students/secretary/search?query={firstName}` - Buscar estudiantes
- `GET /students/secretary/by-grade/{grade}` - Estudiantes por grado
- `GET /students/secretary/statistics` - Estadísticas generales

#### **👩‍🏫 Teacher Operations:**
- `GET /students/teacher/my-students` - Estudiantes del profesor
- `GET /reports/teacher/my-stats` - Estadísticas del profesor

#### **📊 Reports Operations:**
- `GET /reports/secretary/dashboard` - Dashboard principal
- `GET /reports/secretary/overview` - Vista general

### **Plan de Validaciones (MS-Students-Validation-Test.jmx):**
- `POST /validate-students/document` - Validar documento
- `POST /validate-students/enrollment` - Validar matrícula
- `POST /validate-students/grade-capacity` - Validar capacidad
- `POST /validate-students/bulk` - Validación en lotes

---

## 📈 **5. Ejecutar Pruebas**

### **Ejecución desde GUI:**
1. **Seleccionar plan:** Click en "Test Plan"
2. **Limpiar resultados:** `Run > Clear All`
3. **Iniciar prueba:** `Run > Start` o presionar `Ctrl+R`
4. **Monitorear:** Ver "View Results Tree" y "Summary Report"

### **Ejecución desde Línea de Comandos:**
```bash
# Prueba principal
jmeter -n -t MS-Students-LoadTest.jmx -l results-main.jtl -e -o report-main

# Prueba de validaciones
jmeter -n -t MS-Students-Validation-Test.jmx -l results-validation.jtl -e -o report-validation
```

---

## 📊 **6. Interpretar Resultados**

### **Métricas Clave:**
```
📈 Throughput: Transacciones por segundo
⏱️ Average Response Time: Tiempo promedio de respuesta
🎯 Error Rate: Porcentaje de errores
📏 90th Percentile: 90% de las respuestas bajo este tiempo
🚀 Min/Max: Tiempos mínimos y máximos
```

### **Valores Esperados:**
```yaml
Endpoints de Lectura (GET):
  - Response Time: < 500ms
  - Error Rate: < 1%
  - Throughput: > 50 req/sec

Endpoints de Escritura (POST):
  - Response Time: < 1000ms
  - Error Rate: < 2%
  - Throughput: > 20 req/sec

Validaciones:
  - Response Time: < 200ms
  - Error Rate: < 0.5%
  - Throughput: > 100 req/sec
```

---

## 🛠️ **7. Personalización Avanzada**

### **Modificar Cargas de Trabajo:**
```xml
<!-- En Thread Group -->
<stringProp name="ThreadGroup.num_threads">20</stringProp>     <!-- Usuarios -->
<stringProp name="ThreadGroup.ramp_time">60</stringProp>       <!-- Ramp-up -->
<stringProp name="LoopController.loops">10</stringProp>        <!-- Loops -->
```

### **Agregar Nuevos Endpoints:**
1. **Right-click** en Thread Group > Add > Sampler > HTTP Request
2. **Configurar:**
   - Path: `/nuevo/endpoint`
   - Method: GET/POST
   - Body Data (si es POST)
3. **Agregar Assertion:** Response Assertion

### **Cambiar Datos de Prueba:**
```csv
# Modificar jmeter-test-data.csv
firstName,lastName,documentType,documentNumber,birthDate,gender,address,phone,parentName,parentPhone,parentEmail,studentId,grade,status
NuevoNombre,NuevoApellido,DNI,99999999,2010-01-01,M,Nueva Dirección,999999999,Nuevo Padre,888888888,nuevo@email.com,STU999,1ro Primaria,ACTIVE
```

---

## 🔧 **8. Resolución de Problemas**

### **Errores Comunes:**

#### **Connection Refused:**
```
❌ Problema: El microservicio no está ejecutándose
✅ Solución: mvn spring-boot:run
✅ Verificar: curl http://localhost:8080/actuator/health
```

#### **404 Not Found:**
```
❌ Problema: Endpoint incorrecto
✅ Solución: Verificar la ruta en el código
✅ Revisar: logs del microservicio
```

#### **CSV Data Not Found:**
```
❌ Problema: Archivo jmeter-test-data.csv no encontrado
✅ Solución: Colocar en el mismo directorio que .jmx
✅ O usar ruta absoluta en CSV Data Set Config
```

#### **Memory Issues:**
```bash
# Aumentar memoria de JMeter
export HEAP="-Xms1g -Xmx4g -XX:MaxMetaspaceSize=256m"
```

### **Headers Faltantes:**
```
❌ Problema: Error 400/401
✅ Solución: Verificar headers requeridos:
   - X-User-Id
   - X-Institution-Id  
   - X-User-Roles
   - Content-Type: application/json
```

---

## 📋 **9. Checklist de Ejecución**

### **Pre-Ejecución:**
- [ ] Microservicio ejecutándose (puerto 8080)
- [ ] JMeter instalado y funcionando
- [ ] Archivos .jmx y .csv en el mismo directorio
- [ ] Variables de entorno configuradas

### **Durante la Ejecución:**
- [ ] Monitorear "View Results Tree" para errores
- [ ] Verificar "Summary Report" para métricas
- [ ] Observar logs del microservicio
- [ ] Usar "Graph Results" para visualizar rendimiento

### **Post-Ejecución:**
- [ ] Analizar resultados en reportes HTML
- [ ] Identificar cuellos de botella
- [ ] Documentar hallazgos
- [ ] Ajustar configuración si es necesario

---

## 🎯 **10. Casos de Uso Específicos**

### **Prueba de Stress:**
```
Objetivo: Encontrar punto de quiebre
Configuración:
├── Threads: 50-100
├── Ramp-up: 60s
└── Duration: 10 minutos
```

### **Prueba de Carga:**
```
Objetivo: Comportamiento bajo carga normal
Configuración:
├── Threads: 10-20
├── Ramp-up: 30s
└── Loops: 5-10
```

### **Prueba de Volumen:**
```
Objetivo: Gran cantidad de datos
Configuración:
├── CSV con 1000+ registros
├── Threads: 5-10
└── Loops: hasta agotar CSV
```

---

## 💡 **Tips Adicionales**

### **Optimización:**
- 🚀 Usar HTTP Request Defaults para configuración común
- 📊 Deshabilitar "View Results Tree" en pruebas de carga
- 💾 Guardar resultados en archivos .jtl
- 🔄 Usar modo non-GUI para pruebas extensas

### **Monitoreo:**
- 📈 Usar Grafana + InfluxDB para métricas en tiempo real
- 🎯 Configurar alertas para umbrales críticos
- 📊 Generar reportes HTML automáticos
- 🔍 Correlacionar con métricas del sistema (CPU, RAM)

### **Buenas Prácticas:**
- 🧪 Empezar con cargas pequeñas
- 📋 Documentar configuraciones exitosas
- 🔄 Ejecutar pruebas regularmente
- 📊 Comparar resultados entre versiones

---

## 🚀 **Próximos Pasos**

1. **Ejecutar pruebas básicas** con configuración por defecto
2. **Analizar resultados** e identificar optimizaciones
3. **Integrar con Jenkins** para automatización
4. **Configurar alertas** para monitoreo continuo
5. **Crear dashboards** para seguimiento de rendimiento

---

> **¡Éxito!** 🎉 Tienes todo lo necesario para realizar pruebas de carga completas con JMeter en tu microservicio MS Students.