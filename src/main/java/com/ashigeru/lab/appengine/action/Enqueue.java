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

import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import com.google.appengine.api.labs.taskqueue.TaskOptions;

/**
 * Task Queueに1kのペイロードをキューイングするだけ。
 * @author ashigeru
 */
public class Enqueue extends Repeat {

    private static final byte[] PAYLOAD;
    static {
        PAYLOAD = new byte[1024];
        Random random = new Random("ashigeru".hashCode());
        random.nextBytes(PAYLOAD);
    }

    private Queue queue;

    @Override
    protected int before() {
        queue = QueueFactory.getDefaultQueue();
        return 100;
    }

    @Override
    protected void each(int count) {
        queue.add(TaskOptions.Builder
            .url("/void")
            .payload(PAYLOAD, "application/octet-stream"));
    }
}
