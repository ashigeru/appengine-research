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

import java.util.Arrays;

/**
 * 繰り返しの計測。
 * @author ashigeru
 */
public abstract class Repeat extends Action {

    @Override
    protected void perform() throws Exception {
        int count = before();
        if (count <= 5) {
            count = 100;
        }
        long[] elapsed = new long[count];
        for (int i = 0; i < count; i++) {
            elapsed[i] = -System.currentTimeMillis();
            each(i);
            elapsed[i] += System.currentTimeMillis();
        }
        after();
        Arrays.sort(elapsed);
        output.println("cnt: " + count + "runs");
        output.println("max: " + elapsed[1] + "ms");
        output.println("mid: " + elapsed[count / 2] + "ms");
        output.println("min: " + elapsed[count - 2] + "ms");
    }

    /**
     * 開始処理。既定では100を返す。
     * @return 繰り返す回数
     */
    protected int before() {
        return 100;
    }

    /**
     * 繰り返す処理。
     * @param count 繰り返し回数(0..)
     */
    protected abstract void each(int count);

    /**
     * 終了処理。既定では何もしない。
     */
    protected void after() {
        return;
    }
}
