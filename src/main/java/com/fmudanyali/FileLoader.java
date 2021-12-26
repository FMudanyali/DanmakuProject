/*
 *   Copyright (c) 2021 Furkan Mudanyali

 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.

 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.

 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.fmudanyali;

import java.io.File;
import java.util.Objects;

/**
 * <h3>File Loader Class</h3>
 * 
 * A class that contains static methods for file path
 * operations for SDL2. Since SDL2 cannot access the
 * virtual filesystem inside the .jar package, SDL2 files
 * must be kept in a seperate folder along with the .jar.
 * 
 * @author Furkan Mudanyali
 * @version 1.0.0
 * @since 2021-12-01
 */
public class FileLoader {
    /**
     * Returns absolute path to the file from the assets folder.
     * 
     * @param fileName filename to get path of
     * @return path to the asset in the assets folder,
     * null on any kind of I/O error.
     */
    public static String getFilePath(String fileName){
        try{
            String protocol = FileLoader.class.getResource("").getProtocol();
            if(Objects.equals(protocol, "jar")){
                String jarPath = new File(FileLoader.class.getProtectionDomain().getCodeSource()
                .getLocation().toURI()).getParentFile().getPath();
                return jarPath + "/assets/" + fileName;
            }
            return System.getProperty("user.dir") + "/assets/" + fileName;
        } catch (Exception e){
            return null;
        }
    }
}