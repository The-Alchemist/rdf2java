@echo off


set JAVA_VM=java


set RDF2JAVA_PATH=E:\java\rdf2java

set IMPORT_PATH=%RDF2JAVA_PATH%\import
set LIB_PATH=%RDF2JAVA_PATH%\lib

set IMPORTS=%LIB_PATH%\rdf2java.jar
set IMPORTS=%IMPORTS%;%IMPORT_PATH%\DFKIUtils.jar
set IMPORTS=%IMPORTS%;%IMPORT_PATH%\rdf-api-2001-01-19.jar
set IMPORTS=%IMPORTS%;%IMPORT_PATH%\xercesImpl.jar
set IMPORTS=%IMPORTS%;%IMPORT_PATH%\xmlParserAPIs.jar

REM set FLAGS=-Dorg.w3c.dom.DOMImplementationSourceList=org.apache.xerces.dom.DOMImplementationSourceImpl

%JAVA_VM% -cp %IMPORTS% %FLAGS% de.dfki.rdf.util.RDFDump %1 %2 %3 %4 %5 %6 %7 %8 %9
pause
