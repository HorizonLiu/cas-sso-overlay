#!/usr/bin/env bash
keytool -genkey -alias cas -keyalg RSA -keysize 2048 -keypass changeit -storepass changeit -keystore ./etc/thekeystore -dname "CN=cas.example.org,OU=Example,O=Org,L=Beijing,ST=BeiJing,C=US"
keytool -exportcert -alias cas -keystore ./etc/thekeystore -file ./etc/cas.cer -storepass changeit
#keytool -import -trustcacerts -alias cas -keystore ./etc/thekeystore -file ./etc/cas.cer -storepass changeit