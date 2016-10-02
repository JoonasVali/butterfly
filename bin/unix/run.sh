#!/bin/bash
export ${name.uppercase}_HOME=..
java -jar -Djava.library.path=./$${name.uppercase}_HOME/sl-libs/ -Xmx2048M -Dbutterfly.properties.path=./$${name.uppercase}_HOME/butterfly-unix.properties -Dlogback.configurationFile=logback-prod.xml ./$${name.uppercase}_HOME/lib/${artifactId}.jar