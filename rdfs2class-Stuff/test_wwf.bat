@echo off

REM *************************************************************
REM set the following to the path, where you installed RDFS2Class
set RDFS2CLASS_PATH=C:\java\rdfs2class
REM *************************************************************

set RDFS_FILE=%RDFS2CLASS_PATH%\examples\wwf.rdfs
set OUTPUTDIR=%RDFS2CLASS_PATH%\examples\src

set FLAGS=-s

set N1=http://dfki.frodo.wwf/wwf#
set N2=http://dfki.frodo.wwf/OrganisationalModel#
set N3=http://dfki.frodo.wwf/TaskConcept#
set N4=http://dfki.frodo.wwf/Task#
set N5=http://dfki.frodo.wwf/Audit#
set N6=http://dfki.frodo.wwf/Time#

set P1=dfki.frodo.wwf.rdfs2class.wwf
set P2=dfki.frodo.wwf.rdfs2class.organisationalmodel
set P3=dfki.frodo.wwf.rdfs2class.taskconcept
set P4=dfki.frodo.wwf.rdfs2class.task
set P5=dfki.frodo.wwf.rdfs2class.audit
set P6=dfki.frodo.wwf.rdfs2class.time

set IMPORT_PATH=%RDFS2CLASS_PATH%\import
set CP_RDFS2CLASS=%RDFS2CLASS_PATH%\classes
set CP_DFKIUtils=%IMPORT_PATH%\DFKIUtils-01.08.01.jar
set CP_RDF_API=%IMPORT_PATH%\rdf-api-2001-01-19.jar
java -cp %CP_RDFS2CLASS%;%CP_DFKIUtils%;%CP_RDF_API% dfki.rdf.util.RDFS2Class %FLAGS% %RDFS_FILE% %OUTPUTDIR% %N1% %P1% %N2% %P2% %N3% %P3% %N4% %P4% %N5% %P5% %N6% %P6%
pause
