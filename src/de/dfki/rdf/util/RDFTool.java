/**
 Gnowsis License 1.0

 Copyright (c) 2004, Leo Sauermann & DFKI German Research Center for Artificial Intelligence GmbH
 All rights reserved.

 This license is compatible with the BSD license http://www.opensource.org/licenses/bsd-license.php

 Redistribution and use in source and binary forms, 
 with or without modification, are permitted provided 
 that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, 
 this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, 
 this list of conditions and the following disclaimer in the documentation 
 and/or other materials provided with the distribution.
 * Neither the name of the DFKI nor the names of its contributors 
 may be used to endorse or promote products derived from this software 
 without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
 LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 endOfLic**/
/*
 * Created on 23.08.2003
 */

package de.dfki.rdf.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdql.Query;
import com.hp.hpl.jena.rdql.QueryEngine;
import com.hp.hpl.jena.rdql.QueryExecution;
import com.hp.hpl.jena.rdql.QueryResults;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Organisation: Gnowsis.com
 * </p>
 * 
 * @author Leo Sauermann (leo@gnowsis.com)
 * @version $Id$
 */
public class RDFTool
{

    /**
     * Internal method for reading configurations. Reads a property of a
     * resource
     * 
     * @param r resource
     * @param p property
     * @return null or a string representation of the property
     */
    public static String readValue( Resource r, Property p )
    {
        try
        {
            Statement s = r.getProperty( p );
            if( s == null ) return null;
            RDFNode o = s.getObject();
            if( o instanceof Literal ) 
                return ((Literal) o).getValue().toString();
            else
                return o.toString();
        }
        catch( Exception x )
        {
            return null;
        }
    }

    /**
     * Internal method for reading configurations. Reads a property of a
     * resource
     * 
     * @param r resource
     * @param p property
     * @return null or a string representation of the property
     */
    public static String readValue( Resource r, String p )
    {
        Model model = ModelFactory.createDefaultModel();
        Property pP = model.createProperty( p );
        try
        {
            RDFNode o = r.getProperty( pP ).getObject();
            if( o instanceof Literal ) 
                return ((Literal) o).getValue().toString();
            else
                return o.toString();
        }
        catch( Exception x )
        {
            return null;
        }
    }

    /**
     * Internal method for reading configurations. Reads a property of a
     * resource
     * 
     * @param r resource
     * @param p property
     * @return null or a string representation of the property
     */
    public static Resource readValueR( Resource r, Property p )
    {
        try
        {
            RDFNode o = r.getProperty( p ).getObject();
            if( o instanceof Resource ) 
                return (Resource) o;
            else
                return null;
        }
        catch( Exception x )
        {
            return null;
        }
    }

    /**
     * parse the query, execute it on the source and return the QueryResults.
     * 
     * @param queryString
     * @param source
     * @param target
     * @return the query results.
     */
    public static QueryResults runQuery( String queryString, Model source )
    {
        Query query = new Query( queryString );
        query.setSource( source );
        QueryExecution engine = new QueryEngine( query );
        QueryResults qres = engine.exec();
        return qres;
    }

    /**
     * inspired by com.hp.hpl.jena.graph.impl.FileGraph#guessLang with the
     * addition of toLowerCase
     * 
     * @param name
     * @return
     */
    public static String guessLang( String name )
    {
        String suffix = name.substring( name.lastIndexOf( '.' ) + 1 ).toLowerCase();
        if( suffix.equals( "n3" ) ) return "N3";
        if( suffix.equals( "nt" ) ) return "N-TRIPLE";
        return "RDF/XML";
    }

    /**
     * format the given date in a good dateTime format: ISO 8601, using the T
     * seperator and the - and : seperators accordingly. example:
     * 2003-01-22T17:00:00
     * 
     * @param date
     * @return a formatted string.
     */
    public static String dateTime2String( Date date )
    {
        if( date == null ) return null;
        return getDateTimeFormat().format( date );
    }

    private static DateFormat dateTimeFormat = null;

    public static DateFormat getDateTimeFormat()
    {
        if( dateTimeFormat == null )
        {
            dateTimeFormat = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );
        }
        return dateTimeFormat;
    }

    /**
     * format the given date in a good date format: ISO 8601, using only the
     * date and not the T seperator example: 2003-01-22
     * 
     * @param date
     * @return a formatted string.
     */
    public static String dateTime2DateString( Date date )
    {
        if( date == null ) return null;
        return getDateFormat().format( date );
    }

    /**
     * try to get a date out of a string. If this works, return it, otherwise
     * return null
     * 
     * @param isodate
     * @return
     */
    public static Date string2Date( String isodate )
    {
        if( isodate == null ) return null;
        try
        {
            return getDateTimeFormat().parse( isodate );
        }
        catch( ParseException e )
        {
            try
            {
                return getDateFormat().parse( isodate );
            }
            catch( ParseException e1 )
            {
                return null;
            }
        }

    }

    /**
     * format the given date in a good date format: ISO 8601, using only the
     * date and not the T seperator example: 2003-01-22
     * 
     * @param date
     * @return a formatted string.
     */
    /*
     * public static Date string2DateTime(String date) throws ParseException {
     * return getDateFormat().parse(date); }
     */

    private static DateFormat dateFormat = null;

    public static DateFormat getDateFormat()
    {
        if( dateFormat == null )
        {
            dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        }
        return dateFormat;
    }


    /**
     * Get the label of a rdfNode. If it is a resource, check for an rdfs:label.
     * If it is a Literal, return the Lexical Form.
     */
    public static String getLabel( RDFNode o )
    {
        if( o instanceof Resource )
        {
            // resource
            Statement rdfslabel = ((Resource) o).getProperty( RDFS.label );
            if( rdfslabel != null )
            {
                if( rdfslabel.getObject() instanceof Literal ) 
                    return ((Literal) rdfslabel.getObject()).getLexicalForm();
                else
                    return rdfslabel.getObject().toString();
            }
            else
                return o.toString();
        }
        else if( o instanceof Literal )                     // Literal
            return ((Literal) o).getLexicalForm();
        else                                                // else ? well, just string it
            return o.toString();
    }

    /**
     * Get the Displaylabel of a rdfNode. This method does more than getLabel,
     * if the resource does not have a "rdfs:label" property, it shortens the
     * url. Results may vary. If it is a resource, check for an rdfs:label. If
     * it is a Literal, return the Lexical Form.
     */
    public static String getGoodLabel( RDFNode o )
    {
        if( o instanceof Resource )
        {
            // resource
            Statement rdfslabel = ((Resource) o).getProperty( RDFS.label );
            if( rdfslabel != null )
            {
                if( rdfslabel.getObject() instanceof Literal ) 
                    return ((Literal) rdfslabel.getObject()).getLexicalForm();
                else
                    return rdfslabel.getObject().toString();
            }
            else
            {
                // resource, but no rdfs.label

                return getShortName( o.toString() );
            }
        }
        else if( o instanceof Literal )                     // Literal
            return ((Literal) o).getLexicalForm();
        else                                                // else ? well, just string it
            return o.toString();
    }

    /**
     * The passed uri identifies something on the web, probably a namespace. To
     * shorten this, parse the url for something like a localname. Returns the
     * last string after a '#' or a '/'.
     * 
     * @param uri a URI
     * @return a short name for it, for display.
     */
    public static String getShortName( String uri )
    {
        if( uri.indexOf( '#' ) > 0 ) 
            uri = uri.substring( uri.lastIndexOf( '#' ) + 1 );
        else if( uri.indexOf( '/' ) > 0 ) 
            uri = uri.substring( uri.lastIndexOf( '/' ) + 1 );
        return uri;
    }

    /**
     * set the property pred of the resource res. If it exists, change it. If it
     * not exists, create it.
     * 
     * @param res
     * @param pred
     * @param value
     */
    public static void setSingleValue( Resource res, Property pred, String value )
    {
        Statement st = res.getProperty( pred );
        if( st == null ) 
            res.addProperty( pred, value );
        else
            st.changeObject( value );
    }

    /**
     * set the property pred of the resource res. If it exists, change it. If it
     * not exists, create it.
     * 
     * @param res
     * @param pred
     * @param value
     */
    public static void setSingleValue( Resource res, Property pred,
            RDFNode value )
    {
        Statement st = res.getProperty( pred );
        if( st == null ) 
            res.addProperty( pred, value );
        else
            st.changeObject( value );
    }


    /**
     * convert a model to a string RDF/XML for serialization
     * 
     * @param model
     * @return
     */
    public static String modelToString( Model model )
    {
        return modelToString( model, "RDF/XML-ABBREV" );
    }

    /**
     * convenience function to create a memModel from an RDF/XML-ABBREV stream
     * 
     * @param rdfxml
     * @return
     */
    public static Model stringToModel( String rdfxml )
    {
        return stringToModel( rdfxml, "RDF/XML-ABBREV" );
    }

    /**
     * convert a model to a string RDF/XML for serialization
     * 
     * @param model
     * @return
     */
    public static String modelToString( Model model, String lang )
    {
        StringWriter buffer = new StringWriter();
        model.write( buffer, lang );
        return buffer.toString();
    }

    /**
     * convenience function to create a memModel from an RDF/XML-ABBREV stream
     * 
     * @param rdfxml
     * @return
     */
    public static Model stringToModel( String rdfxml, String lang )
    {
        Model m = ModelFactory.createDefaultModel();
        StringReader s = new StringReader( rdfxml );
        m.read( s, "", lang );
        s.close();
        return m;
    }


}

