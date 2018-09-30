cd ../
#keytool -keystore keystore.p12 -genkey -alias selfsigned_localhost_sslserver -keyalg RSA -keysize 4096 -validity 700 -keypass ted -storepass ted_project
keytool -genkeypair -alias tomcat -storetype PKCS12 -keyalg RSA -keysize 2048  -keystore keystore.p12
#password: ted_project
keytool -export -keystore keystore.p12 -alias tomcat -file tedCertificate.crt
keytool -importcert -file tedCertificate.crt -alias tomcat -keystore %JRE_HOME%\lib\security\cacerts
