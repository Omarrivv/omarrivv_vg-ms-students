# ===============================================
# SCRIPT PARA SUBIR PROYECTO A GITHUB
# ===============================================
# Ejecutar este script en PowerShell como administrador

Write-Host "🚀 SUBIENDO PROYECTO A GITHUB..." -ForegroundColor Green

# 1. Verificar estado actual
Write-Host "📋 Estado actual del repositorio:" -ForegroundColor Yellow
git status

# 2. Configurar repositorio remoto
Write-Host "🔗 Configurando repositorio remoto..." -ForegroundColor Yellow
git remote set-url origin https://github.com/Omarrivv/omarrivv_vg-ms-students.git

# 3. Verificar configuración remota
Write-Host "✅ Verificando configuración remota:" -ForegroundColor Yellow
git remote -v

# 4. Subir a GitHub
Write-Host "⬆️ Subiendo archivos a GitHub..." -ForegroundColor Yellow
git push -u origin main

Write-Host "🎉 ¡PROYECTO SUBIDO EXITOSAMENTE!" -ForegroundColor Green
Write-Host "🌐 Tu repositorio está disponible en: https://github.com/Omarrivv/omarrivv_vg-ms-students" -ForegroundColor Cyan

# 5. Mostrar siguiente paso
Write-Host ""
Write-Host "📋 SIGUIENTE PASO - CONFIGURAR SONARCLOUD:" -ForegroundColor Magenta
Write-Host "1. 🌐 Ir a: https://sonarcloud.io/projects/create" -ForegroundColor White
Write-Host "2. 🔗 Seleccionar 'GitHub'" -ForegroundColor White
Write-Host "3. 📁 Buscar y seleccionar: omarrivv_vg-ms-students" -ForegroundColor White
Write-Host "4. 🎯 Configurar proyecto con:" -ForegroundColor White
Write-Host "   - Organization: omarrivv" -ForegroundColor Gray
Write-Host "   - Project Key: omarrivv_vg-ms-students" -ForegroundColor Gray
Write-Host ""

pause