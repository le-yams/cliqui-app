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

package com.mytdev.cliqui.model;

import com.mytdev.cliqui.CLI;
import com.mytdev.cliqui.CLIBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Yann D'Isanto
 */
public final class CLIEntryRepository {
    
    private final Path repositoryFolder;

    private final Map<String, CLIEntry> cache = new HashMap<>();
    
    public CLIEntryRepository(Path repositoryFolder) {
        this.repositoryFolder = repositoryFolder;
    }
    
    public CLIEntry importFile(String name, File fileToImport) throws IOException, IllegalArgumentException {
        final CLIEntry entry = new CLIEntry(name, CLIBuilder.fromFile(fileToImport));
        final Path dest = repositoryFolder.resolve(name + ".cliqui");
        if(Files.exists(repositoryFolder) == false) {
            Files.createDirectories(repositoryFolder);
        }
        Files.copy(fileToImport.toPath(), dest);
        cache.put(name, entry);
        return entry;
    }
    
    public Set<String> getEntryNames() {
        return cache.keySet();
    }
    
    public Collection<CLIEntry> getEntries() {
        return cache.values();
    }
    
    public boolean removeEntry(String name) throws IOException {
        if(cache.containsKey(name) == false) {
            return false;
        }
        final Path path = repositoryFolder.resolve(name + ".cliqui");
        if(Files.deleteIfExists(path)) {
            cache.remove(name);
            return true;
        }
        return false;
    }
    
    /**
     * 
     * @param fileToValidate
     * @return
     * @throws IOException
     * @throws IllegalArgumentException if invalid cliqui format
     */
    public static CLI validateFile(File fileToValidate) throws IOException, IllegalArgumentException {
        return CLIBuilder.fromFile(fileToValidate);
    }
    
    public void load() throws IOException {
        cache.clear();
        if(Files.exists(repositoryFolder) == false) {
            return;
        }
        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(repositoryFolder, new DirectoryStream.Filter<Path>() {

            @Override
            public boolean accept(Path entry) throws IOException {
                return entry.getFileName().toString().endsWith(".cliqui");
            }
        });
        for (Path path : directoryStream) {
            final String name = path.getFileName().toString().replaceAll("\\.cliqui$", "");
            cache.put(name, new CLIEntry(name, CLIBuilder.fromFile(path.toFile())));
        }
    }
}
