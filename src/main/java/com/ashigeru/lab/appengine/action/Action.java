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

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 動作確認用のアクションの基底。
 * @author ashigeru
 */
public abstract class Action implements Runnable {

    private final StringWriter buffer;

    /**
     * アクションの結果を出力する先を表す。
     */
    public final PrintWriter output;

    /**
     * インスタンスを生成する。
     */
    public Action() {
        buffer = new StringWriter();
        output = new PrintWriter(buffer);
    }

    /**
     * アクションの結果を指定の出力先に出力する。
     * @param out 出力先
     */
    public void flushTo(PrintWriter out) {
        output.flush();
        out.print(buffer.toString());
    }

    @Override
    public void run() {
        output.printf("#Starting: %s%n", getClass().getSimpleName());
        long start = System.currentTimeMillis();
        try {
            perform();
        }
        catch (Exception e) {
            e.printStackTrace(output);
        }
        finally {
            long end = System.currentTimeMillis();
            output.printf("%n#Elapsed: %d ms%n", end - start);
        }
    }

    /**
     * 実際に実行されるアクション。
     * @throws Exception アクションの実行に失敗した場合
     */
    protected abstract void perform() throws Exception;
}
