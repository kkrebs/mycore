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

package org.mycore.services.fieldquery.data2fields;

import java.io.IOException;

import org.mycore.common.content.MCRContent;
import org.mycore.datamodel.ifs.MCRFile;

public class MCRData2FieldsFileAdditionalData extends MCRData2FieldsDelegating {

    public MCRData2FieldsFileAdditionalData(String index, final MCRFile file) throws IOException {
        MCRFieldsSelector selector = new MCRFieldsSelectorFile(index, file, "fileAdditionalData");
        MCRContent content = file.getAllAdditionalData();
        slaves.add(new MCRData2FieldsXML(content, selector));
    }

}