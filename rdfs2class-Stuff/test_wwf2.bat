@echo off

REM *************************************************************
REM set the following to the path, where you installed RDFS2Class
set RDFS2CLASS_PATH=C:\java\rdfs2class
REM *************************************************************

set RDFS_FILE=E:\DATEN\wwf\behaviour\TaskRepAgentBehaviour_merged.rdfs
set OUTPUTDIR=E:\DATEN\wwf\behaviour\src

set FLAGS=-So

set N1=http://dfki.frodo.ap/behaviour#
set N2=http://dfki.frodo.wwf/agents/TaskRepAgentMsg#

set P1=dfki.frodo.ap.behaviour.rdftojava
set P2=dfki.frodo.wwf.agents.taskrepagentmsg.rdftojava

set IMPORT_PATH=%RDFS2CLASS_PATH%\import
set CP_RDFS2CLASS=%RDFS2CLASS_PATH%\classes
set CP_DFKIUtils=%IMPORT_PATH%\DFKIUtils-01.08.01.jar
set CP_RDF_API=%IMPORT_PATH%\rdf-api-2001-01-19.jar
java -cp %CP_RDFS2CLASS%;%CP_DFKIUtils%;%CP_RDF_API% dfki.rdf.util.RDFS2Class %FLAGS% %RDFS_FILE% %OUTPUTDIR% %N1% %P1% %N2% %P2% %N3% %P3% %N4% %P4% %N5% %P5% %N6% %P6%
pause
