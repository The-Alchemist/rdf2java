SET JAR=lib\rdf2javaApidoc.jar
del %JAR%
%JAVA_HOME%\bin\jar cvf %JAR% -C apidoc .
pause