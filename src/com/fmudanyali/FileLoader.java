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
 * @param fileName filename to get path of
 * @return path to the asset in the assets folder.
 */
public class FileLoader {
    public static String getFilePath(String fileName) throws Exception {
        String protocol = FileLoader.class.getResource("").getProtocol();
        if(Objects.equals(protocol, "jar")){
            String jarPath = new File(FileLoader.class.getProtectionDomain().getCodeSource()
            .getLocation().toURI()).getParentFile().getPath();
            return jarPath + "/assets/" + fileName;
        }
        return System.getProperty("user.dir") + "/assets/" + fileName;
    }
}