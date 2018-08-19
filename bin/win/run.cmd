@echo off
SET ${name.uppercase}_HOME=..
java -jar -Xmx1024M -Dbutterfly.properties.path=./%${name.uppercase}_HOME%/butterfly-win.properties -Dlogback.configurationFile=logback-prod.xml ./%${name.uppercase}_HOME%/lib/${artifactId}.jar