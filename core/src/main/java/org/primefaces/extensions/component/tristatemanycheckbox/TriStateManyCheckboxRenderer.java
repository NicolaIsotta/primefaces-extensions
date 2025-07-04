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
package org.primefaces.extensions.component.tristatemanycheckbox;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import jakarta.faces.FacesException;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.component.UINamingContainer;
import jakarta.faces.component.UISelectMany;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.convert.Converter;
import jakarta.faces.model.SelectItem;

import org.primefaces.component.selectmanycheckbox.SelectManyCheckbox;
import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.SelectManyRenderer;
import org.primefaces.util.Constants;
import org.primefaces.util.EscapeUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

/**
 * TriStateManyCheckboxRenderer
 *
 * @author Mauricio Fenoglio / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
public class TriStateManyCheckboxRenderer extends SelectManyRenderer {

    @Override
    public Object getConvertedValue(final FacesContext context, final UIComponent component, final Object submittedValue) {
        // only convert the values of the maps
        if (submittedValue instanceof Map) {
            final Map<String, Object> mapSub = (Map<String, Object>) submittedValue;
            final List<String> keyValues = new ArrayList<>(mapSub.keySet());
            final Map<String, Object> mapSubConv = new LinkedHashMap<>();
            final TriStateManyCheckbox checkbox = (TriStateManyCheckbox) component;
            final Converter<?> converter = checkbox.getConverter();
            if (converter != null) {
                for (final String keyVal : keyValues) {
                    final Object mapVal = converter.getAsObject(context, checkbox, (String) mapSub.get(keyVal));
                    mapSubConv.put(keyVal, mapVal);
                }

                return mapSubConv;
            }
            else {
                return mapSub;
            }
        }
        else {
            throw new FacesException("Value of '" + component.getClientId() + "'must be a Map instance");
        }
    }

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final TriStateManyCheckbox checkbox = (TriStateManyCheckbox) component;
        if (!shouldDecode(checkbox)) {
            return;
        }

        decodeBehaviors(context, checkbox);

        final String submitParam = getSubmitParam(context, checkbox);
        final Map<String, String[]> params = context.getExternalContext().getRequestParameterValuesMap();

        String[] valuesArray = null;
        if (params.containsKey(submitParam)) {
            valuesArray = params.get(submitParam);
        }

        checkbox.setSubmittedValue(getSubmitedMap(context, checkbox, valuesArray));
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final TriStateManyCheckbox checkbox = (TriStateManyCheckbox) component;
        encodeMarkup(context, checkbox);
        encodeScript(context, checkbox);
    }

    protected void encodeMarkup(final FacesContext context, final TriStateManyCheckbox checkbox) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = checkbox.getClientId(context);
        final String style = checkbox.getStyle();
        final String styleClass = getStyleClassBuilder(context)
                    .add(SelectManyCheckbox.STYLE_CLASS)
                    .add(checkbox.getStyleClass())
                    .build();

        writer.startElement("table", checkbox);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");
        if (style != null) {
            writer.writeAttribute(Attrs.STYLE, style, Attrs.STYLE);
        }

        encodeSelectItems(context, checkbox);

        writer.endElement("table");
    }

    protected void encodeSelectItems(final FacesContext context, final TriStateManyCheckbox checkbox) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final List<SelectItem> selectItems = getSelectItems(context, checkbox);
        final Converter<Object> converter = checkbox.getConverter();
        Map<String, Object> values = (Map<String, Object>) getValues(checkbox);
        final Map<String, Object> submittedMap = getSubmittedFromComp(checkbox);
        final String layout = checkbox.getLayout();
        final boolean pageDirection = layout != null && "pageDirection".equals(layout);

        if (submittedMap != null) {
            values = submittedMap;
        }

        if (converter != null && submittedMap == null) {
            for (final Entry<String, Object> entry : values.entrySet()) {
                final String keyValue = converter.getAsString(context, checkbox, entry.getValue());
                values.put(entry.getKey(), keyValue);
            }
        }

        int idx = -1;
        for (final SelectItem selectItem : selectItems) {
            idx++;
            if (pageDirection) {
                writer.startElement("tr", null);
            }

            encodeOption(context, checkbox, values, selectItem, idx);
            if (pageDirection) {
                writer.endElement("tr");
            }
        }
    }

    protected void encodeOption(final FacesContext context, final UIInput component, final Map<String, Object> values,
                final SelectItem option, final int idx) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final TriStateManyCheckbox checkbox = (TriStateManyCheckbox) component;
        final String itemValueAsString = String.valueOf(option.getValue());
        final String name = checkbox.getClientId(context);
        final String id = name + UINamingContainer.getSeparatorChar(context) + idx;
        final boolean disabled = option.isDisabled() || checkbox.isDisabled();

        final String itemValue = (String) option.getValue();

        final int valueInput = getValueForInput(component, itemValue, values);
        if (option.isNoSelectionOption() && values != null && Constants.EMPTY_STRING.equals(itemValue)) {
            return;
        }

        writer.startElement("td", null);

        writer.startElement("div", null);
        writer.writeAttribute(Attrs.CLASS, HTML.CHECKBOX_CLASS, null);

        encodeOptionInput(context, checkbox, id, name, disabled, itemValueAsString, valueInput);
        encodeOptionOutput(context, checkbox, valueInput, disabled);

        writer.endElement("div");
        writer.endElement("td");

        writer.startElement("td", null);
        encodeOptionLabel(context, id, option, disabled);
        writer.endElement("td");
    }

    protected void encodeOptionInput(final FacesContext context, final TriStateManyCheckbox checkbox, final String id, final String name,
                final boolean disabled, final String value, final int valueInput) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", null);
        writer.writeAttribute(Attrs.CLASS, HTML.CHECKBOX_INPUT_WRAPPER_CLASS, null);

        writer.startElement("input", null);
        writer.writeAttribute("id", id, null);
        writer.writeAttribute("name", name, null);
        writer.writeAttribute("type", "text", null);
        writer.writeAttribute("value", valueInput, null);
        writer.writeAttribute("itemValue", value, null);

        if (disabled) {
            writer.writeAttribute("disabled", "disabled", null);
        }

        if (checkbox.getOnchange() != null) {
            writer.writeAttribute("onchange", checkbox.getOnchange(), null);
        }

        writer.endElement("input");

        writer.endElement("div");
    }

    protected void encodeOptionOutput(final FacesContext context, final TriStateManyCheckbox checkbox, final int valCheck,
                final boolean disabled) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String styleClass = getStyleClassBuilder(context)
                    .add(HTML.CHECKBOX_BOX_CLASS)
                    .add(valCheck == 1 || valCheck == 2, "ui-state-active")
                    .add(disabled, "ui-state-disabled")
                    .build();

        // if stateIcon is defined use it insted of default icons.
        final String stateOneIconClass = checkbox.getStateOneIcon() != null ? TriStateManyCheckbox.UI_ICON + checkbox.getStateOneIcon()
                    : Constants.EMPTY_STRING;
        final String stateTwoIconClass = checkbox.getStateTwoIcon() != null ? TriStateManyCheckbox.UI_ICON + checkbox.getStateTwoIcon()
                    : TriStateManyCheckbox.UI_ICON + "ui-icon-check";
        final String stataThreeIconClass = checkbox.getStateThreeIcon() != null ? TriStateManyCheckbox.UI_ICON + checkbox.getStateThreeIcon()
                    : TriStateManyCheckbox.UI_ICON + "ui-icon-closethick";

        final String comma = "\",\"";
        final String statesIconsClasses = "[\"" + stateOneIconClass + comma + stateTwoIconClass + comma + stataThreeIconClass + "\"]";

        final String statesTitles = "[\"" + EscapeUtils.forJavaScript(checkbox.getStateOneTitle()) + comma
                    + EscapeUtils.forJavaScript(checkbox.getStateTwoTitle()) + comma + EscapeUtils.forJavaScript(checkbox.getStateThreeTitle()) + "\"]";

        String iconClass = "ui-chkbox-icon ui-icon ui-c";
        String activeTitle = Constants.EMPTY_STRING;
        if (valCheck == 0) {
            iconClass = iconClass + " " + stateOneIconClass;
            activeTitle = checkbox.getStateOneTitle();
        }
        else if (valCheck == 1) {
            iconClass = iconClass + " " + stateTwoIconClass;
            activeTitle = checkbox.getStateTwoTitle();
        }
        else if (valCheck == 2) {
            iconClass = iconClass + " " + stataThreeIconClass;
            activeTitle = checkbox.getStateThreeTitle();
        }

        String dataTitles = Constants.EMPTY_STRING;
        String titleAtt = Constants.EMPTY_STRING;

        if (!checkbox.getStateOneTitle().isEmpty()
                    || !checkbox.getStateTwoTitle().isEmpty() || !checkbox.getStateThreeTitle().isEmpty()) {
            // preparation with singe quotes for .data('titlestates')
            dataTitles = "data-titlestates='" + statesTitles + "' ";
            // active title Att
            titleAtt = " title=\"" + EscapeUtils.forJavaScript(activeTitle) + "\" ";
        }

        String tabIndexTag = " tabIndex=0 ";
        if (checkbox.getTabindex() != null) {
            tabIndexTag = "tabIndex=" + checkbox.getTabindex() + " ";
        }

        // preparation with singe quotes for .data('iconstates')
        writer.write("<div " + tabIndexTag + titleAtt + "class=\"" + styleClass + "\" data-iconstates='" + statesIconsClasses + "' "
                    + dataTitles + ">"
                    + "<span class=\"" + iconClass + "\"></span></div>");

    }

    protected void encodeScript(final FacesContext context, final TriStateManyCheckbox checkbox) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtTriStateManyCheckbox", checkbox);
        encodeClientBehaviors(context, checkbox);
        wb.finish();
    }

    protected void encodeOptionLabel(final FacesContext context, final String containerClientId,
                final SelectItem option, final boolean disabled) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement(Attrs.LABEL, null);
        writer.writeAttribute("for", containerClientId, null);
        if (disabled) {
            writer.writeAttribute(Attrs.CLASS, "ui-state-disabled", null);
        }

        if (option.isEscape()) {
            writer.writeText(option.getLabel(), null);
        }
        else {
            writer.write(option.getLabel());
        }

        writer.endElement(Attrs.LABEL);
    }

    @Override
    protected String getSubmitParam(final FacesContext context, final UISelectMany selectMany) {
        return selectMany.getClientId(context);
    }

    /*
     * return the value for the triState based on the value of the selectItems on the iteration
     */
    protected int getValueForInput(final UIInput component, final String itemValue, final Map<String, Object> valueArray) {
        try {
            int retInt = 0;
            if (itemValue == null || valueArray == null) {
                return retInt;
            }

            if (valueArray.containsKey(itemValue)) {
                retInt = Integer.parseInt((String) valueArray.get(itemValue));

                return retInt % 3;
            }
            else {
                return retInt;
            }
        }
        catch (final NumberFormatException ex) {
            throw new FacesException("State of '" + component.getClientId() + "' must be an integer representation");
        }
    }

    @Override
    protected Object getValues(UISelectMany component) {
        final Object value = component.getValue();

        if (value == null) {
            return null;
        }
        else if (value instanceof Map) {
            return value;
        }
        else {
            throw new FacesException("Value of '" + component.getClientId() + "'must be a Map instance");
        }
    }

    protected Map<String, Object> getSubmitedMap(final FacesContext context, final TriStateManyCheckbox checkbox, final String[] valuesArray) {
        final List<SelectItem> selectItems = getSelectItems(context, checkbox);
        final Map<String, Object> subValues = new LinkedHashMap<>();
        if (valuesArray != null && valuesArray.length == selectItems.size()) {
            int idx = -1;
            for (final SelectItem item : selectItems) {
                idx++;

                final String keyMap = (String) item.getValue();
                final Object valueMap = valuesArray[idx];
                subValues.put(keyMap, valueMap);
            }

            return subValues;
        }
        else {
            return null;
        }
    }

    protected Map<String, Object> getSubmittedFromComp(final UIComponent component) {
        final TriStateManyCheckbox checkbox = (TriStateManyCheckbox) component;
        final Map<String, Object> ret = (Map<String, Object>) checkbox.getSubmittedValue();
        if (ret != null) {
            final Map<String, Object> subValues = new LinkedHashMap<>();

            // need to reverse the order of element on the map to take the value as on decode.
            final Set<String> keys = ret.keySet();
            final String[] tempArray = keys.toArray(new String[0]);

            final int length = tempArray.length;
            for (int i = length - 1; i >= 0; i--) {
                final String key = tempArray[i];
                final Object val = ret.get(key);
                subValues.put(key, val);
            }

            return subValues;
        }
        else {
            return null;
        }
    }
}