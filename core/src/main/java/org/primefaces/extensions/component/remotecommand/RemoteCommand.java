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
package org.primefaces.extensions.component.remotecommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.el.MethodExpression;
import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.UICommand;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.event.ActionListener;
import jakarta.faces.event.FacesEvent;
import jakarta.faces.event.FacesListener;

import org.primefaces.component.api.AjaxSource;
import org.primefaces.extensions.component.base.AbstractParameter;
import org.primefaces.extensions.component.parameters.AssignableParameter;
import org.primefaces.extensions.component.parameters.MethodParameter;
import org.primefaces.extensions.util.Constants;

/**
 * Component class for the <code>RemoteCommand</code> component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = Constants.LIBRARY, name = "primefaces-extensions.js")
public class RemoteCommand extends UICommand implements AjaxSource {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.RemoteCommand";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.RemoteCommandRenderer";

    private List<AbstractParameter> allParameters = null;
    private List<AssignableParameter> assignableParameters = null;
    private List<MethodParameter> methodParameters = null;
    private Object[] convertedMethodParams = null;

    /**
     * Properties that are tracked by state saving.
     *
     * @author Thomas Andraschko / last modified by $Author$
     * @version $Revision$
     */
    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {

        //@formatter:off
        name,
        update,
        process,
        onstart,
        oncomplete,
        onerror,
        onsuccess,
        global,
        async,
        partialSubmit,
        action,
        autoRun,
        actionListener,
        resetValues,
        ignoreAutoUpdate,
        delay,
        timeout,
        partialSubmitFilter,
        form,
        ignoreComponentNotFound
        //@formatter:on
    }

    public RemoteCommand() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getName() {
        return (String) getStateHelper().eval(PropertyKeys.name, null);
    }

    public void setName(final String name) {
        getStateHelper().put(PropertyKeys.name, name);
    }

    @Override
    public String getUpdate() {
        return (String) getStateHelper().eval(PropertyKeys.update, null);
    }

    public void setUpdate(final String update) {
        getStateHelper().put(PropertyKeys.update, update);
    }

    @Override
    public String getProcess() {
        return (String) getStateHelper().eval(PropertyKeys.process, null);
    }

    public void setProcess(final String process) {
        getStateHelper().put(PropertyKeys.process, process);
    }

    @Override
    public String getOnstart() {
        return (String) getStateHelper().eval(PropertyKeys.onstart, null);
    }

    public void setOnstart(final String onstart) {
        getStateHelper().put(PropertyKeys.onstart, onstart);
    }

    @Override
    public String getOncomplete() {
        return (String) getStateHelper().eval(PropertyKeys.oncomplete, null);
    }

    public void setOncomplete(final String oncomplete) {
        getStateHelper().put(PropertyKeys.oncomplete, oncomplete);
    }

    @Override
    public String getOnerror() {
        return (String) getStateHelper().eval(PropertyKeys.onerror, null);
    }

    public void setOnerror(final String onerror) {
        getStateHelper().put(PropertyKeys.onerror, onerror);
    }

    @Override
    public String getOnsuccess() {
        return (String) getStateHelper().eval(PropertyKeys.onsuccess, null);
    }

    public void setOnsuccess(final String onsuccess) {
        getStateHelper().put(PropertyKeys.onsuccess, onsuccess);
    }

    @Override
    public boolean isGlobal() {
        return (Boolean) getStateHelper().eval(PropertyKeys.global, true);
    }

    public void setGlobal(final boolean global) {
        getStateHelper().put(PropertyKeys.global, global);
    }

    @Override
    public boolean isAsync() {
        return (Boolean) getStateHelper().eval(PropertyKeys.async, false);
    }

    public void setAsync(final boolean async) {
        getStateHelper().put(PropertyKeys.async, async);
    }

    @Override
    public boolean isPartialSubmit() {
        return (Boolean) getStateHelper().eval(PropertyKeys.partialSubmit, false);
    }

    public void setPartialSubmit(final boolean partialSubmit) {
        getStateHelper().put(PropertyKeys.partialSubmit, partialSubmit);
    }

    public boolean isAutoRun() {
        return (Boolean) getStateHelper().eval(PropertyKeys.autoRun, false);
    }

    public void setAutoRun(final boolean autoRun) {
        getStateHelper().put(PropertyKeys.autoRun, autoRun);
    }

    public MethodExpression getActionListenerMethodExpression() {
        return (MethodExpression) getStateHelper().get(PropertyKeys.actionListener);
    }

    public void setActionListenerMethodExpression(final MethodExpression actionListener) {
        getStateHelper().put(PropertyKeys.actionListener, actionListener);
    }

    @Override
    public boolean isPartialSubmitSet() {
        return getStateHelper().get(PropertyKeys.partialSubmit) != null || getValueExpression("partialSubmit") != null;
    }

    @Override
    public boolean isResetValues() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.resetValues, false);
    }

    public void setResetValues(final boolean resetValues) {
        getStateHelper().put(PropertyKeys.resetValues, resetValues);
    }

    @Override
    public boolean isIgnoreAutoUpdate() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.ignoreAutoUpdate, false);
    }

    public void setIgnoreAutoUpdate(final boolean ignoreAutoUpdate) {
        getStateHelper().put(PropertyKeys.ignoreAutoUpdate, ignoreAutoUpdate);
    }

    @Override
    public boolean isResetValuesSet() {
        return getStateHelper().get(PropertyKeys.resetValues) != null || getValueExpression("resetValues") != null;
    }

    @Override
    public java.lang.String getDelay() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.delay, null);
    }

    public void setDelay(final java.lang.String delay) {
        getStateHelper().put(PropertyKeys.delay, delay);
    }

    @Override
    public int getTimeout() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.timeout, 0);
    }

    public void setTimeout(final int timeout) {
        getStateHelper().put(PropertyKeys.timeout, timeout);
    }

    @Override
    public java.lang.String getPartialSubmitFilter() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.partialSubmitFilter, null);
    }

    public void setPartialSubmitFilter(final java.lang.String _partialSubmitFilter) {
        getStateHelper().put(PropertyKeys.partialSubmitFilter, _partialSubmitFilter);
    }

    @Override
    public java.lang.String getForm() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.form, null);
    }

    public void setForm(final java.lang.String _form) {
        getStateHelper().put(PropertyKeys.form, _form);
    }

    @Override
    public void broadcast(final FacesEvent event) {
        for (final FacesListener listener : getFacesListeners(FacesListener.class)) {
            if (event.isAppropriateListener(listener)) {
                event.processListener(listener);
            }
        }

        if (event instanceof ActionEvent) {
            final FacesContext context = getFacesContext();

            // invoke actionListener
            final MethodExpression listener = getActionListenerMethodExpression();
            if (listener != null) {
                listener.invoke(context.getELContext(), getConvertedMethodParameters(context));
            }

            // invoke action
            final ActionListener actionListener = context.getApplication().getActionListener();
            if (actionListener != null) {
                actionListener.processAction((ActionEvent) event);
            }
        }
    }

    protected void findChildParameters() {
        if (allParameters == null || assignableParameters == null || methodParameters == null) {
            allParameters = new ArrayList<>();
            assignableParameters = new ArrayList<>();
            methodParameters = new ArrayList<>();

            for (final UIComponent child : super.getChildren()) {
                if (child instanceof AbstractParameter) {
                    allParameters.add((AbstractParameter) child);

                    if (child instanceof AssignableParameter) {
                        assignableParameters.add((AssignableParameter) child);
                    }
                    else {
                        if (child instanceof MethodParameter) {
                            methodParameters.add((MethodParameter) child);
                        }
                    }
                }
            }
        }
    }

    protected List<AbstractParameter> getAllParameters() {
        findChildParameters();

        return allParameters;
    }

    protected List<AssignableParameter> getAssignableParameters() {
        findChildParameters();

        return assignableParameters;
    }

    protected List<MethodParameter> getMethodParameters() {
        findChildParameters();

        return methodParameters;
    }

    protected Object[] getConvertedMethodParameters(final FacesContext context) {
        if (convertedMethodParams == null) {
            convertedMethodParams = new Object[getMethodParameters().size()];

            for (int i = 0; i < getMethodParameters().size(); i++) {
                final MethodParameter methodParameter = getMethodParameters().get(i);

                final Converter converter = methodParameter.getConverter();
                final String parameterValue = getParameterValue(context, methodParameter.getName());

                if (converter == null) {
                    convertedMethodParams[i] = parameterValue;
                }
                else {
                    final Object convertedValue = converter.getAsObject(context, methodParameter, parameterValue);
                    convertedMethodParams[i] = convertedValue;
                }
            }
        }

        return convertedMethodParams;
    }

    public String getParameterValue(final FacesContext context, final String name) {
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String clientId = getClientId(context);

        return params.get(clientId + "_" + name);
    }

    @Override
    public boolean isAjaxified() {
        return true;
    }

    @Override
    public boolean isIgnoreComponentNotFound() {
        return (Boolean) getStateHelper().eval(PropertyKeys.ignoreComponentNotFound, false);
    }

    public void setIgnoreComponentNotFound(final boolean ignoreComponentNotFound) {
        getStateHelper().put(PropertyKeys.ignoreComponentNotFound, ignoreComponentNotFound);
    }

    @Override
    public Object saveState(FacesContext context) {
        // reset component for MyFaces view pooling
        allParameters = null;
        assignableParameters = null;
        methodParameters = null;
        convertedMethodParams = null;

        return super.saveState(context);
    }
}
