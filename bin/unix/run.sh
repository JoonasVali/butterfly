#!/bin/bash
export ${name.uppercase}_HOME=..
java -jar -Djava.library.path=./$${name.uppercase}_HOME/sl-libs/ -Xmx2048M -Dbutterfly.properties.path=../butterfly.properties ./$${name.uppercase}_HOME/lib/${artifactId}.jar