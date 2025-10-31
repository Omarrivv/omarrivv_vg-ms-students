pipeline {
    agent any
    
    tools {
        maven 'Maven-3.9.4'
        jdk 'JDK-17'
    }
    
    environment {
        SONAR_HOST_URL = 'https://sonarcloud.io'
        SONAR_ORGANIZATION = 'omarrivv'
        SONAR_TOKEN = credentials('sonarcloud-token')
        SLACK_CHANNEL = '#ci-cd-notifications'
        DOCKER_IMAGE = 'vg-ms-students'
    }
    
    stages {
        stage('🚀 Checkout') {
            steps {
                echo '📁 Clonando repositorio...'
                checkout scm
                
                // Notificar inicio en Slack
                slackSend(
                    channel: env.SLACK_CHANNEL,
                    color: '#439FE0',
                    message: """🚀 *Build Iniciado*
                    Proyecto: ${env.JOB_NAME}
                    Build: #${env.BUILD_NUMBER}
                    Branch: ${env.BRANCH_NAME ?: 'main'}
                    Iniciado por: ${env.BUILD_USER ?: 'Sistema'}
                    <${env.BUILD_URL}|Ver Build>"""
                )
            }
        }
        
        stage('🔧 Build') {
            steps {
                echo '⚙️ Compilando aplicación...'
                bat '''
                    echo "Verificando Java y Maven..."
                    java -version
                    mvn -version
                    
                    echo "Compilando proyecto..."
                    mvn clean compile -DskipTests
                '''
            }
        }
        
        stage('🧪 Unit Tests') {
            steps {
                echo '🔬 Ejecutando pruebas unitarias...'
                bat '''
                    mvn test -Dspring.profiles.active=test
                '''
            }
            post {
                always {
                    // Publicar resultados de pruebas
                    publishTestResults testResultsPattern: 'target/surefire-reports/*.xml'
                    
                    // Publicar reporte de cobertura JaCoCo
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'target/site/jacoco',
                        reportFiles: 'index.html',
                        reportName: '📊 JaCoCo Coverage Report'
                    ])
                    
                    // Notificar resultados de pruebas
                    script {
                        def testResults = currentBuild.rawBuild.getAction(hudson.tasks.test.AbstractTestResultAction.class)
                        if (testResults != null) {
                            def total = testResults.totalCount
                            def failed = testResults.failCount
                            def skipped = testResults.skipCount
                            def passed = total - failed - skipped
                            
                            slackSend(
                                channel: env.SLACK_CHANNEL,
                                color: failed > 0 ? 'warning' : 'good',
                                message: """🧪 *Resultados de Pruebas Unitarias*
                                ✅ Pasaron: ${passed}
                                ❌ Fallaron: ${failed}
                                ⏭️ Omitidas: ${skipped}
                                📊 Total: ${total}
                                <${env.BUILD_URL}testReport|Ver Detalles>"""
                            )
                        }
                    }
                }
            }
        }
        
        stage('🌐 SonarCloud Analysis') {
            steps {
                echo '🔍 Ejecutando análisis de SonarCloud (SonarQube en la nube)...'
                withSonarQubeEnv('SonarCloud') {
                    withCredentials([string(credentialsId: 'sonarcloud-token', variable: 'SONAR_TOKEN')]) {
                        bat '''
                            mvn sonar:sonar ^
                            -Dsonar.projectKey=vallegrande_vg-ms-students ^
                            -Dsonar.organization=vallegrande-org ^
                            -Dsonar.projectName="VG MS Students" ^
                            -Dsonar.host.url=https://sonarcloud.io ^
                            -Dsonar.login=%SONAR_TOKEN% ^
                            -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml ^
                            -Dsonar.junit.reportPaths=target/surefire-reports ^
                            -Dsonar.java.coveragePlugin=jacoco ^
                            -Dsonar.java.source=17 ^
                            -Dsonar.qualitygate.wait=true
                        '''
                    }
                }
                echo '✅ Análisis de SonarCloud completado'
            }
        }
        
        stage('🚦 Quality Gate') {
            steps {
                echo '⏳ Esperando Quality Gate de SonarCloud...'
                timeout(time: 3, unit: 'MINUTES') {
                    script {
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            slackSend(
                                channel: env.SLACK_CHANNEL,
                                color: 'danger',
                                message: """❌ *SonarCloud Quality Gate FAILED*
                                Estado: ${qg.status}
                                🔗 <https://sonarcloud.io/project/overview?id=vallegrande_vg-ms-students|Ver en SonarCloud>
                                📊 Proyecto: VG MS Students
                                🏗️ Build: ${env.BUILD_NUMBER}"""
                            )
                            error "Quality Gate falló: ${qg.status}"
                        } else {
                            slackSend(
                                channel: env.SLACK_CHANNEL,
                                color: 'good',
                                message: """✅ *SonarCloud Quality Gate PASSED*
                                Código cumple con estándares de calidad
                                🔗 <https://sonarcloud.io/project/overview?id=vallegrande_vg-ms-students|Ver en SonarCloud>
                                📊 Cobertura y calidad aprobadas
                                🏗️ Build: ${env.BUILD_NUMBER}"""
                            )
                        }
                    }
                }
            }
        }
        
        stage('📦 Package') {
            steps {
                echo '📦 Empaquetando aplicación...'
                bat '''
                    mvn clean package -DskipTests
                '''
            }
            post {
                success {
                    // Archivar artefactos
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                    
                    slackSend(
                        channel: env.SLACK_CHANNEL,
                        color: 'good',
                        message: """📦 *Artefacto Generado*
                        JAR: vg-ms-students-1.0.jar
                        <${env.BUILD_URL}artifact|Descargar Artefacto>"""
                    )
                }
            }
        }
        
        stage('🚀 Performance Tests') {
            steps {
                echo '⚡ Ejecutando pruebas de rendimiento con JMeter...'
                bat '''
                    echo "Iniciando aplicación para pruebas..."
                    start /B java -jar target/vg-ms-students-1.0.jar --server.port=8081
                    
                    echo "Esperando que la aplicación se inicie..."
                    timeout /t 30
                    
                    echo "Ejecutando pruebas JMeter..."
                    cd performance-tests
                    "C:\\JMeter\\bin\\jmeter.bat" -n -t student-api-test.jmx -l results.jtl -e -o jmeter-report
                    
                    echo "Deteniendo aplicación..."
                    taskkill /F /IM java.exe
                '''
            }
            post {
                always {
                    // Publicar reporte JMeter
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'performance-tests/jmeter-report',
                        reportFiles: 'index.html',
                        reportName: '⚡ JMeter Performance Report'
                    ])
                    
                    // Archivar resultados JMeter
                    archiveArtifacts artifacts: 'performance-tests/results.jtl', fingerprint: true
                    
                    slackSend(
                        channel: env.SLACK_CHANNEL,
                        color: 'good',
                        message: """⚡ *Pruebas de Performance Completadas*
                        <${env.BUILD_URL}JMeter_20Performance_20Report|Ver Reporte JMeter>"""
                    )
                }
            }
        }
        
        stage('🐳 Docker Build') {
            when {
                anyOf {
                    branch 'main'
                    branch 'master'
                    branch 'develop'
                }
            }
            steps {
                echo '🐳 Construyendo imagen Docker...'
                bat '''
                    docker build -t %DOCKER_IMAGE%:%BUILD_NUMBER% .
                    docker tag %DOCKER_IMAGE%:%BUILD_NUMBER% %DOCKER_IMAGE%:latest
                    
                    echo "Imagen Docker creada: %DOCKER_IMAGE%:%BUILD_NUMBER%"
                '''
            }
            post {
                success {
                    slackSend(
                        channel: env.SLACK_CHANNEL,
                        color: 'good',
                        message: """🐳 *Imagen Docker Creada*
                        Imagen: ${env.DOCKER_IMAGE}:${env.BUILD_NUMBER}
                        Tag: ${env.DOCKER_IMAGE}:latest"""
                    )
                }
            }
        }
    }
    
    post {
        always {
            echo '🧹 Limpiando workspace...'
            cleanWs()
        }
        
        success {
            slackSend(
                channel: env.SLACK_CHANNEL,
                color: 'good',
                message: """✅ *BUILD EXITOSO* 🎉
                Proyecto: ${env.JOB_NAME}
                Build: #${env.BUILD_NUMBER}
                Duración: ${currentBuild.durationString}
                Branch: ${env.BRANCH_NAME ?: 'main'}
                
                📊 Reportes disponibles:
                • <${env.BUILD_URL}testReport|Pruebas Unitarias>
                • <${env.BUILD_URL}JaCoCo_20Coverage_20Report|Cobertura de Código>
                • <${env.SONAR_HOST_URL}/dashboard?id=vg-ms-students|Análisis SonarQube>
                • <${env.BUILD_URL}JMeter_20Performance_20Report|Pruebas de Performance>
                
                <${env.BUILD_URL}|Ver Build Completo>"""
            )
        }
        
        failure {
            slackSend(
                channel: env.SLACK_CHANNEL,
                color: 'danger',
                message: """❌ *BUILD FALLIDO* 💥
                Proyecto: ${env.JOB_NAME}
                Build: #${env.BUILD_NUMBER}
                Duración: ${currentBuild.durationString}
                Branch: ${env.BRANCH_NAME ?: 'main'}
                
                <${env.BUILD_URL}console|Ver Log del Error>
                <${env.BUILD_URL}|Ir al Build>
                
                @channel Por favor revisar el error."""
            )
        }
        
        unstable {
            slackSend(
                channel: env.SLACK_CHANNEL,
                color: 'warning',
                message: """⚠️ *BUILD INESTABLE* 
                Proyecto: ${env.JOB_NAME}
                Build: #${env.BUILD_NUMBER}
                
                Hay pruebas fallidas o problemas de calidad.
                <${env.BUILD_URL}|Revisar Build>"""
            )
        }
    }
}