/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javax.servlet.jsp.tagext;

/**
 * Information on the scripting variables that are created/modified by a tag (at
 * run-time). This information is provided by TagExtraInfo classes and it is
 * used by the translation phase of JSP.
 * <p>
 * Scripting variables generated by a custom action have an associated scope of
 * either AT_BEGIN, NESTED, or AT_END.
 * <p>
 * The class name (VariableInfo.getClassName) in the returned objects is used to
 * determine the types of the scripting variables. Note that because scripting
 * variables are assigned their values from scoped attributes which cannot be of
 * primitive types, &quot;boxed&quot; types such as
 * <code>java.lang.Integer</code> must be used instead of primitives.
 * <p>
 * The class name may be a Fully Qualified Class Name, or a short class name.
 * <p>
 * If a Fully Qualified Class Name is provided, it should refer to a class that
 * should be in the CLASSPATH for the Web Application (see Servlet 2.4
 * specification - essentially it is WEB-INF/lib and WEB-INF/classes). Failure
 * to be so will lead to a translation-time error.
 * <p>
 * If a short class name is given in the VariableInfo objects, then the class
 * name must be that of a public class in the context of the import directives
 * of the page where the custom action appears. The class must also be in the
 * CLASSPATH for the Web Application (see Servlet 2.4 specification -
 * essentially it is WEB-INF/lib and WEB-INF/classes). Failure to be so will
 * lead to a translation-time error.
 * <p>
 * <B>Usage Comments</B>
 * <p>
 * Frequently a fully qualified class name will refer to a class that is known
 * to the tag library and thus, delivered in the same JAR file as the tag
 * handlers. In most other remaining cases it will refer to a class that is in
 * the platform on which the JSP processor is built (like J2EE). Using fully
 * qualified class names in this manner makes the usage relatively resistant to
 * configuration errors.
 * <p>
 * A short name is usually generated by the tag library based on some attributes
 * passed through from the custom action user (the author), and it is thus less
 * robust: for instance a missing import directive in the referring JSP page
 * will lead to an invalid short name class and a translation error.
 * <p>
 * <B>Synchronization Protocol</B>
 * <p>
 * The result of the invocation on getVariableInfo is an array of VariableInfo
 * objects. Each such object describes a scripting variable by providing its
 * name, its type, whether the variable is new or not, and what its scope is.
 * Scope is best described through a picture:
 * <p>
 * <IMG src="doc-files/VariableInfo-1.gif"
 * alt="NESTED, AT_BEGIN and AT_END Variable Scopes"/>
 * <p>
 * The JSP 2.0 specification defines the interpretation of 3 values:
 * <ul>
 * <li>NESTED, if the scripting variable is available between the start tag and
 * the end tag of the action that defines it.
 * <li>AT_BEGIN, if the scripting variable is available from the start tag of
 * the action that defines it until the end of the scope.
 * <li>AT_END, if the scripting variable is available after the end tag of the
 * action that defines it until the end of the scope.
 * </ul>
 * The scope value for a variable implies what methods may affect its value and
 * thus where synchronization is needed as illustrated by the table below.
 * <b>Note:</b> the synchronization of the variable(s) will occur <em>after</em>
 * the respective method has been called. <blockquote>
 * <table cellpadding="2" cellspacing="2" border="0" width="55%" * bgcolor="#999999" summary="Variable Synchronization Points">
 * <tbody>
 * <tr align="center">
 * <td valign="top" colspan="6" bgcolor="#999999"><u><b>Variable Synchronization
 * Points</b></u><br>
 * </td>
 * </tr>
 * <tr>
 * <th valign="top" bgcolor="#c0c0c0">&nbsp;</th>
 * <th valign="top" bgcolor="#c0c0c0" align="center">doStartTag()</th>
 * <th valign="top" bgcolor="#c0c0c0" align="center">doInitBody()</th>
 * <th valign="top" bgcolor="#c0c0c0" align="center">doAfterBody()</th>
 * <th valign="top" bgcolor="#c0c0c0" align="center">doEndTag()</th>
 * <th valign="top" bgcolor="#c0c0c0" align="center">doTag()</th>
 * </tr>
 * <tr>
 * <td valign="top" bgcolor="#c0c0c0"><b>Tag<br>
 * </b></td>
 * <td valign="top" align="center" bgcolor="#ffffff">AT_BEGIN, NESTED<br>
 * </td>
 * <td valign="top" align="center" bgcolor="#ffffff"><br>
 * </td>
 * <td valign="top" align="center" bgcolor="#ffffff"><br>
 * </td>
 * <td valign="top" align="center" bgcolor="#ffffff">AT_BEGIN, AT_END<br>
 * </td>
 * <td valign="top" align="center" bgcolor="#ffffff"><br>
 * </td>
 * </tr>
 * <tr>
 * <td valign="top" bgcolor="#c0c0c0"><b>IterationTag<br>
 * </b></td>
 * <td valign="top" align="center" bgcolor="#ffffff">AT_BEGIN, NESTED<br>
 * </td>
 * <td valign="top" align="center" bgcolor="#ffffff"><br>
 * </td>
 * <td valign="top" align="center" bgcolor="#ffffff">AT_BEGIN, NESTED<br>
 * </td>
 * <td valign="top" align="center" bgcolor="#ffffff">AT_BEGIN, AT_END<br>
 * </td>
 * <td valign="top" align="center" bgcolor="#ffffff"><br>
 * </td>
 * </tr>
 * <tr>
 * <td valign="top" bgcolor="#c0c0c0"><b>BodyTag<br>
 * </b></td>
 * <td valign="top" align="center" bgcolor="#ffffff">AT_BEGIN,
 * NESTED<sup>1</sup><br>
 * </td>
 * <td valign="top" align="center" bgcolor="#ffffff">AT_BEGIN,
 * NESTED<sup>1</sup><br>
 * </td>
 * <td valign="top" align="center" bgcolor="#ffffff">AT_BEGIN, NESTED<br>
 * </td>
 * <td valign="top" align="center" bgcolor="#ffffff">AT_BEGIN, AT_END<br>
 * </td>
 * <td valign="top" align="center" bgcolor="#ffffff"><br>
 * </td>
 * </tr>
 * <tr>
 * <td valign="top" bgcolor="#c0c0c0"><b>SimpleTag<br>
 * </b></td>
 * <td valign="top" align="center" bgcolor="#ffffff"><br>
 * </td>
 * <td valign="top" align="center" bgcolor="#ffffff"><br>
 * </td>
 * <td valign="top" align="center" bgcolor="#ffffff"><br>
 * </td>
 * <td valign="top" align="center" bgcolor="#ffffff"><br>
 * </td>
 * <td valign="top" align="center" bgcolor="#ffffff">AT_BEGIN, AT_END<br>
 * </td>
 * </tr>
 * </tbody>
 * </table>
 * <sup>1</sup> Called after <code>doStartTag()</code> if
 * <code>EVAL_BODY_INCLUDE</code> is returned, or after
 * <code>doInitBody()</code> otherwise. </blockquote>
 * <p>
 * <B>Variable Information in the TLD</B>
 * <p>
 * Scripting variable information can also be encoded directly for most cases
 * into the Tag Library Descriptor using the &lt;variable&gt; subelement of the
 * &lt;tag&gt; element. See the JSP specification.
 */
public class VariableInfo {

    /**
     * Scope information that scripting variable is visible only within the
     * start/end tags.
     */
    public static final int NESTED = 0;

    /**
     * Scope information that scripting variable is visible after start tag.
     */
    public static final int AT_BEGIN = 1;

    /**
     * Scope information that scripting variable is visible after end tag.
     */
    public static final int AT_END = 2;

    /**
     * Constructor These objects can be created (at translation time) by the
     * TagExtraInfo instances.
     * 
     * @param varName
     *            The name of the scripting variable
     * @param className
     *            The type of this variable
     * @param declare
     *            If true, it is a new variable (in some languages this will
     *            require a declaration)
     * @param scope
     *            Indication on the lexical scope of the variable
     */
    public VariableInfo(String varName, String className, boolean declare,
            int scope) {
        this.varName = varName;
        this.className = className;
        this.declare = declare;
        this.scope = scope;
    }

    // Accessor methods

    /**
     * Returns the name of the scripting variable.
     * 
     * @return the name of the scripting variable
     */
    public String getVarName() {
        return varName;
    }

    /**
     * Returns the type of this variable.
     * 
     * @return the type of this variable
     */
    public String getClassName() {
        return className;
    }

    /**
     * Returns whether this is a new variable. If so, in some languages this
     * will require a declaration.
     * 
     * @return whether this is a new variable.
     */
    public boolean getDeclare() {
        return declare;
    }

    /**
     * Returns the lexical scope of the variable.
     * 
     * @return the lexical scope of the variable, either AT_BEGIN, AT_END, or
     *         NESTED.
     * @see #AT_BEGIN
     * @see #AT_END
     * @see #NESTED
     */
    public int getScope() {
        return scope;
    }

    // == private data
    private final String varName;
    private final String className;
    private final boolean declare;
    private final int scope;
}
