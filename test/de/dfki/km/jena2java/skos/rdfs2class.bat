@echo off

set JAVA_VM=java

REM *************************************************************
REM set the following to the path, where you installed RDFS2Class
set RDF2JAVA_PATH=E:\java\rdf2java
REM *************************************************************

set RDFS_FILE=%RDF2JAVA_PATH%\test\de\dfki\km\jena2java\skos\skos_gnowsis.rdfs
set OUTPUTDIR=%RDF2JAVA_PATH%\test


set FLAGS=

set N1=http://www.w3.org/2004/02/skos/core#
set P1=de.dfki.km.jena2java.skos.vocabulary


set IMPORT=%RDF2JAVA_PATH%\import

set CP=.
set CP=%CP%;%IMPORT%/Jena2/antlr.jar
set CP=%CP%;%IMPORT%/Jena2/commons-logging.jar
set CP=%CP%;%IMPORT%/Jena2/concurrent.jar
set CP=%CP%;%IMPORT%/Jena2/icu4j.jar
set CP=%CP%;%IMPORT%/Jena2/jakarta-oro-2.0.5.jar
set CP=%CP%;%IMPORT%/Jena2/jena.jar
set CP=%CP%;%IMPORT%/Jena2/junit.jar
set CP=%CP%;%IMPORT%/Jena2/log4j-1.2.7.jar
set CP=%CP%;%IMPORT%/Jena2/rdf-api-2001-01-19.jar
set CP=%CP%;%IMPORT%/Jena2/xercesImpl.jar
set CP=%CP%;%IMPORT%/Jena2/xml-apis.jar

set CP=%CP%;%IMPORT%/DFKIUtils.jar
set CP=%CP%;%IMPORT%/xercesImpl.jar
set CP=%CP%;%IMPORT%/xml-apis.jar
set CP=%CP%;%IMPORT%/xmlParserAPIs.jar

set CP_RDFS2CLASS=%RDF2JAVA_PATH%\lib\rdf2java.jar

%JAVA_VM% -cp %CP% de.dfki.km.jena2java.RDFS2Class %FLAGS% %RDFS_FILE% %OUTPUTDIR% %N1% %P1% %N2% %P2% %N3% %P3% %N4% %P4% %N5% %P5% %N6% %P6%
pause
