@echo off


set JAVA_VM=java


set RDF_FILE=%1


set P1=http://org.dfki/rdf2java/example1#hasParent
set V1=-1000

set P1=http://org.dfki/rdf2java/example1#hasChild
set V1=1000

set P2=http://org.dfki/rdf2java/example1#name
set V2=1


set RDF2JAVA_PATH=E:\java\rdf2java

set IMPORT_PATH=%RDF2JAVA_PATH%\import
set LIB_PATH=%RDF2JAVA_PATH%\lib

set IMPORTS=%LIB_PATH%\rdf2java.jar
set IMPORTS=%IMPORTS%;%IMPORT_PATH%\DFKIUtils.jar
set IMPORTS=%IMPORTS%;%IMPORT_PATH%\rdf-api-2001-01-19.jar
set IMPORTS=%IMPORTS%;%IMPORT_PATH%\xercesImpl.jar
set IMPORTS=%IMPORTS%;%IMPORT_PATH%\xmlParserAPIs.jar

REM set FLAGS=-Dorg.w3c.dom.DOMImplementationSourceList=org.apache.xerces.dom.DOMImplementationSourceImpl

%JAVA_VM% -cp %IMPORTS% %FLAGS% de.dfki.rdf.util.RDFNice %RDF_FILE% %P1% %V1% %P2% %V2% %P3% %V3% %P4% %V4% %P5% %V5% %P6% %V6% %P7% %V7% %P8% %V8% %P9% %V9%
REM pause

