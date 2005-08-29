/**
 * This file is part of ** M y C o R e **
 * Visit our homepage at http://www.mycore.de/ for details.
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
 * along with this program, normally in the file license.txt.
 * If not, write to the Free Software Foundation Inc.,
 * 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 **/

package org.mycore.access;

import java.util.Date;

import org.mycore.parsers.bool.MCRCondition;
import org.mycore.parsers.bool.MCRConditionVisitor;
import org.jdom.Element;

/**
 * Implementation of a (date &gt; xy) clause
 *
 * @author Matthias Kramm
 */

class MCRDateAfterClause implements MCRCondition {
    private Date date;

    MCRDateAfterClause(Date date) {
        this.date = date;
    }

    public boolean evaluate(Object o) {
        MCRAccessData data = (MCRAccessData)o;
        return data.date.after(this.date);
    }

    public String toString() {
        return "date > " + date + "\n";
    }

    public Element toXML() {return null; /* TODO */}
    public Element info() {return null; /* TODO */}
    public void accept(MCRConditionVisitor visitor) {/* TODO */}
};
