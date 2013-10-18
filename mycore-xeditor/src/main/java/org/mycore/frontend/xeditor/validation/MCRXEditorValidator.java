/*
 * $Revision$ 
 * $Date$
 *
 * This file is part of ***  M y C o R e  ***
 * See http://www.mycore.de/ for details.
 *
 * This program is free software; you can use it, redistribute it
 * and / or modify it under the terms of the GNU General Public License
 * (GPL) as published by the Free Software Foundation; either version 2
 * of the License or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program, in a file called gpl.txt or license.txt.
 * If not, write to the Free Software Foundation Inc.,
 * 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 */

package org.mycore.frontend.xeditor.validation;

import java.util.ArrayList;
import java.util.List;

import org.jaxen.JaxenException;
import org.jdom2.JDOMException;
import org.mycore.frontend.xeditor.MCRBinding;
import org.mycore.frontend.xeditor.MCREditorSession;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author Frank L\u00FCtzenkirchen
 */
public class MCRXEditorValidator {

    public static final String XED_VALIDATION_FAILED = "xed-validation-failed";

    public static final String XED_VALIDATION_MARKER = "xed-validation-marker";

    private List<MCRValidationRule> validationRules = new ArrayList<MCRValidationRule>();

    private MCRValidationResults results = new MCRValidationResults();

    private MCREditorSession session;

    public MCRXEditorValidator(MCREditorSession session) {
        this.session = session;
    }

    public void addRule(String baseXPath, Node ruleElement) {
        NamedNodeMap attributes = ruleElement.getAttributes();

        Node requiredAttribute = attributes.getNamedItem("required");
        if ((requiredAttribute != null) && "true".equals(requiredAttribute.getNodeValue()))
            validationRules.add(new MCRRequiredRule(baseXPath, ruleElement));

        if ((attributes.getLength() > 1) || (requiredAttribute == null))
            validationRules.add(new MCRComplexRule(baseXPath, ruleElement));
    }

    public void clearRules() {
        validationRules.clear();
    }

    public boolean isValid() throws JDOMException, JaxenException {
        MCRBinding root = session.getRootBinding();
        for (MCRValidationRule rule : validationRules)
            rule.validate(results, root);

        session.getVariables().put(XED_VALIDATION_FAILED, String.valueOf(!results.isValid()));
        return results.isValid();
    }

    public boolean hasError(MCRBinding binding) {
        return results.hasError(binding.getAbsoluteXPath());
    }

    public void setValidationMarker(MCRBinding binding) {
        String xPath = binding.getAbsoluteXPath();
        String marker = results.getValidationMarker(xPath);
        session.getVariables().put(XED_VALIDATION_MARKER, marker);
    }

    public void clearValidationResults() {
        results = new MCRValidationResults();
        session.getVariables().remove(XED_VALIDATION_FAILED);
    }
}