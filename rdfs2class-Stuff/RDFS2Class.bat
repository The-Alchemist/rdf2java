@echo off

REM *************************************************************
REM set the following to the path, where you installed RDFS2Class
set RDFS2CLASS_PATH=C:\java\rdf2java
REM *************************************************************

set IMPORT_PATH=%RDFS2CLASS_PATH%\import
set CP_RDFS2CLASS=%RDFS2CLASS_PATH%\classes
set CP_DFKIUtils=%IMPORT_PATH%\DFKIUtils-01.08.01.jar
set CP_RDF_API=%IMPORT_PATH%\rdf-api-2001-01-19.jar
java -cp %CP_RDFS2CLASS%;%CP_DFKIUtils%;%CP_RDF_API% dfki.rdf.util.RDFS2Class %1 %2 %3 %4 %5 %6 %7 %8 %9