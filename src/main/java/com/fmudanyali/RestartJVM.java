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

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

/**
 * <h3>RestartJVM Class</h3>
 * 
 * Has a single function that restarts JVM on macOS
 * if JVM is not launched with -XstartOnFirstThread arg
 * and launches it with it.
 * 
 * <p>
 * 
 * Credits to kappa from jvm-gaming.org
 * <p>
 * https://jvm-gaming.org/t/starting-jvm-on-mac-with-xstartonfirstthread-programmatically/57547
 * 
 * @author kappa
 * @version 1.0.0
 * @since 2016-08-26
 */
public class RestartJVM {
	public static boolean restartJVM() {
		
		String osName = System.getProperty("os.name");
		
		// if not a mac return false
		if (!osName.startsWith("Mac") && !osName.startsWith("Darwin")) {
			return false;
		}
		
		// get current jvm process pid
		String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
		// get environment variable on whether XstartOnFirstThread is enabled
		String env = System.getenv("JAVA_STARTED_ON_FIRST_THREAD_" + pid);
		
		// if environment variable is "1" then XstartOnFirstThread is enabled
		if (env != null && env.equals("1")) {
			return false;
		}
		
		// restart jvm with -XstartOnFirstThread
		String separator = System.getProperty("file.separator");
		String classpath = System.getProperty("java.class.path");
		String mainClass = System.getenv("JAVA_MAIN_CLASS_" + pid);
		String jvmPath = System.getProperty("java.home") + separator + "bin" + separator + "java";
		
		List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
		
		ArrayList<String> jvmArgs = new ArrayList<String>();
		
		jvmArgs.add(jvmPath);
		jvmArgs.add("-XstartOnFirstThread");
		jvmArgs.addAll(inputArguments);
		jvmArgs.add("-cp");
		jvmArgs.add(classpath);
		jvmArgs.add(mainClass);
		
		// if you don't need console output, just enable these two lines 
		// and delete bits after it. This JVM will then terminate.
		//ProcessBuilder processBuilder = new ProcessBuilder(jvmArgs);
		//processBuilder.start();
		
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(jvmArgs);
			processBuilder.redirectErrorStream(true);
			Process process = processBuilder.start();
			
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
}
