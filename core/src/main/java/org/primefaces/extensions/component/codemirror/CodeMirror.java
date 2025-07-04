/*
 * Copyright (c) 2011-2025 PrimeFaces Extensions
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.primefaces.extensions.component.codemirror;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.el.MethodExpression;
import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.behavior.ClientBehaviorHolder;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.FacesEvent;

import org.primefaces.component.api.AbstractPrimeHtmlInputTextArea;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.CompleteEvent;
import org.primefaces.extensions.util.Constants;
import org.primefaces.util.LangUtils;

/**
 * Component class for the <code>CodeMirror</code> component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = Constants.LIBRARY, name = "primefaces-extensions.js")
@ResourceDependency(library = Constants.LIBRARY, name = "codemirror/codemirror.js")
@ResourceDependency(library = Constants.LIBRARY, name = "codemirror/codemirror.css")
public class CodeMirror extends AbstractPrimeHtmlInputTextArea implements ClientBehaviorHolder, Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.CodeMirror";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String EVENT_HIGHLIGHT_COMPLETE = "highlightComplete";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.CodeMirrorRenderer";

    private static final List<String> UNOBSTRUSIVE_EVENT_NAMES = LangUtils.unmodifiableList(EVENT_HIGHLIGHT_COMPLETE);
    private static final Collection<String> EVENT_NAMES = LangUtils.concat(AbstractPrimeHtmlInputTextArea.EVENT_NAMES, UNOBSTRUSIVE_EVENT_NAMES);

    private List<String> suggestions = null;

    /**
     * Properties that are tracked by state saving.
     *
     * @author Thomas Andraschko / last modified by $Author$
     * @version $Revision$
     */
    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {

        //@formatter:off
        widgetVar,
        theme,
        mode,
        indentUnit,
        smartIndent,
        tabSize,
        indentWithTabs,
        electricChars,
        keyMap,
        lineWrapping,
        lineNumbers,
        firstLineNumber,
        gutter,
        fixedGutter,
        readonly,
        matchBrackets,
        workTime,
        workDelay,
        pollInterval,
        undoDepth,
        tabindex,
        extraKeys,
        completeMethod,
        process,
        onstart,
        oncomplete,
        onerror,
        onsuccess,
        global,
        async,
        escape,
        escapeSuggestions
        //@formatter:on
    }

    public CodeMirror() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getTheme() {
        return (String) getStateHelper().eval(PropertyKeys.theme, null);
    }

    public void setTheme(final String theme) {
        getStateHelper().put(PropertyKeys.theme, theme);
    }

    public String getMode() {
        return (String) getStateHelper().eval(PropertyKeys.mode, null);
    }

    public void setMode(final String mode) {
        getStateHelper().put(PropertyKeys.mode, mode);
    }

    public String getKeyMap() {
        return (String) getStateHelper().eval(PropertyKeys.keyMap, null);
    }

    public void setKeyMap(final String keyMap) {
        getStateHelper().put(PropertyKeys.keyMap, keyMap);
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }

    public Integer getIndentUnit() {
        return (Integer) getStateHelper().eval(PropertyKeys.indentUnit, null);
    }

    public void setIndentUnit(final Integer indentUnit) {
        getStateHelper().put(PropertyKeys.indentUnit, indentUnit);
    }

    public Integer getTabSize() {
        return (Integer) getStateHelper().eval(PropertyKeys.tabSize, null);
    }

    public void setFirstLineNumber(final Integer firstLineNumber) {
        getStateHelper().put(PropertyKeys.firstLineNumber, firstLineNumber);
    }

    public Integer getFirstLineNumber() {
        return (Integer) getStateHelper().eval(PropertyKeys.firstLineNumber, null);
    }

    public void setTabSize(final Integer tabSize) {
        getStateHelper().put(PropertyKeys.tabSize, tabSize);
    }

    public Boolean isLineNumbers() {
        return (Boolean) getStateHelper().eval(PropertyKeys.lineNumbers, null);
    }

    public void setLineNumbers(final Boolean lineNumbers) {
        getStateHelper().put(PropertyKeys.lineNumbers, lineNumbers);
    }

    public Boolean isSmartIndent() {
        return (Boolean) getStateHelper().eval(PropertyKeys.smartIndent, null);
    }

    public void setSmartIndent(final Boolean smartIndent) {
        getStateHelper().put(PropertyKeys.smartIndent, smartIndent);
    }

    @Override
    public boolean isReadonly() {
        return (Boolean) getStateHelper().eval(PropertyKeys.readonly, false);
    }

    @Override
    public void setReadonly(final boolean readonly) {
        getStateHelper().put(PropertyKeys.readonly, readonly);
    }

    public Boolean isIndentWithTabs() {
        return (Boolean) getStateHelper().eval(PropertyKeys.indentWithTabs, null);
    }

    public void setIndentWithTabs(final Boolean indentWithTabs) {
        getStateHelper().put(PropertyKeys.indentWithTabs, indentWithTabs);
    }

    public Boolean isElectricChars() {
        return (Boolean) getStateHelper().eval(PropertyKeys.electricChars, null);
    }

    public void setElectricChars(final Boolean electricChars) {
        getStateHelper().put(PropertyKeys.electricChars, electricChars);
    }

    public Boolean isLineWrapping() {
        return (Boolean) getStateHelper().eval(PropertyKeys.lineWrapping, null);
    }

    public void setLineWrapping(final Boolean lineWrapping) {
        getStateHelper().put(PropertyKeys.lineWrapping, lineWrapping);
    }

    public Boolean isGutter() {
        return (Boolean) getStateHelper().eval(PropertyKeys.gutter, null);
    }

    public void setGutter(final Boolean gutter) {
        getStateHelper().put(PropertyKeys.gutter, gutter);
    }

    public Boolean isFixedGutter() {
        return (Boolean) getStateHelper().eval(PropertyKeys.fixedGutter, null);
    }

    public void setFixedGutter(final Boolean fixedGutter) {
        getStateHelper().put(PropertyKeys.fixedGutter, fixedGutter);
    }

    public Boolean isMatchBrackets() {
        return (Boolean) getStateHelper().eval(PropertyKeys.matchBrackets, null);
    }

    public void setMatchBrackets(final Boolean matchBrackets) {
        getStateHelper().put(PropertyKeys.matchBrackets, matchBrackets);
    }

    public Integer getWorkTime() {
        return (Integer) getStateHelper().eval(PropertyKeys.workTime, null);
    }

    public void setWorkTime(final Integer workTime) {
        getStateHelper().put(PropertyKeys.workTime, workTime);
    }

    public Integer getWorkDelay() {
        return (Integer) getStateHelper().eval(PropertyKeys.workDelay, null);
    }

    public void setWorkDelay(final Integer workDelay) {
        getStateHelper().put(PropertyKeys.workDelay, workDelay);
    }

    public Integer getPollInterval() {
        return (Integer) getStateHelper().eval(PropertyKeys.pollInterval, null);
    }

    public void setPollInterval(final Integer pollInterval) {
        getStateHelper().put(PropertyKeys.pollInterval, pollInterval);
    }

    public Integer getUndoDepth() {
        return (Integer) getStateHelper().eval(PropertyKeys.undoDepth, null);
    }

    public void setUndoDepth(final Integer undoDepth) {
        getStateHelper().put(PropertyKeys.undoDepth, undoDepth);
    }

    @Override
    public String getTabindex() {
        return (String) getStateHelper().eval(PropertyKeys.tabindex, null);
    }

    @Override
    public void setTabindex(final String tabindex) {
        getStateHelper().put(PropertyKeys.tabindex, tabindex);
    }

    public String getExtraKeys() {
        return (String) getStateHelper().eval(PropertyKeys.extraKeys, null);
    }

    public void setExtraKeys(final String extraKeys) {
        getStateHelper().put(PropertyKeys.extraKeys, extraKeys);
    }

    public MethodExpression getCompleteMethod() {
        return (MethodExpression) getStateHelper().eval(PropertyKeys.completeMethod, null);
    }

    public void setCompleteMethod(final MethodExpression completeMethod) {
        getStateHelper().put(PropertyKeys.completeMethod, completeMethod);
    }

    public String getProcess() {
        return (String) getStateHelper().eval(PropertyKeys.process, null);
    }

    public void setProcess(final String process) {
        getStateHelper().put(PropertyKeys.process, process);
    }

    public String getOnstart() {
        return (String) getStateHelper().eval(PropertyKeys.onstart, null);
    }

    public void setOnstart(final String onstart) {
        getStateHelper().put(PropertyKeys.onstart, onstart);
    }

    public String getOncomplete() {
        return (String) getStateHelper().eval(PropertyKeys.oncomplete, null);
    }

    public void setOncomplete(final String oncomplete) {
        getStateHelper().put(PropertyKeys.oncomplete, oncomplete);
    }

    public String getOnerror() {
        return (String) getStateHelper().eval(PropertyKeys.onerror, null);
    }

    public void setOnerror(final String onerror) {
        getStateHelper().put(PropertyKeys.onerror, onerror);
    }

    public String getOnsuccess() {
        return (String) getStateHelper().eval(PropertyKeys.onsuccess, null);
    }

    public void setOnsuccess(final String onsuccess) {
        getStateHelper().put(PropertyKeys.onsuccess, onsuccess);
    }

    public boolean isGlobal() {
        return (Boolean) getStateHelper().eval(PropertyKeys.global, true);
    }

    public void setGlobal(final boolean global) {
        getStateHelper().put(PropertyKeys.global, global);
    }

    public boolean isAsync() {
        return (Boolean) getStateHelper().eval(PropertyKeys.async, false);
    }

    public void setAsync(final boolean async) {
        getStateHelper().put(PropertyKeys.async, async);
    }

    public boolean isEscape() {
        return (Boolean) getStateHelper().eval(PropertyKeys.escape, true);
    }

    public void setEscape(final boolean escape) {
        getStateHelper().put(PropertyKeys.escape, escape);
    }

    public boolean isEscapeSuggestions() {
        return (Boolean) getStateHelper().eval(PropertyKeys.escapeSuggestions, true);
    }

    public void setEscapeSuggestions(final boolean suggestions) {
        getStateHelper().put(PropertyKeys.escapeSuggestions, suggestions);
    }

    @Override
    public void broadcast(final FacesEvent event) {
        super.broadcast(event);

        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final MethodExpression completeMethod = getCompleteMethod();

        if (completeMethod != null && event instanceof CompleteEvent) {
            suggestions = (List<String>) completeMethod.invoke(
                        facesContext.getELContext(),
                        new Object[] {event});

            if (suggestions == null) {
                suggestions = new ArrayList<>();
            }

            facesContext.renderResponse();
        }
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    @Override
    public Object saveState(FacesContext context) {
        // reset component for MyFaces view pooling
        suggestions = null;

        return super.saveState(context);
    }
}