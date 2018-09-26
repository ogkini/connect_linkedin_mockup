cd ../
keytool -keystore keystore.p12 -genkey -alias selfsigned_localhost_sslserver -keyalg RSA -keysize 4096 -validity 700 -keypass changeit -storepass ted_project
