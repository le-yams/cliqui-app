/*
 * Copyright 2014 Yann D'Isanto.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mytdev.cliqui.util;

import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Yann D'Isanto
 */
@Slf4j
@AllArgsConstructor
public final class StreamPipe {

    private static final int DEFAULT_BUFFER_SIZE = 4096;

    private final InputStream input;

    private final OutputStream output;

    private final int bufferSize;

    public StreamPipe(InputStream input, OutputStream output) {
        this(input, output, DEFAULT_BUFFER_SIZE);
    }

    public void start() {
        final Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                final byte[] buffer = new byte[bufferSize];
                int nbRead;
                try {
                    final BufferedOutputStream bufferedOutput = new BufferedOutputStream(output, bufferSize);
                    while ((nbRead = input.read(buffer)) > 0) {
                            bufferedOutput.write(buffer, 0, nbRead);
                            bufferedOutput.flush();
                    }
                } catch (EOFException ex) {
                    log.debug("end of input stream");
                } catch (IOException ex) {
                    log.error(ex.getLocalizedMessage(), ex);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
