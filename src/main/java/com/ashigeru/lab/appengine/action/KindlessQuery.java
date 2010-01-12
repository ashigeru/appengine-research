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

import java.util.LinkedList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

/**
 * Task Queueに1kのペイロードをキューイングするだけ。
 * @author ashigeru
 */
public class KindlessQuery extends Repeat {

    private DatastoreService ds;

    private Query query;

    @Override
    protected int before() {
        ds = DatastoreServiceFactory.getDatastoreService();
        LinkedList<Entity> entities = new LinkedList<Entity>();
        for (int i = 0; i < 20; i++) {
            entities.add(new Entity("Kindless", String.format("ID:%02d", i)));
        }
        ds.put(entities);
        Key from = entities.getFirst().getKey();
        Key to = entities.getLast().getKey();
        this.query = new Query()
            .addFilter(Entity.KEY_RESERVED_PROPERTY,
                FilterOperator.GREATER_THAN_OR_EQUAL, from)
            .addFilter(Entity.KEY_RESERVED_PROPERTY,
                FilterOperator.LESS_THAN_OR_EQUAL, to);
        return 20;
    }

    @Override
    protected void each(int count) {
        ds.prepare(query).asList(FetchOptions.Builder
            .withChunkSize(20)
            .prefetchSize(20));
    }
}
