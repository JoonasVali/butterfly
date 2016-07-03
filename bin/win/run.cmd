@echo off
SET BUTTERFLY_HOME=..
java -jar -Djava.library.path=./%BUTTERFLY_HOME%/sl-libs/ -Xmx2048M ./%BUTTERFLY_HOME%/lib/butterfly.jar