@echo off

set JAVA_VM=%JAVA_HOME%\bin\java

REM *************************************************************
REM set the following to the path, where you installed RDFS2Class
set RDF2JAVA_PATH=..\..
REM *************************************************************

set RDFS_FILE=deepCopy.rdfs
set OUTPUTDIR=%RDF2JAVA_PATH%\test

set FLAGS=-is

set N1=http://de.dfki.rdf.test/rdf2java/deepCopyExample#
set P1=de.dfki.rdf.test.deepCopy


set IMPORT_PATH=%RDF2JAVA_PATH%\import
set CP_DFKIUtils=%IMPORT_PATH%\DFKIUtils.jar
set CP_RDF_API=%IMPORT_PATH%\rdf-api-2001-01-19.jar
set CP_XERCES=%IMPORT_PATH%\xercesImpl.jar;%IMPORT_PATH%\xmlParserAPIs.jar

set CP_RDFS2CLASS=%RDF2JAVA_PATH%\lib\rdf2java.jar


%JAVA_VM% -cp %CP_RDFS2CLASS%;%CP_DFKIUtils%;%CP_RDF_API%;%CP_XERCES% de.dfki.rdf.util.RDFS2Class %FLAGS% %RDFS_FILE% %OUTPUTDIR% %N1% %P1% %N2% %P2% %N3% %P3% %N4% %P4% %N5% %P5% %N6% %P6%
pause
