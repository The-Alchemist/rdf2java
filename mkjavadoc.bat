@echo off
cls

set JD_PROJECT_DIRECTORY=C:\java\rdf2java

set JD_PACKAGES=
set JD_PACKAGES=%JD_PACKAGES% dfki.rdf.util

set JD_TITLE="RDF2Java/RDFS2Class - Classes Documentation"
REM set JD_OVERVIEW=-overview %JD_PROJECT_DIRECTORY%\src\overview.html
set JD_MODE=-use -author -version

set JD_DOCPATH=%JD_PROJECT_DIRECTORY%\doc

set JD_SRCPATH=%JD_PROJECT_DIRECTORY%\src

set JD_CLSPATH=
set JD_CLSPATH=%JD_CLSPATH%;%JD_PROJECT_DIRECTORY%\classes
set JD_CLSPATH=%JD_CLSPATH%;C:\java\rdf-api-2001-01-19-Mirror\rdf-api-2001-01-19.jar
set JD_CLSPATH=%JD_CLSPATH%;C:\java\DFKIUtils-Mirror\classes

set JD_LINKS=
set JD_LINKS=%JD_LINKS% -link file:///D:\lang\jdk1.3\docs\api
set JD_LINKS=%JD_LINKS% -link file:///C:\java\rdf-api-2001-01-19-Mirror\doc
set JD_LINKS=%JD_LINKS% -link file:///C:\java\DFKIUtils-Mirror\doc\api

echo ------------------------------------------------------------------------
echo JD_PACKAGES = %JD_PACKAGES%
echo JD_TITLE    = %JD_TITLE%
echo JD_OVERVIEW = %JD_OVERVIEW%
echo JD_MODE     = %JD_MODE%
echo JD_DOCPATH  = %JD_DOCPATH%
echo JD_SRCPATH  = %JD_SRCPATH%
echo JD_CLSPATH  = %JD_CLSPATH%
echo JD_LINKS    = %JD_LINKS%
echo ------------------------------------------------------------------------

javadoc -sourcepath %JD_SRCPATH% -classpath %JD_CLSPATH% -d %JD_DOCPATH% -windowtitle %JD_TITLE% -doctitle %JD_TITLE% %JD_OVERVIEW% %JD_MODE% %JD_LINKS% %JD_PACKAGES%
pause