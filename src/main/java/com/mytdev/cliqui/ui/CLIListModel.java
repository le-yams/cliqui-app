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
package com.mytdev.cliqui.ui;

import com.mytdev.cliqui.model.CLIEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.AbstractListModel;

/**
 *
 * @author Yann D'Isanto
 */
public final class CLIListModel extends AbstractListModel<CLIEntry> {

    private final List<CLIEntry> entries = new ArrayList<>();

    public void addEntry(CLIEntry entry) {
        final int index = entries.size();
        if (entries.add(entry)) {
            fireIntervalAdded(this, index, index);
        }
        Collections.sort(entries, new Comparator<CLIEntry>() {

            @Override
            public int compare(CLIEntry entry1, CLIEntry entry2) {
                return entry1.getName().compareTo(entry2.getName());
            }
        });
        fireContentsChanged(this, 0, index);
    }

    public void removeEntry(int index) {
        entries.remove(index);
        fireIntervalRemoved(this, index, index);
    }

    @Override
    public int getSize() {
        return entries.size();
    }

    @Override
    public CLIEntry getElementAt(int index) {
        return entries.get(index);
    }

}
