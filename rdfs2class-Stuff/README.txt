 ======================================
|                                      |
|    RDFS2Class (RDFS--to-->Class)     |
|                                      |
|    Version:  1.4.2                   |
|    Author :  Sven.Schwarz@dfki.de    |
|                                      |
 ======================================

The tool generates Java Classes according to a given RDFS-file.

By now only umcompiled Java files for the classes are generated.
Hence you need to compile them later, before you can use Michael
Sintek's rdf2java tool. But as you often enhance the Java files
with additional functionality, this is no real problem...


REQUIREMENTS:
-------------
*  Java JRE / JDK (Java 2)
*  All libs needed are included in the directory "import".


INSTALLATION:
-------------
1) Unpack the zip-file to some directory. Note, that the zip-file already
   contains the directory "rdfs2class".

2) Edit RDFS2Class.bat: Set the right value for the environment variable
   "RDFS2CLASS_PATH".
   Unix/LinuX users: Maybe you write your own batch file. Of course you can
   do that much better than me... ;-)


USAGE:
------

*  You can use the batch file (RDFS2Class.bat). Start it without parameters
   to get information about the (command line) parameters.
   ATTENTION when using the batch file:
   By now only 9 parameters are possible, which means, that only up to
   three namespaces & packages can be given. If more parameters are needed,
   write another batch file. Sorry about that, but I'm not used to
   professional MSDOC batch programming ;-)


*  Or use the tool as an API: For example:

    import dfki.rdf.util.RDFS2Class;

    . . .

        String sRDFSFile     = "mydata.rdfs";
        String sOutputSrcDir = "C:\data\mydata\src";

        Map mapNamespace2Package = new HashMap();
        mapNamespace2Package.put("http://my.own.namespace/mydata#",
                                 "some.package.mydata");
        mapNamespace2Package.put("http://my.own.namespace/something/else#",
                                 "some.package.something.else");

        RDFS2Class gen = new RDFS2Class(sRDFSFile, sOutputSrcDir,
                                        mapNamespace2Package);
        gen.createClasses();


*  Online documentation (javadoc) can be found in the "doc" directory:
        doc/index.html


IMPORTANT NOTES:
----------------

*  The tool is named RDFS2Class. So, you CAN of course generate Java-Classes
   out of RDFS-files created with Protege (http://protege.stanford.edu/), in
   fact this gave the motivation to write this tool. But, keep in mind, that
   not all of the modelling possibilities Protege provides are used for the
   Class-file generation. For example, Protege allowes the overriding of
   properties, but RDFS2Class does not support this!



HISTORY:
--------

v1.4    Restructured implementation to omit redundant code

v1.3    Included additional putter methods for multiple-valued slots,
        like:  put___(Collection coll)

v1.2    In former versions I forgot to include the toString()-stuff of the
        super class.
        Additionally, I added rudimentary support for class valued slots,
        although I appreciate not to use them yet.

v1.1    Included the possibility to generate the Java files "incrementally".
        Now you can relaxedly insert additional slots and methods into the
        Java files. These changes will be kept while re-generating the Java
        files again due to changes of the RDFS file.
        However by now this Option has been included quite quickly, such that
        the user added text is NOT kept at the same place in the file!

v1.0    First working version

