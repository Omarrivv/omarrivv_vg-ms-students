# 🎮 DASHBOARD DE COMANDOS - CONTROL CENTER

<div align="center">

![Command Center](https://img.shields.io/badge/🎮_Command_Center-Quick_Actions-red?style=for-the-badge)
![Status](https://img.shields.io/badge/STATUS-🟢_READY-brightgreen?style=for-the-badge)

</div>

---

## 🚀 **COMANDOS PRINCIPALES**

<div align="center">

### 🎯 **ACCIONES RÁPIDAS**

| 🔥 **COMANDO** | 📋 **DESCRIPCIÓN** | ⏱️ **TIEMPO** |
|:---:|:---:|:---:|
| **`.\scripts\run-full-pipeline.ps1`** | 🚀 **PIPELINE COMPLETO** | ~15 min |
| **`mvn test`** | 🧪 **SOLO PRUEBAS** | ~5 min |
| **`.\scripts\run-sonarcloud-analysis.ps1`** | 📊 **ANÁLISIS CALIDAD** | ~3 min |
| **`.\scripts\run-jmeter.ps1`** | ⚡ **PRUEBAS CARGA** | ~5 min |
| **`docker-compose up`** | 🐳 **LEVANTAR TODO** | ~2 min |

</div>

---

## 🧪 **COMANDOS DE TESTING**

```bash
# 🎯 TESTING ESPECÍFICO
mvn test -Dtest="StudentTest,StudentServiceTest"     # Solo pruebas unitarias
mvn integration-test                                  # Pruebas de integración
mvn test jacoco:report                               # Con reporte cobertura
mvn clean test -Dmaven.test.failure.ignore=true     # Ignorar fallos

# 🔍 ANÁLISIS ESPECÍFICO  
mvn sonar:sonar -Dsonar.login=$SONAR_TOKEN          # Solo SonarCloud
mvn checkstyle:check                                 # Verificar estilo código
mvn spotbugs:check                                   # Detectar bugs
```

---

## 🚀 **COMANDOS DE DESPLIEGUE**

```bash
# 🏗️ BUILD Y PACKAGE
mvn clean compile                    # Solo compilar
mvn clean package                    # Crear JAR
mvn clean package -DskipTests        # JAR sin pruebas
mvn spring-boot:build-image          # Crear imagen Docker

# 🐳 DOCKER COMMANDS
docker build -t vg-ms-students .                    # Build imagen
docker run -p 8080:8080 vg-ms-students             # Ejecutar contenedor
docker-compose up -d                                # Levantar stack completo
docker-compose logs -f vg-ms-students               # Ver logs
```

---

## 📊 **COMANDOS DE MONITOREO**

```bash
# 📈 MÉTRICAS Y LOGS
curl http://localhost:8080/actuator/health          # Health check
curl http://localhost:8080/actuator/metrics         # Métricas app
curl http://localhost:8080/actuator/prometheus      # Métricas Prometheus
docker stats                                        # Stats contenedores

# 🔍 DEBUGGING
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
tail -f logs/application.log                        # Ver logs en tiempo real
```

---

## 🛠️ **COMANDOS DE DESARROLLO**

```bash
# 🔧 DEVELOPMENT TOOLS
mvn dependency:tree                  # Ver dependencias
mvn versions:display-updates         # Actualizar dependencias
mvn help:effective-pom              # Ver POM efectivo
mvn clean compile exec:java         # Ejecutar sin package

# 🎨 CODE QUALITY
mvn fmt:format                      # Formatear código
mvn license:format                  # Formatear licencias
mvn dependency:analyze              # Analizar dependencias
```

---

## ⚡ **SCRIPTS PERSONALIZADOS**

<div align="center">

### 📂 **Scripts Disponibles en `./scripts/`**

| 🔧 **Script** | 🎯 **Función** | 📱 **Uso** |
|:---:|:---:|:---:|
| **`run-full-pipeline.ps1`** | Pipeline CI/CD completo | `.\scripts\run-full-pipeline.ps1` |
| **`run-tests.ps1`** | Solo testing + cobertura | `.\scripts\run-tests.ps1` |
| **`run-sonarcloud-analysis.ps1`** | Análisis SonarCloud | `.\scripts\run-sonarcloud-analysis.ps1 -SonarToken $env:SONAR_TOKEN` |
| **`run-jmeter.ps1`** | Pruebas de carga JMeter | `.\scripts\run-jmeter.ps1 -TestPlan load-test` |
| **`build-docker.ps1`** | Construir imagen Docker | `.\scripts\build-docker.ps1` |

</div>

---

## 🌐 **URLs DE ACCESO RÁPIDO**

<div align="center">

| 🌍 **Servicio** | 🔗 **URL Local** | 🌐 **URL Nube** |
|:---:|:---:|:---:|
| **API Swagger** | `http://localhost:8080/swagger-ui.html` | - |
| **Actuator Health** | `http://localhost:8080/actuator/health` | - |
| **Jenkins** | `http://localhost:8080` | - |
| **MongoDB** | `localhost:27017` | - |
| **SonarCloud** | - | `https://sonarcloud.io/project/overview?id=vallegrande_vg-ms-students` |

</div>

---

## 🔑 **VARIABLES DE ENTORNO**

```bash
# 🔧 CONFIGURACIÓN NECESARIA
export SONAR_TOKEN="tu-sonar-token-aqui"
export SLACK_TOKEN="xoxb-tu-slack-token"  
export MONGODB_URI="mongodb://localhost:27017/vg_students"
export SPRING_PROFILES_ACTIVE="local"

# 🐳 DOCKER
export DOCKER_IMAGE_NAME="vg-ms-students"
export DOCKER_TAG="latest"
```

---

## 🆘 **COMANDOS DE EMERGENCIA**

```bash
# 🚨 TROUBLESHOOTING
docker system prune -a              # Limpiar Docker completamente
mvn dependency:purge-local-repository # Limpiar cache Maven
rm -rf target/                       # Limpiar build
pkill -f java                       # Matar procesos Java
netstat -tulpn | grep :8080         # Ver qué usa puerto 8080

# 🔄 RESET COMPLETO
git clean -fdx                      # Limpiar archivos no trackeados
git reset --hard HEAD               # Reset duro al último commit
mvn clean                           # Limpiar Maven
docker-compose down -v              # Bajar containers + volúmenes
```

---

## 📊 **COMANDOS DE ANÁLISIS**

```bash
# 📈 MÉTRICAS DE CÓDIGO
cloc src/                           # Contar líneas de código
find . -name "*.java" | xargs wc -l # Líneas de código Java
du -sh target/                      # Tamaño del build
```

---

## 🎯 **SHORTCUTS ESPECÍFICOS DEL PROYECTO**

```bash
# 🎓 VG-MS-STUDENTS ESPECÍFICOS
curl -X GET "http://localhost:8080/api/v1/students" -H "Institution-Id: inst-001"
curl -X POST "http://localhost:8080/api/v1/students" -H "Content-Type: application/json" -H "Institution-Id: inst-001" -d @test-data/student.json

# 📊 REPORTES
curl "http://localhost:8080/api/v1/reports/students" -H "Institution-Id: inst-001"
curl "http://localhost:8080/api/v1/reports/csv" -H "Institution-Id: inst-001" -o students-report.csv
```

---

<div align="center">

## 🎮 **CONTROL PANEL COMPLETO**

### ✅ **TODO LISTO PARA USAR**

**¡Copia, pega y ejecuta cualquier comando!** 🚀

---

### 🔥 **COMANDO MÁS USADO**

```bash
# 🎯 EL COMANDO MÁGICO - EJECUTA TODO
.\scripts\run-full-pipeline.ps1
```

**Este comando ejecuta automáticamente:**
- ✅ Compilación
- ✅ Pruebas unitarias
- ✅ Análisis SonarCloud  
- ✅ Pruebas de carga
- ✅ Generación reportes
- ✅ Notificaciones Slack

---

*Dashboard creado para máxima productividad* ⚡

</div>