@echo off

REM set JAVA_VM=C:\jdk1.3\bin\java
REM set JAVA_VM=java



set RDF_FILE=testdata/rdfNice/flat.rdf



set P1=http://dfki.frodo.wwf/task#rootTask
set V1=-10000

set P2=http://dfki.frodo.wwf/task#parentTask
set V2=-1000

set P4=http://dfki.frodo.wwf/task#object
set V4=-1

set P5=http://dfki.frodo.wwf/task#objects
set V5=-1

set P3=http://dfki.frodo.wwf/task#subTask
set V3=100

set P5=http://dfki.frodo.wwf/task#name
set V5=100000



set RDF2JAVA_PATH=.

set IMPORT_PATH=%RDF2JAVA_PATH%\import
set LIB_PATH=%RDF2JAVA_PATH%\lib

set IMPORTS=%LIB_PATH%\rdf2java.jar
set IMPORTS=%IMPORTS%;%IMPORT_PATH%\DFKIUtils.jar
set IMPORTS=%IMPORTS%;%IMPORT_PATH%\rdf-api-2001-01-19.jar
set IMPORTS=%IMPORTS%;%IMPORT_PATH%\xercesImpl.jar
set IMPORTS=%IMPORTS%;%IMPORT_PATH%\xmlParserAPIs.jar

set FLAGS=-Dorg.w3c.dom.DOMImplementationSourceList=org.apache.xerces.dom.DOMImplementationSourceImpl

%JAVA_VM% -cp %IMPORTS% %FLAGS% dfki.rdf.util.RDFNice %RDF_FILE% %P1% %V1% %P2% %V2% %P3% %V3% %P4% %V4% %P5% %V5% %P6% %V6% %P7% %V7% %P8% %V8% %P9% %V9%
pause
