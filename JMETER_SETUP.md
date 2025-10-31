# 🔧 Configuración JMeter para Jenkins

## 📥 Instalación JMeter

### Opción 1: Descarga Manual
1. Ve a: https://jmeter.apache.org/download_jmeter.cgi
2. Descargar: **Apache JMeter 5.6.3** (Binaries)
3. Extraer en: `C:\Tools\apache-jmeter-5.6.3\`

### Opción 2: PowerShell Automático
```powershell
# Crear directorio
New-Item -Path "C:\Tools" -ItemType Directory -Force

# Descargar JMeter
$url = "https://archive.apache.org/dist/jmeter/binaries/apache-jmeter-5.6.3.zip"
$output = "C:\Tools\apache-jmeter-5.6.3.zip"
Invoke-WebRequest -Uri $url -OutFile $output

# Extraer
Expand-Archive -Path $output -DestinationPath "C:\Tools\" -Force

# Verificar instalación
C:\Tools\apache-jmeter-5.6.3\bin\jmeter.bat -version
```

## ⚙️ Configurar JMeter Tool en Jenkins

1. **Manage Jenkins** → **Global Tool Configuration**
2. **JMeter** → **Add JMeter**:
   ```
   Name: JMeter-5.6.3
   JMETER_HOME: C:\Tools\apache-jmeter-5.6.3
   ```

## 🧪 Plan de Pruebas Base

Crear archivo: `src/test/jmeter/load-test.jmx` (se creará automáticamente)