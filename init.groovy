// Configuración automática de Jenkins
import jenkins.model.*
import hudson.security.*
import hudson.security.csrf.DefaultCrumbIssuer
import jenkins.security.s2m.AdminWhitelistRule
import hudson.markup.RawHtmlMarkupFormatter

def instance = Jenkins.getInstance()

// Configurar usuario admin
def hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount("admin", "admin123")
instance.setSecurityRealm(hudsonRealm)

// Configurar autorización
def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
strategy.setAllowAnonymousRead(false)
instance.setAuthorizationStrategy(strategy)

// Habilitar CSRF Protection
instance.setCrumbIssuer(new DefaultCrumbIssuer(true))

// Configurar herramientas
def descriptor = instance.getDescriptor("hudson.tasks.Maven\$MavenInstallation")
def mavenInstallations = [
  new hudson.tasks.Maven.MavenInstallation("Maven-3.9.4", "/usr/share/maven")
]
descriptor.setInstallations(mavenInstallations.toArray(new hudson.tasks.Maven.MavenInstallation[0]))

// Guardar configuración
instance.save()

println("Jenkins configurado automáticamente:")
println("- Usuario: admin")
println("- Password: admin123")
println("- Maven instalado y configurado")
println("- Seguridad habilitada")