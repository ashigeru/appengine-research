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

import java.util.Random;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Transaction;

/**
 * Task Queueに1kのペイロードをキューイングするだけ。
 * @author ashigeru
 */
public class TxDummyEnqueue extends Repeat {

    private static final byte[] PAYLOAD;
    static {
        PAYLOAD = new byte[1024];
        Random random = new Random("ashigeru".hashCode());
        random.nextBytes(PAYLOAD);
    }

    private DatastoreService datastore;

    private Entity[] entities;

    private Entity[] dummies;

    @Override
    protected int before() {
        datastore = DatastoreServiceFactory.getDatastoreService();
        entities = new Entity[100];
        dummies = new Entity[entities.length];
        for (int i = 0; i < entities.length; i++) {
            entities[i] = new Entity("Tx", String.format("TxID:%02d", i));
            dummies[i] = new Entity("QDummy", "SINGLETON", entities[i].getKey());
            dummies[i].setProperty("payload", new Blob(PAYLOAD));
        }
        return entities.length;
    }

    @Override
    protected void each(int count) {
        Transaction tx = datastore.beginTransaction();
        try {
            datastore.put(tx, entities[count]);
            datastore.put(tx, dummies[count]);
            tx.commit();
        }
        finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
}
