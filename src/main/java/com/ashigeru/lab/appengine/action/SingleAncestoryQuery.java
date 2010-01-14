/*
 * Copyright 2010 @ashigeru.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ashigeru.lab.appengine.action;

import java.util.Iterator;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

/**
 * Fetches an empty entity using ancestory queries.
 * @author ashigeru
 */
public class SingleAncestoryQuery extends Repeat {

    private DatastoreService ds;

    private Query query;

    private Key key;

    @Override
    protected int before() {
        ds = DatastoreServiceFactory.getDatastoreService();
        key = KeyFactory.createKey("Ancestor", "SINGLE");

        // :key
        // :key/Child(SINGLE)
        ds.put(new Entity(key));
        ds.put(new Entity(KeyFactory.createKey(key, "Child", "SINGLE")));

        // SELECT * FROM entities e WHERE ANCESTOR IS :key
        // SELECT * FROM entities e WHERE e.key.startsWith(:key)
        query = new Query(key);
        return 100;
    }

    @Override
    protected void each(int count) {
        PreparedQuery pq = ds.prepare(query);
        Iterator<Entity> iter = pq.asIterator(FetchOptions.Builder.withLimit(1));
        if (iter.hasNext()) {
            // assume ORDER BY __key__ ASC
            if (iter.next().getKey().equals(key) == false) {
                throw new AssertionError();
            }
            // limit(1)
            if (iter.hasNext()) {
                throw new AssertionError();
            }
        }
        else {
            throw new AssertionError();
        }
    }
}
