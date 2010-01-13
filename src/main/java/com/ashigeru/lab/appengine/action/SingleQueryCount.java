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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

/**
 * 単一のエンティティに対するクエリ(count)。
 * @author ashigeru
 */
public class SingleQueryCount extends Repeat {

    private DatastoreService ds;

    private Query query;

    private Key key;

    @Override
    protected int before() {
        ds = DatastoreServiceFactory.getDatastoreService();
        key = KeyFactory.createKey("Single", "SINGLE");
        query = new Query("Single");
        ds.put(new Entity(key));
        return 100;
    }

    @Override
    protected void each(int count) {
        ds.prepare(query).countEntities();
    }
}
