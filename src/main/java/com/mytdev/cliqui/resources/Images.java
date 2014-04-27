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
package com.mytdev.cliqui.resources;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 *
 * @author Yann D'Isanto
 */
public final class Images {

    public static final Image IMPORT = loadImage("application_form_add.png");
    
    public static final Image CLOSE_ENABLED = loadImage("tab_close_enabled.png");
    
    public static final Image CLOSE_PRESSED = loadImage("tab_close_pressed.png");
    
    public static final Image CLOSE_ROLLOVER = loadImage("tab_close_rollover.png");
    
    private static Image loadImage(String name) {
        final String path = imagePath(name);
        final URL url = Images.class.getClassLoader().getResource(path);
        try {
            return ImageIO.read(url);
        } catch (IOException ex) {
            throw new IllegalStateException("could not load resource image");
        }
    }
            
    private static String imagePath(String name) {
        return packagePath() + "/" + name;
    }
    
    private static String packagePath() {
        return Images.class.getPackage().getName().replace(".", "/");
    }
    
    private Images() {
    }

}
