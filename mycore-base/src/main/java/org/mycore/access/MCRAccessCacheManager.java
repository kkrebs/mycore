/*
 * $Id$
 * $Revision: 5697 $ $Date: Oct 25, 2012 $
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

package org.mycore.access;

import org.mycore.common.MCRCache;
import org.mycore.common.MCRSession;
import org.mycore.common.MCRSessionMgr;
import org.mycore.common.events.MCRSessionEvent;
import org.mycore.common.events.MCRSessionListener;

/**
 * @author Thomas Scheffler (yagee)
 *
 */
class MCRAccessCacheManager implements MCRSessionListener {
    private static final int CAPACITY = 200;

    private static String key = MCRAccessCacheManager.class.getCanonicalName();

    ThreadLocal<MCRCache<MCRPermissionHandle, Boolean>> accessCache = new ThreadLocal<MCRCache<MCRPermissionHandle, Boolean>>();

    @Override
    @SuppressWarnings("unchecked")
    public void sessionEvent(MCRSessionEvent event) {
        MCRCache<MCRPermissionHandle, Boolean> cache;
        MCRSession session = event.getSession();
        switch (event.getType()) {
        case created:
            cache = createCache(session);
            session.put(key, cache);
            break;
        case activated:
            cache = (MCRCache<MCRPermissionHandle, Boolean>) session.get(key);
            if (cache == null) {
                cache = createCache(session);
                session.put(key, cache);
            }
            accessCache.set(cache);
            break;
        case passivated:
            accessCache.set(null);
            break;

        case destroyed:
            cache = (MCRCache<MCRPermissionHandle, Boolean>) session.get(key);
            if (cache != null) {
                cache.close();
            }
            break;
        default:
            break;
        }
    }

    private MCRCache<MCRPermissionHandle, Boolean> createCache(MCRSession session) {
        return new MCRCache<MCRPermissionHandle, Boolean>(CAPACITY, "Access rights in MCRSession " + session.getID());
    }

    public MCRAccessCacheManager() {
        //store cache of first user into his session: we missed his session create event
        MCRSession session = MCRSessionMgr.getCurrentSession();
        MCRCache<MCRPermissionHandle, Boolean> cache = createCache(session);
        session.put(key, cache);
        accessCache.set(cache);
        //init for current user done
        MCRSessionMgr.addSessionListener(this);
    }

    public Boolean isPermitted(String id, String permission) {
        MCRPermissionHandle handle = new MCRPermissionHandle(id, permission);
        return accessCache.get().getIfUpToDate(handle, MCRSessionMgr.getCurrentSession().getLoginTime());
    }

    public void cachePermission(String id, String permission, boolean permitted) {
        MCRPermissionHandle handle = new MCRPermissionHandle(id, permission);
        accessCache.get().put(handle, permitted);
    }

}