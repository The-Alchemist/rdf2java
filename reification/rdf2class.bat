@echo off

REM set JAVA_VM=java
set JAVA_VM=%WWF_JAVA_HOME%\bin\java.exe

set RDFS_FILE=www.w3.org_1999_02_22-rdf-syntax-ns.rdfs
set OUTPUTDIR=..\src

set FLAGS=-is



set N1=http://www.w3.org/1999/02/22-rdf-syntax-ns#
set N2=http://www.w3.org/TR/1999/PR-rdf-schema-19990303#

set P1=dfki.rdf.util.reification.rdf
set P2=dfki.rdf.util.reification.rdfs



set IMPORT=..\import
set LIB=..\lib

set CLASSPATH=            %IMPORT%\DFKIUtils.jar
set CLASSPATH=%CLASSPATH%;%IMPORT%\rdf-api-2001-01-19.jar

set CLASSPATH=%CLASSPATH%;%LIB%\rdf2java.jar


%JAVA_VM% -cp %CLASSPATH% dfki.rdf.util.RDFS2Class %FLAGS% %RDFS_FILE% %OUTPUTDIR% %N1% %P1% %N2% %P2% %N3% %P3% %N4% %P4% %N5% %P5% %N6% %P6% %N7% %P7% %N8% %P8%  %N9% %P9%  %N10% %P10%
pause
