@echo off

REM set JAVA_VM=C:\TOOLS\JBuilder5\jdk1.3\bin\java
set JAVA_VM=java

REM *************************************************************
REM set the following to the path, where you installed RDFS2Class
set RDFS2CLASS_PATH=C:\java\rdf2java
REM *************************************************************

set RDFS_FILE=C:\java\rdf2java\testdata\nontransitive\nontransitive.rdfs
set OUTPUTDIR=C:\java\rdf2java\src

set FLAGS=-is

set N1=http://dfki.rdf.test/nontransitive#
set P1=dfki.rdf.test.nontransitive

set IMPORT_PATH=%RDFS2CLASS_PATH%\import
set CP_RDFS2CLASS=%RDFS2CLASS_PATH%\classes
set CP_DFKIUtils=%IMPORT_PATH%\DFKIUtils-01.08.01.jar
set CP_RDF_API=%IMPORT_PATH%\rdf-api-2001-01-19.jar
%JAVA_VM% -cp %CP_RDFS2CLASS%;%CP_DFKIUtils%;%CP_RDF_API% dfki.rdf.util.RDFS2Class %FLAGS% %RDFS_FILE% %OUTPUTDIR% %N1% %P1% %N2% %P2% %N3% %P3% %N4% %P4% %N5% %P5% %N6% %P6%
pause
