package it.eup.loganalyser.runner;

import org.h2.tools.Server;

public class H2Server {

	public static void main(String[] args) throws Exception {
		final Server tcpServer = Server.createTcpServer("-baseDir", "./h2", "-trace", "-ifExists").start();

		Server.createWebServer("-webPort", "10500").start();

		Runtime.getRuntime().addShutdownHook(new Thread("H2 Shutdown Hook") {

			@Override
			public void run() {
				System.out.println("Shutdown H2 Server");
				tcpServer.stop();
			}
		});
	}
}
